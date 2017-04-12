package com.ctvit.dev.interfaces;

import com.ctvit.dev.bean.CacheInfo;

public interface ICache {
    /**
     * 根据url返回相应的CacheEntry
     *
     * @param url 资源的url
     * @return 与改url匹配的缓存数据
     */
    CacheInfo findCache(String url);

    /**
     * 移除单个缓存
     *
     * @param url 资源的url
     * @return 是否移除成功
     */
    boolean removeCache(String url);
}
