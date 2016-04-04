package com.example.megan.uwicommunity;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Megan on 03/04/2016.
 */
public class EventsBaseAdapter extends BaseAdapter {
    private static ArrayList pic,data;
    private Context context;
    private static LayoutInflater inflater=null;
    private String path="";


    public EventsBaseAdapter(Context context, ArrayList b, ArrayList c){
        this.context=context;
        this.pic=new ArrayList();//b;
        pic=b;
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
            vi = inflater.inflate(R.layout.event_list, null);

        ImageView imageView= (ImageView) vi.findViewById(R.id.eImg);
        File imgFile= (File) pic.get(position);
        if(imgFile.exists()){

            imageView.setImageURI(Uri.fromFile(imgFile));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        }else{
            imageView.setImageResource(R.drawable.user1);
        }


        HashMap map;
        map =(HashMap)data.get(position) ;

//        TextView title= (TextView) vi.findViewById(R.id.eTitle);
//        String eTitle= map.get("title").toString();
//        title.setText(eTitle);

        TextView descView = (TextView) vi.findViewById(R.id.descView); // title
        descView.setText(map.get("desc").toString());

//        TextView contactNum= (TextView) vi.findViewById(R.id.eContact);
//        String location= map.get("contact").toString();
//        contactNum.setText(location);

        return vi;
    }
}
