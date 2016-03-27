package com.example.system.myapplication;

import android.bluetooth.BluetoothServerSocket;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.*;
import android.util.StringBuilderPrinter;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.net.Socket;

import java.net.SocketException;
import java.util.Set;
import android.widget.Button;

import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothDevice;

/**
 * Main class bluetooth test
 * Author Emanuel Mellblom
 */

public class MainActivity extends Activity{

    //Variables
    private final int REQUEST_ENABLE_BT = 12;
    private TextView out;
    public BluetoothAdapter adapter;
    BluetoothDevice arduinoDevice;
    public BluetoothSocket btSocket;
    OutputStream btOutputStream;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        out = (TextView) findViewById(R.id.textView);
        setBluetoothData();

        if (BtConnection.blueTooth()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        out.setText("");
        setBluetoothData();
    }


   /* public static void connectToBluetooth(View view){
       BtConnection.bleutooth();
        BtConnection.setBluetoothData();
    }*/




    //Add functionallity to the bluetooth
    private void setBluetoothData() {

        // Getting the Bluetooth adapter and print out its name and adress
        adapter = BluetoothAdapter.getDefaultAdapter();
        out.append("\nAdapter: " + adapter.toString() + "\n\nName: "
                + adapter.getName() + "\nAddress: " + adapter.getAddress());

        // Check for Bluetooth support
        // Emulator doesn't support Bluetooth and will return null
        if (adapter == null) {
            Toast.makeText(this, "Bluetooth NOT supported. Aborting.",
                    Toast.LENGTH_LONG).show();
        }
        if (!adapter.isEnabled()) {
            Intent enableBluetooth = new
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        //  Set pairedDevices = adapter.getBondedDevices();
        if (devices.size() > 0) {
            for (BluetoothDevice device : devices) {
                out.append("\nDevices Pared:");
                out.append("\nFound device: " + device.getName() + " Add: "
                        + device.getAddress());
                if (device.getName().equals("Group 13")) {
                    //Device name = Group 13

                    arduinoDevice = device;
                    break;
                }
            }
        }
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            //Standard SerialPortService ID
            btSocket = arduinoDevice.createRfcommSocketToServiceRecord(uuid);
            btSocket.connect();
            btOutputStream = btSocket.getOutputStream();
            // btInputStream = btSocket.getInputStream();
        } catch (SocketException so) {
            System.out.println("Socket failure");
        } catch (IOException io) {
        }
    }





        // Starting the device discovery
        //out.append("\n\nStarting discovery...");
        //adapter.startDiscovery();
      //  out.append("\nDone with discovery...\n");

        // List the paired devices
      //  out.append("\nDevices Pared:");
        /*Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            out.append("\nFound device: " + device.getName() + " Add: "
                    + device.getAddress());
        }
    }*/

    //Button events
    public void goForward(View view) {
        String forward = "f";
        try {
            btOutputStream.write(forward.getBytes());
        } catch (IOException io) {
            System.out.println("IO error writing to serial");
        }
    }

    public void goBackwards(View view){
        String back = "b";
        try {
            btOutputStream.write(back.getBytes());
        } catch (IOException io) {
            System.out.println("IO error writing to serial");
        }
    }

    public void stopMovement(View view){
        String stop = "s";
        try {
            btOutputStream.write(stop.getBytes());
        } catch (IOException io) {
            System.out.println("IO error writing to serial");
        }
    }

    public void goLeft(View view){
        String stop = "l";
        try {
            btOutputStream.write(stop.getBytes());
        } catch (IOException io) {
            System.out.println("IO error writing to serial");
        }
    }
    public void goRight(View view){
        String stop = "r";
        try {
            btOutputStream.write(stop.getBytes());
        } catch (IOException io) {
            System.out.println("IO error writing to serial");
        }
    }

    public void connectToBluetooth(View view){
        /**
         * This is not yet implemented.
         * The event should have the ability to restart the connection
         * to the bluetooth, which mean that it has to call the following
         * 1, BtConnection.bluetooth()
         * 2, setBluetoothData()
         */
        BtConnection.blueTooth();
        setBluetoothData();
    }
}

