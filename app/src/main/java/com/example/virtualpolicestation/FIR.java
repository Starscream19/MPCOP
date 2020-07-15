package com.example.virtualpolicestation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.SetOptions;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class FIR extends AppCompatActivity {




    EditText uFullName,uAddress,uCity,uPostcode,uSubject,uComplaint;
    Button Register_FIR;


    private FirebaseAuth fAuth;

    private FirebaseFirestore fStore;
    String userID;
    public static final String TAG = "TAG";



    KeyGenerator keyGenerator;
    SecretKey secretKey;
    byte[] secretKeyen;
    String strSecretKey;
    byte[] IV = new byte[16];
    byte[] cipherComplaint,cipherName,cipherAddress,cipherCity,cipherSubject,cipherPostcode;
    SecureRandom random;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_i_r);


        uFullName   = findViewById(R.id.Full_name);
        uAddress   = findViewById(R.id.address);
        uCity    = findViewById(R.id.address);

        uPostcode  = findViewById(R.id.postcode);
        uSubject   = findViewById(R.id.subject);
        uComplaint  = findViewById(R.id.Complaint);
        Register_FIR = findViewById(R.id.btn_fir);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        Register_FIR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String complaint = uComplaint.getText().toString();

                if (TextUtils.isEmpty(uComplaint.getText())) {
                    Toast.makeText(FIR.this, "Enter the values " , Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        keyGenerator = KeyGenerator.getInstance("AES");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    keyGenerator.init(256);
                    secretKey = keyGenerator.generateKey();
                    secretKeyen = secretKey.getEncoded();
                    strSecretKey = encoderfun(secretKeyen);
//                    tvSecretKey.setText(strSecretKey);

                    random = new SecureRandom();
                    random.nextBytes(IV);
                    try {
                        cipherComplaint = Encrypt.encrypt(uComplaint.getText().toString().trim().getBytes(), secretKey, IV);

                        String cipher = encoderfun(cipherComplaint);
                        String tvIV = encoderfun(IV);


                        cipherName = Encrypt.encrypt(uFullName.getText().toString().trim().getBytes(), secretKey,IV);

                        String cipherNam = encoderfun(cipherName);


                        cipherAddress = Encrypt.encrypt(uAddress.getText().toString().trim().getBytes(),secretKey ,IV);

                        String cipherAdd = encoderfun(cipherAddress);
                        cipherCity = Encrypt.encrypt(uCity.getText().toString().trim().getBytes(),secretKey,IV);
                        String cipherCit = encoderfun(cipherCity);
                        cipherSubject = Encrypt.encrypt(uSubject.getText().toString().trim().getBytes(),secretKey,IV);
                        String cipherSub = encoderfun(cipherSubject);
                        cipherPostcode = Encrypt.encrypt(uPostcode.getText().toString().trim().getBytes(),secretKey,IV);
                        String cipherPost = encoderfun(cipherPostcode);







                        Toast.makeText(FIR.this, cipher , Toast.LENGTH_SHORT).show();


                        fAuth = FirebaseAuth.getInstance();
                        fStore = FirebaseFirestore.getInstance();


                        userID = fAuth.getCurrentUser().getUid();


                        Map<String, Object> user = new HashMap<>();
                        user.put("Name", cipherNam);
                        user.put("Address", cipherAdd);
                        user.put("City", cipherCit);
                        user.put("User", userID);
                        user.put("PostCode", cipherPost);
                        user.put("Subject", cipherSub);
                        user.put("Complaint", cipher);


                        fStore.collection("F.I.R").add(user)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {


                                        Log.d(TAG, "message has een encrypted  " + userID);
                                        startActivity(new Intent(getApplicationContext(), success.class));

                                    }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());

                            }
                        });





                        Utils.saveData(FIR.this, cipher, strSecretKey, tvIV);











                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }


    public static String encoderfun(byte[] decval) {
        String conVal= android.util.Base64.encodeToString(decval, Base64.DEFAULT);
        return conVal;
    }


}