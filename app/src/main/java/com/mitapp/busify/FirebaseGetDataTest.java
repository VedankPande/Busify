package com.mitapp.busify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FirebaseGetDataTest extends AppCompatActivity {
    FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    String UId = FirebaseAuth.getInstance().getUid();
    TextView text;
    private static final String TAG = "MyFirebaseMsgService";
    private RequestQueue requestQueue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseMessaging.getInstance().subscribeToTopic("busify")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "not subscribed";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_get_data_test);
        text = findViewById(R.id.textView);
        requestQueue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });
    }

    public void sendNotification()
    {
        JSONObject mainObject = new JSONObject();
        try {
            mainObject.put("to","/topic/"+"news");
            JSONObject notifObject= new JSONObject();
            notifObject.put("title","any title");
            notifObject.put("body","any body");
            mainObject.put("notification",notifObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, mainObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(FirebaseGetDataTest.this, "success", Toast.LENGTH_SHORT).show();
                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                            if (!task.isSuccessful()) {

                                                return;
                                            }

                                            // Get new Instance ID token
                                            String token = task.getResult().getToken();

                                            // Log and toast
                                            Log.d(TAG, "Token: " + token);

                                            Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                                            text.setText(token);
                                        }
                                    });

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(FirebaseGetDataTest.this, "failure", Toast.LENGTH_SHORT).show();
                }
            }
            )
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAziddBjU:APA91bFbgbTc_GhQThZhqCrBb5K_xvHgLWO3DIqspDLzNsP87huLcHKKrGxVRtqGiVGC0RNF-umUCo3dwD8SoU-0anZHSgqfruikrFtahIpbyPMfJ3Ot5uzSZ-Lqf0izVAEbdojnCXd6");
                    return header;
                }
            };
            requestQueue.add(request);

        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

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
