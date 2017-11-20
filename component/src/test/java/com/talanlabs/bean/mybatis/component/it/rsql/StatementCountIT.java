package com.talanlabs.bean.mybatis.component.it.rsql;

import com.talanlabs.bean.mybatis.component.data.ICountry;
import com.talanlabs.bean.mybatis.component.it.AbstractHSQLIntegration;
import com.talanlabs.bean.mybatis.rsql.statement.RsqlStatementNameHelper;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class StatementCountIT extends AbstractHSQLIntegration {

    @Test
    public void testSimpleFindCode() {
        Integer count = getSqlSessionManager().selectOne(RsqlStatementNameHelper.buildCountRsqlKey(ICountry.class), "code==FRA");
        Assertions.assertThat(count).isEqualTo(1);
    }

    @Test
    public void testSimpleNotFindCode() {
        Integer count = getSqlSessionManager().selectOne(RsqlStatementNameHelper.buildCountRsqlKey(ICountry.class), "code==TOTO");
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    public void testSimple() {
        Integer count = getSqlSessionManager().selectOne(RsqlStatementNameHelper.buildCountRsqlKey(ICountry.class));
        Assertions.assertThat(count).isEqualTo(7);
    }
}
