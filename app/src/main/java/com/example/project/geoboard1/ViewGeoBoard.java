package com.example.project.geoboard1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewGeoBoard extends AppCompatActivity
{
    private static final String TAG = "";
    TextView tv;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_geo_board);

        tv = (TextView)findViewById(R.id.textView);


        database = FirebaseDatabase.getInstance().getReference();

        database.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String output = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String title = snapshot.child("title").getValue(String.class);
                    String location = snapshot.child("location").getValue(String.class);

                    output += "Title: " + title + "\tLocation: " + location + "\n";
                }

                tv.setText(output);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }
}
