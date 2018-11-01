package com.example.neil.carlocator4l;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

public class signUp extends AppCompatActivity {

    RelativeLayout rel;
    Dialog d;
    String TAG = "SignUPActivity";
    Activity activity;
    int width;
    private String url, otp_url;
    Animation anim;
    EditText name, vehid, email, pass, rePass;
    Button signUp;
    ImageButton visibilityPass, visibilityRePass;
    TextView pass_strength, repass_strength;
    GradientDrawable passDraw, repassDraw;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        activity = signUp.this;

        width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        //REST URLs
        url = getResources().getString(R.string.signup_php_url);
        otp_url = getString(R.string.otp_url);

        //On Activity Load Animation
        rel = findViewById(R.id.baseRelLayoutSignup);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        rel.startAnimation(anim);
        //Setup views
        name = findViewById(R.id.nameET);
        vehid = findViewById(R.id.vehIDET);
        email = findViewById(R.id.mailET);
        pass = findViewById(R.id.passET);
        rePass = findViewById(R.id.repassET);
        pass_strength = findViewById(R.id.passStrength);
        repass_strength = findViewById(R.id.passStrength2);
        signUp = findViewById(R.id.SignUpBT);
        visibilityPass = findViewById(R.id.viewPass);
        visibilityRePass = findViewById(R.id.viewRePass);
        //Setup views
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        //Setting View Functionalities
        visibilityPass.setOnTouchListener(onTouchListener);
        visibilityRePass.setOnTouchListener(onTouchListener);
        email.setOnFocusChangeListener(focusChangeListener);
        pass.setFilters(new InputFilter[]{inputFilter});
        rePass.setFilters(new InputFilter[]{inputFilter});
        passDraw = (GradientDrawable) pass.getBackground();
        repassDraw = (GradientDrawable) rePass.getBackground();
        pass.addTextChangedListener(passTextWatcher);
        rePass.addTextChangedListener(repassTextWatcher);

    }

    //Button Press
    public void signUpClick(View v) {
        if (name.getText().toString().equals("")) {
            name.startAnimation(anim);
            name.requestFocus();
        } else if (vehid.getText().toString().equals("")) {
            vehid.startAnimation(anim);
            vehid.requestFocus();
        } else if (email.getText().toString().equals("") || !email.getText().toString().contains("@")) {
            email.startAnimation(anim);
            email.requestFocus();
        } else if (pass.getText().toString().equals("")) {
            pass.startAnimation(anim);
            pass.requestFocus();
        } else if (rePass.getText().toString().equals("")) {
            rePass.startAnimation(anim);
            rePass.requestFocus();
        } else if (!rePass.getText().toString().equals(pass.getText().toString())) {
            rePass.requestFocus();
            rePass.startAnimation(anim);
            pass.startAnimation(anim);
            Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
        } else if (rePass.getText().toString().equals(pass.getText().toString())) {
            if (passwordStrength(pass.getText().toString()).equals("weak") || passwordStrength(pass.getText().toString()).equals("medium_weak")) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.password_weak), Toast.LENGTH_LONG).show();
            } else {
                //Send To Sever using Volley.
                findViewById(R.id.progressRelLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.scrollView).setVisibility(View.GONE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        findViewById(R.id.progressRelLayout).setVisibility(View.GONE);
                        findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String res = jsonObject.getString("response");
                            Log.d("Response", res);
                            if (res.equals("Password_Missmatch")) {
                                Toast.makeText(getApplicationContext(), "Passwords Donot Match", Toast.LENGTH_SHORT).show();
                                rePass.requestFocus();
                                findViewById(R.id.progressRelLayout).setVisibility(View.GONE);
                                findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                                rePass.startAnimation(anim);
                                pass.startAnimation(anim);
                                pass.setText("");
                                rePass.setText("");
                                Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                            } else if (res.equals("Email_format_wrong")) {
                                email.requestFocus();
                                findViewById(R.id.progressRelLayout).setVisibility(View.GONE);
                                findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                                email.startAnimation(anim);
                                email.setText("");
                                Toast.makeText(getApplicationContext(), "Please Check The Email.", Toast.LENGTH_SHORT).show();
                            } else if (res.equals("Account_Not_Success")) {
                                findViewById(R.id.progressRelLayout).setVisibility(View.GONE);
                                findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                            } else if (res.equals("Account_Created")) {

                                SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
                                sp.edit().putBoolean("Logged", true).apply();
                                sp.edit().putString("name", name.getText().toString()).apply();
                                sp.edit().putString("reg_id", vehid.getText().toString()).apply();
                                sp.edit().putString("email", email.getText().toString()).apply();
                                sp.edit().putString("password", pass.getText().toString()).apply();
                                sp.edit().putInt("verified", 0).apply();
                                //REST CALL FOR CHECKING IF MAIL SENT.
                                StringRequest otp_send = new StringRequest(Request.Method.POST, otp_url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response1) {
                                                if (response1.trim().equals("Mail_Sent")) {
                                                    Intent i = new Intent(activity, OTP_activity.class);
                                                    startActivity(i);
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                                                    Log.i("Otp Request", response1);
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            showDialog(signUp.this, "Login Failed", "Sorry,unableto login. Please check your internet connection.");
                                        } else {
                                            showDialog(signUp.this, "Login Failed", "Sorry,for the inconvenience.");
                                        }
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("email", email.getText().toString());
                                        return params;
                                    }
                                };
                                VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(otp_send);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        findViewById(R.id.progressRelLayout).setVisibility(View.GONE);
                        findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        String password = pass.getText().toString();
                        String rePassword = rePass.getText().toString();
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("name", name.getText().toString());
                        params.put("vehid", vehid.getText().toString());
                        params.put("email", email.getText().toString());
                        params.put("psw", password);
                        params.put("rpsw", rePassword);
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
                VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);
            }
        }


    }
    //Button Press

    // Finds Password Strength
    public String passwordStrength(CharSequence inp) {
        int len = inp.length();
        short l = 1, u = 1, n = 1, s = 1;
        for (int i = 0; i < len; i++) {
            if (n != 1 && s != 1 && l != 1 && u != 1) {
                break;
            }
            if (Character.isDigit(inp.charAt(i)))// a digit
            {
                n = 4;
            } else if (Character.isUpperCase(inp.charAt(i)))// a upper case
            {
                u = 3;
            } else if (Character.isLowerCase(inp.charAt(i)))// a lower case
            {
                l = 2;
            } else// Special Char
            {
                s = 5;
            }
        }
        int x = l * s * u * n;
        //Log.d("lsun = ",Integer.toString(x));
        String ch = "a";
        if (x >= 2 && x <= 7) {
            ch = "a";
        } else if (x >= 8 && x <= 22) {
            ch = "b";
        } else if (x >= 23 && x <= 120) {
            ch = "c";
        }
        if (len <= 4)
            ch = "0" + ch;
        else if (len > 4 && len <= 8)
            ch = "1" + ch;
        else if (len > 8 && len <= 12)
            ch = "2" + ch;
        else if (len > 12)
            ch = "3" + ch;

        if (ch.equals("0a"))
            return "weak";
        else if (ch.equals("0b") || ch.equals("1a"))
            return "medium_weak";
        else if (ch.equals("0c") || ch.equals("1b") || ch.equals("2a"))
            return "medium";
        else if (ch.equals("1c") || ch.equals("2b") || ch.equals("3a"))
            return "medium_strong";
        else if (ch.equals("2c") || ch.equals("3b") || ch.equals("3c"))
            return "strong";

        return "weak";
    }

    //View Listeners
    InputFilter inputFilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String filtered = "";
            for (int i = start; i < end; i++) {
                char character = source.charAt(i);
                if (!Character.isWhitespace(character)) {
                    filtered += character;
                }
            }
            return filtered;
        }

    };

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.getId() == R.id.mailET) {
                if (!hasFocus) {
                    if (!email.getText().toString().contains("@")) {
                        Snackbar.make(rel, "Please Enter a correct Mail Id", Snackbar.LENGTH_LONG)
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        email.requestFocus();
                                    }
                                }).show();
                    }

                }
            }
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (v.getId()) {
                case R.id.viewPass:
                    switch (motionEvent.getAction()) {
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
                case R.id.viewRePass:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            visibilityRePass.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_visibility_24px));
                            rePass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            rePass.setSelection(rePass.getText().length());
                            break;
                        case MotionEvent.ACTION_UP:
                            visibilityRePass.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_visibility_off_24px));
                            rePass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            rePass.setSelection(rePass.getText().length());
                            break;
                        default:
                            break;
                    }
                    break;
            }
            return true;
        }
    };


    TextWatcher repassTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count + start != 0) {
                switch (passwordStrength(s)) {
                    case "weak":
                        repass_strength.setVisibility(View.VISIBLE);
                        repass_strength.setText(getResources().getString(R.string.pass_strength_weak));
                        repassDraw.setStroke(width, getResources().getColor(R.color.red));

                        break;
                    case "medium_weak":
                        repass_strength.setText(getResources().getString(R.string.pass_strength_weak));
                        repassDraw.setStroke(width, getResources().getColor(R.color.orange));
                        break;
                    case "medium":
                        repass_strength.setText(getResources().getString(R.string.pass_strength_med));
                        repassDraw.setStroke(width, getResources().getColor(R.color.yellow));
                        break;
                    case "medium_strong":
                        repass_strength.setText(getResources().getString(R.string.pass_strength_good));
                        repassDraw.setStroke(width, getResources().getColor(R.color.blue));
                        break;
                    case "strong":
                        repass_strength.setText(getResources().getString(R.string.pass_strength_strong));
                        repassDraw.setStroke(width, getResources().getColor(R.color.green));
                        break;
                }
            } else
                repassDraw.setStroke(width, getResources().getColor(R.color.white));

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                repass_strength.setVisibility(View.GONE);
            }

        }
    };


    TextWatcher passTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count + start != 0) {
                switch (passwordStrength(s)) {
                    case "weak":
                        pass_strength.setVisibility(View.VISIBLE);
                        pass_strength.setText(getResources().getString(R.string.pass_strength_weak));
                        passDraw.setStroke(width, getResources().getColor(R.color.red));
                        break;
                    case "medium_weak":
                        pass_strength.setText(getResources().getString(R.string.pass_strength_weak));
                        passDraw.setStroke(width, getResources().getColor(R.color.orange));
                        break;
                    case "medium":
                        pass_strength.setText(getResources().getString(R.string.pass_strength_med));
                        passDraw.setStroke(width, getResources().getColor(R.color.yellow));
                        break;
                    case "medium_strong":
                        pass_strength.setText(getResources().getString(R.string.pass_strength_good));
                        passDraw.setStroke(width, getResources().getColor(R.color.blue));
                        break;
                    case "strong":
                        pass_strength.setText(getResources().getString(R.string.pass_strength_strong));
                        passDraw.setStroke(width, getResources().getColor(R.color.green));
                        break;
                }
            } else
                passDraw.setStroke(width, getResources().getColor(R.color.white));
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                pass_strength.setVisibility(View.GONE);
            }
        }
    };

    //View Listeners
    public void showDialog(AppCompatActivity activity, String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (title != null) builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

}