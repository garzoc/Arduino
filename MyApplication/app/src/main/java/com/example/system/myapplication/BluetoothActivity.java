package com.example.system.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class BluetoothActivity extends AppCompatActivity implements Handler.Callback {
	static Activity activity;
	private static BluetoothRemoteControlApp appState;
	// When launching a new activity and this one stops it doesn't mean something bad (no connection loss)
	protected boolean preventCancel;
	private static String TAG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appState = new BluetoothRemoteControlApp();
		BluetoothActivity.activity = this;
		try {
			if(!BtConnection.getBluetoothConnection()) {
				BtConnection.blueTooth();
				BtConnection.setBluetoothData();
				//MainActivity.connected = true;
			}
		}catch (IllegalStateException il){
			AlertBoxes.bluetoothAlert(this);
		}catch(NullPointerException nu){
			AlertBoxes.bluetoothAlert(this);
		}
	}

	protected boolean write(String message) {
		// Send command to the Bluetooth device
		return appState.write(message);
	}

	protected void disconnect() {
		// Disconnect from the Bluetooth device
		if(BluetoothRemoteControlApp.D) Log.i(TAG, "Connection end request");
		appState.disconnect();
	}

	public boolean handleMessage(Message msg) {
		switch(msg.what) {
			case BluetoothRemoteControlApp.MSG_OK:
				if(BluetoothRemoteControlApp.D) Log.i(TAG, "Result of child activity OK");
			break;
			case BluetoothRemoteControlApp.MSG_CANCEL:
				if(BluetoothRemoteControlApp.D) Log.e(TAG, "Got canceled");
				setResult(BluetoothRemoteControlApp.MSG_CANCEL, new Intent());
				finish();
			break;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Message.obtain(new Handler(this), resultCode).sendToTarget();
	}

	@Override
	protected void onResume() {
		TAG = getLocalClassName();
		if(BluetoothRemoteControlApp.D) Log.i(TAG, "Set handler");
		appState.setActivityHandler(new Handler(this));
		preventCancel = false;
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(BluetoothRemoteControlApp.D) Log.i(TAG, "Back pressed");
		setResult(BluetoothRemoteControlApp.MSG_OK, new Intent());
		finish();
	}
	
	@Override
	public void finish() {
		// Remove the handler from the main application class
		appState.setActivityHandler(null);
		super.finish();
	}

	@Override
	protected void onPause() {
		// Pausing an activity isn't allowed, unless it has been prevented
		if(!preventCancel) {
			Message.obtain(new Handler(this), BluetoothRemoteControlApp.MSG_CANCEL).sendToTarget();
		}
		super.onPause();
	}
}
