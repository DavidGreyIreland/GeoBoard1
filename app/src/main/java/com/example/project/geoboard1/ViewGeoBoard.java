package com.example.project.geoboard1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    MessageDetails m;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_geo_board);

        tv = (TextView)findViewById(R.id.textView);
        m = (MessageDetails)getApplicationContext();

        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geoboard1-33349.firebaseio.com/Messages");


        database.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                firebaseAuth = firebaseAuth.getInstance();
                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
                String output = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String firebaseCurrentUser = userFirebase.getEmail();
                    String user = snapshot.child("User").getValue(String.class);
                    if(user.equals(firebaseCurrentUser))
                    {
                        String title = snapshot.child("Title").getValue(String.class);
                        String location = snapshot.child("Location").getValue(String.class);

                        output += "Title: " + title + "\nLocation: " + location + "\n\n";
                    }
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
