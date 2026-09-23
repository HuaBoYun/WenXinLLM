package com.management.accountant.service.eps.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.eps.EpsBudgetSubject;
import com.management.accountant.mapper.eps.EpsBudgetSubjectMapper;
import com.management.accountant.service.eps.EpsBudgetSubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 预算科目服务实现
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Service
public class EpsBudgetSubjectServiceImpl implements EpsBudgetSubjectService {

    @Autowired
    private EpsBudgetSubjectMapper budgetSubjectMapper;

    @Override
    public IPage<EpsBudgetSubject> queryBudgetSubjectPage(Long current, Long size, String subjectName, 
                                                         String subjectCode, String subjectType, 
                                                         Long systemId, Long parentSubjectId) {
        Page<EpsBudgetSubject> page = new Page<>(current, size);
        QueryWrapper<EpsBudgetSubject> queryWrapper = new QueryWrapper<>();
        
        if (subjectName != null && !subjectName.isEmpty()) {
            queryWrapper.like("subject_name", subjectName);
        }
        if (subjectCode != null && !subjectCode.isEmpty()) {
            queryWrapper.like("subject_code", subjectCode);
        }
        if (subjectType != null && !subjectType.isEmpty()) {
            queryWrapper.eq("subject_type", subjectType);
        }
        if (systemId != null) {
            queryWrapper.eq("system_id", systemId);
        }
        if (parentSubjectId != null) {
            queryWrapper.eq("parent_subject_id", parentSubjectId);
        }
        
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByAsc("subject_level", "sort_order", "subject_code");
        
        return budgetSubjectMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createBudgetSubject(EpsBudgetSubject budgetSubject) {
        try {
            // 验证科目编码唯一性
            if (checkSubjectCodeExists(budgetSubject.getSubjectCode(), null)) {
                throw new RuntimeException("科目编码已存在");
            }
            
            // 验证层级关系
            if (budgetSubject.getParentSubjectId() != null) {
                if (!validateSubjectHierarchy(null, budgetSubject.getParentSubjectId())) {
                    throw new RuntimeException("科目层级关系无效");
                }
                
                // 设置科目层级
                EpsBudgetSubject parentSubject = getBudgetSubjectById(budgetSubject.getParentSubjectId());
                if (parentSubject != null) {
                    budgetSubject.setSubjectLevel(parentSubject.getSubjectLevel() + 1);
                    budgetSubject.setSubjectPath(parentSubject.getSubjectPath() + "/" + budgetSubject.getSubjectCode());
                }
            } else {
                budgetSubject.setSubjectLevel(1);
                budgetSubject.setSubjectPath("/" + budgetSubject.getSubjectCode());
            }
            
            // 设置默认值
            budgetSubject.setCreatedTime(LocalDateTime.now());
            budgetSubject.setUpdatedTime(LocalDateTime.now());
            budgetSubject.setDeleted(0);
            
            if (budgetSubject.getIsEnabled() == null) {
                budgetSubject.setIsEnabled(true);
            }
            
            if (budgetSubject.getIsLeaf() == null) {
                budgetSubject.setIsLeaf(true);
            }
            
            if (budgetSubject.getSortOrder() == null) {
                budgetSubject.setSortOrder(getNextSortOrder(budgetSubject.getParentSubjectId()));
            }
            
            return budgetSubjectMapper.insert(budgetSubject) > 0;
        } catch (Exception e) {
            log.error("创建预算科目失败", e);
            throw new RuntimeException("创建失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBudgetSubject(EpsBudgetSubject budgetSubject) {
        try {
            // 验证科目编码唯一性
            if (checkSubjectCodeExists(budgetSubject.getSubjectCode(), budgetSubject.getSubjectId())) {
                throw new RuntimeException("科目编码已存在");
            }
            
            // 验证层级关系
            if (budgetSubject.getParentSubjectId() != null) {
                if (!validateSubjectHierarchy(budgetSubject.getSubjectId(), budgetSubject.getParentSubjectId())) {
                    throw new RuntimeException("科目层级关系无效");
                }
            }
            
            budgetSubject.setUpdatedTime(LocalDateTime.now());
            return budgetSubjectMapper.updateById(budgetSubject) > 0;
        } catch (Exception e) {
            log.error("更新预算科目失败", e);
            throw new RuntimeException("更新失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBudgetSubject(Long subjectId) {
        try {
            // 检查是否有子科目
            List<EpsBudgetSubject> children = getChildBudgetSubjects(subjectId);
            if (!children.isEmpty()) {
                throw new RuntimeException("存在子科目，不能删除");
            }
            
            // 检查是否被使用
            Map<String, Object> usage = getSubjectUsage(subjectId);
            if (usage != null && (Boolean) usage.getOrDefault("isUsed", false)) {
                throw new RuntimeException("科目正在使用中，不能删除");
            }
            
            EpsBudgetSubject budgetSubject = new EpsBudgetSubject();
            budgetSubject.setSubjectId(subjectId);
            budgetSubject.setDeleted(1);
            budgetSubject.setUpdatedTime(LocalDateTime.now());
            return budgetSubjectMapper.updateById(budgetSubject) > 0;
        } catch (Exception e) {
            log.error("删除预算科目失败", e);
            throw new RuntimeException("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteBudgetSubjects(List<Long> subjectIds) {
        try {
            int successCount = 0;
            for (Long subjectId : subjectIds) {
                if (deleteBudgetSubject(subjectId)) {
                    successCount++;
                }
            }
            return successCount == subjectIds.size();
        } catch (Exception e) {
            log.error("批量删除预算科目失败", e);
            throw new RuntimeException("批量删除失败: " + e.getMessage());
        }
    }

    @Override
    public EpsBudgetSubject getBudgetSubjectById(Long subjectId) {
        return budgetSubjectMapper.selectById(subjectId);
    }

    @Override
    public EpsBudgetSubject getBudgetSubjectByCode(String subjectCode) {
        QueryWrapper<EpsBudgetSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_code", subjectCode);
        queryWrapper.eq("deleted", 0);
        return budgetSubjectMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> getBudgetSubjectTree(Long systemId, String subjectType, Boolean includeDisabled) {
        QueryWrapper<EpsBudgetSubject> queryWrapper = new QueryWrapper<>();
        
        if (systemId != null) {
            queryWrapper.eq("system_id", systemId);
        }
        if (subjectType != null && !subjectType.isEmpty()) {
            queryWrapper.eq("subject_type", subjectType);
        }
        if (!Boolean.TRUE.equals(includeDisabled)) {
            queryWrapper.eq("is_enabled", true);
        }
        
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByAsc("subject_level", "sort_order", "subject_code");
        
        List<EpsBudgetSubject> subjects = budgetSubjectMapper.selectList(queryWrapper);
        return buildSubjectTree(subjects, null);
    }

    @Override
    public List<EpsBudgetSubject> getBudgetSubjectsBySystemId(Long systemId) {
        QueryWrapper<EpsBudgetSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("system_id", systemId);
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByAsc("subject_level", "sort_order", "subject_code");
        return budgetSubjectMapper.selectList(queryWrapper);
    }

    @Override
    public List<EpsBudgetSubject> getBudgetSubjectsByType(String subjectType, Long systemId) {
        QueryWrapper<EpsBudgetSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_type", subjectType);
        if (systemId != null) {
            queryWrapper.eq("system_id", systemId);
        }
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByAsc("subject_level", "sort_order", "subject_code");
        return budgetSubjectMapper.selectList(queryWrapper);
    }

    @Override
    public List<EpsBudgetSubject> getChildBudgetSubjects(Long parentSubjectId) {
        QueryWrapper<EpsBudgetSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_subject_id", parentSubjectId);
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByAsc("sort_order", "subject_code");
        return budgetSubjectMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveBudgetSubject(Long subjectId, Long targetParentId, Integer targetPosition) {
        try {
            // 验证层级关系
            if (!validateSubjectHierarchy(subjectId, targetParentId)) {
                throw new RuntimeException("移动后的层级关系无效");
            }
            
            EpsBudgetSubject subject = getBudgetSubjectById(subjectId);
            if (subject == null) {
                throw new RuntimeException("科目不存在");
            }
            
            // 更新父科目和位置
            subject.setParentSubjectId(targetParentId);
            if (targetPosition != null) {
                subject.setSortOrder(targetPosition);
            }
            
            // 重新计算层级和路径
            if (targetParentId != null) {
                EpsBudgetSubject parentSubject = getBudgetSubjectById(targetParentId);
                if (parentSubject != null) {
                    subject.setSubjectLevel(parentSubject.getSubjectLevel() + 1);
                    subject.setSubjectPath(parentSubject.getSubjectPath() + "/" + subject.getSubjectCode());
                }
            } else {
                subject.setSubjectLevel(1);
                subject.setSubjectPath("/" + subject.getSubjectCode());
            }
            
            subject.setUpdatedTime(LocalDateTime.now());
            return budgetSubjectMapper.updateById(subject) > 0;
        } catch (Exception e) {
            log.error("移动预算科目失败", e);
            throw new RuntimeException("移动失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyBudgetSubject(Long sourceSubjectId, String targetSubjectCode, String targetSubjectName, 
                                    Long targetParentId, Boolean copyChildren) {
        try {
            EpsBudgetSubject sourceSubject = getBudgetSubjectById(sourceSubjectId);
            if (sourceSubject == null) {
                throw new RuntimeException("源科目不存在");
            }
            
            // 检查目标编码是否存在
            if (checkSubjectCodeExists(targetSubjectCode, null)) {
                throw new RuntimeException("目标科目编码已存在");
            }
            
            // 创建新科目
            EpsBudgetSubject targetSubject = new EpsBudgetSubject();
            // 复制基本属性
            targetSubject.setSystemId(sourceSubject.getSystemId());
            targetSubject.setSubjectCode(targetSubjectCode);
            targetSubject.setSubjectName(targetSubjectName);
            targetSubject.setSubjectType(sourceSubject.getSubjectType());
            targetSubject.setSubjectCategory(sourceSubject.getSubjectCategory());
            targetSubject.setParentSubjectId(targetParentId);
            targetSubject.setDataType(sourceSubject.getDataType());
            targetSubject.setCalculationMethod(sourceSubject.getCalculationMethod());
            targetSubject.setUnit(sourceSubject.getUnit());
            targetSubject.setPrecisionScale(sourceSubject.getPrecisionScale());
            targetSubject.setIsEnabled(sourceSubject.getIsEnabled());
            targetSubject.setIsLeaf(sourceSubject.getIsLeaf());
            targetSubject.setDescription(sourceSubject.getDescription());
            targetSubject.setSubjectConfig(sourceSubject.getSubjectConfig());
            targetSubject.setCreatedTime(LocalDateTime.now());
            targetSubject.setUpdatedTime(LocalDateTime.now());
            targetSubject.setDeleted(0);
            
            // 设置层级和路径
            if (targetParentId != null) {
                EpsBudgetSubject parentSubject = getBudgetSubjectById(targetParentId);
                if (parentSubject != null) {
                    targetSubject.setSubjectLevel(parentSubject.getSubjectLevel() + 1);
                    targetSubject.setSubjectPath(parentSubject.getSubjectPath() + "/" + targetSubjectCode);
                }
            } else {
                targetSubject.setSubjectLevel(1);
                targetSubject.setSubjectPath("/" + targetSubjectCode);
            }
            
            targetSubject.setSortOrder(getNextSortOrder(targetParentId));
            
            boolean result = budgetSubjectMapper.insert(targetSubject) > 0;
            
            // 如果需要复制子科目
            if (Boolean.TRUE.equals(copyChildren)) {
                List<EpsBudgetSubject> children = getChildBudgetSubjects(sourceSubjectId);
                for (EpsBudgetSubject child : children) {
                    String childTargetCode = targetSubjectCode + "_" + child.getSubjectCode().substring(child.getSubjectCode().lastIndexOf("_") + 1);
                    copyBudgetSubject(child.getSubjectId(), childTargetCode, child.getSubjectName(), 
                                    targetSubject.getSubjectId(), true);
                }
            }
            
            return result;
        } catch (Exception e) {
            log.error("复制预算科目失败", e);
            throw new RuntimeException("复制失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableBudgetSubject(Long subjectId) {
        try {
            EpsBudgetSubject subject = new EpsBudgetSubject();
            subject.setSubjectId(subjectId);
            subject.setIsEnabled(true);
            subject.setUpdatedTime(LocalDateTime.now());
            return budgetSubjectMapper.updateById(subject) > 0;
        } catch (Exception e) {
            log.error("启用预算科目失败", e);
            throw new RuntimeException("启用失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableBudgetSubject(Long subjectId) {
        try {
            EpsBudgetSubject subject = new EpsBudgetSubject();
            subject.setSubjectId(subjectId);
            subject.setIsEnabled(false);
            subject.setUpdatedTime(LocalDateTime.now());
            return budgetSubjectMapper.updateById(subject) > 0;
        } catch (Exception e) {
            log.error("禁用预算科目失败", e);
            throw new RuntimeException("禁用失败: " + e.getMessage());
        }
    }

    @Override
    public List<EpsBudgetSubject> getBudgetSubjectPath(Long subjectId) {
        List<EpsBudgetSubject> path = new ArrayList<>();
        EpsBudgetSubject current = getBudgetSubjectById(subjectId);
        
        while (current != null) {
            path.add(0, current);
            if (current.getParentSubjectId() != null) {
                current = getBudgetSubjectById(current.getParentSubjectId());
            } else {
                break;
            }
        }
        
        return path;
    }

    @Override
    public boolean checkSubjectCodeExists(String subjectCode, Long excludeId) {
        QueryWrapper<EpsBudgetSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_code", subjectCode);
        queryWrapper.eq("deleted", 0);
        if (excludeId != null) {
            queryWrapper.ne("subject_id", excludeId);
        }
        return budgetSubjectMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public Map<String, Object> getBudgetSubjectStatistics(Long systemId, String subjectType) {
        Map<String, Object> statistics = new HashMap<>();
        
        QueryWrapper<EpsBudgetSubject> queryWrapper = new QueryWrapper<>();
        if (systemId != null) {
            queryWrapper.eq("system_id", systemId);
        }
        if (subjectType != null && !subjectType.isEmpty()) {
            queryWrapper.eq("subject_type", subjectType);
        }
        queryWrapper.eq("deleted", 0);
        
        // 总科目数
        Long totalSubjects = budgetSubjectMapper.selectCount(queryWrapper);
        statistics.put("totalSubjects", totalSubjects);
        
        // 启用科目数
        QueryWrapper<EpsBudgetSubject> enabledWrapper = new QueryWrapper<>(queryWrapper);
        enabledWrapper.eq("is_enabled", true);
        Long enabledSubjects = budgetSubjectMapper.selectCount(enabledWrapper);
        statistics.put("enabledSubjects", enabledSubjects);
        
        // 禁用科目数
        statistics.put("disabledSubjects", totalSubjects - enabledSubjects);
        
        // 叶子科目数
        QueryWrapper<EpsBudgetSubject> leafWrapper = new QueryWrapper<>(queryWrapper);
        leafWrapper.eq("is_leaf", true);
        Long leafSubjects = budgetSubjectMapper.selectCount(leafWrapper);
        statistics.put("leafSubjects", leafSubjects);
        
        // 按层级统计
        Map<Integer, Long> levelStatistics = new HashMap<>();
        for (int level = 1; level <= 5; level++) {
            QueryWrapper<EpsBudgetSubject> levelWrapper = new QueryWrapper<>(queryWrapper);
            levelWrapper.eq("subject_level", level);
            Long levelCount = budgetSubjectMapper.selectCount(levelWrapper);
            if (levelCount > 0) {
                levelStatistics.put(level, levelCount);
            }
        }
        statistics.put("levelStatistics", levelStatistics);
        
        return statistics;
    }

    // 私有辅助方法
    private List<Map<String, Object>> buildSubjectTree(List<EpsBudgetSubject> subjects, Long parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        
        for (EpsBudgetSubject subject : subjects) {
            if (Objects.equals(subject.getParentSubjectId(), parentId)) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", subject.getSubjectId());
                node.put("label", subject.getSubjectName());
                node.put("code", subject.getSubjectCode());
                node.put("type", subject.getSubjectType());
                node.put("level", subject.getSubjectLevel());
                node.put("isLeaf", subject.getIsLeaf());
                node.put("isEnabled", subject.getIsEnabled());
                node.put("data", subject);
                
                List<Map<String, Object>> children = buildSubjectTree(subjects, subject.getSubjectId());
                if (!children.isEmpty()) {
                    node.put("children", children);
                }
                
                tree.add(node);
            }
        }
        
        return tree;
    }

    private Integer getNextSortOrder(Long parentSubjectId) {
        QueryWrapper<EpsBudgetSubject> queryWrapper = new QueryWrapper<>();
        if (parentSubjectId != null) {
            queryWrapper.eq("parent_subject_id", parentSubjectId);
        } else {
            queryWrapper.isNull("parent_subject_id");
        }
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByDesc("sort_order");
        queryWrapper.last("LIMIT 1");
        
        EpsBudgetSubject lastSubject = budgetSubjectMapper.selectOne(queryWrapper);
        if (lastSubject != null && lastSubject.getSortOrder() != null) {
            return lastSubject.getSortOrder() + 1;
        }
        return 1;
    }

    // 其他接口方法的简单实现
    @Override
    public Map<String, Object> importBudgetSubjects(Map<String, Object> importData) {
        // TODO: 实现科目导入
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("importedCount", 0);
        return result;
    }

    @Override
    public Map<String, Object> exportBudgetSubjects(Map<String, Object> exportParams) {
        // TODO: 实现科目导出
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("exportedCount", 0);
        return result;
    }

    @Override
    public Map<String, Object> batchOperateSubjects(Map<String, Object> batchData) {
        // TODO: 实现批量操作
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("processedCount", 0);
        return result;
    }

    @Override
    public Map<String, Object> getBudgetSubjectHierarchy(Long systemId) {
        // TODO: 实现层级结构查询
        return new HashMap<>();
    }

    @Override
    public boolean validateSubjectHierarchy(Long subjectId, Long parentSubjectId) {
        if (parentSubjectId == null) {
            return true;
        }
        
        // 检查是否形成循环引用
        if (subjectId != null && Objects.equals(subjectId, parentSubjectId)) {
            return false;
        }
        
        // 检查父科目是否存在
        EpsBudgetSubject parentSubject = getBudgetSubjectById(parentSubjectId);
        if (parentSubject == null) {
            return false;
        }
        
        // 检查层级深度
        if (parentSubject.getSubjectLevel() >= 5) {
            return false;
        }
        
        // 检查是否会形成循环
        if (subjectId != null) {
            List<EpsBudgetSubject> path = getBudgetSubjectPath(parentSubjectId);
            for (EpsBudgetSubject subject : path) {
                if (Objects.equals(subject.getSubjectId(), subjectId)) {
                    return false;
                }
            }
        }
        
        return true;
    }

    @Override
    public Map<String, Object> getSubjectConfiguration(Long subjectId) {
        // TODO: 实现配置查询
        return new HashMap<>();
    }

    @Override
    public boolean saveSubjectConfiguration(Long subjectId, Map<String, Object> configuration) {
        // TODO: 实现配置保存
        return true;
    }

    @Override
    public Map<String, Object> getSubjectPermissions(Long subjectId, Long userId) {
        // TODO: 实现权限查询
        Map<String, Object> permissions = new HashMap<>();
        permissions.put("canEdit", true);
        permissions.put("canDelete", false);
        return permissions;
    }

    @Override
    public boolean setSubjectPermissions(Long subjectId, Map<String, Object> permissionData) {
        // TODO: 实现权限设置
        return true;
    }

    @Override
    public Map<String, Object> getSubjectUsage(Long subjectId) {
        // TODO: 实现使用情况查询
        Map<String, Object> usage = new HashMap<>();
        usage.put("isUsed", false);
        usage.put("usageCount", 0);
        return usage;
    }

    @Override
    public Map<String, Object> getSubjectRelatedData(Long subjectId) {
        // TODO: 实现关联数据查询
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> syncSubjectData(Long sourceSystemId, Long targetSystemId, Map<String, Object> syncParams) {
        // TODO: 实现数据同步
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> mergeSubjects(List<Long> sourceSubjectIds, Long targetSubjectId, Map<String, Object> mergeParams) {
        // TODO: 实现科目合并
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> splitSubject(Long sourceSubjectId, Map<String, Object> splitParams) {
        // TODO: 实现科目拆分
        return new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> getSubjectTemplates(String templateType) {
        // TODO: 实现模板查询
        return new ArrayList<>();
    }

    @Override
    public boolean applySubjectTemplate(Long systemId, Long templateId, Map<String, Object> applyParams) {
        // TODO: 实现模板应用
        return true;
    }

    @Override
    public List<Map<String, Object>> getSubjectChangeHistory(Long subjectId) {
        // TODO: 实现变更历史查询
        return new ArrayList<>();
    }

    @Override
    public boolean saveSubjectChangeRecord(Map<String, Object> changeRecord) {
        // TODO: 实现变更记录保存
        return true;
    }

    @Override
    public List<Map<String, Object>> getSubjectAuditLogs(Long subjectId, String startDate, String endDate) {
        // TODO: 实现审计日志查询
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> validateSubjectDataIntegrity(Long systemId) {
        // TODO: 实现数据完整性验证
        Map<String, Object> result = new HashMap<>();
        result.put("isValid", true);
        return result;
    }

    @Override
    public Map<String, Object> repairSubjectData(Long systemId, Map<String, Object> repairParams) {
        // TODO: 实现数据修复
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getSubjectPerformanceStatistics(Long systemId, String startDate, String endDate) {
        // TODO: 实现性能统计
        return new HashMap<>();
    }
}
