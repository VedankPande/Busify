package com.mitapp.busify;

public class User {
    public String Stop;
    public String Bus_Letter;
    public String Name;
    public String Phone;
    public User(){

    }
    public User(String Name, String Stop, String Bus_Letter, String Phone)
    {
        this.Phone = Phone;
        this.Stop = Stop;
        this.Bus_Letter = Bus_Letter;
        this.Name = Name;
    }
}
