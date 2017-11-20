package com.talanlabs.bean.mybatis.component.it.rsql.where;

import com.talanlabs.bean.mybatis.component.data.ICountry;
import com.talanlabs.bean.mybatis.component.data.IPerson;
import com.talanlabs.bean.mybatis.component.it.config.DefaultNlsColumnHandler;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.rsql.configuration.RsqlConfigurationBuilder;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.where.BeanRsqlVisitor;
import com.talanlabs.bean.mybatis.rsql.engine.where.registry.DefaultComparisonOperatorManagerRegistry;
import com.talanlabs.bean.mybatis.rsql.engine.where.registry.IComparisonOperatorManagerRegistry;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import cz.jirutka.rsql.parser.RSQLParser;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ComparisonOperatorNlsRsqlIT {

    private static BeanRsqlVisitor<IPerson> personBeanRsqlVisitor;
    private static BeanRsqlVisitor<ICountry> countryBeanRsqlVisitor;
    private static RSQLParser rsqlParser;

    private SqlContext sqlContext;

    @BeforeClass
    public static void initGlobal() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        beanConfiguration.setNlsColumnHandler(new DefaultNlsColumnHandler(beanConfiguration));
        beanConfiguration.setRsqlConfiguration(
                RsqlConfigurationBuilder.newBuilder(beanConfiguration).build());
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
    public void testNlsStringRsql1() {
        SqlResult res = rsqlParser.parse("name==FRA").accept(countryBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).extracting("sql", String.class).containsExactly(
                "T_NLS j0 ON j0.TABLE_NAME = #{0,javaType=java.lang.String} AND j0.COLUMN_NAME = #{1,javaType=java.lang.String} AND j0.LANGUAGE_CODE = #{2,javaType=java.lang.String} AND j0.TABLE_ID = t.ID");
        Assertions.assertThat(res.joins).extracting("type", SqlResult.Join.Type.class).containsExactly(SqlResult.Join.Type.LeftOuter);
        Assertions.assertThat(res.wheres).containsOnly("NVL(j0.MEANING, t.NAME) = #{3,javaType=java.lang.String}");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "T_COUNTRY").containsEntry("1", "NAME").containsEntry("2", "fra").containsEntry("3", "FRA");
    }

    @Test
    public void testNlsStringRsql2() {
        SqlResult res = rsqlParser.parse("name==FRA,name==USA*").accept(countryBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).extracting("sql", String.class).containsExactly(
                "T_NLS j0 ON j0.TABLE_NAME = #{0,javaType=java.lang.String} AND j0.COLUMN_NAME = #{1,javaType=java.lang.String} AND j0.LANGUAGE_CODE = #{2,javaType=java.lang.String} AND j0.TABLE_ID = t.ID");
        Assertions.assertThat(res.joins).extracting("type", SqlResult.Join.Type.class).containsExactly(SqlResult.Join.Type.LeftOuter);
        Assertions.assertThat(res.wheres).containsOnly("(NVL(j0.MEANING, t.NAME) = #{3,javaType=java.lang.String} OR NVL(j0.MEANING, t.NAME) like #{4,javaType=java.lang.String} || '%' ESCAPE '\\')");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "T_COUNTRY").containsEntry("1", "NAME").containsEntry("2", "fra").containsEntry("3", "FRA").containsEntry("4", "USA");
    }

    @Test
    public void testNlsStringRsql3() {
        SqlResult res = rsqlParser.parse("address.country.name==FRA,address2.country.name==USA*").accept(personBeanRsqlVisitor, sqlContext);

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.joins).extracting("sql", String.class).containsExactly("T_ADDRESS j0 ON j0.ID = t.ADDRESS_ID", "T_COUNTRY j1 ON j1.ID = j0.COUNTRY_ID",
                "T_NLS j2 ON j2.TABLE_NAME = #{0,javaType=java.lang.String} AND j2.COLUMN_NAME = #{1,javaType=java.lang.String} AND j2.LANGUAGE_CODE = #{2,javaType=java.lang.String} AND j2.TABLE_ID = j1.ID",
                "T_ASSO_PERSON_ADDRESS j3_0 ON j3_0.PERSON_ID = t.ID", "T_ADDRESS j3 ON j3.ID = j3_0.ADDRESS_ID", "T_COUNTRY j4 ON j4.ID = j3.COUNTRY_ID",
                "T_NLS j5 ON j5.TABLE_NAME = #{4,javaType=java.lang.String} AND j5.COLUMN_NAME = #{5,javaType=java.lang.String} AND j5.LANGUAGE_CODE = #{6,javaType=java.lang.String} AND j5.TABLE_ID = j4.ID");
        Assertions.assertThat(res.joins).extracting("type", SqlResult.Join.Type.class)
                .containsExactly(SqlResult.Join.Type.Inner, SqlResult.Join.Type.Inner, SqlResult.Join.Type.LeftOuter, SqlResult.Join.Type.Inner, SqlResult.Join.Type.Inner, SqlResult.Join.Type.Inner,
                        SqlResult.Join.Type.LeftOuter);
        Assertions.assertThat(res.wheres).containsOnly("(NVL(j2.MEANING, j1.NAME) = #{3,javaType=java.lang.String} OR NVL(j5.MEANING, j4.NAME) like #{7,javaType=java.lang.String} || '%' ESCAPE '\\')");
        Assertions.assertThat(res.parameterMap).containsEntry("0", "T_COUNTRY").containsEntry("1", "NAME").containsEntry("2", "fra").containsEntry("3", "FRA").containsEntry("4", "T_COUNTRY")
                .containsEntry("5", "NAME").containsEntry("6", "fra").containsEntry("7", "USA");
    }
}
