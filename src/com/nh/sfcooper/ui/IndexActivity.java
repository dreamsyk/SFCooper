package com.nh.sfcooper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.nh.sfcooper.AppManager;
import com.nh.sfcooper.R;
import com.nh.sfcooper.ui.base.BaseActivity;

public class IndexActivity extends BaseActivity implements TextWatcher,OnClickListener{

	private View title_more;
	
	private View quick_huodaofukuan;
	private View quick_jijian;
	private View quick_history;
	
	private EditText edit_order_sn;
	private View del_order_edit;
	private View scan_order;
	
	private View query_logistics;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		findViewById();
		initView();
		                                                                                                                                               
	}

	@Override
	protected void findViewById() {
		
		this.title_more = $(R.id.web_share_more);
		this.quick_huodaofukuan = $(R.id.quick_huodaofukuan);
		this.quick_jijian = $(R.id.quick_jijian);
		this.quick_history = $(R.id.quick_history);
		
		this.edit_order_sn = (EditText) $(R.id.login_input_name);
		this.del_order_edit = $(R.id.order_sn_delete);
		this.scan_order = $(R.id.scan_button);
		
		this.query_logistics= $(R.id.query_logistics);
		
	}
	
	@Override
	protected void initView() {
		
		this.title_more.setOnClickListener(this);
		this.title_more.setVisibility(View.INVISIBLE);
		
		this.quick_huodaofukuan.setOnClickListener(this);
		this.quick_jijian.setOnClickListener(this);
		this.quick_history.setOnClickListener(this);
		
		this.edit_order_sn.addTextChangedListener(this);
		this.del_order_edit.setOnClickListener(this);
		this.scan_order.setOnClickListener(this);
		
		this.query_logistics.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.web_share_more: {// 标题栏 "更多" 按钮
			DisplayToast("功能正在开发中，敬请期待...");
		}
			break;
		case R.id.quick_huodaofukuan: //  货到付款 
		case R.id.quick_jijian: {// 我要寄件
			openActivity(DeliveryRequestActivity.class);
		}
			break;
		case R.id.quick_history: {// 操作历史
			openActivity(OpreateHistory.class);
		}
			break;
		case R.id.order_sn_delete: {// 清空订单号
			this.edit_order_sn.setText("");
		}
			break;
		case R.id.scan_button: {// 扫描订单
			Intent intent=new Intent();
			intent.setClass(IndexActivity.this, CaptureActivity.class);
			startActivityForResult(intent, 1);
		}
			break;
		case R.id.query_logistics:{//查询物流

			if (edit_order_sn.getText().toString().length() > 0) {
				
				Intent intent=new Intent();   
				intent.setClass(IndexActivity.this, QueryResultActivity.class);
				intent.putExtra("order_sn", this.edit_order_sn.getText().toString());
				startActivity(intent);
				
			} else {
				
				DisplayToast("运单号码为空，请重新出入!!!");
			}
			
			
		}break;
		
		}
	}
	
	@Override
	public void afterTextChanged(Editable s) {

		if (edit_order_sn.getText().toString().length() > 0) {
			
			del_order_edit.setVisibility(View.VISIBLE);
			scan_order.setVisibility(View.INVISIBLE);
			
		} else {
			
			scan_order.setVisibility(View.VISIBLE);
			del_order_edit.setVisibility(View.INVISIBLE);
		}
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	
	private long mExitTime;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK ){
			
			if((System.currentTimeMillis()-mExitTime)>800){
			   Toast.makeText(this, "再按一次退出正德顺丰~~", Toast.LENGTH_SHORT).show();
			   mExitTime=System.currentTimeMillis();
			   return true;
			}
			else {
				
				AppManager.getInstance().killAllActivity();
				AppManager.getInstance().AppExit(this);
			}
			
		}
		 return super.onKeyDown(keyCode, event);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case CaptureActivity.SCAN_ORDER_SN_SUCCESS: {
			String sn = data.getStringExtra("order_sn");
			edit_order_sn.setText(sn);
		}
			break;
		}
	}



	

}
