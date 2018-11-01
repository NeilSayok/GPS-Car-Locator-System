package com.example.neil.carlocator4l;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chaos.view.PinView;

import java.util.HashMap;
import java.util.Map;

public class OTP_activity extends AppCompatActivity {

    PinView otpView;
    FloatingActionButton one,two,three,four,five,six,seven,eight,nine,zero,bksp,done;
    Button verifyBtn;
    TextView sentTo;
    Animation anim;
    Vibrator vibrator;
    SharedPreferences sp;
    Intent i;
    String verifyOtpUrl,resendOtpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_activity);
        setViews();
        sentTo.setText(sp.getString("email",""));
    }

    void setViews(){
        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sp =  getSharedPreferences("LOGIN",MODE_PRIVATE);
        sentTo = findViewById(R.id.sentTo);

        verifyOtpUrl = getString(R.string.verifyOTPPHP);
        resendOtpUrl = getString(R.string.resend_otp_url);

        otpView = findViewById(R.id.otp);
        verifyBtn = findViewById(R.id.verifyBtn);

        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        zero = findViewById(R.id.zero);
        bksp = findViewById(R.id.bksp);
        done = findViewById(R.id.done);

        one.setOnClickListener(keypadOnClick);
        two.setOnClickListener(keypadOnClick);
        three.setOnClickListener(keypadOnClick);
        four.setOnClickListener(keypadOnClick);
        five.setOnClickListener(keypadOnClick);
        six.setOnClickListener(keypadOnClick);
        seven.setOnClickListener(keypadOnClick);
        eight.setOnClickListener(keypadOnClick);
        nine.setOnClickListener(keypadOnClick);
        zero.setOnClickListener(keypadOnClick);
        bksp.setOnClickListener(keypadOnClick);
        bksp.setOnLongClickListener(bkspLongPress);
        done.setOnClickListener(keypadOnClick);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp();
            }
        });
        otpView.addTextChangedListener(OtpViewTextwather);
    }

    View.OnClickListener keypadOnClick = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            vibrator.vibrate(10);
            switch (v.getId()){
                case R.id.one :
                    otpView.setText(otpView.getText().toString()+ getString(R.string._1));
                    break;
                case R.id.two :
                    otpView.setText(otpView.getText().toString()+ getString(R.string._2));
                    break;
                case R.id.three :
                    otpView.setText(otpView.getText().toString()+ getString(R.string._3));
                    break;
                case R.id.four :
                    otpView.setText(otpView.getText().toString()+ getString(R.string._4));
                    break;
                case R.id.five :
                    otpView.setText(otpView.getText().toString()+ getString(R.string._5));
                    break;
                case R.id.six :
                    otpView.setText(otpView.getText().toString()+ getString(R.string._6));
                    break;
                case R.id.seven :
                    otpView.setText(otpView.getText().toString()+ getString(R.string._7));
                    break;
                case R.id.eight :
                    otpView.setText(otpView.getText().toString()+ getString(R.string._8));
                    break;
                case R.id.nine :
                    otpView.setText(otpView.getText().toString()+ getString(R.string._9));
                    break;
                case R.id.zero :
                    otpView.setText(otpView.getText().toString()+ getString(R.string._0));
                    break;
                case R.id.bksp :
                    try {
                        otpView.setText(otpView.getText().toString().substring(0,otpView.getText().toString().length()-1));
                    }catch (Exception e){
                        otpView.startAnimation(anim);
                        e.printStackTrace();
                    }
                    break;
                case R.id.done :
                    String otp = otpView.getText().toString();
                    if (otp.length() != 6){
                        otpView.startAnimation(anim);
                    }
                    else {
                        // TODO call verify otp function
                        Log.d("Done", otp);
                        verifyOtp(otp);
                    }
                    break;

            }


        }
    };

    TextWatcher OtpViewTextwather = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count == 6){
                verifyBtn.setBackground(getDrawable(R.drawable.rect_btn_green));
                verifyBtn.setText("VERIFY");
                verifyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        verifyOtp(otpView.getText().toString());
                    }
                });
            }else {
                verifyBtn.setBackground(getDrawable(R.drawable.rect_btn_red));
                verifyBtn.setText("RESEND OTP");
                verifyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resendOtp();
                    }
                });
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    View.OnLongClickListener bkspLongPress = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            try {
                vibrator.vibrate(30);
                otpView.setText("");
            }catch (Exception e){
                Log.w("num chars", "0");
            }
            return false;
        }
    };

    public void verifyOtp(final String otp){

        findViewById(R.id.baseOTPLayout).setVisibility(View.GONE);
        findViewById(R.id.progressRelLayout).setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, verifyOtpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
                response = response.trim();
                if (response.equals("not_verified")){
                    Toast.makeText(getApplicationContext(),"Enter a valid OTP",Toast.LENGTH_LONG).show();
                    findViewById(R.id.baseOTPLayout).setVisibility(View.VISIBLE);
                    findViewById(R.id.progressRelLayout).setVisibility(View.GONE);
                    otpView.setText("");
                    otpView.startAnimation(anim);
                }else if (response.equals("verified")){
                    sp.edit().putBoolean("Logged",true).apply();
                    //TODO open TrackActivity
                    i = new Intent(getApplicationContext(),TrackActivity.class);
                    findViewById(R.id.baseOTPLayout).setVisibility(View.VISIBLE);
                    findViewById(R.id.progressRelLayout).setVisibility(View.GONE);
                    startActivity(i);
                }else {
                    //TODO Show Error
                    Toast.makeText(getApplicationContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                    findViewById(R.id.baseOTPLayout).setVisibility(View.VISIBLE);
                    findViewById(R.id.progressRelLayout).setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                findViewById(R.id.baseOTPLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.progressRelLayout).setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String email = sp.getString("email","");
                Map<String,String> params = new HashMap<String, String>();
                params.put("otp_conf",otp);
                params.put("email",email);
                return params;
            }
        };

        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    public void resendOtp(){
        findViewById(R.id.baseOTPLayout).setVisibility(View.GONE);
        findViewById(R.id.progressRelLayout).setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, resendOtpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Resend Otp",response);
                        findViewById(R.id.baseOTPLayout).setVisibility(View.VISIBLE);
                        findViewById(R.id.progressRelLayout).setVisibility(View.GONE);
                        response = response.trim();
                        if (response.equals("Mail_Sent")){
                            Toast.makeText(getApplicationContext(),"OTP has been sent to your email.",Toast.LENGTH_LONG).show();
                            otpView.setText("");
                        }else if(response.equals("Main_Not_Sent")){
                            Toast.makeText(getApplicationContext(),"Mail cannot be sent.",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String email = sp.getString("email","");
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",email);
                return params;
            }
        };

        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    public void reOpenLogin(View v){
        Intent i = new Intent(this,MainActivity.class);
        sp.edit().remove("Logged").apply();
        sp.edit().remove("email").apply();
        sp.edit().remove("name").apply();
        sp.edit().remove("reg_id").apply();
        sp.edit().remove("password").apply();
        sp.edit().remove("verified").apply();
        sp.edit().remove("serviceStat").apply();
        startActivity(i);
        finish();
    }

}