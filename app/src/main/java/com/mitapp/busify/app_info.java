package com.mitapp.busify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class app_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        Toolbar my_toolbar = findViewById(R.id.action_bar);
        my_toolbar.setTitle("");
        setSupportActionBar(my_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.linkedin1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                try {
                    intent.setData(Uri.parse("https://in.linkedin.com/in/pshantanu"));
                    startActivity(intent);
                } catch (ActivityNotFoundException exception) {
                    Toast.makeText(getApplicationContext(), "Error text", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.linkedin2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                try {
                    intent.setData(Uri.parse("https://www.linkedin.com/in/vedank-pande-3b226317b"));
                    startActivity(intent);
                } catch (ActivityNotFoundException exception) {
                    Toast.makeText(getApplicationContext(), "Error text", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
