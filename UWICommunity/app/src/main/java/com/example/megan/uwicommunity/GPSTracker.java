package com.example.megan.uwicommunity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Megan on 22/03/2016.
 */
public class GPSTracker extends Service implements LocationListener {

    private static final int REQUEST_CODE_LOCATION = 2;
    private final Context context;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    private static final long distanceBeforeUpdate = 10; // meters
    private static final long minsBeforeUpdate = 1; // 1 min

    protected LocationManager locationManager;

    //contructor
    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }

    private Location getLocation() {

        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            //get GPS stats
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getApplicationContext(), "Cannot access network provider", Toast.LENGTH_LONG).show();

            } else {
                this.canGetLocation = true; //will get from the network provider 1st
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                minsBeforeUpdate, distanceBeforeUpdate,(LocationListener) this);
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            minsBeforeUpdate, distanceBeforeUpdate,(LocationListener) this);

                    Log.d("MEG", "Network accessible");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                    //get If GPS is enabled
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                    minsBeforeUpdate, distanceBeforeUpdate, this);
                            Log.d("MEG", "Gps is accesible");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    /* ############################ GETTERS ###################################### */

    public double getLatitude() {
        if (location != null)
            latitude = (double)location.getLatitude();
        Log.d("MEG","In getter Lat: "+latitude);
        return latitude;
    }

    public double getLongitude() {
        if (location != null)
            longitude = location.getLongitude();
        return longitude;
    }

    /* ################################ Check user GPS ################################## */

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    // Let them turn on GPS
    public void GPSalert() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this.context);
        ad.setTitle("GPS Settings");
        ad.setMessage("GPS is not enabled. Do you wish to enable it?");
        ad.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        ad.show();

    }

    // Stop updates
    public void stopGPSUpdates() {
        if (locationManager != null) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    locationManager.removeUpdates(GPSTracker.this);
                    return;
                }
                locationManager.removeUpdates(GPSTracker.this);

        }
    }



/* ############################################################################################# */
    @Override
    public void onLocationChanged(Location location) {
        double lat = (double) (location.getLatitude());
        double longit = (double) (location.getLongitude());
        Log.d("MEG", "$Co-ord: " + longit + " " + lat);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // success!
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Display UI and wait for user interaction
                    } else {
                        ActivityCompat.requestPermissions(
                                (Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_LOCATION);
                    }
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        minsBeforeUpdate, distanceBeforeUpdate, (LocationListener)this);
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }
}
