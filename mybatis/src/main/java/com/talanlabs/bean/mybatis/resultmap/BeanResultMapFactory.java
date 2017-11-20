package com.talanlabs.bean.mybatis.resultmap;

import com.talanlabs.bean.mybatis.resultmap.factory.AssociationResultMappingFactory;
import com.talanlabs.bean.mybatis.resultmap.factory.CollectionResultMappingFactory;
import com.talanlabs.bean.mybatis.resultmap.factory.ColumnResultMappingFactory;
import com.talanlabs.bean.mybatis.resultmap.factory.IResultMappingFactory;
import com.talanlabs.bean.mybatis.resultmap.factory.NlsColumnResultMappingFactory;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.factory.AbstractResultMapFactory;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.meta.ValidationBeanException;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BeanResultMapFactory extends AbstractResultMapFactory {

    private static final Logger LOG = LoggerFactory.getLogger(BeanResultMapFactory.class);

    private final List<IResultMappingFactory> resultMappingFactories;

    public BeanResultMapFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration);

        this.resultMappingFactories = new ArrayList<>();

        addResultMappingFactory(new ColumnResultMappingFactory(beanConfiguration));
        addResultMappingFactory(new AssociationResultMappingFactory(beanConfiguration));
        addResultMappingFactory(new CollectionResultMappingFactory(beanConfiguration));
        addResultMappingFactory(new NlsColumnResultMappingFactory(beanConfiguration));
    }

    /**
     * Add result mapping factory, first
     *
     * @param resultMappingFactory a factory for result
     */
    public void addResultMappingFactory(IResultMappingFactory resultMappingFactory) {
        this.resultMappingFactories.add(0, resultMappingFactory);
    }

    @Override
    public boolean acceptKey(String key) {
        return ResultMapNameHelper.isResultMapKey(key) || ResultMapNameHelper.isNestedResultMapKey(key);
    }

    @Override
    public ResultMap createResultMap(String key) {
        if (ResultMapNameHelper.isResultMapKey(key)) {
            Class<?> beanClass = ResultMapNameHelper.extractBeanClassInResultMapKey(key);
            if (beanClass != null) {
                try {
                    getBeanConfiguration().getMetaBean().validateBean(beanClass);
                } catch (ValidationBeanException e) {
                    throw new IllegalArgumentException("Failed to validate Bean=" + beanClass, e);
                }

                return createBeanResultMap(beanClass, 0, "", key);
            }
        } else if (ResultMapNameHelper.isNestedResultMapKey(key)) {
            Class<?> beanClass = ResultMapNameHelper.extractBeanClassInNestedResultMapKey(key);
            Integer level = ResultMapNameHelper.extractDepthInNestedResultMapKey(key);
            String prefix = ResultMapNameHelper.extractColumnPrefixInNestedResultMapKey(key);
            if (beanClass != null) {
                try {
                    getBeanConfiguration().getMetaBean().validateBean(beanClass);
                } catch (ValidationBeanException e) {
                    throw new IllegalArgumentException("Failed to validate Bean=" + beanClass, e);
                }

                return createBeanResultMap(beanClass, level != null ? level : -1, prefix != null ? prefix : "", key);
            }
        }
        return null;
    }

    private ResultMap createBeanResultMap(Class<?> beanClass, int level, String prefix, String key) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create ResultMap for " + beanClass);
        }

        List<ResultMapping> resultMappings = createResultMappings(beanClass, level, prefix);
        ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(getBeanConfiguration(), key, beanClass, resultMappings, null);
        return inlineResultMapBuilder.build();
    }

    private List<ResultMapping> createResultMappings(Class<?> beanClass, int level, String prefix) {
        List<ResultMapping> resultMappings = new ArrayList<>();

        IResultMappingFactory.ResultMappingContext resultMappingContext = new IResultMappingFactory.ResultMappingContext(level, prefix);

        MetaInfoBean metaInfoBean = getBeanConfiguration().getMetaBean().forBeanClass(beanClass);

        metaInfoBean.getPropertyNames().stream().sorted().forEach(propertyName -> {
            ResultMapping resultMapping = buildResultMapping(resultMappingContext, beanClass, propertyName);
            if (resultMapping != null) {
                resultMappings.add(resultMapping);
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Not yet implemented propertyName=" + propertyName);
                }
            }
        });

        return resultMappings;
    }

    private ResultMapping buildResultMapping(IResultMappingFactory.ResultMappingContext resultMappingContext, Class<?> beanClass,
                                             String propertyName) {
        for (IResultMappingFactory resultMappingFactory : resultMappingFactories) {
            if (resultMappingFactory.acceptProperty(beanClass, propertyName)) {
                return resultMappingFactory.buildResultMapping(resultMappingContext, beanClass, propertyName);
            }
        }
        return null;
    }
}
