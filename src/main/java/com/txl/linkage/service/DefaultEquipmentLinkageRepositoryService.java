package com.txl.linkage.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txl.linkage.core.ResponseStatus;
import com.txl.linkage.mapper.IotLinkageStrategyMapper;
import com.txl.linkage.model.ao.StrategyAO;
import com.txl.linkage.model.bo.Return;
import com.txl.linkage.model.bo.SaveReturnBo;
import com.txl.linkage.model.bo.SimpleReturnBo;
import com.txl.linkage.model.entity.IotLinkageStrategy;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class DefaultEquipmentLinkageRepositoryService
        extends ServiceImpl<IotLinkageStrategyMapper, IotLinkageStrategy>
        implements EquipmentLinkageRepositoryService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultEquipmentLinkageRepositoryService.class);

    private IotLinkageStrategyMapper strategyMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public SaveReturnBo insert(StrategyAO vo) throws Exception {
        //1. 创建返回值对象
        SaveReturnBo.SaveReturnBoBuilder saveReturnBoBuilder = SaveReturnBo.builder();
        //2. 参数校验
        Map<String, String> validate = this.validate(vo);
        if (!validate.isEmpty()) {
            saveReturnBoBuilder.validateMap(validate);
            logger.info("[设备联动]-[参数校验]-[校验不通过]，{}", JSONObject.toJSONString(validate));
            return saveReturnBoBuilder.build();
        }
        //3. 转换为持久化对象
        IotLinkageStrategy entity = IotLinkageStrategy.builder()
                .name(vo.getName())
                .type(vo.getType())
                .active(1)
                .startTime(vo.getStart())
                .endTime(vo.getEnd())
                .createTime(System.currentTimeMillis() / 1000)
                .updateTime(System.currentTimeMillis() / 1000)
                .description(vo.getDescription())
                .build();
        // 4. 保存数据
        boolean isSave = false;
        try {
            isSave = this.save(entity);
        } catch (RuntimeException e) {

        } catch (Exception e) {
            throw new SQLSyntaxErrorException(e);
        }
        saveReturnBoBuilder.success(isSave).msg(ResponseStatus.Insert_Success.getName());
        return saveReturnBoBuilder.build();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public SaveReturnBo update(StrategyAO vo) throws Exception {
        //1. 创建返回值对象
        SaveReturnBo.SaveReturnBoBuilder saveReturnBoBuilder = SaveReturnBo.builder();
        //2. 参数校验
        Map<String, String> validate = this.validate(vo);
        if (!validate.isEmpty()) {
            saveReturnBoBuilder.validateMap(validate);
            logger.info("[设备联动]-[参数校验]-[校验不通过]，{}", JSONObject.toJSONString(validate));
            return saveReturnBoBuilder.build();
        }
        //3.设备关闭状态校验
        Integer sId = vo.getSId();
        Map<String, String> deviceIsTurn = deviceIsTurn(sId);
        if (!deviceIsTurn.isEmpty()) {
            saveReturnBoBuilder.validateMap(deviceIsTurn);
            logger.info("[设备联动]-[设备关闭状态校验]-[校验不通过]，[false]");
            return saveReturnBoBuilder.build();
        }

        //4. 转换为持久化对象
        IotLinkageStrategy entity = IotLinkageStrategy.builder()
                .sid(vo.getSId())
                .name(vo.getName())
                .type(vo.getType())
                .startTime(vo.getStart())
                .endTime(vo.getEnd())
                .updateTime(System.currentTimeMillis() / 1000)
                .description(vo.getDescription())
                .build();
        // 5. 修改数据
        boolean isUpdate = false;
        try {
            isUpdate = this.updateById(entity);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new SQLSyntaxErrorException(e);
        }
        saveReturnBoBuilder.success(isUpdate).msg(ResponseStatus.Update_Success.getName());
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
        Map<String, String> deviceIsTurn = deviceIsTurn(sid);
        if (!deviceIsTurn.isEmpty()) {
            saveReturnBoBuilder.validateMap(deviceIsTurn);
            logger.info("[设备联动]-[设备关闭状态校验]-[校验不通过]，[false]");
            return saveReturnBoBuilder.build();
        }
        try {
            //4.删除数据
            strategyMapper.deleteById(sid);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new SQLSyntaxErrorException(e);
        }
        saveReturnBoBuilder.success(true).msg(ResponseStatus.Remove_Success.getName());
        return saveReturnBoBuilder.build();
    }

    /**
     * 设备联动规则名称校验 - 不允许重复
     */
    @Override
    public Return validateName(String name) throws Exception {
        // 1. 创建返回值对象
        SimpleReturnBo.SimpleReturnBoBuilder simpleReturnBoBuilder = SimpleReturnBo.builder();
        // 2. 名称非空校验
        if (StringUtils.isBlank(name)) {
            return simpleReturnBoBuilder
                    .success(false)
                    .msg(ResponseStatus.Valid_Failed.getName() + ",规则名称" + ResponseStatus.Is_Not_Null.getName())
                    .build();
        } else {
            logger.info("[设备联动]-[新增或修改]-[非空校验通过]");
        }

        // 3。 名称数据格式校验
        if (!name.matches("^[\\u4E00-\\u9FA5A-Za-z0-9_]+$") || name.equals("null")) {
            return simpleReturnBoBuilder
                    .success(false)
                    .msg(ResponseStatus.Valid_Failed.getName() + ",规则名称" + ResponseStatus.Is_Not_Null.getName())
                    .build();
        } else {
            logger.info("[设备联动]-[新增或修改]-[数据格式校验通过]");
        }
        // 3. 查询数据是否存在重复的规则名称
        LambdaQueryWrapper<IotLinkageStrategy> queryWrapper = Wrappers.<IotLinkageStrategy>lambdaQuery()
                .eq(IotLinkageStrategy::getName, name);
        List<IotLinkageStrategy> iotLinkageStrategies = this.strategyMapper.selectList(queryWrapper);
        if (iotLinkageStrategies != null && iotLinkageStrategies.size() > 0) {
            return simpleReturnBoBuilder
                    .success(false)
                    .msg(ResponseStatus.Valid_Failed.getName() + ",规则名称存在" + ResponseStatus.Repeat_Data.getName())
                    .build();
        } else {
            logger.info("[设备联动]-[新增或修改]-[数据重复校验通过]");
        }
        // 4. 校验规则名称
        return simpleReturnBoBuilder
                .success(true)
                .msg(ResponseStatus.Valid_Success.getName())
                .build();
    }


    // 参数校验
    protected Map<String, String> validate(StrategyAO vo) {
        Map<String, String> map = new ConcurrentHashMap<>();
        Integer sId = vo.getSId();
        String name = vo.getName();
        int type = vo.getType();
        long start = vo.getStart();
        long end = vo.getEnd();
        if (ObjectUtils.isNotEmpty(sId) && !String.valueOf(sId).matches("^[0-9]*$")) {
            map.put("sId", "参数不合法!");
        }
        if (StringUtils.isBlank(name)) {
            map.put("name", "规则名称不能为空!");
        }
        if (type != 0 && type != 1) {
            map.put("type", "参数不合法!");
        }
        long now = System.currentTimeMillis() / 1000;
        if (start <= 0L || end <= 0L) {
            map.put("start", "有效期开始时间或结束时间参数不合法!");
        }
        if (end <= now) {
            map.put("end", "规则有效期结束时间不能为过去式!");
        }
        if (start >= end) {
            map.put("start", "有效期开始时间参数不合法，比结束时间晚！");
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
        LambdaQueryWrapper<IotLinkageStrategy> queryWrapper = Wrappers.<IotLinkageStrategy>lambdaQuery();
        queryWrapper.select(IotLinkageStrategy::getActive).eq(IotLinkageStrategy::getSid, sid);
        IotLinkageStrategy deviceInfo = this.strategyMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(deviceInfo) && deviceInfo.getActive() == 1) {
            return map;
        }
        map.put("active", "设备还处于开启状态！");
        return map;
    }


}
