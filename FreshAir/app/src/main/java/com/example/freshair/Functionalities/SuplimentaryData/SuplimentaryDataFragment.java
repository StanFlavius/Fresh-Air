package com.example.freshair.Functionalities.SuplimentaryData;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshair.Models.ModelsAuthentication.User;
import com.example.freshair.Models.ModelsAuthentication.UserDevice;
import com.example.freshair.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuplimentaryDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuplimentaryDataFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText firstName;
    private EditText lastName;
    private EditText internetNameProvider;
    private EditText internetPasswordProvider;
    private EditText address;
    private EditText phoneNumber;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SuplimentaryDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuplimentaryDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuplimentaryDataFragment newInstance(String param1, String param2) {
        SuplimentaryDataFragment fragment = new SuplimentaryDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suplimentary_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        TextView txt = view.findViewById(R.id.textViewClickCompleted);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_suplimentaryDataFragment_to_logInFragment);
            }
        });

        firstName = view.findViewById(R.id.firstNameForm);
        lastName = view.findViewById(R.id.lastNameForm);
        internetNameProvider = view.findViewById(R.id.internetProviderNameForm);
        internetPasswordProvider = view.findViewById(R.id.internetProviderPasswordForm);
        address = view.findViewById(R.id.addressForm);
        phoneNumber = view.findViewById(R.id.phoneNumberForm);

        mAuth = FirebaseAuth.getInstance();

        Button sendData = view.findViewById(R.id.buttonToSend);
        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firstName.getText().toString().equals("") ||
                    lastName.getText().toString().equals("") ||
                    address.getText().toString().equals("") ||
                    phoneNumber.getText().toString().equals("") ||
                    internetNameProvider.getText().toString().equals("") ||
                    internetPasswordProvider.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Data could not be saved! No empty box allowed!", Toast.LENGTH_SHORT).show();
                }

                String userToken = UUID.randomUUID().toString();
                User user = new User(
                        userToken,
                  mAuth.getCurrentUser().getEmail(),
                  firstName.getText().toString(),
                  lastName.getText().toString(),
                  address.getText().toString(),
                  phoneNumber.getText().toString()
                );
                db.collection("Users")
                        .document(userToken)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("ADDED", "User was added");
                                UserDevice userDevice = new UserDevice(
                                        userToken,
                                        internetNameProvider.getText().toString(),
                                        internetPasswordProvider.getText().toString()
                                );
                                db.collection("UserDevice")
                                        .document(UUID.randomUUID().toString())
                                        .set(userDevice)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                navController.navigate(R.id.action_suplimentaryDataFragment_to_logInFragment);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                Log.d("FAILED", e.getLocalizedMessage());
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.d("FAILED", e.getLocalizedMessage());
                            }
                        });
            }
        });
    }
}