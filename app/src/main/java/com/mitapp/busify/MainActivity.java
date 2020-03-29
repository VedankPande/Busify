package com.mitapp.busify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    boolean logged_in = false; //change this later (check from firebase)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(logged_in){
            setContentView(R.layout.activity_main);
        }
        else{
            Intent intent = new Intent(MainActivity.this, login_activity.class);
            startActivity(intent);
        }
    }
}
