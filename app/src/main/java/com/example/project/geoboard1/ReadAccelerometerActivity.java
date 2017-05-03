package com.example.project.geoboard1;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReadAccelerometerActivity extends AppCompatActivity implements SensorEventListener
{
    float sensorX, sensorY, x, y;
    SensorManager sensorManager;
    private String geoBoardId, messageId, location, title, subject, userMessage, currentUser;
    private MessageDetails m;
    private DatabaseReference geoBoardRef;
    private Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_accelerometer);

        geoBoardRef = FirebaseDatabase.getInstance().getReference();

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)
        {
            Sensor sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        sensorX = sensorY = 10;
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            a(event);
        }
    }

    public void a(SensorEvent event)
    {
        x = event.values[0];
        y = event.values[1];

        try
        {
            Thread.sleep(5);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        if(sensorX < x || sensorY < y)
        {
            v = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            Intent intent = new Intent(getApplicationContext(), CurrentUsersMessage.class);

            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }

    @Override
    public void onPause()
    {
        sensorManager.unregisterListener(this);
        super.onPause();
    }
}
