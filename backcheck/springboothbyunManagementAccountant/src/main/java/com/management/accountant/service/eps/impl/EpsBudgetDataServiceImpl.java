package com.management.accountant.service.eps.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.eps.EpsBudgetData;
import com.management.accountant.mapper.eps.EpsBudgetDataMapper;
import com.management.accountant.service.eps.EpsBudgetDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 预算数据服务实现
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Service
public class EpsBudgetDataServiceImpl implements EpsBudgetDataService {

    @Autowired
    private EpsBudgetDataMapper budgetDataMapper;

    @Override
    public IPage<EpsBudgetData> queryBudgetDataPage(Long current, Long size, Long versionId, 
                                                   Long subjectId, Long organizationId, 
                                                   String budgetPeriod, String dataStatus) {
        Page<EpsBudgetData> page = new Page<>(current, size);
        QueryWrapper<EpsBudgetData> queryWrapper = new QueryWrapper<>();
        
        if (versionId != null) {
            queryWrapper.eq("version_id", versionId);
        }
        if (subjectId != null) {
            queryWrapper.eq("subject_id", subjectId);
        }
        if (organizationId != null) {
            queryWrapper.eq("organization_id", organizationId);
        }
        if (budgetPeriod != null && !budgetPeriod.isEmpty()) {
            queryWrapper.eq("budget_period", budgetPeriod);
        }
        if (dataStatus != null && !dataStatus.isEmpty()) {
            queryWrapper.eq("data_status", dataStatus);
        }
        
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByDesc("created_time");
        
        return budgetDataMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createBudgetData(EpsBudgetData budgetData) {
        try {
            // 验证数据唯一性
            if (checkDataExists(budgetData.getVersionId(), budgetData.getSubjectId(), 
                               budgetData.getOrganizationId(), budgetData.getBudgetPeriod())) {
                throw new RuntimeException("该组合的预算数据已存在");
            }
            
            // 设置默认值
            budgetData.setCreatedTime(LocalDateTime.now());
            budgetData.setUpdatedTime(LocalDateTime.now());
            budgetData.setDeleted(0);
            
            if (budgetData.getDataStatus() == null) {
                budgetData.setDataStatus("DRAFT");
            }
            
            if (budgetData.getIsLocked() == null) {
                budgetData.setIsLocked(false);
            }
            
            if (budgetData.getBudgetAmount() == null) {
                budgetData.setBudgetAmount(BigDecimal.ZERO);
            }
            
            if (budgetData.getActualAmount() == null) {
                budgetData.setActualAmount(BigDecimal.ZERO);
            }
            
            if (budgetData.getVarianceAmount() == null) {
                budgetData.setVarianceAmount(BigDecimal.ZERO);
            }
            
            return budgetDataMapper.insert(budgetData) > 0;
        } catch (Exception e) {
            log.error("创建预算数据失败", e);
            throw new RuntimeException("创建失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBudgetData(EpsBudgetData budgetData) {
        try {
            // 检查数据是否被锁定
            EpsBudgetData existingData = getBudgetDataById(budgetData.getDataId());
            if (existingData != null && Boolean.TRUE.equals(existingData.getIsLocked())) {
                throw new RuntimeException("数据已被锁定，无法修改");
            }
            
            // 计算差异
            if (budgetData.getBudgetAmount() != null && budgetData.getActualAmount() != null) {
                BigDecimal variance = budgetData.getBudgetAmount().subtract(budgetData.getActualAmount());
                budgetData.setVarianceAmount(variance);
            }
            
            budgetData.setUpdatedTime(LocalDateTime.now());
            return budgetDataMapper.updateById(budgetData) > 0;
        } catch (Exception e) {
            log.error("更新预算数据失败", e);
            throw new RuntimeException("更新失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBudgetData(Long dataId) {
        try {
            // 检查数据是否被锁定
            EpsBudgetData existingData = getBudgetDataById(dataId);
            if (existingData != null && Boolean.TRUE.equals(existingData.getIsLocked())) {
                throw new RuntimeException("数据已被锁定，无法删除");
            }
            
            // 检查数据状态
            if (existingData != null && "APPROVED".equals(existingData.getDataStatus())) {
                throw new RuntimeException("已审批的数据不能删除");
            }
            
            EpsBudgetData budgetData = new EpsBudgetData();
            budgetData.setDataId(dataId);
            budgetData.setDeleted(1);
            budgetData.setUpdatedTime(LocalDateTime.now());
            return budgetDataMapper.updateById(budgetData) > 0;
        } catch (Exception e) {
            log.error("删除预算数据失败", e);
            throw new RuntimeException("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteBudgetData(List<Long> dataIds) {
        try {
            int successCount = 0;
            for (Long dataId : dataIds) {
                if (deleteBudgetData(dataId)) {
                    successCount++;
                }
            }
            return successCount == dataIds.size();
        } catch (Exception e) {
            log.error("批量删除预算数据失败", e);
            throw new RuntimeException("批量删除失败: " + e.getMessage());
        }
    }

    @Override
    public EpsBudgetData getBudgetDataById(Long dataId) {
        return budgetDataMapper.selectById(dataId);
    }

    @Override
    public List<EpsBudgetData> getBudgetDataByVersionId(Long versionId) {
        QueryWrapper<EpsBudgetData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("version_id", versionId);
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByAsc("subject_id", "organization_id", "budget_period");
        return budgetDataMapper.selectList(queryWrapper);
    }

    @Override
    public List<EpsBudgetData> getBudgetDataBySubjectId(Long subjectId, Long versionId) {
        QueryWrapper<EpsBudgetData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_id", subjectId);
        if (versionId != null) {
            queryWrapper.eq("version_id", versionId);
        }
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByAsc("organization_id", "budget_period");
        return budgetDataMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, Object> getBudgetDataMatrix(Long versionId, Long organizationId, String budgetPeriod) {
        Map<String, Object> matrix = new HashMap<>();
        
        try {
            QueryWrapper<EpsBudgetData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("version_id", versionId);
            if (organizationId != null) {
                queryWrapper.eq("organization_id", organizationId);
            }
            if (budgetPeriod != null && !budgetPeriod.isEmpty()) {
                queryWrapper.eq("budget_period", budgetPeriod);
            }
            queryWrapper.eq("deleted", 0);
            
            List<EpsBudgetData> dataList = budgetDataMapper.selectList(queryWrapper);
            
            // 构建矩阵结构
            Map<String, Map<String, Map<String, EpsBudgetData>>> matrixData = new HashMap<>();
            
            for (EpsBudgetData data : dataList) {
                String subjectKey = String.valueOf(data.getSubjectId());
                String orgKey = String.valueOf(data.getOrganizationId());
                String periodKey = data.getBudgetPeriod();
                
                matrixData.computeIfAbsent(subjectKey, k -> new HashMap<>())
                         .computeIfAbsent(orgKey, k -> new HashMap<>())
                         .put(periodKey, data);
            }
            
            matrix.put("matrixData", matrixData);
            matrix.put("totalRecords", dataList.size());
            matrix.put("generateTime", LocalDateTime.now());
            
        } catch (Exception e) {
            log.error("获取预算数据矩阵失败", e);
            matrix.put("error", e.getMessage());
        }
        
        return matrix;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchSaveBudgetData(List<EpsBudgetData> budgetDataList) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int successCount = 0;
            int failCount = 0;
            List<String> errors = new ArrayList<>();
            
            for (EpsBudgetData budgetData : budgetDataList) {
                try {
                    if (budgetData.getDataId() != null) {
                        // 更新
                        if (updateBudgetData(budgetData)) {
                            successCount++;
                        } else {
                            failCount++;
                        }
                    } else {
                        // 创建
                        if (createBudgetData(budgetData)) {
                            successCount++;
                        } else {
                            failCount++;
                        }
                    }
                } catch (Exception e) {
                    failCount++;
                    errors.add("数据保存失败: " + e.getMessage());
                }
            }
            
            result.put("success", true);
            result.put("totalCount", budgetDataList.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);
            
        } catch (Exception e) {
            log.error("批量保存预算数据失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lockBudgetData(Long dataId, String lockReason) {
        try {
            EpsBudgetData budgetData = new EpsBudgetData();
            budgetData.setDataId(dataId);
            budgetData.setIsLocked(true);
            budgetData.setLockedTime(LocalDateTime.now());
            budgetData.setUpdatedTime(LocalDateTime.now());
            
            return budgetDataMapper.updateById(budgetData) > 0;
        } catch (Exception e) {
            log.error("锁定预算数据失败", e);
            throw new RuntimeException("锁定失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlockBudgetData(Long dataId) {
        try {
            EpsBudgetData budgetData = new EpsBudgetData();
            budgetData.setDataId(dataId);
            budgetData.setIsLocked(false);
            budgetData.setLockedBy(null);
            budgetData.setLockedByName(null);
            budgetData.setLockedTime(null);
            budgetData.setUpdatedTime(LocalDateTime.now());
            
            return budgetDataMapper.updateById(budgetData) > 0;
        } catch (Exception e) {
            log.error("解锁预算数据失败", e);
            throw new RuntimeException("解锁失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getBudgetDataStatistics(Long versionId, Long organizationId, String budgetPeriod) {
        Map<String, Object> statistics = new HashMap<>();
        
        QueryWrapper<EpsBudgetData> queryWrapper = new QueryWrapper<>();
        if (versionId != null) {
            queryWrapper.eq("version_id", versionId);
        }
        if (organizationId != null) {
            queryWrapper.eq("organization_id", organizationId);
        }
        if (budgetPeriod != null && !budgetPeriod.isEmpty()) {
            queryWrapper.eq("budget_period", budgetPeriod);
        }
        queryWrapper.eq("deleted", 0);
        
        // 总记录数
        Long totalRecords = budgetDataMapper.selectCount(queryWrapper);
        statistics.put("totalRecords", totalRecords);
        
        // 按状态统计
        QueryWrapper<EpsBudgetData> draftWrapper = new QueryWrapper<>(queryWrapper);
        draftWrapper.eq("data_status", "DRAFT");
        Long draftCount = budgetDataMapper.selectCount(draftWrapper);
        statistics.put("draftCount", draftCount);
        
        QueryWrapper<EpsBudgetData> submittedWrapper = new QueryWrapper<>(queryWrapper);
        submittedWrapper.eq("data_status", "SUBMITTED");
        Long submittedCount = budgetDataMapper.selectCount(submittedWrapper);
        statistics.put("submittedCount", submittedCount);
        
        QueryWrapper<EpsBudgetData> approvedWrapper = new QueryWrapper<>(queryWrapper);
        approvedWrapper.eq("data_status", "APPROVED");
        Long approvedCount = budgetDataMapper.selectCount(approvedWrapper);
        statistics.put("approvedCount", approvedCount);
        
        // 锁定数据统计
        QueryWrapper<EpsBudgetData> lockedWrapper = new QueryWrapper<>(queryWrapper);
        lockedWrapper.eq("is_locked", true);
        Long lockedCount = budgetDataMapper.selectCount(lockedWrapper);
        statistics.put("lockedCount", lockedCount);
        
        // 金额统计
        List<EpsBudgetData> dataList = budgetDataMapper.selectList(queryWrapper);
        BigDecimal totalBudgetAmount = BigDecimal.ZERO;
        BigDecimal totalActualAmount = BigDecimal.ZERO;
        BigDecimal totalVarianceAmount = BigDecimal.ZERO;
        
        for (EpsBudgetData data : dataList) {
            if (data.getBudgetAmount() != null) {
                totalBudgetAmount = totalBudgetAmount.add(data.getBudgetAmount());
            }
            if (data.getActualAmount() != null) {
                totalActualAmount = totalActualAmount.add(data.getActualAmount());
            }
            if (data.getVarianceAmount() != null) {
                totalVarianceAmount = totalVarianceAmount.add(data.getVarianceAmount());
            }
        }
        
        statistics.put("totalBudgetAmount", totalBudgetAmount);
        statistics.put("totalActualAmount", totalActualAmount);
        statistics.put("totalVarianceAmount", totalVarianceAmount);
        
        return statistics;
    }

    // 私有辅助方法
    private boolean checkDataExists(Long versionId, Long subjectId, Long organizationId, String budgetPeriod) {
        QueryWrapper<EpsBudgetData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("version_id", versionId);
        queryWrapper.eq("subject_id", subjectId);
        queryWrapper.eq("organization_id", organizationId);
        queryWrapper.eq("budget_period", budgetPeriod);
        queryWrapper.eq("deleted", 0);
        return budgetDataMapper.selectCount(queryWrapper) > 0;
    }

    // 其他接口方法的简单实现
    @Override
    public Map<String, Object> importBudgetData(Map<String, Object> importData) {
        // TODO: 实现数据导入
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("importedCount", 0);
        return result;
    }

    @Override
    public Map<String, Object> exportBudgetData(Map<String, Object> exportParams) {
        // TODO: 实现数据导出
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("exportedCount", 0);
        return result;
    }

    @Override
    public Map<String, Object> calculateBudgetData(Map<String, Object> calculateParams) {
        // TODO: 实现数据计算
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("calculatedCount", 0);
        return result;
    }

    @Override
    public Map<String, Object> validateBudgetData(Map<String, Object> validateParams) {
        // TODO: 实现数据验证
        Map<String, Object> result = new HashMap<>();
        result.put("isValid", true);
        result.put("validationErrors", new ArrayList<>());
        return result;
    }

    @Override
    public Map<String, Object> summarizeBudgetData(Map<String, Object> summarizeParams) {
        // TODO: 实现数据汇总
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> decomposeBudgetData(Map<String, Object> decomposeParams) {
        // TODO: 实现数据分解
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> copyBudgetData(Map<String, Object> copyParams) {
        // TODO: 实现数据复制
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> submitBudgetData(Map<String, Object> submitParams) {
        // TODO: 实现数据提交
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> approveBudgetData(Map<String, Object> approveParams) {
        // TODO: 实现数据审批
        return new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> getBudgetDataHistory(Long dataId) {
        // TODO: 实现变更历史查询
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> batchOperateBudgetData(Map<String, Object> batchData) {
        // TODO: 实现批量操作
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("processedCount", 0);
        return result;
    }

    @Override
    public Map<String, Object> getBudgetDataConfiguration(Long versionId) {
        // TODO: 实现配置查询
        return new HashMap<>();
    }

    @Override
    public boolean saveBudgetDataConfiguration(Long versionId, Map<String, Object> configuration) {
        // TODO: 实现配置保存
        return true;
    }

    @Override
    public Map<String, Object> getBudgetDataPermissions(Long dataId, Long userId) {
        // TODO: 实现权限查询
        Map<String, Object> permissions = new HashMap<>();
        permissions.put("canEdit", true);
        permissions.put("canDelete", false);
        return permissions;
    }

    @Override
    public boolean setBudgetDataPermissions(Long dataId, Map<String, Object> permissionData) {
        // TODO: 实现权限设置
        return true;
    }

    @Override
    public Map<String, Object> getBudgetDataTemplate(Long versionId) {
        // TODO: 实现模板查询
        return new HashMap<>();
    }

    @Override
    public boolean applyBudgetDataTemplate(Long versionId, Map<String, Object> templateData) {
        // TODO: 实现模板应用
        return true;
    }

    @Override
    public Map<String, Object> getBudgetDataDifferences(Long sourceVersionId, Long targetVersionId) {
        // TODO: 实现差异分析
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> syncBudgetData(Long sourceVersionId, Long targetVersionId, Map<String, Object> syncParams) {
        // TODO: 实现数据同步
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> mergeBudgetData(List<Long> sourceVersionIds, Long targetVersionId, Map<String, Object> mergeParams) {
        // TODO: 实现数据合并
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> splitBudgetData(Long sourceVersionId, Map<String, Object> splitParams) {
        // TODO: 实现数据拆分
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getBudgetDataTrend(Long versionId, Long subjectId, Long organizationId) {
        // TODO: 实现趋势分析
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> forecastBudgetData(Long versionId, Map<String, Object> forecastParams) {
        // TODO: 实现数据预测
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getBudgetDataAnomalies(Long versionId, Map<String, Object> anomalyParams) {
        // TODO: 实现异常检测
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> repairBudgetData(Long versionId, Map<String, Object> repairParams) {
        // TODO: 实现数据修复
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getBudgetDataPerformanceStatistics(Long versionId, String startDate, String endDate) {
        // TODO: 实现性能统计
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> optimizeBudgetData(Long versionId, Map<String, Object> optimizeParams) {
        // TODO: 实现数据优化
        return new HashMap<>();
    }
}
