package com.example.neil.carlocatoruserside1m;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton adduser;
    TextView emptyTxt;
    SharedPreferences sp;
    BroadcastReceiver broadcastReceiver;
    String[] nameArr,emailArr,regidArr,passArr,statArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.mainRecView);
        adduser = findViewById(R.id.adduserBTN);
        emptyTxt = findViewById(R.id.emptyTXT);
        sp = getSharedPreferences("spdb",MODE_PRIVATE);
        startService(new Intent(MainActivity.this,statService.class));
        setRecView();
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddUser.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(MainActivity.this,statService.class));
        if (broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    statArr =intent.getStringArrayExtra("response");
                    try{
                        if (statArr.length > emailArr.length){
                            statArr = ArrayUtils.removeElement(statArr, statArr[0]);
                        }
                        setRecView();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("sendServiceStat"));
    }

    public void setRecView(){
        if (!sp.getString("email","").equals("")){
            //TODO SET RECYCLER VIEW
            nameArr = sp.getString("name","").split("#!!#");
            emailArr = sp.getString("email","").split("#!!#");
            regidArr = sp.getString("regid","").split("#!!#");
            passArr = sp.getString("password","").split("#!!#");
            nameArr = ArrayUtils.removeElement(nameArr, "");
            emailArr = ArrayUtils.removeElement(emailArr, "");
            regidArr = ArrayUtils.removeElement(regidArr, "");
            passArr = ArrayUtils.removeElement(passArr, "");
            emptyTxt.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new RecViewAdapter(this,emptyTxt,nameArr,emailArr,regidArr,passArr,statArr));
        }
        else {
            //TODO SET RECYCLER TO EMPTY VIEW
            emptyTxt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this,statService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(MainActivity.this,statService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(MainActivity.this,statService.class));
    }
}