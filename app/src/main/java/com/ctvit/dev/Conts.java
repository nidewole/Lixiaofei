package com.ctvit.dev;
/**
 * 消息通信常量（4000 - 6000 之间并且不能重复）
 * @author lixiaofei
 *
 */
public class Conts {
	public final static String H5_URL = "H5_URL";//H5 url
	public final static String H5_TITLE = "H5_TITLE";//H5 标题Title
	
	
	
	public interface LocaConts{
		public final int sGSPLocaSuc = 4000;//GSP定位成功
		public final int sGSPLocalOpen = 4001;//GSP定位开启调用
		public final int sGSPLocalClose = 4002;//GSP定位关闭调用
		public final int sGSPLocalStatusChanged = 4003;//GSP定位状态发生变化
		
		public final int sNetLocaSuc = 4004;//网络定位成功
		public final int sNetLocaOpen = 4005;//网络定位开启调用
		public final int sNetLocaClose = 4006;//网络定位关闭调用
		public final int sNetLocaStatusChanged = 4007;//网络定位状态发生变化

		
	}
	
	
	

}
