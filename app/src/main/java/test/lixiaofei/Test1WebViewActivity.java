package test.lixiaofei;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.ctvit.dev.RootWebViewActivity;
import com.ctvit.dev.tools.ResTools;

public class Test1WebViewActivity extends RootWebViewActivity{
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		  WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		setContentView(R.layout.test1_webview_activity);
		init();
		 if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); }
	}
	
	private void init(){
		initView();
		initData();
	}
	
	
	private void initView(){
		mWebView = (WebView) this.findViewById(R.id.wvH5);

	}
	
	private void initData(){
        String url = ResTools.getConfString(this, "test1H5Url");
        url = "http://192.168.1.39:8080/VideoTest/pages/demo1/";
        initWebViewSetting(mWebView,null);
        loadH5(url);

	}
	
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	
	
	
}
