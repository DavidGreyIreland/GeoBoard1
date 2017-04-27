package com.example.project.geoboard1;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Random;

public class NfcSecurity extends AppCompatActivity
{
    NfcAdapter nfcAdapter;
    Random r = new Random();
    private String location, geoBoardId, messageId;
    private MessageDetails m;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_security);

        m = (MessageDetails)getApplicationContext();
        firebaseAuth = firebaseAuth.getInstance();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        enableForegroundDispatchSystem();
    }


    private void enableForegroundDispatchSystem()
    {
        Intent i = new Intent(this, NfcSecurity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
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


    @Override
    protected void onNewIntent(Intent i)
    {
        super.onNewIntent(i);

        if(i.hasExtra(NfcAdapter.EXTRA_TAG))
        {
            Toast.makeText(this, "NfcIntent! Security", Toast.LENGTH_SHORT).show();

            Tag t = i.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage message = createNdefMessage(createNFCId());

            writeMessage(t, message);
        }
    }


    // creates a unique NFC ID/Location ID
    private String createNFCId()
    {
        location = m.getLocation();

        // removed '.' with '-'
        String enhancedLocation = location.replace(".", "-");
        final int min = 1000;
        final int max = 10000;
        int randomNumber = r.nextInt((max - min) + 1) + min;

        // NFC defines which security feature to use when reading GeoBoards.
        geoBoardId = randomNumber + ":" + enhancedLocation;
        m.setNfcId(geoBoardId);
        // this should give : "14328:lat105-123lon85-321:NFC"
        return geoBoardId;
    }

    // creates a unique location ID
    private void createMessageId()
    {
        final int min = 1000000;
        final int max = 10000000;
        int randomNumber = r.nextInt((max - min) + 1) + min;

        // NFC defines which security feature to use when reading GeoBoards.
        messageId = "messageId" + randomNumber;
    }


    private void formatTag(Tag t, NdefMessage message)
    {
        try
        {
            NdefFormatable ndefF = NdefFormatable.get(t);

            if(ndefF == null)
            {
                Toast.makeText(this, "Tag isn't formatable", Toast.LENGTH_SHORT).show();
                createNFCId();
            }

            ndefF.connect();
            ndefF.format(message);
            ndefF.close();
        }
        catch(Exception e)
        {
            Log.e("format Tag", e.getMessage());
        }
    }


    private void writeMessage(Tag t, NdefMessage message)
    {
        boolean tagWritten = false;
        try
        {
            NdefFormatable ndefF = NdefFormatable.get(t);

            if(t == null)
            {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }


            Ndef ndef = Ndef.get(t);


            if(t == null)
            {
                Toast.makeText(this, "Tag is formatting", Toast.LENGTH_SHORT).show();

                formatTag(t, message);
            }
            else
            {
                ndef.connect();

                if(!ndef.isWritable())
                {
                    Toast.makeText(this, "Tag is not writable", Toast.LENGTH_SHORT).show();

                    ndef.close();
                    return;
                }
                ndef.writeNdefMessage(message);
                ndef.close();

                tagWritten = true;
                if(tagWritten)
                {
                    createMessageId();
                    m.setSecurityType("NFC");
                    m.saveToDatabase();

                    Toast.makeText(this, "Tag is written Security", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(this, MapsActivity.class));
                }
            }
        }
        catch(Exception e)
        {
            Log.e("write message", e.getMessage());
        }
    }


    private NdefRecord createTextRecord(String s)
    {
        try
        {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            byte[] text = s.getBytes("UTF-8");
            int languageSize = language.length;
            int length = text.length;
            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + length);

            payload.write((byte)(languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, length);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
        }
        catch(UnsupportedEncodingException e)
        {
            Log.e("create Text Record", e.getMessage());
        }
        return null;
    }


    private NdefMessage createNdefMessage(String s)
    {
        NdefRecord record = createTextRecord(s);
        NdefMessage message = new NdefMessage(new NdefRecord[]
                {
                        record
                });
        return message;
    }
}