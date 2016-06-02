package com.example.system.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;

/**
 * @author Emanuel Mellblom, Siavash Paidar
 * @contributer Tigistu Desta.
 * This class handels the buttonControl option as well as reading from the
 * input stream and checking if there is an obstacle in front of the car.
 * It also has a slider that are used to set the speed of the car
 */


public class buttonControl extends AppCompatActivity {

    TextView textview;
    float speed=0;
    float start=0;
    float end=200;
    float start_pos=0;
    int start_position=0;
    int carSpeed;
    //final Activity activity=this;
    //String startS = "s";
    RelativeLayout videoJoystickForButtons;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#D5F5E3"));


        //This controls the slider that you select the speed of the car with
        start=-200;
        end=100;
        start_pos=0;
        start_position=(int) (((start_pos-start)/(end-start))*100);
        speed=start_pos;
        final TextView t1=(TextView) findViewById(R.id.textView1);
        final TextView t2=(TextView) findViewById(R.id.textView2);
        SeekBar slider=(SeekBar) findViewById(R.id.seekBar1);

        videoJoystickForButtons = (RelativeLayout) findViewById(R.id.videoJoystickForButtons);
        videoJoystickForButtons.setVisibility(View.GONE);

        textview = (TextView)findViewById(R.id.textView3);
        slider.setMax(100);
        slider.setProgress(0);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int i,boolean fromUser) {
                textview.setText("Speed = " + (i));
                carSpeed = i;
                t1.getText();
                t2.getText();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(this.getResources().getConfiguration().orientation!= 2) {
            setContentView(R.layout.activity_buttons);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setLogo(R.drawable.car2);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            videoJoystickForButtons = (RelativeLayout) findViewById(R.id.videoJoystickForButtons);
            videoJoystickForButtons.setVisibility(View.GONE);
        }else{
            setContentView(R.layout.activity_buttons);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setLogo(R.drawable.car2);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            try {
                videoJoystickForButtons = (RelativeLayout) findViewById(R.id.videoJoystickForButtons);
                videoJoystickForButtons.setVisibility(View.GONE);
            }catch (NullPointerException nu){
                Log.d("print", nu.toString());
            }
        }
    }


    /**
     * This is the different button events for the button control of the car.
     * It writes the output on the bluetooth output stream and sends it to the car.
     * The string that gets sent is constructed by first character being a c to let the Arduino know that it is
     * a control command, it then consists of the speed and then a | and at last the angel
     * This the gets parsed by the Arduino.
     */

    public void goForward(View view) {
        String forward = "c"+carSpeed+"|0";
        try {
            BtConnection.btOutputStream.write(forward.getBytes());
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void goBackwards(View view){
        String back = "c-"+carSpeed+"|0";
        try {
            BtConnection.btOutputStream.write(back.getBytes());
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void stopMovement(View view){
        String stop = "c0|0";
        try {
            BtConnection.btOutputStream.write(stop.getBytes());
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void goLeft(View view){
        String left = "c"+carSpeed+"|-180";//-90
        try {
            BtConnection.btOutputStream.write(left.getBytes());
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void goRight(View view){
        String right = "c"+carSpeed+"|180"; //90
        try {
            BtConnection.btOutputStream.write(right.getBytes());
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    //Menu
    /**
     * @author Siavash Paidar
     * @param menu
     * This is the menu for this specific class
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(MainActivity.controllerMode=="button") {
            menu.findItem(R.id.buttonmode).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.buttonmode) {
            if(item.isChecked()){
                item.setChecked(false);
                MainActivity.controllerMode="start";
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        }
        if(id == R.id.action_jo){
            MainActivity.controllerMode="start";
            Intent intent = new Intent(this, JoystickControl.class);
            startActivity(intent);
            finish();
            return true;
        }
        if(id == R.id.connectToBluetooth){
            if(!BtConnection.getBluetoothConnection()) {
                BtConnection.setBluetoothData();
                BtConnection.blueTooth();
                //MainActivity.connected = true;
            }
            return true;
        }
        if (id == R.id.action_video) {
            Intent intent = new Intent(this, VideoDisplay.class);
            MainActivity.controllerMode="start";
            startActivity(intent);
            finish();
        }
        if(id ==R.id.radar){
            if(BtConnection.getBluetoothConnection()){
                try {
                    BtConnection.btSocket.close();
                }catch (IOException io){}
            }
            Intent intent = new Intent(this, radar.class);
            startActivity(intent);
            finish();
        }
        if(id ==R.id.voiceControl){
            Intent intent = new Intent(this, VoiceControl.class);
            startActivity(intent);
            finish();
        }
        if(id ==R.id.accelerometer){
            Intent intent = new Intent(this, Accelerometer2.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
