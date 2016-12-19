package com.example.project.geoboard1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MapActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        String resultName = getIntent().getExtras().getString("SecurityOption");
        String resultThanks = "Security choice: " + resultName;
        TextView result = (TextView)findViewById(R.id.result);
        result.setText(resultThanks);

    }
}
