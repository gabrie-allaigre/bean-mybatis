package com.talanlabs.bean.mybatis.resultmap.factory;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.data.CountryBean;
import com.talanlabs.bean.mybatis.data.Test1Bean;
import com.talanlabs.bean.mybatis.handler.IdTypeHandler;
import com.talanlabs.bean.mybatis.resultmap.BeanResultMapFactory;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.type.StringTypeHandler;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ColumnResultMappingFactoryTest {

    @Test
    public void testResultMapping() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        beanConfiguration.getTypeHandlerRegistry().register(IdTypeHandler.class);
        IResultMappingFactory.ResultMappingContext resultMappingContext = new IResultMappingFactory.ResultMappingContext(-1, "");

        ColumnResultMappingFactory columnResultMappingFactory = new ColumnResultMappingFactory(beanConfiguration);

        ResultMapping rm2 = columnResultMappingFactory.buildResultMapping(resultMappingContext, CountryBean.class, "code");
        Assertions.assertThat(rm2).extracting(ResultMapping::getProperty, ResultMapping::getColumn, ResultMapping::getJavaType).containsOnly("code", "CODE", String.class);
        Assertions.assertThat(rm2.getTypeHandler()).isInstanceOf(StringTypeHandler.class);

        ResultMapping rm3 = columnResultMappingFactory.buildResultMapping(resultMappingContext, CountryBean.class, "id");
        Assertions.assertThat(rm3).extracting(ResultMapping::getProperty, ResultMapping::getColumn, ResultMapping::getJavaType).containsOnly("id", "ID", IId.class);
        Assertions.assertThat(rm3.getTypeHandler()).isInstanceOf(IdTypeHandler.class);

        ResultMapping rm6 = columnResultMappingFactory.buildResultMapping(resultMappingContext, Test1Bean.class, "trainId");
        Assertions.assertThat(rm6).extracting(ResultMapping::getProperty, ResultMapping::getColumn, ResultMapping::getJavaType).containsOnly("trainId", "TRAIN_ID", String.class);
        Assertions.assertThat(rm6.getTypeHandler()).isInstanceOf(StringTypeHandler.class);
    }

}
