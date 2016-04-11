package com.example.system.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.zerokol.views.JoystickView;
import com.zerokol.views.JoystickView;
import com.zerokol.views.JoystickView.OnJoystickMoveListener;

import java.io.IOException;

/**
 * @author Emanuel Mellblom
 * This class handels the joystick control option
 */

public class JoystickControl extends AppCompatActivity {

    private JoystickView joystick;
    private TextView angleTextView;
    private TextView powerTextView;
    //private TextView directionTextView;
    boolean  dialogShownFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_joystick);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        angleTextView = (TextView) findViewById(R.id.angleTextView);
        powerTextView = (TextView) findViewById(R.id.powerTextView);
        //directionTextView = (TextView) findViewById(R.id.directionTextView);
        //Referencing also other views
        joystick = (JoystickView) findViewById(R.id.joystickView);

        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
                angleTextView.setText(" " + String.valueOf(angle) + "°");
                powerTextView.setText(" " + String.valueOf(power) + "%");

                //New test to send to Arduino
                String theangle = "a" + "\n" + String.valueOf(angle);
                String thePower = "p" + "\n" + String.valueOf(power);
                    try {
                        BtConnection.btOutputStream.write(theangle.getBytes());
                        BtConnection.btOutputStream.write(thePower.getBytes());
                    } catch (IOException io) {
                        if(dialogShownFlag){
                            return;
                        }else{
                            dialogShownFlag = true;
                            AlertBoxes.bluetoothAlert(JoystickControl.this);
                        }
                    } catch (IllegalStateException il) {
                        if(dialogShownFlag){
                            return;
                        }else{
                            dialogShownFlag = true;
                            AlertBoxes.bluetoothAlert(JoystickControl.this);
                        }
                    } catch (NullPointerException nu) {
                        if(dialogShownFlag){
                            return;
                        }else{
                            dialogShownFlag = true;
                            AlertBoxes.bluetoothAlert(JoystickControl.this);
                        }
                    }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
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
            return true;
        }
        if(id == R.id.connectToBluetooth){
            BtConnection.setBluetoothData();
            BtConnection.blueTooth();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
