package com.example.project.geoboard1;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Random;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener
{
    float sensorX, sensorY, x, y;
    SensorManager sensorManager;
    Random r = new Random();
    private String geoBoardId, messageId, location, title, subject, userMessage, currentUser;
    private MessageDetails m;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

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
                Toast.makeText(this, "Accelerometer Activated", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(this, MapsActivity.class));

                // m.saveToDatabase();
/*            createMessageId();
            title = m.getTitle();
            subject = m.getSubject();
            userMessage = m.getMessage();
            location = m.getLocation();
            currentUser = m.getUserId();
            m.setSecurityType("Accelerometer");
            m.saveToDatabase();*/
            }
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


    // creates a unique location ID
    private void createMessageId()
    {
        final int min = 1000000;
        final int max = 10000000;
        int randomNumber = r.nextInt((max - min) + 1) + min;

        // NFC defines which security feature to use when reading GeoBoards.
        messageId = "messageId" + randomNumber;
    }
}
