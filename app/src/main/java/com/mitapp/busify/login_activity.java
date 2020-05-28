package com.mitapp.busify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class login_activity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1337;
    FirebaseAuth mAuth;
    EditText pre_email,pre_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        mAuth = FirebaseAuth.getInstance();
        pre_email = findViewById(R.id.login_activity_email_editText);
        pre_password = findViewById(R.id.login_activity_password_editText);

        createRequest();

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if(signInAccount!=null || mAuth.getCurrentUser() != null)
        {
            //SharedPreferences sharedPreferences = getSharedPreferences("system global variables",MODE_PRIVATE);
            //Boolean check_on_login_reg_bool = sharedPreferences.getBoolean("Registered",false);
                //if (check_on_login_reg_bool != true) {
                    startActivity(new Intent(getApplicationContext(), sign_up.class));
                    //setLoginBoolean();
                // else {
                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    //setLoginBoolean();
                //}


        }

        findViewById(R.id.login_activity_google_signin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        final Button signup = findViewById(R.id.login_activity_signup_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login_activity.this, register.class));
            }
        });
    }

    //Google signin functions

    private void createRequest() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //SharedPreferences sharedPreferences = getSharedPreferences("system global variables",MODE_PRIVATE);
                            //Boolean check_on_login_reg_bool = sharedPreferences.getBoolean("Registered",false);
                            // check driver login

                            // Sign in success, update UI with the signed-in user's information

                                //if (check_on_login_reg_bool != true) {
                                    startActivity(new Intent(getApplicationContext(), sign_up.class));
                                    //setLoginBoolean();

                                //} else {

                                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    //setLoginBoolean();

                                //}


                        } else {
                            Toast.makeText(login_activity.this, "Sorry auth failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    public void Email_login(View view)
    {

        mAuth = FirebaseAuth.getInstance();
        pre_email = findViewById(R.id.login_activity_email_editText);
        pre_password = findViewById(R.id.login_activity_password_editText);
        String email = pre_email.getText().toString();
        String password = pre_password.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            SharedPreferences sharedPreferences = getSharedPreferences("system global variables",MODE_PRIVATE);
                            Boolean check_on_login_reg_bool = sharedPreferences.getBoolean("Registered",false);
                            if(check_on_login_reg_bool!=true)
                            {
                                startActivity(new Intent(getApplicationContext(), sign_up.class));
                                //setLoginBoolean();
                            }
                            else
                            {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                //setLoginBoolean();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(login_activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            // ...
                        }

                        // ...
                    }
                });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setLoginBoolean()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("system global variables" ,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LoggedIn", true);
        editor.apply();
    }

    public Boolean checkForDriverLogin()
    {
        String check = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String[] ID = {"c6j3SIAX38W1uSWGBjDGeXmHBVw2"};
        for (String i : ID)
        {
            if(check.equals(i))
                return true;
        }
        return  false;
    }


}