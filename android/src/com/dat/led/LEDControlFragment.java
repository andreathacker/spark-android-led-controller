// @formatter:off
/**
 * Copyright (c) 2013 Aloompa LLC.
 * All rights reserved.
 *
 * The contents of this file are subject to the license terms.
 * You may not use, reproduce, distribute or modify this file
 * except in compliance with the License.
 *
 * PROPRIETARY/CONFIDENTIAL. DO NOT ALTER OR REMOVE THIS HEADER.
 *
 * Create Date: Dec 21, 2013, 5:22:00 PM
 * Original Author: davidthacker
 * Additional Authors:
 */
// @formatter:on
package com.dat.led;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.dat.led.LedParam.LED;

/**
 * @author davidthacker
 */
public class LEDControlFragment extends Fragment implements OnClickListener {
	public final static String	TAG					= LEDControlFragment.class.getSimpleName();

	public final static int		MAX_BRIGHTNESS		= 255;
	public final static long	MAX_TIME			= 5000;

	public static final String	ARG_LED_POSITION	= "arg_led_position";

	private Button				postButton;
	private SeekBar				startBar;
	private SeekBar				endBar;
	private SeekBar				timeBar;
	private CheckBox			loopCheck;
	private CheckBox			invertCheck;

	private LED					mLed;
	private int					mStartBrightness;
	private int					mEndBrightness;
	private long				mTime;

	public static LEDControlFragment newInstance(LED led){
		LEDControlFragment fragment = new LEDControlFragment();

		Bundle args = new Bundle();
		args.putParcelable(ARG_LED_POSITION, led);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View controlView = inflater.inflate(R.layout.control_fragment, null);

		setupStartBar(controlView);
		setupEndBar(controlView);
		setupTimeBar(controlView);

		loopCheck = (CheckBox) controlView.findViewById(R.id.check_box_loop);
		invertCheck = (CheckBox) controlView.findViewById(R.id.check_box_invert);

		postButton = (Button) controlView.findViewById(R.id.post_operation_btn);
		postButton.setOnClickListener(this);

		mLed = (LED) getArguments().get(ARG_LED_POSITION);
		Log.d(TAG, "led = " + mLed);

		return controlView;
	}

	@Override
	public void onClick(View v){
		int id = v.getId();

		if(id == postButton.getId()){
			Log.d(TAG, "Post operation clicked");

			Toast.makeText(getActivity(), "Start= " + mStartBrightness + ", end= " + mEndBrightness + ", time= " + mTime, Toast.LENGTH_SHORT).show();

			performLedOperation(new LedParam(mLed, mStartBrightness, mEndBrightness, mTime, loopCheck.isChecked()));
		}
	}

	public void setupStartBar(View v){
		startBar = (SeekBar) v.findViewById(R.id.seek_bar_start_brightness);
		startBar.setMax(MAX_BRIGHTNESS);
		startBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar){
				mStartBrightness = seekBar.getProgress();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar){

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				mStartBrightness = progress;
			}
		});
		mStartBrightness = startBar.getProgress();
	}

	public void setupEndBar(View v){
		endBar = (SeekBar) v.findViewById(R.id.seek_bar_end_brightness);
		endBar.setMax(MAX_BRIGHTNESS);
		endBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar){
				mEndBrightness = seekBar.getProgress();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar){

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				mEndBrightness = progress;
			}
		});
		mEndBrightness = endBar.getProgress();
	}

	public void setupTimeBar(View v){
		timeBar = (SeekBar) v.findViewById(R.id.seek_bar_time);
		timeBar.setMax((int) MAX_TIME);
		timeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar){
				mTime = seekBar.getProgress();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar){

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				mTime = progress;
			}
		});
		mTime = timeBar.getProgress();
	}

	public void performLedOperation(LedParam param){
		new ProcessOperationTask(ProcessOperationTask.URL_LED_FUNCTION).execute(param);
	}

	public class ProcessOperationTask extends AsyncTask<LedParam, Void, Void> {
		public static final String	URL_LED_FUNCTION		= "https://api.spark.io/v1/devices/53ff6a065067544844451287/led";

		public static final String	PARAM_ACCESS_TOKEN_KEY	= "access_token=";
		public static final String	PARAM_ACCESS_TOKEN_VAL	= "1e17b3754d9610f0ebda439a18231d8cc0f24c1a";
		public static final String	PARAM_FUNCTION_KEY		= "params=";

		String						url;

		public ProcessOperationTask(String url) {
			this.url = url;
		}

		@Override
		protected Void doInBackground(LedParam... params){
			Log.d(TAG, "Running Strobe function");

			LedParam ledParam = params[0];

			try{
				URL obj = new URL(url);
				HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

				// add reuqest header
				con.setRequestMethod("POST");

				// Populate parameters with the access token and LedParam string
				String urlParameters = getPostParameterString(ledParam);

				// Send post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();

				int responseCode = con.getResponseCode();
				Log.d(TAG, "Sending POST request to URL: " + url);
				Log.d(TAG, "Post params: " + urlParameters);
				Log.d(TAG, "Response Code : " + responseCode);

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while((inputLine = in.readLine()) != null){
					response.append(inputLine);
				}
				in.close();

				// print result
				Log.d(TAG, response.toString());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}

		private String getPostParameterString(LedParam ledParam){
			// Always post the access token
			String urlParms = PARAM_ACCESS_TOKEN_KEY + PARAM_ACCESS_TOKEN_VAL;

			// Add led function
			urlParms = urlParms + "&" + PARAM_FUNCTION_KEY + ledParam.getParamString();

			Log.d(TAG, "Url params: " + urlParms);
			return urlParms;
		}
	}

}
