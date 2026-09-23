package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetFreeze;
import com.management.accountant.oracle.entity.budget.BudgetFreezeHistory;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.mapper.budget.BudgetFreezeMapper;
import com.management.accountant.oracle.mapper.budget.BudgetFreezeHistoryMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.service.BudgetFreezeService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 预算冻结Service实现类
 * 
 * @description 预算冻结业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetFreezeServiceImpl implements BudgetFreezeService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetFreezeMapper freezeMapper;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Resource
    private BudgetFreezeHistoryMapper historyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetFreeze create(BudgetFreeze freeze) {
        if (freeze == null) {
            throw new ServiceException("冻结信息不能为空");
        }
        if (!StringUtils.hasText(freeze.getFreezeTitle())) {
            throw new ServiceException("冻结标题不能为空");
        }
        if (!StringUtils.hasText(freeze.getFreezeType())) {
            throw new ServiceException("冻结类型不能为空");
        }
        if (freeze.getFreezeAmount() == null || freeze.getFreezeAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("冻结金额必须大于0");
        }

        // 生成冻结编码
        if (!StringUtils.hasText(freeze.getFreezeCode())) {
            freeze.setFreezeCode(generateFreezeCode());
        }

        // 检查编码唯一性
        QueryWrapper<BudgetFreeze> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("FREEZE_CODE", freeze.getFreezeCode()).eq("DEL_FLAG", 0);
        if (freezeMapper.selectCount(checkWrapper) > 0) {
            throw new ServiceException("冻结编码已存在");
        }

        // 设置默认值
        freeze.setDelFlag(0);
        if (!StringUtils.hasText(freeze.getFreezeStatus())) {
            freeze.setFreezeStatus("FROZEN");
        }
        if (!StringUtils.hasText(freeze.getApprovalStatus())) {
            freeze.setApprovalStatus("PENDING");
        }
        if (freeze.getRemainAmount() == null) {
            freeze.setRemainAmount(freeze.getFreezeAmount());
        }
        if (freeze.getReleasedAmount() == null) {
            freeze.setReleasedAmount(BigDecimal.ZERO);
        }
        freeze.setFreezeTime(new Date());
        freeze.setCreateTime(new Date());
        freeze.setUpdateTime(new Date());

        // 填充组织名称
        if (StringUtils.hasText(freeze.getOrganizationId()) && !StringUtils.hasText(freeze.getOrganizationName())) {
            try {
                QueryWrapper<BudgetOrganization> orgWrapper = new QueryWrapper<>();
                orgWrapper.eq("ORGANIZATION_ID", freeze.getOrganizationId()).eq("DEL_FLAG", 0);
                BudgetOrganization org = organizationMapper.selectOne(orgWrapper);
                if (org != null && StringUtils.hasText(org.getOrganizationName())) {
                    freeze.setOrganizationName(org.getOrganizationName());
                }
            } catch (Exception e) {
                log.warn("查询组织名称失败, organizationId={}", freeze.getOrganizationId(), e);
            }
        }

        // 填充预算科目名称
        if (StringUtils.hasText(freeze.getBudgetAccountId()) && !StringUtils.hasText(freeze.getBudgetAccountName())) {
            try {
                QueryWrapper<BudgetAccount> acctWrapper = new QueryWrapper<>();
                acctWrapper.eq("ACCOUNT_ID", freeze.getBudgetAccountId()).eq("DEL_FLAG", 0);
                BudgetAccount acct = accountMapper.selectOne(acctWrapper);
                if (acct != null && StringUtils.hasText(acct.getAccountName())) {
                    freeze.setBudgetAccountName(acct.getAccountName());
                }
            } catch (Exception e) {
                log.warn("查询预算科目名称失败, budgetAccountId={}", freeze.getBudgetAccountId(), e);
            }
        }

        // 设置冻结日期
        if (freeze.getFreezeDate() == null) {
            freeze.setFreezeDate(new Date());
        }

        int result = freezeMapper.insert(freeze);
        if (result <= 0) {
            throw new ServiceException("创建预算冻结失败");
        }

        log.info("创建预算冻结成功，ID: {}", freeze.getFreezeId());
        recordHistory(freeze.getFreezeId(), "CREATE", "创建预算冻结：" + freeze.getFreezeTitle(),
                freeze.getFreezeAmount(), null, freeze.getFreezeStatus());
        return freeze;
    }

    @Override
    public BudgetFreeze getById(String freezeId) {
        if (!StringUtils.hasText(freezeId)) {
            throw new ServiceException("冻结ID不能为空");
        }
        
        QueryWrapper<BudgetFreeze> wrapper = new QueryWrapper<>();
        wrapper.eq("FREEZE_ID", freezeId)
               .eq("DEL_FLAG", 0);
        
        return freezeMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetFreeze freeze) {
        if (freeze == null || !StringUtils.hasText(freeze.getFreezeId())) {
            throw new ServiceException("冻结ID不能为空");
        }

        BudgetFreeze existing = getById(freeze.getFreezeId());
        if (existing == null) {
            throw new ServiceException("预算冻结不存在");
        }

        // 如果修改了编码，检查唯一性
        if (StringUtils.hasText(freeze.getFreezeCode()) && !freeze.getFreezeCode().equals(existing.getFreezeCode())) {
            QueryWrapper<BudgetFreeze> checkWrapper = new QueryWrapper<>();
            checkWrapper.eq("FREEZE_CODE", freeze.getFreezeCode())
                       .ne("FREEZE_ID", freeze.getFreezeId())
                       .eq("DEL_FLAG", 0);
            if (freezeMapper.selectCount(checkWrapper) > 0) {
                throw new ServiceException("冻结编码已存在");
            }
        }

        freeze.setUpdateTime(new Date());
        int result = freezeMapper.updateById(freeze);
        if (result <= 0) {
            throw new ServiceException("更新预算冻结失败");
        }

        log.info("更新预算冻结成功，ID: {}", freeze.getFreezeId());
        recordHistory(freeze.getFreezeId(), "UPDATE", "更新预算冻结",
                freeze.getFreezeAmount(), null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String freezeId) {
        if (!StringUtils.hasText(freezeId)) {
            throw new ServiceException("冻结ID不能为空");
        }

        BudgetFreeze freeze = getById(freezeId);
        if (freeze == null) {
            throw new ServiceException("预算冻结不存在");
        }

        // 检查是否已释放，已释放不允许删除
        if ("RELEASED".equals(freeze.getFreezeStatus())) {
            throw new ServiceException("冻结已释放，无法删除");
        }

        BudgetFreeze update = new BudgetFreeze();
        update.setFreezeId(freezeId);
        update.setDelFlag(1);
        update.setUpdateTime(new Date());

        int result = freezeMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("删除预算冻结失败");
        }

        log.info("删除预算冻结成功，ID: {}", freezeId);
    }

    @Override
    public PageResult<BudgetFreeze> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetFreeze> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 冻结标题（前端 freezeTitle）
        if (StringUtils.hasText(getStr(params, "freezeTitle"))) {
            wrapper.like("FREEZE_TITLE", getStr(params, "freezeTitle"));
        }
        // 冻结类型
        if (StringUtils.hasText(getStr(params, "freezeType"))) {
            wrapper.eq("FREEZE_TYPE", getStr(params, "freezeType"));
        }
        // 组织单元（前端 organizationPath）
        if (StringUtils.hasText(getStr(params, "organizationPath"))) {
            wrapper.eq("ORGANIZATION_ID", getStr(params, "organizationPath"));
        }
        // 预算科目（前端 budgetAccount）
        if (StringUtils.hasText(getStr(params, "budgetAccount"))) {
            wrapper.eq("BUDGET_ACCOUNT_ID", getStr(params, "budgetAccount"));
        }
        // 冻结状态
        if (StringUtils.hasText(getStr(params, "freezeStatus"))) {
            wrapper.eq("FREEZE_STATUS", getStr(params, "freezeStatus"));
        }
        // 冻结编码
        if (StringUtils.hasText(getStr(params, "freezeCode"))) {
            wrapper.like("FREEZE_CODE", getStr(params, "freezeCode"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        Page<BudgetFreeze> page = new Page<>(pageNum, pageSize);
        IPage<BudgetFreeze> pageResult = freezeMapper.selectPage(page, wrapper);

        PageResult<BudgetFreeze> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    /** 安全取字符串 */
    private String getStr(Map<String, Object> params, String key) {
        Object v = params.get(key);
        return v != null ? v.toString() : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeFreeze(String freezeId) {
        if (!StringUtils.hasText(freezeId)) {
            throw new ServiceException("冻结ID不能为空");
        }

        BudgetFreeze freeze = getById(freezeId);
        if (freeze == null) {
            throw new ServiceException("预算冻结不存在");
        }

        if ("FROZEN".equals(freeze.getFreezeStatus())) {
            throw new ServiceException("预算已冻结");
        }

        // TODO: 实际的冻结逻辑，更新预算可用金额等

        BudgetFreeze update = new BudgetFreeze();
        update.setFreezeId(freezeId);
        update.setFreezeStatus("FROZEN");
        update.setFreezeDate(new Date());
        update.setUpdateTime(new Date());

        int result = freezeMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("执行冻结失败");
        }

        log.info("执行冻结成功，ID: {}", freezeId);
        recordHistory(freezeId, "FREEZE", "执行冻结操作", freeze.getFreezeAmount(),
                freeze.getFreezeStatus(), "FROZEN");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfreeze(String freezeId) {
        if (!StringUtils.hasText(freezeId)) {
            throw new ServiceException("冻结ID不能为空");
        }

        BudgetFreeze freeze = getById(freezeId);
        if (freeze == null) {
            throw new ServiceException("预算冻结不存在");
        }

        if ("RELEASED".equals(freeze.getFreezeStatus())) {
            throw new ServiceException("预算已释放");
        }

        BudgetFreeze update = new BudgetFreeze();
        update.setFreezeId(freezeId);
        update.setFreezeStatus("RELEASED");
        update.setReleaseTime(new Date());
        update.setUpdateTime(new Date());

        int result = freezeMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("解冻失败");
        }

        log.info("解冻成功，ID: {}", freezeId);
        recordHistory(freezeId, "UNFREEZE", "解冻操作", freeze.getFreezeAmount(),
                freeze.getFreezeStatus(), "RELEASED");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchFreeze(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> freezeIds = (List<String>) params.get("freezeIds");
        
        if (freezeIds == null || freezeIds.isEmpty()) {
            throw new ServiceException("冻结ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> failedIds = new ArrayList<>();

        for (String freezeId : freezeIds) {
            try {
                executeFreeze(freezeId);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(freezeId);
                log.error("批量冻结失败，ID: {}", freezeId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", freezeIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量冻结完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchUnfreeze(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> freezeIds = (List<String>) params.get("freezeIds");
        
        if (freezeIds == null || freezeIds.isEmpty()) {
            throw new ServiceException("冻结ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> failedIds = new ArrayList<>();

        for (String freezeId : freezeIds) {
            try {
                unfreeze(freezeId);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(freezeId);
                log.error("批量解冻失败，ID: {}", freezeId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", freezeIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量解冻完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    public List<BudgetFreeze> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetFreeze> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        if (StringUtils.hasText(getStr(params, "freezeStatus"))) {
            wrapper.eq("FREEZE_STATUS", getStr(params, "freezeStatus"));
        }
        if (StringUtils.hasText(getStr(params, "freezeType"))) {
            wrapper.eq("FREEZE_TYPE", getStr(params, "freezeType"));
        }
        if (StringUtils.hasText(getStr(params, "organizationPath"))) {
            wrapper.eq("ORGANIZATION_ID", getStr(params, "organizationPath"));
        }
        if (StringUtils.hasText(getStr(params, "budgetAccount"))) {
            wrapper.eq("BUDGET_ACCOUNT_ID", getStr(params, "budgetAccount"));
        }

        wrapper.orderByDesc("CREATE_TIME");
        return freezeMapper.selectList(wrapper);
    }

    /**
     * 生成冻结编码
     */
    private String generateFreezeCode() {
        return "FRZ" + System.currentTimeMillis();
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        try {
            // 总数（未删除）
            QueryWrapper<BudgetFreeze> totalWrapper = new QueryWrapper<>();
            totalWrapper.eq("DEL_FLAG", 0);
            Integer totalCount = freezeMapper.selectCount(totalWrapper).intValue();
            statistics.put("totalCount", totalCount);

            // 冻结中
            QueryWrapper<BudgetFreeze> frozenWrapper = new QueryWrapper<>();
            frozenWrapper.eq("DEL_FLAG", 0).eq("FREEZE_STATUS", "FROZEN");
            Integer activeCount = freezeMapper.selectCount(frozenWrapper).intValue();
            statistics.put("activeCount", activeCount);

            // 已释放
            QueryWrapper<BudgetFreeze> releasedWrapper = new QueryWrapper<>();
            releasedWrapper.eq("DEL_FLAG", 0).eq("FREEZE_STATUS", "RELEASED");
            Integer releasedCount = freezeMapper.selectCount(releasedWrapper).intValue();
            statistics.put("releasedCount", releasedCount);

            // 待审批
            QueryWrapper<BudgetFreeze> pendingWrapper = new QueryWrapper<>();
            pendingWrapper.eq("DEL_FLAG", 0).eq("APPROVAL_STATUS", "PENDING");
            Integer pendingCount = freezeMapper.selectCount(pendingWrapper).intValue();
            statistics.put("pendingCount", pendingCount);
        } catch (Exception e) {
            log.error("获取冻结统计信息异常", e);
        }
        return statistics;
    }

    @Override
    public List<BudgetFreezeHistory> getHistory(String freezeId) {
        if (!StringUtils.hasText(freezeId)) {
            throw new ServiceException("冻结ID不能为空");
        }
        return historyMapper.selectByFreezeId(freezeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void extendFreeze(String freezeId, Date newPlannedUnfreezeDate, String reason) {
        if (!StringUtils.hasText(freezeId)) {
            throw new ServiceException("冻结ID不能为空");
        }
        if (newPlannedUnfreezeDate == null) {
            throw new ServiceException("新的计划解冻日期不能为空");
        }

        BudgetFreeze freeze = getById(freezeId);
        if (freeze == null) {
            throw new ServiceException("预算冻结不存在");
        }
        if (!"FROZEN".equals(freeze.getFreezeStatus())) {
            throw new ServiceException("只有冻结中的记录才能延期");
        }

        String beforeValue = freeze.getPlannedUnfreezeDate() != null
                ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(freeze.getPlannedUnfreezeDate())
                : "无";
        String afterValue = new java.text.SimpleDateFormat("yyyy-MM-dd").format(newPlannedUnfreezeDate);

        // 更新计划解冻日期
        BudgetFreeze update = new BudgetFreeze();
        update.setFreezeId(freezeId);
        update.setPlannedUnfreezeDate(newPlannedUnfreezeDate);
        update.setUpdateTime(new Date());
        int result = freezeMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("延期冻结失败");
        }

        // 记录历史
        String desc = "延期冻结，原因：" + (StringUtils.hasText(reason) ? reason : "未填写");
        recordHistory(freezeId, "EXTEND", desc, freeze.getFreezeAmount(), beforeValue, afterValue);

        log.info("延期冻结成功，ID: {}，新日期: {}", freezeId, afterValue);
    }

    /**
     * 记录冻结操作历史
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importData(List<Map<String, Object>> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw new ServiceException("导入数据为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> failMessages = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> row = dataList.get(i);
            int rowNum = i + 2; // Excel行号（第1行是表头）
            try {
                BudgetFreeze freeze = new BudgetFreeze();

                // 按列索引映射（与导出列顺序一致）
                freeze.setFreezeCode(getStringValue(row, 0));
                freeze.setFreezeTitle(getStringValue(row, 1));
                freeze.setFreezeType(getStringValue(row, 2));
                freeze.setOrganizationName(getStringValue(row, 3));
                freeze.setBudgetAccountName(getStringValue(row, 4));

                String amountStr = getStringValue(row, 5);
                if (StringUtils.hasText(amountStr)) {
                    freeze.setFreezeAmount(new BigDecimal(amountStr.replace(",", "")));
                }

                freeze.setFreezeReason(getStringValue(row, 6));
                freeze.setFreezeStatus(getStringValue(row, 7));
                freeze.setApprovalStatus(getStringValue(row, 8));
                freeze.setApplicant(getStringValue(row, 9));

                // 日期字段
                String applyDateStr = getStringValue(row, 10);
                if (StringUtils.hasText(applyDateStr)) {
                    freeze.setApplyDate(parseDate(applyDateStr));
                }
                String freezeDateStr = getStringValue(row, 11);
                if (StringUtils.hasText(freezeDateStr)) {
                    freeze.setFreezeDate(parseDate(freezeDateStr));
                }

                // 必填校验
                if (!StringUtils.hasText(freeze.getFreezeCode())) {
                    throw new ServiceException("冻结编码不能为空");
                }
                if (!StringUtils.hasText(freeze.getFreezeTitle())) {
                    throw new ServiceException("冻结标题不能为空");
                }

                // 设置默认值
                if (!StringUtils.hasText(freeze.getFreezeStatus())) {
                    freeze.setFreezeStatus("FROZEN");
                }
                if (!StringUtils.hasText(freeze.getApprovalStatus())) {
                    freeze.setApprovalStatus("PENDING");
                }
                if (freeze.getFreezeDate() == null) {
                    freeze.setFreezeDate(new Date());
                }
                if (freeze.getApplyDate() == null) {
                    freeze.setApplyDate(new Date());
                }
                freeze.setDelFlag(0);
                freeze.setCreateTime(new Date());
                freeze.setUpdateTime(new Date());

                // 根据组织名称反查 ID
                if (StringUtils.hasText(freeze.getOrganizationName()) &&
                        !StringUtils.hasText(freeze.getOrganizationId())) {
                    QueryWrapper<BudgetOrganization> orgQw = new QueryWrapper<>();
                    orgQw.eq("ORGANIZATION_NAME", freeze.getOrganizationName());
                    orgQw.last("AND ROWNUM = 1");
                    BudgetOrganization org = organizationMapper.selectOne(orgQw);
                    if (org != null) {
                        freeze.setOrganizationId(org.getOrganizationId());
                    }
                }

                // 根据科目名称反查 ID
                if (StringUtils.hasText(freeze.getBudgetAccountName()) &&
                        !StringUtils.hasText(freeze.getBudgetAccountId())) {
                    QueryWrapper<BudgetAccount> accQw = new QueryWrapper<>();
                    accQw.eq("ACCOUNT_NAME", freeze.getBudgetAccountName());
                    accQw.last("AND ROWNUM = 1");
                    BudgetAccount acc = accountMapper.selectOne(accQw);
                    if (acc != null) {
                        freeze.setBudgetAccountId(acc.getAccountId());
                    }
                }

                freezeMapper.insert(freeze);
                successCount++;

                // 记录历史
                recordHistory(freeze.getFreezeId(), "IMPORT", "Excel导入创建",
                        freeze.getFreezeAmount(), null, freeze.getFreezeStatus());

            } catch (Exception e) {
                failCount++;
                failMessages.add("第" + rowNum + "行：" + e.getMessage());
                log.warn("导入第{}行失败: {}", rowNum, e.getMessage());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failMessages", failMessages);
        log.info("导入预算冻结完成，成功: {}，失败: {}", successCount, failCount);
        return result;
    }

    /** 从行数据中按列索引取字符串值 */
    private String getStringValue(Map<String, Object> row, int index) {
        Object val = row.get(index);
        if (val == null) val = row.get(String.valueOf(index));
        return val != null ? val.toString().trim() : null;
    }

    /** 简易日期解析，支持 yyyy-MM-dd 和 yyyy/MM/dd */
    private Date parseDate(String dateStr) {
        try {
            java.text.SimpleDateFormat sdf;
            if (dateStr.contains("/")) {
                sdf = new java.text.SimpleDateFormat("yyyy/MM/dd");
            } else {
                sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            }
            return sdf.parse(dateStr);
        } catch (Exception e) {
            log.warn("日期解析失败: {}", dateStr);
            return null;
        }
    }

    private void recordHistory(String freezeId, String operationType, String operationDesc,
                               BigDecimal amount, String beforeValue, String afterValue) {
        try {
            BudgetFreezeHistory history = new BudgetFreezeHistory();
            history.setFreezeId(freezeId);
            history.setOperationType(operationType);
            history.setOperationDesc(operationDesc);
            history.setAmount(amount);
            history.setBeforeValue(beforeValue);
            history.setAfterValue(afterValue);
            history.setOperator("system");
            history.setOperateTime(new Date());
            history.setCreateTime(new Date());
            historyMapper.insert(history);
        } catch (Exception e) {
            log.error("记录冻结历史失败, freezeId={}, type={}", freezeId, operationType, e);
        }
    }
}
