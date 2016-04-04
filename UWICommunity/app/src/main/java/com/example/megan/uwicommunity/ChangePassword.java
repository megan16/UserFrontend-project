package com.example.megan.uwicommunity;
/* TODO: Web services, text watcher
 *
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {

    private String TAG="MEG";
    private EditText newPass;
    private EditText oldpass;
    private TextView textViewPassStrengthIndicator;
    private TextView passIndicatorLabel;
    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if (getSupportActionBar() != null){ // allow to go back to appear in action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Reset Password");
        }

        oldpass= (EditText) findViewById(R.id.oldpassword);
        newPass= (EditText) findViewById(R.id.newpassword);
        passIndicatorLabel= (TextView) findViewById(R.id.passStrengthLabel);
        textViewPassStrengthIndicator= (TextView) findViewById(R.id.passStrengthIndicator);
        submit= (Button) findViewById(R.id.submit_reset);

        newPass.addTextChangedListener(textWatcher);
        onSubmitReset();

    }

    private void onSubmitReset(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
    }

    private void checkFields() {
        if(!newPass.getText().toString().isEmpty() && !oldpass.getText().toString().isEmpty()){
            if(newPass.getText().toString().equalsIgnoreCase(oldpass.getText().toString())){
                Toast.makeText(getApplicationContext(),"Password cannot be the same as before",Toast.LENGTH_SHORT).show();
            }else{
                if(checkLength(newPass.getText().toString())){
                    //do web services
                    this.finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Password is too short",Toast.LENGTH_SHORT).show();
                }


            }

        }
        else{
            //must enter old password
            if(oldpass.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"You must enter your previous password",Toast.LENGTH_SHORT).show();
            }
            if(newPass.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"You must enter a new password",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean checkLength(String s){
        return s.length()>4;
    }

    private int checkPassword(String pass){
        String special = "!@#$%^&*()_{}|;:'?><=+-.,[]";
        int found = 0;

        for (int i=0; i<special.length(); i++) {
            if (pass.indexOf(special.charAt(i)) > 0) {
                found = 1;
                break;
            }

        }
         return found;
        }

    private final TextWatcher textWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        int once=0;
        Pattern pattern=Pattern.compile("[^A-Za-z0-9]");
        boolean isMedium=false,isStrong=false;
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            once++;
            if(once==1){ // to avoid doing it over and over again
                passIndicatorLabel.setVisibility(View.VISIBLE);
                textViewPassStrengthIndicator.setVisibility(View.VISIBLE);
            }
            Matcher matcher=pattern.matcher(s);


            if(s.length()==0){
                textViewPassStrengthIndicator.setText(null);
            }
            else if(s.length()>7  && (checkPassword(s.toString())==1 && matcher.find()) ){
                isMedium=false;
                isStrong=true;
                textViewPassStrengthIndicator.setText(R.string.strong);

            }
            else if(s.length()>7 || (matcher.find() ||checkPassword(s.toString())==1) ){
                isStrong=false;
                isMedium=true;
                textViewPassStrengthIndicator.setText(R.string.medium); //
            }
            if(s.length()<=7 && !matcher.find() && checkPassword(s.toString())==0 ){ //
                isMedium=isStrong=false;
                textViewPassStrengthIndicator.setText(R.string.easy);
            }


            if(s.length()==15 && isStrong){
                textViewPassStrengthIndicator.setText(R.string.maxlength_strong);

            }
            else
            if(s.length()==15 && isMedium){
                textViewPassStrengthIndicator.setText(R.string.maxlength_med);
            }
            else
            if(s.length()==15 && !isStrong && !isMedium){
                textViewPassStrengthIndicator.setText(R.string.maxlength);

            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



    @Override
    public void onBackPressed() {

        Log.d(TAG, "onBackPressed Called ");
        this.finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //Intent intent=new Intent(getApplicationContext(),SafetyActivity.class);
        //startActivity(intent);
        this.finish();
        return true;

        // return super.onOptionsItemSelected(item);
    }
}
