package com.ctvit.dev.api;

import java.beans.PropertyChangeSupport;
import java.lang.ref.SoftReference;

import com.ctvit.dev.bean.CVInfo;
import com.ctvit.dev.factory.LocationFactory;
import com.ctvit.dev.service.LocationSvc;
import com.ctvit.dev.tools.LogTools;
import com.ctvit.dev.tools.Tools;
import com.okgo.OkGo;
import com.okgo.cache.CacheEntity;
import com.okgo.cache.CacheMode;
import com.okgo.cookie.store.PersistentCookieStore;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class CVAPI {
	private final static String TAG = CVAPI.class.getSimpleName();
	private  static CVAPI sInstantce = new CVAPI();//实例
	private SoftReference<Context> mContext;//软引用上下文
	private CVConfig mCvConfig ;//基本配置
	
	public static CVAPI getInstance(){
	      if (sInstantce == null) {
	            synchronized (CVAPI.class) {
	                if (sInstantce == null) {
	                	sInstantce = new CVAPI();
	                }
	            }
	        }
		return sInstantce;
	}
	
	/**
	 * 初始化API
	 * @param context
	 * @param config
	 */
	public void init(Context context,CVConfig config){
		mContext = new SoftReference<Context>(context);
		mCvConfig = config;
		initOKHttp(context);
		if(CVInfo.sOpenlocation && !Tools.isOverMarshmallow()){  //开启定位服务并且系统在6.0一下，初始CVAPI时，就启动定位服务
			LogTools.d(TAG, "启动定位服务...");
			startLocaSer(context);
		}
	}
	
	
	private void initOKHttp(Context context){
		OkGo.init((Application)context);
		OkGo.getInstance().debug(TAG)
		//如果使用默认的 60秒,以下三行也不需要传
        .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
        .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
        .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间
        .setCacheMode(CacheMode.NO_CACHE)//可以全局统一设置缓存模式,默认是不使用缓存,可以不传
       
        .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE) //可以全局统一设置缓存时间,默认永不过期

        //如果不想让框架管理cookie,以下不需要
  //    .setCookieStore(new MemoryCookieStore())                //cookie使用内存缓存（app退出后，cookie消失）
        .setCookieStore(new PersistentCookieStore());          //cookie持久化存储，如果cookie不过期，则一直有效

        //可以设置https的证书,以下几种方案根据需要自己设置,不需要不用设置
//        .setCertificates()                                  //方法一：信任所有证书
//        .setCertificates(getAssets().open("srca.cer"))      //方法二：也可以自己设置https证书
//        .setCertificates(getAssets().open("aaaa.bks"), "123456", getAssets().open("srca.cer"))//方法三：传入bks证书,密码,和cer证书,支持双向加密

        //可以添加全局拦截器,不会用的千万不要传,错误写法直接导致任何回调不执行
//    .addInterceptor(new Interceptor() {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            return chain.proceed(chain.request());
//        }
//    })

        //这两行同上,不需要就不要传
      //  .addCommonHeaders(headers)                                         //设置全局公共头
      //  .addCommonParams(params);                                          //设置全局公共参数
	}
	
	
	
	
	/**
	 * 获取应用上下文Context
	 * @return 上下文
	 */
	private Context getCvContext(){
		if(mContext == null || mContext.get() == null){
			throw new NullPointerException("CVAPI未进行初始化...");
		}
		return mContext.get();
	}
	
	
	/**
	 * 获取基本配置
	 * @return
	 */
	public CVConfig getCVConfig(){
		return mCvConfig;
	}
	
	/**
	 * 启动定位服务
	 * @param context
	 */
	public void startLocaSer(Context context){
		CVInfo.sOpenlocation = true;
		LocationFactory.getInstance(context).startLocaSer(context);
	}
	
	/**
	 * 停止定位服务
	 * @param context
	 */
	public void stopLocaSer(Context context){
		CVInfo.sOpenlocation = false;
		LocationFactory.getInstance(context).stopLocaSer(context);	
     }
	
	

	

	

}
