package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetAccountMapping;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMappingMapper;
import com.management.accountant.service.BudgetAccountService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BudgetAccountServiceImpl implements BudgetAccountService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetAccountMapper accountMapper;

    @Resource
    private BudgetAccountMappingMapper mappingMapper;

    /**
     * 加载科目映射列表
     *
     * @param accountId 科目ID
     * @return 映射列表
     */
    private List<BudgetAccountMapping> loadAccountMappings(String accountId) {
        if (!StringUtils.hasText(accountId)) {
            return new ArrayList<>();
        }
        return mappingMapper.selectByAccountId(accountId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetAccount create(BudgetAccount account) {
        if (account == null) throw new ServiceException("科目信息不能为空");
        if (!StringUtils.hasText(account.getAccountName())) throw new ServiceException("科目名称不能为空");
        if (StringUtils.hasText(account.getAccountCode())) {
            QueryWrapper<BudgetAccount> w = new QueryWrapper<>();
            w.eq("ACCOUNT_CODE", account.getAccountCode()).eq("DEL_FLAG", 0L);
            if (accountMapper.selectCount(w) > 0) throw new ServiceException("科目编码已存在");
        }
        // 根据 parentId 自动计算级次和路径
        // 提前生成 ID，accountPath 需要用到
        if (!StringUtils.hasText(account.getAccountId())) {
            account.setAccountId(UUID.randomUUID().toString().replace("-", ""));
        }
        if (StringUtils.hasText(account.getParentId())) {
            BudgetAccount parent = getById(account.getParentId());
            if (parent != null) {
                int parentLevel = parent.getAccountLevel() != null ? parent.getAccountLevel() : 1;
                account.setAccountLevel(parentLevel + 1);
                String parentPath = StringUtils.hasText(parent.getAccountPath()) ? parent.getAccountPath() : parent.getAccountId();
                account.setAccountPath(parentPath + "/" + account.getAccountId());
            } else {
                account.setAccountLevel(1);
                account.setAccountPath(account.getAccountId());
            }
        } else {
            account.setAccountLevel(1);
            account.setAccountPath(account.getAccountId());
        }
        if (account.getDelFlag() == null) account.setDelFlag(0);
        if (account.getIsEnabled() == null) account.setIsEnabled(1);
        account.setCreateTime(new Date());
        account.setUpdateTime(new Date());
        accountMapper.insert(account);
        return account;
    }

    @Override
    public BudgetAccount getById(String accountId) {
        if (!StringUtils.hasText(accountId)) throw new ServiceException("科目ID不能为空");
        QueryWrapper<BudgetAccount> w = new QueryWrapper<>();
        w.eq("ACCOUNT_ID", accountId).eq("DEL_FLAG", 0L);
        BudgetAccount account = accountMapper.selectOne(w);
        if (account != null) {
            account.setAccountMappings(loadAccountMappings(accountId));
        }
        return account;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetAccount account) {
        if (account == null || !StringUtils.hasText(account.getAccountId())) throw new ServiceException("科目ID不能为空");
        BudgetAccount existing = getById(account.getAccountId());
        if (existing == null) throw new ServiceException("科目不存在");
        account.setUpdateTime(new Date());
        accountMapper.updateById(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String accountId) {
        if (!StringUtils.hasText(accountId)) throw new ServiceException("科目ID不能为空");
        BudgetAccount update = new BudgetAccount();
        update.setAccountId(accountId);
        update.setDelFlag(1);
        update.setUpdateTime(new Date());
        accountMapper.updateById(update);
    }

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
        QueryWrapper<BudgetAccount> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0L);
        if (params.get("accountCode") != null && StringUtils.hasText(params.get("accountCode").toString())) w.like("ACCOUNT_CODE", params.get("accountCode"));
        if (params.get("accountName") != null && StringUtils.hasText(params.get("accountName").toString())) w.like("ACCOUNT_NAME", params.get("accountName"));
        if (params.get("accountType") != null && StringUtils.hasText(params.get("accountType").toString())) w.eq("ACCOUNT_TYPE", params.get("accountType"));
        if (params.get("parentAccountId") != null && StringUtils.hasText(params.get("parentAccountId").toString())) w.eq("PARENT_ID", params.get("parentAccountId"));
        if (params.get("accountStatus") != null && StringUtils.hasText(params.get("accountStatus").toString())) {
            String accountStatus = params.get("accountStatus").toString();
            if ("ACTIVE".equalsIgnoreCase(accountStatus)) {
                w.eq("IS_ENABLED", 1);
            } else if ("INACTIVE".equalsIgnoreCase(accountStatus)) {
                w.eq("IS_ENABLED", 0);
            }
        }
        w.orderByAsc("SORT_ORDER").orderByAsc("ACCOUNT_CODE");
        Page<BudgetAccount> page = new Page<>(pageNum, pageSize);
        IPage<BudgetAccount> pageResult = accountMapper.selectPage(page, w);

        // 批量查上级科目名称，避免 N+1
        List<BudgetAccount> records = pageResult.getRecords();
        Set<String> parentIds = records.stream()
                .map(BudgetAccount::getParentId)
                .filter(StringUtils::hasText)
                .collect(Collectors.toSet());
        Map<String, String> parentNameMap = new HashMap<>();
        if (!parentIds.isEmpty()) {
            QueryWrapper<BudgetAccount> pq = new QueryWrapper<>();
            pq.in("ACCOUNT_ID", parentIds).eq("DEL_FLAG", 0L);
            accountMapper.selectList(pq).forEach(a -> parentNameMap.put(a.getAccountId(), a.getAccountName()));
        }

        // 批量加载科目映射
        Set<String> accountIds = records.stream()
                .map(BudgetAccount::getAccountId)
                .collect(Collectors.toSet());
        Map<String, List<BudgetAccountMapping>> mappingsMap = new HashMap<>();
        if (!accountIds.isEmpty()) {
            for (String accountId : accountIds) {
                mappingsMap.put(accountId, loadAccountMappings(accountId));
            }
        }

        // 把每条记录转成 Map，追加 parentAccountName 和 accountMappings
        List<Map<String, Object>> enrichedRecords = records.stream().map(a -> {
            Map<String, Object> row = new java.util.LinkedHashMap<>();
            row.put("accountId", a.getAccountId());
            row.put("accountCode", a.getAccountCode());
            row.put("accountName", a.getAccountName());
            row.put("accountType", a.getAccountType());
            row.put("accountCategory", a.getAccountCategory());
            row.put("parentId", a.getParentId());
            row.put("parentAccountName", StringUtils.hasText(a.getParentId()) ? parentNameMap.getOrDefault(a.getParentId(), "") : "");
            row.put("accountLevel", a.getAccountLevel());
            row.put("accountPath", a.getAccountPath());
            row.put("isLeaf", a.getIsLeaf());
            row.put("balanceDirection", a.getBalanceDirection());
            row.put("sortOrder", a.getSortOrder());
            row.put("isEnabled", a.getIsEnabled());
            row.put("budgetAmount", a.getBudgetAmount());
            row.put("accountDescription", a.getAccountDescription());
            row.put("remark", a.getRemark());
            row.put("createTime", a.getCreateTime());
            row.put("updateTime", a.getUpdateTime());
            row.put("accountMappings", mappingsMap.getOrDefault(a.getAccountId(), new ArrayList<>()));
            return row;
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("records", enrichedRecords);
        result.put("total", pageResult.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public List<Map<String, Object>> getAccountTree() {
        QueryWrapper<BudgetAccount> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0L).eq("IS_ENABLED", 1).orderByAsc("SORT_ORDER");
        List<BudgetAccount> all = accountMapper.selectList(w);
        return buildTree(all, null);
    }

    private List<Map<String, Object>> buildTree(List<BudgetAccount> all, String parentId) {
        return all.stream()
            .filter(a -> parentId == null ? !StringUtils.hasText(a.getParentId()) : parentId.equals(a.getParentId()))
            .map(a -> {
                Map<String, Object> node = new HashMap<>();
                node.put("id", a.getAccountId());
                node.put("label", a.getAccountCode() + " " + a.getAccountName());
                node.put("accountCode", a.getAccountCode());
                node.put("accountName", a.getAccountName());
                node.put("accountType", a.getAccountType());
                node.put("children", buildTree(all, a.getAccountId()));
                return node;
            }).collect(Collectors.toList());
    }

    @Override
    public List<BudgetAccount> getParentAccounts() {
        QueryWrapper<BudgetAccount> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0L).eq("IS_ENABLED", 1).orderByAsc("SORT_ORDER").orderByAsc("ACCOUNT_CODE");
        return accountMapper.selectList(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String accountId, Boolean isEnabled) {
        BudgetAccount update = new BudgetAccount();
        update.setAccountId(accountId);
        update.setIsEnabled(isEnabled != null && isEnabled ? 1 : 0);
        update.setUpdateTime(new Date());
        accountMapper.updateById(update);
    }

    @Override
    public Map<String, Object> batchValidate(List<String> ids) {
        Map<String, Object> result = new HashMap<>();
        result.put("total", ids.size());
        result.put("valid", ids.size());
        result.put("invalid", 0);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        for (String id : ids) { delete(id); }
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        QueryWrapper<BudgetAccount> baseW = new QueryWrapper<>();
        baseW.eq("DEL_FLAG", 0L);
        long totalAccounts = accountMapper.selectCount(baseW);

        QueryWrapper<BudgetAccount> activeW = new QueryWrapper<>();
        activeW.eq("DEL_FLAG", 0L).eq("IS_ENABLED", 1);
        long activeAccounts = accountMapper.selectCount(activeW);

        QueryWrapper<BudgetAccount> leafW = new QueryWrapper<>();
        leafW.eq("DEL_FLAG", 0L).eq("IS_LEAF", 1);
        long leafAccounts = accountMapper.selectCount(leafW);

        stats.put("totalAccounts", totalAccounts);
        stats.put("activeAccounts", activeAccounts);
        stats.put("leafAccounts", leafAccounts);
        stats.put("mappedAccounts", 0);
        stats.put("activeRate", totalAccounts > 0 ? Math.round(activeAccounts * 100.0 / totalAccounts) : 0);
        stats.put("leafRate", totalAccounts > 0 ? Math.round(leafAccounts * 100.0 / totalAccounts) : 0);
        stats.put("mappedRate", 0);
        return stats;
    }

    @Override
    public List<BudgetAccount> getExportData(Map<String, Object> params) {
        Object idsObj = params.get("ids");
        if (idsObj instanceof List && !((List<?>) idsObj).isEmpty()) {
            List<String> ids = new ArrayList<>();
            for (Object o : (List<?>) idsObj) { ids.add(o.toString()); }
            QueryWrapper<BudgetAccount> w = new QueryWrapper<>();
            w.in("ACCOUNT_ID", ids).eq("DEL_FLAG", 0L);
            return accountMapper.selectList(w);
        }
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
        QueryWrapper<BudgetAccount> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0L);
        if (params.get("accountCode") != null && !params.get("accountCode").toString().isEmpty())
            w.like("ACCOUNT_CODE", params.get("accountCode"));
        if (params.get("accountName") != null && !params.get("accountName").toString().isEmpty())
            w.like("ACCOUNT_NAME", params.get("accountName"));
        if (params.get("accountType") != null && !params.get("accountType").toString().isEmpty())
            w.eq("ACCOUNT_TYPE", params.get("accountType"));
        w.orderByAsc("SORT_ORDER").orderByAsc("ACCOUNT_CODE");
        IPage<BudgetAccount> pageResult = accountMapper.selectPage(new Page<>(pageNum, pageSize), w);
        return pageResult.getRecords();
    }

    @Override
    public BudgetAccount getByIdDirect(String accountId) {
        return accountMapper.selectById(accountId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importAccounts(List<BudgetAccount> accounts) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> failMessages = new ArrayList<>();
        Date now = new Date();
        for (int i = 0; i < accounts.size(); i++) {
            BudgetAccount a = accounts.get(i);
            try {
                if (!StringUtils.hasText(a.getAccountCode())) {
                    throw new ServiceException("科目编码不能为空");
                }
                if (!StringUtils.hasText(a.getAccountName())) {
                    throw new ServiceException("科目名称不能为空");
                }
                QueryWrapper<BudgetAccount> w = new QueryWrapper<>();
                w.eq("ACCOUNT_CODE", a.getAccountCode()).eq("DEL_FLAG", 0L);
                Long count = accountMapper.selectCount(w);
                if (count != null && count > 0) {
                    throw new ServiceException("科目编码 " + a.getAccountCode() + " 已存在");
                }
                if (a.getDelFlag() == null) a.setDelFlag(0);
                if (a.getIsEnabled() == null) a.setIsEnabled(1);
                a.setCreateTime(now);
                a.setUpdateTime(now);
                accountMapper.insert(a);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failMessages.add("第" + (i + 1) + "行: " + e.getMessage());
            }
        }
        result.put("total", accounts.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failMessages", failMessages);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAccountMappings(String accountId, List<BudgetAccountMapping> mappings) {
        if (!StringUtils.hasText(accountId)) {
            throw new ServiceException("科目ID不能为空");
        }

        // 删除该科目原有的所有映射
        QueryWrapper<BudgetAccountMapping> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("ACCOUNT_ID", accountId);
        mappingMapper.delete(deleteWrapper);

        // 保存新的映射
        if (mappings != null && !mappings.isEmpty()) {
            Date now = new Date();
            for (BudgetAccountMapping mapping : mappings) {
                if (!StringUtils.hasText(mapping.getMappingSystem()) ||
                    !StringUtils.hasText(mapping.getMappingCode())) {
                    continue; // 跳过不完整的映射
                }
                mapping.setAccountId(accountId);
                if (!StringUtils.hasText(mapping.getMappingId())) {
                    mapping.setMappingId(UUID.randomUUID().toString().replace("-", ""));
                }
                if (mapping.getIsEnabled() == null) {
                    mapping.setIsEnabled(true);
                }
                if (mapping.getDelFlag() == null) {
                    mapping.setDelFlag(0);
                }
                mapping.setCreateTime(now);
                mapping.setUpdateTime(now);
                mappingMapper.insert(mapping);
            }
        }
    }
}

