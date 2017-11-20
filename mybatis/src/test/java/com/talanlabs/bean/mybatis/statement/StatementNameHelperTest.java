package com.talanlabs.bean.mybatis.statement;

import com.talanlabs.bean.mybatis.annotation.Annotations;
import com.talanlabs.bean.mybatis.annotation.JoinTable;
import com.talanlabs.bean.mybatis.annotation.OrderBy;
import com.talanlabs.bean.mybatis.data.GroupBean;
import com.talanlabs.bean.mybatis.data.WagonBean;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.groups.Tuple;
import org.junit.Test;

public class StatementNameHelperTest {

    @Test
    public void testBuildFindEntityByIdKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.buildFindEntityByIdKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildFindEntityByIdKey(WagonBean.class)).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/findEntityById");
        softAssertions.assertThat(StatementNameHelper.buildFindEntityByIdKey(WagonBean.WheelBean.class)).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/findEntityById");
        softAssertions.assertAll();
    }

    @Test
    public void testIsFindEntityByIdKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.isFindEntityByIdKey(null)).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindEntityByIdKey("com.model.WagonBean/findEntityById")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindEntityByIdKey("com.model.WagonBean1/findEntityById")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindEntityByIdKey("model.WagonBean/findEntityById")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindEntityByIdKey("com.talanlabs.bean.mybatis.data.WagonBean/findEntityById")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindEntityByIdKey("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/findEntityById")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindEntityByIdKey("model.WagonBean")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindEntityByIdKey("/findEntityById")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindEntityByIdKey("model-WagonBean/findEntityById")).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInFindEntityByIdKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindEntityByIdKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindEntityByIdKey("com.talanlabs.bean.mybatis.data.WagonBean/findEntityById")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindEntityByIdKey("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/findEntityById")).isEqualTo(WagonBean.WheelBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindEntityByIdKey("model.WagonBean/findEntityById")).isNull();
        softAssertions.assertAll();
    }

    @Test
    public void testBuildFindBeansByKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByKey(null, false, null, null)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByKey(WagonBean.class, false, null, null)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByKey(WagonBean.class, true, null, null)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByKey(WagonBean.class, false, new String[] { "login" }, null))
                .isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login");
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByKey(WagonBean.class, true, new String[] { "login" }, null))
                .isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login&ignoreCancel");
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByKey(WagonBean.class, false, new String[] { "login", "password" }, null))
                .isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login,password");
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByKey(WagonBean.class, true, new String[] { "login", "password" }, null))
                .isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login,password&ignoreCancel");
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByKey(WagonBean.class, false, new String[] { "login" }, new OrderBy[] { Annotations.orderBy("rang", OrderBy.Sort.Asc) }))
                .isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login&orderBy=rang;Asc");
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByKey(WagonBean.class, false, new String[] { "login" },
                new OrderBy[] { Annotations.orderBy("rang", OrderBy.Sort.Asc), Annotations.orderBy("cancel", OrderBy.Sort.Desc) }))
                .isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login&orderBy=rang;Asc#cancel;Desc");
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByKey(WagonBean.class, true, new String[] { "login", "password" },
                new OrderBy[] { Annotations.orderBy("rang", OrderBy.Sort.Asc), Annotations.orderBy("cancel", OrderBy.Sort.Desc) }))
                .isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login,password&orderBy=rang;Asc#cancel;Desc&ignoreCancel");
        softAssertions.assertAll();
    }

    @Test
    public void testIsFindBeansByKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey(null)).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("model.WagonBean/findBeansBy?properties=login")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("com.model.WagonBean/findBeansBy?properties=login")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("model.WagonBean/findBeansBy?properties=idUser")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("model.WagonBean/findBeansBy?properties=idUser,login")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("model.WagonBean/findBeansBy?properties=idUser;login")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("model.WagonBean/findBeansBy?properties=idUser,login&ignoreCancel")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("model.WagonBean/findBeansBy?properties=idUser,login&toto")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("model.WagonBean/findBeansBy?idUser")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("model.WagonBean/findBeansBy")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("model.WagonBean/findBeansBy?")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("model.WagonBean")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("/findBeansBy")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("model-WagonBean/findBeansBy")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login&orderBy=rang;Asc")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login&orderBy=rang;Asc#cancel;Desc")).isTrue();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInFindBeansByKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindBeansByKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindBeansByKey("model.WagonBean/findBeansBy?properties=login")).isNull();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login,password")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login,password&ignoreCancel"))
                .isEqualTo(WagonBean.class);
        softAssertions.assertAll();
    }

    @Test
    public void testExtractPropertyNamesInFindBeansByKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInFindBeansByKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInFindBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login")).containsOnly("login");
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInFindBeansByKey("model.WagonBean/findBeansBy?properties=login")).containsOnly("login");
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInFindBeansByKey("model.WagonBean/findBeansBy?properties=idUser")).containsOnly("idUser");
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInFindBeansByKey("com.model.WagonBean/findBeansBy?properties=idUser")).containsOnly("idUser");
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInFindBeansByKey("com.model.WagonBean/findBeansBy?properties=idUser,password")).containsOnly("idUser", "password");
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInFindBeansByKey("com.model.WagonBean/findBeansBy?properties=idUser,password&ignoreCancel"))
                .containsOnly("idUser", "password");
        softAssertions.assertAll();
    }

    @Test
    public void testIsIgnoreCancelInFindBeansByKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.isIgnoreCancelInFindBeansByKey(null)).isFalse();
        softAssertions.assertThat(StatementNameHelper.isIgnoreCancelInFindBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isIgnoreCancelInFindBeansByKey("model.WagonBean/findBeansBy?properties=login")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isIgnoreCancelInFindBeansByKey("model.WagonBean/findBeansBy?properties=idUser")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isIgnoreCancelInFindBeansByKey("model.WagonBean/findBeansBy?properties=idUser&ignoreCancel")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isIgnoreCancelInFindBeansByKey("com.model.WagonBean/findBeansBy?properties=idUser")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isIgnoreCancelInFindBeansByKey("com.model.WagonBean/findBeansBy?properties=idUser,password")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isIgnoreCancelInFindBeansByKey("com.model.WagonBean/findBeansBy?properties=idUser,password&ignoreCancel")).isTrue();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractOrderBysInFindBeansByKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractOrderBiesInFindBeansByKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractOrderBiesInFindBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login&orderBy=rang;Asc"))
                .extracting(OrderBy::value, OrderBy::sort).containsExactly(Tuple.tuple("rang", OrderBy.Sort.Asc));
        softAssertions.assertThat(StatementNameHelper.extractOrderBiesInFindBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login&orderBy=rang;Asc#cancel;Desc"))
                .extracting(OrderBy::value, OrderBy::sort).containsExactly(Tuple.tuple("rang", OrderBy.Sort.Asc), Tuple.tuple("cancel", OrderBy.Sort.Desc));
        softAssertions.assertThat(
                StatementNameHelper.extractOrderBiesInFindBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/findBeansBy?properties=login,password&orderBy=rang;Asc#cancel;Desc&ignoreCancel"))
                .extracting(OrderBy::value, OrderBy::sort).containsExactly(Tuple.tuple("rang", OrderBy.Sort.Asc), Tuple.tuple("cancel", OrderBy.Sort.Desc));
        softAssertions.assertAll();
    }

    @Test
    public void testBuildFindBeansByJoinTableKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByJoinTableKey(null, null, false, null, null, null, null)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByJoinTableKey(WagonBean.class, null, false, null, null, null, null)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByJoinTableKey(WagonBean.class, GroupBean.class, false,
                new JoinTable[] { Annotations.joinTable("t_asso_group_user", new String[] { "group_id" }, new String[] { "user_id" }) }, new String[] { "id" }, new String[] { "id" }, null)).isEqualTo(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=id&targetProperties=id&join=t_asso_group_user;group_id;user_id");
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByJoinTableKey(WagonBean.class, GroupBean.class, false,
                new JoinTable[] { Annotations.joinTable("t_asso_group_toto", new String[] { "group_id" }, new String[] { "toto_id" }),
                        Annotations.joinTable("t_asso_toto_user", new String[] { "toto_id" }, new String[] { "user_id" }) }, new String[] { "id" }, new String[] { "id" }, null)).isEqualTo(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=id&targetProperties=id&join=t_asso_group_toto;group_id;toto_id#t_asso_toto_user;toto_id;user_id");
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByJoinTableKey(WagonBean.class, GroupBean.class, false,
                new JoinTable[] { Annotations.joinTable("t_asso_group_toto", new String[] { "group_code", "group_version" }, new String[] { "toto_id" }),
                        Annotations.joinTable("t_asso_toto_user", new String[] { "toto_id" }, new String[] { "user_code" }) }, new String[] { "code", "version" }, new String[] { "code" }, null))
                .isEqualTo(
                        "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=code,version&targetProperties=code&join=t_asso_group_toto;group_code,group_version;toto_id#t_asso_toto_user;toto_id;user_code");
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByJoinTableKey(WagonBean.class, GroupBean.class, false,
                new JoinTable[] { Annotations.joinTable("t_asso_group_user", new String[] { "group_id" }, new String[] { "user_id" }) }, new String[] { "id" }, new String[] { "id" },
                new OrderBy[] { Annotations.orderBy("rang", OrderBy.Sort.Asc) })).isEqualTo(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=id&targetProperties=id&join=t_asso_group_user;group_id;user_id&orderBy=rang;Asc");
        softAssertions.assertThat(StatementNameHelper.buildFindBeansByJoinTableKey(WagonBean.class, GroupBean.class, false,
                new JoinTable[] { Annotations.joinTable("t_asso_group_toto", new String[] { "group_id" }, new String[] { "toto_id" }),
                        Annotations.joinTable("t_asso_toto_user", new String[] { "toto_id" }, new String[] { "user_id" }) }, new String[] { "id" }, new String[] { "id" },
                new OrderBy[] { Annotations.orderBy("rang", OrderBy.Sort.Asc), Annotations.orderBy("cancel", OrderBy.Sort.Desc) })).isEqualTo(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=id&targetProperties=id&join=t_asso_group_toto;group_id;toto_id#t_asso_toto_user;toto_id;user_id&orderBy=rang;Asc#cancel;Desc");
        softAssertions.assertAll();
    }

    @Test
    public void testIsFindBeansByJoinTableKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByJoinTableKey(null)).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByJoinTableKey(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=id&targetProperties=id&join=t_asso_group_user;group_id;user_id"))
                .isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByJoinTableKey(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=id&targetProperties=id&join=t_asso_group_toto;group_id;toto_id#t_asso_toto_user;toto_id;user_id"))
                .isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByJoinTableKey(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=code,version&targetProperties=code&join=t_asso_group_toto;group_code,group_version;toto_id#t_asso_toto_user;toto_id;user_code"))
                .isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByJoinTableKey(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=id&targetProperties=id&join=t_asso_group_user;group_id;user_id&orderBy=rang;Asc"))
                .isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindBeansByJoinTableKey(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=id&targetProperties=id&join=t_asso_group_toto;group_id;toto_id#t_asso_toto_user;toto_id;user_id&orderBy=rang;Asc#cancel;Desc"))
                .isTrue();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInFindBeansByJoinTableKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindBeansByJoinTableKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindBeansByJoinTableKey(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=id&targetProperties=id&join=t_asso_group_user;group_id;user_id"))
                .isEqualTo(GroupBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindBeansByJoinTableKey(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=code,version&targetProperties=code&join=t_asso_group_toto;group_code,group_version;toto_id#t_asso_toto_user;toto_id;user_code"))
                .isEqualTo(GroupBean.class);
        softAssertions.assertAll();
    }

    @Test
    public void testExtractSourceBeanNameInFindBeansByJoinTableKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractSourceBeanClassInFindBeansByJoinTableKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractSourceBeanClassInFindBeansByJoinTableKey(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=id&targetProperties=id&join=t_asso_group_user;group_id;user_id"))
                .isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractSourceBeanClassInFindBeansByJoinTableKey(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=com.talanlabs.bean.mybatis.data.WagonBean&sourceProperties=code,version&targetProperties=code&join=t_asso_group_toto;group_code,group_version;toto_id#t_asso_toto_user;toto_id;user_code"))
                .isEqualTo(WagonBean.class);
        softAssertions.assertAll();
    }

    @Test
    public void testExtractSourcePropertiesInFindBeansByJoinTableKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractSourcePropertiesInFindBeansByJoinTableKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractSourcePropertiesInFindBeansByJoinTableKey(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=WagonBean&sourceProperties=id&targetProperties=targetId&join=t_asso_group_user;group_id;user_id"))
                .containsOnly("id");
        softAssertions.assertThat(StatementNameHelper.extractSourcePropertiesInFindBeansByJoinTableKey(
                "com.talanlabs.bean.mybatis.data.GroupBean/findBeansByJoinTable?sourceBean=WagonBean&sourceProperties=code,version&targetProperties=code&join=t_asso_group_toto;group_code,group_version;toto_id#t_asso_toto_user;toto_id;user_code"))
                .containsOnly("code", "version");
        softAssertions.assertAll();
    }

    @Test
    public void testExtractTargetPropertiesInFindBeansByJoinTableKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractTargetPropertiesInFindBeansByJoinTableKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractTargetPropertiesInFindBeansByJoinTableKey(
                "GroupBean/findBeansByJoinTable?sourceBean=WagonBean&sourceProperties=id&targetProperties=targetId&join=t_asso_group_user;group_id;user_id")).containsOnly("targetId");
        softAssertions.assertThat(StatementNameHelper.extractTargetPropertiesInFindBeansByJoinTableKey(
                "GroupBean/findBeansByJoinTable?sourceBean=WagonBean&sourceProperties=code,version&targetProperties=code&join=t_asso_group_toto;group_code,group_version;toto_id#t_asso_toto_user;toto_id;user_code"))
                .containsOnly("code");
        softAssertions.assertAll();
    }

    @Test
    public void testExtractJoinInFindBeansByJoinTableKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractJoinInFindBeansByJoinTableKey(null)).isNull();
        JoinTable[] p1 = StatementNameHelper
                .extractJoinInFindBeansByJoinTableKey("GroupBean/findBeansByJoinTable?sourceBean=WagonBean&sourceProperties=id&targetProperties=targetId&join=t_asso_group_user;group_id;user_id");
        softAssertions.assertThat(p1).hasSize(1).extracting(JoinTable::name, JoinTable::left, JoinTable::right)
                .containsOnly(Tuple.tuple("t_asso_group_user", new String[] { "group_id" }, new String[] { "user_id" }));
        JoinTable[] p2 = StatementNameHelper.extractJoinInFindBeansByJoinTableKey(
                "GroupBean/findBeansByJoinTable?sourceBean=WagonBean&sourceProperties=code,version&targetProperties=code&join=t_asso_group_toto;group_code,group_version;toto_id#t_asso_toto_user;toto_id;user_code");
        softAssertions.assertThat(p2).hasSize(2).extracting(JoinTable::name, JoinTable::left, JoinTable::right)
                .containsOnly(Tuple.tuple("t_asso_group_toto", new String[] { "group_code", "group_version" }, new String[] { "toto_id" }),
                        Tuple.tuple("t_asso_toto_user", new String[] { "toto_id" }, new String[] { "user_code" }));
        softAssertions.assertAll();
    }

    @Test
    public void testExtractOrderBysInFindBeansByJoinTableKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractOrderBiesInFindBeansByJoinTableKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractOrderBiesInFindBeansByJoinTableKey(
                "GroupBean/findBeansByJoinTable?sourceBean=WagonBean&sourceProperties=id&targetProperties=id&join=t_asso_group_user;group_id;user_id&orderBy=rang;Asc"))
                .extracting(OrderBy::value, OrderBy::sort).containsExactly(Tuple.tuple("rang", OrderBy.Sort.Asc));
        softAssertions.assertThat(StatementNameHelper.extractOrderBiesInFindBeansByJoinTableKey(
                "GroupBean/findBeansByJoinTable?sourceBean=WagonBean&sourceProperties=id&targetProperties=id&join=t_asso_group_user;group_id;user_id&orderBy=rang;Asc#cancel;Desc"))
                .extracting(OrderBy::value, OrderBy::sort).containsExactly(Tuple.tuple("rang", OrderBy.Sort.Asc), Tuple.tuple("cancel", OrderBy.Sort.Desc));
        softAssertions.assertAll();
    }

    @Test
    public void testIsIgnoreCancelInFindBeansByJoinTableKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.isIgnoreCancelInFindBeansByJoinTableKey(null)).isFalse();
        softAssertions.assertThat(StatementNameHelper
                .isIgnoreCancelInFindBeansByJoinTableKey("GroupBean/findBeansByJoinTable?sourceBean=WagonBean&sourceProperties=id&targetProperties=targetId&join=t_asso_group_user;group_id;user_id"))
                .isFalse();
        softAssertions.assertThat(StatementNameHelper.isIgnoreCancelInFindBeansByJoinTableKey(
                "GroupBean/findBeansByJoinTable?sourceBean=WagonBean&sourceProperties=id&targetProperties=targetId&join=t_asso_group_user;group_id;user_id&ignoreCancel")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isIgnoreCancelInFindBeansByJoinTableKey(
                "GroupBean/findBeansByJoinTable?sourceBean=WagonBean&sourceProperties=code,version&targetProperties=code&join=t_asso_group_toto;group_code,group_version;toto_id#t_asso_toto_user;toto_id;user_code"))
                .isFalse();
        softAssertions.assertThat(StatementNameHelper.isIgnoreCancelInFindBeansByJoinTableKey(
                "GroupBean/findBeansByJoinTable?sourceBean=WagonBean&sourceProperties=code,version&targetProperties=code&join=t_asso_group_toto;group_code,group_version;toto_id#t_asso_toto_user;toto_id;user_code&ignoreCancel"))
                .isTrue();
        softAssertions.assertAll();
    }

    @Test
    public void testBuildInsertKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.buildInsertKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildInsertKey(WagonBean.class)).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/insert");
        softAssertions.assertAll();
    }

    @Test
    public void testIsInsertKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.isInsertKey(null)).isFalse();
        softAssertions.assertThat(StatementNameHelper.isInsertKey("com.model.WagonBean/insert")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isInsertKey("com.model.WagonBean1/insert")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isInsertKey("model.WagonBean/insert")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isInsertKey("com.talanlabs.bean.mybatis.data.WagonBean/insert")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isInsertKey("model.WagonBean")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isInsertKey("/insert")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isInsertKey("model-WagonBean/insert")).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInInsertKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInInsertKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInInsertKey("com.talanlabs.bean.mybatis.data.WagonBean/insert")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInInsertKey("model.WagonBean/insert")).isNull();
        softAssertions.assertAll();
    }

    @Test
    public void testBuildUpdateKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.buildUpdateKey(null, null, null)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildUpdateKey(WagonBean.class, null, null)).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=&nlsProperties=");
        softAssertions.assertThat(StatementNameHelper.buildUpdateKey(WagonBean.class, null, new String[] { "name" }))
                .isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=&nlsProperties=name");
        softAssertions.assertThat(StatementNameHelper.buildUpdateKey(WagonBean.class, new String[] { "code" }, null))
                .isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=code&nlsProperties=");
        softAssertions.assertThat(StatementNameHelper.buildUpdateKey(WagonBean.class, new String[] { "code", "name", "createdBy" }, new String[] { "name" }))
                .isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=code,name,createdBy&nlsProperties=name");
        softAssertions.assertAll();
    }

    @Test
    public void testIsUpdateKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.isUpdateKey(null)).isFalse();
        softAssertions.assertThat(StatementNameHelper.isUpdateKey("com.model.WagonBean/update?properties=")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isUpdateKey("com.model.WagonBean/update?properties=code,name,createdBy")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isUpdateKey("com.model.WagonBean/update?properties=&nlsProperties=")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isUpdateKey("com.model.WagonBean1/update?properties=code,name,createdBy&nlsProperties=toto")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isUpdateKey("model.WagonBean/update?properties=&nlsProperties=titi,toto")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isUpdateKey("model.WagonBean/update?properties=code,name,createdBy&nlsProperties=titi,toto")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=code,name,createdBy&nlsProperties=")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isUpdateKey("model.WagonBean")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isUpdateKey("/update?nlsProperties=")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isUpdateKey("model-WagonBean/update")).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInUpdateKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInUpdateKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=code")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=&nlsProperties=")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=&nlsProperties=rien")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=code,name&nlsProperties=rien"))
                .isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInUpdateKey("model.WagonBean/update")).isNull();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractPropertyNamesInUpdateKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractPropertiesInUpdateKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=")).isEmpty();
        softAssertions.assertThat(StatementNameHelper.extractPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=&nlsProperties=")).isEmpty();
        softAssertions.assertThat(StatementNameHelper.extractPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=name")).containsExactly("name");
        softAssertions.assertThat(StatementNameHelper.extractPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=code,name,createdBy"))
                .containsExactly("code", "name", "createdBy");
        softAssertions.assertThat(StatementNameHelper.extractPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=&nlsProperties=code,name")).isEmpty();
        softAssertions.assertThat(StatementNameHelper.extractPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=code&nlsProperties=code,name")).containsExactly("code");
        softAssertions.assertThat(StatementNameHelper.extractPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=code,name,createdBy&nlsProperties=name,name2"))
                .containsExactly("code", "name", "createdBy");
        softAssertions.assertAll();
    }

    @Test
    public void testExtractNlsPropertyNamesInUpdateKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractNlsPropertiesInUpdateKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractNlsPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=")).isEmpty();
        softAssertions.assertThat(StatementNameHelper.extractNlsPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=&nlsProperties=")).isEmpty();
        softAssertions.assertThat(StatementNameHelper.extractNlsPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=&nlsProperties=name")).containsExactly("name");
        softAssertions.assertThat(StatementNameHelper.extractNlsPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=&nlsProperties=code,name"))
                .containsExactly("code", "name");
        softAssertions.assertThat(StatementNameHelper.extractNlsPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=code&nlsProperties=code,name"))
                .containsExactly("code", "name");
        softAssertions.assertThat(StatementNameHelper.extractNlsPropertiesInUpdateKey("com.talanlabs.bean.mybatis.data.WagonBean/update?properties=code,name,createdBy&nlsProperties=code,name"))
                .containsExactly("code", "name");
        softAssertions.assertAll();
    }

    @Test
    public void testBuildDeleteKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.buildDeleteKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildDeleteKey(WagonBean.class)).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/delete");
        softAssertions.assertAll();
    }

    @Test
    public void testIsDeleteKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.isDeleteKey(null)).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteKey("com.model.WagonBean/delete")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteKey("com.model.WagonBean1/delete")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteKey("model.WagonBean/delete")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteKey("com.talanlabs.bean.mybatis.data.WagonBean/delete")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteKey("model.WagonBean")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteKey("/delete")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteKey("model-WagonBean/delete")).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInDeleteKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInDeleteKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInDeleteKey("com.talanlabs.bean.mybatis.data.WagonBean/delete")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInDeleteKey("model.WagonBean/delete")).isNull();
        softAssertions.assertAll();
    }

    @Test
    public void testBuildNlsKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.buildFindNlsColumnKey(null, null)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildFindNlsColumnKey(WagonBean.class, "name")).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/findNlsColumn?property=name");
        softAssertions.assertThat(StatementNameHelper.buildFindNlsColumnKey(WagonBean.class, "toto")).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/findNlsColumn?property=toto");
        softAssertions.assertAll();
    }

    @Test
    public void testIsNlsKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.isFindNlsColumnKey(null)).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindNlsColumnKey("com.model.WagonBean/findNlsColumn?property=name")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindNlsColumnKey("com.model.WagonBean1/findNlsColumn?property=name")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindNlsColumnKey("model.WagonBean/findNlsColumn?property=name")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindNlsColumnKey("com.talanlabs.bean.mybatis.data.WagonBean/findNlsColumn?property=name")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isFindNlsColumnKey("model.WagonBean")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindNlsColumnKey("/findNlsColumn")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindNlsColumnKey("model-WagonBean/findNlsColumn")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isFindNlsColumnKey("model.WagonBean/findNlsColumn")).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInNlsKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindNlsColumnKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindNlsColumnKey("com.talanlabs.bean.mybatis.data.WagonBean/findNlsColumn?property=name")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInFindNlsColumnKey("model.WagonBean/findNlsColumn?property=name")).isNull();
        softAssertions.assertAll();
    }

    @Test
    public void testBuildDeleteEntityByIdKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.buildDeleteEntityByIdKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildDeleteEntityByIdKey(WagonBean.class)).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/deleteEntityById");
        softAssertions.assertAll();
    }

    @Test
    public void testIsDeleteEntityByIdKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.isDeleteEntityByIdKey(null)).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteEntityByIdKey("com.model.WagonBean/deleteEntityById")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteEntityByIdKey("com.model.WagonBean1/deleteEntityById")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteEntityByIdKey("model.WagonBean/deleteEntityById")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteEntityByIdKey("com.talanlabs.bean.mybatis.data.WagonBean/deleteEntityById")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteEntityByIdKey("model.WagonBean")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteEntityByIdKey("/deleteEntityById")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteEntityByIdKey("model-WagonBean/deleteEntityById")).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInDeleteEntityByIdKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInDeleteEntityByIdKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInDeleteEntityByIdKey("com.talanlabs.bean.mybatis.data.WagonBean/deleteEntityById")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInDeleteEntityByIdKey("model.WagonBean/deleteEntityById")).isNull();
        softAssertions.assertAll();
    }

    @Test
    public void testBuildDeleteBeansByKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.buildDeleteBeansByKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildDeleteBeansByKey(WagonBean.class)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildDeleteBeansByKey(WagonBean.class)).isNull();
        softAssertions.assertThat(StatementNameHelper.buildDeleteBeansByKey(WagonBean.class, "login")).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/deleteBeansBy?properties=login");
        softAssertions.assertThat(StatementNameHelper.buildDeleteBeansByKey(WagonBean.class, "login")).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/deleteBeansBy?properties=login");
        softAssertions.assertThat(StatementNameHelper.buildDeleteBeansByKey(WagonBean.class, "login", "password"))
                .isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/deleteBeansBy?properties=login,password");
        softAssertions.assertThat(StatementNameHelper.buildDeleteBeansByKey(WagonBean.class, "login", "password"))
                .isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/deleteBeansBy?properties=login,password");
        softAssertions.assertAll();
    }

    @Test
    public void testIsDeleteBeansByKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey(null)).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("model.WagonBean/deleteBeansBy?properties=login")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("com.model.WagonBean/deleteBeansBy?properties=login")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("model.WagonBean/deleteBeansBy?properties=idUser")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("model.WagonBean/deleteBeansBy?properties=idUser,login")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("model.WagonBean/deleteBeansBy?properties=idUser;login")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("model.WagonBean/deleteBeansBy?properties=idUser,login")).isTrue();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("model.WagonBean/deleteBeansBy?properties=idUser,login&toto")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("model.WagonBean/deleteBeansBy?idUser")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("model.WagonBean/deleteBeansBy")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("model.WagonBean/deleteBeansBy?")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("model.WagonBean")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("/deleteBeansBy")).isFalse();
        softAssertions.assertThat(StatementNameHelper.isDeleteBeansByKey("model-WagonBean/deleteBeansBy")).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInDeleteBeansByKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInDeleteBeansByKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInDeleteBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/deleteBeansBy?properties=login")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInDeleteBeansByKey("model.WagonBean/deleteBeansBy?properties=login")).isNull();
        softAssertions.assertThat(StatementNameHelper.extractBeanClassInDeleteBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/deleteBeansBy?properties=login,password"))
                .isEqualTo(WagonBean.class);
        softAssertions.assertAll();
    }

    @Test
    public void testExtractIdParentNameInDeleteBeansByKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInDeleteBeansByKey(null)).isNull();
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInDeleteBeansByKey("com.talanlabs.bean.mybatis.data.WagonBean/deleteBeansBy?properties=login")).containsOnly("login");
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInDeleteBeansByKey("model.WagonBean/deleteBeansBy?properties=login")).containsOnly("login");
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInDeleteBeansByKey("model.WagonBean/deleteBeansBy?properties=idUser")).containsOnly("idUser");
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInDeleteBeansByKey("com.model.WagonBean/deleteBeansBy?properties=idUser")).containsOnly("idUser");
        softAssertions.assertThat(StatementNameHelper.extractPropertyNamesInDeleteBeansByKey("com.model.WagonBean/deleteBeansBy?properties=idUser,password")).containsOnly("idUser", "password");
        softAssertions.assertAll();
    }
}
