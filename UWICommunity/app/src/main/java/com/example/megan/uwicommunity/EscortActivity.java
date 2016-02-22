package com.example.megan.uwicommunity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import static com.example.megan.uwicommunity.R.drawable.alarmbutton;
import static com.example.megan.uwicommunity.R.drawable.round_button;
import static com.example.megan.uwicommunity.R.id.alarmButton;

public class EscortActivity extends AppCompatActivity  {

    private static final String format = "%02d:%02d";
    private CountDownTimer cdt;
    private boolean flagTimer;
    private boolean finished;
    private int countTaps = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escort);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        flagTimer=false;
        finished=false;
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();


        //getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }

    public void setTimer(View view) {
        final Button button = ((Button) findViewById(alarmButton));
        Button abort= (Button)findViewById(R.id.abort);
        //final boolean finished=false;

//         make button rotate
        /* TODO
            Change button into progress bar circular
            put text in text view
         */

//        Animation rotateCenter = AnimationUtils.loadAnimation(this, R.anim.rotate_center);
//        button.startAnimation(rotateCenter);
        abort.setVisibility(View.VISIBLE);
        countTaps++;

        if(countTaps<=1) {

            startTimer(view,button);
        }

        //}// else reset to extend time or reset time
        cdt.cancel(); //cancel any possible existing timer
        startTimer(view,button); // restart a new timer
        //return;
    }

    public void startTimer(View view, final Button button){
        /////final Button button = ((Button) findViewById(alarmButton));
        flagTimer=true; //flag to cancel timer incase user accidentally starts button
        cdt = new CountDownTimer(120000, 1000) {

            public void onTick(long milliToFini) {
                button.setText("" + String.format(format, TimeUnit.MILLISECONDS.toMinutes(milliToFini),
                        TimeUnit.MILLISECONDS.toSeconds(milliToFini) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((milliToFini)))
                ));


                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);

            }

            @Override
            public void onFinish() {
                button.setBackgroundResource(alarmbutton);
                button.setText("Alerting Campus Police");
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                //button.setRotation(0f);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callPolice();
                    }
                }, 2000);
            }
        }.start();
    }


    public  void abortFunction(View view){
        Button abort= (Button)findViewById(R.id.abort);
        abort.setVisibility(View.INVISIBLE);
        //restore alarm button to original state(push to start
//        Button alarmbtn= (Button)findViewById(alarmButton);
//        alarmbtn.setBackgroundResource(round_button);
//        alarmbtn.setText("Push to Start");
//        alarmbtn.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
    }

    @Override
    public void onBackPressed() {

        Log.d("ME", "onBackPressed Called &/cancelling timer trigger");
        if(flagTimer)
        cdt.cancel(); // cancel cdt just in case user clicked button to cancel trigger

        this.finish();

    }

    private void callPolice() {
        final String phoneNo = "tel:8686408650";

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNo));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_home, menu);
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

       // return super.onOptionsItemSelected(item);
    }


}
