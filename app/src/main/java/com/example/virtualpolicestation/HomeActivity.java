package com.example.virtualpolicestation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    GridView gridView;
    String[] services = {"Register E-FIR","View FIR","Report Lost Article","Know Police Station"
    ,"Accident Alert Area","Stolen/Recovered Vehicle","Character Certificate","Domestic Help",
    "Employee Verification"};
    int[] serviceImage ={R.drawable.images,R.drawable.view_fir,R.drawable.lost_art,R.drawable.location,
    R.drawable.accident,R.drawable.stolen,R.drawable.certificate,R.drawable.help,R.drawable.verification};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gridView = findViewById(R.id.grid_View);

        MainAdapter adapter = new MainAdapter(HomeActivity.this,services,serviceImage);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"You Selected" + services[+position],
                        Toast.LENGTH_SHORT).show();
                Intent myIntent = null;
                if(position == 0){
                    myIntent = new Intent(view.getContext(), FIR.class);
                }
                if(position == 1){
                    myIntent = new Intent(view.getContext(), FIR.class);
                }
                if(position ==2){
                    myIntent = new Intent(view.getContext(), Lost_article.class);
                }
                if(position==3){
                    myIntent = new Intent(view.getContext(), Know_police_station.class);
                }
                if(position==4){
                    myIntent = new Intent(view.getContext(), FIR.class);
                }
                if(position==5){
                    myIntent = new Intent(view.getContext(), Stolen_Recovered_vehicle.class);
                }
                if(position==6){
                    myIntent = new Intent(view.getContext(), FIR.class);
                }
                if(position==7){
                    myIntent = new Intent(view.getContext(), FIR.class);
                }
                if(position==8){
                    myIntent = new Intent(view.getContext(), FIR.class);
                }
                startActivity(myIntent);
                finish();
            }
        });
    }
}