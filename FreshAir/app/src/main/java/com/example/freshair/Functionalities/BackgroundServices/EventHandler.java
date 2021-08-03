package com.example.freshair.Functionalities.BackgroundServices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.model.WorkProgress;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshair.Functionalities.Device.DeviceFragment;
import com.example.freshair.Functionalities.Favourites.FavouritesViewModel;
import com.example.freshair.Models.ModelsBreezoMeter.Data;
import com.example.freshair.Models.ModelsFavourites.Favourite;
import com.example.freshair.R;
import com.example.freshair.Repo.FavouritesRepo;
import com.example.freshair.Utils.ItemFavourite;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.maps.model.LatLng;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.Result;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class EventHandler extends Worker {

    public EventHandler(@NotNull Context context, @NotNull WorkerParameters workerParameters){
        super(context, workerParameters);
    }

    @NotNull
    @Override
    public Result doWork(){

        FavouritesRepo mRepo = FavouritesRepo.getInstance();

        mRepo.getForNotif().subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<List<ItemFavourite>>() {
                                @Override
                                public void onSuccess(@NotNull List<ItemFavourite> itemFavourites) {
                                    for(ItemFavourite item : itemFavourites) {
                                        if(item.getDescription().equals("Excellent air quality"))
                                            continue;
                                        else sendNotification(item);
                                    }
                                }

                                @Override
                                public void onError(@NotNull Throwable e) {
                                }
                            });


        checkForSms();

        return Result.success();
    }

    public void checkForSms(){
        RequestQueue requestQueue;
        ArrayList<Float> dataDeviceSmoke = new ArrayList<Float>();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.thingspeak.com/channels/1395236/fields/2.json?results=20";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray feeds = response.getJSONArray("feeds");
                for(int i = 0; i < feeds.length(); i++){
                    JSONObject feed = feeds.getJSONObject(i);

                    String smoke = feed.getString("field2");
                    smoke = smoke.replaceAll("r","");

                    Float smokeD = Float.valueOf(smoke);
                    //System.out.println(smokeD);
                    dataDeviceSmoke.add(smokeD);
                }
                Collections.reverse(dataDeviceSmoke);

                ArrayList<Integer> newDataDeviceSmoke = new ArrayList<Integer>();
                for(float v : dataDeviceSmoke){
                    if (v >= 350) {
                        sendSMS();
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
        });

        requestQueue.add(request);
    }

    public static void periodRequest(){
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(EventHandler.class, 16, TimeUnit.MINUTES)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .addTag("periodic")
                .build();

        WorkManager.getInstance().enqueueUniquePeriodicWork("periodic", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }

    private void sendNotification(ItemFavourite item) {
        Log.d("asd", "am intrat");
        String content = new String("");
        content += item.getDescription() + " at " + item.getAddress();

        Intent intent = new Intent(getApplicationContext(), DeviceFragment.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        createNotificationChannel(getApplicationContext());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "my_channel")
                .setContentText(content)
                .setContentTitle("Alerta!") // Only on api < 26, see createNotificationChannel otherwise
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Default sound, vibration etc
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_disabled)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(new Random().nextInt(1000000), builder.build());
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "my_channel",
                    "MyApp notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("They will wake you up in the night");
            channel.enableVibration(true);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(
                    NotificationManager.class
            );
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendSMS(){
        Log.d("asd", "am trimis sms");
        SmsManager smsManager = SmsManager.getDefault();
        String swissNumberStr = "0767521893";
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(swissNumberStr, "RO");
            smsManager.sendTextMessage("076-752-18-93", null, "Smoke sensed in the house", null, null);
            Log.d("asd", swissNumberProto.toString());
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
    }
}
