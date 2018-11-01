package com.example.neil.carlocator4l;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TrackActivity extends AppCompatActivity {

    Intent i, serviceIntent;
    RelativeLayout base;
    String delUserUrl;
    SharedPreferences sp;
    BroadcastReceiver broadcastReceiver;
    String checkstatURL, writeStatURL;
    TextView lat, longi, time;
    Button track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        serviceIntent = new Intent(TrackActivity.this, GPS_service.class);
        checkstatURL = getString(R.string.check_log_url);
        writeStatURL = getString(R.string.write_log_url);
        base = findViewById(R.id.baseLayout);
        delUserUrl = getString(R.string.deluserurl);
        lat = findViewById(R.id.lat);
        longi = findViewById(R.id.longi);
        time = findViewById(R.id.time);
        track = findViewById(R.id.startstopTrackingService);
        if (sp.getBoolean("serviceStat", false)) {
            track.setBackground(getDrawable(R.drawable.rect_btn_red));
            track.setText("Stop tracking your car");
        }
        track.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    longi.setText("Longitude : " + intent.getExtras().get("longitude"));
                    lat.setText("Latitude : " + intent.getExtras().get("latitude"));
                    time.setText("Your Location at : " + intent.getExtras().get("time"));

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.startstopTrackingService) {
                final RelativeLayout rel = findViewById(R.id.trackStartLoaderRel);
                rel.setVisibility(View.VISIBLE);
                if (!sp.getBoolean("serviceStat", false)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, checkstatURL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("LogStat", response);
                                    if (response.equals("0")) {
                                        sp.edit().putBoolean("serviceStat", true).apply();
                                        track.setBackground(getDrawable(R.drawable.rect_btn_red));
                                        track.setText("Stop tracking your car");
                                        rel.setVisibility(View.GONE);
                                        ContextCompat.startForegroundService(TrackActivity.this, serviceIntent);
                                    } else {
                                        rel.setVisibility(View.GONE);
                                        final Snackbar snackbar = Snackbar.make(base, "Log out of this account from other devices", Snackbar.LENGTH_LONG)
                                                .setAction("OK", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) { }
                                                });
                                        snackbar.show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {}
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", sp.getString("email", ""));
                            return params;
                        }
                    };

                    VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);

                } else {

                    StringRequest sr = new StringRequest(Request.Method.POST, writeStatURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            rel.setVisibility(View.GONE);
                            sp.edit().putBoolean("serviceStat", false).apply();
                            track.setBackground(getDrawable(R.drawable.rect_btn_green));
                            track.setText("Start tracking your car");
                            stopService(serviceIntent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {}
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", sp.getString("email", ""));
                            params.put("stat", "0");
                            return params;
                        }
                    };
                    VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(sr);
                }

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.logout:
                i = new Intent(this, MainActivity.class);
                sp.edit().remove("Logged").apply();
                sp.edit().remove("email").apply();
                sp.edit().remove("name").apply();
                sp.edit().remove("reg_id").apply();
                sp.edit().remove("password").apply();
                sp.edit().remove("verified").apply();
                sp.edit().remove("serviceStat").apply();
                startActivity(i);
                finish();
                break;
            // action with ID action_settings was selected
            case R.id.delusr:
                i = new Intent(this, MainActivity.class);
                //TODO delete the user from database
                Snackbar snackbar = Snackbar.make(base, "This will remove you from our database too...", Snackbar.LENGTH_LONG)
                        .setAction("OK I UNDERSTAND", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, delUserUrl, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("email", sp.getString("email", ""));
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
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {}
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("email", sp.getString("email", ""));
                                        return params;
                                    }
                                };
                                VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);
                            }
                        });

                snackbar.show();
                break;
            default:
                break;
        }
        return true;
    }
}