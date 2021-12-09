package com.example.kitchen_beta;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TIME {
    String time1,date1;
    int time2,date2;

    /**
     * constructor for time maker (in int or string).
     */
    public TIME(){
        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        StringBuilder t=new StringBuilder(date.toString());
        for(int i=0;i<10;i++){
            t.deleteCharAt(i+2);
        }
        date1=t.toString();
        date2=Integer.parseInt(t.toString());
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
        Date time=new Date();
        StringBuilder t2=new StringBuilder(time.toString());
        for(int j=0;j<10;j++){
            t2.deleteCharAt(j+2);
        }
        time1=t2.toString();
        time2=Integer.parseInt(t2.toString());
    }

    /**
     *
     * @return the date in string.
     */
    public int getIntDate() {
        return date2;
    }
    /**
     *
     * @return the date in int.
     */
    public String getStringDate() {
         return date1;
    }
    /**
     *
     * @return the time in string.
     */
    public String getStringTime() {
        return time1;
    }

    /**
     *
     * @return the time in int.
     */
    public int getIntTime() {
        return time2;
    }
}
