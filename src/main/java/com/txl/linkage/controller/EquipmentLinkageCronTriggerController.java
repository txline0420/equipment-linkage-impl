package com.txl.linkage.controller;


import com.alibaba.fastjson.JSONObject;
import com.txl.linkage.core.ResponseStatus;
import com.txl.linkage.core.ResultBean;
import com.txl.linkage.model.ao.EquipmentLinkageCronTriggerAO;
import com.txl.linkage.model.ao.StrategyAO;
import com.txl.linkage.model.bo.QuerySimpleReturnBo;
import com.txl.linkage.model.bo.SaveReturnBo;
import com.txl.linkage.model.entity.IotLinkageCronTrigger;
import com.txl.linkage.service.EquipmentLinkageCronTriggerRepositoryService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.List;


/**
 * Created by zhengjingtao on 2023-03-28 14:40
 * 联动设备触发器的cron数据服务
 */
@RestController
@RequestMapping(value = "/iot/equipmentLinkage/linkageManagement/crontrigger")
@AllArgsConstructor
public class EquipmentLinkageCronTriggerController {
    private static final Logger logger = LoggerFactory.getLogger(EquipmentLinkageCronTriggerController.class);

    private EquipmentLinkageCronTriggerRepositoryService repositoryService;

    /**
     * 相关业务
     * 1.在iot_linkage_cron_trigger进行增删要同步iot_linkage_strategy的triggerCron字段进行增删
     * 2.在iot_linkage_cron_trigger进行增改要检测cron表达式格式
     */

    /**
     * 查询联动设备触发器
     */
    @PostMapping(value = "/query")
    public ResultBean<Object> query() {
        ResultBean<Object> resultBean = ResultBean.success(HttpStatus.SC_OK, null, null);
        logger.info("\n");
        logger.info("------------------- EquipmentLinkageCronTriggerController.query ---------");
        try {
            List<IotLinkageCronTrigger> cronList = this.repositoryService.list();
            QuerySimpleReturnBo.QuerySimpleReturnBoBuilder builder = QuerySimpleReturnBo.builder();
            resultBean.setData( builder
                    .success(true)
                    .data(cronList)
                    .msg(ResponseStatus.Query_Success.getName())
                    .build());
        }  catch (Exception e) {
            logger.error("[设备联动触发器cron]-[查询]-[查询失败运行异常]-{}", e.getCause().getMessage());
            resultBean.setData(
                    QuerySimpleReturnBo.builder()
                            .success(false)
                            .msg(ResponseStatus.Query_Failed.getName())
                            .build()
            );
        }
        logger.info("------------------- EquipmentLinkageCronTriggerController.query --------- ");
        logger.info("\n");
        return resultBean;
    }

    /**
     * 新增或者修改联动设备触发器
     */
    @PostMapping(value = "/save")
    public ResultBean<Object> save(@RequestBody EquipmentLinkageCronTriggerAO vo) {
        ResultBean<Object> resultBean = ResultBean.success(HttpStatus.SC_OK, null, null);
        logger.info("\n");
        logger.info("------------------- EquipmentLinkageCronTriggerController.insertOrUpdate ---------");
        if (ObjectUtils.isEmpty(vo)) {
            logger.info("[设备联动触发器cron]-[新增或者修改]- [新增或者修改失败]: {null}");
            resultBean.setData(
                    SaveReturnBo.builder()
                            .success(false)
                            .msg(ResponseStatus.Is_Not_Null.getName())
                            .build()
            );
            return resultBean;
        }

        SaveReturnBo bo;
        if (ObjectUtils.isEmpty(vo.getTid())) {
            // insert
            logger.info("[设备联动触发器cron]-[新增]，{}", JSONObject.toJSONString(vo));
            try {
                bo = this.repositoryService.insert(vo);
                resultBean.setData(bo);
            } catch (DuplicateKeyException e) {
                logger.error("[设备联动触发器cron]-[新增]-[新增失败]-[cron字段重复]，{}", e.getCause().getMessage());
                HashMap<String, String> map = new HashMap<>();
                map.put("cron", "cron重复");
                resultBean.setData(
                        SaveReturnBo.builder()
                                .success(false)
                                .msg(ResponseStatus.Insert_Failed.getName())
                                .validateMap(map)
                                .build()
                );
            } catch (SQLSyntaxErrorException e) {
                logger.error("[设备联动触发器cron]-[新增]-[新增失败]，{}", e.getCause().getMessage());
                resultBean.setData(
                        SaveReturnBo.builder()
                                .success(false)
                                .msg(ResponseStatus.Insert_Failed.getName())
                                .build()
                );
            } catch (Exception e) {
                logger.error("[设备联动触发器cron]-[新增]-[新增失败]，{}", e.getCause().getMessage());
                resultBean.setData(
                        SaveReturnBo.builder()
                                .success(false)
                                .msg(ResponseStatus.Insert_Failed.getName())
                                .build()
                );
            }
        } else {
            // update
            logger.info("[设备联动触发器cron]-[修改]，{}", JSONObject.toJSONString(vo));
            try {
                //查询设备是否关闭
                bo = this.repositoryService.update(vo);
                resultBean.setData(bo);
            } catch (DuplicateKeyException e) {
                logger.error("[设备联动触发器cron]-[修改]-[修改失败]-[cron字段重复]，{}", e.getCause().getMessage());
                HashMap<String, String> map = new HashMap<>();
                map.put("cron", "cron重复");
                resultBean.setData(
                        SaveReturnBo.builder()
                                .success(false)
                                .msg(ResponseStatus.Insert_Failed.getName())
                                .validateMap(map)
                                .build()
                );
            } catch (SQLSyntaxErrorException e) {
                logger.error("[设备联动触发器cron]-[修改]-[修改失败sql异常]，{}", e.getCause().getMessage());
                resultBean.setData(
                        SaveReturnBo.builder()
                                .success(false)
                                .msg(ResponseStatus.Update_Failed.getName())
                                .build()
                );
            } catch (Exception e) {
                logger.error("[设备联动触发器cron]-[修改]-[修改失败运行异常]，{}", e.getCause().getMessage());
                resultBean.setData(
                        SaveReturnBo.builder()
                                .success(false)
                                .msg(ResponseStatus.Update_Failed.getName())
                                .build()
                );
            }
        }
        logger.info("------------------- EquipmentLinkageCronTriggerController.insertOrUpdate --------- ");
        logger.info("\n");
        return resultBean;
    }

}


