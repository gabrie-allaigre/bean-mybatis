package com.talanlabs.bean.mybatis.component.it.rsql.where;

import com.talanlabs.bean.mybatis.component.data.IPerson;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.where.BeanRsqlVisitor;
import com.talanlabs.bean.mybatis.rsql.engine.where.registry.DefaultComparisonOperatorManagerRegistry;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import cz.jirutka.rsql.parser.RSQLParser;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ComparisonOperatorInOutRsqlIT {

    private static BeanRsqlVisitor<IPerson> personBeanRsqlVisitor;
    private static RSQLParser rsqlParser;

    private SqlContext sqlContext;

    @BeforeClass
    public static void initGlobal() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        personBeanRsqlVisitor = new BeanRsqlVisitor<>(IPerson.class, new DefaultComparisonOperatorManagerRegistry(beanConfiguration));

        rsqlParser = new RSQLParser();
    }

    @Before
    public void init() {
        sqlContext = SqlContext.newBulder().defaultTablePrefix("").build();
    }

    @Test
    public void testSimpleInRsql() {
        SqlResult res = rsqlParser.parse("firstName=in=(FRA,ITA,ESP)").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("FIRST_NAME IN (#{0,javaType=java.lang.String}, #{1,javaType=java.lang.String}, #{2,javaType=java.lang.String})");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "FRA").containsEntry("1", "ITA").containsEntry("2", "ESP");
    }

    @Test
    public void testSimpleOutRsql() {
        SqlResult res = rsqlParser.parse("firstName=out=(FRA,ITA,ESP)").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("FIRST_NAME NOT IN (#{0,javaType=java.lang.String}, #{1,javaType=java.lang.String}, #{2,javaType=java.lang.String})");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "FRA").containsEntry("1", "ITA").containsEntry("2", "ESP");
    }
}
