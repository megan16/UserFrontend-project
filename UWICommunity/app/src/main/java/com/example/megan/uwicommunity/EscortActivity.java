package com.example.megan.uwicommunity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import static com.example.megan.uwicommunity.R.drawable.alarmbutton2;
import static com.example.megan.uwicommunity.R.id.alarmButton;

public class EscortActivity extends AppCompatActivity implements GPSActivity {

    private static final String URL ="https://projectcomp3990.herokuapp.com/findBuddy";
    private static final String format = "%02d:%02d";
    private CountDownTimer cdt;
    private boolean flagTimer;
    private boolean finished;
    private int countTaps = 0;
    GPSTracker gps;
    private Button escortBtn;
    private Button wb;
    private AlertDialog walkBuddyDialog;
    //TODO: Web Services


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escort);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        flagTimer=false;
        finished=false;

        gps= new GPSTracker(this,EscortActivity.this);
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();


        //getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wb= (Button) findViewById(R.id.walkBud);
        walkBudListener();  // call button onclick listener function
        escortBtn= (Button) findViewById(R.id.alarmButton);

        // on touch listener

        escortBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    countTaps++;
                    if(flagTimer){
                        cdt.cancel();
                        flagTimer=false;
                        //user pressed button down again
                    }
                    if(countTaps==1){
                        setHelpButtonsVisible(); // to avoid multiple redraws to screen
                    }else{
                        escortBtn.setText(null);
                    }
                    return true;
                }
                else
                if(event.getAction()==MotionEvent.ACTION_UP){
                    startTimer(v,escortBtn);
                    return  false;
                }



               return false;
            }
        });




    }

    /* ######################### SETTING UP LOCATION DATA #################### */
    @Override
    public void locationChanged(double longitude, double latitude) {
        Toast.makeText(getApplicationContext(),"Long: "+longitude+" Lat: "+latitude,
                Toast.LENGTH_LONG).show();
        Log.d("MEG","Loc Changed "+"Long: "+longitude+" Lat: "+latitude);
    }

    @Override
    public void displayGPSDialog() {
        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        Log.d("MEG","ON resume called");
        if(!gps.isRunning()){
            gps.resumeGetLocation();
        }
        super.onResume();
    }

    @Override
    protected void onStop(){
        gps.stopGPS();
        super.onStop();
    }


    public void setHelpButtonsVisible(){
        Button abort= (Button)findViewById(R.id.abort);
        Button panic= (Button) findViewById(R.id.panic);

        abort.setVisibility(View.VISIBLE);
        panic.setVisibility(View.VISIBLE);
    }

    public void startTimer(View view, final Button button){
        /////final Button button = ((Button) findViewById(alarmButton));
        flagTimer=true; //flag to cancel timer incase user accidentally starts button
        cdt = new CountDownTimer(10000, 1000) {

            public void onTick(long milliToFini) {
                button.setText("" + String.format(format, TimeUnit.MILLISECONDS.toMinutes(milliToFini),
                        TimeUnit.MILLISECONDS.toSeconds(milliToFini) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((milliToFini)))
                ));


                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

            }

            @Override
            public void onFinish() {
                button.setBackgroundResource(alarmbutton2);
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


        Intent intent=new Intent(this,EscortActivity.class);
        cdt.cancel();
        this.finish();
        startActivity(intent);

    }

    public  void panicFunction(View view){
        Button panic= (Button)findViewById(R.id.panic);
       // panic.setVisibility(View.INVISIBLE);

        Toast.makeText(getApplicationContext(),"Dialing Police",Toast.LENGTH_SHORT).show();
        callPolice();
    }

    private void callPolice() {
        final String phoneNo = "tel:8686622022,83510";

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

/* ============================================== WALK BUDDY =================================================================== */


    public void walkBudListener(){
        wb.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "No functionality yet", Toast.LENGTH_SHORT).show();
                //set up alerts
                setUpBudDialog();
                // walkBuddyDialog.show();

            }
        });

    }

    public void setUpBudDialog(){
        final String[] items={"Male","Female","None"};
        AlertDialog.Builder builder= new AlertDialog.Builder(EscortActivity.this);
        int selectedItem = 0;
        builder.setTitle("Please choose a gender preference for your Walk Buddy: ");

        //.setChoiceItems(R.array.selectedItem)
        builder.setSingleChoiceItems(items,selectedItem,null);

        builder.setPositiveButton(R.string.okP, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //put gender in intent and send to next activity
                // do a log instead which=-1
                int getGender = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                Log.d("ME", "Gender preference: " + items[getGender]);
                Toast.makeText(getApplicationContext(),
                        "Gender preference: " + items[getGender] +
                                " send to next activity", Toast.LENGTH_SHORT).show();
                walkBuddyDialog.dismiss();
                Intent intent=new Intent(getApplicationContext(),WalkBData.class);
                startActivity(intent);
            }
        });

//
        builder.setNegativeButton(R.string.canP, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //redirect which=-2 => no
                Log.d("ME", "Cancelling walk buddy ");

                walkBuddyDialog.cancel();

            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d("ME","Cancelling Dialog");
            }
        });

        builder.setCancelable(true);

        walkBuddyDialog=builder.create();
        walkBuddyDialog.show();
    }



/* ============================================= Built-in Functions ============================================================ */

    @Override
    public void onBackPressed() {

        Log.d("ME", "onBackPressed Called &/cancelling timer trigger");
        if(flagTimer)
        cdt.cancel(); // cancel cdt just in case user clicked button to cancel trigger


        // if walkbuddy dialog was launched
//        walkBuddyDialog.dismiss();

        this.finish();

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

        //Intent intent=new Intent(getApplicationContext(),SafetyActivity.class);
        //startActivity(intent);
        this.finish();
        return true;

       // return super.onOptionsItemSelected(item);
    }



}
