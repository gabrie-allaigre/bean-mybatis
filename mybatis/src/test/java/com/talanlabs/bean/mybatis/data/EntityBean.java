package com.talanlabs.bean.mybatis.data;

import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Id;
import com.talanlabs.bean.mybatis.annotation.Version;
import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.helper.IdKeyGenerator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class EntityBean {

    @Id(keyGeneratorClass = IdKeyGenerator.class)
    @Column(name = "ID")
    protected IId id;
    @Version
    @Column(name = "VERSION")
    protected Integer version;

    public IId getId() {
        return id;
    }

    public void setId(IId id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EntityBean that = (EntityBean) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("version", version)
                .toString();
    }
}
