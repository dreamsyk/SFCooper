package com.nh.sfcooper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtils {

	private HttpParams httpParams;
	private HttpClient httpClient;

	public HttpUtils() {
		getHttpClient();
	}

	public String doGet(String url, Map params) {

		/* 建立HTTPGet对象 */
		String paramStr = "";
		Iterator<Map.Entry> iter = params.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			paramStr += paramStr = "&" + key + "=" + val;
		}
		if (!paramStr.equals("")) {
			paramStr = paramStr.replaceFirst("&", "?");
			url += paramStr;
		}
		HttpGet httpRequest = new HttpGet(url);
		String strResult = "doGetError";
		try {
			/* 发送请求并等待响应 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				strResult = EntityUtils.toString(httpResponse.getEntity());
			} else {
				strResult = "Error Response: "
						+ httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = "Error Response: ";
			e.printStackTrace();
		} catch (IOException e) {
			strResult = "Error Response: ";
			e.printStackTrace();
		} catch (Exception e) {
			strResult = "Error Response: ";
			e.printStackTrace();
		}
		Log.v("strResult", strResult);

		return strResult;
	}

	public String doPost(String url, List<NameValuePair> params) {

		/* 建立HTTPPost对象 */
		HttpPost httpRequest = new HttpPost(url);
		String strResult = "doPostError";
		try {
			/* 添加请求参数到请求对象 */
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			/* 发送请求并等待响应 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				strResult = EntityUtils.toString(httpResponse.getEntity());
			} else {
				strResult = "Error Response: "
						+ httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = "Error Response: ";
			e.printStackTrace();
		} catch (IOException e) {
			strResult = "Error Response: ";
			e.printStackTrace();
		} catch (Exception e) {
			strResult = "Error Response: ";
			e.printStackTrace();
		}
		Log.v("strResult", strResult);

		return strResult;
	}

	public String doGet(String url) {
		return doGet(url, new HashMap());
	}

	public String doPost(String url) {
		return doPost(url, new ArrayList<NameValuePair>());
	}

	public HttpClient getHttpClient() {
		// 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
		this.httpParams = new BasicHttpParams();
		// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		// 设置重定向，缺省为 true
		HttpClientParams.setRedirecting(httpParams, true);
		// 设置 user agent
		String userAgent = "Juning Co.Ltd  (Android)";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);
		// 创建一个 HttpClient 实例
		// 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient
		// 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient
		httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}
}
