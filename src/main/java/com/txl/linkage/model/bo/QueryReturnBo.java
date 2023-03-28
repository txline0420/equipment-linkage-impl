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
public class QueryReturnBo<T> implements Return {

    private boolean success;
    private String msg;
    private Map<String,String> validateMap;
    //当前第几页
    private long pageNum;
    //每页行数
    private long pageSize;
    //总条数
    private long recordsTotal;
    private List<T> data;

    @Override
    public boolean success() {
        return this.success;
    }

    @Override
    public String msg() {
        return this.msg;
    }

}
