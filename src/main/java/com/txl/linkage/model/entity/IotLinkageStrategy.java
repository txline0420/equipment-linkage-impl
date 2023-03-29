package com.txl.linkage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by TangXiangLin on 2023-03-23 11:09
 * 设备联动规则表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "iot_linkage_strategy", autoResultMap = true)
public class IotLinkageStrategy {
    @TableId(type = IdType.AUTO)
    @TableField(value = "sid")
    private Integer sid;     // 主键id
    @TableField(value = "name")
    private String name;     // 设备联动策略规则名称
    @TableField(value = "type")
    private int type;        // 联动类型（0:设备联动, 1:场景联动）
    @TableField(value = "active")
    private int active;      // 是否启动（0:启用, 1:禁用）
    @TableField(value = "startTime")
    private Long startTime;      // 有效期开始时间(单位秒)
    @TableField(value = "endTime")
    private Long endTime;        // 有效期结束时间(单位秒)
    @TableField(value = "createTime")
    private Long createTime;     // 创建时间(单位秒)
    @TableField(value = "updateTime")
    private Long updateTime;     // 更新时间(单位秒)
    @TableField(value = "description")
    private String description; // 规则描述
    @TableField(value = "triggerScene")
    private String triggerScene; //"场景触发器"
    @TableField(value = "triggerCron")
    private String triggerCron; //"定时触发器"
    @TableField(value = "triggerDevice")
    private String triggerDevice; //"设备触发器"
}
