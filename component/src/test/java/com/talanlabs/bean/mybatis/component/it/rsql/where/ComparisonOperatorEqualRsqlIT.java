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

public class ComparisonOperatorEqualRsqlIT {

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
    public void testSimpleStringRsql() {
        SqlResult res = rsqlParser.parse("firstName==FRA").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("FIRST_NAME = #{0,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "FRA");
    }

    @Test
    public void testStartStringRsql() {
        SqlResult res = rsqlParser.parse("firstName==FRA*").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("FIRST_NAME like #{0,javaType=java.lang.String} || '%' ESCAPE '\\'");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "FRA");
    }

    @Test
    public void testEndStringRsql() {
        SqlResult res = rsqlParser.parse("firstName==*FRA").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("FIRST_NAME like '%' || #{0,javaType=java.lang.String} ESCAPE '\\'");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "FRA");
    }

    @Test
    public void testContains1StringRsql() {
        SqlResult res = rsqlParser.parse("firstName==*FRA*").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("FIRST_NAME like '%' || #{0,javaType=java.lang.String} || '%' ESCAPE '\\'");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "FRA");
    }

    @Test
    public void testContains2StringRsql() {
        SqlResult res = rsqlParser.parse("firstName==*F*A*").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("FIRST_NAME like '%' || #{0,javaType=java.lang.String} || '%' || #{1,javaType=java.lang.String} || '%' ESCAPE '\\'");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "F").containsEntry("1", "A");
    }

    @Test
    public void testThreeStringRsql() {
        SqlResult res = rsqlParser.parse("(firstName==FRA,firstName==ENG);lastName==GAB").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsExactly("(FIRST_NAME = #{0,javaType=java.lang.String} OR FIRST_NAME = #{1,javaType=java.lang.String})","LAST_NAME = #{2,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "FRA").containsEntry("1", "ENG").containsEntry("2", "GAB");
    }

    @Test
    public void testSimpleIntegerRsql() {
        SqlResult res = rsqlParser.parse("age==10").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("AGE = #{0,javaType=int}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", 10);
    }

    @Test
    public void testSimpleFloatRsql() {
        SqlResult res = rsqlParser.parse("height==180.5").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("HEIGHT = #{0,javaType=float}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", 180.5f);
    }

    @Test
    public void testSimpleDoubleRsql() {
        SqlResult res = rsqlParser.parse("weight==75.25").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("WEIGHT = #{0,javaType=double}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", 75.25);
    }

    @Test
    public void testContainsDoubleRsql() {
        SqlResult res = rsqlParser.parse("weight==7*").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("WEIGHT like #{0,javaType=java.lang.String} || '%' ESCAPE '\\'");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "7");
    }

    @Test
    public void testSimpleEnumRsql() {
        SqlResult res = rsqlParser.parse("sexe==WOMAN").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("SEXE = #{0,javaType=com.talanlabs.bean.mybatis.component.data.IPerson$Sexe}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", IPerson.Sexe.WOMAN);
    }

    @Test
    public void testContainsEnumRsql() {
        SqlResult res = rsqlParser.parse("sexe==*MAN").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).isEmpty();
        Assertions.assertThat(res.wheres).containsOnly("SEXE like '%' || #{0,javaType=java.lang.String} ESCAPE '\\'");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "MAN");
    }
}
