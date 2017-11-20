import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;

import java.util.List;

public class MainBean {

    public static void main(String[] args) throws Exception {
        System.out.println(Toto.class.getAnnotation(Entity.class));
        System.out.println(SurToto.class.getAnnotation(Entity.class));

        MetaInfoBean metaInfoBean = new MetaBean(new BeanConfiguration()).forBeanClass(SurToto.class);
        System.out.println(metaInfoBean.getPropertyAnnotation("name",Column.class));
    }

    @Entity(name = "TOTO")
    public static class Toto {

        @Column(name = "NAME1")
        private String name;
        private List<String> list;

        @Column(name = "NAME2")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

    }

    public static class SurToto extends Toto {

        @Override
        @Column(name = "SUR_NAME")
        public String getName() {
            return super.getName();
        }
    }

}
