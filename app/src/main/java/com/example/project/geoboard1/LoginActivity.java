package com.example.project.geoboard1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    Button buttonSignin;
    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewRegister;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonSignin= (Button)findViewById(R.id.buttonSignin);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewRegister = (TextView)findViewById(R.id.textViewRegister);
        progressDialog = new ProgressDialog(this);

        buttonSignin.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);

        // initializes firebase object
        firebaseAuth = firebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null)
        {
            finish();
            startActivity(new Intent(getApplication(), SecurityChoiceActivity.class));
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view == buttonSignin)
        {
            userLogin();
        }
        else if(view == textViewRegister)
        {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void userLogin()
    {
        Toast.makeText(this, "button works", Toast.LENGTH_SHORT).show();
        // .trim() removes spaces in a string
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            // email is empty
            Toast.makeText(this, "please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            // password is empty
            Toast.makeText(this, "please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        // if it passes both if statements, both email and password have been added
        progressDialog.setMessage("Logging in User....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        progressDialog.dismiss();

                        if(task.isSuccessful())
                        {
                            finish();
                            startActivity(new Intent(getApplication(), SecurityChoiceActivity.class));
                        }
                    }
                });
    }
}
