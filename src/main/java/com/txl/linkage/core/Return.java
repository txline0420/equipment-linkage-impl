package com.txl.linkage.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by TangXiangLin on 2023-03-15 20:25
 * 统一返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Return implements Serializable {
    private static final long serialVersionUID = 3809374950818378332L;
    private boolean success;
    private String msg;
}
