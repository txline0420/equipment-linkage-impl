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
public class EquipmentLinkageCronTriggerAO implements Serializable {
    //联动设备id
    private Integer sid;
    //cron表的id
    private  Integer tid;

    private String cron;

}
