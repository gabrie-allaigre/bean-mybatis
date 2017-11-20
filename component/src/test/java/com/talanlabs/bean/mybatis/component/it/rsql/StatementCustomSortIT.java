package com.talanlabs.bean.mybatis.component.it.rsql;

import com.talanlabs.bean.mybatis.component.data.IPerson;
import com.talanlabs.bean.mybatis.component.data.PersonFields;
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

public class StatementCustomSortIT extends AbstractHSQLIntegration {

    @BeforeClass
    public static void init() {
        getBeanConfiguration().setRsqlConfiguration(
                RsqlConfigurationBuilder.newBuilder(getBeanConfiguration()).stringPolicy(new AlwaysUpperStringPolicy()).pageStatementFactory(new HSQLDBHandler(getBeanConfiguration())).build());
    }

    @Test
    public void testSortCode() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class),
                Request.newBuilder().customSortLeft(this::buildCustomSortSqlResultLeft1).sort("lastName").customSortRight(this::buildCustomSortSqlResultRight1).build());
        Assertions.assertThat(persons).isNotNull().hasSize(5).extracting(PersonFields.firstName).containsSequence("David", "Gabriel", "Laureline", "Raphael", "Sandra");
    }

    private SqlResult buildCustomSortSqlResultLeft1(SqlContext context) {
        return SqlResult.newBuilder().appendOrderBy("FIRST_NAME").build();
    }

    private SqlResult buildCustomSortSqlResultRight1(SqlContext context) {
        return SqlResult.newBuilder().appendOrderBy("AGE").build();
    }
}
