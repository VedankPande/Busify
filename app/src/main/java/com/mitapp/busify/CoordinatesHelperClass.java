package com.mitapp.busify;

public class CoordinatesHelperClass {
    double latitude;
    double longitude;
    String route;

    public CoordinatesHelperClass() {

    }

    public CoordinatesHelperClass(String route, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.route = route;
    }
}

