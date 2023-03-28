package com.txl.linkage.controller;

import com.alibaba.fastjson.JSONObject;
import com.txl.linkage.core.PageModel;
import com.txl.linkage.core.ResponseStatus;
import com.txl.linkage.core.ResultBean;
import com.txl.linkage.model.ao.EquipmentLinkageQueryAO;
import com.txl.linkage.model.ao.StrategyAO;
import com.txl.linkage.model.bo.QueryReturnBo;
import com.txl.linkage.model.bo.Return;
import com.txl.linkage.model.bo.SaveReturnBo;
import com.txl.linkage.service.EquipmentLinkageRepositoryService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by TangXiangLin on 2023-03-15 14:40
 * 设备联动页面服务
 */
@RestController
@RequestMapping(value = "/iot/equipmentLinkage/linkageManagement")
@AllArgsConstructor
public class EquipmentLinkageController {
    private static final Logger logger = LoggerFactory.getLogger(EquipmentLinkageController.class);

    private EquipmentLinkageRepositoryService repositoryService;

    /*
     * 新增或修改设备联动规则
     *  相关业务：
     *      1. 新增规则默认情况下，都是未启动状态，新增完毕以后，再开进行开启/关闭操作。
     *      2. 修改规则时，需要先将规则停止，才能修改相应的规则。
     *
     */
    @PostMapping(value = "/save")
    //@ApiOperation(value = "新增或修改设备联动规则")
    public ResultBean<Object> save(@RequestBody StrategyAO vo) {
        ResultBean<Object> resultBean = ResultBean.success(HttpStatus.SC_OK, null, null);
        logger.info("\n");
        logger.info("------------------- EquipmentLinkageController.insertOrUpdate ---------");
        if (ObjectUtils.isEmpty(vo)) {
            logger.info("[设备联动]-[新增或者修改]- [新增或者修改失败]: {null}");
            resultBean.setData(
                    SaveReturnBo.builder()
                            .success(false)
                            .msg(ResponseStatus.Is_Not_Null.getName())
                            .build()
            );
            return resultBean;
        }

        SaveReturnBo bo;
        if (ObjectUtils.isEmpty(vo.getSId())) {
            // insert
            logger.info("[设备联动]-[新增]，{}", JSONObject.toJSONString(vo));
            try {
                bo = this.repositoryService.insert(vo);
                resultBean.setData(bo);
            } catch (DuplicateKeyException e) {
                logger.error("[设备联动]-[新增]-[新增失败]-[name字段重复]，{}", e.getCause().getMessage());
                HashMap<String, String> map = new HashMap<>();
                map.put("name", "设备名重复");
                resultBean.setData(
                        SaveReturnBo.builder()
                                .success(false)
                                .msg(ResponseStatus.Insert_Failed.getName())
                                .validateMap(map)
                                .build()
                );
            } catch (SQLSyntaxErrorException e) {
                logger.error("[设备联动]-[新增]-[新增失败]，{}", e.getCause().getMessage());
                resultBean.setData(
                        SaveReturnBo.builder()
                                .success(false)
                                .msg(ResponseStatus.Insert_Failed.getName())
                                .build()
                );
            } catch (Exception e) {
                logger.error("[设备联动]-[新增]-[新增失败]，{}", e.getCause().getMessage());
                resultBean.setData(
                        SaveReturnBo.builder()
                                .success(false)
                                .msg(ResponseStatus.Insert_Failed.getName())
                                .build()
                );
            }
        } else {
            // update
            logger.info("[设备联动]-[修改]，{}", JSONObject.toJSONString(vo));
            try {
                //查询设备是否关闭
                bo = this.repositoryService.update(vo);
                resultBean.setData(bo);
            } catch (DuplicateKeyException e) {
                logger.error("[设备联动]-[修改]-[修改失败]-[name字段重复]，{}", e.getCause().getMessage());
                HashMap<String, String> map = new HashMap<>();
                map.put("name", "设备名重复");
                resultBean.setData(
                        SaveReturnBo.builder()
                                .success(false)
                                .msg(ResponseStatus.Insert_Failed.getName())
                                .validateMap(map)
                                .build()
                );
            } catch (SQLSyntaxErrorException e) {
                logger.error("[设备联动]-[修改]-[修改失败sql异常]，{}", e.getCause().getMessage());
                resultBean.setData(
                        SaveReturnBo.builder()
                                .success(false)
                                .msg(ResponseStatus.Update_Failed.getName())
                                .build()
                );
            } catch (Exception e) {
                logger.error("[设备联动]-[修改]-[修改失败运行异常]，{}", e.getCause().getMessage());
                resultBean.setData(
                        SaveReturnBo.builder()
                                .success(false)
                                .msg(ResponseStatus.Update_Failed.getName())
                                .build()
                );
            }

        }
        logger.info("------------------- EquipmentLinkageController.insertOrUpdate --------- ");
        logger.info("\n");
        return resultBean;
    }

    /**
     * 删除设备联动规则
     *
     * @param sid 主键id
     * @return
     */
    @PostMapping(value = "/remove")
    public ResultBean<Object> remove(@RequestParam(value = "sid") Integer sid) {
        ResultBean<Object> resultBean = ResultBean.success(HttpStatus.SC_OK, null, null);
        logger.info("\n");
        logger.info("------------------- EquipmentLinkageController.remove ---------");

        // remove
        logger.info("[设备联动]-[删除]，设备id：{}", sid);
        try {
            SaveReturnBo bo = this.repositoryService.remove(sid);
            resultBean.setData(bo);
        } catch (SQLSyntaxErrorException e) {
            logger.error("[设备联动]-[删除]-[删除失败sql异常]-{}", e.getCause().getMessage());
            resultBean.setData(
                    SaveReturnBo.builder()
                            .success(false)
                            .msg(ResponseStatus.Remove_Failed.getName())
                            .build()
            );
        } catch (Exception e) {
            logger.error("[设备联动]-[删除]-[删除失败运行异常]-{}", e.getCause().getMessage());
            resultBean.setData(
                    SaveReturnBo.builder()
                            .success(false)
                            .msg(ResponseStatus.Remove_Failed.getName())
                            .build()
            );
        }
        logger.info("------------------- EquipmentLinkageController.remove --------- ");
        logger.info("\n");
        return resultBean;
    }

    @PostMapping(value = "/query")
    public ResultBean<Object> query(@RequestBody EquipmentLinkageQueryAO vo) {
        ResultBean<Object> resultBean = ResultBean.success(HttpStatus.SC_OK, null, null);
        logger.info("\n");
        logger.info("------------------- EquipmentLinkageController.query ---------");
        try {
            QueryReturnBo queryData = this.repositoryService.query(vo);
            resultBean.setData(queryData);
        } catch (SQLSyntaxErrorException e) {
            logger.error("[设备查询]-[查询]-[查询失败sql异常]-{}", e.getCause().getMessage());
            resultBean.setData(
                    SaveReturnBo.builder()
                            .success(false)
                            .msg(ResponseStatus.Remove_Failed.getName())
                            .build()
            );
        } catch (Exception e) {
            logger.error("[设备查询]-[查询]-[查询失败运行异常]-{}", e.getCause().getMessage());
            resultBean.setData(
                    SaveReturnBo.builder()
                            .success(false)
                            .msg(ResponseStatus.Remove_Failed.getName())
                            .build()
            );
        }
        logger.info("------------------- EquipmentLinkageController.query --------- ");
        logger.info("\n");
        return resultBean;
    }


    @GetMapping(value = "/validateName")
    //@ApiOperation(value = "设备联动规则名称校验")
    public ResultBean<Object> validateName(@RequestParam(value = "name") String name) {
        ResultBean<Object> resultBean = ResultBean.success(HttpStatus.SC_OK, null, null);
        logger.info("\n");
        logger.info("------------------- EquipmentLinkageController.validateName ---------");
        logger.info("[设备联动]-[新增或修改]-[校验内容]: {}", name);

        try {
            Return bo = this.repositoryService.validateName(name);
            resultBean.setData(bo);
            logger.info("[设备联动]-[新增或修改]-[校验完毕]: {}", "该规则名可以使用");
        } catch (SQLSyntaxErrorException e) {
            logger.error("[设备联动]-[新增或修改]-[重复性校验失败]: {}", name);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("------------------- EquipmentLinkageController.validateName --------- ");
        logger.info("\n");
        return resultBean;
    }


}
