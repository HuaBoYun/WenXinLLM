package com.huabo.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.finance.entity.TransformAlgorithmConfig;
import com.huabo.finance.mapper.TransformAlgorithmConfigMapper;
import com.huabo.finance.service.ITransformAlgorithmConfigService;
import com.huabo.finance.service.algorithm.TransformAlgorithm;
import com.huabo.finance.service.algorithm.TransformAlgorithmEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 算法配置Service实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Service
public class TransformAlgorithmConfigServiceImpl extends ServiceImpl<TransformAlgorithmConfigMapper, TransformAlgorithmConfig> 
        implements ITransformAlgorithmConfigService {

    @Autowired
    private TransformAlgorithmConfigMapper algorithmConfigMapper;

    @Autowired
    private TransformAlgorithmEngine algorithmEngine;

    /**
     * 保存算法配置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveConfigs(List<TransformAlgorithmConfig> configs, String transformTaskId, String createUser) {
        try {
            log.info("开始保存算法配置, transformTaskId={}, 配置数量={}", transformTaskId, configs.size());

            // 1. 删除该任务的旧算法配置
            QueryWrapper<TransformAlgorithmConfig> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("TRANSFORM_TASK_ID", transformTaskId);
            algorithmConfigMapper.delete(deleteWrapper);

            // 2. 批量插入新算法配置
            for (TransformAlgorithmConfig config : configs) {
                config.setConfigId(UUID.randomUUID().toString().replace("-", ""));
                config.setTransformTaskId(transformTaskId);
                config.setCreateTime(new Date());
                config.setUpdateTime(new Date());
                
                algorithmConfigMapper.insert(config);
            }

            log.info("算法配置保存成功, transformTaskId={}", transformTaskId);
            return ResponseFormat.retParam(1, "保存成功", null);

        } catch (Exception e) {
            log.error("保存算法配置失败", e);
            return ResponseFormat.retParam(0, "保存失败: " + e.getMessage(), null);
        }
    }

    /**
     * 查询算法配置列表
     */
    @Override
    public JsonBean getConfigList(String transformTaskId) {
        try {
            log.info("查询算法配置列表, transformTaskId={}", transformTaskId);

            List<TransformAlgorithmConfig> configs = algorithmConfigMapper.selectByTransformTaskId(transformTaskId);

            log.info("查询到{}条算法配置", configs.size());
            return ResponseFormat.retParam(1, 200, configs);

        } catch (Exception e) {
            log.error("查询算法配置列表失败", e);
            return ResponseFormat.retParam(0, "查询失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取所有可用算法列表
     */
    @Override
    public JsonBean getAvailableAlgorithms() {
        try {
            log.info("查询所有可用算法");

            List<TransformAlgorithm> algorithms = algorithmEngine.getAllAlgorithms();
            
            List<Map<String, Object>> algorithmList = new ArrayList<>();
            for (TransformAlgorithm algorithm : algorithms) {
                Map<String, Object> algorithmInfo = new HashMap<>();
                algorithmInfo.put("algorithmCode", algorithm.getAlgorithmCode());
                algorithmInfo.put("algorithmName", algorithm.getAlgorithmName());
                algorithmList.add(algorithmInfo);
            }

            log.info("查询到{}个可用算法", algorithmList.size());
            return ResponseFormat.retParam(1, 200, algorithmList);

        } catch (Exception e) {
            log.error("查询可用算法失败", e);
            return ResponseFormat.retParam(0, "查询失败: " + e.getMessage(), null);
        }
    }

    /**
     * 删除算法配置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean deleteConfig(String configId) {
        try {
            log.info("删除算法配置, configId={}", configId);

            algorithmConfigMapper.deleteById(configId);

            log.info("算法配置删除成功, configId={}", configId);
            return ResponseFormat.retParam(1, "删除成功", null);

        } catch (Exception e) {
            log.error("删除算法配置失败", e);
            return ResponseFormat.retParam(0, "删除失败: " + e.getMessage(), null);
        }
    }
}

