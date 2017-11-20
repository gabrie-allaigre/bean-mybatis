package com.talanlabs.bean.mybatis.data;

import java.util.Date;

public interface ICancelable {

    boolean isCanceled();

    void setCanceled(boolean canceled);

    Date getCanceledDate();

    void setCanceledDate(Date canceledDate);

    String getCanceledBy();

    void setCanceledBy(String canceledBy);

}
