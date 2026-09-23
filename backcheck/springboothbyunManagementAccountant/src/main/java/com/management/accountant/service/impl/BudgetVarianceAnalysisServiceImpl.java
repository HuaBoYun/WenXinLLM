package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.budget.BudgetVarianceAnalysis;
import com.management.accountant.oracle.entity.TblStaffOracle;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.budget.BudgetVarianceAnalysisMapper;
import com.management.accountant.oracle.mapper.TblStaffOracleMapper;
import com.management.accountant.service.BudgetVarianceAnalysisService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 预算差异分析Service实现类
 * 
 * @description 预算差异分析业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetVarianceAnalysisServiceImpl implements BudgetVarianceAnalysisService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetVarianceAnalysisMapper varianceMapper;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Resource
    private TblStaffOracleMapper staffMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetVarianceAnalysis create(BudgetVarianceAnalysis variance) {
        // 1. 参数校验
        if (variance == null) {
            throw new ServiceException("差异分析信息不能为空");
        }
        if (!StringUtils.hasText(variance.getAnalysisName())) {
            throw new ServiceException("分析名称不能为空");
        }

        // 2. 生成分析编码
        if (!StringUtils.hasText(variance.getAnalysisCode())) {
            variance.setAnalysisCode(generateAnalysisCode());
        }

        // 3. 如果有组织ID，查询组织名称
        if (StringUtils.hasText(variance.getOrganizationId())) {
            try {
                QueryWrapper<com.management.accountant.oracle.entity.budget.BudgetOrganization> orgWrapper = new QueryWrapper<>();
                orgWrapper.eq("ORGANIZATION_ID", variance.getOrganizationId());
                orgWrapper.eq("DEL_FLAG", 0);  // 只查询未删除的记录
                com.management.accountant.oracle.entity.budget.BudgetOrganization org = organizationMapper.selectOne(orgWrapper);
                if (org != null && StringUtils.hasText(org.getOrganizationName())) {
                    variance.setOrganizationName(org.getOrganizationName());
                } else {
                    log.warn("未找到组织ID对应的组织记录: {}", variance.getOrganizationId());
                }
            } catch (Exception e) {
                log.error("查询组织信息失败，组织ID: {}", variance.getOrganizationId(), e);
            }
        }

        // 4. 如果有科目ID，查询科目名称
        if (StringUtils.hasText(variance.getAccountId())) {
            try {
                QueryWrapper<BudgetAccount> accountWrapper = new QueryWrapper<>();
                accountWrapper.eq("ACCOUNT_ID", variance.getAccountId());
                accountWrapper.eq("DEL_FLAG", 0);  // 只查询未删除的记录
                BudgetAccount account = accountMapper.selectOne(accountWrapper);
                if (account != null && StringUtils.hasText(account.getAccountName())) {
                    variance.setAccountName(account.getAccountName());
                } else {
                    log.warn("未找到科目ID对应的科目记录: {}", variance.getAccountId());
                }
            } catch (Exception e) {
                log.error("查询科目信息失败，科目ID: {}", variance.getAccountId(), e);
            }
        }

        // 5. 计算差异
        if (variance.getBudgetAmount() != null && variance.getActualAmount() != null) {
            BigDecimal varianceAmount = variance.getActualAmount().subtract(variance.getBudgetAmount());
            variance.setVarianceAmount(varianceAmount);

            if (variance.getBudgetAmount().compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal varianceRate = varianceAmount.divide(variance.getBudgetAmount(), 4, RoundingMode.HALF_UP)
                                                       .multiply(new BigDecimal("100"));
                variance.setVarianceRate(varianceRate);
            }

            // 设置差异类型
            if (varianceAmount.compareTo(BigDecimal.ZERO) > 0) {
                variance.setVarianceType("POSITIVE");
            } else if (varianceAmount.compareTo(BigDecimal.ZERO) < 0) {
                variance.setVarianceType("NEGATIVE");
            } else {
                variance.setVarianceType("ZERO");
            }
        }

        // 6. 设置默认值
        if (variance.getDelFlag() == null) {
            variance.setDelFlag(0);
        }
        if (variance.getAnalysisStatus() == null) {
            variance.setAnalysisStatus("draft");
        }
        // 设置分析时间为当前时间
        variance.setAnalyzedTime(new Date());
        variance.setCreateTime(new Date());
        variance.setUpdateTime(new Date());

        // 7. 插入数据库
        int result = varianceMapper.insert(variance);
        if (result <= 0) {
            throw new ServiceException("创建差异分析失败");
        }

        log.info("创建差异分析成功，ID: {}", variance.getId());
        return variance;
    }

    @Override
    public BudgetVarianceAnalysis getById(String id) {
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("分析ID不能为空");
        }

        QueryWrapper<BudgetVarianceAnalysis> wrapper = new QueryWrapper<>();
        wrapper.eq("ID", id)
               .eq("DEL_FLAG", 0);

        return varianceMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetVarianceAnalysis update(BudgetVarianceAnalysis variance) {
        // 1. 参数校验
        if (variance == null) {
            throw new ServiceException("差异分析信息不能为空");
        }
        if (!StringUtils.hasText(variance.getId())) {
            throw new ServiceException("差异分析ID不能为空");
        }

        // 2. 检查记录是否存在
        BudgetVarianceAnalysis existing = getById(variance.getId());
        if (existing == null) {
            throw new ServiceException("差异分析记录不存在或已删除");
        }

        // 3. 如果组织ID或科目ID有变化，重新查询名称
        if (StringUtils.hasText(variance.getOrganizationId()) &&
            !variance.getOrganizationId().equals(existing.getOrganizationId())) {
            try {
                QueryWrapper<com.management.accountant.oracle.entity.budget.BudgetOrganization> orgWrapper = new QueryWrapper<>();
                orgWrapper.eq("ORGANIZATION_ID", variance.getOrganizationId());
                orgWrapper.eq("DEL_FLAG", 0);
                com.management.accountant.oracle.entity.budget.BudgetOrganization org = organizationMapper.selectOne(orgWrapper);
                if (org != null && StringUtils.hasText(org.getOrganizationName())) {
                    variance.setOrganizationName(org.getOrganizationName());
                } else {
                    log.warn("未找到组织ID对应的组织记录: {}", variance.getOrganizationId());
                }
            } catch (Exception e) {
                log.error("查询组织信息失败，组织ID: {}", variance.getOrganizationId(), e);
            }
        } else {
            // 如果组织ID没变，保留原有名称
            variance.setOrganizationName(existing.getOrganizationName());
        }

        if (StringUtils.hasText(variance.getAccountId()) &&
            !variance.getAccountId().equals(existing.getAccountId())) {
            try {
                QueryWrapper<BudgetAccount> accountWrapper = new QueryWrapper<>();
                accountWrapper.eq("ACCOUNT_ID", variance.getAccountId());
                accountWrapper.eq("DEL_FLAG", 0);
                BudgetAccount account = accountMapper.selectOne(accountWrapper);
                if (account != null && StringUtils.hasText(account.getAccountName())) {
                    variance.setAccountName(account.getAccountName());
                } else {
                    log.warn("未找到科目ID对应的科目记录: {}", variance.getAccountId());
                }
            } catch (Exception e) {
                log.error("查询科目信息失败，科目ID: {}", variance.getAccountId(), e);
            }
        } else {
            // 如果科目ID没变，保留原有名称
            variance.setAccountName(existing.getAccountName());
        }

        // 4. 计算差异（如果提供了预算金额和实际金额）
        if (variance.getBudgetAmount() != null && variance.getActualAmount() != null) {
            BigDecimal varianceAmount = variance.getActualAmount().subtract(variance.getBudgetAmount());
            variance.setVarianceAmount(varianceAmount);

            if (variance.getBudgetAmount().compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal varianceRate = varianceAmount.divide(variance.getBudgetAmount(), 4, RoundingMode.HALF_UP)
                                                       .multiply(new BigDecimal("100"));
                variance.setVarianceRate(varianceRate);

                // 设置差异类型
                if (varianceAmount.compareTo(BigDecimal.ZERO) > 0) {
                    variance.setVarianceType("POSITIVE");
                } else if (varianceAmount.compareTo(BigDecimal.ZERO) < 0) {
                    variance.setVarianceType("NEGATIVE");
                } else {
                    variance.setVarianceType("ZERO");
                }
            }
        }

        // 5. 设置更新时间
        variance.setUpdateTime(new Date());

        // 6. 执行更新
        int result = varianceMapper.updateById(variance);
        if (result <= 0) {
            throw new ServiceException("更新差异分析失败");
        }

        log.info("更新差异分析成功，ID: {}", variance.getId());
        return getById(variance.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("差异分析ID不能为空");
        }

        // 检查记录是否存在
        BudgetVarianceAnalysis existing = getById(id);
        if (existing == null) {
            throw new ServiceException("差异分析记录不存在或已删除");
        }

        // 逻辑删除
        existing.setDelFlag(1);
        existing.setUpdateTime(new Date());
        int result = varianceMapper.updateById(existing);
        if (result <= 0) {
            throw new ServiceException("删除差异分析失败");
        }

        log.info("删除差异分析成功，ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("差异分析ID列表不能为空");
        }

        int result = varianceMapper.batchDelete(ids);
        if (result <= 0) {
            throw new ServiceException("批量删除差异分析失败");
        }

        log.info("批量删除差异分析成功，数量: {}", result);
    }

    @Override
    public PageResult<BudgetVarianceAnalysis> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建 XML 查询参数：前端字段名 → 数据库字段名适配
        Map<String, Object> queryParams = new HashMap<>();
        // 组织ID：前端字段 organizationPath 对应数据库 ORGANIZATION_ID
        Object orgPath = params.get("organizationPath");
        if (orgPath == null) orgPath = params.get("organizationId");
        if (orgPath != null && !orgPath.toString().isEmpty()) {
            queryParams.put("organizationId", orgPath.toString());
        }
        // 预算科目ID：前端字段 budgetAccount 对应数据库 ACCOUNT_ID
        Object acct = params.get("budgetAccount");
        if (acct == null) acct = params.get("accountId");
        if (acct != null && !acct.toString().isEmpty()) {
            queryParams.put("accountId", acct.toString());
        }
        // 差异类型
        Object vType = params.get("varianceType");
        if (vType != null && !vType.toString().isEmpty()) {
            queryParams.put("varianceType", vType.toString());
        }
        // 分析日期范围：前端字段 analysisDateRange 是 [startDate, endDate]
        Object dateRange = params.get("analysisDateRange");
        if (dateRange instanceof List) {
            List<?> range = (List<?>) dateRange;
            if (range.size() >= 2) {
                queryParams.put("startDate", range.get(0) != null ? range.get(0).toString().substring(0, 10) : null);
                queryParams.put("endDate", range.get(1) != null ? range.get(1).toString().substring(0, 10) : null);
            }
        }

        // 3. 使用 PageHelper 分页
        PageHelper.startPage(pageNum, pageSize);
        List<BudgetVarianceAnalysis> list = varianceMapper.selectByPage(queryParams);
        PageInfo<BudgetVarianceAnalysis> pageInfo = new PageInfo<>(list);

        // 4. 封装返回结果
        PageResult<BudgetVarianceAnalysis> result = new PageResult<>();
        result.setTlist(list);
        result.setTotalRecord((int) pageInfo.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);
        result.setTotalPage(pageInfo.getPages());
        return result;
    }

    @Override
    public Map<String, Object> analyze(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        try {
            String budgetId = (String) params.get("budgetId");
            Integer budgetYear = params.get("budgetYear") != null ? Integer.parseInt(params.get("budgetYear").toString()) : null;
            String budgetPeriod = (String) params.get("budgetPeriod");

            if (!StringUtils.hasText(budgetId)) {
                result.put("success", false);
                result.put("message", "预算ID不能为空");
                return result;
            }

            // 从数据库查询实际差异分析数据
            QueryWrapper<BudgetVarianceAnalysis> wrapper = new QueryWrapper<>();
            wrapper.eq("DEL_FLAG", 0);
            wrapper.eq("BUDGET_ID", budgetId);
            if (budgetYear != null) {
                wrapper.eq("BUDGET_YEAR", budgetYear);
            }
            if (StringUtils.hasText(budgetPeriod)) {
                wrapper.eq("BUDGET_PERIOD", budgetPeriod);
            }
            List<BudgetVarianceAnalysis> records = varianceMapper.selectList(wrapper);

            BigDecimal totalBudget = BigDecimal.ZERO;
            BigDecimal totalActual = BigDecimal.ZERO;
            for (BudgetVarianceAnalysis record : records) {
                if (record.getBudgetAmount() != null) {
                    totalBudget = totalBudget.add(record.getBudgetAmount());
                }
                if (record.getActualAmount() != null) {
                    totalActual = totalActual.add(record.getActualAmount());
                }
            }
            BigDecimal totalVariance = totalActual.subtract(totalBudget);
            BigDecimal varianceRate = BigDecimal.ZERO;
            if (totalBudget.compareTo(BigDecimal.ZERO) != 0) {
                varianceRate = totalVariance.divide(totalBudget, 4, RoundingMode.HALF_UP)
                                           .multiply(new BigDecimal("100"));
            }

            result.put("success", true);
            result.put("budgetId", budgetId);
            result.put("budgetYear", budgetYear);
            result.put("budgetPeriod", budgetPeriod);
            result.put("totalBudget", totalBudget);
            result.put("totalActual", totalActual);
            result.put("totalVariance", totalVariance);
            result.put("varianceRate", varianceRate);
            result.put("analyzeTime", new Date());

            // 差异类型分析
            if (totalVariance.compareTo(BigDecimal.ZERO) > 0) {
                result.put("varianceType", "FAVORABLE"); // 有利差异
            } else if (totalVariance.compareTo(BigDecimal.ZERO) < 0) {
                result.put("varianceType", "UNFAVORABLE"); // 不利差异
            } else {
                result.put("varianceType", "NONE"); // 无差异
            }

            log.info("执行差异分析成功，预算ID: {}", budgetId);

        } catch (Exception e) {
            log.error("执行差异分析异常", e);
            result.put("success", false);
            result.put("message", "分析失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> generateReport(Map<String, Object> params) {
        Map<String, Object> report = new HashMap<>();

        try {
            String budgetId = (String) params.get("budgetId");
            Integer budgetYear = params.get("budgetYear") != null ? Integer.parseInt(params.get("budgetYear").toString()) : null;
            String budgetPeriod = (String) params.get("budgetPeriod");

            if (!StringUtils.hasText(budgetId)) {
                report.put("success", false);
                report.put("message", "预算ID不能为空");
                return report;
            }

            // 执行差异分析
            Map<String, Object> analyzeResult = analyze(params);

            // 构建报告
            report.put("success", true);
            report.put("reportTitle", "预算差异分析报告");
            report.put("budgetId", budgetId);
            report.put("budgetYear", budgetYear);
            report.put("budgetPeriod", budgetPeriod);
            report.put("generateTime", new Date());
            report.put("analyzeData", analyzeResult);

            // 差异分析汇总
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalBudget", analyzeResult.get("totalBudget"));
            summary.put("totalActual", analyzeResult.get("totalActual"));
            summary.put("totalVariance", analyzeResult.get("totalVariance"));
            summary.put("varianceRate", analyzeResult.get("varianceRate"));
            summary.put("varianceType", analyzeResult.get("varianceType"));
            report.put("summary", summary);

            // 差异原因分析 - 从数据库查询
            QueryWrapper<BudgetVarianceAnalysis> reasonWrapper = new QueryWrapper<>();
            reasonWrapper.eq("DEL_FLAG", 0);
            reasonWrapper.eq("BUDGET_ID", budgetId);
            if (budgetYear != null) {
                reasonWrapper.eq("BUDGET_YEAR", budgetYear);
            }
            reasonWrapper.isNotNull("VARIANCE_REASON");
            reasonWrapper.orderByDesc("VARIANCE_AMOUNT");
            reasonWrapper.last("FETCH FIRST 10 ROWS ONLY");
            List<BudgetVarianceAnalysis> reasonRecords = varianceMapper.selectList(reasonWrapper);

            List<Map<String, Object>> reasons = new ArrayList<>();
            for (BudgetVarianceAnalysis r : reasonRecords) {
                Map<String, Object> reasonItem = new HashMap<>();
                reasonItem.put("category", r.getAccountName() != null ? r.getAccountName() : "未分类");
                reasonItem.put("description", r.getVarianceReason() != null ? r.getVarianceReason() : "");
                reasonItem.put("impact", r.getVarianceType() != null ? r.getVarianceType() : "未知");
                reasonItem.put("amount", r.getVarianceAmount());
                reasons.add(reasonItem);
            }
            report.put("reasons", reasons);

            // 改进建议 - 从数据库记录中提取
            QueryWrapper<BudgetVarianceAnalysis> improveWrapper = new QueryWrapper<>();
            improveWrapper.eq("DEL_FLAG", 0);
            improveWrapper.eq("BUDGET_ID", budgetId);
            improveWrapper.isNotNull("IMPROVEMENT_MEASURES");
            improveWrapper.last("FETCH FIRST 10 ROWS ONLY");
            List<BudgetVarianceAnalysis> improveRecords = varianceMapper.selectList(improveWrapper);

            List<String> suggestions = new ArrayList<>();
            for (BudgetVarianceAnalysis ir : improveRecords) {
                if (StringUtils.hasText(ir.getImprovementMeasures())) {
                    suggestions.add(ir.getImprovementMeasures());
                }
            }
            if (suggestions.isEmpty()) {
                suggestions.add("暂无改进建议，请在差异分析中添加改进措施");
            }
            report.put("suggestions", suggestions);

            log.info("生成差异分析报告成功，预算ID: {}", budgetId);

        } catch (Exception e) {
            log.error("生成差异分析报告异常", e);
            report.put("success", false);
            report.put("message", "生成失败：" + e.getMessage());
        }

        return report;
    }

    /**
     * 生成分析编码
     */
    private String generateAnalysisCode() {
        return "VAR" + System.currentTimeMillis();
    }

    @Override
    public Object getOrganizations() {
        try {
            QueryWrapper<com.management.accountant.oracle.entity.budget.BudgetOrganization> wrapper = new QueryWrapper<>();
            wrapper.eq("DEL_FLAG", 0);
            // 只查询未删除的组织，不限制IS_ENABLED
            wrapper.orderByAsc("SORT_ORDER");
            List<com.management.accountant.oracle.entity.budget.BudgetOrganization> orgList = organizationMapper.selectList(wrapper);

            List<Map<String, Object>> organizations = new ArrayList<>();
            for (com.management.accountant.oracle.entity.budget.BudgetOrganization org : orgList) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", org.getOrganizationId());
                item.put("code", org.getOrganizationCode());
                item.put("name", org.getOrganizationName());
                item.put("parentId", org.getParentId());
                organizations.add(item);
            }
            log.info("查询到组织列表数量: {}", organizations.size());
            return organizations;
        } catch (Exception e) {
            log.error("查询组织列表失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Object getBudgetAccounts() {
        try {
            // 从 TBL_BUDGET_ACCOUNT 表查询所有启用状态的科目
            QueryWrapper<BudgetAccount> wrapper = new QueryWrapper<>();
            wrapper.eq("DEL_FLAG", 0);
            // 只查询未删除的科目，不限制IS_ENABLED
            wrapper.orderByAsc("SORT_ORDER", "ACCOUNT_CODE");
            List<BudgetAccount> accountList = accountMapper.selectList(wrapper);

            List<Map<String, Object>> accounts = new ArrayList<>();
            for (BudgetAccount acc : accountList) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", acc.getAccountId());
                item.put("code", acc.getAccountCode());
                item.put("name", acc.getAccountName());
                item.put("label", acc.getAccountCode() + " - " + acc.getAccountName());
                item.put("type", acc.getAccountType());
                item.put("parentId", acc.getParentId());
                accounts.add(item);
            }
            log.info("查询到科目列表数量: {}", accounts.size());
            return accounts;
        } catch (Exception e) {
            log.error("查询预算科目列表失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Object getUsers() {
        try {
            // 从 TBL_STAFF 表查询启用状态的员工列表（STATUS=1）
            List<TblStaffOracle> staffList = staffMapper.selectAllActiveStaff();
            List<Map<String, Object>> users = new ArrayList<>();
            for (TblStaffOracle staff : staffList) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", staff.getStaffId());
                item.put("name", staff.getRealName());
                users.add(item);
            }
            return users;
        } catch (Exception e) {
            log.error("查询用户列表失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public void updateReason(Map<String, Object> params) {
        String id = (String) params.get("id");
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("差异分析ID不能为空");
        }
        // 前端字段: varianceReason / improvementMeasures / responsiblePerson
        String varianceReason = params.get("varianceReason") != null ? params.get("varianceReason").toString() : null;
        String improvementMeasures = params.get("improvementMeasures") != null ? params.get("improvementMeasures").toString() : null;
        // responsiblePerson 存到 reviewed_by 字段
        String responsiblePerson = params.get("responsiblePerson") != null ? params.get("responsiblePerson").toString() : null;

        int rows = varianceMapper.updateReasonById(id, varianceReason, improvementMeasures, responsiblePerson);
        if (rows <= 0) {
            throw new ServiceException("更新失败，记录不存在或已删除");
        }
        log.info("更新差异分析原因成功，ID: {}", id);
    }

    @Override
    public List<BudgetVarianceAnalysis> exportReport(Map<String, Object> params) {
        // 优先按ids列表导出选中行
        Object idsObj = params.get("ids");
        if (idsObj instanceof List && !((List<?>) idsObj).isEmpty()) {
            List<?> ids = (List<?>) idsObj;
            QueryWrapper<BudgetVarianceAnalysis> wrapper = new QueryWrapper<>();
            wrapper.in("ID", ids).eq("DEL_FLAG", 0);
            return varianceMapper.selectList(wrapper);
        }
        // 否则按查询条件导出（不分页）
        Map<String, Object> queryParams = new HashMap<>();
        Object orgPath = params.get("organizationPath");
        if (orgPath == null) orgPath = params.get("organizationId");
        if (orgPath != null && !orgPath.toString().isEmpty()) {
            queryParams.put("organizationId", orgPath.toString());
        }
        Object acct = params.get("budgetAccount");
        if (acct == null) acct = params.get("accountId");
        if (acct != null && !acct.toString().isEmpty()) {
            queryParams.put("accountId", acct.toString());
        }
        Object vType = params.get("varianceType");
        if (vType != null && !vType.toString().isEmpty()) {
            queryParams.put("varianceType", vType.toString());
        }
        Object dateRange = params.get("analysisDateRange");
        if (dateRange instanceof List) {
            List<?> range = (List<?>) dateRange;
            if (range.size() >= 2) {
                queryParams.put("startDate", range.get(0) != null ? range.get(0).toString().substring(0, 10) : null);
                queryParams.put("endDate", range.get(1) != null ? range.get(1).toString().substring(0, 10) : null);
            }
        }
        return varianceMapper.selectByPage(queryParams);
    }

    @Override
    public List<BudgetVarianceAnalysis> exportSingle(String varianceId) {
        if (!StringUtils.hasText(varianceId)) {
            throw new ServiceException("差异分析ID不能为空");
        }
        QueryWrapper<BudgetVarianceAnalysis> wrapper = new QueryWrapper<>();
        wrapper.eq("ID", varianceId).eq("DEL_FLAG", 0);
        BudgetVarianceAnalysis item = varianceMapper.selectOne(wrapper);
        return item != null ? Collections.singletonList(item) : new ArrayList<>();
    }

    @Override
    public Map<String, Object> getVarianceStats() {
        // 从数据库统计真实差异数据：totalVariance/positiveVariance/negativeVariance/varianceRate
        Map<String, Object> stats = new HashMap<>();
        try {
            Map<String, Object> dbStats = varianceMapper.selectStats();
            if (dbStats != null) {
                // 前端期望字段: totalVariance / positiveVariance / negativeVariance / varianceRate
                stats.put("totalVariance",    getDecimalValue(dbStats, "TOTAL_VARIANCE"));
                stats.put("positiveVariance", getDecimalValue(dbStats, "POSITIVE_VARIANCE"));
                stats.put("negativeVariance", getDecimalValue(dbStats, "NEGATIVE_VARIANCE"));
                stats.put("varianceRate",     getDecimalValue(dbStats, "VARIANCE_RATE"));
                stats.put("totalCount",       getIntValue(dbStats, "TOTAL_COUNT"));
                stats.put("totalBudget",      getDecimalValue(dbStats, "TOTAL_BUDGET"));
            } else {
                stats.put("totalVariance", BigDecimal.ZERO);
                stats.put("positiveVariance", BigDecimal.ZERO);
                stats.put("negativeVariance", BigDecimal.ZERO);
                stats.put("varianceRate", BigDecimal.ZERO);
                stats.put("totalCount", 0);
                stats.put("totalBudget", BigDecimal.ZERO);
            }
        } catch (Exception e) {
            log.error("获取差异分析统计数据失败", e);
            stats.put("totalVariance", BigDecimal.ZERO);
            stats.put("positiveVariance", BigDecimal.ZERO);
            stats.put("negativeVariance", BigDecimal.ZERO);
            stats.put("varianceRate", BigDecimal.ZERO);
            stats.put("totalCount", 0);
            stats.put("totalBudget", BigDecimal.ZERO);
        }
        return stats;
    }

    @Override
    public Map<String, Object> getVarianceChartData(Map<String, Object> params) {
        Map<String, Object> chartData = new HashMap<>();
        try {
            // 判断是否是对比分析模式（前端传 type='compare' 和 id）
            String type = params != null && params.get("type") != null ? params.get("type").toString() : "";
            String id   = params != null && params.get("id") != null ? params.get("id").toString() : null;

            if ("compare".equals(type) && StringUtils.hasText(id)) {
                // 对比分析：按期间汇总单个记录的历史数据
                List<Map<String, Object>> compareRows = varianceMapper.selectCompareData(id);
                List<Map<String, Object>> items = new ArrayList<>();
                for (Map<String, Object> row : compareRows) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("periodName",     row.get("PERIOD_NAME"));
                    item.put("budgetAmount",   getDecimalValue(row, "BUDGET_AMOUNT"));
                    item.put("actualAmount",   getDecimalValue(row, "ACTUAL_AMOUNT"));
                    item.put("varianceAmount", getDecimalValue(row, "VARIANCE_AMOUNT"));
                    items.add(item);
                }
                chartData.put("items", items);
                return chartData;
            }

            // 标准图表：差异趋势（折线图）+ 差异分布（饼图）
            String trendChartType = params != null && params.get("trendChartType") != null
                    ? params.get("trendChartType").toString() : "monthly";

            List<Map<String, Object>> trendRows;
            String labelKey;
            if ("quarterly".equals(trendChartType)) {
                trendRows = varianceMapper.selectQuarterlyChartData();
                labelKey = "QUARTER_LABEL";
            } else if ("yearly".equals(trendChartType)) {
                trendRows = varianceMapper.selectYearlyChartData();
                labelKey = "YEAR_LABEL";
            } else {
                trendRows = varianceMapper.selectMonthlyChartData();
                labelKey = "MONTH_LABEL";
            }

            List<String> xAxis = new ArrayList<>();
            List<Object> positiveData = new ArrayList<>();
            List<Object> negativeData = new ArrayList<>();
            List<Object> rateData = new ArrayList<>();
            for (Map<String, Object> row : trendRows) {
                xAxis.add(row.get(labelKey) != null ? row.get(labelKey).toString() : "");
                positiveData.add(getDecimalValue(row, "POSITIVE_AMOUNT"));
                negativeData.add(getDecimalValue(row, "NEGATIVE_AMOUNT"));
                rateData.add(getDecimalValue(row, "RATE"));
            }

            Map<String, Object> trendChart = new HashMap<>();
            trendChart.put("xAxis", xAxis);
            trendChart.put("series", Arrays.asList(
                buildSeriesItem("正差异", positiveData),
                buildSeriesItem("负差异", negativeData),
                buildSeriesItem("差异率", rateData)
            ));
            chartData.put("trendChart", trendChart);

            // 饼图分布数据：按 distributionChartType 分维度查询
            String distributionChartType = params != null && params.get("distributionChartType") != null
                    ? params.get("distributionChartType").toString() : "department";

            List<Map<String, Object>> distRows;
            if ("account".equals(distributionChartType)) {
                distRows = varianceMapper.selectDistributionByAccount();
            } else if ("project".equals(distributionChartType)) {
                distRows = varianceMapper.selectDistributionByProject();
            } else {
                // 默认按部门
                distRows = varianceMapper.selectDistributionByDept();
            }

            List<Map<String, Object>> distributionChart = new ArrayList<>();
            for (Map<String, Object> row : distRows) {
                Map<String, Object> item = new HashMap<>();
                item.put("name",  row.get("DIST_NAME") != null ? row.get("DIST_NAME").toString() : "未知");
                item.put("value", getDecimalValue(row, "DIST_VALUE"));
                distributionChart.add(item);
            }
            chartData.put("distributionChart", distributionChart);

        } catch (Exception e) {
            log.error("获取差异分析图表数据失败", e);
            chartData.put("trendChart", new HashMap<>());
            chartData.put("distributionChart", new ArrayList<>());
        }
        return chartData;
    }

    // ==================== 私有工具方法 ====================

    private BigDecimal getDecimalValue(Map<String, Object> map, String key) {
        if (map == null || map.get(key) == null) return BigDecimal.ZERO;
        Object val = map.get(key);
        if (val instanceof BigDecimal) return (BigDecimal) val;
        try { return new BigDecimal(val.toString()); } catch (Exception e) { return BigDecimal.ZERO; }
    }

    private int getIntValue(Map<String, Object> map, String key) {
        if (map == null || map.get(key) == null) return 0;
        Object val = map.get(key);
        if (val instanceof Number) return ((Number) val).intValue();
        try { return Integer.parseInt(val.toString()); } catch (Exception e) { return 0; }
    }

    private Map<String, Object> buildSeriesItem(String name, List<Object> data) {
        Map<String, Object> series = new HashMap<>();
        series.put("name", name);
        series.put("data", data);
        return series;
    }
}

