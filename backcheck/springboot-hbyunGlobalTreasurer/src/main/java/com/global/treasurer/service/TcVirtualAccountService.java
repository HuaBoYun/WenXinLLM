package com.global.treasurer.service;

import com.global.treasurer.entity.TcVirtualAccount;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 财资公共模块 - 虚拟账户管理Service
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
public interface TcVirtualAccountService {

    /**
     * 分页查询虚拟账户列表
     */
    PageInfo<TcVirtualAccount> getList(int pageNum, int pageSize, String accountCode, String accountName, 
                                       String accountType, String platformName, String currencyCode, 
                                       String syncStatus, String status);

    /**
     * 新增或更新虚拟账户
     */
    TcVirtualAccount saveOrUpdate(TcVirtualAccount virtualAccount);

    /**
     * 根据ID删除虚拟账户
     */
    void delete(String id);

    /**
     * 根据ID查询虚拟账户详情
     */
    TcVirtualAccount findById(String id);

    /**
     * 根据账户编码查询
     */
    TcVirtualAccount findByAccountCode(String accountCode);

    /**
     * 根据账户类型查询账户列表
     */
    List<TcVirtualAccount> getAccountsByType(String accountType);

    /**
     * 根据平台名称查询账户列表
     */
    List<TcVirtualAccount> getAccountsByPlatformName(String platformName);

    /**
     * 根据币种查询账户列表
     */
    List<TcVirtualAccount> getAccountsByCurrencyCode(String currencyCode);

    /**
     * 根据同步状态查询账户列表
     */
    List<TcVirtualAccount> getAccountsBySyncStatus(String syncStatus);

    /**
     * 查询需要同步的账户
     */
    List<TcVirtualAccount> getAccountsForSync();

    /**
     * 更新账户余额
     */
    void updateBalance(String id, BigDecimal balance, BigDecimal availableBalance, 
                       BigDecimal frozenBalance, String updateUser);

    /**
     * 更新同步状态
     */
    void updateSyncStatus(String id, String syncStatus, String updateUser);

    /**
     * 批量删除虚拟账户
     */
    void batchDelete(List<String> ids);

    /**
     * 统计各账户类型数量
     */
    List<Map<String, Object>> getAccountTypeStatistics();

    /**
     * 统计各同步状态数量
     */
    List<Map<String, Object>> getSyncStatusStatistics();

    /**
     * 检查账户编码是否存在
     */
    boolean checkAccountCodeExists(String accountCode, String excludeId);

    /**
     * 同步账户余额
     */
    boolean syncAccountBalance(String id);

    /**
     * 批量同步账户余额
     */
    Map<String, Boolean> batchSyncAccountBalance(List<String> ids);

    /**
     * 计算总余额
     */
    BigDecimal calculateTotalBalance(String accountType, String currencyCode);

    /**
     * 冻结账户余额
     */
    void freezeBalance(String id, BigDecimal amount, String updateUser);

    /**
     * 解冻账户余额
     */
    void unfreezeBalance(String id, BigDecimal amount, String updateUser);

    /**
     * 获取账户余额信息
     */
    Map<String, BigDecimal> getAccountBalanceInfo(String id);
}
