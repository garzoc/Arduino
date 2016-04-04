package com.example.system.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Set;
import java.util.UUID;


/**
 * This Class handles the bluetooth connection between Arduino and Android
 * @uthor Emanuel Mellblom
 */

public class BtConnection extends MainActivity{

    private final int REQUEST_ENABLE_BT = 12;
    public static BluetoothAdapter adapter;
    static BluetoothDevice arduinoDevice;
    public static BluetoothSocket btSocket;
    static OutputStream btOutputStream;
    public static TextView out;

    //New InputStream
    static InputStream btInputStream;



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

    public static void setBluetoothData() {
        // Getting the Bluetooth adapter and print out its name and adress
        adapter = BluetoothAdapter.getDefaultAdapter();

        // Check for Bluetooth support
        // Emulator doesn't support Bluetooth and will return null
        if (adapter == null) {
           // AlertBoxes.turnOnBluetooth();
        }
        if (!adapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //AlertBoxes.turnOnBluetooth();

        }
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
         Set pairedDevices = adapter.getBondedDevices();
        if (devices.size() > 0) {
            for (BluetoothDevice device : devices) {
                if (device.getName().equals("Group 13")) {
                    //Device name = Group 13
                    arduinoDevice = device;
                    break;
                }
            }
        }
        try {
            System.out.println("getting here step 1");
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
            System.out.println("getting here step 2");
            btSocket = arduinoDevice.createRfcommSocketToServiceRecord(uuid);
            System.out.println("getting here step 3");
            btSocket.connect();
            System.out.println("getting here step 4");
            btOutputStream = btSocket.getOutputStream();
            System.out.println("getting here step 5");

            //New InputStream
            btInputStream = btSocket.getInputStream();

        } catch (SocketException so) {
            System.out.println("Socket failure");
            /**
             * This launches an alert box if bluetooth is disconnected.
             */
            System.out.println("socket connection faild");
        } catch (IOException io) {
            System.out.println("This is an error");
        }
    }
    }








