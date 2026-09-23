package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.ProjectBriefing;
import com.huabo.contract.mapper.ProjectBriefingMapper;
import com.huabo.contract.service.ProjectBriefingService;
import com.huabo.contract.vo.ProjectBriefingQueryParam;
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
 * 项目交底表 服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
public class ProjectBriefingServiceImpl extends ServiceImpl<ProjectBriefingMapper, ProjectBriefing> implements ProjectBriefingService {

    @Autowired
    private ProjectBriefingMapper projectBriefingMapper;

    @Override
    public PageInfo<ProjectBriefing> getProjectBriefingList(ProjectBriefingQueryParam param) {
        try {
            log.info("分页查询项目交底列表，参数：{}", param);
            
            // 设置分页参数
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            
            // 查询数据
            List<ProjectBriefing> list = projectBriefingMapper.selectProjectBriefingList(param);
            
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("分页查询项目交底列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveProjectBriefing(ProjectBriefing projectBriefing) {
        try {
            log.info("保存项目交底，交底：{}", projectBriefing);
            
            Date now = new Date();
            
            if (projectBriefing.getId() == null) {
                // 新增
                if (!StringUtils.hasText(projectBriefing.getBriefingNo())) {
                    projectBriefing.setBriefingNo(generateBriefingNo());
                }

                // 设置默认值
                if (projectBriefing.getBriefingStatus() == null) {
                    projectBriefing.setBriefingStatus(1); // 待交底
                }
                if (projectBriefing.getConfirmationStatus() == null) {
                    projectBriefing.setConfirmationStatus(0); // 未确认
                }
                if (projectBriefing.getBrieferId() == null) {
                    projectBriefing.setBrieferId(1L); // 默认交底人ID，实际应从当前用户获取
                }
                if (projectBriefing.getBriefingTime() == null) {
                    projectBriefing.setBriefingTime(now); // 默认交底时间
                }

                projectBriefing.setCreateTime(now);
                projectBriefing.setUpdateTime(now);
                return this.save(projectBriefing);
            } else {
                // 修改
                projectBriefing.setUpdateTime(now);
                return this.updateById(projectBriefing);
            }
        } catch (Exception e) {
            log.error("保存项目交底失败", e);
            throw new RuntimeException("保存失败：" + e.getMessage());
        }
    }

    @Override
    public ProjectBriefing getProjectBriefingById(Long id) {
        try {
            log.info("根据ID获取项目交底详情，ID：{}", id);
            return this.getById(id);
        } catch (Exception e) {
            log.error("获取项目交底详情失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public ProjectBriefing getProjectBriefingByBriefingNo(String briefingNo) {
        try {
            log.info("根据交底编号获取项目交底，交底编号：{}", briefingNo);
            return projectBriefingMapper.selectByBriefingNo(briefingNo);
        } catch (Exception e) {
            log.error("根据交底编号获取项目交底失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProjectBriefing(Long id) {
        try {
            log.info("删除项目交底，ID：{}", id);
            return this.removeById(id);
        } catch (Exception e) {
            log.error("删除项目交底失败", e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteProjectBriefing(List<Long> ids) {
        try {
            log.info("批量删除项目交底，ID列表：{}", ids);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            return this.removeByIds(ids);
        } catch (Exception e) {
            log.error("批量删除项目交底失败", e);
            throw new RuntimeException("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    public String generateBriefingNo() {
        try {
            // 生成交底编号：BRIEF + 年月日 + 4位序号
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = sdf.format(new Date());
            String prefix = "BRIEF" + dateStr;

            // 查询当天最大序号
            QueryWrapper<ProjectBriefing> queryWrapper = new QueryWrapper<>();
            queryWrapper.likeRight("briefing_no", prefix)
                       .orderByDesc("briefing_no");

            List<ProjectBriefing> briefings = this.list(queryWrapper);
            int maxSeq = 0;
            if (!briefings.isEmpty()) {
                ProjectBriefing lastBriefing = briefings.get(0);
                if (lastBriefing != null && lastBriefing.getBriefingNo() != null) {
                    String lastNo = lastBriefing.getBriefingNo();
                    if (lastNo.length() >= prefix.length() + 4) {
                        try {
                            String seqStr = lastNo.substring(prefix.length());
                            maxSeq = Integer.parseInt(seqStr);
                        } catch (NumberFormatException e) {
                            log.warn("解析交底编号序号失败：{}", lastNo);
                        }
                    }
                }
            }

            String seq = String.format("%04d", maxSeq + 1);
            return prefix + seq;
        } catch (Exception e) {
            log.error("生成交底编号失败", e);
            throw new RuntimeException("生成交底编号失败：" + e.getMessage());
        }
    }

    @Override
    public boolean existsBriefingNo(String briefingNo, Long excludeId) {
        try {
            log.info("检查交底编号是否存在，交底编号：{}，排除ID：{}", briefingNo, excludeId);
            return projectBriefingMapper.existsBriefingNo(briefingNo, excludeId);
        } catch (Exception e) {
            log.error("检查交底编号是否存在失败", e);
            throw new RuntimeException("检查失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBriefingStatus(Long id, Integer briefingStatus) {
        try {
            log.info("更新交底状态，ID：{}，交底状态：{}", id, briefingStatus);
            
            ProjectBriefing briefing = new ProjectBriefing();
            briefing.setId(id);
            briefing.setBriefingStatus(briefingStatus);
            briefing.setUpdateTime(new Date());
            
            return this.updateById(briefing);
        } catch (Exception e) {
            log.error("更新交底状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateBriefingStatus(List<Long> ids, Integer briefingStatus) {
        try {
            log.info("批量更新交底状态，ID列表：{}，交底状态：{}", ids, briefingStatus);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            
            int count = projectBriefingMapper.batchUpdateBriefingStatus(ids, briefingStatus, null);
            return count > 0;
        } catch (Exception e) {
            log.error("批量更新交底状态失败", e);
            throw new RuntimeException("批量更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCompletionRate(Long id, BigDecimal completionRate) {
        try {
            log.info("更新完成度，ID：{}，完成度：{}", id, completionRate);
            
            int count = projectBriefingMapper.updateCompletionRate(id, completionRate);
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
            
            int count = projectBriefingMapper.batchUpdateCompletionRate(ids, completionRate, null);
            return count > 0;
        } catch (Exception e) {
            log.error("批量更新完成度失败", e);
            throw new RuntimeException("批量更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean conductBriefing(Long id, Long brieferId, String brieferName, String briefingLocation, String participants) {
        try {
            log.info("进行交底，ID：{}，交底人：{}，交底地点：{}", id, brieferName, briefingLocation);
            
            ProjectBriefing briefing = new ProjectBriefing();
            briefing.setId(id);
            briefing.setBrieferId(brieferId);
            // brieferName字段已删除，不再设置
            briefing.setBriefingTime(new Date());
            briefing.setBriefingLocation(briefingLocation);
            briefing.setParticipants(participants);
            briefing.setBriefingStatus(3); // 已交底
            briefing.setUpdateTime(new Date());
            
            return this.updateById(briefing);
        } catch (Exception e) {
            log.error("进行交底失败", e);
            throw new RuntimeException("交底失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean receiveBriefing(Long id, Long receiverId, String receiverName) {
        try {
            log.info("接收交底，ID：{}，接收人：{}", id, receiverName);
            
            ProjectBriefing briefing = new ProjectBriefing();
            briefing.setId(id);
            // receiverId, receiverName, receiveTime字段已删除，不再设置
            briefing.setBriefingStatus(4); // 已接收
            briefing.setUpdateTime(new Date());
            
            return this.updateById(briefing);
        } catch (Exception e) {
            log.error("接收交底失败", e);
            throw new RuntimeException("接收失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmBriefing(Long id, Long confirmerId, String confirmerName, String confirmComments) {
        try {
            log.info("确认交底，ID：{}，确认人：{}", id, confirmerName);
            
            ProjectBriefing briefing = new ProjectBriefing();
            briefing.setId(id);
            briefing.setConfirmerId(confirmerId);
            briefing.setConfirmationDate(new Date());
            // confirmerName, confirmComments字段已删除，不再设置
            briefing.setBriefingStatus(5); // 已确认
            briefing.setUpdateTime(new Date());
            
            return this.updateById(briefing);
        } catch (Exception e) {
            log.error("确认交底失败", e);
            throw new RuntimeException("确认失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeBriefing(Long id, String executionStatus) {
        try {
            log.info("完成交底，ID：{}", id);
            
            ProjectBriefing briefing = new ProjectBriefing();
            briefing.setId(id);
            briefing.setBriefingStatus(6); // 已完成
            // executionStatus, completionRate字段已删除，不再设置
            briefing.setUpdateTime(new Date());
            
            return this.updateById(briefing);
        } catch (Exception e) {
            log.error("完成交底失败", e);
            throw new RuntimeException("完成交底失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelBriefing(Long id) {
        try {
            log.info("取消交底，ID：{}", id);
            return updateBriefingStatus(id, 7); // 已取消
        } catch (Exception e) {
            log.error("取消交底失败", e);
            throw new RuntimeException("取消交底失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean acceptBriefing(Long id, Long acceptorId, String acceptorName, String acceptanceResult) {
        try {
            log.info("验收交底，ID：{}，验收人：{}", id, acceptorName);
            
            ProjectBriefing briefing = new ProjectBriefing();
            briefing.setId(id);
            // acceptorId, acceptorName, acceptanceTime, acceptanceResult字段已删除，不再设置
            briefing.setUpdateTime(new Date());
            
            return this.updateById(briefing);
        } catch (Exception e) {
            log.error("验收交底失败", e);
            throw new RuntimeException("验收失败：" + e.getMessage());
        }
    }

    // 其他方法的简化实现...
    @Override
    public List<ProjectBriefing> getDraftBriefings() {
        return projectBriefingMapper.selectDraftBriefings();
    }

    @Override
    public List<ProjectBriefing> getPendingBriefings() {
        return projectBriefingMapper.selectPendingBriefings();
    }

    @Override
    public List<ProjectBriefing> getBriefedBriefings() {
        return projectBriefingMapper.selectBriefedBriefings();
    }

    @Override
    public List<ProjectBriefing> getReceivedBriefings() {
        return projectBriefingMapper.selectReceivedBriefings();
    }

    @Override
    public List<ProjectBriefing> getConfirmedBriefings() {
        return projectBriefingMapper.selectConfirmedBriefings();
    }

    @Override
    public List<ProjectBriefing> getCompletedBriefings() {
        return projectBriefingMapper.selectCompletedBriefings();
    }

    @Override
    public List<ProjectBriefing> getCancelledBriefings() {
        return projectBriefingMapper.selectCancelledBriefings();
    }

    @Override
    public List<ProjectBriefing> getUrgentBriefings() {
        return projectBriefingMapper.selectUrgentBriefings();
    }

    @Override
    public List<ProjectBriefing> getImportantBriefings() {
        return projectBriefingMapper.selectImportantBriefings();
    }

    @Override
    public List<ProjectBriefing> getHighPriorityBriefings() {
        return projectBriefingMapper.selectHighPriorityBriefings();
    }

    @Override
    public List<ProjectBriefing> getMyBriefings(Long userId) {
        return projectBriefingMapper.selectMyBriefings(userId);
    }

    @Override
    public List<ProjectBriefing> getMyReceivedBriefings(Long userId) {
        return projectBriefingMapper.selectMyReceivedBriefings(userId);
    }

    @Override
    public List<ProjectBriefing> getMyConfirmedBriefings(Long userId) {
        return projectBriefingMapper.selectMyConfirmedBriefings(userId);
    }

    @Override
    public List<ProjectBriefing> getMyAcceptedBriefings(Long userId) {
        return projectBriefingMapper.selectMyAcceptedBriefings(userId);
    }

    @Override
    public List<ProjectBriefing> getProjectBriefingByProjectId(Long projectId) {
        return projectBriefingMapper.selectByProjectId(projectId);
    }

    @Override
    public Map<String, Object> getProjectBriefingStatistics(ProjectBriefingQueryParam param) {
        return projectBriefingMapper.selectProjectBriefingStatistics(param);
    }

    @Override
    public List<Map<String, Object>> getBriefingTypeDistribution(ProjectBriefingQueryParam param) {
        return projectBriefingMapper.selectBriefingTypeDistribution(param);
    }

    @Override
    public List<Map<String, Object>> getBriefingStatusDistribution(ProjectBriefingQueryParam param) {
        return projectBriefingMapper.selectBriefingStatusDistribution(param);
    }

    @Override
    public List<Map<String, Object>> getMonthlyBriefingTrend(ProjectBriefingQueryParam param) {
        return projectBriefingMapper.selectMonthlyBriefingTrend(param);
    }

    @Override
    public List<ProjectBriefing> searchProjectBriefings(String keyword, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return projectBriefingMapper.searchProjectBriefings(keyword, limit);
    }

    @Override
    public List<ProjectBriefing> getTodayBriefings() {
        return projectBriefingMapper.selectTodayBriefings();
    }

    @Override
    public List<ProjectBriefing> getThisWeekBriefings() {
        return projectBriefingMapper.selectThisWeekBriefings();
    }

    @Override
    public List<ProjectBriefing> getThisMonthBriefings() {
        return projectBriefingMapper.selectThisMonthBriefings();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importProjectBriefings(List<ProjectBriefing> briefingList) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errorMessages = new ArrayList<>();
        
        for (ProjectBriefing briefing : briefingList) {
            try {
                if (!StringUtils.hasText(briefing.getBriefingNo())) {
                    briefing.setBriefingNo(generateBriefingNo());
                }
                
                if (existsBriefingNo(briefing.getBriefingNo(), null)) {
                    failCount++;
                    errorMessages.add("交底编号 " + briefing.getBriefingNo() + " 已存在");
                    continue;
                }
                
                briefing.setCreateTime(new Date());
                briefing.setUpdateTime(new Date());
                
                if (this.save(briefing)) {
                    successCount++;
                } else {
                    failCount++;
                    errorMessages.add("保存交底 " + briefing.getBriefingTitle() + " 失败");
                }
            } catch (Exception e) {
                failCount++;
                errorMessages.add("导入交底 " + briefing.getBriefingTitle() + " 异常：" + e.getMessage());
            }
        }
        
        result.put("total", briefingList.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errorMessages", errorMessages);
        
        return result;
    }

    @Override
    public List<ProjectBriefing> exportProjectBriefings(ProjectBriefingQueryParam param) {
        return projectBriefingMapper.selectProjectBriefingList(param);
    }

    @Override
    public int sendBriefingReminders() {
        // 发送交底提醒的逻辑
        List<ProjectBriefing> pendingBriefings = getPendingBriefings();
        // 这里可以添加发送提醒的逻辑
        return pendingBriefings.size();
    }

    @Override
    public Map<String, Object> getBriefingEffectiveness(ProjectBriefingQueryParam param) {
        return projectBriefingMapper.selectBriefingEffectiveness(param);
    }
}
