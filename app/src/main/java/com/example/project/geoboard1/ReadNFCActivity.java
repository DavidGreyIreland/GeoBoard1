package com.example.project.geoboard1;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class ReadNFCActivity extends AppCompatActivity
{
    NfcAdapter nfcAdapter;
    Random r = new Random();
    private String nfcId, databaseLocations;
    private MessageDetails m;
    FirebaseAuth firebaseAuth;
    DatabaseReference geoBoardRef, correctMessageReference;
    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_nfc);

        m = (MessageDetails)getApplicationContext();
        firebaseAuth = firebaseAuth.getInstance();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        geoBoardRef = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        enableForegroundDispatchSystem();
    }


    private void enableForegroundDispatchSystem()
    {
        Intent i = new Intent(this, ReadNFCActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pI = PendingIntent.getActivity(this, 0 ,i, 0);
        IntentFilter[] iF = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pI, iF, null);
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        disableForegroundDispatchSystem();
    }


    private void disableForegroundDispatchSystem()
    {
        nfcAdapter.disableForegroundDispatch(this);
    }

    private String getNFCId()
    {
        geoBoardRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geoboard1-33349.firebaseio.com/Messages");
        geoBoardRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    // each database location
                    databaseLocations = snapshot.child("location").getValue(String.class);

                    // comparing database locations with marker location
                    if (databaseLocations.equals(m.getMarkerLocation()))
                    {

                        correctMessageReference = snapshot.child("location").getRef().getParent();
                        correctMessageReference.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                nfcId = dataSnapshot.child("NFC Id").getValue(String.class);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
        return nfcId;
    }


    @Override
    protected void onNewIntent(Intent i)
    {
        super.onNewIntent(i);

        if(i.hasExtra(NfcAdapter.EXTRA_TAG))
        {
            Parcelable[] parcelables = i.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(parcelables != null && parcelables.length > 0)
            {
                String nfcTagId = readNFCTag((NdefMessage)parcelables[0]);

                if(nfcTagId.equals(getNFCId()))
                {
                    v = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(500);
                    Intent intent = new Intent(getApplicationContext(), CurrentUsersMessage.class);

                    startActivity(intent);
                    finish();
                }
            }
            else
            {
                Toast.makeText(this, "No ID Found", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private String readNFCTag(NdefMessage ndefMessage)
    {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        String tagId = "";

        if(ndefRecords != null && ndefRecords.length > 0)
        {
            NdefRecord ndefRecord = ndefRecords[0];
            tagId = getNFCText(ndefRecord);
        }
        else
        {
            tagId = "no id";
            Toast.makeText(this, "No message found!", Toast.LENGTH_SHORT).show();
        }
        return tagId;
    }

    public String getNFCText(NdefRecord ndefRecord)
    {
        String text = "";
        try
        {
            byte[] payload = ndefRecord.getPayload();
            String encode = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            text = new String(payload, languageSize + 1, payload.length - languageSize -1, encode);
        }
        catch(UnsupportedEncodingException e)
        {
            Log.e("getNFCText", e.getMessage(), e);
        }
        return text;
    }
}