package com.talanlabs.bean.mybatis.data;

import com.talanlabs.bean.mybatis.annotation.Canceled;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.helper.IId;

import java.util.Date;

@Entity(name = "T_GROUP")
public class GroupBean extends EntityBean implements ITracable, ICancelable {

    @Column(name = "USER_ID")
    private IId userId;
    @NlsColumn(name = "NAME")
    private String name;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Canceled
    @Column(name = "CANCELED")
    private boolean canceled;
    @Column(name = "CANCELED_DATE")
    private Date canceledDate;
    @Column(name = "CANCELED_BY")
    private String canceledBy;

    public IId getUserId() {
        return userId;
    }

    public void setUserId(IId userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Date getCanceledDate() {
        return canceledDate;
    }

    public void setCanceledDate(Date canceledDate) {
        this.canceledDate = canceledDate;
    }

    public String getCanceledBy() {
        return canceledBy;
    }

    public void setCanceledBy(String canceledBy) {
        this.canceledBy = canceledBy;
    }
}
