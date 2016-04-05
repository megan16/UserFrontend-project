package com.example.megan.uwicommunity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EventsActivity extends AppCompatActivity {
    private static final String URL ="https://projectcomp3990.herokuapp.com/events";
    private File pic=null;
    private EventsBaseAdapter adapter;
    private ListView list;
    private String desc;
    private HashMap<String, String> reportsMap;
    private ArrayList <HashMap<String,String>> reportList;
    private ArrayList<File> picturesList;
    private String TAG="MEG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                navToUpload(v);
            }
        });
    }

    private void navToUpload(View v) {
        Log.d("MEG","I was called");
        Intent intent= new Intent(this,UploadEvent.class);
        startActivity(intent);
    }


    @Override
    protected void onResume(){
        webService();
        super.onResume();
        if(isNetworkAvailable(EventsActivity.this)){
            //list.setAdapter(null);//clean all data

            //add to screen
            if(adapter!=null)
                adapter.notifyDataSetChanged();

        }
    }
/* ====================================== Volley Web Service to retrieve events ================== */
    private void webService() {
        Log.d("MEG", "In web services created my json objects before: ");
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest stringRequest = new JsonArrayRequest(URL,new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                desc=null;
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

    private void getReportsFromServer(JSONArray response){
        JSONObject report = null;
        String user="";
        byte [] stringToImage=null;
        String image=null;
        String title=null,contact=null;
        desc=null;

        for(int i=0; i<response.length();i++) {
            try {
                report = (JSONObject)response.getJSONObject(i);
                image=report.getString("picture");
                title=report.getString("title");
                desc=report.getString("desc");
                contact=report.getString("contact");

                Log.d("MEG", "Description: " + desc);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //SET STRING FOR LOG DATA HERE (not setting inside try catch)
            //user= "Num:"+i+"\n"+description+"\n"+location+"\n"+type+"\n"+date+"\n"+lng+" "+lat+"\n\n";
            //TODO: add folder to uwi folder inside of pictures or decide whether or not we going to do this
            File eventDir= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Events");
            if(!eventDir.exists()){
                if(!eventDir.mkdirs()){
                    Log.d("MEG", "Failed to create directory: " + eventDir.getPath());
                }
            }

            if(image!=null)
                stringToImage= Base64.decode(image, Base64.DEFAULT);

            pic= new File(eventDir.getAbsolutePath()+File.separator+"Events_"+i);
            if(stringToImage!=null){

                try {
                    FileOutputStream fos=new FileOutputStream(pic);
                    fos.write(stringToImage);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //reset
                desc=null;
                title=null;

            }

            reportsMap=new HashMap<String,String>();
            reportsMap.put("title",title);
            reportsMap.put("desc", desc);
            reportsMap.put("contact",contact);

            reportList.add(reportsMap);
            picturesList.add(pic); //add corresponding picture to list
            //if if null we put as we use a default picture in said case
            //Log.d("MEG","Report: "+user);
        }
//        for(Map.Entry<String,String>entry : reports.entrySet() ){
//            Log.i("MEG",entry.getKey()+" : "+entry.getValue());
//        }

        adapter= new EventsBaseAdapter(EventsActivity.this,picturesList,reportList);
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
        this.finish();
    }

}
