package com.txl.linkage.spi;

import com.alibaba.fastjson.JSONObject;
import com.txl.linkage.core.ResultBean;
import com.txl.linkage.model.vo.LinkageManagementStrategySaveVO;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by TangXiangLin on 2023-03-15 14:40
 * 设备联动页面服务
 */
@RestController
@RequestMapping(value="/iot/equipmentLinkage/linkageManagement")
public class EquipmentLinkageController {
    private static final Logger logger = LoggerFactory.getLogger(EquipmentLinkageController.class);



    /*
     * 新增或修改设备联动规则
     *  相关业务：
     *      1. 新增规则默认情况下，都是未启动状态，新增完毕以后，再开进行开启/关闭操作。
     *      2. 修改规则时，需要先将规则停止，才能修改相应的规则。
     *
     */
    @PostMapping(value = "/save")
    //@ApiOperation(value = "新增或修改设备联动规则")
    public ResultBean insertOrUpdate(@RequestBody @Valid LinkageManagementStrategySaveVO vo) {

        logger.info("\n");
        logger.info("------------------- EquipmentLinkageController.insertOrUpdate ---------");
        Integer id = vo.getStrategyId();
        if(ObjectUtils.isEmpty(id)){
            // insert
            logger.info("[设备联动]-[新增]，{}", JSONObject.toJSONString(vo));


        }else{
            // update

        }
        logger.info("------------------- EquipmentLinkageController.insertOrUpdate --------- ");
        logger.info("\n");
        return null;
    }
}
