package com.talanlabs.bean.mybatis.session.meta;

import com.talanlabs.bean.mybatis.data.ContainerBean;
import com.talanlabs.bean.mybatis.data.CountryBean;
import com.talanlabs.bean.mybatis.data.TrainBean;
import com.talanlabs.bean.mybatis.data.WagonBean;
import com.talanlabs.bean.mybatis.data.check.WrongAssociation1Bean;
import com.talanlabs.bean.mybatis.data.check.WrongAssociation2Bean;
import com.talanlabs.bean.mybatis.data.check.WrongAssociation3Bean;
import com.talanlabs.bean.mybatis.data.check.WrongAssociation4Bean;
import com.talanlabs.bean.mybatis.data.check.WrongAssociation5Bean;
import com.talanlabs.bean.mybatis.data.check.WrongAssociation6Bean;
import com.talanlabs.bean.mybatis.data.check.WrongCache1Bean;
import com.talanlabs.bean.mybatis.data.check.WrongCache2Bean;
import com.talanlabs.bean.mybatis.data.check.WrongCanceledBean;
import com.talanlabs.bean.mybatis.data.check.WrongCollection1Bean;
import com.talanlabs.bean.mybatis.data.check.WrongCollection2Bean;
import com.talanlabs.bean.mybatis.data.check.WrongCollection3Bean;
import com.talanlabs.bean.mybatis.data.check.WrongCollection4Bean;
import com.talanlabs.bean.mybatis.data.check.WrongCollection5Bean;
import com.talanlabs.bean.mybatis.data.check.WrongCollection6Bean;
import com.talanlabs.bean.mybatis.data.check.WrongCollection7Bean;
import com.talanlabs.bean.mybatis.data.check.WrongCollection8Bean;
import com.talanlabs.bean.mybatis.data.check.WrongColumnBean;
import com.talanlabs.bean.mybatis.data.check.WrongEntityBean;
import com.talanlabs.bean.mybatis.data.check.WrongMultiAnnotationBean;
import com.talanlabs.bean.mybatis.data.check.WrongNlsColumn1Bean;
import com.talanlabs.bean.mybatis.data.check.WrongNlsColumn2Bean;
import com.talanlabs.bean.mybatis.data.check.WrongNlsColumn3Bean;
import com.talanlabs.bean.mybatis.data.check.WrongNotBean;
import com.talanlabs.bean.mybatis.data.check.WrongVersionBean;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MetaBeanTest {

    @Test
    public void testBean() {
        MetaBean metaBean = new MetaBean(new BeanConfiguration());

        MetaInfoBean metaInfoBean = metaBean.forBeanClass(CountryBean.class);
        Assertions.assertThat(metaBean.forBeanClass(CountryBean.class)).isEqualTo(metaInfoBean);
        Assertions.assertThat(metaBean.getIdPropertyName(CountryBean.class)).isNotNull().isEqualTo("id");
        Assertions.assertThat(metaBean.getVersionPropertyName(CountryBean.class)).isNotNull().isEqualTo("version");
        Assertions.assertThat(metaBean.getCanceledPropertyName(CountryBean.class)).isNotNull().isEqualTo("canceled");
        Assertions.assertThat(metaBean.getPropertyNamesWithNlsColumn(CountryBean.class)).isNotNull().containsOnly("name");
        Assertions.assertThat(metaBean.isUseNlsColumn(CountryBean.class)).isTrue();
        Assertions.assertThat(metaBean.isUseNlsColumn(TrainBean.class)).isFalse();
        Assertions.assertThat(metaBean.isAllUseNlsColumn(CountryBean.class)).isTrue();
        Assertions.assertThat(metaBean.isAllUseNlsColumn(TrainBean.class)).isTrue();
        Assertions.assertThat(metaBean.isAllUseNlsColumn(ContainerBean.class)).isFalse();
        Assertions.assertThat(metaBean.getCollectionElementClass(TrainBean.class, "wagons")).isNotNull().isEqualTo(WagonBean.class);
        Assertions.assertThat(metaBean.prepareColumns(CountryBean.class, new String[]{"code", "id"})).isNotNull().containsOnly("CODE", "ID");
    }

    @Test
    public void testCheckBean() {
        MetaBean metaBean = new MetaBean(new BeanConfiguration());

        Assertions.assertThatCode(() -> metaBean.validateBean(TrainBean.class)).doesNotThrowAnyException();
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongNotBean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongEntityBean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongColumnBean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongVersionBean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongCanceledBean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongNlsColumn1Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongNlsColumn2Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongNlsColumn3Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongCache1Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongCache2Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongMultiAnnotationBean.class)).isInstanceOf(ValidationBeanException.class);
    }

    @Test
    public void testCheckAssociationBean() {
        MetaBean metaBean = new MetaBean(new BeanConfiguration());

        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongAssociation1Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongAssociation2Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongAssociation3Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongAssociation4Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongAssociation5Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongAssociation6Bean.class)).isInstanceOf(ValidationBeanException.class);
    }

    @Test
    public void testCheckCollectionBean() {
        MetaBean metaBean = new MetaBean(new BeanConfiguration());

        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongCollection1Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongCollection2Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongCollection3Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongCollection4Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongCollection5Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongCollection6Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongCollection7Bean.class)).isInstanceOf(ValidationBeanException.class);
        Assertions.assertThatThrownBy(() -> metaBean.validateBean(WrongCollection8Bean.class)).isInstanceOf(ValidationBeanException.class);
    }
}
