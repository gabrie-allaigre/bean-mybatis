package com.talanlabs.bean.mybatis.cache;

import org.apache.ibatis.session.Configuration;

import java.util.HashMap;
import java.util.Map;

public class BeanCache extends AbstractBeanCache {

    private final Map<Object, Object> cache = new HashMap<>();

    public BeanCache(Configuration configuration, BeanCacheManager beanCacheManager, Class<?> beanClass, String id) {
        super(configuration, beanCacheManager, beanClass, id);
    }

    @Override
    public void putObject(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return cache.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();

        super.clear();
    }

    @Override
    public int getSize() {
        return cache.size();
    }

}
