package com.ctvit.dev.interfaces;

public interface IAppObserver {
    /**
     * 通知绑定观察者数据更新
     * @param type 需要更新类型
     * @param data 更新数据
     */
    public void update(int type, Object data);
}

