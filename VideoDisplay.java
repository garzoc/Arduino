package com.example.system.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

/**
 * @authors Emanuel Mellblom, John Sundling.
 * Class that handels the task of showing the video in the GUI
 * it also handels the initial connection to a UDP socket on the RaspberryPi.
 * info: http://developer.android.com/reference/android/widget/VideoView.html
 */

public class VideoDisplay extends AppCompatActivity {

    boolean connected = false;
    String ipAdress = "172.20.10.5";
    int port = 6666;
    Socket socket;
    BufferedReader in = null;
    File tmpVideo;
    DatagramSocket dataSocket;
    //static ImageView images;
    public static ImageView images;
    String testVideo = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
    final Activity activity=this;
    boolean dialogShownFlag = false;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ImageView tempImage = (ImageView) findViewById(R.id.imageBox);
        //images=tempImage;
        //connect();
        playStream();
    }

    private void mySetContentView(@LayoutRes View view, RelativeLayout.LayoutParams params) {
        addContentView(view, params);
    }

    //Play a video stream
    public void playStream() {
        final VideoView videoView = (VideoView) findViewById(R.id.videoframe);
        final Uri video = Uri.parse(testVideo);
        videoView.setVideoURI(video);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)videoView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
        videoView.setLayoutParams(layoutParams);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer arg0) {
                videoView.start();
            }
        });
    }

    public static void setImages(Drawable drawable){
        images.setImageDrawable(drawable);
    }

    private InetAddress setInetaddres(){
        java.net.InetAddress inetaddres = null;
        try {
            inetaddres = java.net.InetAddress.getByName(ipAdress);
        }catch (IOException io){
            System.out.println("wifi failed");
        }
        return inetaddres;
    }

    private DatagramSocket declareSocket(){
        try {
            dataSocket = new DatagramSocket();
        }catch (IOException io){
            System.out.println(io);
        }
        return dataSocket;
    }

    public static boolean canWriteToStorage(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return  true;
        }
        return false;
    }

    public void connect(){
        connected = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                SocketAddress distanceAdress = new InetSocketAddress(port);
                try {
                    System.out.println(distanceAdress.toString());
                    DatagramSocket newDatagramSocket = declareSocket();
                    SocketAddress peerAdress = new InetSocketAddress(ipAdress, port);
                    newDatagramSocket.connect(peerAdress);
                    System.out.println("Connect");

                    //Send initial  data
                    byte[] initial = new byte[DataHandeler.packetSize];
                    DatagramPacket data = new DatagramPacket(initial, initial.length);
                    newDatagramSocket.send(data);

                    DatagramPacket recieved = new DatagramPacket(initial, initial.length);
                    int outdata = 0;
                    int counter = 0;
                    final DataHandeler dataHandeler = new DataHandeler(200, images);
                    final BooleanC decoderBusy=new BooleanC(false);

                    while(true) {
                        newDatagramSocket.receive(recieved);
                        System.out.println("NumPackets = " + counter++);
                        dataHandeler.push(Arrays.copyOf(recieved.getData(), DataHandeler.packetSize));
                        if(!decoderBusy.getBool()) {
                            decoderBusy.setBool(true);
                            VideoDisplay.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataHandeler.decodeBuffer();
                                    decoderBusy.setBool(false);
                                }
                            });
                        }
                        recieved = new DatagramPacket(initial, initial.length);
                        //counter++;
                    }
                }
                catch (NullPointerException nu){
                    System.out.println(nu);
                    System.out.println("Null error in connect");
                }
                catch (IOException io){
                    System.out.println(io);
                    System.out.println("Io error in connect");
                }
            }
        }).start();
    }

    //menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_video).setChecked(true);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.buttonmode) {
            if (id == R.id.buttonmode && MainActivity.controllerMode != "button") {
                if (MainActivity.controllerMode == "joystick") {
                    findViewById(R.id.joy).setVisibility(View.GONE);
                    item.setChecked(false);
                }
                MainActivity.controllerMode = "button";
                item.setChecked(true);
                /**
                 * This code inflates the buttons in the video view
                 */
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.button_control, null);
                VideoView container = (VideoView) findViewById(R.id.videoframe);
                this.addContentView(view, container.getLayoutParams());
                Intent intent = new Intent(this, buttonControl.class);

                //Button controller for video view
                Button forward = (Button) this.findViewById(R.id.forwardButton);
                Button backward = (Button) this.findViewById(R.id.backButton);
                Button left = (Button) this.findViewById(R.id.leftButton);
                Button right = (Button) this.findViewById(R.id.rightButton);
                Button stop = (Button) this.findViewById(R.id.stopButton);

                forward.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("forward");
                        String forward = "c50|0";
                        try {
                            BtConnection.btOutputStream.write(forward.getBytes());
                        } catch (IOException io) {
                            AlertBoxes.bluetoothAlert(activity);
                        } catch (IllegalStateException il) {
                            AlertBoxes.bluetoothAlert(activity);
                        } catch (NullPointerException nu) {
                            AlertBoxes.bluetoothAlert(activity);
                        }
                    }
                });

                backward.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("backwards");
                        String back = "c-50|0";
                        try {
                            BtConnection.btOutputStream.write(back.getBytes());
                        } catch (IOException io) {
                            AlertBoxes.bluetoothAlert(activity);
                        } catch (IllegalStateException il) {
                            AlertBoxes.bluetoothAlert(activity);
                        } catch (NullPointerException nu) {
                            AlertBoxes.bluetoothAlert(activity);
                        }
                    }
                });

                left.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("left");
                        String left = "c50|-180";//-90
                        try {
                            BtConnection.btOutputStream.write(left.getBytes());
                        } catch (IOException io) {
                            AlertBoxes.bluetoothAlert(activity);
                        } catch (IllegalStateException il) {
                            AlertBoxes.bluetoothAlert(activity);
                        } catch (NullPointerException nu) {
                            AlertBoxes.bluetoothAlert(activity);
                        }
                    }
                });

                right.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("right");
                        String right = "c50|180"; //90
                        try {
                            BtConnection.btOutputStream.write(right.getBytes());
                        } catch (IOException io) {
                            AlertBoxes.bluetoothAlert(activity);
                        } catch (IllegalStateException il) {
                            AlertBoxes.bluetoothAlert(activity);
                        } catch (NullPointerException nu) {
                            AlertBoxes.bluetoothAlert(activity);
                        }
                    }
                });

                stop.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("stop");
                        String stop = "c0|0";
                        try {
                            BtConnection.btOutputStream.write(stop.getBytes());
                        } catch (IOException io) {
                            AlertBoxes.bluetoothAlert(activity);
                        } catch (IllegalStateException il) {
                            AlertBoxes.bluetoothAlert(activity);
                        } catch (NullPointerException nu) {
                            AlertBoxes.bluetoothAlert(activity);
                        }
                    }
                });
                return true;
            } else if (id == R.id.buttonmode && MainActivity.controllerMode == "button") {
                findViewById(R.id.butt).setVisibility(View.GONE);
                item.setChecked(false);
                MainActivity.controllerMode = "start";
            }
        }


        /**
         * Joystick controller for videoView
         */
        if(id == R.id.action_jo) {
            if (id == R.id.action_jo && MainActivity.controllerMode != "joystick") {
                if (MainActivity.controllerMode == "button") {
                    findViewById(R.id.butt).setVisibility(View.GONE);
                    item.setChecked(false);
                }
                MainActivity.controllerMode = "joystick";
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.joystick_control, null);
                VideoView container = (VideoView) findViewById(R.id.videoframe);
                this.addContentView(view, container.getLayoutParams());
                Intent intent = new Intent(this, buttonControl.class);

                item.setChecked(true);

                /**
                 * joystick controller
                 */
                RelativeLayout layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);

                final JoyStickClass js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.lever);
                js.setStickSize(100, 100);
                js.setLayoutSize(350, 350);
                js.setLayoutAlpha(150);
                js.setStickAlpha(100);
                js.setOffset(90);
                js.setMinimumDistance(50);

                final BooleanC threadOpen = new BooleanC(false);

                layout_joystick.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        js.drawStick(arg1);
                        if (arg1.getAction() == MotionEvent.ACTION_UP) {
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
                                            String send = "c" + Integer.toString(Math.round(js.getDistance() < 50 ? 50 : js.getDistance()) % 100) + "|" + Integer.toString(js.turnRight() ? Math.round(js.getAngle()) : -Math.round(js.getAngle()));
                                            BtConnection.btOutputStream.write(send.getBytes());
                                        } catch (IOException io) {
                                            if (dialogShownFlag) {
                                            } else {
                                                dialogShownFlag = true;
                                                AlertBoxes.bluetoothAlert(activity);
                                            }
                                        } catch (IllegalStateException il) {
                                            if (dialogShownFlag) {
                                            } else {
                                                dialogShownFlag = true;
                                                AlertBoxes.bluetoothAlert(activity);
                                            }
                                        } catch (NullPointerException nu) {
                                            if (dialogShownFlag) {
                                            } else {
                                                dialogShownFlag = true;
                                                AlertBoxes.bluetoothAlert(activity);
                                            }
                                        }
                                    }
                                }, 250);
                            }
                        }
                        return true;
                    }
                });
                return true;
            } else if (id == R.id.action_jo && MainActivity.controllerMode == "joystick") {
                findViewById(R.id.joy).setVisibility(View.GONE);
                item.setChecked(false);
                MainActivity.controllerMode = "start";
            }
        }

        if(id == R.id.connectToBluetooth){
            try {
                BtConnection.setBluetoothData();
                BtConnection.blueTooth();
                return true;
            }catch (NullPointerException nu){
                AlertBoxes.turnOnBluetooth(activity);
            }
        }

        if (id == R.id.action_video) {
            item.setChecked(false);
            MainActivity.controllerMode="start";
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}





