package com.example.virtualpolicestation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.example.virtualpolicestation.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;


import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class FormActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword,mPhone;
    Button next;

    private FirebaseAuth fAuth;

    private FirebaseFirestore fStore;
    String userID;
    Boolean verificationOnProgress = false;

    TextView state,resend;
    PhoneAuthProvider.ForceResendingToken token;

    String verificationId;
    String otpCode;
    EditText optEnter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);






        mFullName   = findViewById(R.id.Fullname);
        mEmail      = findViewById(R.id.email);
        mPassword   = findViewById(R.id.password1);
        mPhone      = findViewById(R.id.mobileNo);
        next = findViewById(R.id.nextBtn);
        optEnter = findViewById(R.id.codeEnter);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),FIR.class));
            finish();
        }




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mPhone.getText().toString().isEmpty() && mPhone.getText().toString().length() == 10) {
                    if(!verificationOnProgress){
                        next.setEnabled(false);

                        String phoneNum = "+91" + mPhone.getText().toString();
                        Log.d("phone", "Phone No.: " + phoneNum);
                        requestPhoneAuth(phoneNum);
                    }else {
                        next.setEnabled(false);

                        otpCode = optEnter.getText().toString();
                        if(otpCode.isEmpty()){
                            optEnter.setError("Required");
                            return;
                        }








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

                }else {
                    mPhone.setError("Valid Phone Required");
                }
            }
        });
    }




    private void requestPhoneAuth(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60L, TimeUnit.SECONDS,this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        Toast.makeText(FormActivity.this, "OTP Timeout, Please Re-generate the OTP Again.", Toast.LENGTH_SHORT).show();
                        resend.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        token = forceResendingToken;
                        verificationOnProgress = true;
//                        progressBar.setVisibility(View.GONE);
//                        state.setVisibility(View.GONE);
                        next.setText("Verify");
                        next.setEnabled(true);
                        optEnter.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {













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

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(FormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}