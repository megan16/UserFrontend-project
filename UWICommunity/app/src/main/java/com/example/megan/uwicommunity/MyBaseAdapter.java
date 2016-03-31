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

    private static ArrayList pic,data;
    private Context context;
    private static LayoutInflater inflater=null;
    private String path="";


    public MyBaseAdapter(Context context, ArrayList b,ArrayList c){
        this.context=context;
        this.pic=new ArrayList();//b;
        pic=b;
        data=new ArrayList();
        data=c;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pic.size();
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
        File imgFile= (File) pic.get(position);
        if(imgFile.exists()){

            imageView.setImageURI(Uri.fromFile(imgFile));
        }else{
            imageView.setImageResource(R.drawable.user1);
        }

        TextView crimeView = (TextView) vi.findViewById(R.id.crType); // title
        String crimeType = data.get(position).toString();
        crimeView.setText(crimeType);

        TextView locView= (TextView) vi.findViewById(R.id.crLoc);
        String location= data.get(position).toString();
        locView.setText(location);

        return vi;
    }
}
