package com.example.assigment1.Utillities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.assigment1.Interfaces.StepCallBack;

public class StepDetector {
    private SensorManager sensorManager;

    private Sensor sensor;

    private SensorEventListener sensorEventListener;
    private int StepCountX =0 ;
    private int StepCountY = 0;
    private long time= 0l;
    private final double ACCELERATION = 2.0;
    private StepCallBack stepCallBack;


    public StepDetector(Context context, StepCallBack stepCallBack) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.stepCallBack = stepCallBack;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float X = event.values[0];
                float Y = event.values[1];
                move(X, Y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //pass
            }
        };
    }

    private void move(float x, float y) {
        String direction;
        String speed;
        direction = (x>0)? "left" : (x<0)? "right" : "";
        speed = (y>0)? "slow" : (y<0)? "fast" : "";

        if (System.currentTimeMillis() - time > 500) {
            time = System.currentTimeMillis();
            if (x > ACCELERATION || x< -ACCELERATION) {
                if (stepCallBack != null)
                    stepCallBack.moveX(direction);
            }
            if (y > ACCELERATION || y< -ACCELERATION) {
                if (stepCallBack != null)
                    stepCallBack.moveY(speed);
            }
        }
    }




    public void start(){
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop(){
        sensorManager.unregisterListener(sensorEventListener, sensor);
    }
}
