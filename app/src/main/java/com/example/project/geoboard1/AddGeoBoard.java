package com.example.project.geoboard1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class AddGeoBoard extends AppCompatActivity
{
    EditText message;
    FirebaseAuth firebaseAuth;
    MessageDetails m;
    private Vibrator v;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_geo_board);

        firebaseAuth = firebaseAuth.getInstance();
        message = (EditText)findViewById(R.id.geoBoardMessageTextArea);
        m = (MessageDetails)getApplicationContext();
    }


    public void saveGeoBoardInfo(View view)
    {
        String saveMessage = message.getText().toString().trim();

        if(saveMessage.equals(null) || saveMessage.equals(""))
        {
            Toast.makeText(this, "Please enter a Message!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            DatabaseReference messageDetails = m.getCorrectMessageReference().push();

            messageDetails.child("msg").setValue(saveMessage);
            messageDetails.child("user").setValue(user.getEmail());

            v = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);

            Intent i = new Intent(getApplicationContext(), CurrentUsersMessage.class);
            startActivity(i);
        }
    }


    public void backButton(View view)
    {
        finish();
        startActivity(new Intent(this, MapsActivity.class));
    }
}
