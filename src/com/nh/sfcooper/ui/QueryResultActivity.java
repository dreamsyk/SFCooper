package com.nh.sfcooper.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.nh.sfcooper.R;
import com.nh.sfcooper.config.Constants;
import com.nh.sfcooper.dialog.LoadingDialog;
import com.nh.sfcooper.ui.base.BaseActivity;

public class QueryResultActivity extends BaseActivity {

	private View title_back;
	private TextView titleText;
	
	private WebView webView;
	
	private final int SHOW_LOADING_DIALOG = 0x0102;
	private final int DISMISS_LOADING_DIALOG = 0x0103;
	private LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_result);
		findViewById();
		initView();

	}

	@Override
	protected void findViewById() {
		this.webView = (WebView) $(R.id.webView1);
		this.title_back = $(R.id.title_back);
		this.titleText = $(R.id.titleText);
	}

	@Override
	protected void initView() {

		this.title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QueryResultActivity.this.finish();
			}
		});

		this.titleText.setText("查询结果");

		initInfoPageContentView();
		infoPageLoadData();
	}
	 
	/**
	 * 保存物流查询记录
	 */
	private void saveRecord(String sn) {
		
		Log.v("save-record---START", sn);
		
		SharedPreferences spf = getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		
		Editor editor=spf.edit();
		
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-d H:mm:ss");
		
		editor.putString(sn, sdf.format(new Date()));
		
		editor.commit();
		
		
		Log.v("save-record---END", sn);
		
	}
	
	/**
	 * 初始化WEBVIEW
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled"})
	private void initInfoPageContentView() {
		
		WebView webs[] = new WebView[1];
		webs[0] = this.webView;

		for (int i = 0; i < webs.length; i++) {

			final WebView temp = webs[i];
			
			WebSettings settings = webs[i].getSettings();

			settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
			// 支持javascript
			settings.setJavaScriptEnabled(true);
			//设置默认编码为UTF-8
			settings.setDefaultTextEncodingName(HTTP.UTF_8);
		
			// 设置滚动条内侧面显示
			webs[i].setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			// 设置可以支持缩放
			settings.setSupportZoom(true);
			// 设置出现缩放工具
			settings.setBuiltInZoomControls(true);
			// 设置默认缩放方式尺寸是far
			settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
			// 设置缩放比率
			//webs[i].setInitialScale(55);
			
			// 自适应屏幕
			settings.setLoadWithOverviewMode(true);
			// 扩大比例的缩放
			settings.setUseWideViewPort(true);
		
			temp.setOnTouchListener(new OnTouchListener() {
				
				private float OldX1, OldY1, OldX2, OldY2;
				private float NewX1, NewY1, NewX2, NewY2;


				@Override
				public boolean onTouch(View v, MotionEvent event) {

					switch (event.getAction()) {
					case MotionEvent.ACTION_POINTER_2_DOWN:
						if (event.getPointerCount() == 2) {
							OldX1 = event.getX(0);
							OldY1 = event.getY(0);
							OldX2 = event.getX(1);
							OldY2 = event.getY(1);
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if (event.getPointerCount() == 2) {// 双指移动
							if (OldX1 == -1 && OldX2 == -1)
								break;
							NewX1 = event.getX(0);
							NewY1 = event.getY(0);
							NewX2 = event.getX(1);
							NewY2 = event.getY(1);
							float disOld = (float) Math.sqrt((Math.pow(OldX2
									- OldX1, 2) + Math.pow(OldY2 - OldY1, 2)));
							float disNew = (float) Math.sqrt((Math.pow(NewX2
									- NewX1, 2) + Math.pow(NewY2 - NewY1, 2)));
							Log.e("onTouch", "disOld=" + disOld + "|disNew="
									+ disNew);
							if (disOld - disNew >= 10) {
								// 缩小
								//temp.zoomOut();
								 temp.loadUrl("javascript:mapScale=1;");
								Log.e("onTouch", "zoomOut");
							} else if (disNew - disOld >= 10) {
								// 放大
								//temp.zoomIn();
								temp.loadUrl("javascript:mapScale=-1;");
								Log.e("onTouch", "zoomIn");
							}
							OldX1 = NewX1;
							OldX2 = NewX2;
							OldY1 = NewY1;
							OldY2 = NewY2;
						}
						
						break;
					case MotionEvent.ACTION_UP:
						if (event.getPointerCount() < 2) {
							OldX1 = -1;
							OldY1 = -1;
							OldX2 = -1;
							OldY2 = -1;
						} 
						break;
					}
					return false;
				}
			});

			temp.setWebChromeClient(new WebChromeClient() {
				
				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					// TODO Auto-generated method stub
					super.onProgressChanged(view, newProgress);
					
					dialog.setProgress(newProgress);
					
					if (newProgress == 100) {
						uiFlusHandler.sendEmptyMessage(DISMISS_LOADING_DIALOG);
					}
						
					Log.i("Progress", ""+newProgress);
					
				}
 
			});
			
			temp.setWebViewClient(new WebViewClient(){
				
				@Override
				public void onPageFinished(WebView view, String url) {
					// TODO Auto-generated method stub
					super.onPageFinished(view, url);
					// 页面下载完毕,却不代表页面渲染完毕显示出来
					// WebChromeClient中progress==100时也是一样
					if (view.getContentHeight() != 0) {
						// 这个时候网页才显示
						Log.e("Detail Info WEBVIEW", "Page Finished");
					}
				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// TODO Auto-generated method stub
					// 自身加载新链接,不做外部跳转
					view.loadUrl(url);
					Log.i("WEBVIEW LINK CLICKED",url);
					return true;
				}

			});

		}
		

	}

	/**
	 * WEBVIEW数据填充
	 */
	private void infoPageLoadData() {
		
		uiFlusHandler.sendEmptyMessage(SHOW_LOADING_DIALOG);
		
		String order_sn = getIntent().getStringExtra("order_sn");
		
		saveRecord(order_sn);
		
		if (order_sn != null) {
			String url = "http://m.kuaidi100.com/index_all.html?postid="
					+ order_sn + "&callbackurl=m.kuaidi100.com";
			this.webView.loadUrl(url);
		}
		
	}
	
	private Handler uiFlusHandler=new Handler(){

  		@Override
		public void handleMessage(Message msg) {
  			
  			switch (msg.what) {
  			
			case SHOW_LOADING_DIALOG: {
				if(dialog!=null&&dialog.isShowing()){
					dialog.dismiss();
				} 
				 
				dialog=new LoadingDialog(QueryResultActivity.this);
				dialog.show();
				
				Log.i("dialog-state", "dialog-showing!!!!");
			}
				break;
				
			case DISMISS_LOADING_DIALOG: {
				if(dialog!=null&&dialog.isShowing()){
					dialog.dismiss();
				} 
				
				Log.i("dialog-state", "dialog-dismissed!!!!");
			}
				break;

			}
		}
		
	};
}
