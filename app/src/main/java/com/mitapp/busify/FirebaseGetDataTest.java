package com.mitapp.busify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class FirebaseGetDataTest extends AppCompatActivity {
    FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    String UId = FirebaseAuth.getInstance().getUid();
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_get_data_test);
        text = findViewById(R.id.textView);
    }


    public void getFromFirebase(View view)
    {
        DocumentReference ref = mDB.collection("locations").document(UId);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        GeoPoint geoPoint = document.getGeoPoint("Location");
                        //Toast.makeText(FirebaseGetDataTest.this, geoPoint.toString(), Toast.LENGTH_SHORT).show();
                        text.setText(geoPoint.toString());
                        Map<String,Object> map = new HashMap<>();
                        map = document.getData();
                        Toast.makeText(FirebaseGetDataTest.this, map.get("Location").toString(), Toast.LENGTH_SHORT).show();

                    } else {

                    }
                } else {
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                }
            }


        });
    }
}
