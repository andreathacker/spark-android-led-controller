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
 * Create Date: Dec 21, 2013, 8:19:04 PM
 * Original Author: davidthacker
 * Additional Authors:
 */
// @formatter:on
package com.dat.led;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author davidthacker
 */
public class LedParam {
	public final static String	TAG	= LedParam.class.getSimpleName();

	public LED					mLed;
	public String				mStart;
	public String				mEnd;
	public String				mTime;
	public String				mLoop;

	public enum LED implements Parcelable {
		LED_A("l0"),
		LED_B("l1");

		private final String	mLedEnum;

		private LED(String led) {
			mLedEnum = led;
		}

		public boolean equalsName(String otherLed){
			return (otherLed == null) ? false : mLedEnum.equals(otherLed);
		}

		public String toString(){
			return mLedEnum;
		}

		public static LED getLED(int position){
			switch(position){
				case 0:
					return LED_A;
				case 1:
					return LED_B;
				default:
					return null;
			}
		}

		@Override
		public int describeContents(){
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags){
			dest.writeInt(ordinal());
		}

		// @formatter:off
		public static final Creator<LED>	CREATOR	= new Creator<LED>() {
			@Override
			public LED createFromParcel(final Parcel source){
				return LED.values()[source.readInt()];
			}

			@Override
			public LED[] newArray(final int size){
				return new LED[size];
			}
		};
		// @formatter:on
	}

	public LedParam(LED led, int start, int end, long time, boolean loop) {
		setOperation(led, start, end, time, loop);
	}

	public void setOperation(LED led, int start, int end, long time, boolean loop){
		mLed = led;
		mStart = "" + start;
		mEnd = "" + end;
		mTime = "" + time;
		mLoop = "" + (loop == true ? 1 : 0);
	}

	public String getParamString(){
		String output = mLed + "," + mStart + "," + mEnd + "," + mTime + "," + mLoop;

		Log.d(TAG, "Param string: " + output);
		return output;
	}
}
