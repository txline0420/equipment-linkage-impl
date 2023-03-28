package com.txl.linkage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.txl.linkage.model.ao.StrategyAO;
import com.txl.linkage.model.bo.Return;
import com.txl.linkage.model.bo.SaveReturnBo;
import com.txl.linkage.model.entity.IotLinkageStrategy;

import java.sql.SQLSyntaxErrorException;

/**
 * Created by TangXiangLin on 2023-03-15 20:07
 * 设备联动存储接口
 */
public interface EquipmentLinkageRepositoryService extends IService<IotLinkageStrategy> {

    /**
     * 新增
     * @param vo 新增FORM表单数据
     * @return 业务逻辑对象
     * @throws SQLSyntaxErrorException  数据库异常
     * @throws Exception 业务异常
     */
    SaveReturnBo insert(StrategyAO vo) throws Exception;

    /**
     *
     * @param vo 修改FORM表单数据
     * @return 业务逻辑对象
     * @throws SQLSyntaxErrorException  数据库异常
     * @throws Exception 业务异常
     */
    SaveReturnBo update(StrategyAO vo) throws Exception;

    /**
     *
     * @param sid 设备id
     * @return 业务逻辑对象
     * @throws Exception 业务异常
     */
    SaveReturnBo remove(Integer sid) throws Exception;

    /**
     * 规则名称校验 - 不允许重复的名称
     * @param name 规则名称
     */
    Return validateName(String name) throws Exception;


}
