package com.hbfk.service.impl;

import com.hbfk.mapper.ModelCombinationExecutionMapper;
import com.hbfk.service.ModelCombinationExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 模型组合执行结果服务实现类
 * 
 * @author AI Assistant
 * @date 2025-01-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ModelCombinationExecutionServiceImpl implements ModelCombinationExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(ModelCombinationExecutionServiceImpl.class);

    @Autowired
    private ModelCombinationExecutionMapper executionMapper;

    @Override
    public int clearExecutionResultsByCombinationId(String combinationId) {
        try {
            logger.info("开始清理组合执行结果, combinationId: {}", combinationId);
            
            // 先查询要删除的记录数
            int countBefore = executionMapper.countExecutionResultsByCombinationId(combinationId);
            logger.info("组合 {} 当前有 {} 条执行结果记录", combinationId, countBefore);
            
            if (countBefore == 0) {
                logger.info("组合 {} 没有执行结果记录，无需清理", combinationId);
                return 0;
            }
            
            // 执行删除操作
            int deletedCount = executionMapper.deleteExecutionResultsByCombinationId(combinationId);
            
            logger.info("组合 {} 执行结果清理完成，删除了 {} 条记录", combinationId, deletedCount);
            
            return deletedCount;
            
        } catch (Exception e) {
            logger.error("清理组合执行结果失败, combinationId: {}", combinationId, e);
            throw new RuntimeException("清理组合执行结果失败: " + e.getMessage(), e);
        }
    }

    @Override
    public int clearExecutionResultsByConfigId(String configId) {
        try {
            logger.info("开始清理配置执行结果, configId: {}", configId);

            // 先查询要删除的记录数
            int countBefore = executionMapper.countExecutionResultsByConfigId(configId);
            logger.info("配置 {} 当前有 {} 条执行结果记录", configId, countBefore);

            if (countBefore == 0) {
                logger.info("配置 {} 没有执行结果记录，无需清理", configId);
                return 0;
            }

            // 执行删除操作
            int deletedCount = executionMapper.deleteExecutionResultsByConfigId(configId);

            logger.info("配置 {} 执行结果清理完成，删除了 {} 条记录", configId, deletedCount);

            return deletedCount;

        } catch (Exception e) {
            logger.error("清理配置执行结果失败, configId: {}", configId, e);
            throw new RuntimeException("清理配置执行结果失败: " + e.getMessage(), e);
        }
    }

    @Override
    public int clearAllExecutionResults() {
        try {
            logger.warn("开始清理所有执行结果数据");

            // 先查询总记录数
            int countBefore = executionMapper.countAllExecutionResults();
            logger.warn("当前共有 {} 条执行结果记录", countBefore);

            if (countBefore == 0) {
                logger.warn("没有执行结果记录，无需清理");
                return 0;
            }

            // 执行删除操作
            int deletedCount = executionMapper.deleteAllExecutionResults();

            logger.warn("所有执行结果清理完成，删除了 {} 条记录", deletedCount);

            return deletedCount;

        } catch (Exception e) {
            logger.error("清理所有执行结果失败", e);
            throw new RuntimeException("清理所有执行结果失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getExecutionResultStatistics(String combinationId) {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            if (combinationId != null && !combinationId.trim().isEmpty()) {
                // 查询指定组合的统计信息
                int count = executionMapper.countExecutionResultsByCombinationId(combinationId);
                statistics.put("combinationId", combinationId);
                statistics.put("executionResultCount", count);
                
                logger.info("组合 {} 的执行结果统计: {} 条记录", combinationId, count);
            } else {
                // 查询所有执行结果的统计信息
                int totalCount = executionMapper.countAllExecutionResults();
                Map<String, Integer> countByCombination = executionMapper.countExecutionResultsGroupByCombination();
                
                statistics.put("totalExecutionResultCount", totalCount);
                statistics.put("combinationCount", countByCombination.size());
                statistics.put("countByCombination", countByCombination);
                
                logger.info("所有执行结果统计: 总计 {} 条记录，涉及 {} 个组合", totalCount, countByCombination.size());
            }
            
            return statistics;

        } catch (Exception e) {
            logger.error("获取执行结果统计失败, combinationId: {}", combinationId, e);
            throw new RuntimeException("获取执行结果统计失败: " + e.getMessage(), e);
        }
    }

    @Override
    public int deleteExecutionRecord(String executionId) {
        try {
            logger.info("开始删除执行记录, executionId: {}", executionId);

            // 检查执行记录是否存在
            int recordCount = executionMapper.countExecutionRecord(executionId);
            if (recordCount == 0) {
                logger.warn("执行记录不存在, executionId: {}", executionId);
                throw new RuntimeException("执行记录不存在");
            }

            // 先删除执行结果数据
            int resultDeletedCount = executionMapper.deleteExecutionResultsByExecutionId(executionId);
            logger.info("删除执行结果数据完成, executionId: {}, 删除记录数: {}", executionId, resultDeletedCount);

            // 再删除执行记录
            int recordDeletedCount = executionMapper.deleteExecutionRecord(executionId);
            logger.info("删除执行记录完成, executionId: {}, 删除记录数: {}", executionId, recordDeletedCount);

            return recordDeletedCount;

        } catch (Exception e) {
            logger.error("删除执行记录失败, executionId: {}", executionId, e);
            throw new RuntimeException("删除执行记录失败: " + e.getMessage(), e);
        }
    }
}
