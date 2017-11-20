package com.talanlabs.bean.mybatis.cache;

import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.ValidationBeanException;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.factory.AbstractCacheFactory;
import com.talanlabs.bean.mybatis.session.handler.INlsColumnHandler;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanCacheFactory extends AbstractCacheFactory {

    private static final Logger LOG = LoggerFactory.getLogger(BeanCacheFactory.class);

    private final BeanCacheManager beanCacheManager;

    public BeanCacheFactory(BeanConfiguration beanConfiguration) {
        this(beanConfiguration, new BeanCacheManager());
    }

    public BeanCacheFactory(BeanConfiguration beanConfiguration, BeanCacheManager beanCacheManager) {
        super(beanConfiguration);

        this.beanCacheManager = beanCacheManager;
    }

    @Override
    public boolean acceptKey(String key) {
        return CacheNameHelper.isCacheKey(key);
    }

    @Override
    public Cache createCache(String key) {
        if (CacheNameHelper.isCacheKey(key)) {
            Class<?> beanClass = CacheNameHelper.extractBeanClassInCacheKey(key);
            if (beanClass != null) {
                try {
                    getBeanConfiguration().getMetaBean().validateBean(beanClass);
                } catch (ValidationBeanException e) {
                    throw new IllegalArgumentException("Failed to validate Bean=" + beanClass, e);
                }

                return createBeanCache(beanClass, key);
            }
        }
        return null;
    }

    private Cache createBeanCache(Class<?> beanClass, String key) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create Cache for " + beanClass);
        }

        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        metaBean.findAllLinks(beanClass).forEach(subBeanClass -> {
            if (beanClass != subBeanClass) {
                beanCacheManager.putCacheLink(subBeanClass, beanClass);
            }
        });

        com.talanlabs.bean.mybatis.annotation.Cache cache = beanClass.getAnnotation(com.talanlabs.bean.mybatis.annotation.Cache.class);

        Cache res;
        if (cache == null) {
            res = new BeanCache(getBeanConfiguration(), beanCacheManager, beanClass, key);
        } else {
            res = new BeanCache(getBeanConfiguration(), beanCacheManager, beanClass, key);
            res = new LruCache(res);
            ((LruCache) res).setSize(cache.size());
            res = new ScheduledCache(res);
            ((ScheduledCache) res).setClearInterval(cache.clearInterval());
            if (cache.readWrite()) {
                res = new SerializedCache(res);
            }
            res = new LoggingCache(res);
            res = new SynchronizedCache(res);
        }

        INlsColumnHandler nlsColumnHandler = getBeanConfiguration().getNlsColumnHandler();
        if (nlsColumnHandler != null && metaBean.isAllUseNlsColumn(beanClass)) {
            res = new NlsColumnCache(nlsColumnHandler, res);
        }

        return res;
    }
}
