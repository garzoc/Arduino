package com.example.system.myapplication;


//import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import java.io.IOException;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.widget.TextView;
//import android.widget.Toolbar;

import com.zerokol.views.JoystickView;
import com.zerokol.views.JoystickView.OnJoystickMoveListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
/**
 * Main class for MARS SmartCar
 * @author  Emanuel Mellblom
 */

public class MainActivity extends AppCompatActivity {

    //Variables
    private final int REQUEST_ENABLE_BT = 12;
    //private TextView out;
    //public BluetoothAdapter adapter;
    //BluetoothDevice arduinoDevice;
    //public BluetoothSocket btSocket;
    //OutputStream btOutputStream;

    //This belongs to the joystick test
/*    private JoystickView joystick;
    private TextView angleTextView;
    private TextView powerTextView;
    private TextView directionTextView;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Joysticktest----------------------------------------------------------------------------
    /*    angleTextView = (TextView) findViewById(R.id.angleTextView);
        powerTextView = (TextView) findViewById(R.id.powerTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);
        //Referencing also other views
        joystick = (JoystickView) findViewById(R.id.joystickView);

        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick.setOnJoystickMoveListener(new OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
                angleTextView.setText(" " + String.valueOf(angle) + "°");
                powerTextView.setText(" " + String.valueOf(power) + "%");
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);*/

        //---------------------------------------------------------------------------------------

        if (BtConnection.blueTooth()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //BtConnection.setBluetoothData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.buttonmode) {
            Intent intent = new Intent(this, buttonControl.class);
            startActivity(intent);
            //Intent intent = new Intent();
            //intent.setComponent(
              //     new ComponentName("com.example.system.MARS", "com.example.system.myapplication.buttonControl"));
            //startActivity(intent);
            //write your code here about maps action

            return true;
        }
        if(id == R.id.action_jo){
            Intent intent = new Intent(this, JoystickControl.class);
            startActivity(intent);
            //write your code here about joystick option


            return true;
        }
        if(id == R.id.connectToBluetooth){
            BtConnection.setBluetoothData();
            BtConnection.blueTooth();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This is the Button events for the Button controller.
     */
    /*public void goForward(View view) {
        String forward = "f";
        try {
            BtConnection.btOutputStream.write(forward.getBytes());
            //New read sensor data
            //sensordata();
            readInput();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void goBackwards(View view){
        String back = "b";
        try {
            BtConnection.btOutputStream.write(back.getBytes());
            //New read sensor data
            //sensordata();
            readInput();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void stopMovement(View view){
        String stop = "s";
        try {
            BtConnection.btOutputStream.write(stop.getBytes());
            //New read sensor data
            //sensordata();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void goLeft(View view){
        String left = "l";
        try {
            BtConnection.btOutputStream.write(left.getBytes());
            //New read sensor data
            //sensordata();
            readInput();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }
    public void goRight(View view){
        String right = "r";
        try {
            BtConnection.btOutputStream.write(right.getBytes());
            //New read the sensor
            //sensordata();
            readInput();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void connectToBluetooth(View view){
        *//**
         * This is a button event for the button connectToBluetooth.
         *//*
        BtConnection.setBluetoothData();
        BtConnection.blueTooth();
    }

    //New read input from the bluetooth
    public void readInput(){
        try {
            //char in = (char)BtConnection.btInputStream.read();
            //if (in == '1') {
             if(BtConnection.btInputStream.read() == 49){
                System.out.println("Obstacle detected");
                System.out.println("" + BtConnection.btInputStream.read());
                AlertBoxes.obstacleDetected(this);
                    }else{}
        }catch(IOException io){
            //System.out.println("Error reading input");
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }*/


    //New read sensor data in its own thread
/*    public void sensordata(){
    Thread reading = new Thread(){
        @Override
        public void run(){
            Looper.prepare();
            readInput();
        }
    };
    reading.start();
    return;
}*/
}

