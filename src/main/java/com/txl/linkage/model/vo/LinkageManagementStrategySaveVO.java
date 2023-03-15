package com.txl.linkage.model.vo;

import lombok.*;

import java.io.Serializable;

/**
 * Created by TangXiangLin on 2023-03-14 10:12
 * 新增或修改设备联动规则
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LinkageManagementStrategySaveVO implements Serializable {
    private static final long serialVersionUID = 4656838407253620538L;
    /** 标识 */
    private Integer strategyId;
    /** 名称 */
    private String strategyName;
    /** 类型 0:设备类型、1:场景类型 */
    private int strategyType;
    /** 有效期开始时间(单位秒) */
    private long strategyEffectiveStartTime;
    /** 有效期结束时间(单位秒) */
    private long strategyEffectiveEndTime;
    /** 描述信息 */
    private String strategyDescription;
}