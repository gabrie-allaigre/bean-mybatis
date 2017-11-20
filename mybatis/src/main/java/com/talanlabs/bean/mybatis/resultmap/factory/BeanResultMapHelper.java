package com.talanlabs.bean.mybatis.resultmap.factory;

import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.apache.ibatis.mapping.ResultMapping;

import java.util.ArrayList;
import java.util.List;

public class BeanResultMapHelper {

    private BeanResultMapHelper() {
        super();
    }

    /**
     * Build composites result
     *
     * @param beanConfiguration
     * @param beanClass
     * @param propertyNames
     * @param columns
     * @return
     */
    public static List<ResultMapping> buildComposites(BeanConfiguration beanConfiguration, Class<?> beanClass, String[] propertyNames, String[] columns) {
        MetaInfoBean metaInfoBean = beanConfiguration.getMetaBean().forBeanClass(beanClass);
        List<ResultMapping> composites = new ArrayList<>();
        if (propertyNames.length > 1) {
            int param = 1;
            for (int i = 0; i < propertyNames.length; i++) {
                String propertyName = propertyNames[i];

                composites.add(new ResultMapping.Builder(beanConfiguration, StatementNameHelper.buildParam(param), columns[i], metaInfoBean.getPropertyClass(propertyName)).build());

                param++;
            }
        }
        return composites;
    }
}
