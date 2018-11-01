package com.example.neil.carlocatoruserside1m;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class statService extends Service {

    SharedPreferences sp;
    String statUrl;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startFunc();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
    }

    public void checkStatOnline(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, statUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("From Service",response);
                        String[] rArr = new String[0];
                        try {
                            response = response.substring(1,response.length()-1);
                            rArr = response.split(",");
                            Intent i = new Intent("sendServiceStat");
                            i.putExtra("response",rArr);
                            sendBroadcast(i);
                        }catch (Exception e){e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("emails", sp.getString("email",""));
                return params;
            }
        };
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkStatOnline();
            mHandler.postDelayed(runnable,3000);
        }
    };

    public void startFunc(){
        sp = getSharedPreferences("spdb",MODE_PRIVATE);
        statUrl = getString(R.string.statUrl);
        runnable.run();
    }
}