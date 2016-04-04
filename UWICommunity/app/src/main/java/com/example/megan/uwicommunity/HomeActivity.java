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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
//TODO: Implement location using external class

public class HomeActivity extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_CODE_LOCATION = 2;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final String URL = "https://projectcomp3990.herokuapp.com/crimeReports" ;
    private JSONArray reports;

    private Button reportBtn;
    private ImageButton safetyBtn;
    private ImageButton eventsBtn;
    private ImageButton tradeBtn;
    private ImageButton activitiesBtn;
    private GPSTracker gps;
    private LocationManager locationManager;
    private Location location;
    private String provider;

    private GoogleMap map;
    private Marker marker=null;

    //to move to login screen
    private TextView changePass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null){
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setIcon(R.mipmap.ic_uwi);
            actionbar.setTitle("STA Connected");
        }

      //  webService();
        //TODO: to move to login
        changePass= (TextView) findViewById(R.id.changepass);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),ChangePassword.class);
                startActivity(intent);
            }
        });

        reportBtn = (Button) findViewById(R.id.quickReportBtn);
        safetyBtn = (ImageButton) findViewById(R.id.safetyButton);
        eventsBtn = (ImageButton) findViewById(R.id.eventsBtn);
        tradeBtn = (ImageButton) findViewById(R.id.tradesBtn);
        activitiesBtn = (ImageButton) findViewById(R.id.activities);

        /* ################################ MAP #################################### */
        getLatLng();
       // replaceMap();

        //go to quick report listener
        navToQuickReport();
        navToSafety();
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

    public void navToSafety(){
        safetyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SafetyActivity.class);
                if(reports!=null)
                intent.putExtra("reports",reports.toString());
                startActivity(intent);
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



    /* ##################################### VOLLEY FUNCTIONS ###################################
    public void webService() {
        Log.d("MEG", "In web services created my json objects before: " );
        RequestQueue queue = Volley.newRequestQueue(this);


        int code=0;
        JsonArrayRequest stringRequest = new JsonArrayRequest(URL,new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        reports=response;
                        Log.e("MEG", " response from server: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // hide progress dialog
                        // VolleyLog.d("ME", "Error: " + error.getMessage());
                        NetworkResponse networkResponse = error.networkResponse;
                        int sc = 0;
                        if(networkResponse!=null && networkResponse.data!=null) {
                            sc = networkResponse.statusCode;
                        }

                        Log.d("MEG", "Error: " +String.valueOf(sc)+" .. "+ error.getMessage());
                        //  Toast.makeText(getApplicationContext(), "Error: " + sc, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }) {
                    @Override
                    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                        if(response!=null)
                            Log.d("MEG","Status code= "+response.statusCode);
                        if (response.statusCode == 200) {
                            Log.d("MEG","Success");

                        }
                        return super.parseNetworkResponse(response);
                    }

        };
        queue.add(stringRequest);


    }
*/
    /* ######################### LOCATION & MAP FUNCTIONS ################################## */

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
           // Location location = locationManager.getLastKnownLocation(provider);
            return;
        }
        location = locationManager.getLastKnownLocation(provider);
        if(location!=null){
            Log.d("MEG","Provider: "+provider);
            //set up map
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            //make map zoomable
            map.getUiSettings().setZoomGesturesEnabled(true);
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //TODO:
                return;
            }
            map.setMyLocationEnabled(true);

            //call on change of location
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
        if(location!=null){
            double lat = (double) (location.getLatitude());
            double longit = (double) (location.getLongitude());
            //  latituteField.setText(String.valueOf(lat));
            //longitudeField.setText(String.valueOf(lng));
            LatLng loc= new LatLng(location.getLatitude(),location.getLongitude());
            if(marker!=null)
                marker.remove();
            marker= map.addMarker(new MarkerOptions().position(loc));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            Toast.makeText(getApplicationContext(),"Long: "+longit+" Lat: "+lat,Toast.LENGTH_LONG).show();

            Log.d("MEG", "$Co-ord: " + longit + " " + lat);
            // Toast.makeText(getApplicationContext(),"longit: "+longit+"lat: "+lat,Toast.LENGTH_LONG).show();
        }
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
