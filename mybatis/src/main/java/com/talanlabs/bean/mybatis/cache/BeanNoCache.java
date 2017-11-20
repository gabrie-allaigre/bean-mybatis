package com.talanlabs.bean.mybatis.cache;

import org.apache.ibatis.session.Configuration;

public class BeanNoCache extends AbstractBeanCache {

    public BeanNoCache(Configuration configuration, BeanCacheManager beanCacheManager, Class<?> beanClass, String id) {
        super(configuration, beanCacheManager, beanClass, id);
    }

    @Override
    public void putObject(Object key, Object value) {
        // Nothing
    }

    @Override
    public Object getObject(Object key) {
        return null;// Nothing
    }

    @Override
    public Object removeObject(Object key) {
        return null; // Nothing
    }

    @Override
    public int getSize() {
        return 0; // Nothing
    }
}
