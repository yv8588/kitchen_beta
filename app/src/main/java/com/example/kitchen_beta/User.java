package com.example.kitchen_beta;

public class User {
    private String user_id;
    private int type;
    private String user_name;
    public void User(String user_id,int type,String user_name){
        this.user_id=user_id;
        this.user_name=user_name;
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
