package com.example.system.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;
import android.os.Handler;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.content.Context;



/**
 * @authors Emanuel Mellblom, Tigistu Desta, John Sundling
 * This class handels the joystick control option
 */

public class JoystickControl extends AppCompatActivity {



    //private JoystickView joystick;
    private TextView angleTextView;
    private TextView powerTextView;
    //private TextView directionTextView;
    boolean dialogShownFlag = false;
    TextView textView1, textView2, textView3, textView4, textView5;
    RelativeLayout layout_joystick;
    public static boolean obstacleAlertisShown;
    TextView textview;
    float speed=0;
    float start=0;
    float end=200;
    float start_pos=0;
    int start_position=0;
    int carSpeed;
    final Activity activity=this;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        /*Joystick joy = new Joystick();
        View view = new View(this);
        view.draw(joy);*/


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_joystick);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#959292"));

        start=-200;
        end=200;
        start_pos=0;
        start_position=(int) (((start_pos-start)/(end-start))*100);
        speed=start_pos;
        final TextView t1=(TextView) findViewById(R.id.textView1);
        final TextView t2=(TextView) findViewById(R.id.textView2);
        SeekBar slider=(SeekBar) findViewById(R.id.seekBar1);
        textview = (TextView)findViewById(R.id.textView3);
        slider.setMax(200);
        slider.setProgress(100);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int i,boolean fromUser) {
                textview.setText("Speed = " + (i-100));
                carSpeed = i-100;
                t1.getText();
                t2.getText();
            }
        });

        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);

        final JoyStickClass js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.lever);
        js.setStickSize(150, 150);
        js.setLayoutSize(450, 450);
        js.setLayoutAlpha(100);
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
                            String up = "c0|0";
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
                                System.out.println("delayed");
                                threadOpen.setBool(false);
                                try {
                                    //String send = "c" + Integer.toString(Math.round(js.getDistance() < 50 ? 50 : js.getDistance()) % 100) + "|" + Integer.toString(js.turnRight() ? Math.round(js.getAngle()) : -Math.round(js.getAngle()));
                                    String send = "c" + Integer.toString(Math.round(js.getDistance() < 50 ? 50 : js.getDistance()) % 100) + "|" + carSpeed;

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

    public void readSensorInput() {
        try {
            obstacleAlertisShown = false;
            //char in = (char)BtConnection.btInputStream.read();
            //if (in == '1') {
            if (BtConnection.btInputStream.read() == 49) {
                System.out.println("Obstacle detected");
                System.out.println("" + BtConnection.btInputStream.read());
                if(!obstacleAlertisShown){
                    AlertBoxes.obstacleDetected(this);
                    obstacleAlertisShown = true;
                }
            } else {
            }
        }catch (IOException io) {
            //System.out.println("Error reading input");
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il) {
            AlertBoxes.bluetoothAlert(this);
        }catch (NullPointerException nu) {
            AlertBoxes.bluetoothAlert(this);
        }
    }

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
            return true;
        }
        if(id == R.id.connectToBluetooth){
            BtConnection.setBluetoothData();
            BtConnection.blueTooth();
            return true;
        }
        if (id == R.id.action_video) {
            Intent intent = new Intent(this, VideoDisplay.class);
            startActivity(intent);
        }
        if (id == R.id.action_jo){
            Intent intent = new Intent(this, MainActivity.class);
            MainActivity.controllerMode="start";
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
