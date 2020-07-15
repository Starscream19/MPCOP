package com.example.virtualpolicestation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.content.Intent;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.widget.Toast;

import androidx.annotation.NonNull;


import com.example.virtualpolicestation.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword,mPhone;
    Button RegisterBtn;

    private FirebaseAuth fAuth;
    ProgressBar progressBar;
    private FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Male", "Female", "Transgender"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);






        mFullName   = findViewById(R.id.Fullname);
        mEmail      = findViewById(R.id.email);
        mPassword   = findViewById(R.id.password1);
        mPhone      = findViewById(R.id.mobileNo);
        RegisterBtn= findViewById(R.id.regster1);



        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),FIR.class));
            finish();
        }
        progressBar = findViewById(R.id.loading);



        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String phone    = mPhone.getText().toString();


                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

//                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(FormActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();

                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);

                            fStore.collection("users").document(userID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void Void) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());

                                }
                            });
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                        }else {
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Log.e("LoginActivity", "Failed Registration", e);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }
                    }
                });
            }
        });








    }
}