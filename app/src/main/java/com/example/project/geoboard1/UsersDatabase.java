package com.example.project.geoboard1;

/**
 * Created by david on 27/02/2017.
 */

public class UsersDatabase
{
    String messageId;
    String locationId;
    String nfcId;

    public UsersDatabase(String messageId, String locationId, String nfcId)
    {
        this.messageId = messageId;
        this.locationId = locationId;
        this.nfcId = nfcId;
    }
}
