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

public class ComparisonOperatorJoinsRsqlIT {

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
        sqlContext = SqlContext.newBulder().defaultTablePrefix("t").build();
    }

    @Test
    public void testJoinStringRsql1() {
        SqlResult res = rsqlParser.parse("address.city==FRA").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).extracting("sql", String.class).containsExactly("T_ADDRESS j0 ON j0.ID = t.ADDRESS_ID");
        Assertions.assertThat(res.joins).extracting("type", SqlResult.Join.Type.class).containsExactly(SqlResult.Join.Type.Inner);
        Assertions.assertThat(res.wheres).containsOnly("j0.CITY = #{0,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "FRA");
    }

    @Test
    public void testJoinStringRsql2() {
        SqlResult res = rsqlParser.parse("firstName==Gab;address.city==V*;address.postalZip==78*").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).extracting("sql", String.class).containsExactly("T_ADDRESS j0 ON j0.ID = t.ADDRESS_ID");
        Assertions.assertThat(res.joins).extracting("type", SqlResult.Join.Type.class).containsExactly(SqlResult.Join.Type.Inner);
        Assertions.assertThat(res.wheres).containsExactly(
                "t.FIRST_NAME = #{0,javaType=java.lang.String}", "j0.CITY like #{1,javaType=java.lang.String} || '%' ESCAPE '\\'", "j0.POSTAL_ZIP like #{2,javaType=java.lang.String} || '%' ESCAPE '\\'");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "Gab").containsEntry("1", "V").containsEntry("2", "78");
    }

    @Test
    public void testJoinStringRsql3() {
        SqlResult res = rsqlParser.parse("address.country.code==France").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).extracting("sql", String.class).containsExactly("T_ADDRESS j0 ON j0.ID = t.ADDRESS_ID", "T_COUNTRY j1 ON j1.ID = j0.COUNTRY_ID");
        Assertions.assertThat(res.joins).extracting("type", SqlResult.Join.Type.class).containsExactly(SqlResult.Join.Type.Inner, SqlResult.Join.Type.Inner);
        Assertions.assertThat(res.wheres).containsOnly("j1.CODE = #{0,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "France");
    }

    @Test
    public void testJoinStringRsql4() {
        SqlResult res = rsqlParser.parse("address.country.code==France,addressBis.country.code==Italie").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).extracting("sql", String.class)
                .containsExactly("T_ADDRESS j0 ON j0.ID = t.ADDRESS_ID", "T_COUNTRY j1 ON j1.ID = j0.COUNTRY_ID", "T_ADDRESS j2 ON j2.ID = t.ADDRESS_BIS_ID", "T_COUNTRY j3 ON j3.ID = j2.COUNTRY_ID");
        Assertions.assertThat(res.joins).extracting("type", SqlResult.Join.Type.class)
                .containsExactly(SqlResult.Join.Type.Inner, SqlResult.Join.Type.Inner, SqlResult.Join.Type.Inner, SqlResult.Join.Type.Inner);
        Assertions.assertThat(res.wheres).containsOnly("(j1.CODE = #{0,javaType=java.lang.String} OR j3.CODE = #{1,javaType=java.lang.String})");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "France").containsEntry("1", "Italie");
    }

    @Test
    public void testJoinStringRsql5() {
        SqlResult res = rsqlParser.parse("address2.city==Valence").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).extracting("sql", String.class).containsExactly("T_ASSO_PERSON_ADDRESS j0_0 ON j0_0.PERSON_ID = t.ID", "T_ADDRESS j0 ON j0.ID = j0_0.ADDRESS_ID");
        Assertions.assertThat(res.joins).extracting("type", SqlResult.Join.Type.class).containsExactly(SqlResult.Join.Type.Inner, SqlResult.Join.Type.Inner);
        Assertions.assertThat(res.wheres).containsOnly("j0.CITY = #{0,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "Valence");
    }

    @Test
    public void testJoinStringRsql6() {
        SqlResult res = rsqlParser.parse("address3.city==Valence").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).extracting("sql", String.class)
                .containsExactly("T_ASSO_PERSON_INT j0_0 ON j0_0.PERSON_ID = t.ID", "T_ASSO_INT_ADDRESS j0_1 ON j0_1.INT_ID = j0_0.INT_ID", "T_ADDRESS j0 ON j0.CITY = j0_1.ADDRESS_ID");
        Assertions.assertThat(res.joins).extracting("type", SqlResult.Join.Type.class).containsExactly(SqlResult.Join.Type.Inner, SqlResult.Join.Type.Inner, SqlResult.Join.Type.Inner);
        Assertions.assertThat(res.wheres).containsOnly("j0.CITY = #{0,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "Valence");
    }

    @Test
    public void testJoinStringRsql7() {
        SqlResult res = rsqlParser.parse("address3.city=in=(Valence,Versailles)").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).extracting("sql", String.class)
                .containsExactly("T_ASSO_PERSON_INT j0_0 ON j0_0.PERSON_ID = t.ID", "T_ASSO_INT_ADDRESS j0_1 ON j0_1.INT_ID = j0_0.INT_ID", "T_ADDRESS j0 ON j0.CITY = j0_1.ADDRESS_ID");
        Assertions.assertThat(res.joins).extracting("type", SqlResult.Join.Type.class).containsExactly(SqlResult.Join.Type.Inner, SqlResult.Join.Type.Inner, SqlResult.Join.Type.Inner);
        Assertions.assertThat(res.wheres).containsOnly("j0.CITY IN (#{0,javaType=java.lang.String}, #{1,javaType=java.lang.String})");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "Valence").containsEntry("1", "Versailles");
    }
}
