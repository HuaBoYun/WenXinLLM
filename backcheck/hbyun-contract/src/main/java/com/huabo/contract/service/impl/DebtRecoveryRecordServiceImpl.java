package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.contract.entity.DebtRecoveryRecord;
import com.huabo.contract.mapper.DebtRecoveryRecordMapper;
import com.huabo.contract.service.DebtRecoveryRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 债权回收登记记录服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DebtRecoveryRecordServiceImpl extends ServiceImpl<DebtRecoveryRecordMapper, DebtRecoveryRecord> implements DebtRecoveryRecordService {

    @Autowired
    private DebtRecoveryRecordMapper debtRecoveryRecordMapper;

    @Override
    public List<DebtRecoveryRecord> getRecoveryRecordsByDebtId(Long debtId) {
        log.info("根据债权ID查询回收记录列表，债权ID：{}", debtId);
        return debtRecoveryRecordMapper.selectByDebtId(debtId);
    }

    @Override
    public boolean saveRecoveryRecord(DebtRecoveryRecord recoveryRecord) {
        log.info("保存回收记录：{}", recoveryRecord);
        try {
            if (recoveryRecord.getId() == null) {
                // 新增
                return this.save(recoveryRecord);
            } else {
                // 更新
                return this.updateById(recoveryRecord);
            }
        } catch (Exception e) {
            log.error("保存回收记录失败", e);
            throw new RuntimeException("保存回收记录失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchSaveRecoveryRecords(List<DebtRecoveryRecord> recoveryRecords) {
        log.info("批量保存回收记录，数量：{}", recoveryRecords.size());
        try {
            return this.saveOrUpdateBatch(recoveryRecords);
        } catch (Exception e) {
            log.error("批量保存回收记录失败", e);
            throw new RuntimeException("批量保存回收记录失败：" + e.getMessage());
        }
    }

    @Override
    public boolean deleteRecoveryRecord(Long id) {
        log.info("删除回收记录，ID：{}", id);
        try {
            return this.removeById(id);
        } catch (Exception e) {
            log.error("删除回收记录失败", e);
            throw new RuntimeException("删除回收记录失败：" + e.getMessage());
        }
    }

    @Override
    public DebtRecoveryRecord getRecoveryRecordById(Long id) {
        log.info("根据ID获取回收记录详情，ID：{}", id);
        return this.getById(id);
    }

    @Override
    public List<DebtRecoveryRecord> getRecoveryRecordsByPersonId(Long recoveryPersonId) {
        log.info("根据回收人员ID查询回收记录列表，回收人员ID：{}", recoveryPersonId);
        return debtRecoveryRecordMapper.selectByRecoveryPersonId(recoveryPersonId);
    }

    @Override
    public List<DebtRecoveryRecord> getRecoveryRecordsByMethod(Integer recoveryMethod) {
        log.info("根据回收方式查询回收记录列表，回收方式：{}", recoveryMethod);
        return debtRecoveryRecordMapper.selectByRecoveryMethod(recoveryMethod);
    }

    @Override
    public BigDecimal getTotalRecoveryAmountByDebtId(Long debtId) {
        log.info("根据债权ID计算总回收金额，债权ID：{}", debtId);
        return debtRecoveryRecordMapper.getTotalRecoveryAmountByDebtId(debtId);
    }

    @Override
    public Map<String, Object> getRecoverySummary(Long debtId) {
        log.info("获取债权回收统计信息，债权ID：{}", debtId);
        try {
            Map<String, Object> summary = debtRecoveryRecordMapper.getRecoverySummary(debtId);
            if (summary == null) {
                summary = new HashMap<>();
                summary.put("totalRecoveries", 0);
                summary.put("totalRecoveredAmount", BigDecimal.ZERO);
                summary.put("averageRecoveryAmount", BigDecimal.ZERO);
                summary.put("maxSingleRecovery", BigDecimal.ZERO);
            }
            
            // 计算回收进度（需要债权总金额，这里模拟为80%）
            summary.put("recoveryProgress", 80);
            
            // 计算回收效率（基于回收进度）
            Integer recoveryProgress = (Integer) summary.get("recoveryProgress");
            if (recoveryProgress == null) recoveryProgress = 0;
            summary.put("recoveryEfficiency", recoveryProgress);
            
            return summary;
        } catch (Exception e) {
            log.error("获取债权回收统计信息失败", e);
            throw new RuntimeException("获取债权回收统计信息失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getRecoveryData(Long debtId) {
        log.info("获取回收数据，债权ID：{}", debtId);
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 获取回收记录列表
            List<DebtRecoveryRecord> records = this.getRecoveryRecordsByDebtId(debtId);
            result.put("records", records);
            
            // 获取回收统计信息
            Map<String, Object> summary = this.getRecoverySummary(debtId);
            result.put("summary", summary);
            
            return result;
        } catch (Exception e) {
            log.error("获取回收数据失败", e);
            throw new RuntimeException("获取回收数据失败：" + e.getMessage());
        }
    }
}
