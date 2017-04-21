package com.example.project.geoboard1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CurrentUsersMessage extends AppCompatActivity
{
    ListView listView;
    TextView messageBoardHeading;
    MessageDetails m;
    DatabaseReference messageBoardDetails;
    String msgDetails;
    Button addMessageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_users_message);

        addMessageButton= (Button)findViewById(R.id.addMessageButton);
        listView = (ListView) findViewById(R.id.listView);
        messageBoardHeading = (TextView)findViewById(R.id.messageBoardHeading);
        m = (MessageDetails)getApplicationContext();

        setMessageBoardHeading();
        getMessages();
    }

    private void setMessageBoardHeading()
    {
        messageBoardDetails = m.messageBoardDetails;

        messageBoardDetails.getParent().addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String messageTitle = "";

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    messageTitle = snapshot.child("title").getValue(String.class);
                }
                messageBoardHeading.setText(messageTitle);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void getMessages()
    {
        messageBoardDetails = m.messageBoardDetails;
        DatabaseReference messageDetails = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geoboard1-33349.firebaseio.com/Messages/messageId1637048/message");

        messageDetails.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    // each database location
                    msgDetails = snapshot.child("user").getValue(String.class) + "\n\n" + snapshot.child("msg").getValue(String.class);
                    displayListView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(CurrentUsersMessage.this, "Error with database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayListView()
    {
        String [] listViewArray = {msgDetails};

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(this, R.layout.single_row_list_view, R.id.textViewUserName, listViewArray);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(listViewAdapter);
    }

    public void addMessageButton(View view)
    {
        finish();
        startActivity(new Intent(this, AddGeoBoard.class));
    }
}
