package com.talanlabs.bean.mybatis.data;

import java.util.Date;

public interface ITracable {

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    Date getUpdatedDate();

    void setUpdatedDate(Date updatedDate);

    String getUpdatedBy();

    void setUpdatedBy(String updatedBy);

}
