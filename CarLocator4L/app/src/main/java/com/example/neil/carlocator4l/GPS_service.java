package com.example.neil.carlocator4l;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.neil.carlocator4l.App.CHANNEL_ID;

public class GPS_service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    SharedPreferences sp;
    Intent notificationIntent;
    PendingIntent pendingIntent;
    Notification notification;
    String hms,writeStatURL;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        writeStatURL = getString(R.string.write_log_url);
        sp = getSharedPreferences("LOGIN",MODE_PRIVATE);

        StringRequest sr = new StringRequest(Request.Method.POST, writeStatURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email", sp.getString("email",""));
                params.put("stat","1");
                return params;
            }
        };

        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(sr);

        notificationIntent = new Intent(this,MainActivity.class);

        pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent,0);

        notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Car Locator")
                .setContentText("Tracking Your Car : " + sp.getString("reg_id",""))
                .setSmallIcon(R.drawable.ic_location_on_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                //This will be called when location is changed

                Date date = new Date(location.getTime());
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss", Locale.US);
                hms = dateFormat.format(date);
                Intent i = new Intent("location_update");
                i.putExtra("latitude",location.getLatitude());
                i.putExtra("longitude",location.getLongitude());
                i.putExtra("time",hms);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://car-locator-javalab-proj.000webhostapp.com/updatelocation.php"
                        , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("email", sp.getString("email",""));
                        params.put("lat",Double.toString(location.getLatitude()));
                        params.put("time",hms);
                        params.put("longi",Double.toString(location.getLongitude()));
                        return params;
                    }
                };

                VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);


                Log.d("lat/long/time", Double.toString(location.getLatitude())+
                        "/"+ Double.toString(location.getLongitude())+"/"+
                        hms);

                sendBroadcast(i);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return START_STICKY;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, listener);

        startForeground(1,notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null){
            locationManager.removeUpdates(listener);
        }
        Log.d("Service","stopped");
        sp.edit().putBoolean("serviceStat",false).apply();

        StringRequest sr = new StringRequest(Request.Method.POST, writeStatURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email", sp.getString("email",""));
                params.put("stat","0");
                return params;
            }
        };

        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(sr);
    }

}