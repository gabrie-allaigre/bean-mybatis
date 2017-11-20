package com.talanlabs.bean.mybatis.component.it;

import com.talanlabs.bean.mybatis.component.data.IUser;
import com.talanlabs.bean.mybatis.factory.IdFactory;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DeleteByIdIT extends AbstractHSQLIntegration {

    @Test
    public void testDeleteEntityById() {
        IUser user = getBeanSqlSessionManager().findById(IUser.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(user).isNotNull();

        int i = getSqlSessionManager().delete(StatementNameHelper.buildDeleteEntityByIdKey(IUser.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(i).isEqualTo(1);

        user = getBeanSqlSessionManager().findById(IUser.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(user).isNull();
    }
}
