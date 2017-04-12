package com.ctvit.dev.bean;

import java.io.Serializable;
/**
 * 定位bean
 * @author lixiaofei
 *
 */

import com.ctvit.dev.tools.LogTools;

import android.location.Address;
import android.location.Location;
public class LocaInfo implements Serializable{
	private final static String TAG = LocaInfo.class.getSimpleName();
    private static final long serialVersionUID = 7247714666080613255L;
    private Address mAddress;
	private Location mLocation;//定位
	private String mCurrAddrStr;//当前位置
    
	public Address getmAddress() {
		return mAddress;
	}
	
	public void setmAddress(Address mAddress) {
		if(mAddress == null){
			LogTools.e(TAG, "mAddress == null");
			return ;
		}
		this.mAddress = mAddress;
	}
	public Location getmLocation() {
		return mLocation;
	}
	public void setmLocation(Location mLocation) {
		this.mLocation = mLocation;
	}
	public String getmCurrAddrStr() {
		return mCurrAddrStr;
	}
	public void setmCurrAddreStr(String mCurrAddress) {
		this.mCurrAddrStr = mCurrAddress;
	}
    

}
