package com.example.project.geoboard1;

import android.app.Application;

/**
 * Created by david on 27/02/2017.
 */

public class MessageDetails extends Application
{
    String title;
    String subject;
    String message;
    String location;
    String userId;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
}
