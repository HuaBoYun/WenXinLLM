package com.management.accountant.service.ncv65.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.ncv65.BudgetIndicator;
import com.management.accountant.mapper.ncv65.BudgetIndicatorMapper;
import com.management.accountant.service.ncv65.IBudgetIndicatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * NCV65全面预算系统 - 预算指标服务实现类
 * 
 * @description 预算指标业务逻辑实现，支持指标的完整生命周期管理
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Slf4j
@Service
public class BudgetIndicatorServiceImpl extends ServiceImpl<BudgetIndicatorMapper, BudgetIndicator> 
        implements IBudgetIndicatorService {

    @Resource
    private BudgetIndicatorMapper budgetIndicatorMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createIndicator(BudgetIndicator indicator) {
        try {
            log.info("开始创建预算指标：{}", indicator.getIndicatorName());
            
            // 1. 业务验证
            if (checkIndicatorCodeExists(indicator.getIndicatorCode(), null)) {
                throw new RuntimeException("指标编码已存在：" + indicator.getIndicatorCode());
            }
            
            // 2. 设置默认值
            if (indicator.getIsEnabled() == null) {
                indicator.setIsEnabled(true);
            }
            if (indicator.getIsRequired() == null) {
                indicator.setIsRequired(false);
            }
            if (indicator.getIsSystem() == null) {
                indicator.setIsSystem(false);
            }
            if (indicator.getIsCalculated() == null) {
                indicator.setIsCalculated(false);
            }
            if (!StringUtils.hasText(indicator.getStatus())) {
                indicator.setStatus(BudgetIndicator.STATUS_ACTIVE);
            }
            
            // 3. 设置层级和排序
            if (StringUtils.hasText(indicator.getParentId())) {
                BudgetIndicator parent = getById(indicator.getParentId());
                if (parent != null) {
                    indicator.setIndicatorLevel(parent.getIndicatorLevel() + 1);
                } else {
                    indicator.setIndicatorLevel(1);
                }
            } else {
                indicator.setIndicatorLevel(1);
            }
            
            // 4. 设置排序号
            if (indicator.getSortOrder() == null) {
                Integer maxSort = budgetIndicatorMapper.getMaxSortOrder(indicator.getParentId(), getCurrentTenantId());
                indicator.setSortOrder(maxSort + 1);
            }
            
            // 5. 验证公式
            if (StringUtils.hasText(indicator.getFormula()) && !validateFormula(indicator.getFormula())) {
                throw new RuntimeException("指标公式格式错误");
            }
            
            // 6. 保存指标
            boolean result = save(indicator);
            
            if (result) {
                log.info("创建预算指标成功：{}", indicator.getIndicatorName());
            } else {
                log.error("创建预算指标失败：{}", indicator.getIndicatorName());
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("创建预算指标异常：{}", e.getMessage(), e);
            throw new RuntimeException("创建预算指标失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateIndicator(BudgetIndicator indicator) {
        try {
            log.info("开始更新预算指标：{}", indicator.getId());
            
            // 1. 检查指标是否存在
            BudgetIndicator existingIndicator = getById(indicator.getId());
            if (existingIndicator == null) {
                throw new RuntimeException("指标不存在");
            }
            
            // 2. 检查编码唯一性
            if (!existingIndicator.getIndicatorCode().equals(indicator.getIndicatorCode())) {
                if (checkIndicatorCodeExists(indicator.getIndicatorCode(), indicator.getId())) {
                    throw new RuntimeException("指标编码已存在：" + indicator.getIndicatorCode());
                }
            }
            
            // 3. 验证公式
            if (StringUtils.hasText(indicator.getFormula()) && !validateFormula(indicator.getFormula())) {
                throw new RuntimeException("指标公式格式错误");
            }
            
            // 4. 更新指标
            boolean result = updateById(indicator);
            
            if (result) {
                log.info("更新预算指标成功：{}", indicator.getId());
            } else {
                log.error("更新预算指标失败：{}", indicator.getId());
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("更新预算指标异常：{}", e.getMessage(), e);
            throw new RuntimeException("更新预算指标失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteIndicator(String id) {
        try {
            log.info("开始删除预算指标：{}", id);
            
            // 1. 检查是否有子指标
            List<BudgetIndicator> children = getChildIndicators(id);
            if (!children.isEmpty()) {
                throw new RuntimeException("存在子指标，无法删除");
            }
            
            // 2. 检查是否被其他指标引用
            BudgetIndicator indicator = getById(id);
            if (indicator != null) {
                List<BudgetIndicator> references = budgetIndicatorMapper.selectByFormulaContains(indicator.getIndicatorCode(), getCurrentTenantId());
                if (!references.isEmpty()) {
                    throw new RuntimeException("指标被其他指标引用，无法删除");
                }
            }
            
            // 3. 删除指标
            boolean result = removeById(id);
            
            if (result) {
                log.info("删除预算指标成功：{}", id);
            } else {
                log.error("删除预算指标失败：{}", id);
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("删除预算指标异常：{}", e.getMessage(), e);
            throw new RuntimeException("删除预算指标失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteIndicators(List<String> ids) {
        try {
            log.info("开始批量删除预算指标：{}", ids);
            
            for (String id : ids) {
                deleteIndicator(id);
            }
            
            log.info("批量删除预算指标成功");
            return true;
            
        } catch (Exception e) {
            log.error("批量删除预算指标异常：{}", e.getMessage(), e);
            throw new RuntimeException("批量删除预算指标失败：" + e.getMessage());
        }
    }

    @Override
    public BudgetIndicator getIndicatorById(String id) {
        return getById(id);
    }

    @Override
    public BudgetIndicator getIndicatorByCode(String indicatorCode) {
        return budgetIndicatorMapper.selectByIndicatorCode(indicatorCode, getCurrentTenantId());
    }

    @Override
    public IPage<BudgetIndicator> getIndicatorPage(Long current, Long size, Map<String, Object> params) {
        Page<BudgetIndicator> page = new Page<>(current, size);
        
        // 添加租户ID
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("tenantId", getCurrentTenantId());
        
        return budgetIndicatorMapper.selectPageWithConditions(page, params);
    }

    @Override
    public List<BudgetIndicator> getIndicatorsByType(String indicatorType) {
        return budgetIndicatorMapper.selectByIndicatorType(indicatorType, getCurrentTenantId());
    }

    @Override
    public List<BudgetIndicator> getEnabledIndicators() {
        return budgetIndicatorMapper.selectEnabledIndicators(getCurrentTenantId());
    }

    @Override
    public List<BudgetIndicator> getRequiredIndicators() {
        return budgetIndicatorMapper.selectRequiredIndicators(getCurrentTenantId());
    }

    @Override
    public List<BudgetIndicator> getSystemIndicators() {
        return budgetIndicatorMapper.selectSystemIndicators(getCurrentTenantId());
    }

    @Override
    public List<BudgetIndicator> getCalculatedIndicators() {
        return budgetIndicatorMapper.selectCalculatedIndicators(getCurrentTenantId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableIndicator(String id) {
        BudgetIndicator indicator = new BudgetIndicator();
        indicator.setId(id);
        indicator.setIsEnabled(true);
        indicator.setUpdateTime(LocalDateTime.now());
        return updateById(indicator);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableIndicator(String id) {
        BudgetIndicator indicator = new BudgetIndicator();
        indicator.setId(id);
        indicator.setIsEnabled(false);
        indicator.setUpdateTime(LocalDateTime.now());
        return updateById(indicator);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateStatus(List<String> ids, String status) {
        return budgetIndicatorMapper.batchUpdateStatus(ids, status, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateEnabled(List<String> ids, Boolean isEnabled) {
        return budgetIndicatorMapper.batchUpdateEnabled(ids, isEnabled, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetIndicator copyIndicator(String id, String targetName, String targetCode) {
        try {
            log.info("开始复制预算指标：{}", id);
            
            // 1. 获取源指标
            BudgetIndicator sourceIndicator = getById(id);
            if (sourceIndicator == null) {
                throw new RuntimeException("源指标不存在");
            }
            
            // 2. 检查目标编码是否存在
            if (checkIndicatorCodeExists(targetCode, null)) {
                throw new RuntimeException("目标指标编码已存在：" + targetCode);
            }
            
            // 3. 创建新指标
            BudgetIndicator newIndicator = new BudgetIndicator();
            // 复制属性（排除ID、编码、名称、创建时间等）
            newIndicator.setIndicatorCode(targetCode);
            newIndicator.setIndicatorName(targetName);
            newIndicator.setIndicatorType(sourceIndicator.getIndicatorType());
            newIndicator.setDataType(sourceIndicator.getDataType());
            newIndicator.setUnit(sourceIndicator.getUnit());
            newIndicator.setFormula(sourceIndicator.getFormula());
            newIndicator.setSummaryMethod(sourceIndicator.getSummaryMethod());
            newIndicator.setDecimalPlaces(sourceIndicator.getDecimalPlaces());
            newIndicator.setDefaultValue(sourceIndicator.getDefaultValue());
            newIndicator.setMinValue(sourceIndicator.getMinValue());
            newIndicator.setMaxValue(sourceIndicator.getMaxValue());
            newIndicator.setIsRequired(sourceIndicator.getIsRequired());
            newIndicator.setIsCalculated(sourceIndicator.getIsCalculated());
            newIndicator.setIsSystem(false); // 复制的指标不是系统指标
            newIndicator.setIsEnabled(true);
            newIndicator.setStatus(BudgetIndicator.STATUS_ACTIVE);
            newIndicator.setDescription(sourceIndicator.getDescription());
            newIndicator.setRemark("复制自：" + sourceIndicator.getIndicatorName());
            
            // 4. 保存新指标
            boolean result = createIndicator(newIndicator);
            
            if (result) {
                log.info("复制预算指标成功：{} -> {}", sourceIndicator.getIndicatorName(), targetName);
                return newIndicator;
            } else {
                throw new RuntimeException("保存新指标失败");
            }
            
        } catch (Exception e) {
            log.error("复制预算指标异常：{}", e.getMessage(), e);
            throw new RuntimeException("复制预算指标失败：" + e.getMessage());
        }
    }

    @Override
    public boolean checkIndicatorCodeExists(String indicatorCode, String excludeId) {
        return budgetIndicatorMapper.checkIndicatorCodeExists(indicatorCode, excludeId, getCurrentTenantId()) > 0;
    }

    @Override
    public List<Map<String, Object>> getIndicatorTree(String parentId) {
        List<BudgetIndicator> indicators;
        if (StringUtils.hasText(parentId)) {
            indicators = budgetIndicatorMapper.selectByParentId(parentId, getCurrentTenantId());
        } else {
            indicators = budgetIndicatorMapper.selectByParentId(null, getCurrentTenantId());
        }
        
        return buildIndicatorTree(indicators);
    }

    @Override
    public List<Map<String, Object>> getFullIndicatorTree() {
        List<BudgetIndicator> allIndicators = budgetIndicatorMapper.selectIndicatorTree(getCurrentTenantId());
        return buildFullIndicatorTree(allIndicators);
    }

    @Override
    public List<BudgetIndicator> getChildIndicators(String parentId) {
        return budgetIndicatorMapper.selectByParentId(parentId, getCurrentTenantId());
    }

    @Override
    public List<BudgetIndicator> getIndicatorsByLevel(Integer level) {
        return budgetIndicatorMapper.selectByLevel(level, getCurrentTenantId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveIndicator(String id, String newParentId) {
        try {
            // 1. 计算新的层级
            Integer newLevel = 1;
            if (StringUtils.hasText(newParentId)) {
                BudgetIndicator parent = getById(newParentId);
                if (parent != null) {
                    newLevel = parent.getIndicatorLevel() + 1;
                }
            }
            
            // 2. 移动指标
            return budgetIndicatorMapper.moveIndicator(id, newParentId, newLevel, getCurrentTenantId()) > 0;
            
        } catch (Exception e) {
            log.error("移动指标异常：{}", e.getMessage(), e);
            throw new RuntimeException("移动指标失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean adjustIndicatorSort(String id, Integer sortOrder) {
        return budgetIndicatorMapper.updateSortOrder(id, sortOrder, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncIndicatorData(String id) {
        return budgetIndicatorMapper.syncIndicatorData(id, "success", getCurrentTenantId()) > 0;
    }

    @Override
    public boolean validateFormula(String formula) {
        // 简单的公式验证逻辑
        if (!StringUtils.hasText(formula)) {
            return true;
        }
        
        // 检查括号匹配
        int leftCount = 0;
        int rightCount = 0;
        for (char c : formula.toCharArray()) {
            if (c == '(') leftCount++;
            if (c == ')') rightCount++;
        }
        
        return leftCount == rightCount;
    }

    @Override
    public Map<String, Object> calculateIndicatorValue(String indicatorId, Map<String, Object> params) {
        // TODO: 实现指标计算逻辑
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("value", 0);
        return result;
    }

    @Override
    public List<Map<String, Object>> getIndicatorDependencies(String indicatorId) {
        // TODO: 实现依赖关系查询
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getIndicatorImpacts(String indicatorId) {
        // TODO: 实现影响范围查询
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> countByIndicatorType() {
        return budgetIndicatorMapper.countByIndicatorType(getCurrentTenantId());
    }

    @Override
    public List<Map<String, Object>> countByDataType() {
        return budgetIndicatorMapper.countByDataType(getCurrentTenantId());
    }

    @Override
    public List<Map<String, Object>> countByStatus() {
        return budgetIndicatorMapper.countByStatus(getCurrentTenantId());
    }

    @Override
    public String exportIndicatorConfig(List<String> ids) {
        // TODO: 实现导出功能
        return "";
    }

    @Override
    public Map<String, Object> importIndicatorConfig(String filePath) {
        // TODO: 实现导入功能
        return new HashMap<>();
    }

    @Override
    public String getIndicatorPath(String indicatorId) {
        return budgetIndicatorMapper.getIndicatorPath(indicatorId, getCurrentTenantId());
    }

    @Override
    public Map<String, Object> validateIndicatorConfig(BudgetIndicator indicator) {
        Map<String, Object> result = new HashMap<>();
        result.put("valid", true);
        result.put("errors", new ArrayList<>());
        return result;
    }

    @Override
    public boolean refreshIndicatorCache(String indicatorId) {
        // TODO: 实现缓存刷新
        return true;
    }

    @Override
    public Map<String, Object> getIndicatorUsageStats(String indicatorId) {
        // TODO: 实现使用统计
        return new HashMap<>();
    }

    /**
     * 构建指标树结构
     */
    private List<Map<String, Object>> buildIndicatorTree(List<BudgetIndicator> indicators) {
        return indicators.stream().map(indicator -> {
            Map<String, Object> node = new HashMap<>();
            node.put("id", indicator.getId());
            node.put("label", indicator.getIndicatorName());
            node.put("code", indicator.getIndicatorCode());
            node.put("type", indicator.getIndicatorType());
            node.put("level", indicator.getIndicatorLevel());
            node.put("isEnabled", indicator.getIsEnabled());
            
            // 查询子节点
            List<BudgetIndicator> children = getChildIndicators(indicator.getId());
            if (!children.isEmpty()) {
                node.put("children", buildIndicatorTree(children));
            }
            
            return node;
        }).collect(Collectors.toList());
    }

    /**
     * 构建完整指标树结构
     */
    private List<Map<String, Object>> buildFullIndicatorTree(List<BudgetIndicator> allIndicators) {
        // 按父子关系构建树结构
        Map<String, List<BudgetIndicator>> parentChildMap = allIndicators.stream()
                .collect(Collectors.groupingBy(indicator -> 
                        StringUtils.hasText(indicator.getParentId()) ? indicator.getParentId() : "root"));
        
        return buildTreeRecursive(parentChildMap.get("root"), parentChildMap);
    }

    /**
     * 递归构建树结构
     */
    private List<Map<String, Object>> buildTreeRecursive(List<BudgetIndicator> indicators, 
                                                        Map<String, List<BudgetIndicator>> parentChildMap) {
        if (indicators == null) {
            return new ArrayList<>();
        }
        
        return indicators.stream().map(indicator -> {
            Map<String, Object> node = new HashMap<>();
            node.put("id", indicator.getId());
            node.put("label", indicator.getIndicatorName());
            node.put("code", indicator.getIndicatorCode());
            node.put("type", indicator.getIndicatorType());
            node.put("level", indicator.getIndicatorLevel());
            node.put("isEnabled", indicator.getIsEnabled());
            
            // 递归构建子节点
            List<BudgetIndicator> children = parentChildMap.get(indicator.getId());
            if (children != null && !children.isEmpty()) {
                node.put("children", buildTreeRecursive(children, parentChildMap));
            }
            
            return node;
        }).collect(Collectors.toList());
    }

    /**
     * 获取当前租户ID
     */
    private String getCurrentTenantId() {
        // TODO: 从上下文获取租户ID
        return "default";
    }
}
