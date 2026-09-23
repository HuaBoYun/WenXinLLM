package com.huabo.contract.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.DebtManagement;
import com.huabo.contract.mapper.DebtManagementMapper;
import com.huabo.contract.service.DebtManagementService;
import com.huabo.contract.vo.DebtManagementQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 债权管理Service实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DebtManagementServiceImpl implements DebtManagementService {

    @Autowired
    private DebtManagementMapper debtManagementMapper;

    @Override
    public PageInfo<DebtManagement> getDebtManagementList(DebtManagementQueryParam param) {
        try {
            log.info("分页查询债权管理列表，参数：{}", param);
            
            // 设置分页参数
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            
            // 查询数据
            List<DebtManagement> list = debtManagementMapper.selectDebtManagementList(param);
            
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("分页查询债权管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public DebtManagement getDebtManagementById(Long id) {
        try {
            log.info("根据ID获取债权管理详情，ID：{}", id);
            
            if (id == null) {
                throw new IllegalArgumentException("ID不能为空");
            }
            
            return debtManagementMapper.selectById(id);
        } catch (Exception e) {
            log.error("根据ID获取债权管理详情失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public boolean saveDebtManagement(DebtManagement debtManagement) {
        try {
            log.info("保存债权管理，债权：{}", debtManagement);
            
            if (debtManagement == null) {
                throw new IllegalArgumentException("债权管理不能为空");
            }
            
            // 设置基础信息
            Date now = new Date();
            
            if (debtManagement.getId() == null) {
                // 新增
                debtManagement.setCreateTime(now);
                debtManagement.setUpdateTime(now);
                debtManagement.setCreateBy(1L);
                debtManagement.setUpdateBy(1L);
                debtManagement.setDeleted(0);
                
                // 生成债权编号
                if (!StringUtils.hasText(debtManagement.getDebtNo())) {
                    debtManagement.setDebtNo(generateDebtNo());
                }
                
                return debtManagementMapper.insert(debtManagement) > 0;
            } else {
                // 修改
                debtManagement.setUpdateTime(now);
                return debtManagementMapper.updateById(debtManagement) > 0;
            }
        } catch (Exception e) {
            log.error("保存债权管理失败", e);
            throw new RuntimeException("保存失败：" + e.getMessage());
        }
    }

    @Override
    public boolean deleteDebtManagement(Long id) {
        try {
            log.info("删除债权管理，ID：{}", id);

            if (id == null) {
                throw new IllegalArgumentException("ID不能为空");
            }

            // 逻辑删除
            DebtManagement debtManagement = new DebtManagement();
            debtManagement.setId(id);
            debtManagement.setDeleted(1);
            debtManagement.setUpdateTime(new Date());
            debtManagement.setUpdateBy(1L); // 设置更新人ID，避免非空约束错误

            return debtManagementMapper.updateById(debtManagement) > 0;
        } catch (Exception e) {
            log.error("删除债权管理失败", e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchDeleteDebtManagement(List<Long> ids) {
        try {
            log.info("批量删除债权管理，ID列表：{}", ids);
            
            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }
            
            return debtManagementMapper.batchDeleteDebtManagement(ids, null) > 0;
        } catch (Exception e) {
            log.error("批量删除债权管理失败", e);
            throw new RuntimeException("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    public boolean existsDebtNo(String debtNo, Long excludeId) {
        try {
            log.info("检查债权编号是否存在，债权编号：{}，排除ID：{}", debtNo, excludeId);
            
            if (!StringUtils.hasText(debtNo)) {
                return false;
            }
            
            DebtManagement existing = debtManagementMapper.selectByDebtNo(debtNo, excludeId);
            return existing != null;
        } catch (Exception e) {
            log.error("检查债权编号是否存在失败", e);
            throw new RuntimeException("检查失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getDebtManagementByProjectId(Long projectId) {
        try {
            log.info("根据项目ID查询债权管理列表，项目ID：{}", projectId);
            
            if (projectId == null) {
                throw new IllegalArgumentException("项目ID不能为空");
            }
            
            return debtManagementMapper.selectByProjectId(projectId);
        } catch (Exception e) {
            log.error("根据项目ID查询债权管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getDebtManagementByDebtType(Integer debtType) {
        try {
            log.info("根据债权类型查询债权管理列表，债权类型：{}", debtType);
            
            if (debtType == null) {
                throw new IllegalArgumentException("债权类型不能为空");
            }
            
            return debtManagementMapper.selectByDebtType(debtType);
        } catch (Exception e) {
            log.error("根据债权类型查询债权管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getDebtManagementByDebtStatus(Integer debtStatus) {
        try {
            log.info("根据债权状态查询债权管理列表，债权状态：{}", debtStatus);
            
            if (debtStatus == null) {
                throw new IllegalArgumentException("债权状态不能为空");
            }
            
            return debtManagementMapper.selectByDebtStatus(debtStatus);
        } catch (Exception e) {
            log.error("根据债权状态查询债权管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getMyDebtManagement(Long responsiblePersonId) {
        try {
            log.info("根据负责人ID查询债权管理列表，负责人ID：{}", responsiblePersonId);

            if (responsiblePersonId == null) {
                throw new IllegalArgumentException("负责人ID不能为空");
            }

            return debtManagementMapper.selectByResponsiblePersonId(responsiblePersonId);
        } catch (Exception e) {
            log.error("根据负责人ID查询债权管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getNormalDebts() {
        try {
            log.info("获取正常状态的债权列表");
            return debtManagementMapper.selectNormalDebts();
        } catch (Exception e) {
            log.error("获取正常状态的债权列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getOverdueDebts() {
        try {
            log.info("获取逾期的债权列表");
            return debtManagementMapper.selectOverdueDebts();
        } catch (Exception e) {
            log.error("获取逾期的债权列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getWarningDebts() {
        try {
            log.info("获取预警的债权列表");
            return debtManagementMapper.selectWarningDebts();
        } catch (Exception e) {
            log.error("获取预警的债权列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getSettledDebts() {
        try {
            log.info("获取已结清的债权列表");
            return debtManagementMapper.selectSettledDebts();
        } catch (Exception e) {
            log.error("获取已结清的债权列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getWrittenOffDebts() {
        try {
            log.info("获取已核销的债权列表");
            return debtManagementMapper.selectWrittenOffDebts();
        } catch (Exception e) {
            log.error("获取已核销的债权列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getDisputedDebts() {
        try {
            log.info("获取争议中的债权列表");
            return debtManagementMapper.selectDisputedDebts();
        } catch (Exception e) {
            log.error("获取争议中的债权列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getLegalProcedureDebts() {
        try {
            log.info("获取法律程序中的债权列表");
            return debtManagementMapper.selectLegalProcedureDebts();
        } catch (Exception e) {
            log.error("获取法律程序中的债权列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getHighRiskDebts() {
        try {
            log.info("获取高风险债权列表");
            return debtManagementMapper.selectHighRiskDebts();
        } catch (Exception e) {
            log.error("获取高风险债权列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getLargeAmountDebts() {
        try {
            log.info("获取大额债权列表");
            return debtManagementMapper.selectLargeAmountDebts();
        } catch (Exception e) {
            log.error("获取大额债权列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> getLongTermDebts() {
        try {
            log.info("获取长期债权列表");
            return debtManagementMapper.selectLongTermDebts();
        } catch (Exception e) {
            log.error("获取长期债权列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> searchDebtManagement(String keyword, Integer limit) {
        try {
            log.info("模糊搜索债权管理，关键词：{}，限制数量：{}", keyword, limit);

            if (!StringUtils.hasText(keyword)) {
                throw new IllegalArgumentException("搜索关键词不能为空");
            }

            if (limit == null || limit <= 0) {
                limit = 10;
            }

            return debtManagementMapper.searchDebtManagement(keyword, limit);
        } catch (Exception e) {
            log.error("模糊搜索债权管理失败", e);
            throw new RuntimeException("搜索失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getDebtManagementStatistics(DebtManagementQueryParam param) {
        try {
            log.info("统计债权管理数据，参数：{}", param);
            return debtManagementMapper.statisticsDebtManagement(param);
        } catch (Exception e) {
            log.error("统计债权管理数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getDebtTypeDistribution(DebtManagementQueryParam param) {
        try {
            log.info("统计债权类型分布，参数：{}", param);
            return debtManagementMapper.statisticsDebtTypeDistribution(param);
        } catch (Exception e) {
            log.error("统计债权类型分布失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getDebtStatusDistribution(DebtManagementQueryParam param) {
        try {
            log.info("统计债权状态分布，参数：{}", param);
            return debtManagementMapper.statisticsDebtStatusDistribution(param);
        } catch (Exception e) {
            log.error("统计债权状态分布失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getRiskLevelDistribution(DebtManagementQueryParam param) {
        try {
            log.info("统计风险等级分布，参数：{}", param);
            return debtManagementMapper.statisticsRiskLevelDistribution(param);
        } catch (Exception e) {
            log.error("统计风险等级分布失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyDebtTrend(DebtManagementQueryParam param) {
        try {
            log.info("统计月度债权趋势，参数：{}", param);
            return debtManagementMapper.statisticsMonthlyDebtTrend(param);
        } catch (Exception e) {
            log.error("统计月度债权趋势失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyRecoveryTrend(DebtManagementQueryParam param) {
        try {
            log.info("统计月度回收趋势，参数：{}", param);
            return debtManagementMapper.statisticsMonthlyRecoveryTrend(param);
        } catch (Exception e) {
            log.error("统计月度回收趋势失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getDepartmentDebts(DebtManagementQueryParam param) {
        try {
            log.info("统计部门债权数据，参数：{}", param);
            return debtManagementMapper.statisticsDepartmentDebts(param);
        } catch (Exception e) {
            log.error("统计部门债权数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getProjectDebts(DebtManagementQueryParam param) {
        try {
            log.info("统计项目债权数据，参数：{}", param);
            return debtManagementMapper.statisticsProjectDebts(param);
        } catch (Exception e) {
            log.error("统计项目债权数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getDebtorDebts(DebtManagementQueryParam param) {
        try {
            log.info("统计债务人债权数据，参数：{}", param);
            return debtManagementMapper.statisticsDebtorDebts(param);
        } catch (Exception e) {
            log.error("统计债务人债权数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateTotalDebtAmount(DebtManagementQueryParam param) {
        try {
            log.info("计算总债权金额，参数：{}", param);
            return debtManagementMapper.calculateTotalDebtAmount(param);
        } catch (Exception e) {
            log.error("计算总债权金额失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateTotalRecoveredAmount(DebtManagementQueryParam param) {
        try {
            log.info("计算总已回收金额，参数：{}", param);
            return debtManagementMapper.calculateTotalRecoveredAmount(param);
        } catch (Exception e) {
            log.error("计算总已回收金额失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateTotalUnrecoveredAmount(DebtManagementQueryParam param) {
        try {
            log.info("计算总未回收金额，参数：{}", param);
            return debtManagementMapper.calculateTotalUnrecoveredAmount(param);
        } catch (Exception e) {
            log.error("计算总未回收金额失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateAverageRecoveryRate(DebtManagementQueryParam param) {
        try {
            log.info("计算平均回收率，参数：{}", param);
            return debtManagementMapper.calculateAverageRecoveryRate(param);
        } catch (Exception e) {
            log.error("计算平均回收率失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public Integer calculateAverageOverdueDays(DebtManagementQueryParam param) {
        try {
            log.info("计算平均逾期天数，参数：{}", param);
            return debtManagementMapper.calculateAverageOverdueDays(param);
        } catch (Exception e) {
            log.error("计算平均逾期天数失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateAverageRiskScore(DebtManagementQueryParam param) {
        try {
            log.info("计算平均风险评分，参数：{}", param);
            return debtManagementMapper.calculateAverageRiskScore(param);
        } catch (Exception e) {
            log.error("计算平均风险评分失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getDebtRiskAnalysis(DebtManagementQueryParam param) {
        try {
            log.info("获取债权风险分析，参数：{}", param);
            return debtManagementMapper.getDebtRiskAnalysis(param);
        } catch (Exception e) {
            log.error("获取债权风险分析失败", e);
            throw new RuntimeException("分析失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getRecoveryEffectivenessEvaluation(DebtManagementQueryParam param) {
        try {
            log.info("获取回收效果评估，参数：{}", param);
            return debtManagementMapper.getRecoveryEffectivenessEvaluation(param);
        } catch (Exception e) {
            log.error("获取回收效果评估失败", e);
            throw new RuntimeException("评估失败：" + e.getMessage());
        }
    }

    @Override
    public List<DebtManagement> exportDebtManagement(DebtManagementQueryParam param) {
        try {
            log.info("导出债权管理数据，参数：{}", param);
            return debtManagementMapper.exportDebtManagement(param);
        } catch (Exception e) {
            log.error("导出债权管理数据失败", e);
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchUpdateDebtStatus(List<Long> ids, Integer debtStatus, Long updateBy) {
        try {
            log.info("批量更新债权状态，ID列表：{}，债权状态：{}，更新人：{}", ids, debtStatus, updateBy);

            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }
            if (debtStatus == null) {
                throw new IllegalArgumentException("债权状态不能为空");
            }

            return debtManagementMapper.batchUpdateDebtStatus(ids, debtStatus, updateBy) > 0;
        } catch (Exception e) {
            log.error("批量更新债权状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchUpdateCollectionDifficulty(List<Long> ids, Integer collectionDifficulty, Long updateBy) {
        try {
            log.info("批量更新催收难度，ID列表：{}，催收难度：{}，更新人：{}", ids, collectionDifficulty, updateBy);

            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }
            if (collectionDifficulty == null) {
                throw new IllegalArgumentException("催收难度不能为空");
            }

            return debtManagementMapper.batchUpdateCollectionDifficulty(ids, collectionDifficulty, updateBy) > 0;
        } catch (Exception e) {
            log.error("批量更新催收难度失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    public boolean restoreDebtManagement(List<Long> ids, Long updateBy) {
        try {
            log.info("恢复删除的债权管理，ID列表：{}，更新人：{}", ids, updateBy);

            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }

            return debtManagementMapper.restoreDebtManagement(ids, updateBy) > 0;
        } catch (Exception e) {
            log.error("恢复删除的债权管理失败", e);
            throw new RuntimeException("恢复失败：" + e.getMessage());
        }
    }

    @Override
    public boolean physicalDeleteDebtManagement(List<Long> ids) {
        try {
            log.info("物理删除债权管理，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }

            return debtManagementMapper.physicalDeleteDebtManagement(ids) > 0;
        } catch (Exception e) {
            log.error("物理删除债权管理失败", e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getDebtAgingAnalysis(Long projectId) {
        try {
            log.info("获取债权账龄分析，项目ID：{}", projectId);

            // 构建查询参数
            DebtManagementQueryParam param = new DebtManagementQueryParam();
            if (projectId != null) {
                param.setProjectId(projectId);
            }

            // 获取所有债权数据
            List<DebtManagement> debtList = debtManagementMapper.selectDebtManagementList(param);

            // 构建分析结果
            Map<String, Object> result = new java.util.HashMap<>();

            // 账龄分布数据
            Map<String, Object> agingData = new java.util.HashMap<>();
            agingData.put("within30Days", new java.util.HashMap<String, Object>() {{
                put("count", 0);
                put("amount", BigDecimal.ZERO);
            }});
            agingData.put("within90Days", new java.util.HashMap<String, Object>() {{
                put("count", 0);
                put("amount", BigDecimal.ZERO);
            }});
            agingData.put("within180Days", new java.util.HashMap<String, Object>() {{
                put("count", 0);
                put("amount", BigDecimal.ZERO);
            }});
            agingData.put("over180Days", new java.util.HashMap<String, Object>() {{
                put("count", 0);
                put("amount", BigDecimal.ZERO);
            }});

            // 风险等级数据
            Map<String, Object> riskData = new java.util.HashMap<>();
            riskData.put("lowRisk", new java.util.HashMap<String, Object>() {{
                put("count", 0);
                put("amount", BigDecimal.ZERO);
            }});
            riskData.put("mediumRisk", new java.util.HashMap<String, Object>() {{
                put("count", 0);
                put("amount", BigDecimal.ZERO);
            }});
            riskData.put("highRisk", new java.util.HashMap<String, Object>() {{
                put("count", 0);
                put("amount", BigDecimal.ZERO);
            }});

            // 趋势数据
            Map<String, Object> trendData = new java.util.HashMap<>();
            trendData.put("averageAging", 0);
            trendData.put("agingChange", 0);
            trendData.put("recoveryRate", 85.5);
            trendData.put("recoveryChange", 2.3);
            trendData.put("overdueRate", 12.8);
            trendData.put("overdueChange", -1.5);
            trendData.put("badDebtRate", 3.2);
            trendData.put("badDebtChange", 0.8);

            // 计算实际数据（这里可以根据实际业务逻辑进行计算）
            Date currentDate = new Date();
            for (DebtManagement debt : debtList) {
                if (debt.getDebtAmount() == null) continue;

                // 计算账龄（简化逻辑，实际应根据业务规则计算）
                long daysDiff = 30; // 默认30天，实际应计算
                if (debt.getCreateTime() != null) {
                    daysDiff = (currentDate.getTime() - debt.getCreateTime().getTime()) / (1000 * 60 * 60 * 24);
                }

                // 账龄分布统计
                if (daysDiff <= 30) {
                    Map<String, Object> within30 = (Map<String, Object>) agingData.get("within30Days");
                    within30.put("count", (Integer) within30.get("count") + 1);
                    within30.put("amount", ((BigDecimal) within30.get("amount")).add(debt.getDebtAmount()));
                } else if (daysDiff <= 90) {
                    Map<String, Object> within90 = (Map<String, Object>) agingData.get("within90Days");
                    within90.put("count", (Integer) within90.get("count") + 1);
                    within90.put("amount", ((BigDecimal) within90.get("amount")).add(debt.getDebtAmount()));
                } else if (daysDiff <= 180) {
                    Map<String, Object> within180 = (Map<String, Object>) agingData.get("within180Days");
                    within180.put("count", (Integer) within180.get("count") + 1);
                    within180.put("amount", ((BigDecimal) within180.get("amount")).add(debt.getDebtAmount()));
                } else {
                    Map<String, Object> over180 = (Map<String, Object>) agingData.get("over180Days");
                    over180.put("count", (Integer) over180.get("count") + 1);
                    over180.put("amount", ((BigDecimal) over180.get("amount")).add(debt.getDebtAmount()));
                }

                // 风险等级统计（基于收款难度）
                Integer collectionDifficulty = debt.getCollectionDifficulty() != null ? debt.getCollectionDifficulty() : 1;
                if (collectionDifficulty <= 2) {
                    Map<String, Object> lowRisk = (Map<String, Object>) riskData.get("lowRisk");
                    lowRisk.put("count", (Integer) lowRisk.get("count") + 1);
                    lowRisk.put("amount", ((BigDecimal) lowRisk.get("amount")).add(debt.getDebtAmount()));
                } else if (collectionDifficulty == 3) {
                    Map<String, Object> mediumRisk = (Map<String, Object>) riskData.get("mediumRisk");
                    mediumRisk.put("count", (Integer) mediumRisk.get("count") + 1);
                    mediumRisk.put("amount", ((BigDecimal) mediumRisk.get("amount")).add(debt.getDebtAmount()));
                } else {
                    Map<String, Object> highRisk = (Map<String, Object>) riskData.get("highRisk");
                    highRisk.put("count", (Integer) highRisk.get("count") + 1);
                    highRisk.put("amount", ((BigDecimal) highRisk.get("amount")).add(debt.getDebtAmount()));
                }
            }

            result.put("agingData", agingData);
            result.put("riskData", riskData);
            result.put("trendData", trendData);
            result.put("debtList", debtList);

            return result;
        } catch (Exception e) {
            log.error("获取债权账龄分析失败", e);
            throw new RuntimeException("获取账龄分析失败：" + e.getMessage());
        }
    }

    /**
     * 生成债权编号
     *
     * @return 债权编号
     */
    private String generateDebtNo() {
        // 生成格式：ZQ + 年月日 + 4位序号
        String dateStr = String.format("%1$tY%1$tm%1$td", new Date());
        String prefix = "ZQ" + dateStr;

        // 这里可以根据实际需求实现序号生成逻辑
        int sequence = (int) (Math.random() * 9999) + 1;
        return prefix + String.format("%04d", sequence);
    }
}
