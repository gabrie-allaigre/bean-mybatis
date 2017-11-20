package com.talanlabs.bean.mybatis.session.meta;

import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.FetchType;
import com.talanlabs.bean.mybatis.annotation.Id;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.data.Country2Bean;
import com.talanlabs.bean.mybatis.data.CountryBean;
import com.talanlabs.bean.mybatis.data.TestOnlyFieldBean;
import com.talanlabs.bean.mybatis.data.TestOnlyReadBean;
import com.talanlabs.bean.mybatis.data.TestOnlyWriteBean;
import com.talanlabs.bean.mybatis.factory.IdFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DefaultMetaInfoBeanTest {

    @Test
    public void testBean() {
        MetaInfoBean metaInfoBean = new DefaultMetaInfoBean(CountryBean.class);
        Assertions.assertThat(metaInfoBean.getPropertyNames())
                .contains("code", "name", "id", "version", "createdDate", "createdBy", "updatedDate", "updatedBy", "canceled", "canceledDate", "canceledBy");
        Assertions.assertThat(metaInfoBean.getPropertyType("code")).isEqualTo(String.class);

        CountryBean country = new CountryBean();
        country.setId(IdFactory.IdString.from("1"));
        country.setCode("fr");

        Assertions.assertThat(country.getId()).isEqualTo(IdFactory.IdString.from("1"));
        Assertions.assertThat(metaInfoBean.getPropertyValue(country, "id")).isEqualTo(IdFactory.IdString.from("1"));
        metaInfoBean.setPropertyValue(country, "id", IdFactory.IdString.from("2"));
        Assertions.assertThat(metaInfoBean.getPropertyValue(country, "id")).isEqualTo(IdFactory.IdString.from("2"));
        Assertions.assertThat(country.getId()).isEqualTo(IdFactory.IdString.from("2"));

        Assertions.assertThat(country.getCode()).isEqualTo("fr");
        Assertions.assertThat(metaInfoBean.getPropertyValue(country, "code")).isEqualTo("fr");
        metaInfoBean.setPropertyValue(country, "code", "en");
        Assertions.assertThat(metaInfoBean.getPropertyValue(country, "code")).isEqualTo("en");
        Assertions.assertThat(country.getCode()).isEqualTo("en");
        Assertions.assertThatThrownBy(() -> metaInfoBean.getPropertyValue(country, "toto")).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> metaInfoBean.getPropertyValue("toto", "code")).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> metaInfoBean.setPropertyValue(country, "toto", 10)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> metaInfoBean.setPropertyValue("toto", "code", 10)).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThat(metaInfoBean.isPropertyAnnotationPresent("id", Id.class)).isTrue();
        Assertions.assertThat(metaInfoBean.isPropertyAnnotationPresent("id", Column.class)).isTrue();
        Assertions.assertThat(metaInfoBean.isPropertyAnnotationPresent("code", Column.class)).isTrue();
        Assertions.assertThat(metaInfoBean.isPropertyAnnotationPresent("code", Association.class)).isFalse();
        Assertions.assertThatThrownBy(() -> metaInfoBean.isPropertyAnnotationPresent("toto", Id.class)).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThat(metaInfoBean.getPropertyAnnotation("id", Id.class)).isNotNull();
        Assertions.assertThat(metaInfoBean.getPropertyAnnotation("id", Column.class)).isNotNull().extracting(Column::name).contains("ID");
        Assertions.assertThat(metaInfoBean.getPropertyAnnotation("code", Column.class)).isNotNull().extracting(Column::name).contains("CODE");
        Assertions.assertThat(metaInfoBean.getPropertyAnnotation("code", Association.class)).isNull();
        Assertions.assertThatThrownBy(() -> metaInfoBean.getPropertyAnnotation("toto", Id.class)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testBeanWithSuper() {
        MetaInfoBean metaInfoBean1 = new DefaultMetaInfoBean(CountryBean.class);
        Assertions.assertThat(metaInfoBean1.getPropertyAnnotation("name", NlsColumn.class)).isNotNull().extracting(NlsColumn::name, NlsColumn::fetchType, NlsColumn::nested)
                .contains("NAME", FetchType.DEFAULT, false);

        MetaInfoBean metaInfoBean2 = new DefaultMetaInfoBean(Country2Bean.class);
        Assertions.assertThat(metaInfoBean2.getPropertyAnnotation("name", NlsColumn.class)).isNotNull().extracting(NlsColumn::name, NlsColumn::fetchType, NlsColumn::nested)
                .contains("NAME", FetchType.EAGER, true);
    }

    @Test
    public void testBeanOnlyRead() {
        MetaInfoBean metaInfoBean = new DefaultMetaInfoBean(TestOnlyReadBean.class);
        TestOnlyReadBean testOnlyRead = new TestOnlyReadBean();
        Assertions.assertThat(metaInfoBean.getPropertyValue(testOnlyRead, "code")).isNull();

        metaInfoBean.setPropertyValue(testOnlyRead, "code", "Gaby");
        Assertions.assertThat(metaInfoBean.getPropertyValue(testOnlyRead, "code")).isNotNull().isEqualTo("Gaby");
    }

    @Test
    public void testBeanOnlyWrite() {
        MetaInfoBean metaInfoBean = new DefaultMetaInfoBean(TestOnlyWriteBean.class);
        TestOnlyWriteBean testOnlyWrite = new TestOnlyWriteBean();
        Assertions.assertThat(metaInfoBean.getPropertyValue(testOnlyWrite, "code")).isNull();
        metaInfoBean.setPropertyValue(testOnlyWrite, "code", "Gaby");
        Assertions.assertThat(metaInfoBean.getPropertyValue(testOnlyWrite, "code")).isNotNull().isEqualTo("Gaby");
    }

    @Test
    public void testBeanOnlyField() {
        MetaInfoBean metaInfoBean = new DefaultMetaInfoBean(TestOnlyFieldBean.class);
        TestOnlyFieldBean testOnlyField = new TestOnlyFieldBean();
        Assertions.assertThat(metaInfoBean.getPropertyValue(testOnlyField, "code")).isNull();
        metaInfoBean.setPropertyValue(testOnlyField, "code", "Gaby");
        Assertions.assertThat(metaInfoBean.getPropertyValue(testOnlyField, "code")).isNotNull().isEqualTo("Gaby");
    }
}
