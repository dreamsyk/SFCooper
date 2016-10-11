package com.nh.sfcooper.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.nh.sfcooper.R;
import com.nh.sfcooper.dialog.LoadingDialog;
import com.nh.sfcooper.ui.base.BaseActivity;

public class DeliveryRequestActivity extends BaseActivity implements OnClickListener{
	
	private String TITLE_NAME = "免费预约上门取件";
	
	private View title_back;
	private TextView titleText;
	
	private EditText delivery_name;
	private EditText delivery_addr;
	private EditText delivery_tel;
	private EditText delivery_comment;
	
	private View delivery_request;
	
	private final int SHOW_LOADING_DIALOG = 0x0102;
	private final int DISMISS_LOADING_DIALOG = 0x0103;
	private LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivery_request);
		
		mHandler=new Handler();
		
		findViewById();
		initView();
		                                                                                                                                               
	}
	
	@Override
	protected void findViewById() {

		this.title_back = $(R.id.title_back);
		this.titleText = (TextView) $(R.id.titleText);
		
		this.delivery_name = $(R.id.delivery_name);
		this.delivery_addr = $(R.id.delivery_detail);
		this.delivery_tel = $(R.id.delivery_tel);
		this.delivery_comment = $(R.id.delivery_comment);
		
		this.delivery_request = $(R.id.delivery_request_button);
		
	}

	@Override
	protected void initView() {
		
		this.title_back.setOnClickListener(this);
		this.titleText.setText(TITLE_NAME);
		
		this.delivery_name.setFocusableInTouchMode(true);
		this.delivery_addr.setFocusableInTouchMode(true);
		this.delivery_tel.setFocusableInTouchMode(true);
		this.delivery_comment.setFocusableInTouchMode(true);
		
		this.delivery_request.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.title_back: {
			this.finish();
		}
			break;
		case R.id.delivery_request_button: {

			if(checkValite()){
				
				uiFlusHandler.sendEmptyMessage(SHOW_LOADING_DIALOG);
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						uiFlusHandler.sendEmptyMessage(DISMISS_LOADING_DIALOG);
						DeliveryRequestActivity.this.finish();
						DisplayToast("预约成功，稍后会有快递员上门取件 ~ ~ ~");
					}
				}, 1500);
				
			} else {
				DisplayToast("上门取件信息输入错误，请检查并重新提交！！！");
			}
			
		}
			break;

		}
	}
	
	/**
	 * 校验上门取件信息
	 */
	private boolean checkValite() {
		
		if (delivery_name.getText().length() == 0) return false;
		if (delivery_addr.getText().length() == 0) return false;
		if (delivery_tel.getText().length() == 0) return false;
		
		return true;
	}

 	private Handler uiFlusHandler=new Handler(){

  		@Override
		public void handleMessage(Message msg) {
  			
  			switch (msg.what) {
  			
			case SHOW_LOADING_DIALOG: {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				 
				dialog=new LoadingDialog(DeliveryRequestActivity.this);
				dialog.show();
				
				Log.i("dialog-state", "dialog-showing!!!!");
			}
				break;
				
			case DISMISS_LOADING_DIALOG: { 
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				
				Log.i("dialog-state", "dialog-dismissed!!!!");
			}
				break;

			}
		}
		
	};
}
