package com.example.virtualpolicestation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    GridView gridView;
    String[] services = {"Register E-FIR","View FIR","Report Lost Article","Know Police Station"
    ,"Affidavit","Stolen/Recovered Vehicle","Character Certificate","Domestic Help",
    "Employee Verification"};
    int[] serviceImage ={R.drawable.images,R.drawable.view_fir,R.drawable.lost_art,R.drawable.location,
    R.drawable.accident,R.drawable.stolen,R.drawable.certificate,R.drawable.help,R.drawable.verification};


    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gridView = findViewById(R.id.grid_View);


        fAuth = FirebaseAuth.getInstance();


//        if(fAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//            finish();
//        }




        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar_layout);
        View view =getSupportActionBar().getCustomView();

        ImageButton logout= (ImageButton)view.findViewById(R.id.logout_icon);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        ImageButton admin= (ImageButton)view.findViewById(R.id.admin_icon);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),adminValid.class));


            }
        });

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
                    myIntent = new Intent(view.getContext(), affidavit.class);
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