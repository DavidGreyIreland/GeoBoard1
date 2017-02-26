package com.example.project.geoboard1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SecurityChoiceActivity extends AppCompatActivity implements View.OnClickListener
{
    Button buttonLogout;
    Button buttonSecurity;
    TextView textViewUserEmail;
    FirebaseAuth firebaseAuth;
    Bundle retrievingBundle;
    Bundle passingBundle;
    private String location, geoBoardId, messageId, title, subject, userMessage, currentUser;
    private DatabaseReference geoBoardRef;

    /***************************************************************************************/
    /****************************** Security setting choices *******************************/
    /***************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_choice);

        buttonLogout= (Button)findViewById(R.id.buttonLogout);
        buttonSecurity = (Button)findViewById(R.id.buttonSecurity);
        textViewUserEmail = (TextView)findViewById(R.id.textViewUserEmail);

        buttonLogout.setOnClickListener(this);
        buttonSecurity.setOnClickListener(this);

        // initializes firebase object
        firebaseAuth = firebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail.setText("welcome " + user.getEmail());
    }


    /***************************************************************************************/
    /**************** Dealing with all the buttons clicked in this activity *****************/
    /***************************************************************************************/
    @Override
    public void onClick(View view)
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(view == buttonLogout)
        {
            Toast.makeText(this, user.getEmail() + " has logged out!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        if(view == buttonSecurity)
        {
            RadioButton radioButton;
            RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroupSecurityOptions);

            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton)findViewById(selectedId);
            String radioButtonSecurityResult = radioButton.getText().toString();

            if(radioButtonSecurityResult.equals("Security Setting (None)"))
            {
                finish();

                //get an instance of the firebaseAuth
                firebaseAuth = firebaseAuth.getInstance();
                retrievingBundle = getIntent().getExtras();
                title = retrievingBundle.getString("title");
                subject = retrievingBundle.getString("subject");
                userMessage = retrievingBundle.getString("message");
                currentUser = firebaseAuth.getCurrentUser().getUid();
/*
        geoBoardRef = FirebaseDatabase.getInstance().getReference();

        // perfect
        geoBoardRef.child("Messages").child(messageId).child("message").setValue(userMessage);
        geoBoardRef.child("Messages").child(messageId).child("securityType").setValue("NFC");

        //geoBoardRef.child("Users").child(currentUser).child("userId").setValue(currentUser);
        geoBoardRef.child("Users").child(currentUser).child("locationId").setValue(geoBoardId);
        geoBoardRef.child("Users").child(currentUser).child("messageId").setValue(messageId);
*/

                // Write a message to the database
                geoBoardRef = FirebaseDatabase.getInstance().getReference("asdf");

                // sets geoBoardId for child Strings
                geoBoardRef.child("location").setValue(location);
                geoBoardRef.child("message").setValue(userMessage);
                geoBoardRef.child("userId").setValue(currentUser);
                geoBoardRef.child("title").setValue(title);
                geoBoardRef.child("subject").setValue(subject);

                Toast.makeText(this, "info saved:", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MapsActivity.class));
            }
            else if(radioButtonSecurityResult.equals("Security Setting (NFC)"))
            {
                finish();

                retrievingBundle = getIntent().getExtras();

                passingBundle = new Bundle();
                passingBundle.putString("location", retrievingBundle.getString("location"));
                passingBundle.putString("title", retrievingBundle.getString("title"));
                passingBundle.putString("subject", retrievingBundle.getString("subject"));
                passingBundle.putString("message", retrievingBundle.getString("message"));

                Intent i = new Intent(getApplicationContext(), NfcSecurity.class);
                i.putExtras(passingBundle);

                startActivity(i);
            }
        }
    }
}
