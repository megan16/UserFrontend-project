package com.example.megan.uwicommunity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Megan on 22/03/2016.
 */
@SuppressWarnings("ALL")
public class GPSTracker  {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION =99 ;
    private Location location;
    LocationListener locationListener;
    public LocationManager locationManager;
    private GPSActivity main;
    private boolean isGPSEnabled=false;
    private boolean isNetwork= false;
    private boolean isRunning;
    private Context context;
    private AlertDialog alert;

    public GPSTracker(GPSActivity main,Context context) {
        this.main=main;
        this.context=context;
        locationManager = (LocationManager)((Activity)this.main).getSystemService(Context.LOCATION_SERVICE);
        locationListener= new MyLocationListener();
        isGPSEnabled=false;
        getLocationMethod();

    }

    //determins if to user network or GPS
    private void getLocationMethod(){
        //if(locationManager!=null)
        isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //isNetwork=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(isGPSEnabled){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,40,locationListener);
            this.isRunning=true;
            isNetwork=false;
        }else if(isGPSEnabled==false){
            //not enabled ask to turn on
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1,40,locationListener);
            //isNetwork=true;
            isGPSEnabled=false;
            this.isRunning=true;
            GPSalert();
        }

    }

    public void resumeGetLocation(){
        isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
       // isNetwork=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(isGPSEnabled){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,40,locationListener);
            this.isRunning=true;
            Log.d("MEG","Using GPS");
        }else if(isGPSEnabled==false){
            //not enabled ask to turn on
            Log.d("MEG", "Still using network ");
            //GPSalert(); //dont alert on resumed app ask only once
           // isGPSEnabled=false;
            isRunning=true;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 40, locationListener);
        }

    }

    public void stopGPS(){
        if(isRunning){
            locationManager.removeUpdates(locationListener);
            this.isRunning=false;
        }
    }

    public boolean isRunning(){
        return this.isRunning;
    }

    public void GPSalert() {
        isGPSEnabled=false;
        isNetwork=false;
        AlertDialog.Builder ad = new AlertDialog.Builder(this.context);
        ad.setTitle("GPS Settings");
        ad.setMessage("GPS is not enabled. (It is recommended) Do you wish to enable it?" +
        " If not your Network Provider will be used. Please ensure your Wi-Fi is turned on for more accuracy. ");
        ad.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
                resumeGetLocation();
            }
        });

        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //use network if avail
                Log.d("MEG","cancel called");
                isGPSEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                resumeGetLocation();
                dialog.cancel();
            }
        });
        alert=ad.create();
        alert.show();

    }






    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            GPSTracker.this.main.locationChanged(location.getLongitude(),location.getLatitude());
            if(location==null){
                Toast.makeText(context,"Location not available. Ensure GPS is on or connected to" +
                        " Wi-Fi Network",Toast.LENGTH_LONG).show();

            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if(provider.equalsIgnoreCase("network")){
                GPSTracker.this.main.displayGPSDialog();
            }

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
//            Toast.makeText(this, "Disabled provider " + provider,
//                    Toast.LENGTH_SHORT).show();

        }
    }
}
