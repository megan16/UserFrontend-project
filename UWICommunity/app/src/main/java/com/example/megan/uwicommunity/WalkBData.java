package com.example.megan.uwicommunity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WalkBData extends AppCompatActivity {

    private Spinner meetUpList;
    private Spinner destinationList;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_bdata);

        meetUpList= (Spinner) findViewById(R.id.meetUp);
        destinationList= (Spinner) findViewById(R.id.dest);
        submit= (Button) findViewById(R.id.submit);

        addItemsToSpinners();
        setUpSpinnerListeners();
        onSubmit();

        getSupportActionBar().setTitle("Walk Buddy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff1a1a")));


    }

    public void onSubmit(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             Log.d("MEG", "Do web services");

                if(meetUpList.getSelectedItem().toString().equalsIgnoreCase("Select Location") ||
                   destinationList.getSelectedItem().toString().equalsIgnoreCase("Select Location")){

                    Toast.makeText(getApplicationContext(), "Invalid please select a location",
                            Toast.LENGTH_SHORT).show();
                }

                //else
                Log.d("MEG","values selected: "+meetUpList.getSelectedItem()+
                        "& "+destinationList.getSelectedItem());
            }
        });
    }



    public void addItemsToSpinners(){
        List <String> list= new ArrayList<String>();
        //TODO:Make select location not a choice& default
        list.add("Select Location");
        list.add("Bus Route- North Gate (Yvettes)");
        list.add("South Gate- Gate Boys");

        ArrayAdapter <String> adapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meetUpList.setAdapter(adapter);
        destinationList.setAdapter(adapter);


    }

    public void setUpSpinnerListeners(){
        meetUpList.setOnItemSelectedListener(new CustomItemSelectedListner());

    }
}
