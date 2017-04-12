package com.ctvit.dev.bean;

import java.io.Serializable;

import android.location.Location;

/**
 * APP基本信息（在此类获取相应信息）
 * @author lixiaofei
 *
 */
public class CVInfo implements Serializable{
    private static final long serialVersionUID = 7247714666080613254L;

	public static boolean sOpenlocation = true;//默认情况下，定位是开启的
	
	
	private LocaInfo mLocaBean;//location实体类
	
	public LocaInfo getLocalBean(){
		if(mLocaBean == null){
			mLocaBean = new LocaInfo();
		}
		return mLocaBean;
	}
	
	
	
	
	
}
