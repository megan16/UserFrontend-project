package com.example.megan.uwicommunity;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.location.places.Place;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Megan on 30/03/2016.
 */

//TODO: get location on server side
public class GetAddress extends AsyncTask<String,Void,String> {

    private Activity activity;

    public GetAddress(Activity activity){
        super();
        this.activity=activity;
    }

    @Override
    protected String doInBackground(String... params) {
        Geocoder geocoder;
        List <Address> address;
        String place = null;
        geocoder=new Geocoder(activity, Locale.getDefault());

        try {
            address=geocoder.getFromLocation(Double.parseDouble(params[0]), Double.parseDouble(params[1]), 1);
            place=address.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return place;
    }
}
