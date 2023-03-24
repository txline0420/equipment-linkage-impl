package com.txl.linkage.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by TangXiangLin on 2023-03-23 17:15
 * 简单的返回值对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleReturnBo implements Return{
    private static final long serialVersionUID = 8587315569034866329L;
    private boolean success;
    private String msg;

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public String msg() {
        return null;
    }
}
