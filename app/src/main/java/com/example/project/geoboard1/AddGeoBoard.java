package com.example.project.geoboard1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AddGeoBoard extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_geo_board);
    }

    public void backButton(View view)
    {
        finish();
        startActivity(new Intent(this, MapsActivity.class));
    }
}
