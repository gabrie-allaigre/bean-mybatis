package com.talanlabs.bean.mybatis.cache;

import java.util.*;

public class BeanCacheManager {

    private ThreadLocal<Boolean> undispatchThreadLocal;
    private Map<Class<?>, Set<String>> linkMap;
    private Set<ICacheListener> cacheListeners;

    public BeanCacheManager() {
        super();

        this.linkMap = Collections.synchronizedMap(new HashMap<>());
        this.undispatchThreadLocal = new ThreadLocal<>();
        this.cacheListeners = new HashSet<>();
    }

    /**
     * Add link
     *
     * @param beanClass     origin
     * @param linkBeanClass link
     */
    public synchronized void putCacheLink(Class<?> beanClass, Class<?> linkBeanClass) {
        Set<String> links = linkMap.computeIfAbsent(beanClass, k -> new HashSet<>());
        links.add(CacheNameHelper.buildCacheKey(linkBeanClass));
    }

    /**
     * All link for bean
     *
     * @param beanClass bean
     * @return links
     */
    public synchronized Set<String> getCacheLinks(Class<?> beanClass) {
        Set<String> links = linkMap.computeIfAbsent(beanClass, k -> new HashSet<>());
        return Collections.unmodifiableSet(links);
    }

    /**
     * Dispatch clear in current thread
     *
     * @return dispatch
     */
    public synchronized boolean isDispatch() {
        Boolean res = undispatchThreadLocal.get();
        return res == null || !res;
    }

    /**
     * Active dispatch for current thread
     */
    public synchronized void dispatch() {
        undispatchThreadLocal.remove();
    }

    /**
     * Desactive dispatch for current thread
     */
    public synchronized void undispatch() {
        undispatchThreadLocal.set(true);
    }

    /**
     * Add cache observer
     *
     * @param cacheListener cache observer
     */
    public synchronized void addCacheListener(ICacheListener cacheListener) {
        cacheListeners.add(cacheListener);
    }

    /**
     * Remove cache observer
     *
     * @param cacheListener cache observer
     */
    public synchronized void removeCacheListener(ICacheListener cacheListener) {
        cacheListeners.remove(cacheListener);
    }

    /**
     * Fire clear for id cache
     *
     * @param id cache
     */
    public synchronized void fireCleared(String id) {
        for (ICacheListener l : cacheListeners) {
            l.cleared(id);
        }
    }
}
