package com.example.virtualpolicestation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class currentuserfir extends AppCompatActivity {




    TextView Tusername,Taddress,Tpostcode,Tcity,Tsubject,Tcompliant;
    ImageView sign;
    Button nxt;



    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    String userID,decryptedName,decryptedAddress,decryptedPost,decryptedCity,decryptedSubject,decryptedComp,signatureUser;
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
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),adminsignature.class));
            }
        });


        final DocumentReference Dref = fStore.collection("F.I.R").document(userID);
        Dref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {



                String key = documentSnapshot.getString("Key");
                byte[] encodedSecretKey = decoderfun(key);
                SecretKey originalSecretKey = new SecretKeySpec(encodedSecretKey, 0, encodedSecretKey.length, "AES");


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

    public static byte[] decoderfun(String enval) {
        byte[] conVal = Base64.decode(enval,Base64.DEFAULT);
        return conVal;

    }






}