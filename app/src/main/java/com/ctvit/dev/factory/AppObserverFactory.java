package com.ctvit.dev.factory;

import java.util.Enumeration;

import com.ctvit.dev.interfaces.IAppObserver;
import com.ctvit.dev.interfaces.IAppObserverFactory;

/**
 * Created by lixiaofie on 2016/6/13.
 * 观察者工厂，主要添加移除通知观察者更新
 */

public class AppObserverFactory implements IAppObserverFactory{
    private static AppObserverFactory sInstance = new AppObserverFactory();

    private AppObserverFactory() { }

    public static AppObserverFactory getInstance() {
        if (sInstance == null) {
            synchronized (AppObserverFactory.class) {
                if (sInstance == null) {
                    sInstance = new AppObserverFactory();
                }
            }
        }
        return sInstance;
    }

    //存储观察者链表
    private java.util.Vector<IAppObserver> mAppOserversVector = new java.util.Vector<IAppObserver>();

    /**
     * 获取所有观察者
     *
     * @return
     */
    private Enumeration<IAppObserver> sysDBobservers() {
        return ((java.util.Vector<IAppObserver>) mAppOserversVector.clone()).elements();
    }

    /**
     *  添加观察者
     * @param observer
     */
    @Override
    public void attach(IAppObserver observer) {
        mAppOserversVector.add(observer);
    }

    /**
     *  移除观察者
     * @param observer
     */
    @Override
    public void detach(IAppObserver observer) {
        mAppOserversVector.removeElement(observer);
    }

    /**
     * 通知被观察者对象更新（不同类型通知type不能重复）
     * @param type 类型
     * @param obj  数据
     */
    @Override
    public void notifyAppObservers(int type, Object obj) {
        Enumeration<IAppObserver> enumeration = sysDBobservers();
        while (enumeration.hasMoreElements()) {
            IAppObserver observer = enumeration.nextElement();
            if (observer != null)//解决观察者被设为null的情况
            {
                observer.update(type,obj);
            }
        }
    }



}