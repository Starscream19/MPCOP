package com.example.virtualpolicestation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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

public class adminview extends AppCompatActivity {

    TextView sub1,sub2,sub3;


    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    String userID,decryptedText,status;
    public static final String TAG = "TAG";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminview);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        sub1 = findViewById(R.id.subject1);
        sub2 = findViewById(R.id.subject2);
        sub3 = findViewById(R.id.subject3);


        final DocumentReference Dref = fStore.collection("F.I.R").document(userID);
        Dref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                String StrEncText = documentSnapshot.getString("Subject");
                byte[] encText = decoderfun(StrEncText);
                String key = documentSnapshot.getString("Key");
                byte[] encodedSecretKey = decoderfun(key);
                String status = documentSnapshot.getString("Status");
                String date = documentSnapshot.getString("Date");

                SecretKey originalSecretKey = new SecretKeySpec(encodedSecretKey, 0, encodedSecretKey.length, "AES");

                decryptedText = Decrypt.decrypt(encText, (SecretKeySpec) originalSecretKey);

                sub1.setText("F.I.R Subject- " +decryptedText + "\n Date of File- " + date + "\n Status- " + status);
            }
        });

        final DocumentReference Dref2 = fStore.collection("F.I.R").document("HQhJIY2wBherEvnUUq3GTkhliW02");
        Dref2.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                String StrEncText = documentSnapshot.getString("Subject");
                byte[] encText = decoderfun(StrEncText);
                String key = documentSnapshot.getString("Key");
                byte[] encodedSecretKey = decoderfun(key);
                String status = documentSnapshot.getString("Status");
                String date = documentSnapshot.getString("Date");

                SecretKey originalSecretKey = new SecretKeySpec(encodedSecretKey, 0, encodedSecretKey.length, "AES");

                decryptedText = Decrypt.decrypt(encText, (SecretKeySpec) originalSecretKey);

                sub2.setText("F.I.R Subject- " +decryptedText + "\n Date of File- " + date + "\n Status- " + status);
            }
        });


        final DocumentReference Dref3 = fStore.collection("F.I.R").document("aISNZMFbqVYS9ALmxdOUla3CKav1");
        Dref3.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                String StrEncText = documentSnapshot.getString("Subject");
                byte[] encText = decoderfun(StrEncText);
                String key = documentSnapshot.getString("Key");
                byte[] encodedSecretKey = decoderfun(key);
                status = documentSnapshot.getString("Status");
                String date = documentSnapshot.getString("Date");

                SecretKey originalSecretKey = new SecretKeySpec(encodedSecretKey, 0, encodedSecretKey.length, "AES");

                decryptedText = Decrypt.decrypt(encText, (SecretKeySpec) originalSecretKey);

                sub3.setText("F.I.R Subject- " +decryptedText + "\n Date of File- " + date + "\n Status- " + status);
            }
        });


        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (status.equals("Verified")){

                    startActivity(new Intent(getApplicationContext(),officialfir.class));

                }
                if (status.equals("Pending")){

                    startActivity(new Intent(getApplicationContext(),currentuserfir.class));

                }


            }
        });
        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                startActivity(new Intent(getApplicationContext(),official2.class));

            }
        });

        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),official3.class));
            }
        });


    }

    public static byte[] decoderfun(String enval) {
        byte[] conVal = Base64.decode(enval,Base64.DEFAULT);
        return conVal;

    }
}