package com.example.system.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import java.io.IOException;

/**
 * @author Emanuel Mellblom
 * This class handels the buttonControl option
 */


public class buttonControl extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();

        setContentView(R.layout.activity_buttons);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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













    public void goForward(View view) {
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
        /**
         * This is a button event for the button connectToBluetooth.
         */
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
    }
}
