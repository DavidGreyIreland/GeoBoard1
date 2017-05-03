package com.example.project.geoboard1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SecurityChoiceActivity extends AppCompatActivity implements View.OnClickListener
{
    Button buttonLogout;
    Button buttonSecurity;
    TextView textViewUserEmail;
    FirebaseAuth firebaseAuth;
    Bundle retrievingBundle;
    private String location, title, subject, userMessage, currentUser;
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
        //textViewUserEmail.setText(m.getLocation());
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

            if(radioButtonSecurityResult.equals("Security Setting (Location)"))
            {
                finish();
                MessageDetails m = (MessageDetails)getApplicationContext();
                retrievingBundle = getIntent().getExtras();
                title = m.getTitle();
                subject = m.getSubject();
                userMessage = m.getMessage();
                location = m.getLocation();
                currentUser = m.getUserId();
                m.setSecurityType("NONE");
                m.saveToDatabase();

                startActivity(new Intent(this, MapsActivity.class));
            }
            else if(radioButtonSecurityResult.equals("Security Setting (NFC)"))
            {
                Intent i = new Intent(getApplicationContext(), NfcSecurity.class);

                startActivity(i);
                finish();
            }
            else if(radioButtonSecurityResult.equals("Security Setting (Accelerometer)"))
            {
                Intent i = new Intent(getApplicationContext(), AccelerometerActivity.class);

                startActivity(i);
                finish();
            }
        }
    }
}
