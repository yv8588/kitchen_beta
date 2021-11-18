package com.example.kitchen_beta;

import java.util.ArrayList;

public class bon {
    private String time;
    private ArrayList<meal>b;
    private String date;
    private boolean above;

    /**
     * creats new bon.
     * @param time the time the bon was created.
     * @param b the meals in the bon.
     * @param date the date the bon was created.
     * @param above
     */
    public void Bon(String time,ArrayList<meal>b,String date,boolean above){
        this.time=time;
        this.b=b;
        this.date=date;
        this.above=above;
    }

    /**
     * @return the meals in the bon.
     */
    public ArrayList<meal> getB() {
        return b;
    }

    /**
     * @return the date of the bon.
     */
    public String getDate() {
        return date;
    }

    /**
     * @return the time of the bon.
     */
    public String getTime() {
        return time;
    }

    /**
     * @return if there is any perferation
     */
    public boolean isAbove() {
        return above;
    }

    /**
     * sets the Array list of meals.
     * @param b the Array list of meals.
     */
    public void setB(ArrayList<meal> b) {
        this.b = b;
    }

    /**
     * sets the date of the bon.
     * @param date the date the bon was created.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * sets the time of the bon
     * @param time the time the bon was created.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * sets if there is any perferation in this bon.
     * @param above
     */
    public void setAbove(boolean above) {
        this.above = above;
    }
}
