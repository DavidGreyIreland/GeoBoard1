package com.example.project.geoboard1;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

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
    String securityType = "NFC";
    String locationId;
    String messageId;
    String nfcId = "NFC";
    Random r = new Random();
    private DatabaseReference databaseRef;

    // creates a unique location ID
    public String messageId()
    {
        final int min = 1000000;
        final int max = 10000000;
        int randomNumber = r.nextInt((max - min) + 1) + min;

        // NFC defines which security feature to use when reading GeoBoards.
        messageId = "messageId" + randomNumber;
        return messageId;
    }

    // creates a unique NFC ID/Location ID
    public String locationId()
    {
        location = getLocation();

        // removed '.' with '-'
        String enhancedLocation = location.replace(".", "-");
        final int min = 10000;
        final int max = 100000;
        int randomNumber = r.nextInt((max - min) + 1) + min;

        locationId = randomNumber + ":" + enhancedLocation + getSecurityType();

        // this should give : "14328:lat105-123lon85-321:NFC"
        return locationId;
    }

    // saves to the database
    public void saveToDatabase()
    {
        databaseRef = FirebaseDatabase.getInstance().getReference();

        messageId();
        locationId();

        // perfect
        databaseRef.child("Messages").child(messageId).child("message").setValue(getMessage());
        databaseRef.child("Messages").child(messageId).child("securityType").setValue(securityType);
        DatabaseReference dMessageId = databaseRef.child("Messages").child(messageId);
        //databaseRef.child("Users").child(getUserId()).child("locationId").setValue(locationId());
        //databaseRef.child("Users").child(getUserId()).child("messageId").setValue(messageId());
        //databaseRef.child("Users").child(getUserId()).push().child("locationId").setValue(locationId);
        //databaseRef.child("Users").child(getUserId()).push().child("messageId").setValue(messageId);
        UsersDatabase u = new UsersDatabase(locationId, messageId, nfcId);

        databaseRef.child("Users").child(getUserId()).push().setValue(u);
        //Map<String, UsersDatabase> users = new HashMap<>();
        //users.put("User",new UsersDatabase(locationId, messageId));
        //usersRef.setValue(users);
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

    public String getSecurityType()
    {
        return securityType;
    }

    public void setSecurityType(String securityType)
    {
        this.securityType = securityType;
    }

    public String getNfcId()
    {
        return nfcId;
    }

    public void setNfcId(String nfcId)
    {
        this.nfcId = nfcId;
    }
}
