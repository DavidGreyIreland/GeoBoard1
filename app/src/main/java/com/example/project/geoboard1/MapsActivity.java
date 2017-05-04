package com.example.project.geoboard1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener
{
    double lat;
    double lon;
    Location location;
    LatLng currentLocation;
    private GoogleMap mMap;
    private Marker currentLocationMarker, marker;
    Button buttonLogout;
    String markerSecurityType, markerPosition, databaseLocations, markerTitle, dbSecurityType;
    private DatabaseReference database, geoBoardRef, correctMessageReference;
    private FirebaseAuth firebaseAuth;
    TextView textViewLocation;
    MessageDetails m;
    private double markerLat, markerLng;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        m = (MessageDetails)getApplicationContext();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        buttonLogout = (Button)findViewById(R.id.buttonLogout);

        // initializes firebase object
        firebaseAuth = firebaseAuth.getInstance();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }


        try
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            setLat(location.getLatitude());
            setLon(location.getLongitude());
        } catch(SecurityException ex)
        {
            ex.printStackTrace();
        }

        currentLocation();
        geoBoardLocations();

        textViewLocation = (TextView)findViewById(R.id.textViewLocation);

        // replaces "lat/lng:" with "Current Location"
        textViewLocation.setText(currentLocation.toString().replaceFirst("lat/lng:", "Current Location"));
    }

    private void geoBoardLocations()
    {
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geoboard1-33349.firebaseio.com/Messages");
        database.addListenerForSingleValueEvent(new ValueEventListener()
        {
            MarkerOptions markerOptions;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                ArrayList<LatLng> locationLists = new ArrayList<LatLng>();
                ArrayList<String> titleLists = new ArrayList<String>();
                ArrayList<String> subjectLists = new ArrayList<String>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    /************************************** location markers *******************************/
                    int latStart, latEnd, lonStart, lonEnd;
                    String lat, lon;
                    String location = "";

                    try
                    {
                        location = snapshot.child("location").getValue(String.class);
                    }catch(Exception e)
                    {
                        System.out.println("Locations not found for markers to be added");
                    }

                    latStart = 3;
                    latEnd = location.indexOf('o') - 1;
                    lonStart = location.indexOf('n') + 1;
                    lonEnd = location.length();
                    lat = location.substring(latStart, latEnd);
                    lon = location.substring(lonStart, lonEnd);
                    locationLists.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)));

                    /************************************** title markers *******************************/
                    String title = snapshot.child("title").getValue(String.class);
                    titleLists.add("Title: " + title);

                    /************************************** subject markers *******************************/
                    String subject = snapshot.child("title").getValue(String.class);
                    subjectLists.add("Subject: " + subject);
                }

                for (int i = 0; i < locationLists.size(); i++)
                {
                    mMap.addMarker(new MarkerOptions()
                            .position(locationLists.get(i))
                            .title(titleLists.get(i))
                            .snippet(subjectLists.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.geoboardlogo2)));
                    mMap.setOnMarkerClickListener(MapsActivity.this);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void currentLocation()
    {
        currentLocation = new LatLng(getLat(), getLon());
        currentLocationMarker = mMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.person))
                .title("GeoBoardTitle")
                .snippet("Click to Open"));
        currentLocationMarker.showInfoWindow();
        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public double getLat()
    {
        return lat;
    }


    public void setLat(double lat)
    {
        this.lat = lat;
    }


    public double getLon()
    {
        return lon;
    }


    public void setLon(double lon)
    {
        this.lon = lon;
    }


    private void myLocation(Location location)
    {
        lat = location.getLatitude();
        lon = location.getLongitude();
    }


    /****************************************************************************************************************/
    /***************** if within location of marker, appropriate security activity is called ************************/
    /****************************************************************************************************************/
    @Override
    public boolean onMarkerClick(Marker marker)
    {
        markerLat = marker.getPosition().latitude;
        markerLng = marker.getPosition().longitude;
        markerTitle = marker.getTitle();

        m.setMarkerLocation("lat" + markerLat + "lon" + markerLng);
        // checks if the current location is within range of the desired marker
        if(markerLat - currentLocationMarker.getPosition().latitude < 0.05 && markerLng - currentLocationMarker.getPosition().longitude < 0.05 )
        {
            geoBoardRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geoboard1-33349.firebaseio.com/Messages");
            geoBoardRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    dbSecurityType = "";
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        // each database location of currently clicked messageBoard
                        databaseLocations = snapshot.child("location").getValue(String.class);

                        // comparing database locations with marker location
                        if (databaseLocations.equals(m.getMarkerLocation()))
                        {
                            dbSecurityType = snapshot.child("securityType").getValue(String.class);
                        }
                    }
                    setSecurityType(dbSecurityType);
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }
        else
        {
            Toast.makeText(this, "Location Incorrect", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public void setSecurityType(String securityType)
    {
        if(securityType.equals("NONE"))
        {
            Intent i = new Intent(getApplicationContext(), CurrentUsersMessage.class);
            m.setMarkerLocation("lat" + markerLat + "lon" + markerLng);
            i.putExtra("markerTitle",markerTitle);

            Toast.makeText(this, "none!!!", Toast.LENGTH_SHORT).show();

            startActivity(i);
            finish();
        }
        else if(securityType.equals("NFC"))
        {
            Intent i = new Intent(getApplicationContext(), ReadNFCActivity.class);
            m.setMarkerLocation("lat" + markerLat + "lon" + markerLng);

            startActivity(i);
            finish();
        }
        else if(securityType.equals("WrongLocation"))
        {
            finish();
            startActivity(new Intent(getApplication(), MapsActivity.class));
            Toast.makeText(this, "Location Access Incorrect!!!", Toast.LENGTH_SHORT).show();
        }
    }


    // views a list of users GeoBoards
    public void viewGeoBoard(View v)
    {
        finish();
        startActivity(new Intent(getApplication(), UserMessageListActivity.class));
    }


    // creates GeoBoards
    public void createGeoBoard(View v)
    {
        String location = "lat" + getLat() + "lon" + getLon();

        m.setLocation(location);
        m.setUserId(firebaseAuth.getCurrentUser().getUid());

        Intent i = new Intent(getApplicationContext(), CreateGeoBoard.class);
        startActivity(i);
    }


    public void logOut(View view)
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(view == buttonLogout)
        {
            Toast.makeText(this, user.getEmail() + " has logged out!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}