package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.ProjectBudget;
import com.huabo.contract.mapper.ProjectBudgetMapper;
import com.huabo.contract.service.ProjectBudgetService;
import com.huabo.contract.vo.ProjectBudgetQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 项目预算表 服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
public class ProjectBudgetServiceImpl extends ServiceImpl<ProjectBudgetMapper, ProjectBudget> implements ProjectBudgetService {

    @Autowired
    private ProjectBudgetMapper projectBudgetMapper;

    @Override
    public PageInfo<ProjectBudget> getProjectBudgetList(ProjectBudgetQueryParam param) {
        try {
            log.info("分页查询项目预算列表，参数：{}", param);
            
            // 设置分页参数
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            
            // 查询数据
            List<ProjectBudget> list = projectBudgetMapper.selectProjectBudgetList(param);
            
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("分页查询项目预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveProjectBudget(ProjectBudget projectBudget) {
        try {
            log.info("保存项目预算，预算：{}", projectBudget);

            // 验证预算总金额
            if (projectBudget.getTotalBudget() == null || projectBudget.getTotalBudget().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("预算总金额必须大于0");
            }

            Date now = new Date();

            if (projectBudget.getId() == null) {
                // 新增
                if (!StringUtils.hasText(projectBudget.getBudgetNo())) {
                    projectBudget.setBudgetNo(generateBudgetNo());
                }
                projectBudget.setCreateTime(now);
                projectBudget.setUpdateTime(now);
                // 设置创建人和更新人，这里暂时设置为1，实际应该从当前登录用户获取
                projectBudget.setCreateBy(1L);
                projectBudget.setUpdateBy(1L);

                // 保存预算
                return this.save(projectBudget);
            } else {
                // 修改
                projectBudget.setUpdateTime(now);
                // 设置更新人，这里暂时设置为1，实际应该从当前登录用户获取
                projectBudget.setUpdateBy(1L);

                return this.updateById(projectBudget);
            }
        } catch (Exception e) {
            log.error("保存项目预算失败", e);
            throw new RuntimeException("保存失败：" + e.getMessage());
        }
    }

    @Override
    public ProjectBudget getProjectBudgetById(Long id) {
        try {
            log.info("根据ID获取项目预算详情，ID：{}", id);
            return this.getById(id);
        } catch (Exception e) {
            log.error("获取项目预算详情失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public ProjectBudget getProjectBudgetByBudgetNo(String budgetNo) {
        try {
            log.info("根据预算编号获取项目预算，预算编号：{}", budgetNo);
            return projectBudgetMapper.selectByBudgetNo(budgetNo);
        } catch (Exception e) {
            log.error("根据预算编号获取项目预算失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProjectBudget(Long id) {
        try {
            log.info("删除项目预算，ID：{}", id);
            return this.removeById(id);
        } catch (Exception e) {
            log.error("删除项目预算失败", e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteProjectBudget(List<Long> ids) {
        try {
            log.info("批量删除项目预算，ID列表：{}", ids);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            return this.removeByIds(ids);
        } catch (Exception e) {
            log.error("批量删除项目预算失败", e);
            throw new RuntimeException("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    public String generateBudgetNo() {
        try {
            // 生成预算编号：BUDGET + 年月日 + 4位序号
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = sdf.format(new Date());
            String prefix = "BUDGET" + dateStr;

            // 查询当天最大序号
            int maxSeq = 0;
            try {
                // 查询当天已存在的最大序号
                String maxBudgetNo = projectBudgetMapper.getMaxBudgetNoByDate(dateStr);
                if (StringUtils.hasText(maxBudgetNo) && maxBudgetNo.length() >= 4) {
                    String seqStr = maxBudgetNo.substring(maxBudgetNo.length() - 4);
                    maxSeq = Integer.parseInt(seqStr);
                }
            } catch (Exception e) {
                log.warn("查询当天最大预算编号失败，使用默认序号：{}", e.getMessage());
            }

            String seq = String.format("%04d", maxSeq + 1);
            return prefix + seq;
        } catch (Exception e) {
            log.error("生成预算编号失败", e);
            throw new RuntimeException("生成预算编号失败：" + e.getMessage());
        }
    }

    @Override
    public boolean existsBudgetNo(String budgetNo, Long excludeId) {
        try {
            log.info("检查预算编号是否存在，预算编号：{}，排除ID：{}", budgetNo, excludeId);
            return projectBudgetMapper.existsBudgetNo(budgetNo, excludeId);
        } catch (Exception e) {
            log.error("检查预算编号是否存在失败", e);
            throw new RuntimeException("检查失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBudgetStatus(Long id, Integer budgetStatus) {
        try {
            log.info("更新预算状态，ID：{}，预算状态：{}", id, budgetStatus);
            
            ProjectBudget budget = new ProjectBudget();
            budget.setId(id);
            budget.setBudgetStatus(budgetStatus);
            budget.setUpdateTime(new Date());
            
            return this.updateById(budget);
        } catch (Exception e) {
            log.error("更新预算状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateBudgetStatus(List<Long> ids, Integer budgetStatus) {
        try {
            log.info("批量更新预算状态，ID列表：{}，预算状态：{}", ids, budgetStatus);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            
            int count = projectBudgetMapper.batchUpdateBudgetStatus(ids, budgetStatus, null);
            return count > 0;
        } catch (Exception e) {
            log.error("批量更新预算状态失败", e);
            throw new RuntimeException("批量更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUsedAmount(Long id, BigDecimal usedAmount) {
        try {
            log.info("更新使用金额，ID：{}，使用金额：{}", id, usedAmount);
            
            int count = projectBudgetMapper.updateUsedAmount(id, usedAmount);
            return count > 0;
        } catch (Exception e) {
            log.error("更新使用金额失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateUsedAmount(List<Long> ids, BigDecimal usedAmount) {
        try {
            log.info("批量更新使用金额，ID列表：{}，使用金额：{}", ids, usedAmount);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            
            int count = projectBudgetMapper.batchUpdateUsedAmount(ids, usedAmount, null);
            return count > 0;
        } catch (Exception e) {
            log.error("批量更新使用金额失败", e);
            throw new RuntimeException("批量更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reviewBudget(Long id, Long reviewerId, String reviewerName, String reviewComments, boolean approved) {
        try {
            log.info("审核预算，ID：{}，审核人：{}，是否通过：{}", id, reviewerName, approved);
            
            ProjectBudget budget = new ProjectBudget();
            budget.setId(id);
            budget.setReviewerId(reviewerId);
            budget.setReviewDate(new Date());
            budget.setReviewComments(reviewComments);
            budget.setBudgetStatus(approved ? 3 : 1); // 通过：已审核，不通过：草稿
            budget.setUpdateTime(new Date());
            
            return this.updateById(budget);
        } catch (Exception e) {
            log.error("审核预算失败", e);
            throw new RuntimeException("审核失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveBudget(Long id, Long approverId, String approverName, String approveComments, boolean approved) {
        try {
            log.info("批准预算，ID：{}，批准人：{}，是否通过：{}", id, approverName, approved);
            
            ProjectBudget budget = new ProjectBudget();
            budget.setId(id);
            budget.setApproverId(approverId);
            budget.setApprovalDate(new Date());
            budget.setApprovalComments(approveComments);
            budget.setBudgetStatus(approved ? 4 : 3); // 通过：已批准，不通过：已审核
            budget.setUpdateTime(new Date());
            
            return this.updateById(budget);
        } catch (Exception e) {
            log.error("批准预算失败", e);
            throw new RuntimeException("批准失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startBudget(Long id) {
        try {
            log.info("开始执行预算，ID：{}", id);
            return updateBudgetStatus(id, 5); // 执行中
        } catch (Exception e) {
            log.error("开始执行预算失败", e);
            throw new RuntimeException("开始执行失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeBudget(Long id) {
        try {
            log.info("完成预算，ID：{}", id);
            return updateBudgetStatus(id, 6); // 已完成
        } catch (Exception e) {
            log.error("完成预算失败", e);
            throw new RuntimeException("完成预算失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean voidBudget(Long id) {
        try {
            log.info("作废预算，ID：{}", id);
            return updateBudgetStatus(id, 7); // 已作废
        } catch (Exception e) {
            log.error("作废预算失败", e);
            throw new RuntimeException("作废预算失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> getPendingReviewBudgets() {
        try {
            log.info("获取待审核的预算列表");
            return projectBudgetMapper.selectPendingReviewBudgets();
        } catch (Exception e) {
            log.error("获取待审核的预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> getInProgressBudgets() {
        try {
            log.info("获取执行中的预算列表");
            return projectBudgetMapper.selectInProgressBudgets();
        } catch (Exception e) {
            log.error("获取执行中的预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> getCompletedBudgets() {
        try {
            log.info("获取已完成的预算列表");
            return projectBudgetMapper.selectCompletedBudgets();
        } catch (Exception e) {
            log.error("获取已完成的预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> getOverBudgets() {
        try {
            log.info("获取超预算的预算列表");
            return projectBudgetMapper.selectOverBudgets();
        } catch (Exception e) {
            log.error("获取超预算的预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> getTightBudgets() {
        try {
            log.info("获取预算紧张的预算列表");
            return projectBudgetMapper.selectTightBudgets();
        } catch (Exception e) {
            log.error("获取预算紧张的预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> getLargeBudgets(BigDecimal minAmount) {
        try {
            log.info("获取大额预算列表，最小金额：{}", minAmount);
            if (minAmount == null) {
                minAmount = new BigDecimal("10000000"); // 默认1000万
            }
            return projectBudgetMapper.selectLargeBudgets(minAmount);
        } catch (Exception e) {
            log.error("获取大额预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> getMyResponsibleBudgets(Long userId) {
        try {
            log.info("获取我负责的预算列表，用户ID：{}", userId);
            return projectBudgetMapper.selectMyResponsibleBudgets(userId);
        } catch (Exception e) {
            log.error("获取我负责的预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> getMyParticipateBudgets(Long userId) {
        try {
            log.info("获取我参与的预算列表，用户ID：{}", userId);
            return projectBudgetMapper.selectMyParticipateBudgets(userId);
        } catch (Exception e) {
            log.error("获取我参与的预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> getProjectBudgetByProjectId(Long projectId) {
        try {
            log.info("根据项目ID查询预算列表，项目ID：{}", projectId);
            return projectBudgetMapper.selectByProjectId(projectId);
        } catch (Exception e) {
            log.error("根据项目ID查询预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getProjectBudgetStatistics(ProjectBudgetQueryParam param) {
        try {
            log.info("统计项目预算数据，参数：{}", param);
            return projectBudgetMapper.selectProjectBudgetStatistics(param);
        } catch (Exception e) {
            log.error("统计项目预算数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getBudgetTypeDistribution(ProjectBudgetQueryParam param) {
        try {
            log.info("获取预算类型分布统计，参数：{}", param);
            return projectBudgetMapper.selectBudgetTypeDistribution(param);
        } catch (Exception e) {
            log.error("获取预算类型分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getBudgetStatusDistribution(ProjectBudgetQueryParam param) {
        try {
            log.info("获取预算状态分布统计，参数：{}", param);
            return projectBudgetMapper.selectBudgetStatusDistribution(param);
        } catch (Exception e) {
            log.error("获取预算状态分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyBudgetTrend(ProjectBudgetQueryParam param) {
        try {
            log.info("获取月度预算趋势，参数：{}", param);
            return projectBudgetMapper.selectMonthlyBudgetTrend(param);
        } catch (Exception e) {
            log.error("获取月度预算趋势失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getBudgetAmountDistribution(ProjectBudgetQueryParam param) {
        try {
            log.info("获取预算金额分布统计，参数：{}", param);
            return projectBudgetMapper.selectBudgetAmountDistribution(param);
        } catch (Exception e) {
            log.error("获取预算金额分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getUsageRateDistribution(ProjectBudgetQueryParam param) {
        try {
            log.info("获取使用率分布统计，参数：{}", param);
            return projectBudgetMapper.selectUsageRateDistribution(param);
        } catch (Exception e) {
            log.error("获取使用率分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getCompilerDistribution(ProjectBudgetQueryParam param) {
        try {
            log.info("获取编制人分布统计，参数：{}", param);
            return projectBudgetMapper.selectCompilerDistribution(param);
        } catch (Exception e) {
            log.error("获取编制人分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> searchProjectBudgets(String keyword, Integer limit) {
        try {
            log.info("模糊搜索项目预算，关键词：{}，限制数量：{}", keyword, limit);
            if (limit == null || limit <= 0) {
                limit = 10;
            }
            return projectBudgetMapper.searchProjectBudgets(keyword, limit);
        } catch (Exception e) {
            log.error("模糊搜索项目预算失败", e);
            throw new RuntimeException("搜索失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> getTodayExpiringBudgets() {
        try {
            log.info("获取今日到期的预算列表");
            return projectBudgetMapper.selectTodayExpiringBudgets();
        } catch (Exception e) {
            log.error("获取今日到期的预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> getThisWeekExpiringBudgets() {
        try {
            log.info("获取本周到期的预算列表");
            return projectBudgetMapper.selectThisWeekExpiringBudgets();
        } catch (Exception e) {
            log.error("获取本周到期的预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> getThisMonthExpiringBudgets() {
        try {
            log.info("获取本月到期的预算列表");
            return projectBudgetMapper.selectThisMonthExpiringBudgets();
        } catch (Exception e) {
            log.error("获取本月到期的预算列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importProjectBudgets(List<ProjectBudget> budgetList) {
        try {
            log.info("导入项目预算，数量：{}", budgetList.size());

            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();

            for (ProjectBudget budget : budgetList) {
                try {
                    // 设置默认值
                    if (!StringUtils.hasText(budget.getBudgetNo())) {
                        budget.setBudgetNo(generateBudgetNo());
                    }

                    // 检查预算编号是否重复
                    if (existsBudgetNo(budget.getBudgetNo(), null)) {
                        failCount++;
                        errorMessages.add("预算编号 " + budget.getBudgetNo() + " 已存在");
                        continue;
                    }

                    budget.setCreateTime(new Date());
                    budget.setUpdateTime(new Date());
                    // 设置创建人和更新人，这里暂时设置为1，实际应该从当前登录用户获取
                    budget.setCreateBy(1L);
                    budget.setUpdateBy(1L);

                    if (this.save(budget)) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMessages.add("保存预算 " + budget.getBudgetName() + " 失败");
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add("导入预算 " + budget.getBudgetName() + " 异常：" + e.getMessage());
                }
            }

            result.put("total", budgetList.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);

            return result;
        } catch (Exception e) {
            log.error("导入项目预算失败", e);
            throw new RuntimeException("导入失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectBudget> exportProjectBudgets(ProjectBudgetQueryParam param) {
        try {
            log.info("导出项目预算，参数：{}", param);
            return projectBudgetMapper.selectProjectBudgetList(param);
        } catch (Exception e) {
            log.error("导出项目预算失败", e);
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int autoUpdateExpiredBudgets() {
        try {
            log.info("自动更新过期预算状态");

            // 获取超预算但状态未更新的预算
            List<ProjectBudget> overBudgets = getOverBudgets();
            if (CollectionUtils.isEmpty(overBudgets)) {
                return 0;
            }

            List<Long> ids = new ArrayList<>();
            for (ProjectBudget budget : overBudgets) {
                if (budget.getBudgetStatus() != null && budget.getBudgetStatus() == 5) {
                    ids.add(budget.getId());
                }
            }

            if (CollectionUtils.isEmpty(ids)) {
                return 0;
            }

            // 批量更新为已完成状态
            batchUpdateBudgetStatus(ids, 6);
            return ids.size();
        } catch (Exception e) {
            log.error("自动更新过期预算状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    public int sendBudgetWarnings() {
        try {
            log.info("发送预算预警通知");

            List<ProjectBudget> tightBudgets = getTightBudgets();
            if (CollectionUtils.isEmpty(tightBudgets)) {
                return 0;
            }

            // 这里可以添加发送预警的逻辑，比如发送邮件、短信等
            // 暂时只记录日志
            for (ProjectBudget budget : tightBudgets) {
                log.info("预算 {} 预算紧张，总预算：{}", budget.getBudgetName(), budget.getTotalBudget());
            }

            return tightBudgets.size();
        } catch (Exception e) {
            log.error("发送预算预警通知失败", e);
            throw new RuntimeException("发送预警失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getYearlyBudgetSummary(Integer year) {
        try {
            log.info("获取年度预算汇总，年度：{}", year);
            return projectBudgetMapper.selectYearlyBudgetSummary(year);
        } catch (Exception e) {
            log.error("获取年度预算汇总失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getQuarterlyBudgetSummary(Integer year, Integer quarter) {
        try {
            log.info("获取季度预算汇总，年度：{}，季度：{}", year, quarter);
            return projectBudgetMapper.selectQuarterlyBudgetSummary(year, quarter);
        } catch (Exception e) {
            log.error("获取季度预算汇总失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getMonthlyBudgetSummary(Integer year, Integer month) {
        try {
            log.info("获取月度预算汇总，年度：{}，月份：{}", year, month);
            return projectBudgetMapper.selectMonthlyBudgetSummary(year, month);
        } catch (Exception e) {
            log.error("获取月度预算汇总失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getDepartmentBudgetSummary(Long departmentId, Integer year) {
        try {
            log.info("获取部门预算汇总，部门ID：{}，年度：{}", departmentId, year);
            return projectBudgetMapper.selectDepartmentBudgetSummary(departmentId, year);
        } catch (Exception e) {
            log.error("获取部门预算汇总失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getProjectBudgetSummary(Long projectId) {
        try {
            log.info("获取项目预算汇总，项目ID：{}", projectId);
            return projectBudgetMapper.selectProjectBudgetSummary(projectId);
        } catch (Exception e) {
            log.error("获取项目预算汇总失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getBudgetExecution(Long budgetId) {
        try {
            log.info("获取预算执行情况，预算ID：{}", budgetId);
            return projectBudgetMapper.selectBudgetExecution(budgetId);
        } catch (Exception e) {
            log.error("获取预算执行情况失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getBudgetComparison(Long budgetId1, Long budgetId2) {
        try {
            log.info("获取预算对比分析，预算ID1：{}，预算ID2：{}", budgetId1, budgetId2);
            return projectBudgetMapper.selectBudgetComparison(budgetId1, budgetId2);
        } catch (Exception e) {
            log.error("获取预算对比分析失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getBudgetWarnings(ProjectBudgetQueryParam param) {
        try {
            log.info("获取预算预警信息，参数：{}", param);
            return projectBudgetMapper.selectBudgetWarnings(param);
        } catch (Exception e) {
            log.error("获取预算预警信息失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }
}
