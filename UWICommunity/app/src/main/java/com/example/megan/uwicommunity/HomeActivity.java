package com.example.megan.uwicommunity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class HomeActivity extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_CODE_LOCATION = 2;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Button reportBtn;
    private ImageButton safetyBtn;
    private ImageButton eventsBtn;
    private ImageButton tradeBtn;
    private ImageButton activitiesBtn;
    private GPSTracker gps;
    private LocationManager locationManager;
    private String provider;

//    private GoogleMap map;
//    private GoogleApiClient googleApiClient;
//    private LocationRequest locationRequest;
//    private Location lastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.ic_uwi);
        actionbar.setTitle("STA Connected");

        reportBtn = (Button) findViewById(R.id.quickReportBtn);
        safetyBtn = (ImageButton) findViewById(R.id.safetyButton);
        eventsBtn= (ImageButton) findViewById(R.id.eventsBtn);
        tradeBtn= (ImageButton) findViewById(R.id.tradesBtn);
        activitiesBtn= (ImageButton) findViewById(R.id.activities);
        /* ################################ MAP #################################### */
        getLatLng();

        //go to quick report listener
        navToQuickReport();
        navToEvents();
        navToTrade();
        navToActivities();





    }


    /* ################################# NAV FUNCTIONS ########################################### */
    private void navToActivities() {
      activitiesBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Toast.makeText(getApplication(), "Under Construction", Toast.LENGTH_LONG).show();
          }
      });
    }

    private void navToTrade() {
        tradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Under Construction", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void navToEvents() {
        eventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Under Construction", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void navToQuickReport(){
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuickReport.class);
                startActivity(intent);
            }
        });

    }

    public void navToSafety(View view){
        Intent intent = new Intent(this, SafetyActivity.class);
        startActivity(intent);
        //finish();
    }



    /* ######################### LOCATION FUNCTIONS ################################## */

    private void getLatLng(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Location location = locationManager.getLastKnownLocation(provider);
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if(location!=null){
            Log.d("MEG","Provider: "+provider);
            onLocationChanged(location);
        }
        else{
            Toast.makeText(getApplicationContext(),"Location not available",Toast.LENGTH_LONG).show();

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
            return;
        }

        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
    }




    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            locationManager.removeUpdates((LocationListener) this);
            return;
        }


        locationManager.removeUpdates((LocationListener) this);
//        gps.stopGPSUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = (double) (location.getLatitude());
        double longit = (double) (location.getLongitude());
      //  latituteField.setText(String.valueOf(lat));
        //longitudeField.setText(String.valueOf(lng));


            Log.d("MEG", "$Co-ord: " + longit + " " + lat);
       // Toast.makeText(getApplicationContext(),"longit: "+longit+"lat: "+lat,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }


   /* ###################################################################################### */

    @Override
    public void onBackPressed() {
        Log.d("ME", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);

        super.onBackPressed();
    }


//
//    public void exit(View view){ // stays logged in
//        moveTaskToBack(true);
//        WelcomePage.this.finish();
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }




    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // success!
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Display UI and wait for user interaction
                    } else {
                        ActivityCompat.requestPermissions(
                                this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_LOCATION);
                    }
                }
                Location location = locationManager.getLastKnownLocation(provider);
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }


}
