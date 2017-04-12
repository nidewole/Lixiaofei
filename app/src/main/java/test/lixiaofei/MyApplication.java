package test.lixiaofei;


import com.ctvit.dev.RootAplication;
import com.ctvit.dev.api.CVAPI;
import com.ctvit.dev.api.CVConfig;
import com.ctvit.dev.permission.CVPermission;
import com.ctvit.dev.tools.PkTools;

import android.Manifest;
import android.content.Context;


public class MyApplication extends RootAplication{
	private Context mContext;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		init();

		
	}
	
	private void init(){
		mContext = getApplicationContext();
		//初始化参数
		CVConfig cvConfig =	new CVConfig.Builder()
				.setLog(BuildConfig.DEBUG)//设置打印日志
				.setmOpenlocation(true)//开启定位服务
				.build();
		CVAPI.getInstance().init(mContext, cvConfig);
		
	};
	
	
	
	
	
	
	
	
	
	
	
	
}
