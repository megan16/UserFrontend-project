package com.example.megan.uwicommunity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MedicalContact extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ((Button)findViewById(R.id.callbutton1)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String phoneNo = "tel:6408650";

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNo));
                startActivity(intent);

            }

        });

        ((Button)findViewById(R.id.callbutton2)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String phoneNo = "tel:7317291";

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNo));
                startActivity(intent);

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //Intent intent=new Intent(getApplicationContext(),SafetyActivity.class);
        //startActivity(intent);
        this.finish();
        return true;
    }

}
