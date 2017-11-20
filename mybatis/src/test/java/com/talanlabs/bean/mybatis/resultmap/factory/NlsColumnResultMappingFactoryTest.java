package com.talanlabs.bean.mybatis.resultmap.factory;

import com.talanlabs.bean.mybatis.data.CountryBean;
import com.talanlabs.bean.mybatis.handler.IdTypeHandler;
import com.talanlabs.bean.mybatis.resultmap.BeanResultMapFactory;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.handler.INlsColumnHandler;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.type.StringTypeHandler;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class NlsColumnResultMappingFactoryTest {

    @Test
    public void testResultMapping() {
        INlsColumnHandler nlsColumnHandler = Mockito.mock(INlsColumnHandler.class);
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        beanConfiguration.setNlsColumnHandler(nlsColumnHandler);
        beanConfiguration.getTypeHandlerRegistry().register(IdTypeHandler.class);

        IResultMappingFactory.ResultMappingContext resultMappingContext = new IResultMappingFactory.ResultMappingContext(-1, "");

        NlsColumnResultMappingFactory nlsColumnResultMappingFactory = new NlsColumnResultMappingFactory(beanConfiguration);

        ResultMapping rm2 = nlsColumnResultMappingFactory.buildResultMapping(resultMappingContext, CountryBean.class, "name");
        Assertions.assertThat(rm2).extracting(ResultMapping::getProperty, ResultMapping::getColumn, ResultMapping::getJavaType).containsOnly("name", null, String.class);
        Assertions.assertThat(rm2.getTypeHandler()).isInstanceOf(StringTypeHandler.class);
        Assertions.assertThat(rm2.getNestedQueryId()).isNotBlank();
    }

}
