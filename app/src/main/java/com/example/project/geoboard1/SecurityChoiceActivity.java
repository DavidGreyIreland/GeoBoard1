package com.example.project.geoboard1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SecurityChoiceActivity extends AppCompatActivity implements View.OnClickListener
{
    Button buttonLogout;
    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewUserEmail;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_choice);

        buttonLogout= (Button)findViewById(R.id.buttonLogout);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewUserEmail = (TextView)findViewById(R.id.textViewUserEmail);
        progressDialog = new ProgressDialog(this);

        buttonLogout.setOnClickListener(this);

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
    }
}
