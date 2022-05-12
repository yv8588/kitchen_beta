package com.example.kitchen_beta;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterspin extends BaseAdapter {
    Context context;
    ArrayList<Bon> bonlist;
    LayoutInflater inflater;

    /**
     * constractor.
     * @param applicationContext app context
     * @param  bonlist the list.
     */
    public CustomAdapterspin(Context applicationContext,ArrayList<Bon> bonlist){
        this.context=applicationContext;
        this.bonlist=bonlist;
        inflater=(LayoutInflater.from(applicationContext));
    }

    /**
     * @return the list size.
     */
    @Override
    public int getCount() {
        return bonlist.size();
    }

    /**
     *
     * @param i the item id.
     * @return the place in the list of the  object.
     */
    @Override
    public Object getItem(int i) {
        return null;
    }

    /**
     *
     * @param i the item in the list.
     * @return the id
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     *
     * @param i place clicked in list.
     * @param view the view in the list(row).
     * @param viewGroup
     * @return the view.
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.custom_spinner,null);
        TextView date=(TextView) view.findViewById(R.id.tvDate);
        TextView desert=(TextView) view.findViewById(R.id.tvDesert);
        TextView drink=(TextView) view.findViewById(R.id.tvDrink);
        TextView first=(TextView) view.findViewById(R.id.tvFirst);
        TextView main=(TextView) view.findViewById(R.id.tvMain);
        Bon b=bonlist.get(i);
        if(b!=null) {
            int mainCount = 0;
            int firstCount = 0;
            int desertCount = 0;
            int drinkcount = 0;
            ArrayList<Meal> bons = b.getB();
            for (int j = 0; j < bons.size(); j++) {
                Meal tmp = bons.get(i);
                if (tmp.getCategory().equals("first")) {
                    firstCount++;
                } else if (tmp.getCategory().equals("main")) {
                    mainCount++;
                } else if (tmp.getCategory().equals("desert")) {
                    desertCount++;
                } else if (tmp.getCategory().equals("drink")) {
                    drinkcount++;
                }
            }
            date.setText(TIME.TimeToClear(b.getTime()));
            desert.setText("deserts" + desertCount);
            drink.setText("drinks" + drinkcount);
            first.setText("first meals" + firstCount);
            main.setText("main meal" + mainCount);
        }
        return view;
    }
}
