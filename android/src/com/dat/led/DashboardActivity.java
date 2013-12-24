package com.dat.led;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class DashboardActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		return false;
	}

}
