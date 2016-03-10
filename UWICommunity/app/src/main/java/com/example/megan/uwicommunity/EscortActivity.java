package com.example.megan.uwicommunity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import static com.example.megan.uwicommunity.R.drawable.alarmbutton2;
import static com.example.megan.uwicommunity.R.id.alarmButton;

public class EscortActivity extends AppCompatActivity  {

    private static final String format = "%02d:%02d";
    private CountDownTimer cdt;
    private boolean flagTimer;
    private boolean finished;
    private int countTaps = 0;

    private Button wb;
    private AlertDialog walkBuddyDialog;


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

        wb= (Button) findViewById(R.id.walkBud);
        walkBudListener();  // call button onclick listener function

    }

    public void setTimer(View view) {
        final Button button = ((Button) findViewById(alarmButton));
        Button abort= (Button)findViewById(R.id.abort);
        Button panic= (Button) findViewById(R.id.panic);
        //final boolean finished=false;

        /* TODO:
            *Make button rotate
            Change button into progress bar circular
            put text in text view
         */

//        Animation rotateCenter = AnimationUtils.loadAnimation(this, R.anim.rotate_center);
//        button.startAnimation(rotateCenter);
        abort.setVisibility(View.VISIBLE);
        panic.setVisibility(View.VISIBLE);
        countTaps++;

        if(countTaps<=1) {

            startTimer(view,button);
        }

        //}// else reset to extend time or reset time
        cdt.cancel(); //cancel any possible existing timer
        startTimer(view, button); // restart a new timer
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
        final String[] items={"Male","Female"};
        AlertDialog.Builder builder= new AlertDialog.Builder(EscortActivity.this);
        int selectedItem = 0;
        builder.setTitle("Walk Buddy Preference:\n\tClick (No) if you have none.");

        //.setChoiceItems(R.array.selectedItem)
        builder.setSingleChoiceItems(items,selectedItem,null);

        builder.setPositiveButton(R.string.yesP, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //put gender in intent and send to next activity
                // do a log instead which=-1
                int getGender = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                Log.d("ME", "Yes i have a gender preference: " + items[getGender]);
                Toast.makeText(getApplicationContext(),
                        "Yes i have a gender preference" + items[getGender] +
                                " send to next activity", Toast.LENGTH_SHORT).show();
                walkBuddyDialog.dismiss();
                Intent intent=new Intent(getApplicationContext(),WalkBData.class);
                startActivity(intent);
            }
        });
//
        builder.setNegativeButton(R.string.noP, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //redirect which=-2 => no
                Log.d("ME", "Yes i have a gender preference: " + which);
                Toast.makeText(getApplicationContext(), "Redirecting..", Toast.LENGTH_SHORT).show();
                walkBuddyDialog.dismiss();
                Intent intent=new Intent(getApplicationContext(),WalkBData.class);
                startActivity(intent);
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
