package com.talanlabs.bean.mybatis.component.it;

import com.talanlabs.bean.mybatis.component.data.IUser;
import com.talanlabs.bean.mybatis.component.data.UserFields;
import com.talanlabs.bean.mybatis.factory.IdFactory;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DeleteComponentsByIT extends AbstractHSQLIntegration {

    @Test
    public void testDelete() {
        IUser user = getBeanSqlSessionManager().findById(IUser.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(user).isNotNull();

        int i = getSqlSessionManager().delete(StatementNameHelper.buildDeleteBeansByKey(IUser.class, UserFields.login), user.getLogin());
        Assertions.assertThat(i).isEqualTo(1);

        user = getBeanSqlSessionManager().findById(IUser.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(user).isNull();
    }
}
