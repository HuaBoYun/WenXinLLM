package com.global.treasurer.service.impl;

import com.global.treasurer.entity.ETicketAccountConfig;
import com.global.treasurer.mapper.ETicketAccountConfigMapper;
import com.global.treasurer.service.ETicketAccountConfigService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 电票账户配置服务实现
 *
 * @author AI Developer
 * @date 2025-02-26
 */
@Service
public class ETicketAccountConfigServiceImpl implements ETicketAccountConfigService {
    private static final Logger log = LoggerFactory.getLogger(ETicketAccountConfigServiceImpl.class);

    @Resource
    private ETicketAccountConfigMapper eTicketAccountConfigMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> getAccountPage(Map<String, Object> param) {
        // 使用PageHelper分页，添加默认值处理
        // 支持pageNo/page和pageSize/limit两种参数命名方式
        int pageNo = 1;
        if (param.get("pageNo") != null) {
            pageNo = Integer.parseInt(param.get("pageNo").toString());
        } else if (param.get("page") != null) {
            pageNo = Integer.parseInt(param.get("page").toString());
        }

        int pageSize = 20;
        if (param.get("pageSize") != null) {
            pageSize = Integer.parseInt(param.get("pageSize").toString());
        } else if (param.get("limit") != null) {
            pageSize = Integer.parseInt(param.get("limit").toString());
        }

        com.github.pagehelper.PageHelper.startPage(pageNo, pageSize);

        List<ETicketAccountConfig> list = eTicketAccountConfigMapper.selectAccountPage(param);

        // 新列为 null 时用旧列兜底，保证前端渲染正常
        list.forEach(this::fillCompatFields);

        // PageInfo格式
        com.github.pagehelper.PageInfo<ETicketAccountConfig> pageInfo = new com.github.pagehelper.PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", pageInfo.getTotal());
        result.put("pageNo", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());

        return result;
    }

    @Override
    public ETicketAccountConfig getAccountById(String id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        List<ETicketAccountConfig> list = eTicketAccountConfigMapper.selectAccountPage(param);
        if (list.isEmpty()) return null;
        ETicketAccountConfig account = list.get(0);
        fillCompatFields(account);
        return account;
    }

    /**
     * 新列（ALTER TABLE 后加的）为 null 时，用旧列兜底回填，保证前端渲染正常。
     * 新增数据会直接写新列，历史数据只有旧列有值。
     */
    private void fillCompatFields(ETicketAccountConfig a) {
        // accountCode: 新列 ACCOUNT_CODE 为空时用 ACCOUNT_NO 兜底
        if (a.getAccountCode() == null) a.setAccountCode(a.getAccountNo());
        // accountName: 新列 ACCOUNT_NAME 为空时用 BANK_NAME 兜底
        if (a.getAccountName() == null) a.setAccountName(a.getBankName());
        // eTicketSystem: 新列 ETICKET_SYSTEM 为空时用 BUSINESS_SCOPE 兜底
        if (a.getETicketSystem() == null) a.setETicketSystem(a.getBusinessScope());
        // accountStatus: 新列 ACCOUNT_STATUS 为空时用 ETICKET_STATUS 兜底
        if (a.getAccountStatus() == null) a.setAccountStatus(a.getEticketStatus());
        // apiEndpoint: 新列 API_ENDPOINT 为空时用 INTERFACE_URL 兜底
        if (a.getApiEndpoint() == null) a.setApiEndpoint(a.getInterfaceUrl());
        // balanceLimit: 新列 BALANCE_LIMIT 为空时用 MONTHLY_LIMIT 兜底
        if (a.getBalanceLimit() == null) a.setBalanceLimit(a.getMonthlyLimit());
        // accountNumber: exist=false 字段，从 accountNo 补填
        if (a.getAccountNumber() == null) a.setAccountNumber(a.getAccountNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createAccount(ETicketAccountConfig account) {
        TblStaffUtil loginStaff;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            throw new RuntimeException("用户信息获取失败", e);
        }
        if (loginStaff == null) {
            throw new RuntimeException("用户信息获取失败");
        }
        account.setCreateUser(loginStaff.getRealname());
        account.setCreateTime(new Date());
        account.setStatus("1");
        syncAliasFields(account);
        return eTicketAccountConfigMapper.insert(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAccount(ETicketAccountConfig account) {
        TblStaffUtil loginStaff;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            throw new RuntimeException("用户信息获取失败", e);
        }
        if (loginStaff == null) {
            throw new RuntimeException("用户信息获取失败");
        }
        account.setUpdateUser(loginStaff.getRealname());
        account.setUpdateTime(new Date());
        syncAliasFields(account);
        return eTicketAccountConfigMapper.updateById(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<String> ids) {
        return eTicketAccountConfigMapper.batchDelete(ids);
    }

    @Override
    public Map<String, Object> syncAccountStatus(String id) {
        Map<String, Object> result = eTicketAccountConfigMapper.syncAccountStatus(id);
        if (result == null || result.isEmpty()) {
            result = new HashMap<>();
            result.put("success", 0);
            result.put("message", "账户不存在");
        }
        return result;
    }

    @Override
    public Map<String, Object> getAccountStatistics() {
        Map<String, Object> stats = eTicketAccountConfigMapper.selectAccountStatistics();
        if (stats == null) {
            stats = new HashMap<>();
        }
        // 达梦数据库 resultType=HashMap 时列名别名会变大写，统一转为小驼峰
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount",      getStatValue(stats, "totalCount",      "TOTALCOUNT"));
        result.put("normalCount",     getStatValue(stats, "normalCount",      "NORMALCOUNT"));
        result.put("totalCreditLimit",getStatValue(stats, "totalCreditLimit", "TOTALCREDITLIMIT"));
        result.put("todayCount",      getStatValue(stats, "todayCount",       "TODAYCOUNT"));
        return result;
    }

    /**
     * 将前端别名字段同步到数据库真实字段（ALTER TABLE 后直接写新列）
     */
    private void syncAliasFields(ETicketAccountConfig account) {
        // accountNumber → ACCOUNT_NO（兼容旧字段名）
        if (account.getAccountNumber() != null && account.getAccountNo() == null) {
            account.setAccountNo(account.getAccountNumber());
        }
        // 防御：eTicketSystem 因 Jackson 命名问题丢失时，从 businessScope 兜底
        if ((account.getETicketSystem() == null || account.getETicketSystem().isEmpty())
                && account.getBusinessScope() != null) {
            account.setETicketSystem(account.getBusinessScope());
        }
    }

    private Object getStatValue(Map<String, Object> map, String camelKey, String upperKey) {
        if (map.containsKey(camelKey)) return map.get(camelKey);
        if (map.containsKey(upperKey)) return map.get(upperKey);
        return 0;
    }
}
