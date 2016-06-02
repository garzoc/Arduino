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
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.IOException;
import android.os.Handler;

/**
 * @authors Emanuel Mellblom, Tigistu Desta, John Sundling
 * This class handels the joystick control option
 */

public class JoystickControl extends AppCompatActivity {

    //private TextView angleTextView;
    //private TextView powerTextView;
    boolean dialogShownFlag = false;
    //TextView textView1, textView2, textView3, textView4, textView5;
    RelativeLayout layout_joystick;
    RelativeLayout videoJoystick;
    public static boolean obstacleAlertisShown;
    TextView textview;
    float speed=0;
    float start=0;
    float end=200;
    float start_pos=0;
    int start_position=0;
    //final Activity activity=this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#000000"));
        MainActivity.controllerMode="joystick";


        start=-200;
        end=200;
        start_pos=0;
        start_position=(int) (((start_pos-start)/(end-start))*100);
        speed=start_pos;
        final TextView t1=(TextView) findViewById(R.id.textView1);
        final TextView t2=(TextView) findViewById(R.id.textView2);
        textview = (TextView)findViewById(R.id.textView3);

        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);
        videoJoystick = (RelativeLayout) findViewById(R.id.videoJoystick);
        videoJoystick.setVisibility(View.GONE);


        final JoyStickClass js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.lever);
        js.setStickSize(150, 150);
        js.setLayoutSize(750, 750);
        js.setLayoutAlpha(150);
        js.setStickAlpha(255);
        js.setOffset(90);
        js.setMinimumDistance(50);

       final BooleanC threadOpen = new BooleanC(false);

        layout_joystick.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    //threadOpen.setBool(true);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Send a string to the arduino to tell it to be stopped if joystick is not touched
                            String up = "j0|0";
                            try {
                                BtConnection.btOutputStream.write(up.getBytes());
                            } catch (IOException io) {
                                System.out.println(io);
                            } catch (NullPointerException nu) {
                                System.out.println(nu);
                            }
                        }
                    }, 250);
                }

                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!threadOpen.getBool()) {
                        threadOpen.setBool(true);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                threadOpen.setBool(false);
                                try {
                                    //String that gets sent to the arduino where it is being parsed and sets the speed and angle of the car
                                    String send = "j" + Integer.toString(Math.round(js.getDistance() < 50 ? 50 : js.getDistance())) + "|" + Integer.toString(js.turnRight() ? Math.round(js.getAngle()) : -Math.round(js.getAngle()));
                                    BtConnection.btOutputStream.write(send.getBytes());
                                } catch (IOException io) {
                                    if (dialogShownFlag) {
                                    } else {
                                        dialogShownFlag = true;
                                        AlertBoxes.bluetoothAlert(JoystickControl.this);
                                    }
                                } catch (IllegalStateException il) {
                                    if (dialogShownFlag) {
                                    } else {
                                        dialogShownFlag = true;
                                        AlertBoxes.bluetoothAlert(JoystickControl.this);
                                    }
                                } catch (NullPointerException nu) {
                                    if (dialogShownFlag) {
                                    } else {
                                        dialogShownFlag = true;
                                        AlertBoxes.bluetoothAlert(JoystickControl.this);
                                    }
                                }
                            }
                        }, 250);
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(this.getResources().getConfiguration().orientation!= 2) {
            setContentView(R.layout.activity_joystick);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setLogo(R.drawable.car2);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            try {
                videoJoystick = (RelativeLayout) findViewById(R.id.videoJoystick);
                videoJoystick.setVisibility(View.GONE);
            }catch (NullPointerException nu){
                Log.d("print", nu.toString());
            }
        }else{
            setContentView(R.layout.activity_joystick);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setLogo(R.drawable.car2);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            try {
                videoJoystick = (RelativeLayout) findViewById(R.id.videoJoystick);
                videoJoystick.setVisibility(View.GONE);
            }catch (NullPointerException nu){
                Log.d("print", nu.toString());
            }
        }
    }


    /**
     * @author Siavash Paidar
     * @param menu
     * This is the menu for this specific class
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.buttonmode) {
            MainActivity.controllerMode="button";
            Intent intent = new Intent(this, buttonControl.class);
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
            startActivity(intent);
            MainActivity.controllerMode="start";
            finish();
        }
        if (id == R.id.action_jo){
            Intent intent = new Intent(this, MainActivity.class);
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
            MainActivity.controllerMode="start";
            finish();
        }
        if(id ==R.id.voiceControl){
            Intent intent = new Intent(this, VoiceControl.class);
            startActivity(intent);
            MainActivity.controllerMode="start";
            finish();
        }
        if(id ==R.id.accelerometer){
            Intent intent = new Intent(this, Accelerometer2.class);
            startActivity(intent);
            MainActivity.controllerMode="start";
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
