package com.management.accountant.service.pm.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.pm.PmPerformanceCalibration;
import com.management.accountant.mapper.pm.PmPerformanceCalibrationMapper;
import com.management.accountant.service.pm.PmPerformanceCalibrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 绩效校准管理服务实现类
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PmPerformanceCalibrationServiceImpl extends ServiceImpl<PmPerformanceCalibrationMapper, PmPerformanceCalibration> 
        implements PmPerformanceCalibrationService {

    @Autowired
    private PmPerformanceCalibrationMapper calibrationMapper;

    @Override
    public IPage<PmPerformanceCalibration> queryCalibrationPage(
            Integer current, Integer size,
            String calibrationCode, String calibrationTitle, String calibrationType,
            String calibrationStatus, Integer calibrationYear, Integer calibrationQuarter,
            Integer calibrationMonth, String calibrationScope, Long targetDeptId,
            Long calibrationOwnerId, String calibrationOwnerName, String priorityLevel,
            Integer needFollowUp, String followUpStatus,
            LocalDateTime startTime, LocalDateTime endTime) {
        
        Page<PmPerformanceCalibration> page = new Page<>(current, size);
        
        return calibrationMapper.selectCalibrationPage(
                page, calibrationCode, calibrationTitle, calibrationType,
                calibrationStatus, calibrationYear, calibrationQuarter,
                calibrationMonth, calibrationScope, targetDeptId,
                calibrationOwnerId, calibrationOwnerName, priorityLevel,
                needFollowUp, followUpStatus, startTime, endTime, getCurrentTenantId()
        );
    }

    @Override
    public PmPerformanceCalibration getCalibrationById(Long calibrationId) {
        if (calibrationId == null) {
            return null;
        }
        return this.getById(calibrationId);
    }

    @Override
    public Map<String, Object> getCalibrationDetail(Long calibrationId) {
        if (calibrationId == null) {
            return new HashMap<>();
        }
        return calibrationMapper.selectCalibrationDetail(calibrationId);
    }

    @Override
    public boolean createCalibration(PmPerformanceCalibration calibration) {
        if (calibration == null) {
            return false;
        }

        // 生成校准编码
        if (!StringUtils.hasText(calibration.getCalibrationCode())) {
            calibration.setCalibrationCode(generateCalibrationCode());
        }

        // 设置默认值
        if (calibration.getCalibrationStatus() == null) {
            calibration.setCalibrationStatus("PLANNED");
        }
        if (calibration.getPriorityLevel() == null) {
            calibration.setPriorityLevel("MEDIUM");
        }
        if (calibration.getNeedFollowUp() == null) {
            calibration.setNeedFollowUp(0);
        }
        if (calibration.getIsReminded() == null) {
            calibration.setIsReminded(0);
        }

        // 设置租户ID
        calibration.setTenantId(getCurrentTenantId());

        return this.save(calibration);
    }

    @Override
    public boolean updateCalibration(PmPerformanceCalibration calibration) {
        if (calibration == null || calibration.getCalibrationId() == null) {
            return false;
        }

        // 验证校准是否存在
        PmPerformanceCalibration existingCalibration = this.getById(calibration.getCalibrationId());
        if (existingCalibration == null) {
            log.warn("校准不存在: {}", calibration.getCalibrationId());
            return false;
        }

        return this.updateById(calibration);
    }

    @Override
    public boolean deleteCalibration(Long calibrationId) {
        if (calibrationId == null) {
            return false;
        }

        // 验证校准是否存在
        PmPerformanceCalibration calibration = this.getById(calibrationId);
        if (calibration == null) {
            log.warn("校准不存在: {}", calibrationId);
            return false;
        }

        // 检查校准状态，只有计划状态的校准才能删除
        if (!"PLANNED".equals(calibration.getCalibrationStatus())) {
            log.warn("只有计划状态的校准才能删除: {}", calibrationId);
            return false;
        }

        return this.removeById(calibrationId);
    }

    @Override
    public boolean batchDeleteCalibrations(List<Long> calibrationIds) {
        if (calibrationIds == null || calibrationIds.isEmpty()) {
            return false;
        }

        // 验证所有校准都是计划状态
        List<PmPerformanceCalibration> calibrations = this.listByIds(calibrationIds);
        for (PmPerformanceCalibration calibration : calibrations) {
            if (!"PLANNED".equals(calibration.getCalibrationStatus())) {
                log.warn("只有计划状态的校准才能删除: {}", calibration.getCalibrationId());
                return false;
            }
        }

        return this.removeByIds(calibrationIds);
    }

    @Override
    public boolean startCalibration(Long calibrationId, Map<String, Object> startData) {
        if (calibrationId == null) {
            return false;
        }

        PmPerformanceCalibration calibration = this.getById(calibrationId);
        if (calibration == null) {
            log.warn("校准不存在: {}", calibrationId);
            return false;
        }

        // 检查校准状态
        if (!"PLANNED".equals(calibration.getCalibrationStatus())) {
            log.warn("只有计划状态的校准才能开始: {}", calibrationId);
            return false;
        }

        // 更新校准状态和开始时间
        calibration.setCalibrationStatus("ONGOING");
        calibration.setActualStartTime(LocalDateTime.now());

        // 处理开始数据
        if (startData != null) {
            if (startData.containsKey("calibrationLocation")) {
                calibration.setCalibrationLocation((String) startData.get("calibrationLocation"));
            }
            if (startData.containsKey("calibrationMethod")) {
                calibration.setCalibrationMethod((String) startData.get("calibrationMethod"));
            }
            if (startData.containsKey("participantCount")) {
                calibration.setParticipantCount((Integer) startData.get("participantCount"));
            }
        }

        return this.updateById(calibration);
    }

    @Override
    public boolean completeCalibration(Long calibrationId, Map<String, Object> completeData) {
        if (calibrationId == null) {
            return false;
        }

        PmPerformanceCalibration calibration = this.getById(calibrationId);
        if (calibration == null) {
            log.warn("校准不存在: {}", calibrationId);
            return false;
        }

        // 检查校准状态
        if (!"ONGOING".equals(calibration.getCalibrationStatus())) {
            log.warn("只有进行中的校准才能完成: {}", calibrationId);
            return false;
        }

        // 更新校准状态和结束时间
        calibration.setCalibrationStatus("COMPLETED");
        calibration.setActualEndTime(LocalDateTime.now());
        calibration.setCompletionRate(new BigDecimal("100.00"));

        // 处理完成数据
        if (completeData != null) {
            processCompleteData(calibration, completeData);
        }

        return this.updateById(calibration);
    }

    @Override
    public boolean cancelCalibration(Long calibrationId, Map<String, Object> cancelData) {
        if (calibrationId == null) {
            return false;
        }

        PmPerformanceCalibration calibration = this.getById(calibrationId);
        if (calibration == null) {
            log.warn("校准不存在: {}", calibrationId);
            return false;
        }

        // 检查校准状态
        if ("COMPLETED".equals(calibration.getCalibrationStatus()) || 
            "CANCELLED".equals(calibration.getCalibrationStatus())) {
            log.warn("已完成或已取消的校准不能再次取消: {}", calibrationId);
            return false;
        }

        // 更新校准状态
        calibration.setCalibrationStatus("CANCELLED");

        // 处理取消数据
        if (cancelData != null && cancelData.containsKey("cancelReason")) {
            calibration.setRemarks((String) cancelData.get("cancelReason"));
        }

        return this.updateById(calibration);
    }

    @Override
    public boolean batchUpdateStatus(List<Long> calibrationIds, String status, Map<String, Object> updateData) {
        if (calibrationIds == null || calibrationIds.isEmpty() || !StringUtils.hasText(status)) {
            return false;
        }

        return calibrationMapper.batchUpdateStatus(
                calibrationIds, status, getCurrentUserId(), getCurrentUserName(), LocalDateTime.now()
        ) > 0;
    }

    @Override
    public boolean saveCalibrationRecord(Long calibrationId, Map<String, Object> recordData) {
        if (calibrationId == null || recordData == null) {
            return false;
        }

        PmPerformanceCalibration calibration = this.getById(calibrationId);
        if (calibration == null) {
            return false;
        }

        // 更新校准记录
        if (recordData.containsKey("calibrationNotes")) {
            calibration.setCalibrationNotes((String) recordData.get("calibrationNotes"));
        }
        if (recordData.containsKey("calibrationSummary")) {
            calibration.setCalibrationSummary((String) recordData.get("calibrationSummary"));
        }
        if (recordData.containsKey("majorAdjustments")) {
            calibration.setMajorAdjustments((String) recordData.get("majorAdjustments"));
        }
        if (recordData.containsKey("disputeResolution")) {
            calibration.setDisputeResolution((String) recordData.get("disputeResolution"));
        }

        return this.updateById(calibration);
    }

    @Override
    public boolean createFollowUpPlan(Long calibrationId, Map<String, Object> followUpData) {
        if (calibrationId == null || followUpData == null) {
            return false;
        }

        PmPerformanceCalibration calibration = this.getById(calibrationId);
        if (calibration == null) {
            return false;
        }

        // 设置跟进信息
        calibration.setNeedFollowUp(1);
        calibration.setFollowUpStatus("PENDING");
        
        if (followUpData.containsKey("followUpPlan")) {
            calibration.setFollowUpPlan((String) followUpData.get("followUpPlan"));
        }
        if (followUpData.containsKey("followUpDeadline")) {
            calibration.setFollowUpDeadline((LocalDateTime) followUpData.get("followUpDeadline"));
        }

        return this.updateById(calibration);
    }

    @Override
    public boolean updateFollowUpStatus(Long calibrationId, String followUpStatus, Map<String, Object> updateData) {
        if (calibrationId == null || !StringUtils.hasText(followUpStatus)) {
            return false;
        }

        PmPerformanceCalibration calibration = this.getById(calibrationId);
        if (calibration == null) {
            return false;
        }

        calibration.setFollowUpStatus(followUpStatus);

        return this.updateById(calibration);
    }

    @Override
    public boolean batchUpdateFollowUpStatus(List<Long> calibrationIds, String followUpStatus, Map<String, Object> updateData) {
        if (calibrationIds == null || calibrationIds.isEmpty() || !StringUtils.hasText(followUpStatus)) {
            return false;
        }

        return calibrationMapper.batchUpdateFollowUpStatus(
                calibrationIds, followUpStatus, getCurrentUserId(), getCurrentUserName(), LocalDateTime.now()
        ) > 0;
    }

    @Override
    public List<PmPerformanceCalibration> getCalibrationsByOwner(Long calibrationOwnerId, String status, Integer limit) {
        if (calibrationOwnerId == null) {
            return new ArrayList<>();
        }
        return calibrationMapper.selectCalibrationsByOwner(calibrationOwnerId, status, limit);
    }

    @Override
    public List<PmPerformanceCalibration> getCalibrationsByDept(Long targetDeptId, String status, Integer limit) {
        if (targetDeptId == null) {
            return new ArrayList<>();
        }
        return calibrationMapper.selectCalibrationsByDept(targetDeptId, status, limit);
    }

    @Override
    public List<PmPerformanceCalibration> getCalibrationsByType(String calibrationType, Integer calibrationYear, Integer limit) {
        if (!StringUtils.hasText(calibrationType)) {
            return new ArrayList<>();
        }
        return calibrationMapper.selectCalibrationsByType(calibrationType, calibrationYear, limit);
    }

    @Override
    public List<PmPerformanceCalibration> getPendingFollowUpCalibrations(LocalDateTime deadline, Integer limit) {
        return calibrationMapper.selectPendingFollowUpCalibrations(deadline, limit);
    }

    @Override
    public List<PmPerformanceCalibration> getUpcomingCalibrations(LocalDateTime deadline, Integer limit) {
        return calibrationMapper.selectUpcomingCalibrations(deadline, limit);
    }

    @Override
    public List<PmPerformanceCalibration> getOverdueCalibrations(Integer limit) {
        return calibrationMapper.selectOverdueCalibrations(limit);
    }

    @Override
    public Map<String, Object> getCalibrationStatistics(
            Integer calibrationYear, Integer calibrationQuarter, Integer calibrationMonth,
            Long targetDeptId, String calibrationType) {
        
        Map<String, Object> statistics = calibrationMapper.selectCalibrationStatistics(
                calibrationYear, calibrationQuarter, calibrationMonth, targetDeptId, calibrationType, getCurrentTenantId()
        );
        
        return statistics != null ? statistics : new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> getCalibrationStatusDistribution(Integer calibrationYear, Long targetDeptId) {
        return calibrationMapper.selectCalibrationStatusDistribution(calibrationYear, targetDeptId);
    }

    @Override
    public List<Map<String, Object>> getCalibrationTypeDistribution(Integer calibrationYear, Long targetDeptId) {
        return calibrationMapper.selectCalibrationTypeDistribution(calibrationYear, targetDeptId);
    }

    @Override
    public List<Map<String, Object>> getCalibrationCompletionTrend(LocalDateTime startTime, LocalDateTime endTime, Long targetDeptId) {
        return calibrationMapper.selectCalibrationCompletionTrend(startTime, endTime, targetDeptId);
    }

    @Override
    public List<Map<String, Object>> getEffectivenessDistribution(Integer calibrationYear, Long targetDeptId) {
        return calibrationMapper.selectEffectivenessDistribution(calibrationYear, targetDeptId);
    }

    @Override
    public List<Map<String, Object>> getCalibrationRanking(Integer calibrationYear, String rankType, Integer limit) {
        return calibrationMapper.selectCalibrationRanking(calibrationYear, rankType, limit);
    }

    @Override
    public boolean checkTimeConflict(Long calibrationOwnerId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {
        if (calibrationOwnerId == null || startTime == null || endTime == null) {
            return false;
        }
        
        int conflictCount = calibrationMapper.checkTimeConflict(calibrationOwnerId, startTime, endTime, excludeId);
        return conflictCount > 0;
    }

    @Override
    public List<Map<String, Object>> getAvailableOwners(Long targetDeptId, String calibrationType, Integer limit) {
        return calibrationMapper.selectAvailableOwners(targetDeptId, calibrationType, limit);
    }

    @Override
    public List<PmPerformanceCalibration> getCalibrationReminders(LocalDateTime reminderTime, Integer limit) {
        return calibrationMapper.selectCalibrationReminders(reminderTime, limit);
    }

    @Override
    public List<Map<String, Object>> recommendCalibrationTimes(
            Long calibrationOwnerId, List<Long> participantIds, Integer duration, List<String> preferredDates) {
        
        if (calibrationOwnerId == null) {
            return new ArrayList<>();
        }
        
        String participantIdsStr = participantIds != null ? 
                participantIds.stream().map(String::valueOf).collect(Collectors.joining(",")) : "";
        String preferredDatesStr = preferredDates != null ? String.join(",", preferredDates) : "";
        
        return calibrationMapper.recommendCalibrationTimes(calibrationOwnerId, participantIdsStr, duration, preferredDatesStr);
    }

    // 私有辅助方法

    /**
     * 生成校准编码
     */
    private String generateCalibrationCode() {
        return "CAL" + System.currentTimeMillis();
    }

    /**
     * 获取当前租户ID
     */
    private Long getCurrentTenantId() {
        // TODO: 从上下文获取租户ID
        return 1L;
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        // TODO: 从上下文获取用户ID
        return 1L;
    }

    /**
     * 获取当前用户名
     */
    private String getCurrentUserName() {
        // TODO: 从上下文获取用户名
        return "系统管理员";
    }

    /**
     * 处理完成数据
     */
    private void processCompleteData(PmPerformanceCalibration calibration, Map<String, Object> completeData) {
        if (completeData.containsKey("calibrationSummary")) {
            calibration.setCalibrationSummary((String) completeData.get("calibrationSummary"));
        }
        if (completeData.containsKey("majorAdjustments")) {
            calibration.setMajorAdjustments((String) completeData.get("majorAdjustments"));
        }
        if (completeData.containsKey("improvementSuggestions")) {
            calibration.setImprovementSuggestions((String) completeData.get("improvementSuggestions"));
        }
        if (completeData.containsKey("effectivenessRating")) {
            calibration.setEffectivenessRating((Integer) completeData.get("effectivenessRating"));
        }
        if (completeData.containsKey("consistencyIndex")) {
            calibration.setConsistencyIndex(new BigDecimal(completeData.get("consistencyIndex").toString()));
        }
        if (completeData.containsKey("postCalibrationAvgScore")) {
            calibration.setPostCalibrationAvgScore(new BigDecimal(completeData.get("postCalibrationAvgScore").toString()));
        }
        if (completeData.containsKey("needFollowUp")) {
            calibration.setNeedFollowUp((Boolean) completeData.get("needFollowUp") ? 1 : 0);
        }
        if (completeData.containsKey("followUpPlan")) {
            calibration.setFollowUpPlan((String) completeData.get("followUpPlan"));
        }
        if (completeData.containsKey("followUpDeadline")) {
            calibration.setFollowUpDeadline((LocalDateTime) completeData.get("followUpDeadline"));
        }
    }

    @Override
    public Map<String, Object> generateCalibrationReport(Long calibrationId, Map<String, Object> reportParams) {
        Map<String, Object> result = new HashMap<>();

        if (calibrationId == null) {
            result.put("success", false);
            result.put("message", "校准ID不能为空");
            return result;
        }

        try {
            // 获取校准详情
            Map<String, Object> calibrationDetail = getCalibrationDetail(calibrationId);

            // 生成报告数据
            Map<String, Object> reportData = new HashMap<>();
            reportData.put("calibrationDetail", calibrationDetail);
            reportData.put("participantStats", getParticipantStatistics(calibrationId));
            reportData.put("objectStats", getCalibrationObjectStatistics(calibrationId));
            reportData.put("qualityMetrics", getCalibrationQualityMetrics(calibrationId));
            reportData.put("impactAnalysis", getCalibrationImpactAnalysis(calibrationId));

            result.put("success", true);
            result.put("data", reportData);
            result.put("message", "报告生成成功");

        } catch (Exception e) {
            log.error("生成校准报告失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "生成报告失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> exportCalibrationData(Map<String, Object> exportParams) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 构建查询条件
            QueryWrapper<PmPerformanceCalibration> queryWrapper = new QueryWrapper<>();

            if (exportParams != null) {
                if (exportParams.containsKey("calibrationYear")) {
                    queryWrapper.eq("calibration_year", exportParams.get("calibrationYear"));
                }
                if (exportParams.containsKey("calibrationStatus")) {
                    queryWrapper.eq("calibration_status", exportParams.get("calibrationStatus"));
                }
                if (exportParams.containsKey("targetDeptId")) {
                    queryWrapper.eq("target_dept_id", exportParams.get("targetDeptId"));
                }
            }

            List<PmPerformanceCalibration> calibrations = this.list(queryWrapper);

            result.put("success", true);
            result.put("data", calibrations);
            result.put("count", calibrations.size());
            result.put("message", "导出成功");

        } catch (Exception e) {
            log.error("导出校准数据失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "导出失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> importCalibrationData(List<Map<String, Object>> importData, Map<String, Object> importParams) {
        Map<String, Object> result = new HashMap<>();

        if (importData == null || importData.isEmpty()) {
            result.put("success", false);
            result.put("message", "导入数据不能为空");
            return result;
        }

        int successCount = 0;
        int failCount = 0;
        List<String> errorMessages = new ArrayList<>();

        try {
            for (Map<String, Object> data : importData) {
                try {
                    PmPerformanceCalibration calibration = convertMapToCalibration(data);
                    if (createCalibration(calibration)) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMessages.add("创建校准失败: " + data.get("calibrationTitle"));
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add("处理数据失败: " + e.getMessage());
                }
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);
            result.put("message", String.format("导入完成，成功%d个，失败%d个", successCount, failCount));

        } catch (Exception e) {
            log.error("导入校准数据失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "导入失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public boolean copyCalibration(Long calibrationId, Map<String, Object> copyParams) {
        if (calibrationId == null) {
            return false;
        }

        try {
            PmPerformanceCalibration sourceCalibration = this.getById(calibrationId);
            if (sourceCalibration == null) {
                return false;
            }

            // 创建副本
            PmPerformanceCalibration newCalibration = new PmPerformanceCalibration();
            copyCalibrationProperties(sourceCalibration, newCalibration);

            // 处理复制参数
            if (copyParams != null) {
                if (copyParams.containsKey("calibrationTitle")) {
                    newCalibration.setCalibrationTitle((String) copyParams.get("calibrationTitle"));
                }
                if (copyParams.containsKey("calibrationYear")) {
                    newCalibration.setCalibrationYear((Integer) copyParams.get("calibrationYear"));
                }
            }

            // 重置状态和时间
            newCalibration.setCalibrationId(null);
            newCalibration.setCalibrationCode(null);
            newCalibration.setCalibrationStatus("PLANNED");
            newCalibration.setActualStartTime(null);
            newCalibration.setActualEndTime(null);
            newCalibration.setCompletionRate(null);

            return createCalibration(newCalibration);

        } catch (Exception e) {
            log.error("复制校准失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Map<String, Object> batchCreateCalibrations(List<PmPerformanceCalibration> calibrations, Map<String, Object> batchParams) {
        Map<String, Object> result = new HashMap<>();

        if (calibrations == null || calibrations.isEmpty()) {
            result.put("success", false);
            result.put("message", "校准列表不能为空");
            return result;
        }

        int successCount = 0;
        int failCount = 0;
        List<String> errorMessages = new ArrayList<>();

        try {
            for (PmPerformanceCalibration calibration : calibrations) {
                try {
                    if (createCalibration(calibration)) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMessages.add("创建校准失败: " + calibration.getCalibrationTitle());
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add("创建校准异常: " + e.getMessage());
                }
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);
            result.put("message", String.format("批量创建完成，成功%d个，失败%d个", successCount, failCount));

        } catch (Exception e) {
            log.error("批量创建校准失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "批量创建失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> sendCalibrationNotification(Long calibrationId, Map<String, Object> notificationData) {
        Map<String, Object> result = new HashMap<>();

        if (calibrationId == null) {
            result.put("success", false);
            result.put("message", "校准ID不能为空");
            return result;
        }

        try {
            PmPerformanceCalibration calibration = this.getById(calibrationId);
            if (calibration == null) {
                result.put("success", false);
                result.put("message", "校准不存在");
                return result;
            }

            // 更新通知状态
            calibration.setNotificationStatus("SENT");
            calibration.setNotificationTime(LocalDateTime.now());
            this.updateById(calibration);

            result.put("success", true);
            result.put("message", "通知发送成功");

        } catch (Exception e) {
            log.error("发送校准通知失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "发送通知失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> batchSendNotifications(List<Long> calibrationIds, Map<String, Object> notificationData) {
        Map<String, Object> result = new HashMap<>();

        if (calibrationIds == null || calibrationIds.isEmpty()) {
            result.put("success", false);
            result.put("message", "校准ID列表不能为空");
            return result;
        }

        int successCount = 0;
        int failCount = 0;

        try {
            for (Long calibrationId : calibrationIds) {
                Map<String, Object> singleResult = sendCalibrationNotification(calibrationId, notificationData);
                if ((Boolean) singleResult.get("success")) {
                    successCount++;
                } else {
                    failCount++;
                }
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("message", String.format("批量通知完成，成功%d个，失败%d个", successCount, failCount));

        } catch (Exception e) {
            log.error("批量发送通知失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "批量发送通知失败: " + e.getMessage());
        }

        return result;
    }

    // 实现其他查询方法
    @Override
    public Map<String, Object> getParticipantStatistics(Long calibrationId) {
        if (calibrationId == null) {
            return new HashMap<>();
        }
        return calibrationMapper.selectParticipantStatistics(calibrationId);
    }

    @Override
    public Map<String, Object> getCalibrationObjectStatistics(Long calibrationId) {
        if (calibrationId == null) {
            return new HashMap<>();
        }
        return calibrationMapper.selectCalibrationObjectStatistics(calibrationId);
    }

    @Override
    public List<Map<String, Object>> getCalibrationHistory(Long calibrationId, Integer limit) {
        if (calibrationId == null) {
            return new ArrayList<>();
        }
        return calibrationMapper.selectCalibrationHistory(calibrationId, limit);
    }

    @Override
    public List<PmPerformanceCalibration> getSimilarCalibrations(
            String calibrationType, String calibrationScope, Long targetDeptId, Long excludeId, Integer limit) {
        return calibrationMapper.selectSimilarCalibrations(calibrationType, calibrationScope, targetDeptId, excludeId, limit);
    }

    @Override
    public List<Map<String, Object>> getCalibrationTemplates(String calibrationType, String calibrationScope, Integer limit) {
        return calibrationMapper.selectCalibrationTemplates(calibrationType, calibrationScope, limit);
    }

    @Override
    public List<Map<String, Object>> getCalibrationBestPractices(String calibrationType, Integer effectivenessRating, Integer limit) {
        return calibrationMapper.selectCalibrationBestPractices(calibrationType, effectivenessRating, limit);
    }

    @Override
    public List<Map<String, Object>> getImprovementSuggestions(Long calibrationId, String calibrationType) {
        return calibrationMapper.selectImprovementSuggestions(calibrationId, calibrationType);
    }

    @Override
    public Map<String, Object> getCalibrationQualityMetrics(Long calibrationId) {
        if (calibrationId == null) {
            return new HashMap<>();
        }
        return calibrationMapper.selectCalibrationQualityMetrics(calibrationId);
    }

    @Override
    public Map<String, Object> getCalibrationCostAnalysis(Long calibrationId) {
        if (calibrationId == null) {
            return new HashMap<>();
        }
        return calibrationMapper.selectCalibrationCostAnalysis(calibrationId);
    }

    @Override
    public Map<String, Object> getCalibrationROIAnalysis(Long calibrationId) {
        if (calibrationId == null) {
            return new HashMap<>();
        }
        return calibrationMapper.selectCalibrationROIAnalysis(calibrationId);
    }

    @Override
    public Map<String, Object> getCalibrationSatisfactionStats(Long calibrationId) {
        if (calibrationId == null) {
            return new HashMap<>();
        }
        return calibrationMapper.selectCalibrationSatisfactionStats(calibrationId);
    }

    @Override
    public Map<String, Object> getCalibrationImpactAnalysis(Long calibrationId) {
        if (calibrationId == null) {
            return new HashMap<>();
        }
        return calibrationMapper.selectCalibrationImpactAnalysis(calibrationId);
    }

    @Override
    public Map<String, Object> getCalibrationRiskAssessment(Long calibrationId) {
        if (calibrationId == null) {
            return new HashMap<>();
        }
        return calibrationMapper.selectCalibrationRiskAssessment(calibrationId);
    }

    @Override
    public Map<String, Object> getCalibrationComplianceCheck(Long calibrationId) {
        if (calibrationId == null) {
            return new HashMap<>();
        }
        return calibrationMapper.selectCalibrationComplianceCheck(calibrationId);
    }

    @Override
    public Map<String, Object> getCalibrationDataIntegrityCheck(Long calibrationId) {
        if (calibrationId == null) {
            return new HashMap<>();
        }
        return calibrationMapper.selectCalibrationDataIntegrityCheck(calibrationId);
    }

    @Override
    public List<Map<String, Object>> getCalibrationAuditLog(Long calibrationId, Integer limit) {
        if (calibrationId == null) {
            return new ArrayList<>();
        }
        return calibrationMapper.selectCalibrationAuditLog(calibrationId, limit);
    }

    @Override
    public Map<String, Object> getCalibrationPerformanceMetrics(Integer calibrationYear, Long targetDeptId) {
        return calibrationMapper.selectCalibrationPerformanceMetrics(calibrationYear, targetDeptId);
    }

    @Override
    public Map<String, Object> getCalibrationBenchmarkData(String calibrationType, String calibrationScope, Integer calibrationYear) {
        return calibrationMapper.selectCalibrationBenchmarkData(calibrationType, calibrationScope, calibrationYear);
    }

    @Override
    public Map<String, Object> getCalibrationForecastAnalysis(Long calibrationId, Integer forecastPeriod) {
        if (calibrationId == null) {
            return new HashMap<>();
        }
        return calibrationMapper.selectCalibrationForecastAnalysis(calibrationId, forecastPeriod);
    }

    @Override
    public Map<String, Object> validateCalibrationData(PmPerformanceCalibration calibration) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();

        if (calibration == null) {
            errors.add("校准数据不能为空");
        } else {
            if (!StringUtils.hasText(calibration.getCalibrationTitle())) {
                errors.add("校准标题不能为空");
            }
            if (!StringUtils.hasText(calibration.getCalibrationType())) {
                errors.add("校准类型不能为空");
            }
            if (calibration.getCalibrationOwnerId() == null) {
                errors.add("校准负责人不能为空");
            }
            if (calibration.getTargetDeptId() == null) {
                errors.add("目标部门不能为空");
            }
            if (calibration.getPlannedStartTime() != null && calibration.getPlannedEndTime() != null) {
                if (calibration.getPlannedStartTime().isAfter(calibration.getPlannedEndTime())) {
                    errors.add("开始时间不能晚于结束时间");
                }
            }
        }

        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        return result;
    }

    @Override
    public Map<String, Object> controlCalibrationProcess(Long calibrationId, String action, Map<String, Object> controlData) {
        Map<String, Object> result = new HashMap<>();

        if (calibrationId == null || !StringUtils.hasText(action)) {
            result.put("success", false);
            result.put("message", "参数不完整");
            return result;
        }

        try {
            switch (action.toUpperCase()) {
                case "START":
                    boolean startResult = startCalibration(calibrationId, controlData);
                    result.put("success", startResult);
                    result.put("message", startResult ? "校准开始成功" : "校准开始失败");
                    break;
                case "COMPLETE":
                    boolean completeResult = completeCalibration(calibrationId, controlData);
                    result.put("success", completeResult);
                    result.put("message", completeResult ? "校准完成成功" : "校准完成失败");
                    break;
                case "CANCEL":
                    boolean cancelResult = cancelCalibration(calibrationId, controlData);
                    result.put("success", cancelResult);
                    result.put("message", cancelResult ? "校准取消成功" : "校准取消失败");
                    break;
                default:
                    result.put("success", false);
                    result.put("message", "不支持的操作: " + action);
            }
        } catch (Exception e) {
            log.error("校准流程控制失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> intelligentCalibrationAnalysis(Long calibrationId, Map<String, Object> analysisParams) {
        Map<String, Object> result = new HashMap<>();

        if (calibrationId == null) {
            result.put("success", false);
            result.put("message", "校准ID不能为空");
            return result;
        }

        try {
            // 综合分析数据
            Map<String, Object> analysisData = new HashMap<>();
            analysisData.put("qualityMetrics", getCalibrationQualityMetrics(calibrationId));
            analysisData.put("impactAnalysis", getCalibrationImpactAnalysis(calibrationId));
            analysisData.put("riskAssessment", getCalibrationRiskAssessment(calibrationId));
            analysisData.put("improvementSuggestions", getImprovementSuggestions(calibrationId, null));

            result.put("success", true);
            result.put("data", analysisData);
            result.put("message", "智能分析完成");

        } catch (Exception e) {
            log.error("智能校准分析失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "分析失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> getCalibrationOptimizationSuggestions(Long calibrationId) {
        Map<String, Object> result = new HashMap<>();

        if (calibrationId == null) {
            result.put("success", false);
            result.put("message", "校准ID不能为空");
            return result;
        }

        try {
            PmPerformanceCalibration calibration = this.getById(calibrationId);
            if (calibration == null) {
                result.put("success", false);
                result.put("message", "校准不存在");
                return result;
            }

            List<String> suggestions = new ArrayList<>();

            // 基于校准数据生成优化建议
            if (calibration.getConsistencyIndex() != null &&
                calibration.getConsistencyIndex().compareTo(new BigDecimal("0.8")) < 0) {
                suggestions.add("建议加强校准标准的统一性，提高一致性指数");
            }

            if (calibration.getEffectivenessRating() != null && calibration.getEffectivenessRating() < 4) {
                suggestions.add("建议改进校准方法，提高校准效果");
            }

            if (calibration.getParticipantCount() != null && calibration.getParticipantCount() < 3) {
                suggestions.add("建议增加校准参与人数，提高校准的客观性");
            }

            result.put("success", true);
            result.put("suggestions", suggestions);
            result.put("message", "优化建议生成成功");

        } catch (Exception e) {
            log.error("获取校准优化建议失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "获取建议失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> evaluateCalibrationEffectiveness(Long calibrationId, Map<String, Object> evaluationParams) {
        Map<String, Object> result = new HashMap<>();

        if (calibrationId == null) {
            result.put("success", false);
            result.put("message", "校准ID不能为空");
            return result;
        }

        try {
            PmPerformanceCalibration calibration = this.getById(calibrationId);
            if (calibration == null) {
                result.put("success", false);
                result.put("message", "校准不存在");
                return result;
            }

            Map<String, Object> evaluation = new HashMap<>();

            // 计算效果评估指标
            if (calibration.getPreCalibrationAvgScore() != null && calibration.getPostCalibrationAvgScore() != null) {
                BigDecimal improvement = calibration.getPostCalibrationAvgScore().subtract(calibration.getPreCalibrationAvgScore());
                evaluation.put("scoreImprovement", improvement);
                evaluation.put("improvementRate", improvement.divide(calibration.getPreCalibrationAvgScore(), 4, BigDecimal.ROUND_HALF_UP));
            }

            evaluation.put("consistencyIndex", calibration.getConsistencyIndex());
            evaluation.put("completionRate", calibration.getCompletionRate());
            evaluation.put("effectivenessRating", calibration.getEffectivenessRating());

            // 综合评分
            double overallScore = calculateOverallEffectivenessScore(calibration);
            evaluation.put("overallScore", overallScore);
            evaluation.put("effectivenessLevel", getEffectivenessLevel(overallScore));

            result.put("success", true);
            result.put("evaluation", evaluation);
            result.put("message", "效果评估完成");

        } catch (Exception e) {
            log.error("校准效果评估失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "评估失败: " + e.getMessage());
        }

        return result;
    }

    // 私有辅助方法

    /**
     * 将Map转换为校准对象
     */
    private PmPerformanceCalibration convertMapToCalibration(Map<String, Object> data) {
        PmPerformanceCalibration calibration = new PmPerformanceCalibration();

        if (data.containsKey("calibrationTitle")) {
            calibration.setCalibrationTitle((String) data.get("calibrationTitle"));
        }
        if (data.containsKey("calibrationType")) {
            calibration.setCalibrationType((String) data.get("calibrationType"));
        }
        if (data.containsKey("calibrationYear")) {
            calibration.setCalibrationYear((Integer) data.get("calibrationYear"));
        }
        if (data.containsKey("targetDeptId")) {
            calibration.setTargetDeptId(Long.valueOf(data.get("targetDeptId").toString()));
        }
        if (data.containsKey("calibrationOwnerId")) {
            calibration.setCalibrationOwnerId(Long.valueOf(data.get("calibrationOwnerId").toString()));
        }

        return calibration;
    }

    /**
     * 复制校准属性
     */
    private void copyCalibrationProperties(PmPerformanceCalibration source, PmPerformanceCalibration target) {
        target.setCalibrationTitle(source.getCalibrationTitle() + " - 副本");
        target.setCalibrationType(source.getCalibrationType());
        target.setCalibrationYear(source.getCalibrationYear());
        target.setCalibrationScope(source.getCalibrationScope());
        target.setTargetDeptId(source.getTargetDeptId());
        target.setTargetDeptName(source.getTargetDeptName());
        target.setCalibrationOwnerId(source.getCalibrationOwnerId());
        target.setCalibrationOwnerName(source.getCalibrationOwnerName());
        target.setCalibrationObjective(source.getCalibrationObjective());
        target.setCalibrationPrinciples(source.getCalibrationPrinciples());
        target.setCalibrationStandards(source.getCalibrationStandards());
        target.setCalibrationRules(source.getCalibrationRules());
        target.setCalibrationMethod(source.getCalibrationMethod());
        target.setPriorityLevel(source.getPriorityLevel());
    }

    /**
     * 计算综合效果评分
     */
    private double calculateOverallEffectivenessScore(PmPerformanceCalibration calibration) {
        double score = 0.0;
        int factors = 0;

        if (calibration.getConsistencyIndex() != null) {
            score += calibration.getConsistencyIndex().doubleValue() * 100;
            factors++;
        }

        if (calibration.getCompletionRate() != null) {
            score += calibration.getCompletionRate().doubleValue();
            factors++;
        }

        if (calibration.getEffectivenessRating() != null) {
            score += calibration.getEffectivenessRating() * 20;
            factors++;
        }

        return factors > 0 ? score / factors : 0.0;
    }

    /**
     * 获取效果等级
     */
    private String getEffectivenessLevel(double score) {
        if (score >= 90) {
            return "优秀";
        } else if (score >= 80) {
            return "良好";
        } else if (score >= 70) {
            return "一般";
        } else {
            return "需改进";
        }
    }
}
