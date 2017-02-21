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
    long databaseInfo;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_geo_board);

        tv = (TextView)findViewById(R.id.textView);

        //database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geoboard1-33349.firebaseio.com/geoBoardId7084254NFC/message");
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geoboard1-33349.firebaseio.com/geoBoardId7084254NFC/message");
        database.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String value =  dataSnapshot.getValue(String.class);

                //Map<String, String> map = dataSnapshot.getValue(Map.class);
                //String title = map.get("title");
                //String subject = map.get("subject");
                //tv.setText(title + "\n" + subject);
                tv.setText(value);
                databaseInfo = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

/*        ValueEventListener postListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Map<Object, Map<String, Object>> data = (Map<Object, Map<String, Object>>)dataSnapshot.getValue();
                String message = "message: " + data.get("message");

                // gets the amount of objects in the database
                //String message = "message: " + data.containsKey("geoBoardId7084254NFC");
                tv.setText(message);
                databaseInfo = dataSnapshot.getChildrenCount();
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        database.addValueEventListener(postListener);
*/

/*
        tv = (TextView)findViewById(R.id.textView);
        tv.setText(databaseInfo);

        tv = (TextView)findViewById(R.id.textView);
        tv.setText(databaseInfo);
*/
    }
}
