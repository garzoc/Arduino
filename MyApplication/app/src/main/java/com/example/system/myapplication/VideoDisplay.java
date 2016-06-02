package com.example.system.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;


/**
 * @authors Emanuel Mellblom, John Sundling.
 * Class that handels the task of showing the video in the GUI
 * it also handels the initial connection to a UDP socket on the RaspberryPi.
 * info: http://developer.android.com/reference/android/widget/VideoView.html
 */

public class VideoDisplay extends AppCompatActivity {

    //boolean connected = false;
    //String ipAdress = "172.20.10.14";
    //int port = 6666;
    //Socket socket;
    BufferedReader in = null;
    //File tmpVideo;
    DatagramSocket dataSocket;
    public static ImageView images;
    final Activity activity = this;
    boolean dialogShownFlag = false;
    String raspberryVideoAdress = "http://172.20.10.14/cam_pic.php";
    int carSpeed;
    int savedState = 0; //1 = buttons, 2=joystick


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Starts the video streaming as soon as this activity starts
        playImages();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.car2);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        playImages();
        MainActivity.controllerMode = "start";
        if (this.getResources().getConfiguration().orientation != 2) {
            /*setContentView(R.layout.activity_video);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setLogo(R.drawable.car2);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            playImages();
            MainActivity.controllerMode = "start";*/
           /* if(savedState ==1){
                try {
                    findViewById(R.id.joy).setVisibility(View.GONE);
                }catch (NullPointerException nu){
                    Log.d("print", nu.toString());
                }
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.button_control, null);
                ImageView container = (ImageView) findViewById(R.id.imageVideoBox);
                this.addContentView(view, container.getLayoutParams());
                Intent intent = new Intent(this, buttonControl.class);



            }else if(savedState ==2){
                try {
                    findViewById(R.id.butt).setVisibility(View.GONE);
                }catch (NullPointerException nu){
                    Log.d("print", nu.toString());
                }
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.joystick_control, null);
                ImageView container = (ImageView) findViewById(R.id.imageVideoBox);
                this.addContentView(view, container.getLayoutParams());
                Intent intent = new Intent(this, buttonControl.class);
            }
        }else{
            setContentView(R.layout.activity_video);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setLogo(R.drawable.car2);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            playImages();
            MainActivity.controllerMode = "start";

            if(savedState ==1){
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.button_control, null);
                ImageView container = (ImageView) findViewById(R.id.imageVideoBox);
                this.addContentView(view, container.getLayoutParams());
                Intent intent = new Intent(this, buttonControl.class);


            }else if(savedState ==2){
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.joystick_control, null);
                ImageView container = (ImageView) findViewById(R.id.imageVideoBox);
                this.addContentView(view, container.getLayoutParams());
                Intent intent = new Intent(this, buttonControl.class);
            }
        }*/
        }
    }


    private void mySetContentView(@LayoutRes View view, RelativeLayout.LayoutParams params) {
        addContentView(view, params);
    }

    /**
     * Method for recive the data stream from the raspberryPi camera, it the converts that data to a
     * BitMap and then to a drawable and at last it sets the the image in the imageView.
     */

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            final String urldisplay = urls[0];
            final BooleanC myImage = new BooleanC(false);
            try {
                Looper.prepare();
                Handler handler = new Handler();
                int numImages = 0;
                final BooleanC threadbusy = new BooleanC(false);
                java.net.URL input = new java.net.URL(urldisplay);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferQualityOverSpeed = false;
                options.inDither = false;
                options.inSampleSize = 1;

                while (numImages < 9000) { //Run the camera for 5 minutes at a time (takes 9000 individual images)
                    if (!threadbusy.getBool()) {
                        numImages++;
                        threadbusy.setBool(true);
                        Thread.sleep(3); //3ms ~ 30 fps
                        myImage.setBitmap(BitmapFactory.decodeStream(input.openStream()));
                        VideoDisplay.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bmImage.setImageBitmap(myImage.getBitmap());
                            }
                        });
                        threadbusy.setBool(false);
                    }
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return myImage.getBitmap();
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void playImages() {
        final ImageView videoView = (ImageView) findViewById(R.id.imageVideoBox);
        videoView.setScaleType(ImageView.ScaleType.FIT_XY);
        //new flip the image
        videoView.setRotation(180);
        final AsyncTask<String, Void, Bitmap> task = new DownloadImageTask((ImageView) findViewById(R.id.imageVideoBox))
                .execute(raspberryVideoAdress);
    }

   /* public void displayOwnImageStream(){
        images = (ImageView) findViewById(R.id.imageVideoBox);
        //images.setScaleType(ImageView.ScaleType.FIT_XY);
        connect();
    }*/

    public static void setImages(Drawable drawable) {
        images.setImageDrawable(drawable);
    }

    private DatagramSocket declareSocket() {
        try {
            dataSocket = new DatagramSocket();
        } catch (IOException io) {
            System.out.println(io);
        }
        return dataSocket;
    }

    /*public void connect(){
        connected = true;
        new Thread(
                new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                SocketAddress distanceAdress = new InetSocketAddress(port);
                try {
                    DatagramSocket newDatagramSocket = declareSocket();
                    SocketAddress peerAdress = new InetSocketAddress(ipAdress, port);
                    newDatagramSocket.connect(peerAdress);

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
    }*/

    //menu
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
        if (id == R.id.buttonmode) {
            if (id == R.id.buttonmode && MainActivity.controllerMode != "button") {
                if (MainActivity.controllerMode == "joystick") {
                    try {
                        findViewById(R.id.joy).setVisibility(View.GONE);
                        item.setChecked(false);
                    } catch (NullPointerException nu) {
                        Log.d("print", nu.toString());
                    }
                }
                MainActivity.controllerMode = "button";
                savedState = 1;
                item.setChecked(true);

                /**
                 * This code inflates the buttons in the video view if the button option is selected
                 * in the menu while you are in the VideoView mode
                 */

                //Inflate the button view on top of the imageView
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.button_control, null);
                ImageView container = (ImageView) findViewById(R.id.imageVideoBox);
                this.addContentView(view, container.getLayoutParams());
                Intent intent = new Intent(this, buttonControl.class);


                //Button controller for video view
                Button forward = (Button) this.findViewById(R.id.forwardButton);
                Button backward = (Button) this.findViewById(R.id.backButton);
                Button left = (Button) this.findViewById(R.id.leftButton);
                Button right = (Button) this.findViewById(R.id.rightButton);
                Button stop = (Button) this.findViewById(R.id.stopButton);

                final TextView textview;
                float speed = 0;
                float start = 0;
                float end = 200;
                float start_pos = 0;
                int start_position = 0;

                start = -200;
                end = 100;
                start_pos = 0;
                start_position = (int) (((start_pos - start) / (end - start)) * 100);
                speed = start_pos;
                final TextView t1 = (TextView) findViewById(R.id.textView1);
                final TextView t2 = (TextView) findViewById(R.id.textView2);
                SeekBar slider = (SeekBar) findViewById(R.id.seekBar1);

                textview = (TextView) findViewById(R.id.textView3);
                slider.setMax(100);
                slider.setProgress(0);
                slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                        textview.setText("Speed = " + (i));
                        carSpeed = i;
                        t1.getText();
                        t2.getText();
                    }
                });

                forward.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String forward = "c" + carSpeed + "|0";
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
                        String back = "c-" + carSpeed + "|0";
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
                        String left = "c" + carSpeed + "|-180";
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
                        String right = "c" + carSpeed + "|180";
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
                try {
                    final RelativeLayout videoJoystickForButtons = (RelativeLayout) findViewById(R.id.videoJoystickForButtons);

                    final JoyStickClass cameraJoystickButtons = new JoyStickClass(getApplicationContext(), videoJoystickForButtons, R.drawable.lever);
                    cameraJoystickButtons.setStickSize(125, 125);
                    cameraJoystickButtons.setLayoutSize(650, 650);
                    cameraJoystickButtons.setLayoutAlpha(100);
                    cameraJoystickButtons.setStickAlpha(100);
                    cameraJoystickButtons.setOffset(90);
                    cameraJoystickButtons.setMinimumDistance(50);
                    final BooleanC threadOpenCamera = new BooleanC(false);
                    final Coord coordinates = new Coord(0, 0);

                    videoJoystickForButtons.setOnTouchListener(new View.OnTouchListener() {
                        public boolean onTouch(View arg3, MotionEvent arg2) {
                            cameraJoystickButtons.drawStick(arg2);

                            if (arg2.getAction() == MotionEvent.ACTION_UP) {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        String up = "w";
                                        try {
                                            BtConnection.btOutputStream.write(up.getBytes());
                                        } catch (IOException io) {
                                            System.out.println(io);
                                        } catch (NullPointerException nu) {
                                            System.out.println(nu);
                                        }
                                    }
                                }, 100);
                            }

                            if (arg2.getAction() == MotionEvent.ACTION_DOWN
                                    || arg2.getAction() == MotionEvent.ACTION_MOVE) {
                                if (!threadOpenCamera.getBool()) {
                                    threadOpenCamera.setBool(true);
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            threadOpenCamera.setBool(false);
                                            try {
                                                Coord camCoords = cameraJoystickButtons.cameraDir(cameraJoystickButtons.turnRight() ? Math.round(cameraJoystickButtons.getAngle()) : -Math.round(cameraJoystickButtons.getAngle()), coordinates);
                                                String send = "v" + Integer.toString(camCoords.x()) + "|" + Integer.toString(camCoords.y());
                                                BtConnection.btOutputStream.write(send.getBytes());
                                            } catch (IOException io) {
                                                Log.d("print", io.toString());
                                            } catch (NullPointerException nu) {
                                                Log.d("print", nu.toString());
                                            }
                                        }
                                    }, 100);
                                }
                            }
                            return true;
                        }
                    });
                    return true;
                } catch (NullPointerException nu) {
                    Log.d("string", nu.toString());
                }
            }

                return true;
            } else if (id == R.id.buttonmode && MainActivity.controllerMode == "button") {
                findViewById(R.id.butt).setVisibility(View.GONE);
                item.setChecked(false);
                MainActivity.controllerMode = "start";
            }


            /**
             * This code inflates the joystick in the video view if the button option is selected
             * in the menu while you are in the VideoView mode
             */
            if (id == R.id.action_jo) {
                if (id == R.id.action_jo && MainActivity.controllerMode != "joystick") {
                    if (MainActivity.controllerMode == "button") {
                        findViewById(R.id.butt).setVisibility(View.GONE);
                        item.setChecked(false);
                    }
                    MainActivity.controllerMode = "joystick";
                    savedState = 2;
                    item.setChecked(true);

                    //Inflate the joystick view on top of the imageView
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
                    final View view = inflater.inflate(R.layout.joystick_control, null);
                    ImageView container = (ImageView) findViewById(R.id.imageVideoBox);
                    this.addContentView(view, container.getLayoutParams());
                    Intent intent = new Intent(this, buttonControl.class);


                    /**
                     * Code for controlling the car via joystick while you are in the videoView
                     */
                    RelativeLayout layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);

                    final JoyStickClass js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.lever); //ControllerJoystick
                    js.setStickSize(150, 150);
                    js.setLayoutSize(750, 750);
                    js.setLayoutAlpha(100);
                    js.setStickAlpha(255);
                    js.setOffset(90);
                    js.setMinimumDistance(50);
                    final BooleanC threadOpen = new BooleanC(false);

                    layout_joystick.setOnTouchListener(new View.OnTouchListener() {
                        public boolean onTouch(View arg0, MotionEvent arg1) {
                            js.drawStick(arg1);
                            //Sen a command to the car to stop its motion if the joystick is not touched.
                            if (arg1.getAction() == MotionEvent.ACTION_UP) {
                                //threadOpen.setBool(true);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        String up = "j0|0";
                                        try {
                                            BtConnection.btOutputStream.write(up.getBytes());
                                        } catch (IOException io) {
                                            Log.d("print", io.toString());
                                        } catch (NullPointerException nu) {
                                            Log.d("print", nu.toString());
                                        }
                                    }
                                }, 250);
                            }
                            //Continuously send the joysticks position and distance from the center to the edge. Gets sent over bluetooth to the car.
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
                                                String send = "j" + Integer.toString(Math.round(js.getDistance() < 50 ? 50 : js.getDistance()) % 100) + "|" + Integer.toString(js.turnRight() ? Math.round(js.getAngle()) : -Math.round(js.getAngle()));
                                                BtConnection.btOutputStream.write(send.getBytes());
                                            } catch (IOException io) {
                                                Log.d("print", io.toString());
                                                if (dialogShownFlag) {
                                                } else {
                                                    dialogShownFlag = true;
                                                    AlertBoxes.bluetoothAlert(activity);

                                                }
                                            } catch (IllegalStateException il) {
                                                Log.d("print", il.toString());
                                                if (dialogShownFlag) {
                                                } else {
                                                    dialogShownFlag = true;
                                                    AlertBoxes.bluetoothAlert(activity);

                                                }
                                            } catch (NullPointerException nu) {
                                                Log.d("print", nu.toString());
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

                    /**
                     * This code if for controlling the pan and tilt functionality of the camera on the
                     * car, it uses a joystick
                     */
                    final RelativeLayout videoJoystick = (RelativeLayout) findViewById(R.id.videoJoystick);
                    final JoyStickClass cameraJoystick = new JoyStickClass(getApplicationContext(), videoJoystick, R.drawable.lever);
                    cameraJoystick.setStickSize(125, 125);
                    cameraJoystick.setLayoutSize(650, 650);
                    cameraJoystick.setLayoutAlpha(100);
                    cameraJoystick.setStickAlpha(100);
                    cameraJoystick.setOffset(90);
                    cameraJoystick.setMinimumDistance(50);
                    final BooleanC threadOpenCamera = new BooleanC(false);
                    final Coord coordinates = new Coord(0, 0);

                    videoJoystick.setOnTouchListener(new View.OnTouchListener() {
                        public boolean onTouch(View arg3, MotionEvent arg2) {
                            cameraJoystick.drawStick(arg2);

                            if (arg2.getAction() == MotionEvent.ACTION_UP) {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        String up = "w";
                                        try {
                                            BtConnection.btOutputStream.write(up.getBytes());
                                        } catch (IOException io) {
                                            Log.d("print", io.toString());
                                        } catch (NullPointerException nu) {
                                            Log.d("print", nu.toString());
                                        }
                                    }
                                }, 250);
                            }

                            if (arg2.getAction() == MotionEvent.ACTION_DOWN
                                    || arg2.getAction() == MotionEvent.ACTION_MOVE) {
                                if (!threadOpenCamera.getBool()) {
                                    threadOpenCamera.setBool(true);
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            threadOpenCamera.setBool(false);
                                            try {
                                                Coord camCoords = cameraJoystick.cameraDir(cameraJoystick.turnRight() ? Math.round(cameraJoystick.getAngle()) : -Math.round(cameraJoystick.getAngle()), coordinates);
                                                String send = "v" + Integer.toString(camCoords.x()) + "|" + Integer.toString(camCoords.y());
                                                BtConnection.btOutputStream.write(send.getBytes());
                                            } catch (IOException io) {
                                                Log.d("print", io.toString());
                                            } catch (NullPointerException nu) {
                                                Log.d("print", nu.toString());
                                            }
                                        }
                                    }, 100);
                                }
                            }
                            return true;
                        }
                    });
                    return true;
                }
            }
            if (id == R.id.action_jo && MainActivity.controllerMode == "joystick") {
                try {
                    findViewById(R.id.joy).setVisibility(View.GONE);
                    item.setChecked(false);
                    MainActivity.controllerMode = "start";
                } catch (NullPointerException nu) {
                    Log.d("print", nu.toString());
                }
            }

            if (id == R.id.connectToBluetooth) {
                try {
                    if (!BtConnection.getBluetoothConnection()) {
                        BtConnection.setBluetoothData();
                        BtConnection.blueTooth();
                    }
                    return true;
                } catch (NullPointerException nu) {
                    AlertBoxes.turnOnBluetooth(activity);
                    Log.d("print", nu.toString());
                }
            }
            if (id == R.id.action_video) {
                item.setChecked(false);
                MainActivity.controllerMode = "start";
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            if (id == R.id.radar) {
                if(BtConnection.getBluetoothConnection()){
                    try {
                        BtConnection.btSocket.close();
                    }catch (IOException io){}
                }
                Intent intent = new Intent(this, radar.class);
                startActivity(intent);
                finish();
            }
            if (id == R.id.voiceControl) {
                Intent intent = new Intent(this, VoiceControl.class);
                startActivity(intent);
                finish();
            }
            if (id == R.id.accelerometer) {
                Intent intent = new Intent(this, Accelerometer2.class);
                startActivity(intent);
                finish();
            }
            return super.onOptionsItemSelected(item);
        }

    }


