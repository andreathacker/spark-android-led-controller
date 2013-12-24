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
 * Create Date: Dec 22, 2013, 2:30:16 PM
 * Original Author: davidthacker
 * Additional Authors:
 */
// @formatter:on
package com.dat.led;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author davidthacker
 */
public class DashboardFragment extends Fragment {

	private ViewPager	mViewPager;

	public static DashboardFragment newInstance(){
		return new DashboardFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View dashboardView = inflater.inflate(R.layout.dashboard_fragment, null);

		mViewPager = (ViewPager) dashboardView.findViewById(R.id.dashboard_view_pager);
		mViewPager.setAdapter(new DashboardFragmentAdapter(getFragmentManager()));

		return dashboardView;
	}

}
