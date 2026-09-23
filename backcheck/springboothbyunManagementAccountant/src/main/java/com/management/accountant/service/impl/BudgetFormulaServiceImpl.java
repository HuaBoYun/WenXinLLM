package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetFormula;
import com.management.accountant.oracle.mapper.budget.BudgetFormulaMapper;
import com.management.accountant.service.BudgetFormulaService;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.*;

/**
 * 预算公式Service实现类
 * 
 * @description 预算公式业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetFormulaServiceImpl implements BudgetFormulaService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetFormulaMapper formulaMapper;

    @Resource
    private UserProvider userProvider;

    private String getCurrentUser() {
        try {
            TblStaffUtil staff = userProvider.get();
            return staff != null ? staff.getUsername() : "admin";
        } catch (Exception e) {
            log.error("获取当前用户失败", e);
            return "admin";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetFormula create(BudgetFormula formula) {
        // 1. 参数校验
        if (formula == null) {
            throw new ServiceException("公式信息不能为空");
        }
        if (!StringUtils.hasText(formula.getFormulaName())) {
            throw new ServiceException("公式名称不能为空");
        }
        if (!StringUtils.hasText(formula.getFormulaExpression())) {
            throw new ServiceException("公式表达式不能为空");
        }

        // 2. 检查公式编码是否重复
        if (StringUtils.hasText(formula.getFormulaCode())) {
            QueryWrapper<BudgetFormula> wrapper = new QueryWrapper<>();
            wrapper.eq("FORMULA_CODE", formula.getFormulaCode())
                   .eq("DEL_FLAG", 0L);
            long count = formulaMapper.selectCount(wrapper);
            if (count > 0) {
                throw new ServiceException("公式编码已存在");
            }
        } else {
            // 自动生成公式编码
            formula.setFormulaCode(generateFormulaCode());
        }

        // 3. 设置默认值
        if (formula.getDelFlag() == null) {
            formula.setDelFlag(0);
        }
        if (formula.getIsEnabled() == null) {
            formula.setIsEnabled(1);
        }
        // 设置创建人/更新人
        String currentUser = getCurrentUser();
        if (!StringUtils.hasText(formula.getCreateBy())) {
            formula.setCreateBy(currentUser);
        }
        if (!StringUtils.hasText(formula.getUpdateBy())) {
            formula.setUpdateBy(currentUser);
        }
        formula.setCreateTime(new Date());
        formula.setUpdateTime(new Date());

        // 4. 插入数据库
        int result = formulaMapper.insert(formula);
        if (result <= 0) {
            throw new ServiceException("创建公式失败");
        }

        log.info("创建公式成功，ID: {}", formula.getFormulaId());
        return formula;
    }

    @Override
    public BudgetFormula getById(String formulaId) {
        if (!StringUtils.hasText(formulaId)) {
            throw new ServiceException("公式ID不能为空");
        }
        
        QueryWrapper<BudgetFormula> wrapper = new QueryWrapper<>();
        wrapper.eq("FORMULA_ID", formulaId)
               .eq("DEL_FLAG", 0L);
        
        return formulaMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetFormula formula) {
        if (formula == null || !StringUtils.hasText(formula.getFormulaId())) {
            throw new ServiceException("公式ID不能为空");
        }

        BudgetFormula existing = getById(formula.getFormulaId());
        if (existing == null) {
            throw new ServiceException("公式不存在");
        }

        // 检查公式编码是否重复
        if (StringUtils.hasText(formula.getFormulaCode()) && !formula.getFormulaCode().equals(existing.getFormulaCode())) {
            QueryWrapper<BudgetFormula> wrapper = new QueryWrapper<>();
            wrapper.eq("FORMULA_CODE", formula.getFormulaCode())
                   .eq("DEL_FLAG", 0L)
                   .ne("FORMULA_ID", formula.getFormulaId());
            long count = formulaMapper.selectCount(wrapper);
            if (count > 0) {
                throw new ServiceException("公式编码已存在");
            }
        }

        formula.setUpdateTime(new Date());
        int result = formulaMapper.updateById(formula);
        if (result <= 0) {
            throw new ServiceException("更新公式失败");
        }

        log.info("更新公式成功，ID: {}", formula.getFormulaId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String formulaId) {
        if (!StringUtils.hasText(formulaId)) {
            throw new ServiceException("公式ID不能为空");
        }

        BudgetFormula formula = getById(formulaId);
        if (formula == null) {
            throw new ServiceException("公式不存在");
        }

        BudgetFormula update = new BudgetFormula();
        update.setFormulaId(formulaId);
        update.setDelFlag(1);
        update.setUpdateTime(new Date());

        int result = formulaMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("删除公式失败");
        }

        log.info("删除公式成功，ID: {}", formulaId);
    }

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetFormula> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0L);

        // 公式编码
        if (hasValue(params.get("formulaCode"))) {
            wrapper.like("FORMULA_CODE", params.get("formulaCode"));
        }

        // 公式名称
        if (hasValue(params.get("formulaName"))) {
            wrapper.like("FORMULA_NAME", params.get("formulaName"));
        }

        // 公式类型
        if (hasValue(params.get("formulaType"))) {
            wrapper.eq("FORMULA_TYPE", params.get("formulaType"));
        }

        // 公式状态筛选（映射到 isEnabled）
        if (hasValue(params.get("formulaStatus"))) {
            String status = params.get("formulaStatus").toString();
            if ("DRAFT".equals(status)) {
                wrapper.eq("IS_ENABLED", 0);
            } else if ("PUBLISHED".equals(status)) {
                wrapper.eq("IS_ENABLED", 1);
            }
        }

        // 分类筛选
        if (hasValue(params.get("categoryId"))) {
            wrapper.eq("FORMULA_TYPE", params.get("categoryId"));
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetFormula> page = new Page<>(pageNum, pageSize);
        IPage<BudgetFormula> pageResult = formulaMapper.selectPage(page, wrapper);

        // 4. 填充虚拟字段
        for (BudgetFormula f : pageResult.getRecords()) {
            populateVirtualFields(f);
        }

        // 5. 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);

        return result;
    }

    @Override
    public Map<String, Object> validateFormula(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String expression = (String) params.get("expression");
            if (!StringUtils.hasText(expression)) {
                result.put("valid", false);
                result.put("message", "公式表达式不能为空");
                return result;
            }

            // 基本语法检查
            if (!expression.matches("[a-zA-Z0-9+\\-*/()\\s.]+")) {
                result.put("valid", false);
                result.put("message", "公式包含非法字符");
                return result;
            }

            // 括号匹配检查
            int leftCount = 0;
            int rightCount = 0;
            for (char c : expression.toCharArray()) {
                if (c == '(') leftCount++;
                if (c == ')') rightCount++;
            }
            if (leftCount != rightCount) {
                result.put("valid", false);
                result.put("message", "括号不匹配");
                return result;
            }

            result.put("valid", true);
            result.put("message", "公式验证通过");
            
        } catch (Exception e) {
            log.error("验证公式异常", e);
            result.put("valid", false);
            result.put("message", "验证失败：" + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> calculateFormula(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String formulaId = (String) params.get("formulaId");
            Map<String, Object> variables = (Map<String, Object>) params.get("variables");
            
            if (!StringUtils.hasText(formulaId)) {
                result.put("success", false);
                result.put("message", "公式ID不能为空");
                return result;
            }

            BudgetFormula formula = getById(formulaId);
            if (formula == null) {
                result.put("success", false);
                result.put("message", "公式不存在");
                return result;
            }

            if (formula.getIsEnabled() == null || formula.getIsEnabled() != 1) {
                result.put("success", false);
                result.put("message", "公式未启用");
                return result;
            }

            // 替换变量
            String expression = formula.getFormulaExpression();
            if (variables != null && !variables.isEmpty()) {
                for (Map.Entry<String, Object> entry : variables.entrySet()) {
                    expression = expression.replace(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }

            // 使用JavaScript引擎计算
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            Object calcResult = engine.eval(expression);

            result.put("success", true);
            result.put("result", calcResult);
            result.put("expression", expression);
            
        } catch (Exception e) {
            log.error("计算公式异常", e);
            result.put("success", false);
            result.put("message", "计算失败：" + e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要删除的记录");
        }

        for (String id : ids) {
            try {
                delete(id);
            } catch (Exception e) {
                log.error("批量删除失败，ID: {}", id, e);
            }
        }

        log.info("批量删除完成，数量: {}", ids.size());
    }

    @Override
    public List<BudgetFormula> getByType(String formulaType) {
        if (!StringUtils.hasText(formulaType)) {
            throw new ServiceException("公式类型不能为空");
        }

        QueryWrapper<BudgetFormula> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0L)
               .eq("FORMULA_TYPE", formulaType)
               .orderByDesc("CREATE_TIME");

        return formulaMapper.selectList(wrapper);
    }

    @Override
    public List<BudgetFormula> getEnabledFormulas() {
        QueryWrapper<BudgetFormula> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0L)
               .eq("IS_ENABLED", 1)
               .orderByDesc("CREATE_TIME");

        return formulaMapper.selectList(wrapper);
    }

    /**
     * 生成公式编码
     */
    private String generateFormulaCode() {
        return "FORMULA" + System.currentTimeMillis();
    }

    @Override
    public Map<String, Object> getStats() {
        QueryWrapper<BudgetFormula> total = new QueryWrapper<BudgetFormula>().eq("DEL_FLAG", 0L);
        QueryWrapper<BudgetFormula> active = new QueryWrapper<BudgetFormula>().eq("DEL_FLAG", 0L).eq("IS_ENABLED", 1);
        long totalCount = formulaMapper.selectCount(total);
        long activeCount = formulaMapper.selectCount(active);
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFormulas", totalCount);
        stats.put("activeFormulas", activeCount);
        stats.put("complexFormulas", 0);
        stats.put("errorFormulas", 0);
        stats.put("activeRate", totalCount > 0 ? Math.round(activeCount * 100.0 / totalCount * 10) / 10.0 : 0);
        stats.put("complexRate", 0);
        stats.put("errorRate", 0);
        return stats;
    }

    private boolean hasValue(Object val) {
        if (val == null) return false;
        if (val instanceof String) return !((String) val).trim().isEmpty();
        if (val instanceof java.util.Collection) return !((java.util.Collection<?>) val).isEmpty();
        return true;
    }

    /**
     * 填充虚拟字段（formulaStatus, complexity, validationStatus）
     */
    private void populateVirtualFields(BudgetFormula formula) {
        if (formula == null) return;
        // formulaStatus: 根据 isEnabled 映射
        if (formula.getIsEnabled() != null && formula.getIsEnabled() == 1) {
            formula.setFormulaStatus("PUBLISHED");
        } else {
            formula.setFormulaStatus("DRAFT");
        }
        // complexity: 根据公式表达式长度简单计算
        String expr = formula.getFormulaExpression();
        if (expr != null) {
            int len = expr.length();
            if (len > 200) formula.setComplexity(5);
            else if (len > 100) formula.setComplexity(4);
            else if (len > 50) formula.setComplexity(3);
            else if (len > 20) formula.setComplexity(2);
            else formula.setComplexity(1);
        } else {
            formula.setComplexity(1);
        }
        // validationStatus: 根据 remark 字段判断
        String remark = formula.getRemark();
        if (remark != null && remark.startsWith("验证通过")) {
            formula.setValidationStatus("VALID");
        } else if (remark != null && remark.startsWith("验证失败")) {
            formula.setValidationStatus("INVALID");
        } else {
            formula.setValidationStatus("PENDING");
        }
    }

    @Override
    public List<Map<String, Object>> getCategoryTree() {
        List<Map<String, Object>> tree = new ArrayList<>();
        String[] types = {"CALCULATION", "VALIDATION", "ALLOCATION", "FORECAST"};
        String[] names = {"计算公式", "验证公式", "分配公式", "预测公式"};
        for (int i = 0; i < types.length; i++) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", types[i]);
            node.put("name", names[i]);
            node.put("label", names[i]);
            node.put("value", types[i]);
            // 查询该分类下的公式数量
            QueryWrapper<BudgetFormula> wrapper = new QueryWrapper<>();
            wrapper.eq("DEL_FLAG", 0L).eq("FORMULA_TYPE", types[i]);
            long count = formulaMapper.selectCount(wrapper);
            node.put("formulaCount", count);
            node.put("children", new ArrayList<>());
            tree.add(node);
        }
        return tree;
    }

    @Override
    public List<Map<String, Object>> getCategories() {
        return getCategoryTree();
    }

    @Override
    public Map<String, Object> validateById(String formulaId) {
        Map<String, Object> result = new HashMap<>();
        BudgetFormula formula = getById(formulaId);
        if (formula == null) {
            result.put("valid", false);
            result.put("message", "公式不存在");
            return result;
        }
        // 执行表达式验证
        Map<String, Object> params = new HashMap<>();
        params.put("expression", formula.getFormulaExpression());
        Map<String, Object> validateResult = validateFormula(params);
        boolean valid = Boolean.TRUE.equals(validateResult.get("valid"));

        // 把验证结果写回数据库备注字段，并更新 isEnabled 状态
        BudgetFormula updateEntity = new BudgetFormula();
        updateEntity.setFormulaId(formulaId);
        updateEntity.setUpdateTime(new Date());
        if (valid) {
            updateEntity.setRemark("验证通过 - " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } else {
            updateEntity.setRemark("验证失败: " + validateResult.get("message") + " - " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
        formulaMapper.updateById(updateEntity);

        result.put("valid", valid);
        result.put("message", valid ? "验证通过" : validateResult.get("message"));
        result.put("formulaId", formulaId);
        result.put("formulaName", formula.getFormulaName());
        result.put("validationStatus", valid ? "VALID" : "INVALID");
        return result;
    }

    @Override
    public Map<String, Object> batchValidate(List<String> ids) {
        Map<String, Object> result = new HashMap<>();
        int validCount = 0;
        int invalidCount = 0;
        List<Map<String, Object>> details = new ArrayList<>();

        for (String id : ids) {
            Map<String, Object> single = validateById(id);
            boolean valid = Boolean.TRUE.equals(single.get("valid"));
            if (valid) {
                validCount++;
            } else {
                invalidCount++;
            }
            details.add(single);
        }

        result.put("total", ids.size());
        result.put("valid", validCount);
        result.put("invalid", invalidCount);
        result.put("details", details);
        return result;
    }

    @Override
    public Map<String, Object> importFormulas(List<BudgetFormula> formulas) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        String currentUser = getCurrentUser();

        for (int i = 0; i < formulas.size(); i++) {
            try {
                BudgetFormula f = formulas.get(i);
                if (!StringUtils.hasText(f.getFormulaName())) {
                    errors.add("第" + (i + 1) + "行：公式名称不能为空");
                    failCount++;
                    continue;
                }
                if (!StringUtils.hasText(f.getFormulaExpression())) {
                    errors.add("第" + (i + 1) + "行：公式表达式不能为空");
                    failCount++;
                    continue;
                }
                // 自动生成编码
                if (!StringUtils.hasText(f.getFormulaCode())) {
                    f.setFormulaCode(generateFormulaCode());
                }
                if (f.getDelFlag() == null) f.setDelFlag(0);
                if (f.getIsEnabled() == null) f.setIsEnabled(0);
                if (!StringUtils.hasText(f.getCreateBy())) f.setCreateBy(currentUser);
                if (!StringUtils.hasText(f.getUpdateBy())) f.setUpdateBy(currentUser);
                f.setCreateTime(new Date());
                f.setUpdateTime(new Date());
                formulaMapper.insert(f);
                successCount++;
            } catch (Exception e) {
                errors.add("第" + (i + 1) + "行：" + e.getMessage());
                failCount++;
            }
        }

        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        return result;
    }

    @Override
    public Map<String, Object> exportFormulas(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("exportUrl", "/exports/formulas_" + System.currentTimeMillis() + ".xlsx");
        result.put("exportTime", new Date());
        return result;
    }

    @Override
    public Map<String, Object> exportById(String formulaId) {
        Map<String, Object> result = new HashMap<>();
        result.put("exportUrl", "/exports/formula_" + formulaId + ".xlsx");
        result.put("exportTime", new Date());
        return result;
    }
}

