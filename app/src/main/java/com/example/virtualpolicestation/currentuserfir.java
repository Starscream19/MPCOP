package com.example.virtualpolicestation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.io.ByteArrayOutputStream;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class currentuserfir extends AppCompatActivity {




    TextView Tusername,Taddress,Tpostcode,Tcity,Tsubject,Tcompliant;
    Uri uri,shouri;
    ImageView sign;
    Button nxt,choose;
    String shosiguri;





    byte[] cipherShoSig;
    SecretKey originalSecretKey;



    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    String userID,decryptedName,decryptedAddress,decryptedPost,decryptedCity,decryptedSubject,decryptedComp,signatureUser,decryptSIg;
    public static final String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        setContentView(R.layout.activity_currentuserfir);
        Tusername = findViewById(R.id.fir_user_name);
        Taddress = findViewById(R.id.fir_user_address);
        Tpostcode = findViewById(R.id.fir_user_postcode);
        Tcity = findViewById(R.id.fir_user_city);
        Tsubject = findViewById(R.id.fir_user_subject);
        Tcompliant = findViewById(R.id.fir_user_complaint);
        sign = findViewById(R.id.fir_user_signature);
        nxt = findViewById(R.id.fir_user_btn);
        choose = findViewById(R.id.sho_choose);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectimage();
            }
        });
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),officialfir.class));
            }
        });


        final DocumentReference Dref = fStore.collection("F.I.R").document(userID);
        Dref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {



                String key = documentSnapshot.getString("Key");
                byte[] encodedSecretKey = decoderfun(key);
                originalSecretKey = new SecretKeySpec(encodedSecretKey, 0, encodedSecretKey.length, "AES");


                String StrNamText = documentSnapshot.getString("Name");
                byte[] encNamText = decoderfun(StrNamText);
                decryptedName= Decrypt.decrypt(encNamText, (SecretKeySpec) originalSecretKey);
                Tusername.setText(decryptedName);


                String StrAddText = documentSnapshot.getString("Address");
                byte[] encAddText = decoderfun(StrAddText);
                decryptedAddress = Decrypt.decrypt(encAddText, (SecretKeySpec) originalSecretKey);
                Taddress.setText(decryptedAddress);



                String StrCitText = documentSnapshot.getString("City");
                byte[] encCitText = decoderfun(StrCitText);
                decryptedCity = Decrypt.decrypt(encCitText, (SecretKeySpec) originalSecretKey);
                Tcity.setText(decryptedCity);


                String StrPosText = documentSnapshot.getString("PostCode");
                byte[] encPosText = decoderfun(StrPosText);
                decryptedPost = Decrypt.decrypt(encPosText, (SecretKeySpec) originalSecretKey);
                Tpostcode.setText(decryptedPost);



                String StrComText = documentSnapshot.getString("Complaint");
                byte[] encComText = decoderfun(StrComText);
                decryptedComp = Decrypt.decrypt(encComText, (SecretKeySpec) originalSecretKey);
                Tcompliant.setText(decryptedComp);


                String StrSubText = documentSnapshot.getString("Subject");
                byte[] encSubText = decoderfun(StrSubText);
                decryptedSubject = Decrypt.decrypt(encSubText, (SecretKeySpec) originalSecretKey);
                Tsubject.setText(decryptedSubject);

                signatureUser =documentSnapshot.getString("Signature");

                byte[] image = Base64.decode(signatureUser, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);


                sign.setImageBitmap(bitmap);


            }
        });





    }

    private void selectimage() {



        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 786);
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == 786 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            shouri = data.getData();
            Bitmap bitmap= null;





            try {


                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), shouri);
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte [] b=baos.toByteArray();
                shosiguri = Base64.encodeToString(b, Base64.DEFAULT);
                Map<String, Object> user = new HashMap<>();
                user.put("SHO Signature", shosiguri);


                fStore.collection("F.I.R").document(userID).set(user, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(currentuserfir.this, "signature is uploaded", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());

                    }
                });

            }catch (Exception e){

            }














        }







    }


    public static byte[] decoderfun(String enval) {
        byte[] conVal = Base64.decode(enval,Base64.DEFAULT);
        return conVal;

    }


    public static String encoderfun(byte[] decval) {
        String conVal= android.util.Base64.encodeToString(decval, Base64.DEFAULT);
        return conVal;
    }







}