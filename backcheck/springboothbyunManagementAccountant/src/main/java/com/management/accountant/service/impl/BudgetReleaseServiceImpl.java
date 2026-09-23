package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.budget.BudgetRelease;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.budget.BudgetReleaseMapper;
import com.management.accountant.service.BudgetReleaseService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预算释放Service实现类
 * 
 * @description 预算释放业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetReleaseServiceImpl implements BudgetReleaseService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetReleaseMapper releaseMapper;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetRelease create(BudgetRelease release) {
        // 1. 参数校验
        if (release == null) {
            throw new ServiceException("释放信息不能为空");
        }
        if (!StringUtils.hasText(release.getReleaseTitle()) && !StringUtils.hasText(release.getReleaseName())) {
            throw new ServiceException("释放标题不能为空");
        }
        if (release.getReleaseAmount() == null || release.getReleaseAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("释放金额必须大于0");
        }

        // 2. 生成释放编码
        if (!StringUtils.hasText(release.getReleaseCode())) {
            release.setReleaseCode(generateReleaseCode());
        }

        // 3. 设置默认值
        if (release.getDelFlag() == null) {
            release.setDelFlag(0);
        }
        if (!StringUtils.hasText(release.getReleaseStatus())) {
            release.setReleaseStatus("PENDING");
        }
        release.setCreateTime(new Date());
        release.setUpdateTime(new Date());

        // 4. 插入数据库
        int result = releaseMapper.insert(release);
        if (result <= 0) {
            throw new ServiceException("创建释放申请失败");
        }

        log.info("创建释放申请成功，ID: {}", release.getReleaseId());
        return release;
    }

    @Override
    public BudgetRelease getById(String releaseId) {
        if (!StringUtils.hasText(releaseId)) {
            throw new ServiceException("释放ID不能为空");
        }
        
        QueryWrapper<BudgetRelease> wrapper = new QueryWrapper<>();
        wrapper.eq("RELEASE_ID", releaseId)
               .eq("DEL_FLAG", 0);
        
        return releaseMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetRelease release) {
        if (release == null || !StringUtils.hasText(release.getReleaseId())) {
            throw new ServiceException("释放ID不能为空");
        }

        BudgetRelease existing = getById(release.getReleaseId());
        if (existing == null) {
            throw new ServiceException("释放记录不存在");
        }

        if ("APPROVED".equals(existing.getReleaseStatus()) || "EXECUTED".equals(existing.getReleaseStatus())) {
            throw new ServiceException("已审批或已执行的释放申请不能修改");
        }

        release.setUpdateTime(new Date());
        int result = releaseMapper.updateById(release);
        if (result <= 0) {
            throw new ServiceException("更新释放申请失败");
        }

        log.info("更新释放申请成功，ID: {}", release.getReleaseId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String releaseId) {
        if (!StringUtils.hasText(releaseId)) {
            throw new ServiceException("释放ID不能为空");
        }

        BudgetRelease release = getById(releaseId);
        if (release == null) {
            throw new ServiceException("释放记录不存在");
        }

        if ("EXECUTED".equals(release.getReleaseStatus())) {
            throw new ServiceException("已执行的释放申请不能删除");
        }

        BudgetRelease update = new BudgetRelease();
        update.setReleaseId(releaseId);
        update.setDelFlag(1);
        update.setUpdateTime(new Date());

        int result = releaseMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("删除释放申请失败");
        }

        log.info("删除释放申请成功，ID: {}", releaseId);
    }

    @Override
    public PageResult<BudgetRelease> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetRelease> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        if (params.get("releaseCode") != null && StringUtils.hasText(params.get("releaseCode").toString())) {
            wrapper.like("RELEASE_CODE", params.get("releaseCode"));
        }
        // 释放标题模糊查询（前端 queryForm.releaseTitle）
        if (params.get("releaseTitle") != null && StringUtils.hasText(params.get("releaseTitle").toString())) {
            wrapper.and(w -> w.like("RELEASE_TITLE", params.get("releaseTitle"))
                              .or().like("RELEASE_NAME", params.get("releaseTitle")));
        }
        // 组织单元筛选（前端 queryForm.organizationPath，值为 organizationId）
        if (params.get("organizationPath") != null && StringUtils.hasText(params.get("organizationPath").toString())) {
            wrapper.eq("ORGANIZATION_ID", params.get("organizationPath"));
        }
        // 预算科目筛选（前端 queryForm.budgetAccount，值为 accountId）
        if (params.get("budgetAccount") != null && StringUtils.hasText(params.get("budgetAccount").toString())) {
            wrapper.eq("BUDGET_ACCOUNT_ID", params.get("budgetAccount"));
        }
        // 释放类型筛选
        if (params.get("releaseType") != null && StringUtils.hasText(params.get("releaseType").toString())) {
            wrapper.eq("RELEASE_TYPE", params.get("releaseType"));
        }
        if (params.get("budgetId") != null && StringUtils.hasText(params.get("budgetId").toString())) {
            wrapper.eq("BUDGET_ID", params.get("budgetId"));
        }
        if (params.get("releaseStatus") != null && StringUtils.hasText(params.get("releaseStatus").toString())) {
            wrapper.eq("RELEASE_STATUS", params.get("releaseStatus"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetRelease> page = new Page<>(pageNum, pageSize);
        IPage<BudgetRelease> pageResult = releaseMapper.selectPage(page, wrapper);

        // 4. 批量填充展示字段（organizationName / budgetAccountName / approvalStatus / applicant）
        List<BudgetRelease> records = pageResult.getRecords();
        if (!records.isEmpty()) {
            fillDisplayFields(records);
        }

        // 5. 封装返回结果
        PageResult<BudgetRelease> result = new PageResult<>();
        result.setTlist(records);
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    /**
     * 批量填充列表展示字段
     */
    private void fillDisplayFields(List<BudgetRelease> records) {
        // 收集需要查询的 ID 集合
        Set<String> orgIds = new HashSet<>();
        Set<String> accIds = new HashSet<>();
        for (BudgetRelease r : records) {
            if (StringUtils.hasText(r.getOrganizationId())) orgIds.add(r.getOrganizationId());
            if (StringUtils.hasText(r.getBudgetAccountId())) accIds.add(r.getBudgetAccountId());
        }

        // 批量查组织名称
        Map<String, String> orgNameMap = new HashMap<>();
        if (!orgIds.isEmpty()) {
            QueryWrapper<BudgetOrganization> orgWrapper = new QueryWrapper<>();
            orgWrapper.in("ORGANIZATION_ID", orgIds);
            organizationMapper.selectList(orgWrapper).forEach(o ->
                orgNameMap.put(o.getOrganizationId(), o.getOrganizationName()));
        }

        // 批量查科目名称
        Map<String, String> accNameMap = new HashMap<>();
        if (!accIds.isEmpty()) {
            QueryWrapper<BudgetAccount> accWrapper = new QueryWrapper<>();
            accWrapper.in("ACCOUNT_ID", accIds);
            accountMapper.selectList(accWrapper).forEach(a ->
                accNameMap.put(a.getAccountId(), a.getAccountName()));
        }

        // 填充到每条记录
        for (BudgetRelease r : records) {
            r.setOrganizationName(orgNameMap.get(r.getOrganizationId()));
            r.setBudgetAccountName(accNameMap.get(r.getBudgetAccountId()));
            // approvalStatus 与 releaseStatus 保持一致（前端两列都展示状态）
            r.setApprovalStatus(r.getReleaseStatus());
            // applicant 使用 applyBy 字段
            r.setApplicant(r.getApplyBy());
            // releaseTitle 前端展示字段，数据库存在 releaseName 中
            if (!StringUtils.hasText(r.getReleaseTitle())) {
                r.setReleaseTitle(r.getReleaseName());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Map<String, Object> params) {
        String releaseId = (String) params.get("releaseId");
        String approveStatus = (String) params.get("approveStatus"); // APPROVED, REJECTED
        String approveRemark = (String) params.get("approveRemark");

        if (!StringUtils.hasText(releaseId)) {
            throw new ServiceException("释放ID不能为空");
        }

        BudgetRelease release = getById(releaseId);
        if (release == null) {
            throw new ServiceException("释放记录不存在");
        }

        if (!"PENDING".equals(release.getReleaseStatus())) {
            throw new ServiceException("只能审批待审批状态的释放申请");
        }

        BudgetRelease update = new BudgetRelease();
        update.setReleaseId(releaseId);
        update.setReleaseStatus(approveStatus);
        update.setApproveRemark(approveRemark);
        update.setApproveTime(new Date());
        update.setUpdateTime(new Date());

        int result = releaseMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("审批释放申请失败");
        }

        log.info("审批释放申请成功，ID: {}, 结果: {}", releaseId, approveStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(String releaseId) {
        if (!StringUtils.hasText(releaseId)) {
            throw new ServiceException("释放ID不能为空");
        }

        BudgetRelease release = getById(releaseId);
        if (release == null) {
            throw new ServiceException("释放记录不存在");
        }

        if (!"APPROVED".equals(release.getReleaseStatus())) {
            throw new ServiceException("只能执行已审批的释放申请");
        }

        // TODO: 实际的预算释放逻辑
        // 1. 更新预算占用金额
        // 2. 记录释放历史

        BudgetRelease update = new BudgetRelease();
        update.setReleaseId(releaseId);
        update.setReleaseStatus("EXECUTED");
        update.setExecuteTime(new Date());
        update.setUpdateTime(new Date());

        int result = releaseMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("执行释放失败");
        }

        log.info("执行释放成功，ID: {}", releaseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchRelease(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> releaseIds = (List<String>) params.get("releaseIds");

        if (releaseIds == null || releaseIds.isEmpty()) {
            throw new ServiceException("释放ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> failedIds = new ArrayList<>();

        for (String releaseId : releaseIds) {
            try {
                execute(releaseId);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(releaseId);
                log.error("批量释放失败，释放ID: {}", releaseId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", releaseIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量释放完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    public List<BudgetRelease> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetRelease> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 如果传了 releaseIds，按 ID 列表导出（选中数据导出）
        Object releaseIdsObj = params.get("releaseIds");
        if (releaseIdsObj instanceof List && !((List<?>) releaseIdsObj).isEmpty()) {
            wrapper.in("RELEASE_ID", (List<?>) releaseIdsObj);
        } else {
            // 否则按查询条件导出
            if (params.get("releaseTitle") != null && StringUtils.hasText(params.get("releaseTitle").toString())) {
                wrapper.and(w -> w.like("RELEASE_TITLE", params.get("releaseTitle"))
                                  .or().like("RELEASE_NAME", params.get("releaseTitle")));
            }
            if (params.get("releaseType") != null && StringUtils.hasText(params.get("releaseType").toString())) {
                wrapper.eq("RELEASE_TYPE", params.get("releaseType"));
            }
            if (params.get("organizationPath") != null && StringUtils.hasText(params.get("organizationPath").toString())) {
                wrapper.eq("ORGANIZATION_ID", params.get("organizationPath"));
            }
            if (params.get("budgetAccount") != null && StringUtils.hasText(params.get("budgetAccount").toString())) {
                wrapper.eq("BUDGET_ACCOUNT_ID", params.get("budgetAccount"));
            }
            if (params.get("releaseStatus") != null && StringUtils.hasText(params.get("releaseStatus").toString())) {
                wrapper.eq("RELEASE_STATUS", params.get("releaseStatus"));
            }
            // 限制导出数量为当前页大小
            if (params.get("pageSize") != null) {
                int pageSize = Integer.parseInt(params.get("pageSize").toString());
                int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
                wrapper.last("OFFSET " + ((pageNum - 1) * pageSize) + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY");
            }
        }

        wrapper.orderByDesc("CREATE_TIME");

        List<BudgetRelease> records = releaseMapper.selectList(wrapper);
        // 填充展示字段
        if (!records.isEmpty()) {
            fillDisplayFields(records);
        }
        return records;
    }

    /**
     * 生成释放编码
     */
    private String generateReleaseCode() {
        return "REL" + System.currentTimeMillis();
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        try {
            QueryWrapper<BudgetRelease> wrapper = new QueryWrapper<>();
            Integer totalCount = releaseMapper.selectCount(wrapper).intValue();
            statistics.put("totalCount", totalCount);

            QueryWrapper<BudgetRelease> completedWrapper = new QueryWrapper<>();
            completedWrapper.eq("RELEASE_STATUS", "completed");
            Integer completedCount = releaseMapper.selectCount(completedWrapper).intValue();
            statistics.put("completedCount", completedCount);

            QueryWrapper<BudgetRelease> pendingWrapper = new QueryWrapper<>();
            pendingWrapper.eq("RELEASE_STATUS", "pending");
            Integer pendingCount = releaseMapper.selectCount(pendingWrapper).intValue();
            statistics.put("pendingCount", pendingCount);

            statistics.put("processingCount", totalCount - completedCount - pendingCount);
        } catch (Exception e) {
            log.error("获取释放统计信息异常", e);
        }
        return statistics;
    }

    @Override
    public boolean updateById(BudgetRelease release) {
        return releaseMapper.updateById(release) > 0;
    }
}

