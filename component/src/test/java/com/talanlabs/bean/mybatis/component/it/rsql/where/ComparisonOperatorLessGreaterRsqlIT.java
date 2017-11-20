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

public class ComparisonOperatorLessGreaterRsqlIT {

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
    public void testSimpleAgeGtRsql() {
        SqlResult res = rsqlParser.parse("age=gt=12").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("AGE > #{0,javaType=int}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", 12);
    }

    @Test
    public void testSimpleAgeGeRsql() {
        SqlResult res = rsqlParser.parse("age=ge=12").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("AGE >= #{0,javaType=int}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", 12);
    }

    @Test
    public void testSimpleAgeLtRsql() {
        SqlResult res = rsqlParser.parse("age=lt=12").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("AGE < #{0,javaType=int}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", 12);
    }

    @Test
    public void testSimpleAgeLeRsql() {
        SqlResult res = rsqlParser.parse("age=le=12").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("AGE <= #{0,javaType=int}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", 12);
    }

    @Test
    public void testSimpleAgeLe2Rsql() {
        SqlResult res = rsqlParser.parse("age<=12").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("AGE <= #{0,javaType=int}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", 12);
    }
}
