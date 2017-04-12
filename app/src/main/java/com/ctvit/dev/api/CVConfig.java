package com.ctvit.dev.api;

import com.ctvit.dev.BuildConf;
import com.ctvit.dev.bean.CVInfo;

import android.text.TextUtils;
/**
 * CVSDK基本配置信息（在此类设置相应信息）
 * @author lixiaofei
 *
 */
public class CVConfig {	
	
	public static class Builder{
		private boolean DEBUG = false;//是否展示日志 SDK默认是关闭
		private boolean mOpenlocation = false ;//是否开启定位服务 默认情况下，定位是开启的
		
		/**
		 * 设置是否显示SDK里日志
		 * @param isLog true:显示 false:不显示
		 * @return
		 */
		public Builder setLog(boolean isLog){
			this.DEBUG = isLog;
			return this;
		}
		
	

		/**
		 *  设置是否开启定位服务（定位优先使用网络定位，若GSP开启状态，在进行GSP定位）
		 * @param mOpenlocation true:开启 false:关闭
		 */
		public Builder setmOpenlocation(boolean mOpenlocation) {
			this.mOpenlocation = mOpenlocation;
			return this;
		}

		
		  /* 
         * CVConfig对象创建器，想得到一个CVConfig对象必须使用build 方法， 
         * 在方法中增加对Builder参数的验证，并以异常的形式告诉给开发人员。 
         */  
		public CVConfig build(){
			
			/* 检查Builder对象中的数据是否合法。 
             * 针对这个例子，就是检查主键冲突，外键制约等 
             * 如果不满足我们可以抛出一个IllegalArgumentException 
             */  
			return new CVConfig(this);
		}



	

		
		
	}
	
	
	public CVInfo mCVInfo;
	private CVConfig(Builder builder){
		if(mCVInfo == null){
			mCVInfo = new CVInfo();
		}
		BuildConf.DEBUG = builder.DEBUG;
		mCVInfo.sOpenlocation = builder.mOpenlocation;
	}

	
}
