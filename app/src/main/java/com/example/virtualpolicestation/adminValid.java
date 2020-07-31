package com.example.virtualpolicestation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class adminValid extends AppCompatActivity {

    EditText adminId, adminpass;

    Button checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_valid);


        adminId = findViewById(R.id.admin_id);
        adminId.setText("admin@mpcop.com");
        adminpass = findViewById(R.id.admin_pass);
        checker = findViewById(R.id.check);

        final String c ="abesit1234";


        checker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass = adminpass.getText().toString().trim();








                if (pass.equals(c)){


                    Toast.makeText(adminValid.this,"Admin logged in" , Toast.LENGTH_SHORT).show();



                    startActivity(new Intent(getApplicationContext(),adminview.class));


                }
                else {

                    Toast.makeText(adminValid.this,"Wrong Password Entered" , Toast.LENGTH_SHORT).show();


                }


            }
        });


    }
}