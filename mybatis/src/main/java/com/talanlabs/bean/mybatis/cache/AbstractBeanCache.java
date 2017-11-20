package com.talanlabs.bean.mybatis.cache;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.session.Configuration;

import java.util.concurrent.locks.ReadWriteLock;

public abstract class AbstractBeanCache implements Cache {

    private final Configuration configuration;
    private final BeanCacheManager beanCacheManager;
    private final Class<?> beanClass;
    private final String id;

    public AbstractBeanCache(Configuration configuration, BeanCacheManager beanCacheManager, Class<?> beanClass, String id) {
        super();

        this.configuration = configuration;
        this.beanCacheManager = beanCacheManager;
        this.beanClass = beanClass;
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void clear() {
        if (beanCacheManager.isDispatch()) {
            beanCacheManager.undispatch();

            beanCacheManager.getCacheLinks(beanClass).forEach(cacheName -> configuration.getCache(cacheName).clear());

            beanCacheManager.dispatch();

            beanCacheManager.fireCleared(getId());
        }
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cache)) {
            return false;
        }

        Cache otherCache = (Cache) o;
        return getId().equals(otherCache.getId());
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        return getId().hashCode();
    }
}
