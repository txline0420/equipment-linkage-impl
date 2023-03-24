package com.txl.linkage.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Created by TangXiangLin on 2023-03-15 20:25
 * 统一返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveReturnBo implements Return {
    private static final long serialVersionUID = 3809374950818378332L;
    private boolean success;
    private String msg;
    private Map<String,String> validateMap;

    @Override
    public boolean success() {
        return this.success;
    }

    @Override
    public String msg() {
        return this.msg;
    }

}
