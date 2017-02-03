package com.example.project.geoboard1;

import android.content.Intent;
import android.os.Bundle;
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
    String location;
    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Bundle retrievingBundle;
    Bundle passingBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_geo_board);

        title = (EditText)findViewById(R.id.geoBoardTitleEditText);
        subject = (EditText)findViewById(R.id.geoBoardSubjectEditText);
        message = (EditText)findViewById(R.id.geoBoardMessageTextArea);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        retrievingBundle = getIntent().getExtras();
    }


    public void saveGeoBoardInfo(View view)
    {
        String saveTitle = title.getText().toString().trim();
        String saveSubject = subject.getText().toString().trim();
        String saveMessage = message.getText().toString().trim();
        Boolean saveToDatabase = true;

        if(saveTitle.equals(null) || saveTitle.equals(""))
        {
            Toast.makeText(this, "Please enter a Title!", Toast.LENGTH_SHORT).show();
            saveToDatabase = false;
        }
        else if(saveSubject.equals(null) || saveSubject.equals(""))
        {
            Toast.makeText(this, "Please enter a Subject!", Toast.LENGTH_SHORT).show();
            saveToDatabase = false;
        }
        else if(saveMessage.equals(null) || saveMessage.equals(""))
        {
            Toast.makeText(this, "Please enter a Message!", Toast.LENGTH_SHORT).show();
            saveToDatabase = false;
        }
        else
        {
            saveToDatabase = true;
        }

        // TODO also needs to choose security setting and apply it before saving
        if(saveToDatabase)
        {
            //UserGeoBoardDatabase userGeoBoardDatabase = new UserGeoBoardDatabase(saveTitle, saveSubject, saveMessage, lat, lon);

            //FirebaseUser user = firebaseAuth.getCurrentUser();
            //databaseReference.child(user.getUid()).setValue(userGeoBoardDatabase);

            passingBundle = new Bundle();
            passingBundle.putString("location", retrievingBundle.getString("location"));

            Intent i = new Intent(getApplicationContext(), SecurityChoiceActivity.class);
            i.putExtras(passingBundle);

            startActivity(i);
        }


/*        if(saveToDatabase)
        {
            //UserGeoBoardDatabase userGeoBoardDatabase = new UserGeoBoardDatabase(saveTitle, saveSubject, saveMessage, lat, lon);

            //FirebaseUser user = firebaseAuth.getCurrentUser();
            //databaseReference.child(user.getUid()).setValue(userGeoBoardDatabase);
            Toast.makeText(this, "Info saved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MapsActivity.class));
        }*/
    }
}
