package com.mitapp.busify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "Busify";
    private final String ADMIN_CHANNEL_ID ="admin_channel";
    private final String MY_TOPIC = FirebaseAuth.getInstance().getUid();
    String destination;
    Intent resIntent,passengerIntent,driverIntent;
    PendingIntent pendingIntent;
    @Override
    public void onNewToken(@NonNull String s) {



        // Once the token is generated, subscribe to topic with the userId

        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        FirebaseMessaging.getInstance().subscribeToTopic(MY_TOPIC);
        //Log.i(TAG, "onTokenRefresh completed with token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        destination = remoteMessage.getData().get("destination");
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        if(destination.equals("passenger")) //if current notification is from driver to passenger
        {
            passengerIntent = new Intent(getApplicationContext(),MainActivity.class);
            passengerIntent.putExtra("location", remoteMessage.getData().get("location"));
            passengerIntent.putExtra("method", "marker");
            passengerIntent.putExtra("ID",remoteMessage.getData().get("ID"));
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, passengerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupChannels(notificationManager);
            }

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.busifylogofinal);

            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.drawable.busifylogofinal)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("message")))
                    .setContentIntent(pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            notificationManager.notify(notificationID, notificationBuilder.build());

        }
        else if(destination.equals("driver")) //if current notification is from passenger to driver
        {
            driverIntent = new Intent(getApplicationContext(),Request_Stop_Respond.class);
            driverIntent.putExtra("location", remoteMessage.getData().get("location"));
            driverIntent.putExtra("method", "marker");
            driverIntent.putExtra("ID",remoteMessage.getData().get("ID"));
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, driverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupChannels(notificationManager);
            }

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.busifylogofinal);

            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.drawable.busifylogofinal)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("message")))
                    .setContentIntent(pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            notificationManager.notify(notificationID, notificationBuilder.build());

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
