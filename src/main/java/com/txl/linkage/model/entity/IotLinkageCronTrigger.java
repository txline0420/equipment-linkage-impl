package com.txl.linkage.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Created by zhengjingtao on 2023-03-23 11:09
 * 设备联动定时器表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "iot_linkage_cron_trigger", autoResultMap = true)
public class IotLinkageCronTrigger {
    @TableId(type = IdType.AUTO)
    @TableField(value = "tid")
    private Integer tid;     // 主键id

    @TableField(value = "cron")
    private String cron; // 定时触发器表达式
}
