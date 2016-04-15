package com.example.siavash.myapplication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.buttonsound);
        Button FORWARD = (Button)this.findViewById(R.id.forward);
        Button BACK = (Button)this.findViewById(R.id.back);
        Button LEFT = (Button)this.findViewById(R.id.left);
        Button RIGHT = (Button)this.findViewById(R.id.right);

        FORWARD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mp.start();
            }
        });

        BACK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mp.start();
            }
        });

        LEFT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mp.start();
            }
        });

        RIGHT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mp.start();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_map) {

            //write your code here about maps action

            return true;
        }
        if(id == R.id.action_jo){

            //write your code here about joystick option


            return true;
        }
        if(id==R.id.action_vi){

           //write your code here about video streaming here


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
