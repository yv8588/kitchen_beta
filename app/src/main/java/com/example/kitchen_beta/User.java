package com.example.kitchen_beta;

public class User {
    private String user_id;
    private int type;
    private String user_name;
    /**
     *
     * @param user_id the users auth id.
     * @param type the user type.
     * @param user_name users name.
     */
    public  User(String user_id,int type,String user_name){
        this.user_id=user_id;
        this.user_name=user_name;
        this.type=type;
    }
    /**
     * default constractor.
     */
    public User() {
    }

    /**
     * @return the type of the user.
     */
    public int getType() {
        return type;
    }

    /**
     * @return the user id.
     */
    public String getUser_id() {
        return user_id;
    }

    /**
     * @return user name.
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * @param type user type.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @param user_id users id.
     */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    /**
     * @param user_name users name.
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
