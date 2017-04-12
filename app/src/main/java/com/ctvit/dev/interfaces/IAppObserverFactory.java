package com.ctvit.dev.interfaces;

public interface IAppObserverFactory {
	   public void attach(IAppObserver observer);//添加观察者
	    public void detach(IAppObserver observer);//移除观察者
	    void notifyAppObservers(int type, Object obj);//通知所有被观察者对象
}
