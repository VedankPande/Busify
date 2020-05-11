package com.mitapp.busify;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;


public class FirestoreUserLocation {
    private GeoPoint geoPoint;
    private @ServerTimestamp Date timestamp;
    public FirestoreUserLocation() {
    }

    public FirestoreUserLocation(GeoPoint geoPoint, Date timestamp) {
        this.timestamp = timestamp;
        this.geoPoint = geoPoint;
    }

}
