package com.example.megan.uwicommunity;



import org.apache.http.HttpEntity;
import android.support.v7.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EmergencyContact extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        TextView text = (TextView) findViewById(R.id.ec_info);
//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Molot.otf");
//        text.setTypeface(tf);

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

        Intent intent=new Intent(getApplicationContext(),SafetyActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

}
