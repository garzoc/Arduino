package com.example.system.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;

/**
 * @author Alemeseged Worku
 * @contributer Emanuel Mellblom
 * This class handels the option to controll the car via the phones
 * accelerometer.
 */

public class Accelerometer2 extends AppCompatActivity implements SensorEventListener {

    TextView textX, textY, textZ;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener accelListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#D5F5E3"));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textX = (TextView) findViewById(R.id.textX);
        textY = (TextView) findViewById(R.id.textY);
        textZ = (TextView) findViewById(R.id.textZ);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(this.getResources().getConfiguration().orientation!= 2) {
            setContentView(R.layout.activity_accelerometer2);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setLogo(R.drawable.car2);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }else{
            setContentView(R.layout.activity_accelerometer2);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setLogo(R.drawable.car2);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int acc) {
    }

    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        textX.setText("X : " + (int) x);
        textY.setText("Y : " + (int) y);
        textZ.setText("Z : " + (int) z);
    }

    /**
     * @param menu
     * Menu for this specific activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.buttonmode) {
            MainActivity.controllerMode = "button";
            Intent intent = new Intent(this, buttonControl.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.action_jo) {
            MainActivity.controllerMode = "joystick";
            Intent intent = new Intent(this, JoystickControl.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.connectToBluetooth) {
            if(!BtConnection.getBluetoothConnection()) {
                BtConnection.setBluetoothData();
                BtConnection.blueTooth();
                MainActivity.connected = true;
            }
            return true;
        }
        if (id == R.id.action_video) {
            Intent intent = new Intent(this, VideoDisplay.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.radar) {
            if(BtConnection.getBluetoothConnection()){
                try {
                    BtConnection.btSocket.close();
                }catch (IOException io){}
            }
            Intent intent = new Intent(this, radar.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.voiceControl) {
            Intent intent = new Intent(this, VoiceControl.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.accelerometer) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}




