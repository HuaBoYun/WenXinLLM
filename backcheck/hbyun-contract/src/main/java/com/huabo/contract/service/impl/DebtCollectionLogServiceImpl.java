package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.contract.entity.DebtCollectionLog;
import com.huabo.contract.mapper.DebtCollectionLogMapper;
import com.huabo.contract.service.DebtCollectionLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 债权催收记录服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DebtCollectionLogServiceImpl extends ServiceImpl<DebtCollectionLogMapper, DebtCollectionLog> implements DebtCollectionLogService {

    @Autowired
    private DebtCollectionLogMapper debtCollectionLogMapper;

    @Override
    public List<DebtCollectionLog> getCollectionLogsByDebtId(Long debtId) {
        log.info("根据债权ID查询催收记录列表，债权ID：{}", debtId);
        return debtCollectionLogMapper.selectByDebtId(debtId);
    }

    @Override
    public boolean saveCollectionLog(DebtCollectionLog collectionLog) {
        log.info("保存催收记录：{}", collectionLog);
        try {
            if (collectionLog.getId() == null) {
                // 新增
                return this.save(collectionLog);
            } else {
                // 更新
                return this.updateById(collectionLog);
            }
        } catch (Exception e) {
            log.error("保存催收记录失败", e);
            throw new RuntimeException("保存催收记录失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchSaveCollectionLogs(List<DebtCollectionLog> collectionLogs) {
        log.info("批量保存催收记录，数量：{}", collectionLogs.size());
        try {
            return this.saveOrUpdateBatch(collectionLogs);
        } catch (Exception e) {
            log.error("批量保存催收记录失败", e);
            throw new RuntimeException("批量保存催收记录失败：" + e.getMessage());
        }
    }

    @Override
    public boolean deleteCollectionLog(Long id) {
        log.info("删除催收记录，ID：{}", id);
        try {
            return this.removeById(id);
        } catch (Exception e) {
            log.error("删除催收记录失败", e);
            throw new RuntimeException("删除催收记录失败：" + e.getMessage());
        }
    }

    @Override
    public DebtCollectionLog getCollectionLogById(Long id) {
        log.info("根据ID获取催收记录详情，ID：{}", id);
        return this.getById(id);
    }

    @Override
    public List<DebtCollectionLog> getCollectionLogsByCollectorId(Long collectorId) {
        log.info("根据催收人员ID查询催收记录列表，催收人员ID：{}", collectorId);
        return debtCollectionLogMapper.selectByCollectorId(collectorId);
    }

    @Override
    public List<DebtCollectionLog> getCollectionLogsByMethod(Integer collectionMethod) {
        log.info("根据催收方式查询催收记录列表，催收方式：{}", collectionMethod);
        return debtCollectionLogMapper.selectByCollectionMethod(collectionMethod);
    }

    @Override
    public List<DebtCollectionLog> getCollectionLogsByResult(Integer collectionResult) {
        log.info("根据催收结果查询催收记录列表，催收结果：{}", collectionResult);
        return debtCollectionLogMapper.selectByCollectionResult(collectionResult);
    }

    @Override
    public Map<String, Object> getCollectionSummary(Long debtId) {
        log.info("获取债权催收统计信息，债权ID：{}", debtId);
        try {
            Map<String, Object> summary = debtCollectionLogMapper.getCollectionSummary(debtId);
            if (summary == null) {
                summary = new HashMap<>();
                summary.put("totalCollections", 0);
                summary.put("successfulCollections", 0);
                summary.put("totalPromisedAmount", 0);
                summary.put("successRate", 0);
            }
            
            // 计算催收效果评分（基于成功率和承诺金额）
            Double successRate = (Double) summary.get("successRate");
            if (successRate == null) successRate = 0.0;
            
            int effectivenessScore = (int) Math.min(100, successRate + 20); // 简单的评分算法
            summary.put("effectivenessScore", effectivenessScore);
            
            // 计算平均响应时间（模拟数据，实际应根据业务逻辑计算）
            summary.put("averageResponseTime", 3);
            
            return summary;
        } catch (Exception e) {
            log.error("获取债权催收统计信息失败", e);
            throw new RuntimeException("获取债权催收统计信息失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getCollectionData(Long debtId) {
        log.info("获取催收数据，债权ID：{}", debtId);
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 获取催收记录列表
            List<DebtCollectionLog> records = this.getCollectionLogsByDebtId(debtId);
            result.put("records", records);
            
            // 获取催收统计信息
            Map<String, Object> summary = this.getCollectionSummary(debtId);
            result.put("summary", summary);
            
            return result;
        } catch (Exception e) {
            log.error("获取催收数据失败", e);
            throw new RuntimeException("获取催收数据失败：" + e.getMessage());
        }
    }
}
