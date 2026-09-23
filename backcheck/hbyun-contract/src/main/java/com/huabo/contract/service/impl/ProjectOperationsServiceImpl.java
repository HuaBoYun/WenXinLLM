package com.huabo.contract.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.ProjectOperations;
import com.huabo.contract.mapper.ProjectOperationsMapper;
import com.huabo.contract.service.ProjectOperationsService;
import com.huabo.contract.vo.ProjectOperationsQueryParam;
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
 * 项目经营管理Service实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectOperationsServiceImpl implements ProjectOperationsService {

    @Autowired
    private ProjectOperationsMapper projectOperationsMapper;

    @Override
    public PageInfo<ProjectOperations> getProjectOperationsList(ProjectOperationsQueryParam param) {
        try {
            log.info("分页查询项目经营管理列表，参数：{}", param);
            
            // 设置分页参数
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            
            // 查询数据
            List<ProjectOperations> list = projectOperationsMapper.selectProjectOperationsList(param);
            
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("分页查询项目经营管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public ProjectOperations getProjectOperationsById(Long id) {
        try {
            log.info("根据ID获取项目经营管理详情，ID：{}", id);
            
            if (id == null) {
                throw new IllegalArgumentException("ID不能为空");
            }
            
            return projectOperationsMapper.selectById(id);
        } catch (Exception e) {
            log.error("根据ID获取项目经营管理详情失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public boolean saveProjectOperations(ProjectOperations projectOperations) {
        try {
            log.info("保存项目经营管理，经营：{}", projectOperations);
            
            if (projectOperations == null) {
                throw new IllegalArgumentException("项目经营管理不能为空");
            }
            
            // 设置基础信息
            Date now = new Date();
            
            if (projectOperations.getId() == null) {
                // 新增
                projectOperations.setCreateTime(now);
                projectOperations.setUpdateTime(now);
                projectOperations.setDelFlag(0);
                projectOperations.setVersion(1);
                
                // 生成经营编号
                if (!StringUtils.hasText(projectOperations.getOperationsNo())) {
                    projectOperations.setOperationsNo(generateOperationsNo());
                }
                
                return projectOperationsMapper.insert(projectOperations) > 0;
            } else {
                // 修改
                projectOperations.setUpdateTime(now);
                return projectOperationsMapper.updateById(projectOperations) > 0;
            }
        } catch (Exception e) {
            log.error("保存项目经营管理失败", e);
            throw new RuntimeException("保存失败：" + e.getMessage());
        }
    }

    @Override
    public boolean deleteProjectOperations(Long id) {
        try {
            log.info("删除项目经营管理，ID：{}", id);
            
            if (id == null) {
                throw new IllegalArgumentException("ID不能为空");
            }
            
            // 逻辑删除
            ProjectOperations projectOperations = new ProjectOperations();
            projectOperations.setId(id);
            projectOperations.setDelFlag(1);
            projectOperations.setUpdateTime(new Date());
            
            return projectOperationsMapper.updateById(projectOperations) > 0;
        } catch (Exception e) {
            log.error("删除项目经营管理失败", e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchDeleteProjectOperations(List<Long> ids) {
        try {
            log.info("批量删除项目经营管理，ID列表：{}", ids);
            
            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }
            
            return projectOperationsMapper.batchDeleteProjectOperations(ids, null) > 0;
        } catch (Exception e) {
            log.error("批量删除项目经营管理失败", e);
            throw new RuntimeException("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    public boolean existsOperationsNo(String operationsNo, Long excludeId) {
        try {
            log.info("检查经营编号是否存在，经营编号：{}，排除ID：{}", operationsNo, excludeId);
            
            if (!StringUtils.hasText(operationsNo)) {
                return false;
            }
            
            ProjectOperations existing = projectOperationsMapper.selectByOperationsNo(operationsNo, excludeId);
            return existing != null;
        } catch (Exception e) {
            log.error("检查经营编号是否存在失败", e);
            throw new RuntimeException("检查失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> getProjectOperationsByProjectId(Long projectId) {
        try {
            log.info("根据项目ID查询项目经营管理列表，项目ID：{}", projectId);
            
            if (projectId == null) {
                throw new IllegalArgumentException("项目ID不能为空");
            }
            
            return projectOperationsMapper.selectByProjectId(projectId);
        } catch (Exception e) {
            log.error("根据项目ID查询项目经营管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> getProjectOperationsByOperationsType(Integer operationsType) {
        try {
            log.info("根据经营类型查询项目经营管理列表，经营类型：{}", operationsType);
            
            if (operationsType == null) {
                throw new IllegalArgumentException("经营类型不能为空");
            }
            
            return projectOperationsMapper.selectByOperationsType(operationsType);
        } catch (Exception e) {
            log.error("根据经营类型查询项目经营管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> getProjectOperationsByOperationsStatus(Integer operationsStatus) {
        try {
            log.info("根据经营状态查询项目经营管理列表，经营状态：{}", operationsStatus);
            
            if (operationsStatus == null) {
                throw new IllegalArgumentException("经营状态不能为空");
            }
            
            return projectOperationsMapper.selectByOperationsStatus(operationsStatus);
        } catch (Exception e) {
            log.error("根据经营状态查询项目经营管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> getProjectOperationsByRiskLevel(Integer riskLevel) {
        try {
            log.info("根据风险等级查询项目经营管理列表，风险等级：{}", riskLevel);
            
            if (riskLevel == null) {
                throw new IllegalArgumentException("风险等级不能为空");
            }
            
            return projectOperationsMapper.selectByRiskLevel(riskLevel);
        } catch (Exception e) {
            log.error("根据风险等级查询项目经营管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> getMyProjectOperations(Long managerId) {
        try {
            log.info("根据负责人ID查询项目经营管理列表，负责人ID：{}", managerId);
            
            if (managerId == null) {
                throw new IllegalArgumentException("负责人ID不能为空");
            }
            
            return projectOperationsMapper.selectByManagerId(managerId);
        } catch (Exception e) {
            log.error("根据负责人ID查询项目经营管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> getNormalOperations() {
        try {
            log.info("获取正常状态的项目经营管理列表");
            return projectOperationsMapper.selectNormalOperations();
        } catch (Exception e) {
            log.error("获取正常状态的项目经营管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> getWarningOperations() {
        try {
            log.info("获取预警状态的项目经营管理列表");
            return projectOperationsMapper.selectWarningOperations();
        } catch (Exception e) {
            log.error("获取预警状态的项目经营管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> getAbnormalOperations() {
        try {
            log.info("获取异常状态的项目经营管理列表");
            return projectOperationsMapper.selectAbnormalOperations();
        } catch (Exception e) {
            log.error("获取异常状态的项目经营管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> getHighRiskOperations() {
        try {
            log.info("获取高风险的项目经营管理列表");
            return projectOperationsMapper.selectHighRiskOperations();
        } catch (Exception e) {
            log.error("获取高风险的项目经营管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> getProfitableOperations() {
        try {
            log.info("获取盈利的项目经营管理列表");
            return projectOperationsMapper.selectProfitableOperations();
        } catch (Exception e) {
            log.error("获取盈利的项目经营管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> getLossOperations() {
        try {
            log.info("获取亏损的项目经营管理列表");
            return projectOperationsMapper.selectLossOperations();
        } catch (Exception e) {
            log.error("获取亏损的项目经营管理列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> searchProjectOperations(String keyword, Integer limit) {
        try {
            log.info("模糊搜索项目经营管理，关键词：{}，限制数量：{}", keyword, limit);
            
            if (!StringUtils.hasText(keyword)) {
                throw new IllegalArgumentException("搜索关键词不能为空");
            }
            
            if (limit == null || limit <= 0) {
                limit = 10;
            }
            
            return projectOperationsMapper.searchProjectOperations(keyword, limit);
        } catch (Exception e) {
            log.error("模糊搜索项目经营管理失败", e);
            throw new RuntimeException("搜索失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getProjectOperationsStatistics(ProjectOperationsQueryParam param) {
        try {
            log.info("统计项目经营管理数据，参数：{}", param);
            return projectOperationsMapper.statisticsProjectOperations(param);
        } catch (Exception e) {
            log.error("统计项目经营管理数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getOperationsTypeDistribution(ProjectOperationsQueryParam param) {
        try {
            log.info("统计经营类型分布，参数：{}", param);
            return projectOperationsMapper.statisticsOperationsTypeDistribution(param);
        } catch (Exception e) {
            log.error("统计经营类型分布失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getOperationsStatusDistribution(ProjectOperationsQueryParam param) {
        try {
            log.info("统计经营状态分布，参数：{}", param);
            return projectOperationsMapper.statisticsOperationsStatusDistribution(param);
        } catch (Exception e) {
            log.error("统计经营状态分布失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getRiskLevelDistribution(ProjectOperationsQueryParam param) {
        try {
            log.info("统计风险等级分布，参数：{}", param);
            return projectOperationsMapper.statisticsRiskLevelDistribution(param);
        } catch (Exception e) {
            log.error("统计风险等级分布失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyRevenueTrend(ProjectOperationsQueryParam param) {
        try {
            log.info("统计月度收入趋势，参数：{}", param);
            return projectOperationsMapper.statisticsMonthlyRevenueTrend(param);
        } catch (Exception e) {
            log.error("统计月度收入趋势失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyCostTrend(ProjectOperationsQueryParam param) {
        try {
            log.info("统计月度成本趋势，参数：{}", param);
            return projectOperationsMapper.statisticsMonthlyCostTrend(param);
        } catch (Exception e) {
            log.error("统计月度成本趋势失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyProfitTrend(ProjectOperationsQueryParam param) {
        try {
            log.info("统计月度利润趋势，参数：{}", param);
            return projectOperationsMapper.statisticsMonthlyProfitTrend(param);
        } catch (Exception e) {
            log.error("统计月度利润趋势失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getDepartmentOperations(ProjectOperationsQueryParam param) {
        try {
            log.info("统计部门经营数据，参数：{}", param);
            return projectOperationsMapper.statisticsDepartmentOperations(param);
        } catch (Exception e) {
            log.error("统计部门经营数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getProjectOperationsData(ProjectOperationsQueryParam param) {
        try {
            log.info("统计项目经营数据，参数：{}", param);
            return projectOperationsMapper.statisticsProjectOperationsData(param);
        } catch (Exception e) {
            log.error("统计项目经营数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getManagerOperations(ProjectOperationsQueryParam param) {
        try {
            log.info("统计负责人经营数据，参数：{}", param);
            return projectOperationsMapper.statisticsManagerOperations(param);
        } catch (Exception e) {
            log.error("统计负责人经营数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateTotalRevenue(ProjectOperationsQueryParam param) {
        try {
            log.info("计算总收入，参数：{}", param);
            return projectOperationsMapper.calculateTotalRevenue(param);
        } catch (Exception e) {
            log.error("计算总收入失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateTotalCost(ProjectOperationsQueryParam param) {
        try {
            log.info("计算总成本，参数：{}", param);
            return projectOperationsMapper.calculateTotalCost(param);
        } catch (Exception e) {
            log.error("计算总成本失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateTotalProfit(ProjectOperationsQueryParam param) {
        try {
            log.info("计算总利润，参数：{}", param);
            return projectOperationsMapper.calculateTotalProfit(param);
        } catch (Exception e) {
            log.error("计算总利润失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateAverageProfitRate(ProjectOperationsQueryParam param) {
        try {
            log.info("计算平均利润率，参数：{}", param);
            return projectOperationsMapper.calculateAverageProfitRate(param);
        } catch (Exception e) {
            log.error("计算平均利润率失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateAverageGrossProfitRate(ProjectOperationsQueryParam param) {
        try {
            log.info("计算平均毛利率，参数：{}", param);
            return projectOperationsMapper.calculateAverageGrossProfitRate(param);
        } catch (Exception e) {
            log.error("计算平均毛利率失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateAverageNetProfitRate(ProjectOperationsQueryParam param) {
        try {
            log.info("计算平均净利率，参数：{}", param);
            return projectOperationsMapper.calculateAverageNetProfitRate(param);
        } catch (Exception e) {
            log.error("计算平均净利率失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateAverageROI(ProjectOperationsQueryParam param) {
        try {
            log.info("计算平均投资回报率，参数：{}", param);
            return projectOperationsMapper.calculateAverageROI(param);
        } catch (Exception e) {
            log.error("计算平均投资回报率失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateTotalCashFlow(ProjectOperationsQueryParam param) {
        try {
            log.info("计算总现金流，参数：{}", param);
            return projectOperationsMapper.calculateTotalCashFlow(param);
        } catch (Exception e) {
            log.error("计算总现金流失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateAverageRiskScore(ProjectOperationsQueryParam param) {
        try {
            log.info("计算平均风险评分，参数：{}", param);
            return projectOperationsMapper.calculateAverageRiskScore(param);
        } catch (Exception e) {
            log.error("计算平均风险评分失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateAveragePerformanceScore(ProjectOperationsQueryParam param) {
        try {
            log.info("计算平均绩效评分，参数：{}", param);
            return projectOperationsMapper.calculateAveragePerformanceScore(param);
        } catch (Exception e) {
            log.error("计算平均绩效评分失败", e);
            throw new RuntimeException("计算失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getOperationsHealthAnalysis(ProjectOperationsQueryParam param) {
        try {
            log.info("获取经营健康度分析，参数：{}", param);
            return projectOperationsMapper.getOperationsHealthAnalysis(param);
        } catch (Exception e) {
            log.error("获取经营健康度分析失败", e);
            throw new RuntimeException("分析失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getRiskWarningList(ProjectOperationsQueryParam param) {
        try {
            log.info("获取风险预警列表，参数：{}", param);
            return projectOperationsMapper.getRiskWarningList(param);
        } catch (Exception e) {
            log.error("获取风险预警列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getPerformanceRanking(ProjectOperationsQueryParam param) {
        try {
            log.info("获取绩效排名，参数：{}", param);
            return projectOperationsMapper.getPerformanceRanking(param);
        } catch (Exception e) {
            log.error("获取绩效排名失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectOperations> exportProjectOperations(ProjectOperationsQueryParam param) {
        try {
            log.info("导出项目经营管理数据，参数：{}", param);
            return projectOperationsMapper.exportProjectOperations(param);
        } catch (Exception e) {
            log.error("导出项目经营管理数据失败", e);
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchUpdateOperationsStatus(List<Long> ids, Integer operationsStatus, Long updateBy) {
        try {
            log.info("批量更新经营状态，ID列表：{}，经营状态：{}，更新人：{}", ids, operationsStatus, updateBy);

            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }
            if (operationsStatus == null) {
                throw new IllegalArgumentException("经营状态不能为空");
            }

            return projectOperationsMapper.batchUpdateOperationsStatus(ids, operationsStatus, updateBy) > 0;
        } catch (Exception e) {
            log.error("批量更新经营状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchUpdateRiskLevel(List<Long> ids, Integer riskLevel, Long updateBy) {
        try {
            log.info("批量更新风险等级，ID列表：{}，风险等级：{}，更新人：{}", ids, riskLevel, updateBy);

            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }
            if (riskLevel == null) {
                throw new IllegalArgumentException("风险等级不能为空");
            }

            return projectOperationsMapper.batchUpdateRiskLevel(ids, riskLevel, updateBy) > 0;
        } catch (Exception e) {
            log.error("批量更新风险等级失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    public boolean restoreProjectOperations(List<Long> ids, Long updateBy) {
        try {
            log.info("恢复删除的项目经营管理，ID列表：{}，更新人：{}", ids, updateBy);

            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }

            return projectOperationsMapper.restoreProjectOperations(ids, updateBy) > 0;
        } catch (Exception e) {
            log.error("恢复删除的项目经营管理失败", e);
            throw new RuntimeException("恢复失败：" + e.getMessage());
        }
    }

    @Override
    public boolean physicalDeleteProjectOperations(List<Long> ids) {
        try {
            log.info("物理删除项目经营管理，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                throw new IllegalArgumentException("ID列表不能为空");
            }

            return projectOperationsMapper.physicalDeleteProjectOperations(ids) > 0;
        } catch (Exception e) {
            log.error("物理删除项目经营管理失败", e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    /**
     * 生成经营编号
     *
     * @return 经营编号
     */
    private String generateOperationsNo() {
        // 生成格式：JY + 年月日 + 4位序号
        String dateStr = String.format("%1$tY%1$tm%1$td", new Date());
        String prefix = "JY" + dateStr;

        // 这里可以根据实际需求实现序号生成逻辑
        int sequence = (int) (Math.random() * 9999) + 1;
        return prefix + String.format("%04d", sequence);
    }
}
