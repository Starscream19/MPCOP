package com.example.virtualpolicestation;

import android.content.Intent;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.UUID;

import android.net.Uri;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class FIR extends AppCompatActivity {




    EditText uFullName,uAddress,uCity,uPostcode,uSubject,uComplaint;
    Button Register_FIR,btn_choose;



    StorageReference storageReference;
    FirebaseStorage storage;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;



    private FirebaseAuth fAuth;

    private FirebaseFirestore fStore;
    String userID;
    public static final String TAG = "TAG";



    KeyGenerator keyGenerator;
    SecretKey secretKey;
    byte[] secretKeyen;
    String strSecretKey;

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
        btn_choose = findViewById(R.id.choose);



        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });



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



                    try {
                        cipherComplaint = Encrypt.encrypt(uComplaint.getText().toString().trim().getBytes(), secretKey);

                        String cipher = encoderfun(cipherComplaint);



                        cipherName = Encrypt.encrypt(uFullName.getText().toString().trim().getBytes(), secretKey);

                        String cipherNam = encoderfun(cipherName);


                        cipherAddress = Encrypt.encrypt(uAddress.getText().toString().trim().getBytes(),secretKey );

                        String cipherAdd = encoderfun(cipherAddress);
                        cipherCity = Encrypt.encrypt(uCity.getText().toString().trim().getBytes(),secretKey);
                        String cipherCit = encoderfun(cipherCity);
                        cipherSubject = Encrypt.encrypt(uSubject.getText().toString().trim().getBytes(),secretKey);
                        String cipherSub = encoderfun(cipherSubject);
                        cipherPostcode = Encrypt.encrypt(uPostcode.getText().toString().trim().getBytes(),secretKey);
                        String cipherPost = encoderfun(cipherPostcode);





                        uploadImage();







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
                        user.put("Key", strSecretKey);


                        fStore.collection("F.I.R").document(userID).set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void> () {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "message has een encrypted  " + userID);
                                        startActivity(new Intent(getApplicationContext(), signature.class));

                                    }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());

                            }
                        });

















                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }

    private void SelectImage() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();

        }
    }



    // UploadImage method
    private void uploadImage() {
        if (filePath != null) {



            // Defining the child of storageReference
            StorageReference ref
                    = storageReference.child("images/" + UUID.randomUUID().toString());


            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog

                                    Toast
                                            .makeText(FIR.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded

                            Toast
                                    .makeText(FIR.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());

                                }
                            });
        }
    }


        public static String encoderfun(byte[] decval) {
        String conVal= android.util.Base64.encodeToString(decval, Base64.DEFAULT);
        return conVal;
    }


}