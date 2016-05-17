package com.example.system.myapplication;

/**
 * Created by System on 2016-05-10.
 */

import processing.core.*;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import java.util.Set;
import java.util.UUID;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class radar extends PApplet {

    /*Activity activity = this;
    private AppCompatDelegate delegate;
    AppCompatCallback callback = new AppCompatCallback() {
        @Override
        public void onSupportActionModeStarted(ActionMode mode) {}
        @Override
        public void onSupportActionModeFinished(ActionMode mode) {}
        @Nullable
        @Override
        public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
            return null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //let's create the delegate, passing the activity at both arguments (Activity, AppCompatCallback)
        delegate = AppCompatDelegate.create(this, callback);

        //we need to call the onCreate() of the AppCompatDelegate
        delegate.onCreate(savedInstanceState);

        //we use the delegate to inflate the layout
        delegate.setContentView(R.layout.activity_radar);
        //Finally, let's add the Toolbar
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        delegate.setSupportActionBar(toolbar);
        setup();
        start();
    }*/

    //initRadar start = new initRadar();

            private static final int REQUEST_ENABLE_BT = 3;
            ArrayList devices;
            BluetoothAdapter adapter;
            BluetoothDevice device;
            BluetoothSocket socket;
            boolean registered = false;
            PFont f1;
            PFont f2;
            int state;
            String error;
            byte value;
            int MAX_DISTANCE = 200;
            int angle = -80;
            int dir = 0;
            int distance;
            String comPortString;

            // Types of messages used by Handler
            public static final int WRITE_MSG = 1;
            public static final int READ_MSG = 2;

            Radar myRadar;
            getSendData ReceiveBT = null;

    //Setup Method
    public void onStart() {
        super.onStart();
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null){
            if (!adapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }else{
                starts();
            }
        }
    }

    public void setup() {
        //System.out.println("!!!!!!!!!!!!!!!!!!!!! Setup !!!!!!!!!!!!!!!!!!!!!!!!!!!");
        orientation(LANDSCAPE);
        myRadar = new Radar(displayHeight, displayWidth);

        //f1 = loadFont("data/ArialMT-20.vlw");
        //f2 = loadFont("data/ArialMT-15.vlw");
        stroke(255);
    }


    public void starts() {
        //System.out.println("!!!!!!!!!!!!!!!!!!!!! Starts !!!!!!!!!!!!!!!!!!!!!!!!!!!");
        devices = new ArrayList();
        for (BluetoothDevice device : adapter.getBondedDevices()) {
            devices.add(device);
        }
        state = 1;
    }

    public void onActivityResult (int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            starts();
        }else{
            state = 4;
            error = "Bluetooth has not been activated ";
        }
    }


    public void draw(){
        switch(state){
            case 0:
                listDevices("SEARCHING DEVICE", color(255, 0, 0));
                break;
            case 1:
                listDevices("CHOOSE DEVICE", color(0, 255, 0));
                break;
            case 2:
                connectDevice();
                break;
            case 3:
                showsData();
                break;
            case 4:
                displayError();
                break;
        }
    }

    public void listDevices(String text, int c) {
        background(0);
        //textFont(f1);
        fill(c);
        text(text, 0, 20);
        if (devices != null) {
            for (int index = 0; index < devices.size(); index++) {
                BluetoothDevice device = (BluetoothDevice) devices.get(index);
                fill(255, 255, 0);
                int position = 50 + (index * 55);
                if (device.getName() != null) {
                    text(device.getName(), 0, position);
                }
                fill(180, 180, 255);
                text(device.getAddress(), 0, position + 20);
                fill(255);
                line(0, position + 30, 319, position + 30);
            }
        }
    }

    public void mouseReleased(){
        switch(state){
            case 0:
                break;
            case 1:
                checkElected();
                break;
        }
    }

    //Check elected device
    public void checkElected() {
        //println("Election checks");
        int chosenOne = (mouseY - 50) / 55;
        if(chosenOne < devices.size()){
            device = (BluetoothDevice) devices.get(0);
       /* Set<BluetoothDevice> devices = adapter.getBondedDevices();
            if (devices.size() > 0) {
                for (BluetoothDevice device : devices) {
                    if (device.getName().equals("Group 13")) {
                        this.device = device;
                        break;
                    }
                }
            }*/
            //println(device.getName());
            state = 2;
        }
    }

    //Connect device
    public void connectDevice() {
        //println("connecting device");
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
            //System.out.println("socket " + socket);
            socket.connect();

            //System.out.println(socket.getInputStream());
            ReceiveBT = new getSendData(socket);
            //Looper.prepare();

            Thread thread  = new Thread(ReceiveBT);
            //thread.run();
            thread.start();

            //System.out.println("******************** Starts the thread ************************" + thread.getState());


            state = 3;
        } catch (Exception ex) {
            state = 4;
            error = ex.toString();
            //System.out.println("**************************** This is a super bad error ************************");
            //println(error);
        }
    }

    private class getSendData implements Runnable {
        private InputStream in;
        private OutputStream ons;
        private BluetoothSocket DataSocket;

        public getSendData(BluetoothSocket socket) {
            DataSocket = socket;
            try {
                //My test
                in = DataSocket.getInputStream();
                //in = BtConnection.btInputStream;
                ons = DataSocket.getOutputStream();
                //ons = BtConnection.btOutputStream;
                //System.out.println("********************** get send data ***********************");
            } catch (Exception e) {
            }
        }
        @Override
        public void run() {
            //System.out.println("*************************** RUN *****************************");
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                //System.out.println("*************************** TRY *****************************");
                try {
                    //System.out.println(in);
                    bytes = in.read(buffer);
                    //System.out.println("***************************Buffer at position 1 " + buffer[1]);
                    mHandler.obtainMessage(READ_MSG, bytes, -1, buffer)
                            .sendToTarget();
                } catch (Exception ex) {
                    System.out.println(ex);
                    break;
                }
            }
        }

        public void cancel() {
            try {
                DataSocket.close();
            } catch (Exception ex) {
            }
        }
    }

    public void onStop() {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    //println(ex);
                }
            }
            super.onStop();
        }

    BufferHandler bufferHandler = new BufferHandler();
    String placeHolder = "";
        private final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //System.out.println("********************** handleMessage ***************************** ");
                switch (msg.what) {
                    case WRITE_MSG:
                        break;
                    case READ_MSG:
                        String srtReceived = null;
                        byte[] dataReceived = (byte[]) msg.obj;
                        // construct a string from the valid bytes in the buffer
                        srtReceived = new String(dataReceived, 0, msg.arg1);
                        //System.out.println("recevied string = " + srtReceived);
                        if (srtReceived != null) {
                            srtReceived = trim(srtReceived);
                            if (srtReceived.indexOf('|') ==-1) {
                                placeHolder = placeHolder.concat(srtReceived);
                                //System.out.println("placeholder 1 = "+placeHolder);
                               // System.out.println("recevied string = " + srtReceived);
                            } else {
                                //placeHolder = placeHolder.concat(srtReceived);
                                //int index = placeHolder.indexOf('|');
                                //System.out.println("index " + index);
                                //System.out.println(placeHolder);

                                placeHolder = placeHolder.concat(srtReceived);
                                int lastIndex = placeHolder.lastIndexOf('|');
                                String[] tmp = split(placeHolder,"|");
                                boolean extraSegment = (lastIndex==placeHolder.length()-1)?true:false;
                                int numStrings = tmp.length+(extraSegment?-2:-1);

                                System.out.println("tmp num strings "+tmp[numStrings]);
                                String[] values = split(tmp[numStrings], '&');
                                placeHolder="";

                               /* placeHolder = "";
                                if(index+1<=placeHolder.length()){
                                    int tempIndex = 0;
                                    int numOccurance = 0;

                                    while ((tempIndex=placeHolder.substring(index+1,placeHolder.length()).indexOf('|',tempIndex))!=-1) {
                                        numOccurance++;
                                    }
                                    if(numOccurance==0){
                                        placeHolder = placeHolder.substring(index+1,placeHolder.length());
                                    }else{
                                        String[] temp = split(placeHolder.substring(index+1,placeHolder.length()),'|');
                                        for(int i = 0; i < numOccurance; i++){
                                            bufferHandler.push(temp[i]);
                                        }
                                    }



                                }else {
                                    placeHolder = "";
                                }*/
                                try {
                                    angle = PApplet.parseInt(map(Integer.parseInt(values[0]), 0, 160, -80, 80));
                                    distance = PApplet.parseInt(map(Integer.parseInt(values[1]), 1, MAX_DISTANCE, 1, myRadar.radio));
                                    //println(angle + ", " + distance);
                                } catch (Exception e) {
                                }
                            }
                        }
                        break;
                }
            }
        };

        public String process(String input){
            if (input.indexOf('|') ==-1) {
                placeHolder = placeHolder.concat(input);
                //System.out.println("placeholder 1 = "+placeHolder);
                System.out.println("recevied string = " + input);
            }else {
                placeHolder = placeHolder.concat(input);
                int lastIndex = placeHolder.lastIndexOf('|');
                String[] tmp = placeHolder.split("|");
                boolean extraSegment = (lastIndex==placeHolder.length()-1)?false:true;
                int numStrings = tmp.length+(extraSegment?-1:0);
                for (int i = 0; i<numStrings; i++){
                    bufferHandler.push(tmp[i]);
                }
                if(extraSegment){
                    placeHolder = placeHolder.substring(lastIndex,placeHolder.length());
                }else {
                    placeHolder = "";
                }




            }
            return "";
        }

        public void showsData() {
            background(0, 0, 0);
            myRadar.dibRadar();
            myRadar.Tracking(angle);
            myRadar.found(angle, distance);
        }

        public void displayError() {
            background(255, 0, 0);
            fill(255, 255, 0);
            //textFont(f2);
            textAlign(CENTER);
            translate(width / 2, height / 2);
            rotate(3 * PI / 2);
            text(error, 0, 0);
        }

        class Radar {
            int SIDE_LENGTH;
            int ANGLE_BOUNDS = 80;
            int ANGLE_STEP = 2;
            int HISTORY_SIZE = 10;
            int POINTS_HISTORY_SIZE = 100;
            int radio;
            float x;
            float y;
            float leftAngleRad;
            float rightAngleRad;
            float[] historyX, historyY;
            Point[] points;
            int centerX;
            int centerY;

            Radar(int high, int width) {
                SIDE_LENGTH = (high - 100) * 2;
                radio = SIDE_LENGTH / 2;
                centerX = width / 2;
                centerY = high;
                leftAngleRad = radians(-ANGLE_BOUNDS) - HALF_PI;
                rightAngleRad = radians(ANGLE_BOUNDS) - HALF_PI;
                historyX = new float[HISTORY_SIZE];
                historyY = new float[HISTORY_SIZE];
                points = new Point[POINTS_HISTORY_SIZE];
            }

            public void dibRadar() {
                stroke(100);
                noFill();
                strokeWeight(2);
                for (int i = 0; i <= (SIDE_LENGTH / 100); i++) {
                    arc(centerX, centerY, 100 * i, 100 * i, leftAngleRad, rightAngleRad);
                }
                for (int i = 0; i <= (ANGLE_BOUNDS * 2 / 20); i++) {
                    float angle = -ANGLE_BOUNDS + i * 20;
                    float radAngle = radians(angle);
                    line(centerX, centerY, centerX + radio * sin(radAngle), centerY - radio * cos(radAngle));
                }
            }

            public void Tracking(int angle) {
                //System.out.println("********************* Tracking **************************");
                float radian = radians(angle);
                x = radio * sin(radian);
                y = radio * cos(radian);
                float px = centerX + x;
                float py = centerY - y;
                historyX[0] = px;
                historyY[0] = py;
                strokeWeight(2);
                for (int i = 0; i < HISTORY_SIZE; i++) {
                    stroke(50, 190, 50, 255 - (25 * i));
                    line(centerX, centerY, historyX[i], historyY[i]);
                }
                shiftHistoryArray();
            }

            public void found(int angle, int distance) {

                if (distance > 0) {
                    float radian = radians(angle);
                    x = distance * sin(radian);
                    y = distance * cos(radian);
                    int px = (int) (centerX + x);
                    int py = (int) (centerY - y);
                    points[0] = new Point(px, py);
                } else {
                    points[0] = new Point(0, 0);
                }
                for (int i = 0; i < POINTS_HISTORY_SIZE; i++) {
                    Point punto = points[i];
                    if (punto != null) {
                        int x = punto.x;
                        int y = punto.y;
                        if (x == 0 && y == 0) continue;
                        int colorAlfa = (int) map(i, 0, POINTS_HISTORY_SIZE, 50, 0);
                        int size = (int) map(i, 0, POINTS_HISTORY_SIZE, 30, 5);
                        fill(190, 40, 40, colorAlfa);
                        noStroke();
                        ellipse(x, y, size, size);
                    }
                }
                shiftPointsArray();
            }

            public void shiftHistoryArray() {
                for (int i = HISTORY_SIZE; i > 1; i--) {
                    historyX[i - 1] = historyX[i - 2];
                    historyY[i - 1] = historyY[i - 2];
                }
            }

            public void shiftPointsArray() {
                for (int i = POINTS_HISTORY_SIZE; i > 1; i--) {
                    Point oldPoint = points[i - 2];
                    if (oldPoint != null) {
                        Point punto = new Point(oldPoint.x, oldPoint.y);
                        points[i - 1] = punto;
                    }
                }
            }
        }

        class Point {
            int x, y;

            Point(int xPos, int yPos) {
                x = xPos;
                y = yPos;
            }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }
        }
}


