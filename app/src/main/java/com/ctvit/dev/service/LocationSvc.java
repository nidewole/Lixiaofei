package com.ctvit.dev.service;

import java.util.List;

import com.ctvit.dev.Conts;
import com.ctvit.dev.api.CVAPI;
import com.ctvit.dev.bean.CVInfo;
import com.ctvit.dev.bean.LocaInfo;
import com.ctvit.dev.factory.AppObserverFactory;
import com.ctvit.dev.factory.LocationFactory;
import com.ctvit.dev.tools.LogTools;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;

public class LocationSvc extends Service{
	private final static String TAG = LocationSvc.class.getSimpleName();
    private Location mCurBestLocation;//当前最好的定位服务
    private Context mContext;
	
	//GPS定位回调
	private LocationListener mGPSLocaltionListener = new LocationListener() {
		private boolean isNetRemove = false;//判断网络定位监听是否移除  
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			  boolean isbestLocal = mLocaFactory.isBetterLocation(location,  
					  mCurBestLocation);  
			  
		        if (isbestLocal) {  
		        	mCurBestLocation = location;  
		 		    }  
		        
		        // 获得GPS服务后，移除network监听  
		        if (location !=null && !isNetRemove) {
		        	mLocaFactory.removeLocaListener(mNetLocaltionListener);
		        	isNetRemove = true;  
		        }  
		        
			mCurBestLocation = location;
			
			List<Address> addrList	= mLocaFactory.getAddressbyGeoPoint(mContext, location);
			Address address = mLocaFactory.getNewAddress(addrList);
			mLocaBean.setmAddress(address);
			mLocaBean.setmLocation(mCurBestLocation);
			mLocaBean.setmCurrAddreStr(mLocaFactory.parseAddr(address));
			LogTools.d(TAG, "GPS定位","纬度："+location.getLatitude(),"经度："+location.getLongitude(),"海拔："+location.getAltitude(),"时间："+location.getTime(),"当前详细地址："+address);
			AppObserverFactory.getInstance().notifyAppObservers(Conts.LocaConts.sGSPLocaSuc, mLocaBean);
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			LogTools.d(TAG,"GPS定位", provider,status);
			// TODO Auto-generated method stub
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE://GPS服务丢失,切换至网络定位
				LogTools.e(TAG, "GPS定位","GPS服务丢失,切换至网络定位");
	        	mLocaFactory.removeLocaListener(mNetLocaltionListener);//移除Gsp定位 
	        	mLocaFactory.bindNetLocaListener( 100 * 1000, 500,mNetLocaltionListener); //添加网络定位
				break;

			default:
				break;
			}
			AppObserverFactory.getInstance().notifyAppObservers(Conts.LocaConts.sGSPLocalStatusChanged,provider+"##"+status);
		}
		
		// provider 被用户关闭后调用  
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			LogTools.e(TAG, "GPS定位","已开启GPS定位");
			AppObserverFactory.getInstance().notifyAppObservers(Conts.LocaConts.sGSPLocalOpen,provider);
		}
		
		// provider 用户开后调用  
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			LogTools.e(TAG, "GPS定位","已关闭GPS定位");
			AppObserverFactory.getInstance().notifyAppObservers(Conts.LocaConts.sGSPLocalClose,provider);

		}
		
	
	};
	
	//网络定位回调
	private LocationListener mNetLocaltionListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			mCurBestLocation = location;
			
			if(mLocaFactory.isGSPLoca()){//开启 GPS定位
				mLocaFactory.bindGSPLocaListener( 100 * 1000, 500, mGPSLocaltionListener);
				LogTools.d(TAG, "GSP定位开启...");
			}
			
			List<Address> addrList	= mLocaFactory.getAddressbyGeoPoint(mContext, location);
			Address address = mLocaFactory.getNewAddress(addrList);
			mLocaBean.setmAddress(address);
			mLocaBean.setmLocation(mCurBestLocation);
			mLocaBean.setmCurrAddreStr(mLocaFactory.parseAddr(address));
			
			LogTools.d(TAG, "网络定位","纬度："+location.getLatitude(),"经度："+location.getLongitude(),"海拔："+location.getAltitude(),"时间："+location.getTime(),"当前详细地址："+address);
			AppObserverFactory.getInstance().notifyAppObservers(Conts.LocaConts.sNetLocaSuc, mLocaBean);
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			LogTools.d(TAG, provider,status);
			// TODO Auto-generated method stub
			LogTools.e(TAG, "网络定位",provider,status);
			AppObserverFactory.getInstance().notifyAppObservers(Conts.LocaConts.sNetLocaStatusChanged, provider+"##"+status);

		}
		
		// provider  用户开后调用  
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			LogTools.e(TAG, "网络定位","已开启网络获取定位");
			AppObserverFactory.getInstance().notifyAppObservers(Conts.LocaConts.sNetLocaOpen, provider);
		}
		
		// provider 被用户关闭后调用 
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			LogTools.e(TAG, "网络定位","无法通过网络获取地理位置");
			AppObserverFactory.getInstance().notifyAppObservers(Conts.LocaConts.sNetLocaClose, provider);
		}
		
	};
	
	
	
	

	private String mProvider;
	private LocaInfo mLocaBean;
	private LocationFactory mLocaFactory;//定位工厂类
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mLocaBean = CVAPI.getInstance().getCVConfig().mCVInfo.getLocalBean();
		mLocaFactory = mLocaFactory.getInstance(this);
	}
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		LogTools.d(TAG, "启动对应的定位");
		mContext = this;
		if(mLocaFactory.isNetLoca()){//网络定位
			LogTools.d(TAG, "网络定位");
		 // 设置监听*器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
			mLocaFactory.bindNetLocaListener(100 * 1000, 500,mNetLocaltionListener); 
		}else if(mLocaFactory.isGSPLoca()){//GPS定位
			LogTools.d(TAG, "GSP定位");
			mLocaFactory.bindNetLocaListener(100 * 1000, 500,mNetLocaltionListener); 
		}else{
			LogTools.e(TAG, "无法定位，请检测网络或者GSP是否开启");
		}

		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	
	
	
	
	
	
	
 

  
	
	
	
	
   
	    

}
