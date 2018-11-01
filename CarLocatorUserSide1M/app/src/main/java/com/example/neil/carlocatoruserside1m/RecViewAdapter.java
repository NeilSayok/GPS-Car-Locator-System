package com.example.neil.carlocatoruserside1m;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;

import static android.content.Context.MODE_PRIVATE;


public class RecViewAdapter extends Adapter<RecViewAdapter.RecViewViewHolder> {

    private Context context;
    private String[] name,email,regid,pass,stat;
    TextView nothingtext;

    public RecViewAdapter(Context context,TextView nothingtext, String[] name, String[] email, String[] regid , String[] passw,String[] status) {
        this.context = context;
        this.name = name;
        this.email = email;
        this.regid = regid;
        this.pass = passw;
        this.nothingtext = nothingtext;
        this.stat = status;
    }

    @Override
    public RecViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.listview_element,parent,false);
        RecViewViewHolder item = new RecViewViewHolder(row);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecViewViewHolder holder, final int position) {
        holder.user_name.setText(name[position]);
        holder.user_email.setText(email[position]);
        holder.user_regid.setText(regid[position]);
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = ArrayUtils.removeElement(name, name[position]);
                email = ArrayUtils.removeElement(email, email[position]);
                regid = ArrayUtils.removeElement(regid, regid[position]);
                pass = ArrayUtils.removeElement(pass, pass[position]);
                updateSharedPref(name,email,regid,pass);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
                if (getItemCount()==0){
                    nothingtext.setVisibility(View.VISIBLE);
                }
            }
        });

        try {
            if (stat != null){
                if (stat.length != 0){
                    if (stat[position].trim().equals("\"1\"")){
                        holder.statIV.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.circle_green));
                        holder.statTV.setText("Online");
                    }else {
                        holder.statIV.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.circle_red));
                        holder.statTV.setText("Offline");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,MapsActivity.class);
                i.putExtra("email",email[position]);
                i.putExtra("password",pass[position]);
                context.startActivity(i);
            }
        };
        //SETTING LISTENERS TO VIEWS
        holder.user_name.setOnClickListener(onClickListener);
        holder.user_email.setOnClickListener(onClickListener);
        holder.user_regid.setOnClickListener(onClickListener);
        holder.statTV.setOnClickListener(onClickListener);
        holder.statIV.setOnClickListener(onClickListener);
        holder.justTxt.setOnClickListener(onClickListener);
        holder.rel1.setOnClickListener(onClickListener);
        holder.rel2.setOnClickListener(onClickListener);
        holder.rel3.setOnClickListener(onClickListener);
        holder.jstview.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return email.length;
    }



    public class RecViewViewHolder extends RecyclerView.ViewHolder{
        TextView user_name,user_email,user_regid,statTV,justTxt;
        ImageButton delBtn;
        ImageView statIV;
        RelativeLayout rel1,rel2,rel3;
        View jstview;

        public RecViewViewHolder(final View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.name);
            user_email = itemView.findViewById(R.id.email);
            user_regid = itemView.findViewById(R.id.vehid);
            delBtn = itemView.findViewById(R.id.delBtN);
            statIV = itemView.findViewById(R.id.statusIV);
            statTV = itemView.findViewById(R.id.statusTV);
            justTxt = itemView.findViewById(R.id.statusjustText);
            rel1 =  itemView.findViewById(R.id.lvbaseRel);
            rel2 =  itemView.findViewById(R.id.statRelLayout);
            rel3 =  itemView.findViewById(R.id.justtLAyout);
            jstview = itemView.findViewById(R.id.jstview);

        }
    }

    private void updateSharedPref(String[] nameArr,String[] emailArr,String[] regidArr,String[] passArr){

        SharedPreferences sp =context.getSharedPreferences("spdb",MODE_PRIVATE);
        sp.edit().putString("name", TextUtils.join("#!!#",nameArr)).apply();
        sp.edit().putString("email", TextUtils.join("#!!#",emailArr)).apply();
        sp.edit().putString("regid", TextUtils.join("#!!#",regidArr)).apply();
        sp.edit().putString("password", TextUtils.join("#!!#",passArr)).apply();
    }
}