package com.example.kitchen_beta;

public class ID_CREATOR {
    /**
     * generate a bon id using waiter id and time.
     * @param UID the waiter id.
     * @param time the time the bon was published.
     * @return the bon id.
     */
    public static String getID(String UID,String time){
        return UID+time;
    }
}
