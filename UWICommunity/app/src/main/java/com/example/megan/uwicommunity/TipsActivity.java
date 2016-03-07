package com.example.megan.uwicommunity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ArrayList<String> list= new ArrayList<String>();

        list.add("• Always walk with others.");
        list.add("• Only walk in well lit areas.");
        list.add("• Avoid short-cuts through poorly lit or deserted areas.");
        list.add("• Park in well-lit, busy areas, preferable close to security.");
        list.add("• Have your keys ready before approaching the car.");
        list.add("• Look out for anyone loitering in the car park and report your observations");

        ArrayAdapter listAdapter=new ArrayAdapter<String>(this,R.layout.list_rows,list);
        ListView listView= (ListView) findViewById(R.id.tipsList);
        listView.setAdapter(listAdapter);

    }

    @Override
    public void onBackPressed() {

        Log.d("ME", "onBackPressed Called");
        this.finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

       // Intent intent=new Intent(getApplicationContext(),SafetyActivity.class);
       // startActivity(intent);
        this.finish();
        return true;

        // return super.onOptionsItemSelected(item);
    }



}
