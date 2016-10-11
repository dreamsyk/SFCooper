package com.nh.sfcooper.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.nh.sfcooper.R;
import com.nh.sfcooper.adapter.OpreaterHistoryAdapter;
import com.nh.sfcooper.config.Constants;
import com.nh.sfcooper.model.OperateItemData;
import com.nh.sfcooper.ui.base.BaseActivity;

public class OpreateHistory extends BaseActivity implements OnClickListener,OnItemClickListener {

	private String TITLE_NAME = "操作记录";

	private final int LOAD_OPERATE_RECORD_SUCCESS = 0x0101;

	private View title_back;
	private TextView titleText;
	
	private ListView listView;
	private OpreaterHistoryAdapter adapter;
	private List<OperateItemData> datas = new ArrayList<OperateItemData>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opeate_history);
		findViewById();
		initView();
		readAllOpreateRecord();
		
	}
	
	@Override
	protected void findViewById() {
		this.title_back = $(R.id.title_back);
		this.titleText = $(R.id.titleText);
		this.listView = $(R.id.listView1);
	}

	@Override
	protected void initView() {
		this.title_back.setOnClickListener(this);
		this.titleText.setText(TITLE_NAME);

		this.adapter = new OpreaterHistoryAdapter(this, datas);
		this.listView.setAdapter(adapter);
		this.listView.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back: {
			this.finish();
		}
			break;
			
		}
	}
	
	/**
	 * 读取所有订单查询记录
	 */
	private void readAllOpreateRecord() {
		
		datas.clear();
		
		SharedPreferences spf = getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		
		Map<String, ?> map = spf.getAll();
		Set<String> package_sn = map.keySet();
		
		for (String sn : package_sn) {
			
			OperateItemData item=new OperateItemData();
			item.setContent(sn);
			item.setTime((String)map.get(sn));
			
			datas.add(item);
			
			Log.v("record", sn);
		}
		
		
		uiFlusHandler.sendEmptyMessage(LOAD_OPERATE_RECORD_SUCCESS);
		
	}
	
	@SuppressLint("HandlerLeak")
	private Handler uiFlusHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			
			case LOAD_OPERATE_RECORD_SUCCESS: {
				adapter.notifyDataSetChanged();
			}
				break;
				
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int positon, long id) {
		
		Intent intent=new Intent();   
		intent.setClass(this, QueryResultActivity.class);
		intent.putExtra("order_sn", datas.get(positon).getContent());
		startActivity(intent);
		
	}

}
