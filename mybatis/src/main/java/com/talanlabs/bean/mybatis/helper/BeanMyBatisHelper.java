package com.talanlabs.bean.mybatis.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanMyBatisHelper {

    private static final Logger LOG = LoggerFactory.getLogger(BeanMyBatisHelper.class);

    private BeanMyBatisHelper() {
        super();
    }

    /**
     * Get name for bean class
     *
     * @param beanClass bean class
     * @return name
     */
    public static String beanClassToString(Class<?> beanClass) {
        return beanClass.getName();
    }

    /**
     * Load a bean class
     *
     * @param beanClassString bean class string
     * @return Bean class
     */
    @SuppressWarnings("unchecked")
    public static <E> Class<E> loadBeanClass(String beanClassString) {
        try {
            if (StringUtils.isNotBlank(beanClassString)) {
                return (Class<E>) BeanMyBatisHelper.class.getClassLoader().loadClass(beanClassString);
            }
            return null;
        } catch (ClassNotFoundException e) {
            LOG.error("Failed to load bean class {}", beanClassString, e);
            return null;
        }
    }
}
