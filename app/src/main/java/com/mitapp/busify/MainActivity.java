package com.mitapp.busify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean logged_in = false; //change this later
        super.onCreate(savedInstanceState);
        if(logged_in){
            setContentView(R.layout.activity_main);
        }
        else{
            setContentView(R.layout.activity_login_activity);
        }
    }
}
