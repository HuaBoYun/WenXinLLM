package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TcEticketAccount;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 财资公共模块 - 电票账户设置管理Service
 *
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
public interface TcEticketAccountService extends IService<TcEticketAccount> {

    /**
     * 分页查询电票账户列表
     */
    PageInfo<TcEticketAccount> getList(int pageNum, int pageSize, String bankCode, String bankName, 
                                       String accountNo, String customerNo, String eticketStatus, 
                                       String connectionStatus, String status);

    /**
     * 新增或更新电票账户
     */
    TcEticketAccount saveOrUpdateAccount(TcEticketAccount eticketAccount);

    /**
     * 根据ID删除电票账户
     */
    void delete(String id);

    /**
     * 根据ID查询电票账户详情
     */
    TcEticketAccount findById(String id);

    /**
     * 根据银行代码查询电票账户列表
     */
    List<TcEticketAccount> getAccountsByBankCode(String bankCode);

    /**
     * 根据银行账户ID查询
     */
    TcEticketAccount findByBankAccountId(String bankAccountId);

    /**
     * 根据客户号查询
     */
    TcEticketAccount findByCustomerNo(String customerNo);

    /**
     * 根据电票状态查询账户列表
     */
    List<TcEticketAccount> getAccountsByEticketStatus(String eticketStatus);

    /**
     * 根据连接状态查询账户列表
     */
    List<TcEticketAccount> getAccountsByConnectionStatus(String connectionStatus);

    /**
     * 查询活跃的电票账户
     */
    List<TcEticketAccount> getActiveAccounts();

    /**
     * 根据业务范围查询账户列表
     */
    List<TcEticketAccount> getAccountsByBusinessScope(String businessScope);

    /**
     * 更新连接状态
     */
    void updateConnectionStatus(String id, String connectionStatus, String updateUser);

    /**
     * 更新电票状态
     */
    void updateEticketStatus(String id, String eticketStatus, String updateUser);

    /**
     * 批量删除电票账户
     */
    void batchDelete(List<String> ids);

    /**
     * 统计各银行电票账户数量
     */
    List<Map<String, Object>> getBankStatistics();

    /**
     * 统计各电票状态数量
     */
    List<Map<String, Object>> getEticketStatusStatistics();

    /**
     * 统计各连接状态数量
     */
    List<Map<String, Object>> getConnectionStatusStatistics();

    /**
     * 测试电票账户连接
     */
    boolean testConnection(String id);

    /**
     * 批量测试电票账户连接
     */
    Map<String, Boolean> batchTestConnection(List<String> ids);

    /**
     * 启用电票账户
     */
    void activateAccount(String id, String updateUser);

    /**
     * 停用电票账户
     */
    void deactivateAccount(String id, String updateUser);

    /**
     * 获取账户接口配置
     */
    Map<String, Object> getAccountInterfaceConfig(String id);

    /**
     * 更新账户接口配置
     */
    void updateAccountInterfaceConfig(String id, Map<String, Object> interfaceConfig, String updateUser);

    /**
     * 验证账户配置
     */
    boolean validateAccountConfig(TcEticketAccount account);

    /**
     * 分页查询电票账户（兼容IPage）
     */
    IPage<TcEticketAccount> getETicketAccountPage(Integer pageNo, Integer pageSize, String accountNo,
                                                   String accountName, String eTicketType, String accountType,
                                                   String bankId, String status);
}
