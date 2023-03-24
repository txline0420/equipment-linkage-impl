package com.txl.linkage.model.bo;

import java.io.Serializable;

/**
 * Created by TangXiangLin on 2023-03-23 17:11
 * 业务层返回值
 */
public interface Return extends Serializable {

    /** 业务执行结果 */
   boolean success();

   /** 业务信息 */
   String msg();

}
