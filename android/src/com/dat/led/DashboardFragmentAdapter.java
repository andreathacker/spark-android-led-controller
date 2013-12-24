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
 * Create Date: Dec 22, 2013, 3:00:21 PM
 * Original Author: davidthacker
 * Additional Authors:
 */
// @formatter:on
package com.dat.led;

import com.dat.led.LedParam.LED;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author davidthacker
 */
public class DashboardFragmentAdapter extends FragmentStatePagerAdapter {

	private int	ledsAvailable;

	public DashboardFragmentAdapter(FragmentManager fm) {
		super(fm);
		ledsAvailable = 2;
	}

	@Override
	public int getCount(){
		return ledsAvailable;
	}

	@Override
	public Fragment getItem(int position){
		LED led = LED.getLED(position);
		return LEDControlFragment.newInstance(led);
	}

	@Override
	public CharSequence getPageTitle(int position){
		switch(LED.getLED(position)){
			case LED_A:
				return "LED 1";
			case LED_B:
				return "LED 2";
			default:
				return "null";
		}
	}

}
