package com.talanlabs.bean.mybatis.session;

import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.rsql.configuration.IRsqlConfiguration;
import com.talanlabs.bean.mybatis.rsql.configuration.RsqlConfigurationBuilder;
import com.talanlabs.bean.mybatis.session.defaults.DefaultTypeHandlerFactory;
import com.talanlabs.bean.mybatis.session.dispatcher.TriggerDispatcher;
import com.talanlabs.bean.mybatis.session.factory.ICacheFactory;
import com.talanlabs.bean.mybatis.session.factory.IMappedStatementFactory;
import com.talanlabs.bean.mybatis.session.factory.IResultMapFactory;
import com.talanlabs.bean.mybatis.session.factory.ITypeHandlerFactory;
import com.talanlabs.bean.mybatis.session.handler.INlsColumnHandler;
import com.talanlabs.bean.mybatis.session.registry.CacheFactoryRegistry;
import com.talanlabs.bean.mybatis.session.registry.MappedStatementFactoryRegistry;
import com.talanlabs.bean.mybatis.session.registry.ResultMapFactoryRegistry;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

public class BeanConfiguration extends Configuration {

    protected MappedStatementFactoryRegistry mappedStatementFactoryRegistry = new MappedStatementFactoryRegistry();

    protected ResultMapFactoryRegistry resultMapFactoryRegistry = new ResultMapFactoryRegistry();

    protected CacheFactoryRegistry cacheFactoryRegistry = new CacheFactoryRegistry();

    protected INlsColumnHandler nlsColumnHandler = null;

    protected TriggerDispatcher triggerDispatcher = new TriggerDispatcher();

    protected ITypeHandlerFactory typeHandlerFactory = new DefaultTypeHandlerFactory();

    protected MetaBean metaBean = new MetaBean(this);

    protected IRsqlConfiguration rsqlConfiguration = RsqlConfigurationBuilder.newBuilder(this).build();

    protected BeanSqlResultHelper beanSqlResultHelper = new BeanSqlResultHelper(this);

    public BeanConfiguration() {
        super();
    }

    public BeanConfiguration(Environment environment) {
        super(environment);
    }

    /**
     * @return get mapped statement factory registry, default null
     */
    public MappedStatementFactoryRegistry getMappedStatementFactoryRegistry() {
        return mappedStatementFactoryRegistry;
    }

    /**
     * Set mapped statement factory registry
     */
    public void setMappedStatementFactoryRegistry(MappedStatementFactoryRegistry mappedStatementFactoryRegistry) {
        this.mappedStatementFactoryRegistry = mappedStatementFactoryRegistry;
    }

    /**
     * @return get result map factory registry default null
     */
    public ResultMapFactoryRegistry getResultMapFactoryRegistry() {
        return resultMapFactoryRegistry;
    }

    /**
     * @param resultMapFactoryRegistry result map factory registry
     */
    public void setResultMapFactoryRegistry(ResultMapFactoryRegistry resultMapFactoryRegistry) {
        this.resultMapFactoryRegistry = resultMapFactoryRegistry;
    }

    /**
     * @return get cache factory registry
     */
    public CacheFactoryRegistry getCacheFactoryRegistry() {
        return cacheFactoryRegistry;
    }

    /**
     * @param cacheFactoryRegistry cache factory registry
     */
    public void setCacheFactoryRegistry(CacheFactoryRegistry cacheFactoryRegistry) {
        this.cacheFactoryRegistry = cacheFactoryRegistry;
    }

    /**
     * @return get nls column handler
     */
    public INlsColumnHandler getNlsColumnHandler() {
        return this.nlsColumnHandler;
    }

    /**
     * @param nlsColumnHandler nls column handler
     */
    public void setNlsColumnHandler(INlsColumnHandler nlsColumnHandler) {
        this.nlsColumnHandler = nlsColumnHandler;
    }

    /**
     * @return get trigger dispatcher
     */
    public TriggerDispatcher getTriggerDispatcher() {
        return triggerDispatcher;
    }

    /**
     * @param triggerDispatcher trigger dispatcher
     */
    public void setTriggerDispatcher(TriggerDispatcher triggerDispatcher) {
        this.triggerDispatcher = triggerDispatcher;
    }

    /**
     * @return get type handler factory
     */
    public ITypeHandlerFactory getTypeHandlerFactory() {
        return typeHandlerFactory;
    }

    /**
     * @param typeHandlerFactory type handler factory
     */
    public void setTypeHandlerFactory(ITypeHandlerFactory typeHandlerFactory) {
        this.typeHandlerFactory = typeHandlerFactory;
    }

    /**
     * Get a type handler
     *
     * @param typeHandlerClass class
     * @return instance of type handler
     */
    @SuppressWarnings("unchecked")
    public <E extends TypeHandler<?>> E getTypeHandler(Class<E> typeHandlerClass) {
        E typeHandler = (E) getTypeHandlerRegistry().getTypeHandler(typeHandlerClass);
        if (typeHandler == null) {
            typeHandler = getTypeHandlerFactory().create(typeHandlerClass);
        }
        return typeHandler;
    }

    /**
     * @return get meta bean
     */
    public MetaBean getMetaBean() {
        return metaBean;
    }

    /**
     * Set a meta bean
     *
     * @param metaBean meta bean
     */
    public void setMetaBean(MetaBean metaBean) {
        this.metaBean = metaBean;
    }

    /**
     * @return rsql configuration
     */
    public IRsqlConfiguration getRsqlConfiguration() {
        return rsqlConfiguration;
    }

    /**
     * Set rsql configuration
     *
     * @param rsqlConfiguration config
     */
    public void setRsqlConfiguration(IRsqlConfiguration rsqlConfiguration) {
        this.rsqlConfiguration = rsqlConfiguration;
    }

    public BeanSqlResultHelper getBeanSqlResultHelper() {
        return beanSqlResultHelper;
    }

    public void setBeanSqlResultHelper(BeanSqlResultHelper beanSqlResultHelper) {
        this.beanSqlResultHelper = beanSqlResultHelper;
    }

    @Override
    public boolean hasCache(String id) {
        boolean res = super.hasCache(id);
        if (!res) {
            res = verifyAndCreateCache(id);
        }
        return res;
    }

    @Override
    public Cache getCache(String id) {
        if (hasCache(id)) {
            return super.getCache(id);
        }
        return super.getCache(id);
    }

    private synchronized boolean verifyAndCreateCache(String id) {
        if (cacheFactoryRegistry != null) {
            ICacheFactory cacheFactory = cacheFactoryRegistry.getCacheFactory(id);
            if (cacheFactory != null) {
                Cache cache = cacheFactory.createCache(id);
                if (cache != null) {
                    addCache(cache);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasResultMap(String id) {
        boolean res = super.hasResultMap(id);
        if (!res) {
            res = verifyAndCreateResultMap(id);
        }
        return res;
    }

    @Override
    public ResultMap getResultMap(String id) {
        if (hasResultMap(id)) {
            return super.getResultMap(id);
        }
        return super.getResultMap(id);
    }

    private synchronized boolean verifyAndCreateResultMap(String id) {
        if (resultMapFactoryRegistry != null) {
            IResultMapFactory resultMapFactory = resultMapFactoryRegistry.getResultMapFactory(id);
            if (resultMapFactory != null) {
                ResultMap resultMap = resultMapFactory.createResultMap(id);
                if (resultMap != null) {
                    addResultMap(resultMap);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasStatement(String statementName, boolean validateIncompleteStatements) {
        boolean res = super.hasStatement(statementName, validateIncompleteStatements);
        if (!res) {
            res = verifyAndCreateMappedStatement(statementName);
        }
        return res;
    }

    @Override
    public MappedStatement getMappedStatement(String id, boolean validateIncompleteStatements) {
        if (hasStatement(id, validateIncompleteStatements)) {
            return super.getMappedStatement(id, validateIncompleteStatements);
        }
        return super.getMappedStatement(id, validateIncompleteStatements);
    }

    private synchronized boolean verifyAndCreateMappedStatement(String id) {
        if (mappedStatementFactoryRegistry != null) {
            IMappedStatementFactory mappedStatementFactory = mappedStatementFactoryRegistry.getMappedStatementFactory(id);
            if (mappedStatementFactory != null) {
                MappedStatement mappedStatement = mappedStatementFactory.createMappedStatement(id);
                if (mappedStatement != null) {
                    addMappedStatement(mappedStatement);
                    return true;
                }
            }
        }
        return false;
    }
}
