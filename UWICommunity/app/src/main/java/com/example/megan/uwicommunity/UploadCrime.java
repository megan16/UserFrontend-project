package com.example.megan.uwicommunity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UploadCrime extends AppCompatActivity {
    private Spinner locList;
    private Spinner crimeTypeList;
    private RadioGroup imageChoiceRadioGroup;
    private RadioButton answerRadioButton;
    private ImageView pictureUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_crime);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF1A1A")));
        getSupportActionBar().setTitle("Upload Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        crimeTypeList= (Spinner) findViewById(R.id.crimeCategory);
        locList= (Spinner)findViewById(R.id.crimeLoc);
        addLocationToSpinner(); // add location stored in database to dropdown list
        addCrimeTypesToSpinner(); //adds the categories of crimes to a list for a user to choice from

        imageChoiceRadioGroup= (RadioGroup) findViewById(R.id.uploadPicChoice); //get radio group
        //View view= getWindow().findViewById(android.R.id.content);
        pictureUpload= (ImageView) findViewById(R.id.picture);
        getChoice(); //get whether or not the user has a picture to upload or not


    }

    private void getChoice() {

        imageChoiceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId!=-1) {

                    answerRadioButton = (RadioButton) findViewById(checkedId);// get which radio button was selected
                    Log.d("MEG", " button " + answerRadioButton.getText());
                    if (answerRadioButton.getText().toString().equalsIgnoreCase("yes")) {
                        // set image view visibility to visible
                        pictureUpload.setVisibility(View.VISIBLE);
                    } else {
                        pictureUpload.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });

    }

    private void addCrimeTypesToSpinner() {
        List<String> list= new ArrayList<String>();
        list.add("Select Crime");
        list.add("Motor Theft");
        list.add("Robbery");
        list.add("Rape");
        list.add("Peeping Toms");
        list.add("Assault");
        list.add("Motor Vandalism");

        Collections.sort(list.subList(1,list.size()));// sort list for user dispaly
        ArrayAdapter <String> adapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crimeTypeList.setAdapter(adapter);

    }

    public void addLocationToSpinner(){
        List<String> list= new ArrayList<String>();
        //TODO:Make select location not a choice& default
        list.add("Select Location");
        list.add("Bus Route- North Gate (Yvettes)");
        list.add("South Gate- Gate Boys");
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locList.setAdapter(adapter);

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
                Toast.makeText(getApplicationContext(), "If you wish to discard simply press back to go home",
                        Toast.LENGTH_LONG).show();
            }

        });


        alert.create();
        alert.show();


    }

/* ################################################################################################*/
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


}
