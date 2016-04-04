package com.example.system.myapplication;


import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import java.io.IOException;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;


/**
 * Main class bluetooth test
 * Author Emanuel Mellblom
 */

public class MainActivity extends Activity{

    //Variables
    private final int REQUEST_ENABLE_BT = 12;
    //private TextView out;
    //public BluetoothAdapter adapter;
    //BluetoothDevice arduinoDevice;
    //public BluetoothSocket btSocket;
    //OutputStream btOutputStream;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BtConnection.blueTooth()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BtConnection.setBluetoothData();
    }


    /**
     * This is the Button events for the Button controller.
     */
    public void goForward(View view) {
        String forward = "f";
        try {
            BtConnection.btOutputStream.write(forward.getBytes());
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void goBackwards(View view){
        String back = "b";
        try {
            BtConnection.btOutputStream.write(back.getBytes());
            //readInput(this);
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void stopMovement(View view){
        String stop = "s";
        try {
            BtConnection.btOutputStream.write(stop.getBytes());
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void goLeft(View view){
        String stop = "l";
        try {
            BtConnection.btOutputStream.write(stop.getBytes());
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }
    }
    public void goRight(View view){
        String stop = "r";
        try {
            BtConnection.btOutputStream.write(stop.getBytes());
        } catch (IOException io) {
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

    public void readInput(View view){
        try {
            while(BtConnection.blueTooth()) {
                if (BtConnection.btInputStream.read() > 0) {
                    int in = BtConnection.btInputStream.read();
                    if (in == 1) {
                        System.out.println("Obstacle detected");
                        AlertBoxes.obstacleDetected(this);
                    }
                }
            }
        }catch(IOException io){
            System.out.println("Error reading input");
        }
    }
}

