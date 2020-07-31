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

public class officialfir extends AppCompatActivity {

    TextView Ousername,Oaddress,Opostcode,Ocity,Osubject,Ocompliant;
    ImageView sign,sign2;
    Button home;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    String userID,odecryptedName,odecryptedAddress,odecryptedPost,odecryptedCity,odecryptedSubject,odecryptedComp,signatureUser1,signatureAdmin;
    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        setContentView(R.layout.activity_officialfir);




        Ousername = findViewById(R.id.Off_name);
        Oaddress = findViewById(R.id.Off_address);
        Opostcode = findViewById(R.id.Off_postcode);
        Ocity = findViewById(R.id.Off_city);
        Osubject = findViewById(R.id.Off_subject);
        Ocompliant = findViewById(R.id.Off_complaint);
        sign = findViewById(R.id.Off_signature);
        sign2 = findViewById(R.id.fir_adminsignature);
        home = (Button) findViewById(R.id.adminhome);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
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
                odecryptedName= Decrypt.decrypt(encNamText, (SecretKeySpec) originalSecretKey);
                Ousername.setText(odecryptedName);


                String StrAddText = documentSnapshot.getString("Address");
                byte[] encAddText = decoderfun(StrAddText);
                odecryptedAddress = Decrypt.decrypt(encAddText, (SecretKeySpec) originalSecretKey);
                Oaddress.setText(odecryptedAddress);



                String StrCitText = documentSnapshot.getString("City");
                byte[] encCitText = decoderfun(StrCitText);
                odecryptedCity = Decrypt.decrypt(encCitText, (SecretKeySpec) originalSecretKey);
                Ocity.setText(odecryptedCity);


                String StrPosText = documentSnapshot.getString("PostCode");
                byte[] encPosText = decoderfun(StrPosText);
                odecryptedPost = Decrypt.decrypt(encPosText, (SecretKeySpec) originalSecretKey);
                Opostcode.setText(odecryptedPost);



                String StrComText = documentSnapshot.getString("Complaint");
                byte[] encComText = decoderfun(StrComText);
                odecryptedComp = Decrypt.decrypt(encComText, (SecretKeySpec) originalSecretKey);
                Ocompliant.setText(odecryptedComp);


                String StrSubText = documentSnapshot.getString("Subject");
                byte[] encSubText = decoderfun(StrSubText);
                odecryptedSubject = Decrypt.decrypt(encSubText, (SecretKeySpec) originalSecretKey);
                Osubject.setText(odecryptedSubject);

                signatureUser1 =documentSnapshot.getString("Signature");

                byte[] image = Base64.decode(signatureUser1, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                sign.setImageBitmap(bitmap);


                signatureAdmin = documentSnapshot.getString("AdminSignature");
                byte[] image2 =  Base64.decode(signatureAdmin, Base64.DEFAULT);
                Bitmap bitmap2 = BitmapFactory.decodeByteArray(image2, 0, image2.length);
                sign2.setImageBitmap(bitmap2);


            }
        });




    }

    public static byte[] decoderfun(String enval) {
        byte[] conVal = Base64.decode(enval,Base64.DEFAULT);
        return conVal;

    }
}