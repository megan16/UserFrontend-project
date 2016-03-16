package com.example.megan.uwicommunity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class UploadCrime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_crime);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF1A1A")));
        getSupportActionBar().setTitle("Upload Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upload_crime, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            //verify user submission
            Toast.makeText(getApplicationContext(),"Verify submission",Toast.LENGTH_SHORT).show();
            verifySubmission();

        }

        return super.onOptionsItemSelected(item);
    }


    public void verifySubmission(){

        final AlertDialog.Builder alert=new AlertDialog.Builder(UploadCrime.this);
        alert.setMessage(R.string.verifySubmit);
        alert.setPositiveButton(R.string.yesP, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // perform web services
                //send kill activity and go home
            }
        });

        alert.setNegativeButton(R.string.canP, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(getApplicationContext(),"If you wish to discard simply press back to go home",
                        Toast.LENGTH_LONG).show();
            }

        });


        alert.create();
        alert.show();


    }
}
