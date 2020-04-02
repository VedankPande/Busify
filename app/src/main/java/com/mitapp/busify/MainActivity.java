package com.mitapp.busify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
    //boolean logged_in = true; //change this later to check from firebase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            nDrawerLayout = (DrawerLayout) findViewById(R.id.nav_menu);
            nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, R.string.open, R.string.close);

            nDrawerLayout.addDrawerListener(nToggle);
            nToggle.syncState();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (nToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.change_bus_preference){

        }
        if(id == R.id.bug_report){

        }
        if(id == R.id.logout_nav){
            //write you code here
            FirebaseAuth.getInstance().signOut();
            GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

            });

        }
        return false;
    }
}
