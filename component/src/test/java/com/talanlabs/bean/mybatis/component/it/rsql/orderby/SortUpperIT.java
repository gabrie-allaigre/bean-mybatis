package com.talanlabs.bean.mybatis.component.it.rsql.orderby;

import com.talanlabs.bean.mybatis.component.data.IPerson;
import com.talanlabs.bean.mybatis.component.it.config.DefaultNlsColumnHandler;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.rsql.configuration.RsqlConfigurationBuilder;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.BeanSortVisitor;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.registry.DefaultSortDirectionManagerRegistry;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.registry.ISortDirectionManagerRegistry;
import com.talanlabs.bean.mybatis.rsql.engine.policy.AlwaysUpperStringPolicy;
import com.talanlabs.bean.mybatis.rsql.sort.SortParser;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SortUpperIT {

    private static BeanSortVisitor<IPerson> personBeanSortVisitor;
    private static SortParser sortParser;

    private SqlContext sqlContext;

    @BeforeClass
    public static void initGlobal() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        beanConfiguration.setNlsColumnHandler(new DefaultNlsColumnHandler(beanConfiguration));
        beanConfiguration.setRsqlConfiguration(RsqlConfigurationBuilder.newBuilder(beanConfiguration).stringPolicy(new AlwaysUpperStringPolicy()).build());
        ISortDirectionManagerRegistry sortDirectionManagerRegistry = new DefaultSortDirectionManagerRegistry(beanConfiguration);
        personBeanSortVisitor = new BeanSortVisitor<>(IPerson.class, sortDirectionManagerRegistry);

        sortParser = new SortParser();
    }

    @Before
    public void init() {
        sqlContext = SqlContext.newBulder().defaultTablePrefix("t").build();
    }

    @Test
    public void testUpperStringSort() {
        SqlResult res = personBeanSortVisitor.visit(sortParser.parse("firstName"), sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.orderBies).containsOnly("UPPER(t.FIRST_NAME) ASC");
        Assertions.assertThat(res.parameterMap).isEmpty();
    }

    @Test
    public void testUpperSort() {
        SqlResult res = personBeanSortVisitor.visit(sortParser.parse("-address.country.name"), sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).extracting("sql", String.class).containsExactly("T_ADDRESS j0 ON j0.ID = t.ADDRESS_ID", "T_COUNTRY j1 ON j1.ID = j0.COUNTRY_ID",
                "T_NLS j2 ON j2.TABLE_NAME = #{0,javaType=java.lang.String} AND j2.COLUMN_NAME = #{1,javaType=java.lang.String} AND j2.LANGUAGE_CODE = #{2,javaType=java.lang.String} AND j2.TABLE_ID = j1.ID");
        Assertions.assertThat(res.joins).extracting("type", SqlResult.Join.Type.class).containsExactly(SqlResult.Join.Type.LeftOuter, SqlResult.Join.Type.LeftOuter, SqlResult.Join.Type.LeftOuter);
        Assertions.assertThat(res.orderBies).containsOnly("UPPER(NVL(j2.MEANING, j1.NAME)) DESC");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "T_COUNTRY").containsEntry("1", "NAME").containsEntry("2", "fra");
    }
}
