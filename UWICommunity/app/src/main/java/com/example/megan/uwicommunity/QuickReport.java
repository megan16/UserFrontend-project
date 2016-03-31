package com.example.megan.uwicommunity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class QuickReport extends AppCompatActivity {
//TODO: web services
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_report);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212121")));
        getSupportActionBar().setTitle("Quick Report");



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

        // return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Log.d("ME", "onBackPressed Called &/cancelling timer trigger");
        this.finish();

    }
}
