/*
 * Rory Crispin --rorycrispin.co.uk -- rozzles.com
 * 
 * Distributed under the Creative Commons 
 * Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)
 * License, full conditions can be found here: 
 * http://creativecommons.org/licenses/by-sa/3.0/
 *   
 *   This is free software, and you are welcome to redistribute it
 *   under certain conditions;
 *   
 *   Go crazy,
 *   Rozz xx 
 *
 */
package com.rozzles.camera;

import com.rozzles.camera.R;
import com.rozzles.camera.BlueComms.LocalBinder;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

public class ShutterRelease extends Activity {
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_right);
	}
	
	MainActivity mid = new MainActivity();
	boolean mBounded;
	BlueComms mServer;
	public int prog = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		 Intent mIntent = new Intent(this, BlueComms.class);
	     bindService(mIntent, mConnection, BIND_AUTO_CREATE);
		
		
		setContentView(R.layout.activity_shutter_release);
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/robotoLI.otf");
		TextView tv = (TextView) findViewById(R.id.s1Text);
		tv.setTypeface(tf);
		SeekBar seekBar = (SeekBar) findViewById(R.id.TimeDelaySeek);
		final TextView seekBarValue = (TextView) findViewById(R.id.delayIntView);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				seekBarValue.setText(String.valueOf(progress + " seconds"));
				prog = progress;
			};

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		
	}
	

	
	
	ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName name) {
			mBounded = false;
			mServer = null;
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			mBounded = true;
			LocalBinder mLocalBinder = (LocalBinder)service;
			mServer = mLocalBinder.getServerInstance();
		}
	};

	@Override
	protected void onStop() {
		super.onStop();
		if(mBounded) {
			unbindService(mConnection);
			mBounded = false;
		}
	};
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent = new Intent(getApplicationContext(), FlatHome.class);
		startActivityForResult(myIntent, 0);
		overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_right);
		return true;
	}

	public void CaptureClick(View view) {
		mServer.sendData("1," + prog + ",0,0,0,0,0,0,0,0!");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.shutter_release, menu);
		return true;
	}

	public void wrKill(View v) {

	}

	public void wrH(View v) {
		mServer.sendData("9,99,0,0,0,0,0,0,0,0!");
	}

	public void wrL(View v) {
		mServer.sendData("L");
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		   if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) { 
		       mServer.sendData("1," + prog + ",0,0,0,0,0,0,0,0!");
		       return true;
		   } else {
		       return super.onKeyDown(keyCode, event); 
		   }
		}

}
