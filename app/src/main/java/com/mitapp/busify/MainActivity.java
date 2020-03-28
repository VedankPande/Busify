package com.mitapp.busify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean logged_in = false; //change this later
        super.onCreate(savedInstanceState);
        if(logged_in){
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(MainActivity.this, login_activity.class);
            startActivity(intent);
        }
    }
}
