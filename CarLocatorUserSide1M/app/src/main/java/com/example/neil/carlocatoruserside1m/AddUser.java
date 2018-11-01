package com.example.neil.carlocatoruserside1m;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddUser extends AppCompatActivity {

    EditText uid,pass;
    ImageButton visibilityPass;
    RelativeLayout r1,r2,r3;
    Animation anim;
    String loginURL,email,password;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);
        sp = getSharedPreferences("spdb",MODE_PRIVATE);
        loginURL = getString(R.string.login_php_url);
        //Layout Initialization
        r1 = findViewById(R.id.LoginTextRelLayout);
        r2 = findViewById(R.id.inptRelLayout);
        r3 = findViewById(R.id.buttonRelLayout);
        uid = findViewById(R.id.userNameET);
        pass = findViewById(R.id.passwordET);
        visibilityPass = findViewById(R.id.viewPass);
        visibilityPass.setOnTouchListener(onTouchListener);
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
    }

    public void signinClick(View view) {
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

    private void Volleyfunc(final String email, final String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Volley Return" ,response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            Log.d("Volley Return", jObject.toString());
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
                                Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_LONG).show();
                            }else if(status.equals("credMiss")){
                                r1.setVisibility(View.GONE);
                                r2.setVisibility(View.VISIBLE);
                                uid.setText("");
                                pass.setText("");
                                uid.startAnimation(anim);
                                Toast.makeText(getApplicationContext(),"No Record Found",Toast.LENGTH_LONG).show();
                            }else{
                                try {
                                    int verified = Integer.parseInt(jObject.getString("verified"));
                                    if (verified == 0){
                                        Toast.makeText(getApplicationContext(),"This account is not verified",Toast.LENGTH_LONG).show();
                                        r1.setVisibility(View.GONE);
                                        r2.setVisibility(View.VISIBLE);
                                        uid.setText("");
                                        pass.setText("");
                                    }
                                    else {
                                        //TODO WRITE TO LOCAL DATABASE
                                        //name,email,reg_id,password
                                        String name = jObject.getString("name");
                                        String email = jObject.getString("email");
                                        String veh_id = jObject.getString("reg_id");
                                        if (!sp.getString("email", "").contains(email)) {
                                            sp.edit().putString("name", sp.getString("name", "") + "#!!#" + name).apply();
                                            sp.edit().putString("email", sp.getString("email", "") + "#!!#" + email).apply();
                                            sp.edit().putString("regid", sp.getString("regid", "") + "#!!#" + veh_id).apply();
                                            sp.edit().putString("password", sp.getString("password", "") + "#!!#" + password).apply();
                                        }else {
                                            Toast.makeText(getApplicationContext(),"User Already Exists",Toast.LENGTH_LONG).show();
                                        }
                                        finish();
                                        startActivity(new Intent(AddUser.this,MainActivity.class));
                                    }
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
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
                Toast.makeText(AddUser.this,"Taking too long please try again",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("psw",password);
                return params;
            }
        };

        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
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