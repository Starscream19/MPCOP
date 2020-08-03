package com.example.virtualpolicestation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class affidavit extends AppCompatActivity {
    EditText name, FatherNmae,gfather;
    Button affi;
    TextView cont,heading;
    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affidavit);
        name = findViewById(R.id.affi_user_name);
        FatherNmae = findViewById(R.id.affi_user_father_name);
        gfather = findViewById(R.id.affi_user_gfather_name);
        affi = findViewById(R.id.btn_affi);
        cont = findViewById(R.id.affi_text);
        heading = findViewById(R.id.affi_head);
        affi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(System.currentTimeMillis());

                Log.d(TAG, "date"+time);
                String nam = name.getText().toString();
                String fname = FatherNmae.getText().toString();
                String gname =gfather.getText().toString();

                String text = "I am " + nam + " and my father’s name " + fname + " , appearing on the enclosed ID proof, is single name. My grandfather’s name is " + gname + "  For applying DIN application of mine, I am mentioning my grandfather’s name \" " + gname + "\" as my father’s Last name, as this a mandatory requirement for applying DIN. (Referred point no. 16 in FAQ at www.mca.gov.in). Both names denote one and the same person.\n" +
                        "\n" +
                        "I solemnly state that the contents of this affidavit are true to the best of my knowledge and belief and that it conceals nothing and that no part of it is false.";

                heading.setText("Affidavit");
                cont.setText(text);
                try {
                    saveTextToFile(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void saveTextToFile(String text) throws IOException {


        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

        try {

        }catch (Exception e){
            File path  = Environment.getExternalStorageDirectory();
            File dir = new File(path,"/My Affidavits/");
            dir.mkdirs();
            String filename = "Affidavit_"+time+".txt";
            File file = new File(dir, filename);
            FileWriter writer = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(text);
            bw.close();
        }
    }

    public void home(View view) {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }
}