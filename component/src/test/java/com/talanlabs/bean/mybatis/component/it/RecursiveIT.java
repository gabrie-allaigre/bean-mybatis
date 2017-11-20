package com.talanlabs.bean.mybatis.component.it;

import com.talanlabs.bean.mybatis.component.data.CategorieFields;
import com.talanlabs.bean.mybatis.component.data.ICategorie;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import com.talanlabs.component.helper.ComponentHelper;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class RecursiveIT extends AbstractHSQLIntegration {

    @Test
    public void testFindCategorieById() {
        ICategorie categorie = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(ICategorie.class), 0);

        Assertions.assertThat(categorie).isNotNull();
        Assertions.assertThat(categorie.getLibelleCategorie()).isEqualTo("ELISE");
        Assertions.assertThat(categorie.getListSousCategorie()).isNotNull().hasSize(3).extracting("libelleCategorie").containsOnly("JOSE", "BEATRICE", "MARIE");

        ICategorie beatriceCategorie = ComponentHelper.findComponentBy(categorie.getListSousCategorie(), CategorieFields.idCategorie, 2);
        Assertions.assertThat(beatriceCategorie).isNotNull();
        Assertions.assertThat(beatriceCategorie.getLibelleCategorie()).isEqualTo("BEATRICE");
        Assertions.assertThat(beatriceCategorie.getListSousCategorie()).isNotNull().hasSize(2).extracting("libelleCategorie").containsOnly("GABRIEL", "DAVID");
    }
}
