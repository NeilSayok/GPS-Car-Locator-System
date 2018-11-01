package com.example.neil.carlocatoruserside1m;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    BroadcastReceiver broadcastReceiver;
    Intent i,locationServiceIntent;
    MarkerOptions markerOptions;
    Marker m;
    TextView time,date,city,postalcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        time = findViewById(R.id.lastSeenTimeTV);
        date = findViewById(R.id.lastSeenDateTV);
        city = findViewById(R.id.cityTV);
        postalcode = findViewById(R.id.postalCodeTV);
        locaionServiceHandler();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locaionServiceHandler();
        if (mMap != null){
            setmMap();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng startLocation = new LatLng(22.560214, 88.4879874);
        markerOptions = new MarkerOptions().position(startLocation).title("Here is your car").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name));
        m = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation,16));
        locaionServiceHandler();
        setmMap();
    }

    private void locaionServiceHandler() {
        i = getIntent();
        locationServiceIntent = new Intent(getApplicationContext(),getLocationService.class);
        locationServiceIntent.putExtra("email",i.getStringExtra("email"));
        locationServiceIntent.putExtra("password",i.getStringExtra("password"));
        startService(locationServiceIntent);
    }

    private void setmMap(){
        if (broadcastReceiver == null)
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        double latitude = intent.getDoubleExtra("lat",22.560214);
                        double longitude = intent.getDoubleExtra("longi",22.560214);
                        Log.d("SetMap",intent.getExtras().getString("time"));
                        LatLng startLocation = new LatLng(latitude, longitude );
                        m.setPosition(startLocation);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation,16));
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(context, Locale.getDefault());
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        city.setText("City: "+addresses.get(0).getLocality());
                        postalcode.setText("Postal Code: "+addresses.get(0).getPostalCode());
                        date.setText("Last Seen Date: "+ intent.getExtras().getString("time").trim().split("/")[0]);
                        time.setText("Last Seen Time: "+ intent.getExtras().getString("time").trim().split("/")[1]);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
        registerReceiver(broadcastReceiver,new IntentFilter("carLocationService"));
    }
}