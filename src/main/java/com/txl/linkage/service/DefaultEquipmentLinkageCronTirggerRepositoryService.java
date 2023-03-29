package com.txl.linkage.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txl.linkage.core.ResponseStatus;
import com.txl.linkage.mapper.IotLinkageCronTriggerMapper;
import com.txl.linkage.mapper.IotLinkageStrategyMapper;
import com.txl.linkage.model.ao.EquipmentLinkageCronTriggerAO;
import com.txl.linkage.model.ao.EquipmentLinkageQueryAO;
import com.txl.linkage.model.ao.StrategyAO;
import com.txl.linkage.model.bo.QueryReturnBo;
import com.txl.linkage.model.bo.Return;
import com.txl.linkage.model.bo.SaveReturnBo;
import com.txl.linkage.model.bo.SimpleReturnBo;
import com.txl.linkage.model.entity.IotLinkageCronTrigger;
import com.txl.linkage.model.entity.IotLinkageStrategy;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by TangXiangLin on 2023-03-23 11:07
 * 缺省的设备联动存储接口
 */
@Service
@AllArgsConstructor
public class DefaultEquipmentLinkageCronTirggerRepositoryService
        extends ServiceImpl<IotLinkageCronTriggerMapper, IotLinkageCronTrigger>
        implements EquipmentLinkageCronTriggerRepositoryService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultEquipmentLinkageCronTirggerRepositoryService.class);

    private IotLinkageCronTriggerMapper cronTriggerMapper;

    private IotLinkageStrategyMapper strategyMapper;

    //修改数据数量为1
    private final static int UPDATE_NUM_EQ_1 = 1;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SaveReturnBo insert(EquipmentLinkageCronTriggerAO vo) throws Exception {
        //1. 创建返回值对象
        SaveReturnBo.SaveReturnBoBuilder saveReturnBoBuilder = SaveReturnBo.builder();
        //2. cron表达式校验 + 联动设备id验证
        Map<String, String> validate = this.validate(vo);
        if (!validate.isEmpty()) {
            saveReturnBoBuilder.validateMap(validate);
            logger.info("[设备联动触发器cron]-[参数校验]-[校验不通过]，{}", JSONObject.toJSONString(validate));
            return saveReturnBoBuilder.build();
        }
        //3. 转换为持久化对象
        IotLinkageCronTrigger entity = IotLinkageCronTrigger.builder()
                .cron(vo.getCron())
                .build();
        // 4. 保存数据到iot_linkage_cron_trigger ， 修改iot_linkage_strategy数据
        boolean isSaveCronTrigger = false;
        try {
            //4.1保存数据到iot_linkage_cron_trigger
            isSaveCronTrigger = this.save(entity);
            //todo 这个修改是否需要设备状态停止？ 没有并发需求不用加锁
            Integer sid = vo.getSid();
            Integer tid = entity.getTid();
            LambdaQueryWrapper<IotLinkageStrategy> queryWrapper = Wrappers.<IotLinkageStrategy>lambdaQuery()
                    .eq(IotLinkageStrategy::getSid, sid).select(IotLinkageStrategy::getTriggerCron, IotLinkageStrategy::getSid);
            //4.2查询iot_linkage_strategy的老数据
            IotLinkageStrategy iotLinkageStrategy = strategyMapper.selectOne(queryWrapper);
            iotLinkageStrategy.setUpdateTime(System.currentTimeMillis() / 1000);
            if (ObjectUtils.isNotEmpty(iotLinkageStrategy)) {
                String oldTriggerCron = iotLinkageStrategy.getTriggerCron();
                String newTriggerCron = StringUtils.isBlank(oldTriggerCron) ? String.valueOf(tid) : oldTriggerCron + "," + tid;
                iotLinkageStrategy.setTriggerCron(newTriggerCron);
                //4.3为iot_linkage_strategy修改新TriggerCron
                strategyMapper.updateById(iotLinkageStrategy);
            }

        } catch (DuplicateKeyException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLSyntaxErrorException(e);
        }
        saveReturnBoBuilder.success(isSaveCronTrigger).msg(ResponseStatus.Insert_Success.getName());
        return saveReturnBoBuilder.build();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public SaveReturnBo update(EquipmentLinkageCronTriggerAO vo) throws Exception {
        //1. 创建返回值对象
        SaveReturnBo.SaveReturnBoBuilder saveReturnBoBuilder = SaveReturnBo.builder();
//        //2. 参数校验
//        Map<String, String> validate = this.validate(vo);
//        if (!validate.isEmpty()) {
//            saveReturnBoBuilder.validateMap(validate);
//            logger.info("[设备联动]-[参数校验]-[校验不通过]，{}", JSONObject.toJSONString(validate));
//            return saveReturnBoBuilder.build();
//        }
//        //3.设备关闭状态校验
//        Integer sId = vo.getSId();
//        Map<String, String> deviceIsTurn = deviceIsTurn(sId);
//        if (!deviceIsTurn.isEmpty()) {
//            saveReturnBoBuilder.validateMap(deviceIsTurn);
//            logger.info("[设备联动]-[设备关闭状态校验]-[校验不通过]，[false]");
//            return saveReturnBoBuilder.build();
//        }
//
//        //4. 转换为持久化对象
//        IotLinkageStrategy entity = IotLinkageStrategy.builder()
//                .sid(vo.getSId())
//                .name(vo.getName())
//                .type(vo.getType())
//                .startTime(vo.getStart())
//                .endTime(vo.getEnd())
//                .updateTime(System.currentTimeMillis() / 1000)
//                .description(vo.getDescription())
//                .build();
//        // 5. 修改数据
//        boolean isUpdate = false;
//        try {
//            isUpdate = this.updateById(entity);
//        } catch (DuplicateKeyException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new SQLSyntaxErrorException(e);
//        }
//        saveReturnBoBuilder.success(isUpdate).msg(ResponseStatus.Update_Success.getName());
        return saveReturnBoBuilder.build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SaveReturnBo remove(Integer sid) throws Exception {
        //1. 创建返回值对象
        SaveReturnBo.SaveReturnBoBuilder saveReturnBoBuilder = SaveReturnBo.builder();
        //2. sid 检验
        Map<String, String> validate = new HashMap<>();
        if (ObjectUtils.isNotEmpty(sid) && !String.valueOf(sid).matches("^[0-9]*$")) {
            validate.put("sId", "参数不合法!");
        }
        if (!validate.isEmpty()) {
            saveReturnBoBuilder.validateMap(validate);
            logger.info("[设备联动]-[参数校验]-[校验不通过]，{}", JSONObject.toJSONString(validate));
            return saveReturnBoBuilder.build();
        }
        //3.设备状态检验
        IotLinkageStrategy oldDeviceInfo = null;
        Map<String, String> deviceIsTurn = deviceIsTurn(sid, oldDeviceInfo);
        if (!deviceIsTurn.isEmpty()) {
            saveReturnBoBuilder.validateMap(deviceIsTurn);
            logger.info("[设备联动]-[设备关闭状态校验]-[校验不通过]，[false]");
            return saveReturnBoBuilder.build();
        }
        try {
            //4.删除数据
            cronTriggerMapper.deleteById(sid);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new SQLSyntaxErrorException(e);
        }
        logger.info("[设备联动]-[删除成功]-[删除的设备信息]，{}，[删除时间]，{}", JSONObject.toJSONString(oldDeviceInfo), System.currentTimeMillis() / 1000);
        saveReturnBoBuilder.success(true).msg(ResponseStatus.Remove_Success.getName());
        return saveReturnBoBuilder.build();
    }

    @Override
    public QueryReturnBo query(EquipmentLinkageQueryAO ao) throws Exception {
        //1. 创建返回值对象
        QueryReturnBo.QueryReturnBoBuilder queryReturnBoBuilder = QueryReturnBo.builder();
        //2.name参数校验
//        String name = ao.getName();
//        Map<String, String> validate = new HashMap<>();
//        if(StringUtils.isNotEmpty(name)){
//            validate = queryFunctionValidateName(name);
//        }
//        if (!validate.isEmpty()) {
//            queryReturnBoBuilder.validateMap(validate);
//            logger.info("[设备联动]-[参数校验]-[校验不通过]，{}", JSONObject.toJSONString(validate));
//            return queryReturnBoBuilder.build();
//        }
//        Integer pageNum = ao.getPageNum();
//        Integer pageSize = ao.getPageSize();
//        Page<IotLinkageStrategy> page = null;
//        try {
//            //name不为空，才按name去查
//            LambdaQueryWrapper<IotLinkageStrategy> queryWrapper = Wrappers.<IotLinkageStrategy>lambdaQuery()
//                    .eq(StringUtils.isNotEmpty(name),IotLinkageStrategy::getName, name);
//            //3.查询数据
//            page = new Page<>(pageNum, pageSize);
//            cronTriggerMapper.selectPage(page, queryWrapper);
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            throw new SQLSyntaxErrorException(e);
//        }
//        List<IotLinkageStrategy> records = page.getRecords();
//        long total = page.getTotal();
//        long ppageSize = page.getSize();
//        long ppageNum = page.getCurrent();
//        queryReturnBoBuilder
//                .success(true)
//                .msg(ResponseStatus.Query_Success.getName())
//                .data(records)
//                .pageSize(ppageSize)
//                .pageNum(ppageNum)
//                .recordsTotal(total);
        return queryReturnBoBuilder.build();
    }

    /**
     * 设备联动规则名称校验 - 不允许重复
     */
    @Override
    public Return validateName(String name) throws Exception {
        // 1. 创建返回值对象
        SimpleReturnBo.SimpleReturnBoBuilder simpleReturnBoBuilder = SimpleReturnBo.builder();
//        // 2. 名称非空校验
//        if (StringUtils.isBlank(name)) {
//            return simpleReturnBoBuilder
//                    .success(false)
//                    .msg(ResponseStatus.Valid_Failed.getName() + ",规则名称" + ResponseStatus.Is_Not_Null.getName())
//                    .build();
//        } else {
//            logger.info("[设备联动]-[新增或修改]-[非空校验通过]");
//        }
//
//        // 3。 名称数据格式校验
//        if (!name.matches("^[\\u4E00-\\u9FA5A-Za-z0-9_]+$") || name.equals("null")) {
//            return simpleReturnBoBuilder
//                    .success(false)
//                    .msg(ResponseStatus.Valid_Failed.getName() + ",规则名称" + ResponseStatus.Is_Not_Null.getName())
//                    .build();
//        } else {
//            logger.info("[设备联动]-[新增或修改]-[数据格式校验通过]");
//        }
//        // 3. 查询数据是否存在重复的规则名称
//        LambdaQueryWrapper<IotLinkageStrategy> queryWrapper = Wrappers.<IotLinkageStrategy>lambdaQuery()
//                .eq(IotLinkageStrategy::getName, name);
//        List<IotLinkageStrategy> iotLinkageStrategies = this.cronTriggerMapper.selectList(queryWrapper);
//        if (iotLinkageStrategies != null && iotLinkageStrategies.size() > 0) {
//            return simpleReturnBoBuilder
//                    .success(false)
//                    .msg(ResponseStatus.Valid_Failed.getName() + ",规则名称存在" + ResponseStatus.Repeat_Data.getName())
//                    .build();
//        } else {
//            logger.info("[设备联动]-[新增或修改]-[数据重复校验通过]");
//        }
        // 4. 校验规则名称
        return simpleReturnBoBuilder
                .success(true)
                .msg(ResponseStatus.Valid_Success.getName())
                .build();
    }

    /**
     * 设备联动规则名称校验 - 查询功能校验
     */
    public Map<String, String> queryFunctionValidateName(String name) throws Exception {
        Map<String, String> map = new ConcurrentHashMap<>();
        // 1. 创建返回值对象
        SimpleReturnBo.SimpleReturnBoBuilder simpleReturnBoBuilder = SimpleReturnBo.builder();
        // 2. 名称数据格式校验
        if (!name.matches("^[\\u4E00-\\u9FA5A-Za-z0-9_]+$")) {
            map.put("name", "规则名称格式有误!");
        }
        return map;
    }


    // cron校验
    protected Map<String, String> validate(EquipmentLinkageCronTriggerAO vo) {
        Map<String, String> map = new ConcurrentHashMap<>();
        String cron = vo.getCron();
        boolean validExpression = CronExpression.isValidExpression(cron);
        if (!validExpression) {
            map.put("cron", "cron不合法");
        }
        Integer sid = vo.getSid();
        if (ObjectUtils.isEmpty(sid)) {
            map.put("sid", "sid不能为空");
        }
        IotLinkageStrategy iotLinkageStrategy = strategyMapper.selectById(sid);
        if (ObjectUtils.isEmpty(iotLinkageStrategy)) {
            map.put("sid", "sid后台找不到");
        }
        return map;
    }

    /**
     * 查询设备是否是开启状态
     *
     * @param sid 设备id
     * @return
     */
    private Map<String, String> deviceIsTurn(Integer sid) {
        Map<String, String> map = new HashMap<>();
//        LambdaQueryWrapper<IotLinkageStrategy> queryWrapper = Wrappers.<IotLinkageStrategy>lambdaQuery();
//        queryWrapper.select(IotLinkageStrategy::getActive).eq(IotLinkageStrategy::getSid, sid);
//        IotLinkageStrategy deviceInfo = this.cronTriggerMapper.selectOne(queryWrapper);
//        if (ObjectUtils.isEmpty(deviceInfo) ) {
//            map.put("active", "设备不存在！");
//            return map;
//        }
//        if(deviceInfo.getActive() == 1){
//            return map;
//        }
//        map.put("active", "设备还处于开启状态！");
        return map;
    }

    /**
     * 查询设备是否是开启状态，并获取设备原本信息
     */
    private Map<String, String> deviceIsTurn(Integer sid, IotLinkageStrategy deviceInfo) {
        Map<String, String> map = new HashMap<>();
//        LambdaQueryWrapper<IotLinkageStrategy> queryWrapper = Wrappers.<IotLinkageStrategy>lambdaQuery();
//        queryWrapper.select(IotLinkageStrategy::getActive).eq(IotLinkageStrategy::getSid, sid);
//        deviceInfo = this.cronTriggerMapper.selectOne(queryWrapper);
//        if (ObjectUtils.isEmpty(deviceInfo) ) {
//            map.put("active", "设备不存在！");
//            return map;
//        }
//        if(deviceInfo.getActive() == 1){
//            return map;
//        }
//        map.put("active", "设备还处于开启状态！");
        return map;
    }

}
