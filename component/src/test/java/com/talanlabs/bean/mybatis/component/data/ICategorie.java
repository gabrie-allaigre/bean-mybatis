package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.Id;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

import java.util.List;

@ComponentBean
@Entity(name = "T_CATEGORIE")
public interface ICategorie extends IComponent {

    @Id
    @Column(name = "ID_CATEGORIE")
    Integer getIdCategorie();

    void setIdCategorie(Integer idCategorie);

    @Column(name = "ID_CATEGORIE_PARENT")
    Integer getIdCategorieParent();

    void setIdCategorieParent(Integer idCategorieParent);

    @Column(name = "LIB_CATEGORIE")
    String getLibelleCategorie();

    void setLibelleCategorie(String libelleCategorie);

    @Collection(propertySource = CategorieFields.idCategorie, propertyTarget = CategorieFields.idCategorieParent)
    List<ICategorie> getListSousCategorie();

    void setListSousCategorie(List<ICategorie> listSousCategorie);

}
