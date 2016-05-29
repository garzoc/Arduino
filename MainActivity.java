package com.tigistu.views.sliderforspeed;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
    TextView textview;

   // float speed=0;
   // float start=0;
   // float end=200;
  //  float start_pos=0;
   // int start_position=-200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start=-200;		//you need to give starting value of SeekBar
      //  end=200;			//you need to give end value of SeekBar
      //  start_pos=0;		//you need to give starting position value of SeekBar

       // start_position=(int) (((start_pos-start)/(end-start))*100);
      //  speed=start_pos;
        final TextView t1=(TextView) findViewById(R.id.textView1);
        final TextView t2=(TextView) findViewById(R.id.textView2);

        SeekBar slider=(SeekBar) findViewById(R.id.seekBar1);
        textview = (TextView)findViewById(R.id.textView3);
        slider.setProgress(100);

        slider.setMax(200);




        slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

               // Toast.makeText(getBaseContext(), "speed = "
                     //   +String.valueOf(speed), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int i,boolean fromUser) {
                // TODO Auto-generated method stub
                // To convert it as speed value
                textview.setText("Speed = " + (i-100));
                t1.getText();
                t2.getText();
               // float temp= i;
              //  float dis=end-start;
              //  speed =  (start+((temp/100)*dis));

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;


    }

}
