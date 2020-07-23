package com.example.virtualpolicestation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Know_police_station extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_police_station);
        TextView t = (TextView) findViewById(R.id.text_click_me);
        t.setMovementMethod(LinkMovementMethod.getInstance());
    }
}