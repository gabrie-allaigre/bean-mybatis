package com.talanlabs.bean.mybatis.component.it.rsql.where;

import com.talanlabs.bean.mybatis.component.data.ICountry;
import com.talanlabs.bean.mybatis.component.it.config.DefaultNlsColumnHandler;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.rsql.configuration.RsqlConfigurationBuilder;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.policy.AlwaysUpperStringPolicy;
import com.talanlabs.bean.mybatis.rsql.engine.where.BeanRsqlVisitor;
import com.talanlabs.bean.mybatis.rsql.engine.where.registry.DefaultComparisonOperatorManagerRegistry;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import cz.jirutka.rsql.parser.RSQLParser;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StringPolicyRsqlIT {

    private static BeanRsqlVisitor<ICountry> nothingCountryBeanRsqlVisitor;
    private static BeanRsqlVisitor<ICountry> upperCountryBeanRsqlVisitor;
    private static BeanRsqlVisitor<ICountry> specialCountryBeanRsqlVisitor;
    private static RSQLParser rsqlParser;

    private SqlContext sqlContext;

    @BeforeClass
    public static void initGlobal() {
        BeanConfiguration beanConfiguration1 = new BeanConfiguration();
        beanConfiguration1.setNlsColumnHandler(new DefaultNlsColumnHandler(beanConfiguration1));
        beanConfiguration1.setRsqlConfiguration(RsqlConfigurationBuilder.newBuilder(beanConfiguration1).build());
        nothingCountryBeanRsqlVisitor = new BeanRsqlVisitor<>(ICountry.class, new DefaultComparisonOperatorManagerRegistry(beanConfiguration1));

        BeanConfiguration beanConfiguration2 = new BeanConfiguration();
        beanConfiguration2.setNlsColumnHandler(new DefaultNlsColumnHandler(beanConfiguration2));
        beanConfiguration2.setRsqlConfiguration(RsqlConfigurationBuilder.newBuilder(beanConfiguration2).stringPolicy(new AlwaysUpperStringPolicy()).build());
        upperCountryBeanRsqlVisitor = new BeanRsqlVisitor<>(ICountry.class, new DefaultComparisonOperatorManagerRegistry(beanConfiguration2));

        BeanConfiguration beanConfiguration3 = new BeanConfiguration();
        beanConfiguration3.setNlsColumnHandler(new DefaultNlsColumnHandler(beanConfiguration3));
        beanConfiguration3.setRsqlConfiguration(RsqlConfigurationBuilder.newBuilder(beanConfiguration3).build());
        specialCountryBeanRsqlVisitor = new BeanRsqlVisitor<>(ICountry.class, new DefaultComparisonOperatorManagerRegistry(beanConfiguration3));

        rsqlParser = new RSQLParser();
    }

    @Before
    public void init() {
        sqlContext = SqlContext.newBulder().defaultTablePrefix("t").build();
    }

    @Test
    public void testNothingStringRsql() {
        SqlResult res = rsqlParser.parse("code!=FrA").accept(nothingCountryBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.wheres).containsOnly("t.CODE <> #{0,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "FrA");
    }

    @Test
    public void testNothingNlsStringRsql() {
        SqlResult res = rsqlParser.parse("name==FrA").accept(nothingCountryBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.wheres).containsOnly("NVL(j0.MEANING, t.NAME) = #{3,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("3", "FrA");
    }

    @Test
    public void testUpperStringRsql() {
        SqlResult res = rsqlParser.parse("code!=fra").accept(upperCountryBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.wheres).containsOnly("UPPER(t.CODE) <> #{0,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "FRA");
    }

    @Test
    public void testUpperNlsStringRsql() {
        SqlResult res = rsqlParser.parse("name==fra").accept(upperCountryBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.wheres).containsOnly("UPPER(NVL(j0.MEANING, t.NAME)) = #{3,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("3", "FRA");
    }

    @Test
    public void testSpecialCharacterRsql() {
        SqlResult res = rsqlParser.parse("name==fra").accept(upperCountryBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.wheres).containsOnly("UPPER(NVL(j0.MEANING, t.NAME)) = #{3,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("3", "FRA");
    }
}
