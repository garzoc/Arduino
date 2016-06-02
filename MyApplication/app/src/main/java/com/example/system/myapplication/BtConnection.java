package com.example.system.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.*;
import android.content.Intent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Set;
import java.util.UUID;

/**
 * @uthor Emanuel Mellblom
 * This Class handles the bluetooth connection between Arduino and Android
 */

public class BtConnection extends MainActivity{
    //private final int REQUEST_ENABLE_BT = 12;
    public static BluetoothAdapter adapter;
    static BluetoothDevice arduinoDevice;
    public static BluetoothSocket btSocket;
    static OutputStream btOutputStream;
    static InputStream btInputStream;
    private static boolean state = false;

    public static boolean blueTooth() {
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!bluetooth.isEnabled()) {
            state = true;
        } else if (bluetooth.isEnabled()) {
            String address = bluetooth.getAddress();
            String name = bluetooth.getName();
            state = false;
        }
        return state;
    }

    public static void setBluetoothData() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
        }
        if (!adapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //AlertBoxes.turnOnBluetooth();
        }
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        if (devices.size() > 0) {
            for (BluetoothDevice device : devices) {
                if (device.getName().equals("Group 13")) {
                    arduinoDevice = device;
                    break;
                }
            }
        }
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            btSocket = arduinoDevice.createRfcommSocketToServiceRecord(uuid);
            btSocket.connect();
            btOutputStream = btSocket.getOutputStream();
            btInputStream = btSocket.getInputStream();
        }catch (SocketException so) {
            System.out.println("socket connection faild");
            //MainActivity.connected = false;
        }catch (IOException io) {
            System.out.println("This is an error");
            //MainActivity.connected = false;
        }
    }

    public static boolean getBluetoothConnection(){
        try {
            if (btSocket.isConnected()) {
                return true;
            }
        }catch (NullPointerException nu) {
            return false;
        }
        return false;
    }
}