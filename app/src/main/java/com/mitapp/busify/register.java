package com.mitapp.busify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class register extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public EditText det_pwd,det_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar my_toolbar = findViewById(R.id.action_bar);
        my_toolbar.setTitle("");
        setSupportActionBar(my_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final ImageButton register = findViewById(R.id.goto_signup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(register.this, sign_up.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void signupfunction(View view)
    {
        mAuth = FirebaseAuth.getInstance();
        det_email = findViewById(R.id.signup_email);
        det_pwd = findViewById(R.id.signup_password);
        mAuth.createUserWithEmailAndPassword(det_email.getText().toString(),det_pwd.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(register.this, "You will be redirected to the details page", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), login_activity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}
