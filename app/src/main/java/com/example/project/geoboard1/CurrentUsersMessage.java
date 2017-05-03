package com.example.project.geoboard1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CurrentUsersMessage extends AppCompatActivity
{
    ListView listView;
    TextView messageBoardHeading;
    MessageDetails m;
    ArrayList<String> listViewArray;
    Button addMessageButton;
    String locationSearch, databaseLocations;
    DatabaseReference messageDetails, geoBoardRef, correctMessageReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_users_message);

        geoBoardRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geoboard1-33349.firebaseio.com/Messages").getRef();
        addMessageButton= (Button)findViewById(R.id.geoBoardAddButton);
        listView = (ListView) findViewById(R.id.listView);
        messageBoardHeading = (TextView)findViewById(R.id.messageBoardHeading);
        m = (MessageDetails)getApplicationContext();

        // initializes firebase object
        firebaseAuth = firebaseAuth.getInstance();

        setMessageBoardHeading();
        getMessages();
    }

    public void setMessageBoardHeading()
    {
        locationSearch = m.getMarkerLocation();
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
                    if (databaseLocations.equals(locationSearch))
                    {
                        correctMessageReference = snapshot.child("location").getRef().getParent();
                        messageBoardHeading.setText(snapshot.child("title").getValue(String.class));
                    }
                    else
                    {
                        System.out.println("wrong location");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void getMessages()
    {
        listViewArray = new ArrayList<>();
        locationSearch = m.getMarkerLocation();
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
                    if (databaseLocations.equals(locationSearch))
                    {
                        correctMessageReference = snapshot.child("message").getRef();
                        m.setCorrectMessageReference(correctMessageReference);
                        correctMessageReference.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                {
                                    listViewArray.add(snapshot.child("user").getValue(String.class) + "\n\n" + snapshot.child("msg").getValue(String.class));
                                    displayListView(listViewArray);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {
                            }
                        });
                    }
                    else
                    {
                        System.out.println("wrong location");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void displayListView(ArrayList<String> listViewArray)
    {
        this.listViewArray = listViewArray;

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(this, R.layout.single_row_list_view, R.id.textViewUserName, this.listViewArray);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(listViewAdapter);
    }

    public void addMessageButton(View view)
    {
        finish();
        Intent intent = new Intent(getApplication(), AddGeoBoard.class);

        if(getIntent().resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
    }

    public void backButton(View view)
    {
        finish();
        startActivity(new Intent(this, MapsActivity.class));
    }
}
