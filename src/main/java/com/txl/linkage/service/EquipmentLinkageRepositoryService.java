package com.txl.linkage.service;

import com.txl.linkage.core.Return;
import com.txl.linkage.model.vo.LinkageManagementStrategySaveVO;

/**
 * Created by TangXiangLin on 2023-03-15 20:07
 * 设备联动存储接口
 */
public interface EquipmentLinkageRepositoryService {

    Return insert(LinkageManagementStrategySaveVO vo);
    Return update(LinkageManagementStrategySaveVO vo);


}
