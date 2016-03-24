package com.example.megan.uwicommunity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SafetyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String URL ="https://projectcomp3990.herokuapp.com/crimeReports";
    private String type;
    private String desc;
    private String loc;
    private String date;
    private String lng;
    private String lat;
    private String pic;
    private MyBaseAdapter adapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // list= (ListView) findViewById(R.id.listCrimeReports);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Notice to user on how to make a crime report
        Snackbar.make(findViewById(R.id.coordinatorLayout),"Click the + to report a crime", Snackbar.LENGTH_SHORT).show();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.upload);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Redirecting..",Toast.LENGTH_SHORT);
                navToUpload();
            }
        });

        webService();
    }

    public void navToUpload(){
        Intent intent= new Intent(this,UploadCrime.class);
        startActivity(intent);
        //this.finish();
    }

    // get updates
    protected void onResume(){
        super.onResume();
        if(isNetworkAvailable(SafetyActivity.this)){
            webService();
            //add to screen

        }
    }

    public void addToScreen(){
        LayoutInflater factory= LayoutInflater.from(SafetyActivity.this);
        final View view=factory.inflate(R.layout.crime_reports, null);
        ArrayAdapter listAdapter=new ArrayAdapter<String>(this,R.layout.crime_reports);
        ListView listView= (ListView) findViewById(R.id.tipsList);
        listView.setAdapter(listAdapter);
    }

    public void webService() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams;
        Log.d("MEG", "Web services to retrieved called");
        //client.addHeader("Content-Type", "application/json; charset=utf-8");
        client.get(this,URL,new JsonHttpResponseHandler() {
//
//            @Override
//            public void setUseSynchronousMode(boolean value) {
//                super.setUseSynchronousMode(true);
//            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("MEG", "Success code: " + statusCode);
                if(statusCode==200){
                   // String crimeType=response.toString();
                    Log.d("MEG","Success JsonObject");


                }
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                String respArray = response.toString();
                JSONObject report;
           //     Log.d("MEG", "onSuccess Array: " + respArray.toString());

                    try {
                        for(int i=0; i<=response.length();i++) {
                            report = (JSONObject)response.getJSONObject(i);
                            Log.d("MEG", "onSuccess Array: " +response.toString() );

                            ImageView imageView= (ImageView)findViewById(R.id.crImg);
                            File imgFile= new File((String) response.getJSONObject(i).get("pic").toString());
                            if(imgFile.exists()){

                                imageView.setImageURI(Uri.fromFile(imgFile));
                            }

                            desc = response.getJSONObject(i).get("desc").toString();

                            TextView loc2= (TextView)findViewById(R.id.crLoc);
                            loc2.setText(response.getJSONObject(i).get("loc").toString());
//status
                            lat =response.getJSONObject(i).get("latitude").toString();
                            lng = response.getJSONObject(i).get("longitude").toString();
                            TextView type2 = (TextView)findViewById(R.id.crType); // title
                            type2.setText(response.getJSONObject(i).get("type").toString());




                            //TODO:Picture properly
                            //type = response.getJSONObject(i).get("type").toString();

                            date = response.getJSONObject(i).get("date").toString();
                            //loc = response.getJSONObject(i).get("loc").toString();

                         //   pic = response.getJSONObject(i).get("picture").toString();
                            Log.d("MEG", "We here: "+type + "," + desc + "");

                            //(create list and pass them to this
//                        ArrayList pics= new ArrayList();
//                        pics.add(pic);
//                        ArrayList types= new ArrayList();
//                        types.add(type);
//                        ArrayList locs= new ArrayList();
//                        locs.add(loc);
//
//                        adapter = new MyBaseAdapter(SafetyActivity.this,pics,types,locs);
//                        list.setAdapter(adapter);
                            //get picture
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
                Toast.makeText(getApplicationContext(), "" + statusCode + ".." + response, Toast.LENGTH_SHORT).show();
                Log.d("MEG", "Failure Status Code " + statusCode+".."+response);

                if(statusCode==500){
                    Toast.makeText(getApplicationContext(),"Ensure that ID doesnt already exist",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try{
            NetworkInfo activeNetwork= connectivity.getActiveNetworkInfo();
            if(activeNetwork!=null && activeNetwork.isConnected()){
                return true;
            }
        }catch (Exception e){
            Log.d("MEG", "Network available: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            Intent intent=new Intent(this,HomeActivity.class);
            startActivity(intent);
            this.finish();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tips) {

            Intent intent = new Intent(this,TipsActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_escort) {
            Intent intent = new Intent(this,EscortActivity.class);
            startActivity(intent);
            //finish();

        } else if (id == R.id.nav_police) {
            Intent intent = new Intent(this,EmergencyContact.class);
            startActivity(intent);

        } else if (id == R.id.nav_med) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
