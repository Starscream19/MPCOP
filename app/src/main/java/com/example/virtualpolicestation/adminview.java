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

    TextView sub1;


    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    String userID,decryptedText;
    public static final String TAG = "TAG";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminview);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        sub1 = findViewById(R.id.subject1);


        final DocumentReference Dref = fStore.collection("F.I.R").document(userID);
        Dref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                String StrEncText = documentSnapshot.getString("Subject");
                byte[] encText = decoderfun(StrEncText);
                String key = documentSnapshot.getString("Key");
                byte[] encodedSecretKey = decoderfun(key);

                SecretKey originalSecretKey = new SecretKeySpec(encodedSecretKey, 0, encodedSecretKey.length, "AES");

                decryptedText = Decrypt.decrypt(encText, (SecretKeySpec) originalSecretKey);

                sub1.setText("F.I.R Subject- " +decryptedText);
            }
        });


        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(),currentuserfir.class));

            }
        });


    }

    public static byte[] decoderfun(String enval) {
        byte[] conVal = Base64.decode(enval,Base64.DEFAULT);
        return conVal;

    }
}