package com.example.kitchen_beta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Meal>mealList;
    LayoutInflater inflater;
    public CustomAdapter(Context context,ArrayList<Meal>mealList,LayoutInflater inflater){
        this.context=context;
        this.mealList=mealList;
        this.inflater=inflater;
    }
    @Override
    public int getCount() {
        return mealList.size();
    }

    @Override
    public Object getItem(int i) {
        return mealList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.custom_lv,null);
        TextView price=(TextView) view.findViewById(R.id.mPrice);
        TextView name=(TextView) view.findViewById(R.id.mName);
        TextView des=(TextView) view.findViewById(R.id.desc);
        ImageView photo=(ImageView) view.findViewById(R.id.mImage);
        Meal m=mealList.get(i);
        des.setText(m.getAbout());
        name.setText(m.getName());
        //price.setText();
        // set photo from fb here.
        return view;
    }
}
