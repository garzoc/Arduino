package com.example.system.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;

/**
 * @authors Emanuel Mellblom, Tigistu Desta.
 * This class handels the buttonControl option as well as reading from the
 * input stream and checking if there is an obstacle in front of the car.
 */


public class buttonControl extends AppCompatActivity {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        System.out.println("App context "+this.getApplicationContext().toString());
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#959292"));


        //This controlls the slider that you select the speed with
        start=-200;
        end=100;
        start_pos=0;
        start_position=(int) (((start_pos-start)/(end-start))*100);
        speed=start_pos;
        final TextView t1=(TextView) findViewById(R.id.textView1);
        final TextView t2=(TextView) findViewById(R.id.textView2);
        SeekBar slider=(SeekBar) findViewById(R.id.seekBar1);
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


    /**
     * This is the different button events for the button control of the car.
     * It writes the output on the bluetooth outputstream and sends it to the car.
     */

    public void goForward(View view) {
        System.out.println("veiw "+view.toString());
        //String forward = "c50|0";
        String forward = "c"+carSpeed+"|0";

        try {
            BtConnection.btOutputStream.write(forward.getBytes());
            //System.out.println(forward);
            //New read sensor data
            //sensordata();
            //readInput();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void goBackwards(View view){
        //String back = "c-50|0";
        String back = "c-"+carSpeed+"|0";
        try {
            BtConnection.btOutputStream.write(back.getBytes());
            //System.out.println(back);
            //New read sensor data
            //sensordata();
            //readInput();
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
            //System.out.println(stop);
            //New read sensor data
            //sensordata();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void goLeft(View view){
        //String left = "c50|-180";//-90
        String left = "c"+carSpeed+"|-180";//-90
        try {
            BtConnection.btOutputStream.write(left.getBytes());
            //System.out.println(left);
            //New read sensor data
            //sensordata();
            //readInput();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }
    public void goRight(View view){
        //String right = "c50|180"; //90
        String right = "c"+carSpeed+"|180"; //90
        try {
            BtConnection.btOutputStream.write(right.getBytes());
            //System.out.println(right);
            //New read the sensor
            //sensordata();
            //readInput();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void connectToBluetooth(View view){
        BtConnection.setBluetoothData();
        BtConnection.blueTooth();
    }


    //Read input from the bluetooth
    public void readInput(){
        try {
            boolean isShown = false;
            //char in = (char)BtConnection.btInputStream.read();
            //if (in == '1') {
            if(BtConnection.btInputStream.read() == 49){
                System.out.println("Obstacle detected");
                System.out.println("" + BtConnection.btInputStream.read());
                if(!JoystickControl.obstacleAlertisShown){
                    AlertBoxes.obstacleDetected(this);
                    isShown = true;
                }
            }else{}
        }catch(IOException io){
            //System.out.println("Error reading input");
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                return true;
            }
            //Intent intent = new Intent(this, buttonControl.class);
            //startActivity(intent);
            //return true;

        }
        if(id == R.id.action_jo){
            MainActivity.controllerMode="joystick";
            Intent intent = new Intent(this, JoystickControl.class);
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
        return super.onOptionsItemSelected(item);
    }
}
