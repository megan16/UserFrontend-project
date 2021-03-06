package com.example.megan.uwicommunity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;



public class SafetyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String URL ="https://projectcomp3990.herokuapp.com/crimeReports";
    private String type=null;
    private String desc;
    private String loc;
    private String date;
    private String lng;
    private String lat;
    private File pic=null;
    private CrimeBaseAdapter adapter;
    private ListView list;

    HashMap<String,String> reportsMap;
    private String description;
    private String location=null;
    private ArrayList <HashMap<String,String>> reportList;
    private ArrayList<File> picturesList;
    private String TAG="MEG";
    private Socket mSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        webService();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list= (ListView) findViewById(R.id.listCrimeReports);
        reportList= new ArrayList<HashMap<String,String>>();
        picturesList= new ArrayList<File>();

        Intent intent=getIntent();
        String dataFromHome =intent.getStringExtra("reports");
        Log.d(TAG, "Home " + dataFromHome);


        olderCode();
        mSocket.connect();
        if(mSocket.connected()){
            Log.d(TAG,"Scoket is connected");
        }
        //connectWebSocket();
        //attemptSend();

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



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.uploadfab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                navToUpload(v);
            }
        });


    }

    private void olderCode() {
        try {
             mSocket = IO.socket("https://projectcomp3990.herokuapp.com");
            mSocket.emit("crimereports", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "Received reports: "+args[0]);
                }
            });




        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /* ================================ Web Socket ======================================
     private void connectWebSocket(){

         try {
             Socket socket= IO.socket("https://projectcomp3990.herokuapp.com");

           //  socket.connect();
//             socket.emit("info", new Emitter.Listener() {
//                 @Override
//                 public void call(Object... args) {
//                     Log.d(TAG, "inside report on");
//                     Log.d(TAG, "Received: " + args[0]);
//                 }
//             });
//
//             socket.on("info", new Emitter.Listener() {
//                 @Override
//                 public void call(Object... args) {
//                     Log.d(TAG, "OB: " + args[0]);
//                 }
//             });

             socket.on("crimereports", new Emitter.Listener() {
                 @Override
                 public void call(Object... args) {
                     Log.d(TAG, "inside report on");
                     Log.d(TAG, "Received: " + args[0]);
                 }
             });
//testing this
            socket.connected();
             if(socket.connected()){
                 Log.d(TAG,"Socket is connected");
             }else{

                 Log.d(TAG,"Socket is not connected");
             }


         } catch (URISyntaxException e) {
             e.printStackTrace();
         }


     }

    private void attemptSend(){

        Log.d(TAG, "In attempt send");
        try {

            final Socket socket = IO.socket("https://projectcomp3990.herokuapp.com");

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    //socket.emit("info");
                    Log.d(TAG, "Received: " + args[0]);


                }
            }).on("crimerepots", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "Received: " + args[0]);
                }
            }).on("info", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "Received info: " + args[0]);
                }
            });

            socket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

*/

    public void navToUpload(View v){
        Log.d("MEG","I was called");
        Intent intent= new Intent(this,UploadCrime.class);
        startActivity(intent);
        //this.finish();
    }



    @Override
    protected void onResume(){
        webService();
        super.onResume();
        if(isNetworkAvailable(SafetyActivity.this)){
            //list.setAdapter(null);//clean all data
            if(!mSocket.connected()){
                mSocket.connect();
            }

            //add to screen
            if(adapter!=null)
            adapter.notifyDataSetChanged();

        }
    }

    private void webService() {
        Log.d("MEG", "In web services created my json objects before: " );
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest stringRequest = new JsonArrayRequest(URL,new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                type=null;
                desc=null;
                loc=null;
                pic=null;

                getReportsFromServer(response);
               // Log.e("MEG", " response from server: " + response.toString());

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



//    public void webService() {
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams requestParams;
//        Log.d("MEG", "Web services to retrieved called");
//        //client.addHeader("Content-Type", "application/json; charset=utf-8");
//        client.get(this,URL,new JsonHttpResponseHandler() {
//
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("MEG", "Success code: " + statusCode);
//                if(statusCode==200){
//                   // String crimeType=response.toString();
//                    Log.d("MEG","Success JsonObject");
//                    list.setAdapter(null);//clean all data
//
//                }
//            }
//
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                //String respArray = response.toString();
//           //     Log.d("MEG", "onSuccess Array: " + respArray.toString());4
//                type=null;
//                desc=null;
//                loc=null;
//                pic=null;
//
//                getReportsFromServer(response);
//
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
//                Toast.makeText(getApplicationContext(), "" + statusCode + ".." + response, Toast.LENGTH_SHORT).show();
//                Log.d("MEG", "Failure Status Code " + statusCode+".."+response);
//
//                if(statusCode==500){
//                    Toast.makeText(getApplicationContext(),"Ensure that ID doesnt already exist",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    private void getReportsFromServer(JSONArray response){
        JSONObject report = null;
        String user="";
        byte [] stringToImage=null;
        String image=null;

        for(int i=0; i<response.length();i++) {
            try {
                report = (JSONObject)response.getJSONObject(i);
                image=report.getString("picture");
    //            description=""+report.getString("desc");
                location=""+report.getString("location");
                type=""+report.getString("type");
      //          date= ""+report.getString("date");
//                lng= "Long: "+report.getString("longitude");
  //              lat="Lat: "+report.getString("latitude");
                Log.d("MEG", "Description: " + description);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //SET STRING FOR LOG DATA HERE (not setting inside try catch)
            //user= "Num:"+i+"\n"+description+"\n"+location+"\n"+type+"\n"+date+"\n"+lng+" "+lat+"\n\n";
            File perpDir= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Perps");
            if(!perpDir.exists()){
                if(!perpDir.mkdirs()){
                    Log.d("MEG", "Failed to create directory: " + perpDir.getPath());
                }
            }

            if(image!=null)
            stringToImage= Base64.decode(image,Base64.DEFAULT);
            pic= new File(perpDir.getAbsolutePath()+File.separator+"Suspect_"+i);
            if(stringToImage!=null){

                try {
                    FileOutputStream fos=new FileOutputStream(pic);
                    fos.write(stringToImage);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            //TODO: Put inside of quick Report inside of sending and checking N/A
            // when a quick report is made there is long and lat cords
            if(location.equalsIgnoreCase("n/a")){
                Intent intent= getIntent();
                new GetAddress(SafetyActivity.this).execute(intent.getStringExtra("latitude"),
                        intent.getStringExtra("longitude"));
            }

            reportsMap=new HashMap<String,String>();
        //    reportsMap.put("desc",description);
            reportsMap.put("loc", location);
            reportsMap.put("type", type);
        //    reportsMap.put("date", date);
           // if(!lng.isEmpty() && !lat.isEmpty()){
          //      reportsMap.put("lng",lng);
            //    reportsMap.put("lat", lat);
            //}

            reportList.add(reportsMap);
            picturesList.add(pic); //add corresponding picture to list
            //if if null we put as we use a default picture in said case
            //Log.d("MEG","Report: "+user);
        }
//        for(Map.Entry<String,String>entry : reports.entrySet() ){
//            Log.i("MEG",entry.getKey()+" : "+entry.getValue());
//        }

        adapter= new CrimeBaseAdapter(SafetyActivity.this,picturesList,reportList);
        list.setAdapter(adapter);


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
            Intent safetyIntent=getIntent();
            intent.putExtra("latitude",safetyIntent.getStringExtra("latitude"));
            intent.putExtra("longitude",safetyIntent.getStringExtra("longitude"));
            startActivity(intent);
            //finish();

        } else if (id == R.id.nav_police) {
            Intent intent = new Intent(this,EmergencyContact.class);
            startActivity(intent);

        } else if (id == R.id.nav_med) {
            Intent intent = new Intent(this,MedicalContact.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
