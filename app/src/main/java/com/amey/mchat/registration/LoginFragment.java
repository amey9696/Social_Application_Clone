package com.amey.mchat.registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amey.mchat.MainActivity;
import com.amey.mchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static com.amey.mchat.registration.CreateAccountFragment.VALID_EMAIL_ADDRESS_REGEX;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }
    private EditText ep,passw;
    private Button login;
    private ProgressBar pb2;
    private TextView createAccountTV,forgot;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ep.setError(null);
                passw.setError(null);
                if(ep.getText().toString().isEmpty()){
                    ep.setError("Required");
                    return;
                }
                if (passw.getText().toString().isEmpty()){
                    passw.setError("Required");
                    return;
                }
                if(VALID_EMAIL_ADDRESS_REGEX.matcher(ep.getText().toString()).find()){
                    pb2.setVisibility(View.VISIBLE);
                    login(ep.getText().toString());
                }
                else if (ep.getText().toString().matches("\\d{10}")){
                    pb2.setVisibility(View.VISIBLE);

                    FirebaseFirestore.getInstance().collection("users").whereEqualTo("phone",ep.getText().toString())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful()){
                               List<DocumentSnapshot> document=task.getResult().getDocuments();
                               if (document.isEmpty()){
                                   ep.setError("Number not Found");
                                   pb2.setVisibility(View.INVISIBLE);
                                   return;
                               }else {
                                   String email=document.get(0).get("email").toString();
                                   login(email);
                               }
                           }else {
                               String error=task.getException().getMessage();
                               Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                               pb2.setVisibility(View.INVISIBLE);
                           }
                        }
                    });
                }else {
                    ep.setError("Please Enter a valid Email id or phone");
                    return;
                }
            }
        });
        createAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity)getActivity()).setFragment(new CreateAccountFragment());
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity)getActivity()).setFragment(new ForgotPasswordFragment());
            }
        });
    }
    private void init(View view){
        ep=view.findViewById(R.id.emailphone);
        passw=view.findViewById(R.id.pass);
        login=view.findViewById(R.id.login);
        pb2=view.findViewById(R.id.progressBar2);
        createAccountTV=view.findViewById(R.id.signuptv);
        forgot=view.findViewById(R.id.forgotpass);
    }
    private void login(String email){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,passw.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                   // Intent intent=new Intent(getContext(), UsernameActivity.class);
                   Intent intent=new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                    pb2.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}