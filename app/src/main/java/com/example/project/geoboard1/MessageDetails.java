package com.example.project.geoboard1;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by david on 27/02/2017.
 */

public class MessageDetails extends Application
{
    String title;
    String subject;
    String message;
    String location, locationSearch;
    String userId;
    private String securityType;
    String locationId;
    String messageId;

    String markerLocation;
    String nfcId = "NFC";
    String databaseLocations;
    Random r = new Random();
    DatabaseReference geoBoardRef;

    public DatabaseReference getCorrectMessageReference()
    {
        return correctMessageReference;
    }

    public void setCorrectMessageReference(DatabaseReference correctMessageReference)
    {
        this.correctMessageReference = correctMessageReference;
    }

    DatabaseReference correctMessageReference;
    DatabaseReference currentMessageDR;
    FirebaseAuth firebaseAuth;
    private Vibrator v;

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

    public void saveUserDatabase()
    {
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(securityType.equals("NFC"))
        {
            geoBoardRef = FirebaseDatabase.getInstance().getReference();
            Map user = new HashMap();
            user.put("Location Id", location);
            user.put("Message Id", messageId);
            user.put("Security Type", securityType);
            user.put("Nfc Id", nfcId);
            geoBoardRef.child("Users").child(userId).push().setValue(user);
        }

        if(securityType.equals("NONE"))
        {
            geoBoardRef = FirebaseDatabase.getInstance().getReference();
            Map user = new HashMap();
            user.put("Location Id", location);
            user.put("Message Id", messageId);
            geoBoardRef.child("Users").child(userId).push().setValue(user);
        }

        if(v.hasVibrator())
        {
            v.vibrate(500);
        }
    }

    // saves to the database
    public void saveToDatabase()
    {
        String currentMessageDetails;
        firebaseAuth = firebaseAuth.getInstance();
        title = getTitle();
        subject = getSubject();
        message = getMessage();
        location = getLocation();
        userId = getUserId();
        nfcId = getNfcId();
        securityType = getSecurityType();
        messageId = messageId();
        FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
        geoBoardRef = FirebaseDatabase.getInstance().getReference();
        userFirebase.getUid();

        currentMessageDR = geoBoardRef.child("Messages").child(messageId).child("message").push();
        currentMessageDR.setValue(message);
        currentMessageDR.child("msg").setValue(message);
        currentMessageDR.child("user").setValue(userFirebase.getEmail());

        geoBoardRef.child("Messages").child(messageId).child("title").setValue(title);
        geoBoardRef.child("Messages").child(messageId).child("subject").setValue(subject);
        geoBoardRef.child("Messages").child(messageId).child("location").setValue(location);
        geoBoardRef.child("Messages").child(messageId).child("securityType").setValue(securityType);
        geoBoardRef.child("Messages").child(messageId).child("messageCreator").setValue(userFirebase.getEmail());

        if(securityType.equals("NFC"))
        {
            geoBoardRef.child("Messages").child(messageId).child("NFC Id").setValue(nfcId);
        }
        else
        {
            geoBoardRef.child("Messages").child(messageId).child("NFC Id").setValue("N/A");
        }

        saveUserDatabase();

        Toast.makeText(this, "Message saved:", Toast.LENGTH_SHORT).show();
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

    public String getMarkerLocation()
    {
        return markerLocation;
    }

    public void setMarkerLocation(String markerLocation)
    {
        this.markerLocation = markerLocation;
    }
}