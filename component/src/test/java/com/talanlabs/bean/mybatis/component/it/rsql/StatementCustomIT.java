package com.talanlabs.bean.mybatis.component.it.rsql;

import com.talanlabs.bean.mybatis.component.data.CountryFields;
import com.talanlabs.bean.mybatis.component.data.ICountry;
import com.talanlabs.bean.mybatis.component.data.IPerson;
import com.talanlabs.bean.mybatis.component.it.AbstractHSQLIntegration;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.rsql.configuration.RsqlConfigurationBuilder;
import com.talanlabs.bean.mybatis.rsql.database.HSQLDBHandler;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.policy.AlwaysUpperStringPolicy;
import com.talanlabs.bean.mybatis.rsql.statement.Request;
import com.talanlabs.bean.mybatis.rsql.statement.RsqlStatementNameHelper;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class StatementCustomIT extends AbstractHSQLIntegration {

    @BeforeClass
    public static void init() {
        getBeanConfiguration().setRsqlConfiguration(
                RsqlConfigurationBuilder.newBuilder(getBeanConfiguration()).stringPolicy(new AlwaysUpperStringPolicy()).pageStatementFactory(new HSQLDBHandler(getBeanConfiguration())).build());
    }

    @Test
    public void testCustomFindCode1() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().customRequest(this::buildCustomSqlResult1).build());
        Assertions.assertThat(countries).isNotNull().hasSize(1).extracting(CountryFields.code).containsExactly("FRA");
    }

    private SqlResult buildCustomSqlResult1(SqlContext context) {
        return SqlResult.newBuilder().appendWhere("CODE = 'FRA'").build();
    }

    @Test
    public void testCustomFindCode2() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        List<ICountry> countries = getSqlSessionManager()
                .selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().rsql("name==Fromage").customRequest(this::buildCustomSqlResult2).build());
        Assertions.assertThat(countries).isNotNull().hasSize(1).extracting(CountryFields.code).containsExactly("FRA");
    }

    private SqlResult buildCustomSqlResult2(SqlContext context) {
        String param1 = context.getNewParamName();
        return SqlResult.newBuilder().appendWhere("CODE = #{" + param1 + "}").appendParameter(param1, "FRA").build();
    }

    @Test
    public void testCustom3() {
        List<IPerson> persons = getSqlSessionManager()
                .selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), Request.newBuilder().rsql("lastName==All*").customRequest(this::buildCustomSqlResult3).build());
        Assertions.assertThat(persons).isNotNull().hasSize(3);
    }

    private SqlResult buildCustomSqlResult3(SqlContext context) {
        String param1 = context.getNewParamName();
        String param2 = context.getNewParamName();
        return SqlResult.newBuilder().appendJoin(SqlResult.Join.Type.Inner, "T_ADDRESS a on a.id = t.address_id")
                .appendWhere("a.CITY = #{" + param1 + "}" + " OR exists (select * from t_address b where b.id = t.address_id and b.city = #{" + param2 + "})").appendParameter(param1, "Versailles")
                .appendParameter(param2, "Valence").build();
    }
}
