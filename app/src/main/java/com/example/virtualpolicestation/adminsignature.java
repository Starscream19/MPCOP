package com.example.virtualpolicestation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class adminsignature extends AppCompatActivity {

    Bitmap bitmap;
    Button clear,save;
    SignatureView signatureView;
    String Encoded_Signature_Bitmap_Values;
    private FirebaseAuth fAuth;
    public static final String TAG = "TAG";

    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminsignature);

        signatureView =  (SignatureView) findViewById(R.id.signature_view_admin);
        clear = (Button) findViewById(R.id.clear2);
        save = (Button) findViewById(R.id.save2);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clearCanvas();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = signatureView.getSignatureBitmap();
                Encoded_Signature_Bitmap_Values = saveImage(bitmap);


                fAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();
                final String  userID = fAuth.getCurrentUser().getUid();

                Map<String, Object> user = new HashMap<>();
                user.put("AdminSignature",Encoded_Signature_Bitmap_Values );


                fStore.collection("F.I.R").document(userID).set(user, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Log.d(TAG, "message has een encrypted  " + userID);
                                startActivity(new Intent(getApplicationContext(), officialfir.class));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());

                    }
                });





            }
        });





    }


    private String saveImage(Bitmap myBitmap) {


        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, bao); // bmp is bitmap from user image file
        myBitmap.recycle();
        byte[] byteArray = bao.toByteArray();
        String imageB64 = Base64.encodeToString(byteArray, Base64.DEFAULT);


        return imageB64;
    }
}