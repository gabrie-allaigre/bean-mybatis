package com.talanlabs.bean.mybatis.session.factory;

import org.apache.ibatis.cache.Cache;

public interface ICacheFactory {

    /**
     * Accept key
     *
     * @param key key at verify
     * @return true or false
     */
    boolean acceptKey(String key);

    /**
     * Create cache if key is valid
     *
     * @param key                    key
     * @return cache or null
     */
    Cache createCache(String key);

}
