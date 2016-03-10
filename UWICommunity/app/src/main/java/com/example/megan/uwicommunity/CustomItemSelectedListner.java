package com.example.megan.uwicommunity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by Megan on 10/03/2016.
 */
public class CustomItemSelectedListner implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(parent.getContext(),"Selected: "+parent.getItemAtPosition(position).toString(),
          //      Toast.LENGTH_LONG).show();
        //write to log
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
            Log.d("ME", "They selected something: Invalid must select something");
    }
}
