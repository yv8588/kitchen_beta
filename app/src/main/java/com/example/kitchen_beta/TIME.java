package com.example.kitchen_beta;


public class TIME {
    /**
     * makes a string time into int;
     * @param t time in string.
     * @return the time in int.
     */
    public static int TimetoInt(String t){
        int time=Integer.parseInt(t);
        return time;
    }

    /**
     * makes a int time to a string of the time.
     * @param t time i int.
     * @return the time in string.
     */
    public static String TimeToString(Integer t){
        String time=t.toString();
        return time;
    }

    /**
     *
     * @param t time in string.
     * @return time in int.
     */
    public static String TimeToClear(String t){
        return t.substring(0,4)+"/"+t.substring(4,8)+"/"+t.substring(8,12);
    }
}
