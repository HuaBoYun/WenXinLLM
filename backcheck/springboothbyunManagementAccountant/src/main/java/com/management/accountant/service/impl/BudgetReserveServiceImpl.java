package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.budget.BudgetReserve;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.budget.BudgetReserveMapper;
import com.management.accountant.service.BudgetReserveService;
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
 * 预算保留Service实现类
 * 
 * @description 预算保留业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetReserveServiceImpl implements BudgetReserveService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetReserveMapper reserveMapper;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetReserve create(BudgetReserve reserve) {
        if (reserve == null) {
            throw new ServiceException("保留信息不能为空");
        }
        if (!StringUtils.hasText(reserve.getBudgetId())) {
            reserve.setBudgetId("DEFAULT");
        }
        if (reserve.getReserveAmount() == null || reserve.getReserveAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("保留金额必须大于0");
        }

        if (!StringUtils.hasText(reserve.getReserveCode())) {
            reserve.setReserveCode(generateReserveCode());
        }

        if (reserve.getDelFlag() == null) {
            reserve.setDelFlag(0);
        }
        if (!StringUtils.hasText(reserve.getReserveStatus())) {
            reserve.setReserveStatus("PENDING");
        }
        if (reserve.getAvailableAmount() == null) {
            reserve.setAvailableAmount(reserve.getReserveAmount());
        }
        if (reserve.getUsedAmount() == null) {
            reserve.setUsedAmount(BigDecimal.ZERO);
        }
        reserve.setCreateTime(new Date());
        reserve.setUpdateTime(new Date());

        int result = reserveMapper.insert(reserve);
        if (result <= 0) {
            throw new ServiceException("创建保留申请失败");
        }

        log.info("创建保留申请成功，ID: {}", reserve.getReserveId());
        return reserve;
    }

    @Override
    public BudgetReserve getById(String reserveId) {
        if (!StringUtils.hasText(reserveId)) {
            throw new ServiceException("保留ID不能为空");
        }
        
        QueryWrapper<BudgetReserve> wrapper = new QueryWrapper<>();
        wrapper.eq("RESERVE_ID", reserveId)
               .eq("DEL_FLAG", 0);
        
        return reserveMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetReserve reserve) {
        if (reserve == null || !StringUtils.hasText(reserve.getReserveId())) {
            throw new ServiceException("保留ID不能为空");
        }

        BudgetReserve existing = getById(reserve.getReserveId());
        if (existing == null) {
            throw new ServiceException("保留记录不存在");
        }

        if ("APPROVED".equals(existing.getReserveStatus()) || "EXECUTED".equals(existing.getReserveStatus())) {
            throw new ServiceException("已审批或已执行的保留申请不能修改");
        }

        reserve.setUpdateTime(new Date());
        int result = reserveMapper.updateById(reserve);
        if (result <= 0) {
            throw new ServiceException("更新保留申请失败");
        }

        log.info("更新保留申请成功，ID: {}", reserve.getReserveId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String reserveId) {
        if (!StringUtils.hasText(reserveId)) {
            throw new ServiceException("保留ID不能为空");
        }

        BudgetReserve reserve = getById(reserveId);
        if (reserve == null) {
            throw new ServiceException("保留记录不存在");
        }

        if ("EXECUTED".equals(reserve.getReserveStatus())) {
            throw new ServiceException("已执行的保留申请不能删除");
        }

        BudgetReserve update = new BudgetReserve();
        update.setReserveId(reserveId);
        update.setDelFlag(1);
        update.setUpdateTime(new Date());

        int result = reserveMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("删除保留申请失败");
        }

        log.info("删除保留申请成功，ID: {}", reserveId);
    }

    @Override
    public PageResult<BudgetReserve> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetReserve> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        if (params.get("reserveName") != null && StringUtils.hasText(params.get("reserveName").toString())) {
            wrapper.like("RESERVE_NAME", params.get("reserveName"));
        }
        if (params.get("reserveType") != null && StringUtils.hasText(params.get("reserveType").toString())) {
            wrapper.eq("RESERVE_TYPE", params.get("reserveType"));
        }
        if (params.get("reserveStatus") != null && StringUtils.hasText(params.get("reserveStatus").toString())) {
            wrapper.eq("RESERVE_STATUS", params.get("reserveStatus"));
        }
        if (params.get("reserveCode") != null && StringUtils.hasText(params.get("reserveCode").toString())) {
            wrapper.like("RESERVE_CODE", params.get("reserveCode"));
        }
        if (params.get("budgetId") != null && StringUtils.hasText(params.get("budgetId").toString())) {
            wrapper.eq("BUDGET_ID", params.get("budgetId"));
        }
        if (params.get("organizationId") != null && StringUtils.hasText(params.get("organizationId").toString())) {
            wrapper.eq("ORGANIZATION_ID", params.get("organizationId"));
        }
        if (params.get("budgetAccountId") != null && StringUtils.hasText(params.get("budgetAccountId").toString())) {
            wrapper.eq("BUDGET_ACCOUNT_ID", params.get("budgetAccountId"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        Page<BudgetReserve> page = new Page<>(pageNum, pageSize);
        IPage<BudgetReserve> pageResult = reserveMapper.selectPage(page, wrapper);

        List<BudgetReserve> records = pageResult.getRecords();

        // 批量填充组织名称和科目名称
        if (records != null && !records.isEmpty()) {
            // 收集所有非空的 organizationId 和 budgetAccountId
            Set<String> orgIds = records.stream()
                    .map(BudgetReserve::getOrganizationId)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toSet());
            Set<String> accIds = records.stream()
                    .map(BudgetReserve::getBudgetAccountId)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toSet());

            // 批量查询组织名称
            Map<String, String> orgNameMap = new HashMap<>();
            if (!orgIds.isEmpty()) {
                QueryWrapper<BudgetOrganization> orgWrapper = new QueryWrapper<>();
                orgWrapper.in("ORGANIZATION_ID", orgIds);
                List<BudgetOrganization> orgList = organizationMapper.selectList(orgWrapper);
                for (BudgetOrganization org : orgList) {
                    orgNameMap.put(org.getOrganizationId(), org.getOrganizationName());
                }
            }

            // 批量查询科目名称
            Map<String, String> accNameMap = new HashMap<>();
            if (!accIds.isEmpty()) {
                QueryWrapper<BudgetAccount> accWrapper = new QueryWrapper<>();
                accWrapper.in("ACCOUNT_ID", accIds);
                List<BudgetAccount> accList = accountMapper.selectList(accWrapper);
                for (BudgetAccount acc : accList) {
                    accNameMap.put(acc.getAccountId(), acc.getAccountName());
                }
            }

            // 填充名称
            for (BudgetReserve r : records) {
                if (StringUtils.hasText(r.getOrganizationId())) {
                    r.setOrganizationName(orgNameMap.get(r.getOrganizationId()));
                }
                if (StringUtils.hasText(r.getBudgetAccountId())) {
                    r.setBudgetAccountName(accNameMap.get(r.getBudgetAccountId()));
                }
            }
        }

        PageResult<BudgetReserve> result = new PageResult<>();
        result.setTlist(records);
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Map<String, Object> params) {
        String reserveId = (String) params.get("reserveId");
        String approveStatus = (String) params.get("approveStatus");
        String approveRemark = (String) params.get("approveRemark");

        if (!StringUtils.hasText(reserveId)) {
            throw new ServiceException("保留ID不能为空");
        }

        BudgetReserve reserve = getById(reserveId);
        if (reserve == null) {
            throw new ServiceException("保留记录不存在");
        }

        if (!"PENDING".equals(reserve.getReserveStatus())) {
            throw new ServiceException("只能审批待审批状态的保留申请");
        }

        BudgetReserve update = new BudgetReserve();
        update.setReserveId(reserveId);
        update.setReserveStatus(approveStatus);
        update.setApproveRemark(approveRemark);
        update.setApproveTime(new Date());
        update.setUpdateTime(new Date());

        int result = reserveMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("审批保留申请失败");
        }

        log.info("审批保留申请成功，ID: {}, 结果: {}", reserveId, approveStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(String reserveId) {
        if (!StringUtils.hasText(reserveId)) {
            throw new ServiceException("保留ID不能为空");
        }

        BudgetReserve reserve = getById(reserveId);
        if (reserve == null) {
            throw new ServiceException("保留记录不存在");
        }

        if (!"APPROVED".equals(reserve.getReserveStatus())) {
            throw new ServiceException("只能执行已审批的保留申请");
        }

        BudgetReserve update = new BudgetReserve();
        update.setReserveId(reserveId);
        update.setReserveStatus("EXECUTED");
        update.setExecuteTime(new Date());
        update.setUpdateTime(new Date());

        int result = reserveMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("执行保留失败");
        }

        log.info("执行保留成功，ID: {}", reserveId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchReserve(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> reserveIds = (List<String>) params.get("reserveIds");

        if (reserveIds == null || reserveIds.isEmpty()) {
            throw new ServiceException("保留ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> failedIds = new ArrayList<>();

        for (String reserveId : reserveIds) {
            try {
                execute(reserveId);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(reserveId);
                log.error("批量保留失败，保留ID: {}", reserveId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", reserveIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量保留完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    public List<BudgetReserve> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetReserve> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        if (params.get("reserveName") != null && StringUtils.hasText(params.get("reserveName").toString())) {
            wrapper.like("RESERVE_NAME", params.get("reserveName").toString());
        }
        if (params.get("reserveType") != null && StringUtils.hasText(params.get("reserveType").toString())) {
            wrapper.eq("RESERVE_TYPE", params.get("reserveType").toString());
        }
        if (params.get("reserveStatus") != null && StringUtils.hasText(params.get("reserveStatus").toString())) {
            wrapper.eq("RESERVE_STATUS", params.get("reserveStatus").toString());
        }

        wrapper.orderByDesc("CREATE_TIME");

        return reserveMapper.selectList(wrapper);
    }

    private String generateReserveCode() {
        return "RSV" + System.currentTimeMillis();
    }

    @Override
    public java.util.List<BudgetReserve> list(com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<BudgetReserve> wrapper) {
        return reserveMapper.selectList(wrapper);
    }

    @Override
    public void updateById(BudgetReserve reserve) {
        reserveMapper.updateById(reserve);
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        try {
            QueryWrapper<BudgetReserve> wrapper = new QueryWrapper<>();
            wrapper.eq("DEL_FLAG", 0);
            List<BudgetReserve> allList = reserveMapper.selectList(wrapper);

            int totalCount = allList.size();
            double reservedAmount = 0, usedAmount = 0, availableAmount = 0;
            for (BudgetReserve r : allList) {
                if (r.getReserveAmount() != null) reservedAmount += r.getReserveAmount().doubleValue();
                if (r.getUsedAmount() != null) usedAmount += r.getUsedAmount().doubleValue();
                if (r.getAvailableAmount() != null) availableAmount += r.getAvailableAmount().doubleValue();
            }

            statistics.put("totalCount", totalCount);
            statistics.put("reservedAmount", String.format("%.2f", reservedAmount));
            statistics.put("usedAmount", String.format("%.2f", usedAmount));
            statistics.put("availableAmount", String.format("%.2f", availableAmount));
            statistics.put("reserveRate", reservedAmount > 0 ? Math.round((reservedAmount / (reservedAmount + availableAmount)) * 100) : 0);
            statistics.put("usageRate", reservedAmount > 0 ? Math.round((usedAmount / reservedAmount) * 100) : 0);
            statistics.put("availableRate", reservedAmount > 0 ? Math.round((availableAmount / reservedAmount) * 100) : 0);
        } catch (Exception e) {
            log.error("获取保留统计信息异常", e);
        }
        return statistics;
    }
}

