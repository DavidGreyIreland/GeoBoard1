package com.example.project.geoboard1;

/**
 * Created by david on 19/01/2017.
 */

public class UserGeoBoardDatabase
{
    public String title;
    public String subject;
    public String message;
    public double lat;
    public double lon;

    public UserGeoBoardDatabase(String title, String subject, String message, double lat, double lon)
    {
        this.title = title;
        this.subject = subject;
        this.message = message;
        this.lon = lon;
        this.lat = lat;
    }
}
