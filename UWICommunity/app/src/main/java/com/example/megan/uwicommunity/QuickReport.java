package com.example.megan.uwicommunity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class QuickReport extends AppCompatActivity {
//TODO: web services
    private Button addPic;
    private Button addVid;
    private Button addAud;
    private Button submit;
    private String TAG="MEG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_report);

        if(getSupportActionBar()!=null)
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212121")));
        getSupportActionBar().setTitle("Quick Report");

        addPic= (Button) findViewById(R.id.addpicbtn);
        submit= (Button) findViewById(R.id.submitBtn);
        onSubmit();


    }

    private void onSubmit() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // broadcast notification
                Log.d(TAG,"Submit button clicked");
                notificationTest();
            }
        });
    }

    private void notificationTest() {
        final int nid=0;
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent= new Intent(this, SafetyActivity.class); //view the list of reports
        intent.putExtra("notifId",nid);
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, intent, 0);

        Intent cancel= new Intent(getBaseContext(),ButtonReceiver.class);
        PendingIntent pCancel= PendingIntent.getBroadcast(getApplicationContext(),0,cancel,0);

        Notification notification= new NotificationCompat.Builder(this)
                .setWhen(0)
                .setContentTitle("Crime Bulletin")
                .setContentText("Details of a crime goes here")
                .setSmallIcon(R.drawable.user1)
                .setContentIntent(pendingIntent)
                .setLights(Color.RED, 300, 300)
                .setOngoing(false)
                .addAction(0, "Dismiss", pCancel)
                .build();
                //.addAction(R.drawable.common_plus_signin_btn_icon_dark_normal,"Accept",pendingIntent);
//.setPriority(Notification.PRIORITY_MAX) => for api 15 ^
       // notification.flags |=Notification.FLAG_AUTO_CANCEL;
        //notification.flags |=Notification.FLAG_ONLY_ALERT_ONCE;
        notification.flags |= Notification.DEFAULT_SOUND;
        notification.flags |=Notification.DEFAULT_VIBRATE;
        notification.flags |=Notification.FLAG_SHOW_LIGHTS;
        //notification.build();


        notificationManager.notify(nid,notification);



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

        Log.d("ME", "onBackPressed Called ");
        this.finish();

    }
}
