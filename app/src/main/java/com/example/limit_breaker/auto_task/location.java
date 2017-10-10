package com.example.limit_breaker.auto_task;

/**
 * Created by the-limit-breaker on 7/10/17.
 */

public class location {
    double lat,lng;
    String name;
    int rating;

    public location(double lat, double lng, int rating,String name) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.rating = rating;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }
}
