package com.example.system.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Main class for MARS SmartCar
 * @author  Emanuel Mellblom
 */

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_ENABLE_BT = 12;
    String testVideoAdress = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
    //Controllers and backgrounds
    public static String controllerMode="start";
    public static String background;
    public static boolean connected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (BtConnection.blueTooth()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public void openRadar(View view){
        //radar rad = new radar();
        //radar.Radar radd = radar.Radar(100, 100);
        //radd.dibRadar();
       // rad.onStart();
        //rad.connectDevice();
        //radar.Radar radd = new radar.Radar(250, 250);
        //rad.draw();
        //rad.setup();
        //rad.connectDevice();
    }

    public void takeTour(View view){
        System.out.println("hej");
        Intent i = new Intent(MainActivity.this, Intro.class);
        startActivity(i);
    }

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
            controllerMode = "button";
            Intent intent = new Intent(this, buttonControl.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_jo) {
            controllerMode = "joystick";
            Intent intent = new Intent(this, JoystickControl.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.connectToBluetooth) {
            BtConnection.setBluetoothData();
            BtConnection.blueTooth();
            connected = true;
            return true;
        }
        if (id == R.id.action_video) {
            Intent intent = new Intent(this, VideoDisplay.class);
            startActivity(intent);
        }if(id ==R.id.radar){
            Intent intent = new Intent(this, radar.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

