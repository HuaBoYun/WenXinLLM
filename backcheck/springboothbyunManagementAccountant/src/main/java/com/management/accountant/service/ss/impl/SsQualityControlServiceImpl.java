package com.management.accountant.service.ss.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.ss.SsQualityControl;
import com.management.accountant.mapper.ss.SsQualityControlMapper;
import com.management.accountant.service.ss.SsQualityControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 质量管控服务实现类
 *
 * @author AI Assistant
 * @since 2024-01-15
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SsQualityControlServiceImpl extends ServiceImpl<SsQualityControlMapper, SsQualityControl> implements SsQualityControlService {

    @Autowired
    private SsQualityControlMapper qualityControlMapper;

    @Override
    public IPage<SsQualityControl> getQualityControlPage(Map<String, Object> params) {
        try {
            Integer current = (Integer) params.get("current");
            Integer size = (Integer) params.get("size");
            
            Page<SsQualityControl> page = new Page<>(current != null ? current : 1, size != null ? size : 10);
            return qualityControlMapper.selectQualityControlPage(page, params);
        } catch (Exception e) {
            log.error("分页查询质量管控失败", e);
            throw new RuntimeException("分页查询质量管控失败: " + e.getMessage());
        }
    }

    @Override
    public SsQualityControl getQualityControlById(Long qualityId) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }
            return qualityControlMapper.selectById(qualityId);
        } catch (Exception e) {
            log.error("根据ID查询质量管控失败", e);
            throw new RuntimeException("根据ID查询质量管控失败: " + e.getMessage());
        }
    }

    @Override
    public SsQualityControl getQualityControlByCode(String qualityCode, Long tenantId) {
        try {
            if (!StringUtils.hasText(qualityCode)) {
                throw new IllegalArgumentException("质量管控编码不能为空");
            }
            return qualityControlMapper.selectByQualityCode(qualityCode, tenantId);
        } catch (Exception e) {
            log.error("根据编码查询质量管控失败", e);
            throw new RuntimeException("根据编码查询质量管控失败: " + e.getMessage());
        }
    }

    @Override
    public boolean createQualityControl(SsQualityControl qualityControl) {
        try {
            if (qualityControl == null) {
                throw new IllegalArgumentException("质量管控信息不能为空");
            }
            
            // 验证必填字段
            if (!StringUtils.hasText(qualityControl.getQualityCode())) {
                throw new IllegalArgumentException("质量管控编码不能为空");
            }
            if (!StringUtils.hasText(qualityControl.getQualityName())) {
                throw new IllegalArgumentException("质量管控名称不能为空");
            }
            
            // 检查编码是否重复
            SsQualityControl existingQualityControl = qualityControlMapper.selectByQualityCode(
                qualityControl.getQualityCode(), qualityControl.getTenantId());
            if (existingQualityControl != null) {
                throw new RuntimeException("质量管控编码已存在");
            }
            
            // 设置默认值
            if (qualityControl.getQualityStatus() == null) {
                qualityControl.setQualityStatus("DRAFT");
            }
            if (qualityControl.getDetectionStatus() == null) {
                qualityControl.setDetectionStatus("PENDING");
            }
            if (qualityControl.getPriority() == null) {
                qualityControl.setPriority(5);
            }
            if (qualityControl.getPriorityWeight() == null) {
                qualityControl.setPriorityWeight(BigDecimal.ONE);
            }
            if (qualityControl.getIsEnabled() == null) {
                qualityControl.setIsEnabled(true);
            }
            if (qualityControl.getTenantId() == null) {
                qualityControl.setTenantId(1L);
            }
            
            return qualityControlMapper.insert(qualityControl) > 0;
        } catch (Exception e) {
            log.error("创建质量管控失败", e);
            throw new RuntimeException("创建质量管控失败: " + e.getMessage());
        }
    }

    @Override
    public boolean updateQualityControl(Long qualityId, SsQualityControl qualityControl) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }
            if (qualityControl == null) {
                throw new IllegalArgumentException("质量管控信息不能为空");
            }
            
            // 检查质量管控是否存在
            SsQualityControl existingQualityControl = qualityControlMapper.selectById(qualityId);
            if (existingQualityControl == null) {
                throw new RuntimeException("质量管控不存在");
            }
            
            // 如果修改了编码，检查新编码是否重复
            if (StringUtils.hasText(qualityControl.getQualityCode()) && 
                !qualityControl.getQualityCode().equals(existingQualityControl.getQualityCode())) {
                SsQualityControl duplicateQualityControl = qualityControlMapper.selectByQualityCode(
                    qualityControl.getQualityCode(), existingQualityControl.getTenantId());
                if (duplicateQualityControl != null) {
                    throw new RuntimeException("质量管控编码已存在");
                }
            }
            
            qualityControl.setQualityId(qualityId);
            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("更新质量管控失败", e);
            throw new RuntimeException("更新质量管控失败: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteQualityControl(Long qualityId) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }
            
            // 检查质量管控是否存在
            SsQualityControl qualityControl = qualityControlMapper.selectById(qualityId);
            if (qualityControl == null) {
                throw new RuntimeException("质量管控不存在");
            }
            
            // 检查是否可以删除（如果正在检测中，不允许删除）
            if ("IN_DETECTION".equals(qualityControl.getDetectionStatus())) {
                throw new RuntimeException("正在检测中的质量管控不能删除");
            }
            
            return qualityControlMapper.deleteById(qualityId) > 0;
        } catch (Exception e) {
            log.error("删除质量管控失败", e);
            throw new RuntimeException("删除质量管控失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchDeleteQualityControl(List<Long> qualityIds) {
        try {
            if (qualityIds == null || qualityIds.isEmpty()) {
                throw new IllegalArgumentException("质量管控ID列表不能为空");
            }
            
            // 检查是否有正在检测中的质量管控
            QueryWrapper<SsQualityControl> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("quality_id", qualityIds);
            queryWrapper.eq("detection_status", "IN_DETECTION");
            List<SsQualityControl> inDetectionList = qualityControlMapper.selectList(queryWrapper);
            if (!inDetectionList.isEmpty()) {
                throw new RuntimeException("存在正在检测中的质量管控，不能删除");
            }
            
            return qualityControlMapper.deleteBatchIds(qualityIds) > 0;
        } catch (Exception e) {
            log.error("批量删除质量管控失败", e);
            throw new RuntimeException("批量删除质量管控失败: " + e.getMessage());
        }
    }

    @Override
    public boolean enableQualityControl(Long qualityId) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }
            
            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setIsEnabled(true);
            qualityControl.setQualityStatus("ACTIVE");
            
            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("启用质量管控失败", e);
            throw new RuntimeException("启用质量管控失败: " + e.getMessage());
        }
    }

    @Override
    public boolean disableQualityControl(Long qualityId) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }
            
            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setIsEnabled(false);
            qualityControl.setQualityStatus("INACTIVE");
            
            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("禁用质量管控失败", e);
            throw new RuntimeException("禁用质量管控失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchEnableQualityControl(List<Long> qualityIds) {
        try {
            if (qualityIds == null || qualityIds.isEmpty()) {
                throw new IllegalArgumentException("质量管控ID列表不能为空");
            }
            
            return qualityControlMapper.batchUpdateEnabled(qualityIds, true, 1L) > 0;
        } catch (Exception e) {
            log.error("批量启用质量管控失败", e);
            throw new RuntimeException("批量启用质量管控失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchDisableQualityControl(List<Long> qualityIds) {
        try {
            if (qualityIds == null || qualityIds.isEmpty()) {
                throw new IllegalArgumentException("质量管控ID列表不能为空");
            }
            
            return qualityControlMapper.batchUpdateEnabled(qualityIds, false, 1L) > 0;
        } catch (Exception e) {
            log.error("批量禁用质量管控失败", e);
            throw new RuntimeException("批量禁用质量管控失败: " + e.getMessage());
        }
    }

    @Override
    public boolean startDetection(Long qualityId) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }
            
            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setDetectionStatus("IN_DETECTION");
            qualityControl.setDetectionStartTime(LocalDateTime.now());
            
            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("开始检测失败", e);
            throw new RuntimeException("开始检测失败: " + e.getMessage());
        }
    }

    @Override
    public boolean stopDetection(Long qualityId) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }
            
            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setDetectionStatus("STOPPED");
            qualityControl.setDetectionEndTime(LocalDateTime.now());
            
            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("停止检测失败", e);
            throw new RuntimeException("停止检测失败: " + e.getMessage());
        }
    }

    @Override
    public boolean pauseDetection(Long qualityId) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }
            
            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setDetectionStatus("PAUSED");
            
            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("暂停检测失败", e);
            throw new RuntimeException("暂停检测失败: " + e.getMessage());
        }
    }

    @Override
    public boolean resumeDetection(Long qualityId) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }
            
            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setDetectionStatus("IN_DETECTION");
            
            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("恢复检测失败", e);
            throw new RuntimeException("恢复检测失败: " + e.getMessage());
        }
    }

    @Override
    public boolean completeDetection(Long qualityId, String detectionResult, BigDecimal qualityScore) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }
            
            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setDetectionStatus("COMPLETED");
            qualityControl.setDetectionResult(detectionResult);
            qualityControl.setQualityScore(qualityScore);
            qualityControl.setDetectionEndTime(LocalDateTime.now());
            qualityControl.setLastDetectionTime(LocalDateTime.now());
            
            // 根据质量评分设置质量等级
            if (qualityScore != null) {
                if (qualityScore.compareTo(new BigDecimal("90")) >= 0) {
                    qualityControl.setQualityLevel("EXCELLENT");
                } else if (qualityScore.compareTo(new BigDecimal("80")) >= 0) {
                    qualityControl.setQualityLevel("GOOD");
                } else if (qualityScore.compareTo(new BigDecimal("70")) >= 0) {
                    qualityControl.setQualityLevel("AVERAGE");
                } else if (qualityScore.compareTo(new BigDecimal("60")) >= 0) {
                    qualityControl.setQualityLevel("POOR");
                } else {
                    qualityControl.setQualityLevel("VERY_POOR");
                }
            }
            
            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("完成检测失败", e);
            throw new RuntimeException("完成检测失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchStartDetection(List<Long> qualityIds) {
        try {
            if (qualityIds == null || qualityIds.isEmpty()) {
                throw new IllegalArgumentException("质量管控ID列表不能为空");
            }

            return qualityControlMapper.batchUpdateDetectionStatus(qualityIds, "IN_DETECTION", 1L) > 0;
        } catch (Exception e) {
            log.error("批量开始检测失败", e);
            throw new RuntimeException("批量开始检测失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchStopDetection(List<Long> qualityIds) {
        try {
            if (qualityIds == null || qualityIds.isEmpty()) {
                throw new IllegalArgumentException("质量管控ID列表不能为空");
            }

            return qualityControlMapper.batchUpdateDetectionStatus(qualityIds, "STOPPED", 1L) > 0;
        } catch (Exception e) {
            log.error("批量停止检测失败", e);
            throw new RuntimeException("批量停止检测失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchPauseDetection(List<Long> qualityIds) {
        try {
            if (qualityIds == null || qualityIds.isEmpty()) {
                throw new IllegalArgumentException("质量管控ID列表不能为空");
            }

            return qualityControlMapper.batchUpdateDetectionStatus(qualityIds, "PAUSED", 1L) > 0;
        } catch (Exception e) {
            log.error("批量暂停检测失败", e);
            throw new RuntimeException("批量暂停检测失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchResumeDetection(List<Long> qualityIds) {
        try {
            if (qualityIds == null || qualityIds.isEmpty()) {
                throw new IllegalArgumentException("质量管控ID列表不能为空");
            }

            return qualityControlMapper.batchUpdateDetectionStatus(qualityIds, "IN_DETECTION", 1L) > 0;
        } catch (Exception e) {
            log.error("批量恢复检测失败", e);
            throw new RuntimeException("批量恢复检测失败: " + e.getMessage());
        }
    }

    @Override
    public boolean executeDetectionImmediately(Long qualityId) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }

            // 立即开始检测
            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setDetectionStatus("IN_DETECTION");
            qualityControl.setDetectionStartTime(LocalDateTime.now());

            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("立即执行检测失败", e);
            throw new RuntimeException("立即执行检测失败: " + e.getMessage());
        }
    }

    @Override
    public boolean reDetection(Long qualityId) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }

            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setDetectionStatus("IN_DETECTION");
            qualityControl.setDetectionStartTime(LocalDateTime.now());
            qualityControl.setDetectionResult(null);
            qualityControl.setQualityScore(null);

            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("重新检测失败", e);
            throw new RuntimeException("重新检测失败: " + e.getMessage());
        }
    }

    @Override
    public boolean configureDetectionRule(Long qualityId, String ruleType, Map<String, Object> ruleConfig) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }

            // 这里可以根据实际需求实现检测规则配置逻辑
            log.info("配置检测规则: qualityId={}, ruleType={}, ruleConfig={}", qualityId, ruleType, ruleConfig);

            return true;
        } catch (Exception e) {
            log.error("配置检测规则失败", e);
            throw new RuntimeException("配置检测规则失败: " + e.getMessage());
        }
    }

    @Override
    public boolean configureSamplingMechanism(Long qualityId, BigDecimal samplingRatio, Integer samplingQuantity) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }

            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setSamplingRatio(samplingRatio);
            qualityControl.setSamplingQuantity(samplingQuantity);

            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("配置抽检机制失败", e);
            throw new RuntimeException("配置抽检机制失败: " + e.getMessage());
        }
    }

    @Override
    public boolean assignResponsiblePerson(Long qualityId, Long responsiblePersonId, String responsiblePersonName) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }

            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setResponsiblePersonId(responsiblePersonId);
            qualityControl.setResponsiblePersonName(responsiblePersonName);

            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("分配负责人失败", e);
            throw new RuntimeException("分配负责人失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchAssignResponsiblePerson(List<Long> qualityIds, Long responsiblePersonId, String responsiblePersonName) {
        try {
            if (qualityIds == null || qualityIds.isEmpty()) {
                throw new IllegalArgumentException("质量管控ID列表不能为空");
            }

            return qualityControlMapper.batchAssignResponsiblePerson(qualityIds, responsiblePersonId, responsiblePersonName, 1L) > 0;
        } catch (Exception e) {
            log.error("批量分配负责人失败", e);
            throw new RuntimeException("批量分配负责人失败: " + e.getMessage());
        }
    }

    @Override
    public boolean assignInspector(Long qualityId, Long inspectorId, String inspectorName) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }

            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setInspectorId(inspectorId);
            qualityControl.setInspectorName(inspectorName);

            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("分配检测人员失败", e);
            throw new RuntimeException("分配检测人员失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchAssignInspector(List<Long> qualityIds, Long inspectorId, String inspectorName) {
        try {
            if (qualityIds == null || qualityIds.isEmpty()) {
                throw new IllegalArgumentException("质量管控ID列表不能为空");
            }

            return qualityControlMapper.batchAssignInspector(qualityIds, inspectorId, inspectorName, 1L) > 0;
        } catch (Exception e) {
            log.error("批量分配检测人员失败", e);
            throw new RuntimeException("批量分配检测人员失败: " + e.getMessage());
        }
    }

    @Override
    public boolean setPriority(Long qualityId, Integer priority, BigDecimal priorityWeight) {
        try {
            if (qualityId == null) {
                throw new IllegalArgumentException("质量管控ID不能为空");
            }

            SsQualityControl qualityControl = new SsQualityControl();
            qualityControl.setQualityId(qualityId);
            qualityControl.setPriority(priority);
            qualityControl.setPriorityWeight(priorityWeight);

            return qualityControlMapper.updateById(qualityControl) > 0;
        } catch (Exception e) {
            log.error("设置优先级失败", e);
            throw new RuntimeException("设置优先级失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchSetPriority(List<Long> qualityIds, Integer priority, BigDecimal priorityWeight) {
        try {
            if (qualityIds == null || qualityIds.isEmpty()) {
                throw new IllegalArgumentException("质量管控ID列表不能为空");
            }

            return qualityControlMapper.batchSetPriority(qualityIds, priority, priorityWeight, 1L) > 0;
        } catch (Exception e) {
            log.error("批量设置优先级失败", e);
            throw new RuntimeException("批量设置优先级失败: " + e.getMessage());
        }
    }

    // 查询方法实现
    @Override
    public List<SsQualityControl> getPendingDetection(Long tenantId) {
        return qualityControlMapper.selectPendingDetection(tenantId);
    }

    @Override
    public List<SsQualityControl> getInDetection(Long tenantId) {
        return qualityControlMapper.selectInDetection(tenantId);
    }

    @Override
    public List<SsQualityControl> getCompletedDetection(Long tenantId) {
        return qualityControlMapper.selectCompletedDetection(tenantId);
    }

    @Override
    public List<SsQualityControl> getFailedDetection(Long tenantId) {
        return qualityControlMapper.selectFailedDetection(tenantId);
    }

    @Override
    public List<SsQualityControl> getNeedImprovement(Long tenantId) {
        return qualityControlMapper.selectNeedImprovement(tenantId);
    }

    @Override
    public List<SsQualityControl> getHighRisk(Long tenantId) {
        return qualityControlMapper.selectHighRisk(tenantId);
    }

    @Override
    public List<SsQualityControl> getOverdueDetection(Long tenantId) {
        return qualityControlMapper.selectOverdueDetection(tenantId);
    }

    // 统计方法实现
    @Override
    public Map<String, Object> getQualityControlStatistics(Long tenantId) {
        return qualityControlMapper.selectQualityControlStatistics(tenantId);
    }

    @Override
    public List<Map<String, Object>> getQualityStatusDistribution(Long tenantId) {
        return qualityControlMapper.selectQualityStatusDistribution(tenantId);
    }

    @Override
    public List<Map<String, Object>> getQualityTypeDistribution(Long tenantId) {
        return qualityControlMapper.selectQualityTypeDistribution(tenantId);
    }

    @Override
    public List<Map<String, Object>> getQualityLevelDistribution(Long tenantId) {
        return qualityControlMapper.selectQualityLevelDistribution(tenantId);
    }

    @Override
    public List<Map<String, Object>> getDetectionStatusDistribution(Long tenantId) {
        return qualityControlMapper.selectDetectionStatusDistribution(tenantId);
    }

    @Override
    public List<Map<String, Object>> getRiskLevelDistribution(Long tenantId) {
        return qualityControlMapper.selectRiskLevelDistribution(tenantId);
    }

    @Override
    public List<Map<String, Object>> getQualityControlTrend(Map<String, Object> params) {
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        Long tenantId = (Long) params.get("tenantId");
        return qualityControlMapper.selectQualityControlTrend(startDate, endDate, tenantId);
    }

    @Override
    public List<Map<String, Object>> getDetectionEfficiency(Long tenantId) {
        return qualityControlMapper.selectDetectionEfficiency(tenantId);
    }

    @Override
    public List<Map<String, Object>> getQualityCostAnalysis(Long tenantId) {
        return qualityControlMapper.selectQualityCostAnalysis(tenantId);
    }

    @Override
    public List<Map<String, Object>> getPassRateTrend(Map<String, Object> params) {
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        Long tenantId = (Long) params.get("tenantId");
        return qualityControlMapper.selectPassRateTrend(startDate, endDate, tenantId);
    }

    @Override
    public List<Map<String, Object>> getDefectTypeDistribution(Long tenantId) {
        return qualityControlMapper.selectDefectTypeDistribution(tenantId);
    }

    @Override
    public List<Map<String, Object>> getImprovementEffectAnalysis(Long tenantId) {
        return qualityControlMapper.selectImprovementEffectAnalysis(tenantId);
    }

    @Override
    public List<Map<String, Object>> getResponsiblePersonWorkload(Long tenantId) {
        return qualityControlMapper.selectResponsiblePersonWorkload(tenantId);
    }

    @Override
    public List<Map<String, Object>> getInspectorWorkload(Long tenantId) {
        return qualityControlMapper.selectInspectorWorkload(tenantId);
    }

    @Override
    public List<Map<String, Object>> getTrainingEffectAnalysis(Long tenantId) {
        return qualityControlMapper.selectTrainingEffectAnalysis(tenantId);
    }

    @Override
    public List<Map<String, Object>> getQualityControlRanking(String rankType, Long tenantId) {
        return qualityControlMapper.selectQualityControlRanking(rankType, tenantId);
    }

    @Override
    public List<Map<String, Object>> getQualityControlAlerts(Long tenantId) {
        return qualityControlMapper.selectQualityControlAlerts(tenantId);
    }

    @Override
    public Map<String, Object> getQualityControlKPI(Long tenantId) {
        return qualityControlMapper.selectQualityControlKPI(tenantId);
    }

    @Override
    public Map<String, Object> getQualityControlPerformance(Long tenantId) {
        return qualityControlMapper.selectQualityControlPerformance(tenantId);
    }

    @Override
    public Map<String, Object> getQualityControlHealthAssessment(Long tenantId) {
        return qualityControlMapper.selectQualityControlHealthAssessment(tenantId);
    }

    // 其他方法的简化实现
    @Override
    public boolean setQualityStandard(Long qualityId, Long standardId, String standardName, String standardVersion) {
        SsQualityControl qualityControl = new SsQualityControl();
        qualityControl.setQualityId(qualityId);
        qualityControl.setStandardId(standardId);
        qualityControl.setStandardName(standardName);
        qualityControl.setStandardVersion(standardVersion);
        return qualityControlMapper.updateById(qualityControl) > 0;
    }

    @Override
    public boolean updateQualityScore(Long qualityId, BigDecimal qualityScore, String qualityLevel) {
        SsQualityControl qualityControl = new SsQualityControl();
        qualityControl.setQualityId(qualityId);
        qualityControl.setQualityScore(qualityScore);
        qualityControl.setQualityLevel(qualityLevel);
        return qualityControlMapper.updateById(qualityControl) > 0;
    }

    @Override
    public boolean updatePassRate(Long qualityId, BigDecimal passRate, Integer failQuantity) {
        SsQualityControl qualityControl = new SsQualityControl();
        qualityControl.setQualityId(qualityId);
        qualityControl.setPassRate(passRate);
        qualityControl.setFailQuantity(failQuantity);
        return qualityControlMapper.updateById(qualityControl) > 0;
    }

    @Override
    public boolean recordDefectInfo(Long qualityId, String defectType, String defectLevel, String defectDescription) {
        SsQualityControl qualityControl = new SsQualityControl();
        qualityControl.setQualityId(qualityId);
        qualityControl.setDefectType(defectType);
        qualityControl.setDefectLevel(defectLevel);
        qualityControl.setDefectDescription(defectDescription);
        return qualityControlMapper.updateById(qualityControl) > 0;
    }

    @Override
    public boolean makeImprovementMeasures(Long qualityId, String improvementMeasures) {
        SsQualityControl qualityControl = new SsQualityControl();
        qualityControl.setQualityId(qualityId);
        qualityControl.setImprovementMeasures(improvementMeasures);
        qualityControl.setImprovementStatus("PLANNED");
        return qualityControlMapper.updateById(qualityControl) > 0;
    }

    @Override
    public boolean updateImprovementStatus(Long qualityId, String improvementStatus, String improvementEffect) {
        SsQualityControl qualityControl = new SsQualityControl();
        qualityControl.setQualityId(qualityId);
        qualityControl.setImprovementStatus(improvementStatus);
        qualityControl.setImprovementEffect(improvementEffect);
        return qualityControlMapper.updateById(qualityControl) > 0;
    }

    @Override
    public boolean calculateQualityCost(Long qualityId, BigDecimal preventionCost, BigDecimal appraisalCost,
                                       BigDecimal internalFailureCost, BigDecimal externalFailureCost) {
        BigDecimal totalCost = preventionCost.add(appraisalCost).add(internalFailureCost).add(externalFailureCost);

        SsQualityControl qualityControl = new SsQualityControl();
        qualityControl.setQualityId(qualityId);
        qualityControl.setPreventionCost(preventionCost);
        qualityControl.setAppraisalCost(appraisalCost);
        qualityControl.setInternalFailureCost(internalFailureCost);
        qualityControl.setExternalFailureCost(externalFailureCost);
        qualityControl.setQualityCost(totalCost);

        return qualityControlMapper.updateById(qualityControl) > 0;
    }

    @Override
    public boolean arrangeQualityTraining(Long qualityId, Long trainingId, String trainingName) {
        SsQualityControl qualityControl = new SsQualityControl();
        qualityControl.setQualityId(qualityId);
        qualityControl.setTrainingId(trainingId);
        qualityControl.setTrainingName(trainingName);
        qualityControl.setTrainingStatus("ARRANGED");
        return qualityControlMapper.updateById(qualityControl) > 0;
    }

    @Override
    public boolean updateTrainingStatus(Long qualityId, String trainingStatus, BigDecimal trainingCompletionRate) {
        SsQualityControl qualityControl = new SsQualityControl();
        qualityControl.setQualityId(qualityId);
        qualityControl.setTrainingStatus(trainingStatus);
        qualityControl.setTrainingCompletionRate(trainingCompletionRate);
        return qualityControlMapper.updateById(qualityControl) > 0;
    }

    @Override
    public String generateQualityReport(Long qualityId) {
        SsQualityControl qualityControl = qualityControlMapper.selectById(qualityId);
        if (qualityControl == null) {
            throw new RuntimeException("质量管控不存在");
        }

        StringBuilder report = new StringBuilder();
        report.append("质量管控报告\n");
        report.append("编码: ").append(qualityControl.getQualityCode()).append("\n");
        report.append("名称: ").append(qualityControl.getQualityName()).append("\n");
        report.append("质量评分: ").append(qualityControl.getQualityScore()).append("\n");
        report.append("合格率: ").append(qualityControl.getPassRate()).append("%\n");

        return report.toString();
    }

    @Override
    public List<String> batchGenerateQualityReport(List<Long> qualityIds) {
        return qualityIds.stream()
                .map(this::generateQualityReport)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean importQualityControlData(List<SsQualityControl> qualityControlList) {
        return this.saveBatch(qualityControlList);
    }

    @Override
    public List<SsQualityControl> exportQualityControlData(Map<String, Object> queryParams) {
        QueryWrapper<SsQualityControl> queryWrapper = new QueryWrapper<>();
        if (queryParams.get("qualityType") != null) {
            queryWrapper.eq("quality_type", queryParams.get("qualityType"));
        }
        if (queryParams.get("qualityStatus") != null) {
            queryWrapper.eq("quality_status", queryParams.get("qualityStatus"));
        }
        return qualityControlMapper.selectList(queryWrapper);
    }

    @Override
    public boolean sendNotification(Long qualityId, String notificationType, String message) {
        log.info("发送通知: qualityId={}, type={}, message={}", qualityId, notificationType, message);
        return true;
    }

    @Override
    public boolean batchSendNotification(List<Long> qualityIds, String notificationType, String message) {
        return qualityIds.stream()
                .allMatch(qualityId -> sendNotification(qualityId, notificationType, message));
    }
}
