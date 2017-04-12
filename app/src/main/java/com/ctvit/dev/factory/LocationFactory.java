package com.ctvit.dev.factory;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.ctvit.dev.service.LocationSvc;

import java.util.List;
import java.util.Locale;

public class LocationFactory {
	private final static String TAG = LocationFactory.class.getSimpleName();
	private static LocationFactory sInstance = null;//实例
    private LocationManager mLocationManager;  //定位管理

	public static LocationFactory getInstance(Context context){
		if(sInstance == null){
			synchronized (LocationFactory.class) {
				if (sInstance == null) {
					sInstance = new LocationFactory();
					sInstance.init(context);
				}
			}
		}
		return sInstance;
	}

	private String mProvider;
	private void init(Context context){
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		mProvider = sInstance.getProviderName(mLocationManager);
	}

	/**
	 * 判断是否有网络定位
	 * @return true :代表有网络定位 false：代表无网络定位
	 */
	public boolean isNetLoca(){
		return mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null ? true : false ;
	}

	/**
	 * 判断是否有GSP定位
	 * @return true :代表有GSP定位 false：代表无GSP定位
	 */
	public boolean isGSPLoca(){
		return mLocationManager.getProvider(LocationManager.GPS_PROVIDER) != null ? true : false ;
	}

	/**
	 * 获取最近lcoation
	 * @param provider
	 * @return 最近location
	 */
	public Location getLastLoca(String provider){
		provider = mProvider;
		return mLocationManager.getLastKnownLocation(mProvider);
	}
	
	/**
	 * 绑定网络定位监听
	 * @param /provider
	 * @param minTime
	 * @param minDistance
	 * @param listener
	 */
	public void bindNetLocaListener(long minTime, float minDistance,
            LocationListener listener){
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance,
				listener);  
	}
	
	/**
	 * 绑定GSP定位监听
	 * @param /provider
	 * @param minTime
	 * @param minDistance
	 * @param listener
	 */
	public void bindGSPLocaListener(long minTime, float minDistance,
            LocationListener listener){
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance,
				listener);  
	}
	
	
	public void removeLocaListener(LocationListener listener){
    	mLocationManager.removeUpdates(listener);
	}
	
	/**
	 * 获取 Location Provider 
	 * @return
	 */
    public  String getProviderName(LocationManager locationManager) {  
        // 构建位置查询条件  
        Criteria criteria = new Criteria();  
        
        criteria.setAccuracy(Criteria.ACCURACY_FINE);  // 查询精度：高  
        criteria.setAltitudeRequired(false);     // 是否查询海拨：否 
        criteria.setBearingRequired(false);    // 是否查询方位角 : 否 
        criteria.setCostAllowed(true);   // 是否允许付费：是   
        criteria.setPowerRequirement(Criteria.POWER_LOW);   // 电量要求：低  
  
        // 返回最合适的符合条件的 provider ，第 2 个参数为 true 说明 , 如果只有一个 provider 是有效的 , 则返回当前  
        // provider 
        return locationManager.getBestProvider(criteria, true);  
  
    }  

    
    
    
   /**
    * Gps 监听器调用，处理位置信息 
    * @param /context
    * @param /location
    * @return
    */
    public Address getNewAddress(List<Address> addrList) {    
        if(addrList!=null && !addrList.isEmpty()){
        	return addrList.get(0);
        }
		return null;  
    }  
    
    
    public  String parseAddr(Address address){ 
        return address.getAddressLine(0)+address.getAddressLine(1)+address.getAddressLine(2);  
    }  
    
	
	 /**
	  * 获取地址信息
	  * @param context
	  * @param location
	  * @return
	  */
    public  List<Address> getAddressbyGeoPoint(Context context ,Location location) {  
        List<Address> result = null;  
        // 先将 Location 转换为 GeoPoint  
        // GeoPoint gp =getGeoByLocation(location);  
  
        try {  
            if (location != null) {  
                // 获取 Geocoder ，通过 Geocoder 就可以拿到地址信息  
                Geocoder gc = new Geocoder(context, Locale.getDefault());  
                result = gc.getFromLocation(location.getLatitude(),  
                        location.getLongitude(), 1);  
            }  
  
        } catch (Exception e) { 
            e.printStackTrace();  
        }  
  
        return result;  
    }  
    
    
    private static final int TWO_MINUTES = 1000 * 60 * 2;  
	public boolean isBetterLocation(Location location,  
	            Location currentBestLocation) {  
	        if (currentBestLocation == null) {  
	            // A new location is always better than no location  
	            return true;  
	        }  
	  
	        // Check whether the new location fix is newer or older  
	        long timeDelta = location.getTime() - currentBestLocation.getTime();  
	        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;  
	        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;  
	        boolean isNewer = timeDelta > 0;  
	  
	        // If it's been more than two minutes since the current location, use  
	        // the new location  
	        // because the user has likely moved  
	        if (isSignificantlyNewer) {  
	            return true;  
	            // If the new location is more than two minutes older, it must be  
	            // worse  
	        } else if (isSignificantlyOlder) {  
	            return false;  
	        }  
	  
	        // Check whether the new location fix is more or less accurate  
	        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation  
	                .getAccuracy());  
	        boolean isLessAccurate = accuracyDelta > 0;  
	        boolean isMoreAccurate = accuracyDelta < 0;  
	        boolean isSignificantlyLessAccurate = accuracyDelta > 200;  
	  
	        // Check if the old and new location are from the same provider  
	        boolean isFromSameProvider = isSameProvider(location.getProvider(),  
	                currentBestLocation.getProvider());  
	  
	        // Determine location quality using a combination of timeliness and  
	        // accuracy  
	        if (isMoreAccurate) {  
	            return true;  
	        } else if (isNewer && !isLessAccurate) {  
	            return true;  
	        } else if (isNewer && !isSignificantlyLessAccurate  
	                && isFromSameProvider) {  
	            return true;  
	        }  
	        return false;  
	    }  
	 
	 /** Checks whether two providers are the same */  
	    private  boolean isSameProvider(String provider1, String provider2) {  
	        if (provider1 == null) {  
	            return provider2 == null;  
	        }  
	        return provider1.equals(provider2);  
	    }  
	    
	    
	    /**
	     * 启动定位服务
	     * @param context
	     */
	    public  void startLocaSer(Context context){
	   
	        Intent intent = new Intent();  
	        intent.setClass(context, LocationSvc.class);  
	        context.startService(intent);  
	    }
	    
	    /**
	     * 停掉定位服务
	     * @param context
	     */
	    public  void stopLocaSer(Context context){
	    	Intent intent = new Intent();  
	        intent.setClass(context, LocationSvc.class);  
	        context.stopService(intent);
	    }
  
    
}
