package com.global.treasurer.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcEticketAccount;
import com.global.treasurer.mapper.TcEticketAccountMapper;
import com.global.treasurer.service.TcEticketAccountService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电子票据账户Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
@Service
public class TcEticketAccountServiceImpl extends ServiceImpl<TcEticketAccountMapper, TcEticketAccount>
        implements TcEticketAccountService {
    private static final Logger log = LoggerFactory.getLogger(TcEticketAccountServiceImpl.class);

    @Override
    public IPage<TcEticketAccount> getETicketAccountPage(Integer page, Integer limit, String accountNo,
                                                         String accountName, String eTicketType,
                                                         String accountType, String bankId, String status) {
        // 使用 PageHelper 进行分页
        PageHelper.startPage(page, limit);

        // 构建查询条件
        QueryWrapper<TcEticketAccount> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(accountNo)) {
            queryWrapper.like("ACCOUNT_NO", accountNo);
        }
        if (StringUtils.hasText(accountName)) {
            queryWrapper.like("ACCOUNT_NAME", accountName);
        }
        // 字段已从数据库表中移除: ETICKET_SYSTEM, ACCOUNT_TYPE
        // if (StringUtils.hasText(eTicketType)) {
        //     queryWrapper.eq("ETICKET_SYSTEM", eTicketType);
        // }
        // if (StringUtils.hasText(accountType)) {
        //     queryWrapper.eq("ACCOUNT_TYPE", accountType);
        // }
        if (StringUtils.hasText(bankId)) {
            queryWrapper.eq("BANK_CODE", bankId);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq("STATUS", status);
        }

        // 按创建时间倒序排序
        queryWrapper.orderByDesc("CREATE_TIME");

        // 执行查询 - 使用 ServiceImpl 提供的 list 方法
        List<TcEticketAccount> list = this.list(queryWrapper);

        // 获取分页信息
        PageInfo<TcEticketAccount> pageInfo = new PageInfo<>(list);

        // 将 PageInfo 转换为 IPage
        Page<TcEticketAccount> result = new Page<>(page, limit);
        result.setRecords(list);
        result.setTotal(pageInfo.getTotal());
        result.setCurrent(page);
        result.setSize(limit);

        return result;
    }

    @Override
    public boolean validateAccountConfig(TcEticketAccount account) {
        // TODO: 实现账户配置验证逻辑
        return true;
    }

    @Override
    public Map<String, Object> getAccountInterfaceConfig(String id) {
        TcEticketAccount account = this.getById(id);
        if (account == null) {
            return new HashMap<>();
        }

        Map<String, Object> config = new HashMap<>();
        config.put("interfaceUrl", account.getInterfaceUrl());
        config.put("interfacePort", account.getInterfacePort());
        config.put("certPath", account.getCertPath());
        config.put("certPassword", account.getCertPassword());
        config.put("operatorInfo", account.getOperatorInfo());
        config.put("timeoutSetting", account.getTimeoutSetting());
        config.put("retryCount", account.getRetryCount());
        return config;
    }

    @Override
    public void updateAccountInterfaceConfig(String id, Map<String, Object> interfaceConfig, String updateUser) {
        TcEticketAccount account = this.getById(id);
        if (account == null) {
            throw new RuntimeException("账户不存在: " + id);
        }

        if (interfaceConfig.containsKey("interfaceUrl")) {
            account.setInterfaceUrl((String) interfaceConfig.get("interfaceUrl"));
        }
        if (interfaceConfig.containsKey("interfacePort")) {
            account.setInterfacePort((Integer) interfaceConfig.get("interfacePort"));
        }
        if (interfaceConfig.containsKey("certPath")) {
            account.setCertPath((String) interfaceConfig.get("certPath"));
        }
        if (interfaceConfig.containsKey("certPassword")) {
            account.setCertPassword((String) interfaceConfig.get("certPassword"));
        }
        if (interfaceConfig.containsKey("operatorInfo")) {
            account.setOperatorInfo((String) interfaceConfig.get("operatorInfo"));
        }
        if (interfaceConfig.containsKey("timeoutSetting")) {
            account.setTimeoutSetting((Integer) interfaceConfig.get("timeoutSetting"));
        }
        if (interfaceConfig.containsKey("retryCount")) {
            account.setRetryCount((Integer) interfaceConfig.get("retryCount"));
        }

        account.setUpdateUser(updateUser);
        this.updateById(account);
    }

    @Override
    public void activateAccount(String id, String updateUser) {
        TcEticketAccount account = this.getById(id);
        if (account == null) {
            throw new RuntimeException("账户不存在: " + id);
        }

        account.setStatus("1"); // 设置为启用状态
        account.setUpdateUser(updateUser);
        this.updateById(account);
    }

    @Override
    public void deactivateAccount(String id, String updateUser) {
        TcEticketAccount account = this.getById(id);
        if (account == null) {
            throw new RuntimeException("账户不存在: " + id);
        }

        account.setStatus("0"); // 设置为停用状态
        account.setUpdateUser(updateUser);
        this.updateById(account);
    }

    @Override
    public boolean testConnection(String id) {
        TcEticketAccount account = this.getById(id);
        if (account == null) {
            throw new RuntimeException("账户不存在: " + id);
        }

        // TODO: 实现电票账户连接测试逻辑
        // 这里应该根据账户配置测试实际的连接
        // 例如: 测试银行接口连接、证书验证等

        // 临时返回true,实际应该测试真实的连接
        return true;
    }

    @Override
    public Map<String, Boolean> batchTestConnection(List<String> ids) {
        Map<String, Boolean> results = new HashMap<>();
        if (ids != null && !ids.isEmpty()) {
            for (String id : ids) {
                try {
                    boolean result = testConnection(id);
                    results.put(id, result);
                } catch (Exception e) {
                    results.put(id, false);
                }
            }
        }
        return results;
    }

    @Override
    public List<Map<String, Object>> getConnectionStatusStatistics() {
        List<Map<String, Object>> statistics = new ArrayList<>();

        try {
            // 统计各连接状态的账户数量
            QueryWrapper<TcEticketAccount> wrapper = new QueryWrapper<>();
            wrapper.select("CONNECTION_STATUS as status");
            wrapper.isNotNull("CONNECTION_STATUS");

            List<TcEticketAccount> accounts = this.list(wrapper);

            // 按状态分组统计
            Map<String, Long> statusCount = new HashMap<>();
            for (TcEticketAccount account : accounts) {
                String status = account.getConnectionStatus();
                if (status != null) {
                    statusCount.put(status, statusCount.getOrDefault(status, 0L) + 1);
                }
            }

            // 转换为返回格式
            for (Map.Entry<String, Long> entry : statusCount.entrySet()) {
                Map<String, Object> stat = new HashMap<>();
                stat.put("status", getConnectionStatusName(entry.getKey()));
                stat.put("count", entry.getValue());
                statistics.add(stat);
            }

            // 如果没有数据,返回空列表而不是假数据
            if (statistics.isEmpty()) {
                Map<String, Object> empty = new HashMap<>();
                empty.put("status", "暂无数据");
                empty.put("count", 0);
                statistics.add(empty);
            }

        } catch (Exception e) {
            log.error("获取连接状态统计失败", e);
            // 出错时返回空数据而不是假数据
            Map<String, Object> error = new HashMap<>();
            error.put("status", "统计失败");
            error.put("count", 0);
            statistics.add(error);
        }

        return statistics;
    }

    @Override
    public List<Map<String, Object>> getEticketStatusStatistics() {
        List<Map<String, Object>> statistics = new ArrayList<>();

        try {
            // 统计各账户状态的账户数量
            QueryWrapper<TcEticketAccount> wrapper = new QueryWrapper<>();
            // 字段已从数据库表中移除: ACCOUNT_STATUS，使用 STATUS 字段代替
            wrapper.select("STATUS as status");
            wrapper.isNotNull("STATUS");

            List<TcEticketAccount> accounts = this.list(wrapper);

            // 按状态分组统计
            Map<String, Long> statusCount = new HashMap<>();
            for (TcEticketAccount account : accounts) {
                // 字段已从数据库表中移除: ACCOUNT_STATUS
                // String status = account.getAccountStatus();
                // 使用 status 字段代替
                String status = account.getStatus();
                if (status != null) {
                    statusCount.put(status, statusCount.getOrDefault(status, 0L) + 1);
                }
            }

            // 转换为返回格式
            for (Map.Entry<String, Long> entry : statusCount.entrySet()) {
                Map<String, Object> stat = new HashMap<>();
                stat.put("status", getAccountStatusName(entry.getKey()));
                stat.put("count", entry.getValue());
                statistics.add(stat);
            }

            // 如果没有数据,返回空列表而不是假数据
            if (statistics.isEmpty()) {
                Map<String, Object> empty = new HashMap<>();
                empty.put("status", "暂无数据");
                empty.put("count", 0);
                statistics.add(empty);
            }

        } catch (Exception e) {
            log.error("获取电票状态统计失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "统计失败");
            error.put("count", 0);
            statistics.add(error);
        }

        return statistics;
    }

    @Override
    public List<Map<String, Object>> getBankStatistics() {
        List<Map<String, Object>> statistics = new ArrayList<>();

        try {
            // 统计各银行的账户数量
            QueryWrapper<TcEticketAccount> wrapper = new QueryWrapper<>();
            wrapper.select("BANK_CODE as bankCode");
            wrapper.isNotNull("BANK_CODE");

            List<TcEticketAccount> accounts = this.list(wrapper);

            // 按银行分组统计
            Map<String, Long> bankCount = new HashMap<>();
            for (TcEticketAccount account : accounts) {
                String bankCode = account.getBankCode();
                if (bankCode != null) {
                    bankCount.put(bankCode, bankCount.getOrDefault(bankCode, 0L) + 1);
                }
            }

            // 转换为返回格式
            for (Map.Entry<String, Long> entry : bankCount.entrySet()) {
                Map<String, Object> stat = new HashMap<>();
                stat.put("bankName", getBankName(entry.getKey()));
                stat.put("bankCode", entry.getKey());
                stat.put("count", entry.getValue());
                statistics.add(stat);
            }

            // 如果没有数据,返回空列表而不是假数据
            if (statistics.isEmpty()) {
                Map<String, Object> empty = new HashMap<>();
                empty.put("bankName", "暂无数据");
                empty.put("bankCode", "");
                empty.put("count", 0);
                statistics.add(empty);
            }

        } catch (Exception e) {
            log.error("获取银行统计失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("bankName", "统计失败");
            error.put("bankCode", "");
            error.put("count", 0);
            statistics.add(error);
        }

        return statistics;
    }

    /**
     * 获取连接状态名称
     */
    private String getConnectionStatusName(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "ONLINE": return "已连接";
            case "OFFLINE": return "未连接";
            case "CONNECTING": return "连接中";
            default: return status;
        }
    }

    /**
     * 获取账户状态名称
     */
    private String getAccountStatusName(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "NORMAL": return "正常";
            case "FROZEN": return "冻结";
            case "CLOSED": return "关闭";
            default: return status;
        }
    }

    /**
     * 获取银行名称
     */
    private String getBankName(String bankCode) {
        if (bankCode == null) return "未知银行";
        switch (bankCode) {
            case "ICBC": return "工商银行";
            case "CCB": return "建设银行";
            case "ABC": return "农业银行";
            case "BOC": return "中国银行";
            case "BOCOM": return "交通银行";
            case "CMB": return "招商银行";
            case "SPDB": return "浦发银行";
            default: return bankCode;
        }
    }

    @Override
    public void batchDelete(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            for (String id : ids) {
                try {
                    this.removeById(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void updateEticketStatus(String id, String status, String updateUser) {
        TcEticketAccount account = this.getById(id);
        if (account == null) {
            throw new RuntimeException("账户不存在: " + id);
        }

        account.setEticketStatus(status);
        account.setUpdateUser(updateUser);
        this.updateById(account);
    }

    @Override
    public void updateConnectionStatus(String id, String connectionStatus, String updateUser) {
        TcEticketAccount account = this.getById(id);
        if (account == null) {
            throw new RuntimeException("账户不存在: " + id);
        }

        account.setConnectionStatus(connectionStatus);
        account.setUpdateUser(updateUser);
        this.updateById(account);
    }

    @Override
    public List<TcEticketAccount> getAccountsByBusinessScope(String businessScope) {
        QueryWrapper<TcEticketAccount> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(businessScope)) {
            queryWrapper.like("BUSINESS_SCOPE", businessScope);
        }
        queryWrapper.orderByDesc("CREATE_TIME");
        return this.list(queryWrapper);
    }

    @Override
    public List<TcEticketAccount> getActiveAccounts() {
        QueryWrapper<TcEticketAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("STATUS", "1"); // 1表示启用
        queryWrapper.orderByDesc("CREATE_TIME");
        return this.list(queryWrapper);
    }

    @Override
    public List<TcEticketAccount> getAccountsByConnectionStatus(String connectionStatus) {
        QueryWrapper<TcEticketAccount> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(connectionStatus)) {
            queryWrapper.eq("CONNECTION_STATUS", connectionStatus);
        }
        queryWrapper.orderByDesc("CREATE_TIME");
        return this.list(queryWrapper);
    }

    @Override
    public List<TcEticketAccount> getAccountsByEticketStatus(String eticketStatus) {
        QueryWrapper<TcEticketAccount> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(eticketStatus)) {
            queryWrapper.eq("ETICKET_STATUS", eticketStatus);
        }
        queryWrapper.orderByDesc("CREATE_TIME");
        return this.list(queryWrapper);
    }

    @Override
    public TcEticketAccount findByCustomerNo(String customerNo) {
        QueryWrapper<TcEticketAccount> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(customerNo)) {
            queryWrapper.eq("CUSTOMER_NO", customerNo);
        }
        return this.getOne(queryWrapper);
    }

    @Override
    public TcEticketAccount findByBankAccountId(String bankAccountId) {
        QueryWrapper<TcEticketAccount> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(bankAccountId)) {
            queryWrapper.eq("BANK_ACCOUNT_ID", bankAccountId);
        }
        return this.getOne(queryWrapper);
    }

    @Override
    public List<TcEticketAccount> getAccountsByBankCode(String bankCode) {
        QueryWrapper<TcEticketAccount> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(bankCode)) {
            queryWrapper.eq("BANK_CODE", bankCode);
        }
        queryWrapper.orderByDesc("CREATE_TIME");
        return this.list(queryWrapper);
    }

    @Override
    public TcEticketAccount findById(String id) {
        return this.getById(id);
    }

    @Override
    public TcEticketAccount saveOrUpdateAccount(TcEticketAccount eticketAccount) {
        return this.saveOrUpdate(eticketAccount) ? eticketAccount : null;
    }

    @Override
    public void delete(String id) {
        this.removeById(id);
    }

    @Override
    public PageInfo<TcEticketAccount> getList(int pageNum, int pageSize, String bankCode, String bankName, String accountNo, String customerNo, String eticketStatus, String connectionStatus, String status) {
        // 使用 PageHelper 进行分页
        PageHelper.startPage(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<TcEticketAccount> queryWrapper = new QueryWrapper<>();

        // 只在有条件时添加WHERE子句
        boolean hasCondition = false;

        if (StringUtils.hasText(bankCode)) {
            queryWrapper.eq("BANK_CODE", bankCode);
            hasCondition = true;
        }
        if (StringUtils.hasText(bankName)) {
            queryWrapper.like("BANK_NAME", bankName);
            hasCondition = true;
        }
        if (StringUtils.hasText(accountNo)) {
            queryWrapper.like("ACCOUNT_NO", accountNo);
            hasCondition = true;
        }
        if (StringUtils.hasText(customerNo)) {
            queryWrapper.like("CUSTOMER_NO", customerNo);
            hasCondition = true;
        }
        if (StringUtils.hasText(eticketStatus)) {
            queryWrapper.eq("ETICKET_STATUS", eticketStatus);
            hasCondition = true;
        }
        if (StringUtils.hasText(connectionStatus)) {
            queryWrapper.eq("CONNECTION_STATUS", connectionStatus);
            hasCondition = true;
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq("STATUS", status);
            hasCondition = true;
        }

        // 只在有条件或需要排序时添加orderBy
        if (hasCondition) {
            queryWrapper.orderByDesc("CREATE_TIME");
        }

        // 执行查询 - 使用ServiceImpl提供的list方法
        List<TcEticketAccount> list = this.list(queryWrapper);

        // 返回分页信息
        return new PageInfo<>(list);
    }
}
