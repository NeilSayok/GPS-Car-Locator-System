package com.example.neil.carlocator4l;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    RelativeLayout r1,r2,r3;
    EditText uid,pass;
    ImageButton visibilityPass;
    String email,password;
    String url;
    SharedPreferences sp;
    Animation anim;
    Intent i;
    Activity activity;
    final short REQUEST_ID_MULTIPLE_PERMISSIONS = 10;
    final String TAG = "SignInActivity";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        //Shared Preference
        sp = getSharedPreferences("LOGIN",MODE_PRIVATE);
        //Other setups
        activity = SignInActivity.this;
        //Layout Initialization
        r1 = findViewById(R.id.LoginTextRelLayout);
        r2 = findViewById(R.id.inptRelLayout);
        r3 = findViewById(R.id.buttonRelLayout);
        uid = findViewById(R.id.userNameET);
        pass = findViewById(R.id.passwordET);
        visibilityPass = findViewById(R.id.viewPass);
        //Url for REST api
        url = getString(R.string.login_php_url);
        //Animation
        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in);
        //Start up animation -> The layouts r1,r2,r3 will not be required in future so can be reused
        r1.startAnimation(anim);
        r2.startAnimation(anim);
        r3.startAnimation(anim);
        //Reusing Layout and animation for future use
        r1 = findViewById(R.id.progressRelLayout);
        r2 = findViewById(R.id.baseRelLayout);
        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        visibilityPass.setOnTouchListener(onTouchListener);

        if (!sp.getBoolean("Logged",false) ){
            // TODO Wait for the user to input or click a button
        }else {
            int verfied = sp.getInt("verified",2);
            //If user is not verified
            if (verfied ==0){
                //TODO Open OTP Activity
                i = new Intent(getApplicationContext(),OTP_activity.class);
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
                startActivity(i);
            }
            //If user is verified.
            else if(verfied == 1){
                //TODO open Tracking activity
                i = new Intent(getApplicationContext(),TrackActivity.class);
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
                startActivity(i);

            }
            //If verification cannot be fetched from server or some unknown error
            else {
                Toast.makeText(getApplicationContext(),"Something went wrong. Please login again.",Toast.LENGTH_SHORT).show();
                sp.edit().remove("Logged").apply();
                sp.edit().remove("email").apply();
                sp.edit().remove("veh_id").apply();
                sp.edit().remove("name").apply();
                sp.edit().remove("reg_id").apply();
                sp.edit().remove("password").apply();
                sp.edit().remove("serviceStat").apply();
                //Restarts the applicaion
                i = getIntent();
                finish();
                startActivity(i);
            }
        }
    }

    public void openSignUp(View v){
        i = new Intent(getApplicationContext(),signUp.class);
        startActivity(i);
    }

    public void signinClick(View v){

        email =uid.getText().toString();
        password = pass.getText().toString();
        if (email.equals("") || password.equals("") ){
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
            uid.startAnimation(shake);
            pass.startAnimation(shake);
            Toast.makeText(getApplicationContext(),"Please check the Email or Password.",Toast.LENGTH_SHORT).show();
        }else {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
            // Generate a String Request Using Volley
            password = password.replace("\\s","");
            Volleyfunc(email,password);
        }
    }

    public void Volleyfunc(final String mail, final String paswrd){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volley Return" ,response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    Log.d("Volley Return" ,jObject.toString());
                    String status = null;
                    try {
                        status = jObject.getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(status.equals("passMiss")){
                        r1.setVisibility(View.GONE);
                        r2.setVisibility(View.VISIBLE);
                        pass.setText("");
                        pass.startAnimation(anim);
                        sp.edit().putBoolean("Logged",false).apply();
                        Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_LONG).show();
                    }else if(status.equals("credMiss")){
                        r1.setVisibility(View.GONE);
                        r2.setVisibility(View.VISIBLE);
                        uid.setText("");
                        pass.setText("");
                        uid.startAnimation(anim);
                        sp.edit().putBoolean("Logged",false).apply();
                        Toast.makeText(getApplicationContext(),"No Record Found",Toast.LENGTH_LONG).show();
                    }else{
                        try {
                            int verified = Integer.parseInt(jObject.getString("verified"));
                            String name = jObject.getString("name");
                            String email = jObject.getString("email");
                            String veh_id = jObject.getString("reg_id");
                            isVerified(verified,name,email,veh_id, paswrd);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    showDialog(SignInActivity.this, "Login Failed", "Sorry,unableto login. Please check your internet connection.");
                }else{
                    showDialog(SignInActivity.this, "Login Failed", "Sorry,for the inconvenience.");
                }
                Log.d("Error",error.toString());
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",mail);
                params.put("psw", paswrd);
                return params;
            }
        };

        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);

    }

    public void isVerified(int verified, String name, String email, String regid, String passWrd){

        sp.edit().putBoolean("Logged",true).apply();
        sp.edit().putString("name",name).apply();
        sp.edit().putString("email",email).apply();
        sp.edit().putString("reg_id",regid).apply();
        sp.edit().putString("password",passWrd).apply();

        if(verified == 0){
            //TODO Start OTP VERIFICATION ACTIVITY
            sp.edit().putInt("verified",0).apply();
            i = new Intent(SignInActivity.this,OTP_activity.class);
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
            startActivity(i);

        }
        //User is verified already
        else {
            sp.edit().putInt("verified",1).apply();
            //TODO start tracking activity
            i = new Intent(SignInActivity.this,TrackActivity.class);
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
            startActivity(i);

        }
    }






    public void showDialog(AppCompatActivity activity, String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (title != null) builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (v.getId()){
                case R.id.viewPass:
                    switch ( motionEvent.getAction() ) {
                        case MotionEvent.ACTION_DOWN:
                            visibilityPass.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_visibility_24px));
                            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            pass.setSelection(pass.getText().length());
                            break;
                        case MotionEvent.ACTION_UP:
                            visibilityPass.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_visibility_off_24px));
                            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            pass.setSelection(pass.getText().length());
                            break;
                        default:
                            break;
                    }
                    break;

            }
            return true;
        }
    };

}