package com.example.freshair.Functionalities.Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshair.Functionalities.AirPollutionBlog.AirPollutionBlogViewModel;
import com.example.freshair.Models.ModelsBlog.Post;
import com.example.freshair.R;
import com.example.freshair.Repo.RegisterRepo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private RegisterModelView vModel;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        vModel = ViewModelProviders.of(this).get(RegisterModelView.class);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        TextView txt = view.findViewById(R.id.textViewClick);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_registerFragment_to_logInFragment);
            }
        });

        Button buttonToRegister = view.findViewById(R.id.buttonToRegister);
        buttonToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewButton) {
                EditText email = view.findViewById(R.id.emailRegister);
                String emailData = email.getText().toString().trim();

                EditText password = view.findViewById(R.id.passwordRegister);
                String passwordData = password.getText().toString().trim();

                EditText confirmPassword = view.findViewById(R.id.confirmPasswordRegister);
                String confirmPasswordData = confirmPassword.getText().toString().trim();

                if (emailData.equals("") || passwordData.equals("") || confirmPasswordData.equals("")){
                    Toast.makeText(getActivity(), "Registration failed! No empty box allowed!", Toast.LENGTH_SHORT).show();
                }
                else if(emailData.endsWith("@gmail.com") || emailData.endsWith("@yahoo.com") || emailData.endsWith("@s.unibuc.ro")) {
                    final boolean[] emailExists = {false};
                    vModel.init();

                    vModel.getEmails().observe((LifecycleOwner) requireContext(), new Observer<List<String>>() {
                        @Override
                        public void onChanged(List<String> emails) {
                            for(String e : emails){
                                Log.d("email",e);
                                System.out.println("EMAIL " + e);
                                if (e.equals(emailData)){
                                    emailExists[0] = true;
                                }
                            }
                            if(emailExists[0] == false) {
                                if (checkPasswordStrength(passwordData, emailData)) {
                                    if (passwordData.equals(confirmPasswordData)) {
                                        mAuth.createUserWithEmailAndPassword(emailData, passwordData).addOnCompleteListener((OnCompleteListener<AuthResult>) task -> {
                                            System.out.println("AICIIIIIIIIIIIIIIIIIIII");
                                            if (task.isSuccessful()) {
                                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        navController.navigate(R.id.action_registerFragment_to_suplimentaryDataFragment);
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getActivity(), "Registration failed! Password needs at least 6 characters", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), "Registration failed! Password do not match Confirmed Password!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Registration failed! Password not strong enough!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getActivity(), "Registration failed! Email already exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(), "Registration failed! Email format incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean checkPasswordStrength(String password, String email){
        if(password.length() <= 6)
            return false;

        String[] arrEmail = email.split("@");
        // check contains email data
        if(password.contains(arrEmail[0]))
            return false;

        // check contains consecutive numbers
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(password);
        ArrayList<String> numberList = new ArrayList<String>();
        while(m.find()) {
            numberList.add(m.group());
        }

        for(String number : numberList){
            int start;
            int length = number.length();
            for (int i = 0; i < length / 2; i++)
            {
                String new_str = number.substring(0, i + 1);
                int num = Integer.parseInt(new_str);
                start = num;
                while (new_str.length() < length)
                {
                    num++;
                    new_str = new_str + String.valueOf(num);
                }

                // check if new String becomes equal to
                // input String
                if (new_str.equals(number))
                    return false;
            }
        }

        // check consecutive letters
        if(checkConsecutiveLetters(password, "qwertyuiop") == false)
            return false;
        if(checkConsecutiveLetters(password, "poiuytrewq") == false)
            return false;
        if(checkConsecutiveLetters(password, "asdfghjkl") == false)
            return false;
        if(checkConsecutiveLetters(password, "lkjhgfdsa") == false)
            return false;
        if(checkConsecutiveLetters(password, "zxcvbnm") == false)
            return false;
        if(checkConsecutiveLetters(password, "mnbvcxz") == false)
            return false;

        return true;
    }

    boolean checkConsecutiveLetters(String password, String test){
        for(int k = 3; k <= test.length(); k++){
            for(int i = 0; i <= test.length() - k; i++){
                String s = test.substring(i, i + k);
                if (password.equals(s))
                    return false;
            }
        }
        return true;
    }
}