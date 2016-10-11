package com.nh.sfcooper.ui;

import com.nh.sfcooper.R;
import com.nh.sfcooper.ui.base.BaseActivity;

import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		findViewById();
		initView();

		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				openActivity(IndexActivity.class);
				SplashActivity.this.finish();
			}
			
		}, 1000);
		
	}
	
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}
	

}
