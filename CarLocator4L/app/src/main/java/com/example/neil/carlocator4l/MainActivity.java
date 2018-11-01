package com.example.neil.carlocator4l;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final short REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    final String TAG = "MainActivity";
    Activity activity;
    SharedPreferences sp;
    Intent i;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askForPermissionsCall();

        activity = MainActivity.this;
        sp = getSharedPreferences("LOGIN",MODE_PRIVATE);
        i = new Intent(this,SignInActivity.class);

        url = getString(R.string.checkindburl);

        String email,vhe_id;

        email = sp.getString("email","");
        vhe_id = sp.getString("veh_id","");

        if(email.equals("") && vhe_id.equals("")){
            checkUserInDb(email,vhe_id);
            startActivity(i);
            finish();
        }else{
            checkUserInDb(email,vhe_id);
        }


    }



private void checkUserInDb(final String email, final String vehid){
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d(TAG+" volley",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String present = jsonObject.getString("present");

                if (present.equalsIgnoreCase("yes")){
                    sp.edit().putInt("verified",Integer.parseInt(jsonObject.getString("verified"))).apply();
                    sp.edit().putString("name",jsonObject.getString("name")).apply();
                    sp.edit().putString("reg_id",jsonObject.getString("reg_id")).apply();
                    sp.edit().putString("password",jsonObject.getString("password")).apply();
                    sp.edit().putString("email",email).apply();

                    startActivity(i);
                    finish();
                }else if (present.equals("no")){

                    sp.edit().remove("Logged").apply();
                    sp.edit().remove("email").apply();
                    sp.edit().remove("name").apply();
                    sp.edit().remove("reg_id").apply();
                    sp.edit().remove("password").apply();
                    sp.edit().remove("verified").apply();
                    startActivity(i);
                    finish();
                }else {
                    showDialog(MainActivity.this, "Please Restart Application", "Sorry,for the inconvenience.");
                }
            }catch (Exception e){
                showDialog(MainActivity.this, "Please Restart Application", "Sorry,for the inconvenience.");
            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                showDialog(MainActivity.this, "Please Restart Application", "Sorry,unable to login. Please check your internet connection.");
            }else{
                showDialog(MainActivity.this, "Please Restart Application!", "Sorry,for the inconvenience.");
            }
        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> params = new HashMap<>();
            params.put("email", email);
            params.put("vehid",vehid);
            return params;
        }
    };

    VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);
}

// Required for permission access
    private void askForPermissionsCall(){
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        if(!hasPermissions(getApplicationContext(), PERMISSIONS)){
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    public static boolean hasPermissions (Context context, String...permissions){
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkAndRequestPermissions () {
        int ExtWritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int ExtReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (ExtWritePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ExtReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[],
                                            @NonNull int[] grantResults){
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                /////// perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "Storage services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("Location Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(getApplicationContext(), "Click on Permissions and enable the permissions that are required for this app to work properly.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);

                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK (String message, DialogInterface.OnClickListener okListener)
    {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

// Required for permission access

    public void showDialog(AppCompatActivity activity, String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}