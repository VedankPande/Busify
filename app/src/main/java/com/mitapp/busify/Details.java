package com.mitapp.busify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Details extends AppCompatActivity {
    EditText Name, Bus,Stop,Phone;
    FirebaseDatabase database;
    DatabaseReference reference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Name = findViewById(R.id.details_name);
        Stop = findViewById(R.id.details_stop);
        Bus = findViewById(R.id.details_bus);
        Phone = findViewById(R.id.editText2);

        findViewById(R.id.details_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference();




                String name = Name.getText().toString();
                String bus = Bus.getText().toString();
                String stop = Stop.getText().toString();
                String phone = Phone.getText().toString();
                User user = new User(name, stop, bus, phone);
                Toast.makeText(Details.this, "values:"+ " "+name+" "+bus, Toast.LENGTH_SHORT).show();
                reference.child("users").child(""+name).setValue(user);

                /*Intent intent = new Intent(getApplicationContext(), phone_verification.class);
                intent.putExtra("phoneNo",phone);
                startActivity(intent);*/



            }
        });


    }

    public void details_next(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void details_logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getApplicationContext(), login_activity.class));
            }

        });
    }




}
