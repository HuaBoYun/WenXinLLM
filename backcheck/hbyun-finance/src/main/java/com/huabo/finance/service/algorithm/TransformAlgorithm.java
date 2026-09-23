package com.huabo.finance.service.algorithm;

import java.util.Map;

/**
 * 数据转化算法接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface TransformAlgorithm {

    /**
     * 获取算法编码
     * 
     * @return 算法编码
     */
    String getAlgorithmCode();

    /**
     * 获取算法名称
     * 
     * @return 算法名称
     */
    String getAlgorithmName();

    /**
     * 执行算法
     * 
     * @param params 算法参数(JSON格式解析后的Map)
     * @param taskId 转化任务ID
     * @throws Exception 执行异常
     */
    void execute(Map<String, Object> params, String taskId) throws Exception;

    /**
     * 验证算法参数
     * 
     * @param params 算法参数
     * @return 验证结果,true-通过,false-不通过
     */
    boolean validateParams(Map<String, Object> params);
}

