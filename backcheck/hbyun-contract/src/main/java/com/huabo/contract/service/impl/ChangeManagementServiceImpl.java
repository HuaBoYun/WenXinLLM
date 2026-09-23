package com.huabo.contract.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.ChangeManagement;
import com.huabo.contract.mapper.ChangeManagementMapper;
import com.huabo.contract.service.ChangeManagementService;
import com.huabo.contract.vo.ChangeManagementQueryParam;
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
 * 项目变更管理Service实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ChangeManagementServiceImpl implements ChangeManagementService {

    @Autowired
    private ChangeManagementMapper changeManagementMapper;

    @Override
    public PageInfo<ChangeManagement> getChangeManagementList(ChangeManagementQueryParam param) {
        try {
            log.info("分页查询项目变更管理列表，参数：{}", param);
            
            // 设置分页参数
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            
            // 查询数据
            List<ChangeManagement> list = changeManagementMapper.selectChangeManagementList(param);
            
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("分页查询项目变更管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public ChangeManagement getChangeManagementById(Long id) {
        try {
            log.info("根据ID获取项目变更管理详情，ID：{}", id);
            
            if (id == null) {
                throw new IllegalArgumentException("ID不能为空");
            }
            
            return changeManagementMapper.selectById(id);
        } catch (Exception e) {
            log.error("根据ID获取项目变更管理详情失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public boolean saveChangeManagement(ChangeManagement changeManagement) {
        try {
            log.info("保存项目变更管理，变更：{}", changeManagement);
            
            if (changeManagement == null) {
                throw new IllegalArgumentException("项目变更管理不能为空");
            }
            
            // 设置基础信息
            Date now = new Date();
            
            if (changeManagement.getId() == null) {
                // 新增时，Controller已经设置了createBy和updateBy，这里不再重复设置
                // 只设置其他必要的默认值
                if (changeManagement.getCreateTime() == null) {
                    changeManagement.setCreateTime(now);
                }
                if (changeManagement.getUpdateTime() == null) {
                    changeManagement.setUpdateTime(now);
                }
                if (changeManagement.getDeleted() == null) {
                    changeManagement.setDeleted(0);
                }
                if (changeManagement.getVersion() == null) {
                    changeManagement.setVersion(1);
                }

                // 生成变更编号
                if (!StringUtils.hasText(changeManagement.getChangeNo())) {
                    changeManagement.setChangeNo(generateChangeNo());
                }

                return changeManagementMapper.insert(changeManagement) > 0;
            } else {
                // 修改时，Controller已经设置了updateBy，这里只设置updateTime
                if (changeManagement.getUpdateTime() == null) {
                    changeManagement.setUpdateTime(now);
                }
                return changeManagementMapper.updateById(changeManagement) > 0;
            }
        } catch (Exception e) {
            log.error("保存项目变更管理失败", e);
            throw new RuntimeException("保存失败：" + e.getMessage());
        }
    }

    @Override
    public boolean deleteChangeManagement(Long id) {
        try {
            log.info("删除项目变更管理，ID：{}", id);

            if (id == null) {
                throw new IllegalArgumentException("ID不能为空");
            }

            // 先查询记录是否存在
            ChangeManagement changeManagement = changeManagementMapper.selectById(id);
            if (changeManagement == null) {
                throw new RuntimeException("项目变更记录不存在");
            }

            // 手动设置逻辑删除字段
            changeManagement.setDeleted(1);
            changeManagement.setUpdateBy(1L);
            changeManagement.setUpdateTime(new Date());

            // 使用updateById手动执行逻辑删除
            return changeManagementMapper.updateById(changeManagement) > 0;
        } catch (Exception e) {
            log.error("删除项目变更管理失败", e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchDeleteChangeManagement(List<Long> ids) {
        try {
            log.info("批量删除项目变更管理，ID列表：{}", ids);
            
            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }
            
            return changeManagementMapper.batchDeleteChangeManagement(ids, 1L) > 0;
        } catch (Exception e) {
            log.error("批量删除项目变更管理失败", e);
            throw new RuntimeException("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    public boolean existsChangeNo(String changeNo, Long excludeId) {
        try {
            log.info("检查变更编号是否存在，变更编号：{}，排除ID：{}", changeNo, excludeId);
            
            if (!StringUtils.hasText(changeNo)) {
                return false;
            }
            
            ChangeManagement existing = changeManagementMapper.selectByChangeNo(changeNo, excludeId);
            return existing != null;
        } catch (Exception e) {
            log.error("检查变更编号是否存在失败", e);
            throw new RuntimeException("检查失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getChangeManagementByProjectId(Long projectId) {
        try {
            log.info("根据项目ID查询项目变更管理列表，项目ID：{}", projectId);
            
            if (projectId == null) {
                throw new IllegalArgumentException("项目ID不能为空");
            }
            
            return changeManagementMapper.selectByProjectId(projectId);
        } catch (Exception e) {
            log.error("根据项目ID查询项目变更管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getChangeManagementByChangeType(Integer changeType) {
        try {
            log.info("根据变更类型查询项目变更管理列表，变更类型：{}", changeType);
            
            if (changeType == null) {
                throw new IllegalArgumentException("变更类型不能为空");
            }
            
            return changeManagementMapper.selectByChangeType(changeType);
        } catch (Exception e) {
            log.error("根据变更类型查询项目变更管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getChangeManagementByChangeLevel(Integer changeLevel) {
        try {
            log.info("根据变更等级查询项目变更管理列表，变更等级：{}", changeLevel);
            
            if (changeLevel == null) {
                throw new IllegalArgumentException("变更等级不能为空");
            }
            
            return changeManagementMapper.selectByChangeLevel(changeLevel);
        } catch (Exception e) {
            log.error("根据变更等级查询项目变更管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getChangeManagementByChangeStatus(Integer changeStatus) {
        try {
            log.info("根据变更状态查询项目变更管理列表，变更状态：{}", changeStatus);
            
            if (changeStatus == null) {
                throw new IllegalArgumentException("变更状态不能为空");
            }
            
            return changeManagementMapper.selectByChangeStatus(changeStatus);
        } catch (Exception e) {
            log.error("根据变更状态查询项目变更管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getChangeManagementByApplicantId(Long applicantId) {
        try {
            log.info("根据申请人ID查询项目变更管理列表，申请人ID：{}", applicantId);
            
            if (applicantId == null) {
                throw new IllegalArgumentException("申请人ID不能为空");
            }
            
            return changeManagementMapper.selectByApplicantId(applicantId);
        } catch (Exception e) {
            log.error("根据申请人ID查询项目变更管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getChangeManagementByReviewerId(Long reviewerId) {
        try {
            log.info("根据审核人ID查询项目变更管理列表，审核人ID：{}", reviewerId);
            
            if (reviewerId == null) {
                throw new IllegalArgumentException("审核人ID不能为空");
            }
            
            return changeManagementMapper.selectByReviewerId(reviewerId);
        } catch (Exception e) {
            log.error("根据审核人ID查询项目变更管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getChangeManagementByApproverId(Long approverId) {
        try {
            log.info("根据批准人ID查询项目变更管理列表，批准人ID：{}", approverId);
            
            if (approverId == null) {
                throw new IllegalArgumentException("批准人ID不能为空");
            }
            
            return changeManagementMapper.selectByApproverId(approverId);
        } catch (Exception e) {
            log.error("根据批准人ID查询项目变更管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getChangeManagementByImplementerId(Long implementerId) {
        try {
            log.info("根据实施人ID查询项目变更管理列表，实施人ID：{}", implementerId);
            
            if (implementerId == null) {
                throw new IllegalArgumentException("实施人ID不能为空");
            }
            
            return changeManagementMapper.selectByImplementerId(implementerId);
        } catch (Exception e) {
            log.error("根据实施人ID查询项目变更管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getDraftChanges() {
        try {
            log.info("获取草稿状态的变更列表");
            return changeManagementMapper.selectDraftChanges();
        } catch (Exception e) {
            log.error("获取草稿状态的变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getPendingReviewChanges() {
        try {
            log.info("获取待审核的变更列表");
            return changeManagementMapper.selectPendingReviewChanges();
        } catch (Exception e) {
            log.error("获取待审核的变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getInReviewChanges() {
        try {
            log.info("获取审核中的变更列表");
            return changeManagementMapper.selectInReviewChanges();
        } catch (Exception e) {
            log.error("获取审核中的变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getApprovedChanges() {
        try {
            log.info("获取已批准的变更列表");
            return changeManagementMapper.selectApprovedChanges();
        } catch (Exception e) {
            log.error("获取已批准的变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getRejectedChanges() {
        try {
            log.info("获取已拒绝的变更列表");
            return changeManagementMapper.selectRejectedChanges();
        } catch (Exception e) {
            log.error("获取已拒绝的变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getImplementingChanges() {
        try {
            log.info("获取实施中的变更列表");
            return changeManagementMapper.selectImplementingChanges();
        } catch (Exception e) {
            log.error("获取实施中的变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getCompletedChanges() {
        try {
            log.info("获取已完成的变更列表");
            return changeManagementMapper.selectCompletedChanges();
        } catch (Exception e) {
            log.error("获取已完成的变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getCancelledChanges() {
        try {
            log.info("获取已取消的变更列表");
            return changeManagementMapper.selectCancelledChanges();
        } catch (Exception e) {
            log.error("获取已取消的变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getUrgentChanges() {
        try {
            log.info("获取紧急变更列表");
            return changeManagementMapper.selectUrgentChanges();
        } catch (Exception e) {
            log.error("获取紧急变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getImportantChanges() {
        try {
            log.info("获取重要变更列表");
            return changeManagementMapper.selectImportantChanges();
        } catch (Exception e) {
            log.error("获取重要变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getHighPriorityChanges() {
        try {
            log.info("获取高优先级变更列表");
            return changeManagementMapper.selectHighPriorityChanges();
        } catch (Exception e) {
            log.error("获取高优先级变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getCostImpactChanges() {
        try {
            log.info("获取有成本影响的变更列表");
            return changeManagementMapper.selectCostImpactChanges();
        } catch (Exception e) {
            log.error("获取有成本影响的变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> getScheduleImpactChanges() {
        try {
            log.info("获取有进度影响的变更列表");
            return changeManagementMapper.selectScheduleImpactChanges();
        } catch (Exception e) {
            log.error("获取有进度影响的变更列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> searchChangeManagement(String keyword, Integer limit) {
        try {
            log.info("模糊搜索项目变更管理，关键词：{}，限制数量：{}", keyword, limit);

            if (!StringUtils.hasText(keyword)) {
                throw new IllegalArgumentException("搜索关键词不能为空");
            }

            if (limit == null || limit <= 0) {
                limit = 10;
            }

            return changeManagementMapper.searchChangeManagement(keyword, limit);
        } catch (Exception e) {
            log.error("模糊搜索项目变更管理失败", e);
            throw new RuntimeException("搜索失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getChangeManagementStatistics(ChangeManagementQueryParam param) {
        try {
            log.info("统计项目变更管理数据，参数：{}", param);
            return changeManagementMapper.statisticsChangeManagement(param);
        } catch (Exception e) {
            log.error("统计项目变更管理数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getChangeTypeDistribution(ChangeManagementQueryParam param) {
        try {
            log.info("统计变更类型分布，参数：{}", param);
            return changeManagementMapper.statisticsChangeTypeDistribution(param);
        } catch (Exception e) {
            log.error("统计变更类型分布失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getChangeLevelDistribution(ChangeManagementQueryParam param) {
        try {
            log.info("统计变更等级分布，参数：{}", param);
            return changeManagementMapper.statisticsChangeLevelDistribution(param);
        } catch (Exception e) {
            log.error("统计变更等级分布失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getChangeStatusDistribution(ChangeManagementQueryParam param) {
        try {
            log.info("统计变更状态分布，参数：{}", param);
            return changeManagementMapper.statisticsChangeStatusDistribution(param);
        } catch (Exception e) {
            log.error("统计变更状态分布失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyChangeTrend(ChangeManagementQueryParam param) {
        try {
            log.info("统计月度变更趋势，参数：{}", param);
            return changeManagementMapper.statisticsMonthlyChangeTrend(param);
        } catch (Exception e) {
            log.error("统计月度变更趋势失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getDepartmentChanges(ChangeManagementQueryParam param) {
        try {
            log.info("统计部门变更数据，参数：{}", param);
            return changeManagementMapper.statisticsDepartmentChanges(param);
        } catch (Exception e) {
            log.error("统计部门变更数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getProjectChanges(ChangeManagementQueryParam param) {
        try {
            log.info("统计项目变更数据，参数：{}", param);
            return changeManagementMapper.statisticsProjectChanges(param);
        } catch (Exception e) {
            log.error("统计项目变更数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getApplicantChanges(ChangeManagementQueryParam param) {
        try {
            log.info("统计申请人变更数据，参数：{}", param);
            return changeManagementMapper.statisticsApplicantChanges(param);
        } catch (Exception e) {
            log.error("统计申请人变更数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateTotalCostImpact(ChangeManagementQueryParam param) {
        try {
            log.info("计算总成本影响，参数：{}", param);
            return changeManagementMapper.calculateTotalCostImpact(param);
        } catch (Exception e) {
            log.error("计算总成本影响失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public Integer calculateTotalScheduleImpact(ChangeManagementQueryParam param) {
        try {
            log.info("计算总进度影响，参数：{}", param);
            return changeManagementMapper.calculateTotalScheduleImpact(param);
        } catch (Exception e) {
            log.error("计算总进度影响失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateAverageCompletionRate(ChangeManagementQueryParam param) {
        try {
            log.info("计算平均完成度，参数：{}", param);
            return changeManagementMapper.calculateAverageCompletionRate(param);
        } catch (Exception e) {
            log.error("计算平均完成度失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateChangeSuccessRate(ChangeManagementQueryParam param) {
        try {
            log.info("计算变更成功率，参数：{}", param);
            return changeManagementMapper.calculateChangeSuccessRate(param);
        } catch (Exception e) {
            log.error("计算变更成功率失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getChangeImpactAnalysis(ChangeManagementQueryParam param) {
        try {
            log.info("获取变更影响分析，参数：{}", param);
            return changeManagementMapper.getChangeImpactAnalysis(param);
        } catch (Exception e) {
            log.error("获取变更影响分析失败", e);
            throw new RuntimeException("分析失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getChangeEffectivenessEvaluation(ChangeManagementQueryParam param) {
        try {
            log.info("获取变更效果评估，参数：{}", param);
            return changeManagementMapper.getChangeEffectivenessEvaluation(param);
        } catch (Exception e) {
            log.error("获取变更效果评估失败", e);
            throw new RuntimeException("评估失败：" + e.getMessage());
        }
    }

    @Override
    public List<ChangeManagement> exportChangeManagement(ChangeManagementQueryParam param) {
        try {
            log.info("导出项目变更管理数据，参数：{}", param);
            return changeManagementMapper.exportChangeManagement(param);
        } catch (Exception e) {
            log.error("导出项目变更管理数据失败", e);
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchUpdateChangeStatus(List<Long> ids, Integer changeStatus, Long updateBy) {
        try {
            log.info("批量更新变更状态，ID列表：{}，变更状态：{}，更新人：{}", ids, changeStatus, updateBy);

            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }
            if (changeStatus == null) {
                throw new IllegalArgumentException("变更状态不能为空");
            }

            return changeManagementMapper.batchUpdateChangeStatus(ids, changeStatus, updateBy) > 0;
        } catch (Exception e) {
            log.error("批量更新变更状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchUpdatePriority(List<Long> ids, Integer priority, Long updateBy) {
        try {
            log.info("批量更新优先级，ID列表：{}，优先级：{}，更新人：{}", ids, priority, updateBy);

            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }
            if (priority == null) {
                throw new IllegalArgumentException("优先级不能为空");
            }

            return changeManagementMapper.batchUpdatePriority(ids, priority, updateBy) > 0;
        } catch (Exception e) {
            log.error("批量更新优先级失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    public boolean restoreChangeManagement(List<Long> ids, Long updateBy) {
        try {
            log.info("恢复删除的项目变更管理，ID列表：{}，更新人：{}", ids, updateBy);

            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }

            return changeManagementMapper.restoreChangeManagement(ids, updateBy) > 0;
        } catch (Exception e) {
            log.error("恢复删除的项目变更管理失败", e);
            throw new RuntimeException("恢复失败：" + e.getMessage());
        }
    }

    @Override
    public boolean physicalDeleteChangeManagement(List<Long> ids) {
        try {
            log.info("物理删除项目变更管理，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }

            return changeManagementMapper.physicalDeleteChangeManagement(ids) > 0;
        } catch (Exception e) {
            log.error("物理删除项目变更管理失败", e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    /**
     * 生成变更编号
     *
     * @return 变更编号
     */
    private String generateChangeNo() {
        // 生成格式：BG + 年月日 + 4位序号
        String dateStr = String.format("%1$tY%1$tm%1$td", new Date());
        String prefix = "BG" + dateStr;

        // 这里可以根据实际需求实现序号生成逻辑
        int sequence = (int) (Math.random() * 9999) + 1;
        return prefix + String.format("%04d", sequence);
    }
}
