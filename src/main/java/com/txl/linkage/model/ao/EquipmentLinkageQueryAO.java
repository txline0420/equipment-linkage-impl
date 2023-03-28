package com.txl.linkage.model.ao;

import lombok.*;

import java.io.Serializable;

/**
 *
 * 查询设备联动规则
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EquipmentLinkageQueryAO implements Serializable {
    //当前第几页
    private Integer pageNum;
    //每页行数
    private Integer pageSize;
}
