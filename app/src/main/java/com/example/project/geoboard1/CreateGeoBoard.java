package com.example.project.geoboard1;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGeoBoard extends AppCompatActivity
{
    EditText title, subject, message;
    private double lat, lon;
    Location location;
    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_geo_board);

        title = (EditText)findViewById(R.id.geoBoardTitleEditText);
        subject = (EditText)findViewById(R.id.geoBoardSubjectEditText);
        message = (EditText)findViewById(R.id.geoBoardMessageTextArea);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void createGeoBoard(View view)
    {
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                myLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
            {

            }
        };

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }


        try
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            lat = location.getLatitude();
            lon = location.getLongitude();
        } catch(SecurityException ex)
        {
            ex.printStackTrace();
        }

        //TODO surround with an if statement for when the user finishes the security checks first then they save
        saveGeoBoardInfo();
    }

    private void myLocation(Location location)
    {
        lat = location.getLatitude();
        lon = location.getLongitude();
    }

    private void saveGeoBoardInfo()
    {
        String saveTitle = title.getText().toString().trim();
        String saveSubject = subject.getText().toString().trim();
        String saveMessage = message.getText().toString().trim();

        //UserGeoBoardDatabase userGeoBoardDatabase = new UserGeoBoardDatabase(saveTitle, saveSubject, saveMessage, lat, lon);

        //FirebaseUser user = firebaseAuth.getCurrentUser();
        //databaseReference.child(user.getUid()).setValue(userGeoBoardDatabase);
        Toast.makeText(this, "Info saved", Toast.LENGTH_SHORT).show();
    }
}
