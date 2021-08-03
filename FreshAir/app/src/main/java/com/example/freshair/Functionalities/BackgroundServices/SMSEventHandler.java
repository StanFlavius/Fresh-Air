package com.example.freshair.Functionalities.BackgroundServices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.freshair.Functionalities.Device.DeviceFragment;
import com.example.freshair.Functionalities.Favourites.FavouritesViewModel;
import com.example.freshair.R;
import com.example.freshair.Repo.FavouritesRepo;
import com.example.freshair.Utils.ItemFavourite;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SMSEventHandler extends Worker {

    public SMSEventHandler(@NotNull Context context, @NotNull WorkerParameters workerParameters){
        super(context, workerParameters);
    }

    @NotNull
    @Override
    public Result doWork(){
        Log.d("asd", "am trimis sms");
        sendSMS();

        return Result.success();
    }

    public static void periodRequest(){
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(EventHandler.class, 16, TimeUnit.MINUTES)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .addTag("periodic2")
                .build();

        WorkManager.getInstance().enqueueUniquePeriodicWork("periodic2", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }

    private void sendSMS(){
        Log.d("asd", "am trimis sms");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("0773871342", null, "a", null, null);
    }
}
