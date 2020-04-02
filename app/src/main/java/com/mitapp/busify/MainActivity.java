package com.mitapp.busify;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
    boolean logged_in = false; //change this later to check from firebase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(logged_in){
            setContentView(R.layout.activity_main);

            nDrawerLayout = (DrawerLayout) findViewById(R.id.nav_menu);
            nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, R.string.open, R.string.close);

            nDrawerLayout.addDrawerListener(nToggle);
            nToggle.syncState();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Button logout = (Button) findViewById(R.id.logout_nav);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        else{
            Intent intent = new Intent(MainActivity.this, login_activity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (nToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
