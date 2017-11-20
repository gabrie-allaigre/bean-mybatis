package com.talanlabs.bean.mybatis.component.it.rsql.where;

import com.talanlabs.bean.mybatis.component.data.ICountry;
import com.talanlabs.bean.mybatis.component.data.IPerson;
import com.talanlabs.bean.mybatis.component.it.config.DefaultNlsColumnHandler;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.rsql.configuration.RsqlConfigurationBuilder;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.policy.AlwaysUpperStringPolicy;
import com.talanlabs.bean.mybatis.rsql.engine.where.BeanRsqlVisitor;
import com.talanlabs.bean.mybatis.rsql.engine.where.registry.DefaultComparisonOperatorManagerRegistry;
import com.talanlabs.bean.mybatis.rsql.engine.where.registry.IComparisonOperatorManagerRegistry;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import cz.jirutka.rsql.parser.RSQLParser;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ComparisonOperatorUpperRsqlIT {

    private static BeanRsqlVisitor<IPerson> personBeanRsqlVisitor;
    private static BeanRsqlVisitor<ICountry> countryBeanRsqlVisitor;
    private static RSQLParser rsqlParser;

    private SqlContext sqlContext;

    @BeforeClass
    public static void initGlobal() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        beanConfiguration.setNlsColumnHandler(new DefaultNlsColumnHandler(beanConfiguration));
        beanConfiguration.setRsqlConfiguration(RsqlConfigurationBuilder.newBuilder(beanConfiguration).stringPolicy(new AlwaysUpperStringPolicy()).build());
        IComparisonOperatorManagerRegistry comparisonOperatorManagerRegistry = new DefaultComparisonOperatorManagerRegistry(beanConfiguration);
        personBeanRsqlVisitor = new BeanRsqlVisitor<>(IPerson.class, comparisonOperatorManagerRegistry);
        countryBeanRsqlVisitor = new BeanRsqlVisitor<>(ICountry.class, comparisonOperatorManagerRegistry);

        rsqlParser = new RSQLParser();
    }

    @Before
    public void init() {
        sqlContext = SqlContext.newBulder().defaultTablePrefix("t").build();
    }

    @Test
    public void testUpperStringRsql() {
        SqlResult res = rsqlParser.parse("firstName==fra").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("UPPER(t.FIRST_NAME) = #{0,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "FRA");
    }

    @Test
    public void testUpperStringRsql1() {
        SqlResult res = rsqlParser.parse("name==Fra").accept(countryBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).extracting("sql", String.class).containsExactly(
                "T_NLS j0 ON j0.TABLE_NAME = #{0,javaType=java.lang.String} AND j0.COLUMN_NAME = #{1,javaType=java.lang.String} AND j0.LANGUAGE_CODE = #{2,javaType=java.lang.String} AND j0.TABLE_ID = t.ID");
        Assertions.assertThat(res.joins).extracting("type", SqlResult.Join.Type.class).containsExactly(SqlResult.Join.Type.LeftOuter);
        Assertions.assertThat(res.wheres).containsOnly("UPPER(NVL(j0.MEANING, t.NAME)) = #{3,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "T_COUNTRY").containsEntry("1", "NAME").containsEntry("2", "fra").containsEntry("3", "FRA");
    }
}
