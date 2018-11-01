package com.example.neil.carlocatoruserside1m;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class getLocationService extends Service {

    String email,pass,getLocationUrl;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getLocationOnline();
            mHandler.postDelayed(runnable,3000);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        email = (String) intent.getExtras().get("email");
        pass = (String) intent.getExtras().get("password");
        getLocationUrl = getString(R.string.getLocation);
        runnable.run();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
        mHandler = null;
        runnable = null;
    }

    private void getLocationOnline(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getLocationUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String lat = jsonObject.getString("lat");
                            if (lat.equals("passMiss")){
                                //TODO CHECK FOR PASSWORD MISS
                            }else if (lat.equals("credMiss")){
                                //TODO CHECK FOR EMAIL MISS
                            }else if (lat.equals("null")){ }
                            else {
                                Intent i = new Intent("carLocationService");
                                i.putExtra("lat",Double.parseDouble(lat));
                                i.putExtra("longi",Double.parseDouble(jsonObject.getString("longi")));
                                i.putExtra("time",jsonObject.getString("time"));
                                sendBroadcast(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("password",pass);
                return params;
            }
        };
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }
}