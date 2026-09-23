package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcVirtualAccount;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * 财资公共模块 - 虚拟账户管理Mapper
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
public interface TcVirtualAccountMapper extends Mapper<TcVirtualAccount> {

    /**
     * 根据账户编码查询
     * 
     * @param accountCode 账户编码
     * @return 虚拟账户信息
     */
    TcVirtualAccount selectByAccountCode(@Param("accountCode") String accountCode);

    /**
     * 根据账户类型查询
     * 
     * @param accountType 账户类型
     * @return 虚拟账户列表
     */
    List<TcVirtualAccount> selectByAccountType(@Param("accountType") String accountType);

    /**
     * 根据条件查询虚拟账户列表
     * 
     * @param accountCode 账户编码
     * @param accountName 账户名称
     * @param accountType 账户类型
     * @param platformName 平台名称
     * @param currencyCode 币种代码
     * @param syncStatus 同步状态
     * @param status 状态
     * @return 虚拟账户列表
     */
    List<TcVirtualAccount> selectByCondition(@Param("accountCode") String accountCode,
                                             @Param("accountName") String accountName,
                                             @Param("accountType") String accountType,
                                             @Param("platformName") String platformName,
                                             @Param("currencyCode") String currencyCode,
                                             @Param("syncStatus") String syncStatus,
                                             @Param("status") String status);

    /**
     * 根据平台名称查询
     * 
     * @param platformName 平台名称
     * @return 虚拟账户列表
     */
    List<TcVirtualAccount> selectByPlatformName(@Param("platformName") String platformName);

    /**
     * 根据币种查询
     * 
     * @param currencyCode 币种代码
     * @return 虚拟账户列表
     */
    List<TcVirtualAccount> selectByCurrencyCode(@Param("currencyCode") String currencyCode);

    /**
     * 根据同步状态查询
     * 
     * @param syncStatus 同步状态
     * @return 虚拟账户列表
     */
    List<TcVirtualAccount> selectBySyncStatus(@Param("syncStatus") String syncStatus);

    /**
     * 查询需要同步的账户
     * 
     * @return 需要同步的虚拟账户列表
     */
    List<TcVirtualAccount> selectAccountsForSync();

    /**
     * 更新账户余额
     * 
     * @param id 账户ID
     * @param balance 余额
     * @param availableBalance 可用余额
     * @param frozenBalance 冻结余额
     * @param syncStatus 同步状态
     * @param updateUser 更新人
     * @return 更新数量
     */
    int updateBalance(@Param("id") String id,
                      @Param("balance") BigDecimal balance,
                      @Param("availableBalance") BigDecimal availableBalance,
                      @Param("frozenBalance") BigDecimal frozenBalance,
                      @Param("syncStatus") String syncStatus,
                      @Param("updateUser") String updateUser);

    /**
     * 更新同步状态
     * 
     * @param id 账户ID
     * @param syncStatus 同步状态
     * @param lastSyncTime 最后同步时间
     * @return 更新数量
     */
    int updateSyncStatus(@Param("id") String id,
                         @Param("syncStatus") String syncStatus,
                         @Param("lastSyncTime") String lastSyncTime);

    /**
     * 统计各账户类型数量
     * 
     * @return 账户类型统计结果
     */
    List<java.util.Map<String, Object>> countByAccountType();

    /**
     * 统计各同步状态数量
     * 
     * @return 同步状态统计结果
     */
    List<java.util.Map<String, Object>> countBySyncStatus();

    /**
     * 计算总余额
     * 
     * @param accountType 账户类型
     * @param currencyCode 币种代码
     * @return 总余额
     */
    BigDecimal sumBalanceByTypeAndCurrency(@Param("accountType") String accountType,
                                           @Param("currencyCode") String currencyCode);
}
