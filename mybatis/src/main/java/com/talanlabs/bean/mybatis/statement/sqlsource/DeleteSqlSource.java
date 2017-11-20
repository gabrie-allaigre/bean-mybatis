package com.talanlabs.bean.mybatis.statement.sqlsource;

import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DeleteSqlSource implements SqlSource {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteSqlSource.class);

    private final BeanConfiguration beanConfiguration;

    private final SqlSource sqlSource;

    public DeleteSqlSource(BeanConfiguration beanConfiguration, Class<?> beanClass) {
        super();

        this.beanConfiguration = beanConfiguration;

        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(beanConfiguration);
        String sql = buildDelete(beanClass);
        sqlSource = sqlSourceParser.parse(sql, Map.class, new HashMap<>());
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

    private String buildDelete(Class<?> beanClass) {
        MetaBean metaBean = beanConfiguration.getMetaBean();

        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();

        Entity entity = beanClass.getAnnotation(Entity.class);

        SQL sqlBuilder = new SQL();
        sqlBuilder.DELETE_FROM(entity.name());

        String idPropertyName = metaBean.getIdPropertyName(beanClass);
        sqlBuilder.WHERE(beanSqlResultHelper.buildSetColumn(beanClass, "", idPropertyName));

        String versionPropertyName = metaBean.getVersionPropertyName(beanClass);
        if (StringUtils.isNotBlank(versionPropertyName)) {
            sqlBuilder.WHERE(beanSqlResultHelper.buildSetColumn(beanClass, "", versionPropertyName));
        }

        String sql = sqlBuilder.toString();
        if (LOG.isDebugEnabled()) {
            LOG.debug(sql);
        }
        return sql;
    }
}
