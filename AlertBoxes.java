package com.example.system.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

/**
 * Created by System on 2016-03-30.
 */
public class AlertBoxes extends MainActivity{

    public static void bluetoothAlert(Context context) {

        new AlertDialog.Builder(context)
                .setTitle("Bluetooth disconnected")
                .setMessage("Do you want to reconnect?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BtConnection.blueTooth();
                        BtConnection.setBluetoothData();
                        //MainActivity.setBluetoothData();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void turnOnBluetooth(Context context){
        AlertDialog turnOnBluetooth = new AlertDialog.Builder(context).create();
        turnOnBluetooth.setTitle("No Bluetooth");
        turnOnBluetooth.setMessage("Make sure that you have bluetooth running on your device.");
        turnOnBluetooth.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        turnOnBluetooth.show();
    }

    public static void obstacleDetected(Context context){
        AlertDialog turnOnBluetooth = new AlertDialog.Builder(context).create();
        turnOnBluetooth.setTitle("Obstacle detected");
        turnOnBluetooth.setMessage("The car detected an obstacle in front of the car");
        turnOnBluetooth.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        turnOnBluetooth.show();
    }

    }



