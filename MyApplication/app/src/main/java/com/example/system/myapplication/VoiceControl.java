package com.example.system.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tigistu Desta
 * This class handels the voice command option for controlling the car
 */

public class VoiceControl extends BluetoothActivity {
	private LogView tvData;

	// Available commands
	private static final String[] commands = {"go", "back", "left", "right", "stop", "fast", "slow"};
	private int speed;
	boolean foundCommand;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_control);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		tvData = (LogView) findViewById(R.id.tvData);
		Button bSpeak = (Button) findViewById(R.id.bSpeak);
		getWindow().getDecorView().setBackgroundColor(Color.parseColor("#D5F5E3"));

		// Disable button if no recognition service is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if(activities.size() == 0) {
			bSpeak.setEnabled(false);
			bSpeak.setText("Speech recognizer not present");
		}
	}

	@Override
	protected void onResume() {
		preventCancel = false;
		speed = 20;
		super.onResume();
	}


	public void speakButtonClicked(View v) {
		startVoiceRecognitionActivity();
	}

	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		preventCancel = true;
		startActivityForResult(intent, BluetoothRemoteControlApp.MSG_1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == BluetoothRemoteControlApp.MSG_1 && resultCode == RESULT_OK) {
			// ArrayList contains the words the speech recognition thought it heard
			ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			foundCommand = false;
			for(String command : commands) {
				// If the array contains at least one word that's in the commands array
				if(matches.contains(command)) {
					foundCommand = true;
					tvData.append("Command: " + command + "\n");
					if(command.equals("go")) {
						write("c," + speed + "," + speed);
						String go = "c60|0";
						try{
						BtConnection.btOutputStream.write(go.getBytes());
						}catch(IOException e){}
					}
					else if(command.equals("left")) {
						write("c," + (-speed) + "," + speed);
						String left = "c60|-90";
						try{
							BtConnection.btOutputStream.write(left.getBytes());
						}catch(IOException e){}
					}
					else if(command.equals("right")) {
						write("c," + speed + "," + (-speed));
						String right= "c60|90";
						try{
							BtConnection.btOutputStream.write(right.getBytes());
						}catch(IOException e){}
					}
					else if(command.equals("back")) {
						write("c," + (-speed) + "," + (-speed));
						String back = "c-60|0";
						try{
							BtConnection.btOutputStream.write(back.getBytes());
						}catch(IOException e){}
					}
					else if(command.equals("stop")) {
						write("c,0,0");
						String stop = "c0|0";
						try{
							BtConnection.btOutputStream.write(stop.getBytes());
						}catch(IOException e){}
					}
					else if(command.equals("fast") && speed <= 90) {
						speed += 10;
						write("c"+speed+"|0");
						String fast = "c"+speed+"|0";
						try{
							BtConnection.btOutputStream.write(fast.getBytes());
						}catch(IOException e){}
					}
					else if(command.equals("slow") && speed > 10) {
						speed -= 10;
						write("c"+speed+"|0");
						String slow = "c"+speed+"|0";
						try{
							BtConnection.btOutputStream.write(slow.getBytes());
						}catch(IOException e){}
					}
					break;
				}
			}
			if(!foundCommand) {
				tvData.append("Command not recognised\n");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean handleMessage(Message msg) {
		super.handleMessage(msg);
		switch(msg.what) {
		case BluetoothRemoteControlApp.MSG_READ:
			tvData.append("Read: " + msg.obj + "\n");
			break;
		case BluetoothRemoteControlApp.MSG_WRITE:
			tvData.append("Sent: " + msg.obj + "\n");
			break;
		}
		return super.handleMessage(msg);
	}

	/**
	 * Menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.buttonmode) {
			MainActivity.controllerMode = "button";
			Intent intent = new Intent(this, buttonControl.class);
			startActivity(intent);
			finish();
			return true;
		}
		if (id == R.id.action_jo) {
			MainActivity.controllerMode = "joystick";
			Intent intent = new Intent(this, JoystickControl.class);
			startActivity(intent);
			finish();
			return true;
		}
		if (id == R.id.connectToBluetooth) {
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
			finish();
		}
		if(id ==R.id.radar){
			Intent intent = new Intent(this, radar.class);
			startActivity(intent);
			finish();
		}
		if(id ==R.id.voiceControl){
			Intent intent = new Intent(this, MainActivity.class);
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
