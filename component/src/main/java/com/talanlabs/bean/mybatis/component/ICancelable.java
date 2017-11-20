package com.talanlabs.bean.mybatis.component;

import com.talanlabs.bean.mybatis.annotation.Canceled;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

import java.util.Date;

@ComponentBean
public interface ICancelable extends IComponent {

    @Canceled
    @Column(name = "CANCELED")
    boolean isCanceled();

    void setCanceled(boolean canceled);

    @Column(name = "CANCELED_DATE")
    Date getCanceledDate();

    void setCanceledDate(Date canceledDate);

    @Column(name = "CANCELED_BY")
    String getCanceledBy();

    void setCanceledBy(String canceledBy);

}
