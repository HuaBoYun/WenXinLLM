package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.ProjectPlanning;
import com.huabo.contract.mapper.ProjectPlanningMapper;
import com.huabo.contract.service.ProjectPlanningService;
import com.huabo.contract.vo.ProjectPlanningQueryParam;
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
 * 项目策划表 服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
public class ProjectPlanningServiceImpl extends ServiceImpl<ProjectPlanningMapper, ProjectPlanning> implements ProjectPlanningService {

    @Autowired
    private ProjectPlanningMapper projectPlanningMapper;

    @Override
    public PageInfo<ProjectPlanning> getProjectPlanningList(ProjectPlanningQueryParam param) {
        try {
            log.info("分页查询项目策划列表，参数：{}", param);
            
            // 设置分页参数
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            
            // 查询数据
            List<ProjectPlanning> list = projectPlanningMapper.selectProjectPlanningList(param);
            
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("分页查询项目策划列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveProjectPlanning(ProjectPlanning projectPlanning) {
        try {
            log.info("保存项目策划，策划：{}", projectPlanning);

            Date now = new Date();

            if (projectPlanning.getId() == null) {
                // 新增
                if (!StringUtils.hasText(projectPlanning.getPlanningNo())) {
                    projectPlanning.setPlanningNo(generatePlanningNo());
                }
                projectPlanning.setCreateTime(now);
                projectPlanning.setUpdateTime(now);

                // 使用insert方法并检查受影响行数
                int insertResult = projectPlanningMapper.insert(projectPlanning);
                log.info("插入结果：受影响行数 = {}, 生成的ID = {}", insertResult, projectPlanning.getId());

                if (insertResult > 0) {
                    log.info("项目策划保存成功，策划编号：{}, ID：{}", projectPlanning.getPlanningNo(), projectPlanning.getId());
                    return true;
                } else {
                    log.warn("项目策划保存失败，受影响行数为0");
                    return false;
                }
            } else {
                // 修改
                projectPlanning.setUpdateTime(now);
                int updateResult = this.baseMapper.updateById(projectPlanning);
                log.info("更新结果：受影响行数 = {}", updateResult);

                if (updateResult > 0) {
                    log.info("项目策划更新成功，ID：{}", projectPlanning.getId());
                    return true;
                } else {
                    log.warn("项目策划更新失败，受影响行数为0或记录不存在");
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("保存项目策划失败", e);
            throw new RuntimeException("保存失败：" + e.getMessage());
        }
    }

    @Override
    public ProjectPlanning getProjectPlanningById(Long id) {
        try {
            log.info("根据ID获取项目策划详情，ID：{}", id);
            return this.getById(id);
        } catch (Exception e) {
            log.error("获取项目策划详情失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public ProjectPlanning getProjectPlanningByPlanningNo(String planningNo) {
        try {
            log.info("根据策划编号获取项目策划，策划编号：{}", planningNo);
            return projectPlanningMapper.selectByPlanningNo(planningNo);
        } catch (Exception e) {
            log.error("根据策划编号获取项目策划失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProjectPlanning(Long id) {
        try {
            log.info("删除项目策划，ID：{}", id);
            return this.removeById(id);
        } catch (Exception e) {
            log.error("删除项目策划失败", e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteProjectPlanning(List<Long> ids) {
        try {
            log.info("批量删除项目策划，ID列表：{}", ids);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            return this.removeByIds(ids);
        } catch (Exception e) {
            log.error("批量删除项目策划失败", e);
            throw new RuntimeException("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    public String generatePlanningNo() {
        try {
            // 生成策划编号：PLAN + 年月日 + 4位序号
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = sdf.format(new Date());
            String prefix = "PLAN" + dateStr;

            // 查询当天最大序号
            int maxSeq = projectPlanningMapper.selectMaxSequenceByPrefix(prefix);
            log.info("查询到当天最大序号：{}，前缀：{}", maxSeq, prefix);

            // 生成新的序号
            int newSeq = maxSeq + 1;
            String planningNo = prefix + String.format("%04d", newSeq);

            // 双重检查，确保编号唯一性
            while (existsPlanningNo(planningNo, null)) {
                newSeq++;
                planningNo = prefix + String.format("%04d", newSeq);
                log.warn("策划编号 {} 已存在，尝试新编号：{}", prefix + String.format("%04d", newSeq - 1), planningNo);
            }

            log.info("生成策划编号：{}", planningNo);
            return planningNo;
        } catch (Exception e) {
            log.error("生成策划编号失败", e);
            throw new RuntimeException("生成策划编号失败：" + e.getMessage());
        }
    }

    @Override
    public boolean existsPlanningNo(String planningNo, Long excludeId) {
        try {
            log.info("检查策划编号是否存在，策划编号：{}，排除ID：{}", planningNo, excludeId);
            int count = projectPlanningMapper.existsPlanningNo(planningNo, excludeId);
            return count > 0;
        } catch (Exception e) {
            log.error("检查策划编号是否存在失败", e);
            throw new RuntimeException("检查失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePlanningStatus(Long id, Integer planningStatus) {
        try {
            log.info("更新策划状态，ID：{}，策划状态：{}", id, planningStatus);
            
            ProjectPlanning planning = new ProjectPlanning();
            planning.setId(id);
            planning.setPlanningStatus(planningStatus);
            planning.setUpdateTime(new Date());
            
            return this.updateById(planning);
        } catch (Exception e) {
            log.error("更新策划状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdatePlanningStatus(List<Long> ids, Integer planningStatus) {
        try {
            log.info("批量更新策划状态，ID列表：{}，策划状态：{}", ids, planningStatus);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            
            int count = projectPlanningMapper.batchUpdatePlanningStatus(ids, planningStatus, null);
            return count > 0;
        } catch (Exception e) {
            log.error("批量更新策划状态失败", e);
            throw new RuntimeException("批量更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCompletionRate(Long id, BigDecimal completionRate) {
        try {
            log.info("更新完成度，ID：{}，完成度：{}", id, completionRate);
            
            int count = projectPlanningMapper.updateCompletionRate(id, completionRate);
            return count > 0;
        } catch (Exception e) {
            log.error("更新完成度失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateCompletionRate(List<Long> ids, BigDecimal completionRate) {
        try {
            log.info("批量更新完成度，ID列表：{}，完成度：{}", ids, completionRate);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            
            int count = projectPlanningMapper.batchUpdateCompletionRate(ids, completionRate, null);
            return count > 0;
        } catch (Exception e) {
            log.error("批量更新完成度失败", e);
            throw new RuntimeException("批量更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reviewPlanning(Long id, Long reviewerId, String reviewerName, String reviewComments, boolean approved) {
        try {
            log.info("审核策划，ID：{}，审核人：{}，是否通过：{}", id, reviewerName, approved);
            
            ProjectPlanning planning = new ProjectPlanning();
            planning.setId(id);
            planning.setReviewerId(reviewerId);
            planning.setReviewerName(reviewerName);
            planning.setReviewTime(new Date());
            planning.setReviewComments(reviewComments);
            planning.setPlanningStatus(approved ? 3 : 1); // 通过：已审核，不通过：草稿
            planning.setUpdateTime(new Date());
            
            return this.updateById(planning);
        } catch (Exception e) {
            log.error("审核策划失败", e);
            throw new RuntimeException("审核失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startPlanning(Long id) {
        try {
            log.info("开始执行策划，ID：{}", id);
            
            ProjectPlanning planning = new ProjectPlanning();
            planning.setId(id);
            planning.setPlanningStatus(4); // 执行中
            planning.setActualStartDate(new Date());
            planning.setUpdateTime(new Date());
            
            return this.updateById(planning);
        } catch (Exception e) {
            log.error("开始执行策划失败", e);
            throw new RuntimeException("开始执行失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completePlanning(Long id) {
        try {
            log.info("完成策划，ID：{}", id);
            
            ProjectPlanning planning = new ProjectPlanning();
            planning.setId(id);
            planning.setPlanningStatus(5); // 已完成
            planning.setActualEndDate(new Date());
            planning.setCompletionRate(new BigDecimal("100"));
            planning.setUpdateTime(new Date());
            
            return this.updateById(planning);
        } catch (Exception e) {
            log.error("完成策划失败", e);
            throw new RuntimeException("完成策划失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean pausePlanning(Long id) {
        try {
            log.info("暂停策划，ID：{}", id);
            return updatePlanningStatus(id, 6); // 已暂停
        } catch (Exception e) {
            log.error("暂停策划失败", e);
            throw new RuntimeException("暂停策划失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelPlanning(Long id) {
        try {
            log.info("取消策划，ID：{}", id);
            return updatePlanningStatus(id, 7); // 已取消
        } catch (Exception e) {
            log.error("取消策划失败", e);
            throw new RuntimeException("取消策划失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> getPendingReviewPlannings() {
        try {
            log.info("获取待审核的策划列表");
            return projectPlanningMapper.selectPendingReviewPlannings();
        } catch (Exception e) {
            log.error("获取待审核的策划列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> getInProgressPlannings() {
        try {
            log.info("获取执行中的策划列表");
            return projectPlanningMapper.selectInProgressPlannings();
        } catch (Exception e) {
            log.error("获取执行中的策划列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> getCompletedPlannings() {
        try {
            log.info("获取已完成的策划列表");
            return projectPlanningMapper.selectCompletedPlannings();
        } catch (Exception e) {
            log.error("获取已完成的策划列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> getDelayedPlannings() {
        try {
            log.info("获取延期的策划列表");
            return projectPlanningMapper.selectDelayedPlannings();
        } catch (Exception e) {
            log.error("获取延期的策划列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> getExpiringSoonPlannings(Integer days) {
        try {
            log.info("获取即将到期的策划列表，天数：{}", days);
            if (days == null || days <= 0) {
                days = 7; // 默认7天
            }
            return projectPlanningMapper.selectExpiringSoonPlannings(days);
        } catch (Exception e) {
            log.error("获取即将到期的策划列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> getKeyPlannings(BigDecimal minBudgetAmount) {
        try {
            log.info("获取重点策划列表，最小预算金额：{}", minBudgetAmount);
            if (minBudgetAmount == null) {
                minBudgetAmount = new BigDecimal("5000000"); // 默认500万
            }
            return projectPlanningMapper.selectKeyPlannings(minBudgetAmount);
        } catch (Exception e) {
            log.error("获取重点策划列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> getMyResponsiblePlannings(Long userId) {
        try {
            log.info("获取我负责的策划列表，用户ID：{}", userId);
            return projectPlanningMapper.selectMyResponsiblePlannings(userId);
        } catch (Exception e) {
            log.error("获取我负责的策划列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> getMyParticipatePlannings(Long userId) {
        try {
            log.info("获取我参与的策划列表，用户ID：{}", userId);
            return projectPlanningMapper.selectMyParticipatePlannings(userId);
        } catch (Exception e) {
            log.error("获取我参与的策划列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> getProjectPlanningByProjectId(Long projectId) {
        try {
            log.info("根据项目ID查询策划列表，项目ID：{}", projectId);
            return projectPlanningMapper.selectByProjectId(projectId);
        } catch (Exception e) {
            log.error("根据项目ID查询策划列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getProjectPlanningStatistics(ProjectPlanningQueryParam param) {
        try {
            log.info("统计项目策划数据，参数：{}", param);
            return projectPlanningMapper.selectProjectPlanningStatistics(param);
        } catch (Exception e) {
            log.error("统计项目策划数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getPlanningTypeDistribution(ProjectPlanningQueryParam param) {
        try {
            log.info("获取策划类型分布统计，参数：{}", param);
            return projectPlanningMapper.selectPlanningTypeDistribution(param);
        } catch (Exception e) {
            log.error("获取策划类型分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getPlanningStageDistribution(ProjectPlanningQueryParam param) {
        try {
            log.info("获取策划阶段分布统计，参数：{}", param);
            return projectPlanningMapper.selectPlanningStageDistribution(param);
        } catch (Exception e) {
            log.error("获取策划阶段分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getPlanningStatusDistribution(ProjectPlanningQueryParam param) {
        try {
            log.info("获取策划状态分布统计，参数：{}", param);
            return projectPlanningMapper.selectPlanningStatusDistribution(param);
        } catch (Exception e) {
            log.error("获取策划状态分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyPlanningTrend(ProjectPlanningQueryParam param) {
        try {
            log.info("获取月度策划趋势，参数：{}", param);
            return projectPlanningMapper.selectMonthlyPlanningTrend(param);
        } catch (Exception e) {
            log.error("获取月度策划趋势失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getBudgetAmountDistribution(ProjectPlanningQueryParam param) {
        try {
            log.info("获取预算金额分布统计，参数：{}", param);
            return projectPlanningMapper.selectBudgetAmountDistribution(param);
        } catch (Exception e) {
            log.error("获取预算金额分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getCompletionRateDistribution(ProjectPlanningQueryParam param) {
        try {
            log.info("获取完成度分布统计，参数：{}", param);
            return projectPlanningMapper.selectCompletionRateDistribution(param);
        } catch (Exception e) {
            log.error("获取完成度分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getResponsiblePersonDistribution(ProjectPlanningQueryParam param) {
        try {
            log.info("获取负责人分布统计，参数：{}", param);
            return projectPlanningMapper.selectResponsiblePersonDistribution(param);
        } catch (Exception e) {
            log.error("获取负责人分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> searchProjectPlannings(String keyword, Integer limit) {
        try {
            log.info("模糊搜索项目策划，关键词：{}，限制数量：{}", keyword, limit);
            if (limit == null || limit <= 0) {
                limit = 10;
            }
            return projectPlanningMapper.searchProjectPlannings(keyword, limit);
        } catch (Exception e) {
            log.error("模糊搜索项目策划失败", e);
            throw new RuntimeException("搜索失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> getTodayExpiringPlannings() {
        try {
            log.info("获取今日到期的策划列表");
            return projectPlanningMapper.selectTodayExpiringPlannings();
        } catch (Exception e) {
            log.error("获取今日到期的策划列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> getThisWeekExpiringPlannings() {
        try {
            log.info("获取本周到期的策划列表");
            return projectPlanningMapper.selectThisWeekExpiringPlannings();
        } catch (Exception e) {
            log.error("获取本周到期的策划列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importProjectPlannings(List<ProjectPlanning> planningList) {
        try {
            log.info("导入项目策划，数量：{}", planningList.size());

            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();

            for (ProjectPlanning planning : planningList) {
                try {
                    // 设置默认值
                    if (!StringUtils.hasText(planning.getPlanningNo())) {
                        planning.setPlanningNo(generatePlanningNo());
                    }

                    // 检查策划编号是否重复
                    if (existsPlanningNo(planning.getPlanningNo(), null)) {
                        failCount++;
                        errorMessages.add("策划编号 " + planning.getPlanningNo() + " 已存在");
                        continue;
                    }

                    planning.setCreateTime(new Date());
                    planning.setUpdateTime(new Date());

                    if (this.save(planning)) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMessages.add("保存策划 " + planning.getPlanningName() + " 失败");
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add("导入策划 " + planning.getPlanningName() + " 异常：" + e.getMessage());
                }
            }

            result.put("total", planningList.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);

            return result;
        } catch (Exception e) {
            log.error("导入项目策划失败", e);
            throw new RuntimeException("导入失败：" + e.getMessage());
        }
    }

    @Override
    public List<ProjectPlanning> exportProjectPlannings(ProjectPlanningQueryParam param) {
        try {
            log.info("导出项目策划，参数：{}", param);
            return projectPlanningMapper.selectProjectPlanningList(param);
        } catch (Exception e) {
            log.error("导出项目策划失败", e);
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int autoUpdateExpiredPlannings() {
        try {
            log.info("自动更新过期策划状态");

            // 获取延期但状态未更新的策划
            List<ProjectPlanning> delayedPlannings = getDelayedPlannings();
            if (CollectionUtils.isEmpty(delayedPlannings)) {
                return 0;
            }

            List<Long> ids = new ArrayList<>();
            for (ProjectPlanning planning : delayedPlannings) {
                if (planning.getPlanningStatus() != null && planning.getPlanningStatus() == 4) {
                    ids.add(planning.getId());
                }
            }

            if (CollectionUtils.isEmpty(ids)) {
                return 0;
            }

            // 批量更新为暂停状态
            batchUpdatePlanningStatus(ids, 6);
            return ids.size();
        } catch (Exception e) {
            log.error("自动更新过期策划状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    public int sendExpiryReminders(Integer days) {
        try {
            log.info("发送到期提醒，提前天数：{}", days);

            List<ProjectPlanning> plannings = getExpiringSoonPlannings(days);
            if (CollectionUtils.isEmpty(plannings)) {
                return 0;
            }

            // 这里可以添加发送提醒的逻辑，比如发送邮件、短信等
            // 暂时只记录日志
            for (ProjectPlanning planning : plannings) {
                log.info("策划 {} 即将到期，计划结束时间：{}", planning.getPlanningName(), planning.getPlannedEndDate());
            }

            return plannings.size();
        } catch (Exception e) {
            log.error("发送到期提醒失败", e);
            throw new RuntimeException("发送提醒失败：" + e.getMessage());
        }
    }
}
