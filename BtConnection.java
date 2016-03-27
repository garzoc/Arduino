package com.example.system.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Set;
import java.util.UUID;


/**
 * This Class handles the bluetooth connection between Arduino and Android
 * Author by Emanuel Mellblom
 */
public class BtConnection extends Activity{

    private final int REQUEST_ENABLE_BT = 12;
    //public static TextView out;
    public static BluetoothAdapter adapter;
    static BluetoothDevice arduinoDevice;
    public static BluetoothSocket btSocket;
    static OutputStream btOutputStream;
    public static TextView out;



    private static boolean state = false;

    public static boolean blueTooth() {

        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!bluetooth.isEnabled()) {
            System.out.println("Bluetooth is Disable...");
            state = true;
        } else if (bluetooth.isEnabled()) {
            String address = bluetooth.getAddress();
            String name = bluetooth.getName();
            System.out.println(name + " : " + address);
            state = false;
        }
        return state;
    }
/*
    public static void setBluetoothData() {
        out = (TextView) findViewById(R.id.textView);
        // Getting the Bluetooth adapter and print out its name and adress
        adapter = BluetoothAdapter.getDefaultAdapter();
        out.append("\nAdapter: " + adapter.toString() + "\n\nName: "
                + adapter.getName() + "\nAddress: " + adapter.getAddress());

        // Check for Bluetooth support
        // Emulator doesn't support Bluetooth and will return null
        if (adapter == null) {
            Toast.makeText(this, "Bluetooth NOT supported. Aborting.", Toast.LENGTH_LONG).show();
        }
        if (!adapter.isEnabled()) {
            Intent enableBluetooth = new
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            MainActivity.startActivityForResult(enableBluetooth, 0);
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
    }*/

    public static void sendMsg(char msg){
        char sendMsg = msg; //String msg = "f";
        try {
            btOutputStream.write(sendMsg); //msg.getBytes();
            btOutputStream.flush();
            System.out.println("Sending data");
            System.out.println(btSocket.getOutputStream().toString());
        } catch (IOException io) {
            System.out.println("IO error writing to serial");
        }
    }
    }








