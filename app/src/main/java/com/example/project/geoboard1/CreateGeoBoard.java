package com.example.project.geoboard1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGeoBoard extends AppCompatActivity
{
    EditText title, subject, message;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_geo_board);

        title = (EditText)findViewById(R.id.geoBoardTitleEditText);
        subject = (EditText)findViewById(R.id.geoBoardSubjectEditText);
        message = (EditText)findViewById(R.id.geoBoardMessageTextArea);
    }


    public void saveGeoBoardInfo(View view)
    {
        String saveTitle = title.getText().toString().trim();
        String saveSubject = subject.getText().toString().trim();
        String saveMessage = message.getText().toString().trim();
        Boolean sendToSecurityChoiceActivity = true;

        if(saveTitle.equals(null) || saveTitle.equals(""))
        {
            Toast.makeText(this, "Please enter a Title!", Toast.LENGTH_SHORT).show();
            sendToSecurityChoiceActivity = false;
        }
        else if(saveSubject.equals(null) || saveSubject.equals(""))
        {
            Toast.makeText(this, "Please enter a Subject!", Toast.LENGTH_SHORT).show();
            sendToSecurityChoiceActivity = false;
        }
        else if(saveMessage.equals(null) || saveMessage.equals(""))
        {
            Toast.makeText(this, "Please enter a Message!", Toast.LENGTH_SHORT).show();
            sendToSecurityChoiceActivity = false;
        }
        else
        {
            sendToSecurityChoiceActivity = true;
        }

        if(sendToSecurityChoiceActivity)
        {
            MessageDetails m = (MessageDetails)getApplicationContext();
            m.setTitle(saveTitle);
            m.setSubject(saveSubject);
            m.setMessage(saveMessage);

            Intent i = new Intent(getApplicationContext(), SecurityChoiceActivity.class);

            startActivity(i);
        }
    }
}
