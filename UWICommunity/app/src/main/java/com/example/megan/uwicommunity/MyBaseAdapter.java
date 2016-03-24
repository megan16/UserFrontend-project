package com.example.megan.uwicommunity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Megan on 24/03/2016.
 */
public class MyBaseAdapter extends android.widget.BaseAdapter {

    private static ArrayList pic,type,loc;
    private Activity activity;
    private static LayoutInflater inflater=null;
    private String path="";


    public MyBaseAdapter(Activity a, ArrayList b, ArrayList c,ArrayList d){
        activity=a;
        this.pic=new ArrayList();//b;
        this.type=c;
        loc=d;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return type.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.crime_reports, null);

        ImageView imageView= (ImageView) vi.findViewById(R.id.crImg);
        File imgFile= new File((String) pic.get(position));
        if(imgFile.exists()){

            imageView.setImageURI(Uri.fromFile(imgFile));
        }

        TextView type2 = (TextView) vi.findViewById(R.id.crType); // title

        String crimeType = type.get(position).toString();
        type2.setText(crimeType);

        TextView loc2= (TextView) vi.findViewById(R.id.crLoc);
        String location= loc.get(position).toString();
        loc2.setText(location);

        return vi;
    }
}
