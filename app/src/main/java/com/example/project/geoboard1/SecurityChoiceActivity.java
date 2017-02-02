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

public class SecurityChoiceActivity extends AppCompatActivity implements View.OnClickListener
{
    Button buttonLogout;
    Button buttonSecurity;
    TextView textViewUserEmail;
    FirebaseAuth firebaseAuth;


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

            if(radioButtonSecurityResult.equals("Security Setting (NFC)"))
            {
                finish();
                startActivity(new Intent(this, NfcSecurity.class));
            }
        }
    }
}
