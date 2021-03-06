package com.example.megan.uwicommunity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UploadCrime extends AppCompatActivity {
    private static final String URL ="https://projectcomp3990.herokuapp.com/addCrime" ;
    private Spinner locList;
    private Spinner crimeTypeList;
    private RadioGroup imageChoiceRadioGroup;
    private RadioButton answerRadioButton;
    private ImageView pictureUpload;
    private FloatingActionButton uploadButton;
    private String fileURI=null;
    private final int CAMERA_CAPTURE_REQUEST_CODE=100;
    private final int GALLERY_REQUEST_CODE=200;
    private int cameraOrGalleryFlag;
    private Bitmap photo=null;

    private EditText desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_crime);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF1A1A")));
        getSupportActionBar().setTitle("Upload Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        crimeTypeList= (Spinner) findViewById(R.id.crimeCategory);

        locList= (Spinner)findViewById(R.id.crimeLoc);
        addLocationToSpinner(); // add location stored in database to dropdown list
        addCrimeTypesToSpinner(); //adds the categories of crimes to a list for a user to choice from
        desc= (EditText) findViewById(R.id.description);

        imageChoiceRadioGroup= (RadioGroup) findViewById(R.id.uploadPicChoice); //get radio group
        pictureUpload= (ImageView) findViewById(R.id.picture);
        pictureUpload.setImageResource(0);// clear any possible previous resource
        //photo=null; //clear any possible content to avoid writing duplicate pictures as it's global
        pictureUpload.setImageResource(R.drawable.user1); //set up default picture
        uploadButton= (FloatingActionButton) findViewById(R.id.uploadPicButton);

        getChoice(); //get whether or not the user has a picture to upload or not

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* ############## Handle Camera Upload ################### */
                uploadPicker();

            }
        });

        //Web services


    }

    private void uploadPicker(){
        final AlertDialog picker= new AlertDialog.Builder(this).create();
        LayoutInflater factory= LayoutInflater.from(UploadCrime.this);
        final View view=factory.inflate(R.layout.upload_design, null);
        ImageButton imgbtn1= (ImageButton) view.findViewById(R.id.btn1);
        ImageButton imgbtn2= (ImageButton) view.findViewById(R.id.btn2);


        imgbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MEG", "Clicked camera button");
                picker.dismiss();
                cameraOrGalleryFlag = 1; //camera choosen
                uploadImage(1);// do camera
            }
        });
        imgbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MEG", "Clicked gallery button");
                picker.dismiss();
                cameraOrGalleryFlag = 2;// gallery choosen
                uploadImage(2);// do gallery
                uploadButton.setVisibility(View.VISIBLE);
            }
        });

        picker.setView(view);
        picker.show();
        picker.setCanceledOnTouchOutside(true);
//        picker.setCancelable(true);
        //resize and reposition upload picker
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(picker.getWindow().getAttributes());
        lp.width = 350;
        lp.height =ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.x=175;
        lp.y=100;
        picker.getWindow().setAttributes(lp);

    }

    private void uploadImage(int choice) {

        if(choice==1){
            //choose to take pic with camera
            // check if camera exist on device 1st

            if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                // has a camera
                Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileURI= getMediaURI();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileURI);
                startActivityForResult(cameraIntent,CAMERA_CAPTURE_REQUEST_CODE);

            }else{
                Toast.makeText(getApplicationContext(),"Camera not found on this device",Toast.LENGTH_LONG).show();
            }


        }else
            if(choice==2){
                //choose to choose from gallery
                Intent galleryIntent= new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);


            }
    }

    private static File getMediaFile() {
        String folderName="UWI";
        File pictureDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),folderName);
        if(!pictureDir.exists()){
            if(!pictureDir.mkdirs()){
                Log.d("MEG","Failed to create directory: "+pictureDir.getPath());
            }
        }

        //using time stamp to distinguish b/w multiple perps when storing on device
        String timeStamp= new SimpleDateFormat("yyyymmdd_hhmm",Locale.getDefault()).format(new Date());
        File mediaFile=new File(pictureDir.getPath()+File.separator+"Perp_"+timeStamp+".jpg");
       // Log.d("MEG","Dir: "+mediaFile.getAbsolutePath());


        return mediaFile;
    }

    public String getMediaURI(){

        return  String.valueOf(getMediaFile().getAbsolutePath());
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode== CAMERA_CAPTURE_REQUEST_CODE){
            if(resultCode== RESULT_OK){
            //success save image and add to image view
                photo= (Bitmap) data.getExtras().get("data");
                pictureUpload.setImageResource(0);
                pictureUpload.setImageBitmap(photo);// set picture in the image view
                pictureUpload.setScaleType(ImageView.ScaleType.FIT_XY);


            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),"Cancelled image capture",
                        Toast.LENGTH_SHORT).show();
                photo=null;
            }
            else{
                Toast.makeText(getApplicationContext(),"Sorry, failed to capture image",
                        Toast.LENGTH_LONG).show();
            }

        }else if(requestCode==GALLERY_REQUEST_CODE) {

           if(resultCode==RESULT_OK) {
               Uri uri= data.getData();
               String [] filePathColumn= {MediaStore.Images.Media.DATA};
               Cursor cursor= getContentResolver().query(uri,filePathColumn,null,null,null);
               if (cursor != null) {
                   cursor.moveToFirst();
               }

               int columnIndex= cursor != null ? cursor.getColumnIndex(filePathColumn[0]) : 0;
               assert cursor != null;
               String picturePath=cursor.getString(columnIndex);
               cursor.close();
               pictureUpload.setImageResource(0);// removes the default picture

               BitmapFactory.Options options=new BitmapFactory.Options();
               options.inSampleSize=8;

               photo=BitmapFactory.decodeFile(picturePath,options);
               pictureUpload.setImageBitmap(photo);// set picture in the image view
               pictureUpload.setScaleType(ImageView.ScaleType.FIT_XY);
               //photo=temp;

           }else if(resultCode== RESULT_CANCELED){
               Toast.makeText(getApplicationContext(),"No picture was selected",Toast.LENGTH_SHORT).show();
               photo=null;
           }else{
               Toast.makeText(getApplicationContext(),"Sorry failed to retrieve photo",
                       Toast.LENGTH_SHORT).show();
           }

        }
    }



    private String prepareImageToSend(Bitmap photo) {
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        byte[] byteArray = null;
        String imageDataString = null;
        Log.d("MEG","In prepareImagetoSend");

        if(photo!=null){
//            int size=photo.getByteCount();
//            ByteBuffer bb= ByteBuffer.allocate(size);
//
//            photo.copyPixelsToBuffer(bb);
//            byteArray=bb.array();
//            imageDataString=Base64.encodeToString(byteArray,Base64.DEFAULT);
            Log.d("MEG"," photo is not null");
            photo.compress(Bitmap.CompressFormat.JPEG,80,baos);
            imageDataString=Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);
            Log.d("MEG","String: "+imageDataString);
        }


        return imageDataString;
    }


    private void saveImageToFolderStorage(Bitmap bmp){
        File file=getMediaFile();
        try {
            FileOutputStream fos=new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);

            fos.flush();
            fos.close();
            //purge image view
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // this method will work for kit kat version unlike others
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                Log.d("MEG", "scanned and available to view in gallery immediately");
            }
        });
    }

    private void getChoice() {

        imageChoiceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {

                    answerRadioButton = (RadioButton) findViewById(checkedId);// get which radio button was selected
                    Log.d("MEG", " button " + answerRadioButton.getText());
                    if (answerRadioButton.getText().toString().equalsIgnoreCase("yes")) {
                        // set image view visibility to visible
                        pictureUpload.setVisibility(View.VISIBLE);
                        uploadButton.setVisibility(View.VISIBLE);

                    } else {
                        pictureUpload.setVisibility(View.GONE);
                        uploadButton.setVisibility(View.GONE);
                        pictureUpload.setImageBitmap(null);
                        pictureUpload.setImageResource(0);
                        pictureUpload.setImageResource(R.drawable.user1);
                    }
                }

            }
        });

    }
    /* ################################ End of image upload ################################### */



    private void addCrimeTypesToSpinner() {
        List<String> list= new ArrayList<String>();
        list.add("Select Crime");
        list.add("Motor Theft");
        list.add("Robbery");
        list.add("Rape");
        list.add("Peeping Tom");
        list.add("Assault");
        list.add("Motor Vandalism");
        list.add("Kidnapping");
        list.add("Missing Person");


        Collections.sort(list.subList(1, list.size()));// sort list for user dispaly
        list.add("Other");// leave as last option
        ArrayAdapter <String> adapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crimeTypeList.setAdapter(adapter);
        


    }

    public void addLocationToSpinner(){
        List<String> list= new ArrayList<String>();
        //TODO:Make select location not a choice& default
        list.add("Select Location");
        list.add("Bus Route- North Gate (Yvettes)");
        list.add("South Gate- Gate Boys");

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locList.setAdapter(adapter);

    }



    public void webService(RequestParams params){

        AsyncHttpClient client= new AsyncHttpClient();

        //client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.post(getApplicationContext(), URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("MEG", "Success Status Code " + statusCode);
                Toast.makeText(getApplicationContext(), "Success, Report was submitted", Toast.LENGTH_SHORT).show();
                try {
                    String response = new String(responseBody, "UTF-8");
                    Log.d("MEG", "Success Status Code " + response);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // go to homepage
                //  Intent intent= new Intent(getApplicationContext(),HomeActivity.class);
                //startActivity(intent);
                closeActivity();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "" + statusCode + ".." + response, Toast.LENGTH_SHORT).show();
                Log.d("MEG", "Failure Status Code " + statusCode + ".." + response);

                if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Oops something went wrong at our end", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


    public void verifySubmission(){

        //check values 1st then verify via dialog
        if(crimeTypeList.getSelectedItem().toString().equalsIgnoreCase("select crime")){
            Toast.makeText(getApplicationContext(), "Invalid please select a crime",
                    Toast.LENGTH_SHORT).show();
        }else
        if(locList.getSelectedItem().toString().equalsIgnoreCase("select location")){
            Toast.makeText(getApplicationContext(), "Invalid please select a location",
                    Toast.LENGTH_SHORT).show();
        }else

        if(imageChoiceRadioGroup.getCheckedRadioButtonId()== -1){
            Toast.makeText(getApplicationContext(), "Invalid please whether you have photographic evidence",
                    Toast.LENGTH_SHORT).show();
        }else {

            //else all values should be valid
            final RequestParams reqParams = new RequestParams();
            reqParams.put("category", crimeTypeList.getSelectedItem().toString());
            reqParams.put("desc", desc.getText().toString());
            reqParams.put("loc", locList.getSelectedItem().toString());


            final AlertDialog.Builder alert = new AlertDialog.Builder(UploadCrime.this);
            alert.setMessage(R.string.verifySubmit);
            alert.setPositiveButton(R.string.yesP, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // perform web services
                    // save the image
                    //send kill activity and go home
                    if (photo != null) {

                        if (cameraOrGalleryFlag == 1)
                            saveImageToFolderStorage(photo); //save image to directory as it's a live capture
                        Log.d("MEG", "Flag:" + cameraOrGalleryFlag);
                        String val = prepareImageToSend(photo);
                        Log.d("MEG", "PHoto:" + val);
                        reqParams.put("picture", val);
                        photo = null; // clean global bitmap to avoid accidental dups
                        //photo = null;//reset
                    } else {
                        //default pic
                        Bitmap def = BitmapFactory.decodeResource(getResources(), R.drawable.user1);
                        if (def != null) {
                            String val = prepareImageToSend(def);// to avoid retrieval of image returning null exceptions
                            // Log.d("MEG", "PHoto:" + val);
                            reqParams.put("picture", val);
                        }
                    }

                    // Log.d("MEG", "Photo is null");
                    webService(reqParams);
                    photo = null;//reset
                    closeActivity();

                }
            });

            alert.setNegativeButton(R.string.canP, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), "If you wish to discard simply press back to go home",
                            Toast.LENGTH_LONG).show();
                }

            });


            alert.create();
            alert.show();
        }

    }



    private void closeActivity(){
        this.finish();
    }
/* ################################################################################################*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upload_crime, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            //verify user submission
            Toast.makeText(getApplicationContext(),"Verify submission",Toast.LENGTH_SHORT).show();
            verifySubmission();

        }

        return super.onOptionsItemSelected(item);
    }


}
