package com.txl.linkage.model.bo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 查询返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuerySimpleReturnBo<E> implements Return {

    private boolean success;
    private String msg;
    private Map<String,String> validateMap;
    private List<E> data;

    @Override
    public boolean success() {
        return this.success;
    }

    @Override
    public String msg() {
        return this.msg;
    }

}
