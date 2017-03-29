package com.example.project.geoboard1;

/**
 * Created by david on 28/03/2017.
 */

public class ModelClass
{
    String title, subject, location;

    public ModelClass()
    {
    }

    public ModelClass(String title, String subject, String location)
    {
        this.title = title;
        this.subject = subject;
        this.location = location;
    }

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

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }
}
