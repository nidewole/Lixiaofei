package com.ctvit.dev;


import com.ctvit.dev.tools.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
/**
 * 
 * @author lixiaofei
 * 基类activity
 *
 */


public class RootActivity extends Activity{
    protected boolean mIsActive;//全局变量标识应用是否后台切换到前台

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
		if(!Tools.isAppOnForeground(this)){//app 进入后台
	         mIsActive = false;//记录当前已经进入后台
	     }
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	    if(!mIsActive){//从后台唤醒，进入前台
	         mIsActive = true;
	     }
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	
	
	
	public void init(){
		
	}
	
	
	public void initView(){
		
	}
	
	
	public void initData(){
		
	}
	
	
	public void bindListener(View view){
		
	}
	
	
	 /**
     * [页面跳转]
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        Intent intent = new Intent(RootActivity.this,clz);
       startActivity(intent);
    }

    /**
     * [携带数据的页面跳转]
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
       startActivityForResult(intent, requestCode);
    }
    
    private ProgressDialog mDialog;
    public void showLoading() {
        if (mDialog != null && mDialog.isShowing()) return;
        mDialog = new ProgressDialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("请求网络中...");
        mDialog.show();
    }

    public void dismissLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

}
