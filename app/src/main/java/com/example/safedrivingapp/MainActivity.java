package com.example.safedrivingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView xTextView, yTextView, zTextView, slowSign;
    private Button button;
//    declare sensor manager
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerSensorAvailable, itIsNotFirstTime = false;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private float xDifference, yDifference, zDifference;
    private float shakeThreshold = 5f;
    private Vibrator vibrator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xTextView = findViewById(R.id.xTextView);
        yTextView = findViewById(R.id.yTextView);
        zTextView = findViewById(R.id.zTextView);
        slowSign = findViewById(R.id.textViewSlow);
        slowSign.setVisibility(View.INVISIBLE);
        button = findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        ObjectAnimator animationFadeIn = ObjectAnimator.ofFloat(slowSign, "alpha", 0f, 1f);
//        animationFadeIn.setDuration(300);

//        check sensor availability and
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) !=null)
        {
            accelerometerSensor= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAvailable = true;
        } else {
            xTextView.setText("Accelerometer sensor is not available");
            isAccelerometerSensorAvailable = false;
        }
    }


//    sensor events
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        xTextView.setText(sensorEvent.values[0]+"m/s2");
        yTextView.setText(sensorEvent.values[1]+"m/s2");
        zTextView.setText(sensorEvent.values[2]+"m/s2");

//        we store actual value in variables
        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];

        if(itIsNotFirstTime)
        {
            xDifference = Math.abs(lastX - currentX);
            yDifference = Math.abs(lastY - currentY);
            zDifference = Math.abs(lastZ - currentZ);

            if ((xDifference > shakeThreshold && yDifference > shakeThreshold)||
            (xDifference > shakeThreshold && zDifference > shakeThreshold)||
                    (yDifference > shakeThreshold && zDifference > shakeThreshold))
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(500);
                    //deprecated in API 26
                    slowSign.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(clickDone);
//                    slowSign animationFadeIn.start();

                }
            } else {
//                slowSign.setVisibility(View.INVISIBLE);
            }
        }

        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
        itIsNotFirstTime = true;
    }

    private View.OnClickListener clickDone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            button.setVisibility(View.INVISIBLE);
            slowSign.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

//    sensor mehod 1 ON RESUME
    @Override
    protected void onResume() {
        super.onResume();
        if(isAccelerometerSensorAvailable)
            sensorManager.registerListener(this, accelerometerSensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

//    sensor method 2 ON PAUSE
    @Override
    protected void onPause() {
        super.onPause();
        if(isAccelerometerSensorAvailable)
            sensorManager.unregisterListener(this);

    }
}