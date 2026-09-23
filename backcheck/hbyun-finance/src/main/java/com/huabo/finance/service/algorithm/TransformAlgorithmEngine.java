package com.huabo.finance.service.algorithm;

import com.alibaba.fastjson.JSON;
import com.huabo.finance.entity.TransformAlgorithmConfig;
import com.huabo.finance.entity.TransformLog;
import com.huabo.finance.mapper.TransformAlgorithmConfigMapper;
import com.huabo.finance.mapper.TransformLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 数据转化算法引擎
 * 
 * 功能说明:
 * 1. 管理所有转化算法
 * 2. 根据配置动态调用算法
 * 3. 记录算法执行日志
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Service
public class TransformAlgorithmEngine {

    @Autowired
    private TransformAlgorithmConfigMapper algorithmConfigMapper;

    @Autowired
    private TransformLogMapper transformLogMapper;

    @Autowired(required = false)
    private List<TransformAlgorithm> algorithms;

    /**
     * 算法注册表: algorithmCode -> TransformAlgorithm
     */
    private Map<String, TransformAlgorithm> algorithmRegistry = new HashMap<>();

    /**
     * 初始化算法注册表
     */
    @PostConstruct
    public void init() {
        if (algorithms != null && !algorithms.isEmpty()) {
            for (TransformAlgorithm algorithm : algorithms) {
                algorithmRegistry.put(algorithm.getAlgorithmCode(), algorithm);
                log.info("注册转化算法: {} - {}", algorithm.getAlgorithmCode(), algorithm.getAlgorithmName());
            }
        }
        log.info("算法引擎初始化完成, 共注册{}个算法", algorithmRegistry.size());
    }

    /**
     * 执行转化任务的所有算法
     * 
     * @param transformTaskId 转化任务ID
     * @throws Exception 执行异常
     */
    public void executeAlgorithms(String transformTaskId) throws Exception {
        log.info("开始执行转化任务的算法, transformTaskId={}", transformTaskId);

        // 查询该任务的所有启用的算法配置
        List<TransformAlgorithmConfig> configs = algorithmConfigMapper.selectByTransformTaskId(transformTaskId);
        
        if (configs == null || configs.isEmpty()) {
            log.info("转化任务没有配置算法, transformTaskId={}", transformTaskId);
            return;
        }

        log.info("转化任务共配置{}个算法", configs.size());

        // 按执行顺序执行算法
        for (TransformAlgorithmConfig config : configs) {
            executeAlgorithm(config, transformTaskId);
        }

        log.info("转化任务的所有算法执行完成, transformTaskId={}", transformTaskId);
    }

    /**
     * 执行单个算法
     * 
     * @param config 算法配置
     * @param transformTaskId 转化任务ID
     * @throws Exception 执行异常
     */
    private void executeAlgorithm(TransformAlgorithmConfig config, String transformTaskId) throws Exception {
        String algorithmCode = config.getAlgorithmCode();
        String algorithmName = config.getAlgorithmName();

        log.info("开始执行算法: {} - {}", algorithmCode, algorithmName);

        // 记录开始日志
        saveLog(transformTaskId, "INFO", "ALGORITHM", 
                "开始执行算法: " + algorithmName, 
                "算法编码: " + algorithmCode, null, null, null);

        try {
            // 获取算法实现
            TransformAlgorithm algorithm = algorithmRegistry.get(algorithmCode);
            if (algorithm == null) {
                String errorMsg = "未找到算法实现: " + algorithmCode;
                log.error(errorMsg);
                saveLog(transformTaskId, "ERROR", "ALGORITHM", errorMsg, null, null, null, null);
                throw new RuntimeException(errorMsg);
            }

            // 解析算法参数
            Map<String, Object> params = parseParams(config.getAlgorithmParams());

            // 验证参数
            if (!algorithm.validateParams(params)) {
                String errorMsg = "算法参数验证失败: " + algorithmCode;
                log.error(errorMsg);
                saveLog(transformTaskId, "ERROR", "ALGORITHM", errorMsg, 
                        "参数: " + config.getAlgorithmParams(), null, null, null);
                throw new RuntimeException(errorMsg);
            }

            // 执行算法
            long startTime = System.currentTimeMillis();
            algorithm.execute(params, transformTaskId);
            long elapsedTime = System.currentTimeMillis() - startTime;

            // 记录成功日志
            saveLog(transformTaskId, "INFO", "ALGORITHM", 
                    "算法执行成功: " + algorithmName, 
                    "耗时: " + elapsedTime + "ms", null, null, null);

            log.info("算法执行成功: {} - {}, 耗时: {}ms", algorithmCode, algorithmName, elapsedTime);

        } catch (Exception e) {
            log.error("算法执行失败: {} - {}", algorithmCode, algorithmName, e);
            
            // 记录错误日志
            saveLog(transformTaskId, "ERROR", "ALGORITHM", 
                    "算法执行失败: " + algorithmName, 
                    "错误信息: " + e.getMessage(), null, null, null);
            
            throw e;
        }
    }

    /**
     * 解析算法参数(JSON格式)
     * 
     * @param paramsJson 参数JSON字符串
     * @return 参数Map
     */
    private Map<String, Object> parseParams(String paramsJson) {
        if (paramsJson == null || paramsJson.trim().isEmpty()) {
            return new HashMap<>();
        }

        try {
            return JSON.parseObject(paramsJson, Map.class);
        } catch (Exception e) {
            log.error("解析算法参数失败: {}", paramsJson, e);
            return new HashMap<>();
        }
    }

    /**
     * 保存转化日志
     * 
     * @param taskId 任务ID
     * @param logLevel 日志级别
     * @param logType 日志类型
     * @param logMessage 日志消息
     * @param logDetail 日志详情
     * @param sourceTable 源表名
     * @param targetTable 目标表名
     * @param recordCount 记录数
     */
    private void saveLog(String taskId, String logLevel, String logType, 
                        String logMessage, String logDetail, 
                        String sourceTable, String targetTable, Integer recordCount) {
        try {
            TransformLog log = new TransformLog();
            log.setLogId(UUID.randomUUID().toString().replace("-", ""));
            log.setTaskId(taskId);
            log.setLogLevel(logLevel);
            log.setLogType(logType);
            log.setLogMessage(logMessage);
            log.setLogDetail(logDetail);
            log.setSourceTable(sourceTable);
            log.setTargetTable(targetTable);
            log.setRecordCount(recordCount);
            log.setCreateTime(new Date());

            transformLogMapper.insert(log);
        } catch (Exception e) {
            // 日志保存失败不影响主流程
            this.log.error("保存转化日志失败", e);
        }
    }

    /**
     * 获取所有已注册的算法
     * 
     * @return 算法列表
     */
    public List<TransformAlgorithm> getAllAlgorithms() {
        return new ArrayList<>(algorithmRegistry.values());
    }

    /**
     * 根据算法编码获取算法
     * 
     * @param algorithmCode 算法编码
     * @return 算法实现
     */
    public TransformAlgorithm getAlgorithm(String algorithmCode) {
        return algorithmRegistry.get(algorithmCode);
    }
}

