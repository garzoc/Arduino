package com.example.system.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * @author  Siavash Paidar
 * @contributer Emanuel Mellblom
 * This is the first class that gets loaded when the app is started.
 */

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_ENABLE_BT = 12;

    //Controllers and backgrounds
    public static String controllerMode="start";
    public static String background;
    public static boolean connected = false;
    public static String quitArduino = "q";
    public static String arduinoMode;
    public static String mappingMode = "m";
    public static String radarMode = "d";
    public static String sensorDetectionMode = "s";

    //StartUp method
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.car2);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (BtConnection.blueTooth()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public void takeTour(View view){
        Intent i = new Intent(MainActivity.this, Intro.class);
        startActivity(i);
    }

    /**
     * @author Siavash Paidar
     * @param menu
     * This is the menu for this specific class
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
            controllerMode = "button";
            if (connected) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BtConnection.btOutputStream.write(quitArduino.getBytes());
                            Thread.sleep(250);
                            BtConnection.btOutputStream.write(sensorDetectionMode.getBytes());
                        } catch (IOException io) {
                            System.out.println(io);
                        } catch (InterruptedException ex) {
                            System.out.println(ex);
                        }
                    }
                }).start();
            }
            Intent intent = new Intent(this, buttonControl.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.action_jo) {
            controllerMode = "joystick";
            if (connected) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BtConnection.btOutputStream.write(quitArduino.getBytes());
                            Thread.sleep(250);
                            BtConnection.btOutputStream.write(sensorDetectionMode.getBytes());
                        } catch (IOException io) {
                            System.out.println(io);
                        } catch (InterruptedException ex) {
                            System.out.println(ex);
                        }
                    }
                }).start();
            }
            Intent intent = new Intent(this, JoystickControl.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.connectToBluetooth) {
            if(!connected) {
                BtConnection.setBluetoothData();
                BtConnection.blueTooth();
                connected = true;
            }
            return true;
        }
        if (id == R.id.action_video) {
            Intent intent = new Intent(this, VideoDisplay.class);
            startActivity(intent);
            finish();
        }
        if(id ==R.id.radar) {
            if (connected) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BtConnection.btOutputStream.write(quitArduino.getBytes());
                            Thread.sleep(250);
                            BtConnection.btOutputStream.write(radarMode.getBytes());
                        } catch (IOException io) {
                            System.out.println(io);
                        } catch (InterruptedException ex) {
                            System.out.println(ex);
                        }
                    }
                }).start();
                Intent intent = new Intent(this, radar.class);
                startActivity(intent);
            }
        }

        if(id ==R.id.voiceControl){
        Intent intent = new Intent(this, VoiceControl.class);
        startActivity(intent);
        }
        if(id ==R.id.accelerometer){
            Intent intent = new Intent(this, Accelerometer2.class);
            startActivity(intent);
            finish();
        }
        if(id ==R.id.action_map){
            if(connected) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                    BtConnection.btOutputStream.write(quitArduino.getBytes());
                                Thread.sleep(250);
                    BtConnection.btOutputStream.write(radarMode.getBytes());
                            }catch (IOException io){
                                System.out.println(io);
                            }catch (InterruptedException ex){
                                System.out.println(ex);
                            }
                        }
                    }).start();
                MapDataReciever.connect("172.20.10.5", 6666);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

