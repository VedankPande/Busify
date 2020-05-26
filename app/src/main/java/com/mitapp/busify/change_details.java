package com.mitapp.busify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class change_details extends AppCompatActivity {

    EditText Name,Stop,Phone;
    String bus_id;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_details);
        Toolbar my_toolbar = findViewById(R.id.action_bar);
        my_toolbar.setTitle("");
        setSupportActionBar(my_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Spinner spinner = (Spinner) findViewById(R.id.bus_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.buses_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ImageButton signup = findViewById(R.id.confirm_changes_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(change_details.this, MainActivity.class);
                startActivity(intent);
            }
        });


        Name = findViewById(R.id.signup_name);
        Stop = findViewById(R.id.details_stop);
        Phone = findViewById(R.id.signup_phone);

        findViewById(R.id.confirm_changes_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference();

                String name = Name.getText().toString();
                //String stop = Stop.getText().toString();
                String phone = Phone.getText().toString();
                User user = new User(name, "Home", "A", phone);
                Toast.makeText(change_details.this, "values:"+ " "+name, Toast.LENGTH_SHORT).show();
                reference.child("users").child(""+name).setValue(user);
                reference.child("users").child("test").child("value").setValue("helloworld");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


}
