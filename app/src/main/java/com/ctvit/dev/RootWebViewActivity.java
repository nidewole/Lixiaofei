package com.ctvit.dev;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.ctvit.dev.tools.LogTools;
import com.ctvit.dev.tools.NetTools;
import com.ctvit.dev.tools.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebSettings.TextSize;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 
 * @author lixiaofei
 * 基类webView activity
 *
 */

public class RootWebViewActivity extends Activity{
	private final static String TAG = RootWebViewActivity.class.getSimpleName();
	public static final int FILECHOOSER_RESULTCODE = 1000;
	private ValueCallback<Uri> mUploadMessage;
	private WebView mWebView;
	private String mCameraFilePath;
	private Activity mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	
	
	//保证url跳转不另打开界面  WebViewClient主要帮助WebView处理各种通知、请求事件的
	
	private WebViewClient mWebViewClient = new WebViewClient(){
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			LogTools.d(TAG,"url:"+url,"title:"+view.getTitle());
			String titleString = view.getTitle();
		}
		
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			LogTools.d(TAG,"url:"+url,"title:"+view.getTitle());
			if (url != null && "tel".equals(url.substring(0, 3))) {
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(url)));
				return true;
			}
			return false;
			
		}
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// TODO Auto-generated method stub
			LogTools.e(TAG,"url:"+view.getUrl(),"title:"+view.getTitle());
			handler.proceed(); // 接受所有证书
		}
	
		
		
	};
	
	
	//WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度
	private WebChromeClient mWebChromeClient = new WebChromeClient() {

		@Override
		public boolean onJsTimeout() {
			return super.onJsTimeout();
		}

		@Override
		public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
			return super.onCreateWindow(view, dialog, userGesture, resultMsg);
		}

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			super.onShowCustomView(view, callback);
		}

		@Override
		public void onCloseWindow(WebView window) {
			super.onCloseWindow(window);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
			super.onGeolocationPermissionsShowPrompt(origin, callback);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			LogTools.d(TAG,"url:"+view.getUrl(),"title:"+view.getTitle());

		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
			LogTools.d(TAG,"acceptType:"+acceptType);
			startActivityForResult(createDefaultOpenableIntent(), FILECHOOSER_RESULTCODE);
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			LogTools.d(TAG);
			openFileChooser(uploadMsg, "");
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			LogTools.d(TAG,acceptType,capture);
			openFileChooser(uploadMsg, acceptType);
		}

	};
	
	
	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
			LogTools.d(TAG,"url:"+url,"userAgent:"+userAgent,"contentDisposition:"+contentDisposition,"mimetype:"+mimetype,"contentLength:"+contentLength);
			Tools.downLoadFile(RootWebViewActivity.this, url);
		}
	}

	
	
	
	

	
	/**
	 * 初始化webView设置
	 * @param jsObjHashMap 存储向JS提供本地对象使用里面的函数，若JS不调用本地方法可以传null
	 */
	@SuppressLint("NewApi")
	public void initWebViewSetting(WebView webView, HashMap<String, Object> jsObjHashMap){
		if(webView == null){
			new NullPointerException("webView == null");
		}
		setWebView(webView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		
		//本地向JS提供条用本地对象方法逻辑
		if(jsObjHashMap != null && !jsObjHashMap.isEmpty()){
			Iterator<Entry<String, Object>> iterator = jsObjHashMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				//		mWebView.addJavascriptInterface(new OrderFactory(this, mHwf, callback), "orderFactory");
				mWebView.addJavascriptInterface(entry.getValue(), entry.getKey());
			}
		}
		
		mWebView.requestFocus(View.FOCUS_DOWN);
		// 建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT,无网络时，使用LOAD_CACHE_ELSE_NETWORK
		if (NetTools.isNet(this)) {
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		} else {
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}
		mWebView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
		mWebView.getSettings().setDatabaseEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
		mWebView.getSettings().setBuiltInZoomControls(true);// 设置支持缩放
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBlockNetworkImage(false);
		mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
		mWebView.getSettings().setNeedInitialFocus(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//		webview.getSettings().setSupportMultipleWindows(true);
		mWebView.getSettings().setTextSize(TextSize.NORMAL);
		mWebView.setScrollbarFadingEnabled(true);
		mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.getSettings().setSaveFormData(false);
		mWebView.getSettings().setSavePassword(false);
		mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setPluginState(PluginState.ON);
		//关闭硬件加速（解决webView过渡期出现白块同时界面闪烁）
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		// 启用地理定位
		mWebView.getSettings().setGeolocationEnabled(true);
		// 设置定位的数据库路径
		mWebView.getSettings().setGeolocationDatabasePath(dir);
		// 最重要的方法，一定要设置，这就是出不来的主要原因
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setLoadsImagesAutomatically(true);
		// 支持缩放
		mWebView.setInitialScale(35);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.setScrollbarFadingEnabled(true);
		// 启动缓存
		mWebView.getSettings().setAppCacheEnabled(true);
		// 启用地理定位
		mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		mWebView.setWebViewClient(mWebViewClient);
		mWebView.setWebChromeClient(mWebChromeClient);
		mWebView.setDownloadListener(new MyWebViewDownLoadListener());
	}
	
	private void setWebView(WebView webView){
		mWebView = webView;
	}
	
	private String mUrl;
	public void loadH5(String url){
		if(mWebView == null){
			new NullPointerException("mWebView == null");
		}
		mUrl = url;
		mWebView.loadUrl(url);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
			if (result == null && data == null && resultCode == Activity.RESULT_OK) {
				File cameraFile = new File(mCameraFilePath);
				if (cameraFile.exists()) {
					result = Uri.fromFile(cameraFile);
					// Broadcast to the media scanner that we have a new photo
					// so it will be added into the gallery for the user.
					sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
				}
			}
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}
	
	
	//重写物理按键的返回逻辑(实现返回键跳转到上一页)
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	      //用户触摸返回键
	      if(keyCode == KeyEvent.KEYCODE_BACK){
	          //判断webView能否返回上一页(是否存在历史记录)
	          if(mWebView.canGoBack()){
	        	  mWebView.goBack();
	              return true;//直接返回,不执行父类点击事件
	          }else{
	        	  mContext.finish();
	          }
	      }
	      return super.onKeyDown(keyCode, event);
	  }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private Intent createDefaultOpenableIntent() {
		// Create and return a chooser with the default OPENABLE
		// actions including the camera, camcorder and sound
		// recorder where available.
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("*/*");
		Intent chooser = createChooserIntent(createCameraIntent(), createCamcorderIntent(), createSoundRecorderIntent());
		chooser.putExtra(Intent.EXTRA_INTENT, i);
		return chooser;
	}
	private Intent createChooserIntent(Intent... intents) {
		Intent chooser = new Intent(Intent.ACTION_CHOOSER);
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
		chooser.putExtra(Intent.EXTRA_TITLE, "File Chooser");
		return chooser;
	}

	private Intent createCameraIntent() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File externalDataDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File cameraDataDir = new File(externalDataDir.getAbsolutePath() + File.separator + "browser-photos");
		cameraDataDir.mkdirs();
		mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
		return cameraIntent;
	}

	private Intent createCamcorderIntent() {
		return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	}

	private Intent createSoundRecorderIntent() {
		return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
	}

}
