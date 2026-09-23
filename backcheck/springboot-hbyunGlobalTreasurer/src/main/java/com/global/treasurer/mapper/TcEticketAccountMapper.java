package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TcEticketAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 财资公共模块 - 电票账户设置管理Mapper
 *
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
public interface TcEticketAccountMapper extends BaseMapper<TcEticketAccount> {

    /**
     * 根据银行代码查询
     * 
     * @param bankCode 银行代码
     * @return 电票账户列表
     */
    List<TcEticketAccount> selectByBankCode(@Param("bankCode") String bankCode);

    /**
     * 根据银行账户ID查询
     * 
     * @param bankAccountId 银行账户ID
     * @return 电票账户信息
     */
    TcEticketAccount selectByBankAccountId(@Param("bankAccountId") String bankAccountId);

    /**
     * 根据客户号查询
     * 
     * @param customerNo 客户号
     * @return 电票账户信息
     */
    TcEticketAccount selectByCustomerNo(@Param("customerNo") String customerNo);

    /**
     * 根据条件查询电票账户列表
     * 
     * @param bankCode 银行代码
     * @param bankName 银行名称
     * @param accountNo 账户号码
     * @param customerNo 客户号
     * @param eticketStatus 电票状态
     * @param connectionStatus 连接状态
     * @param status 状态
     * @return 电票账户列表
     */
    List<TcEticketAccount> selectByCondition(@Param("bankCode") String bankCode,
                                             @Param("bankName") String bankName,
                                             @Param("accountNo") String accountNo,
                                             @Param("customerNo") String customerNo,
                                             @Param("eticketStatus") String eticketStatus,
                                             @Param("connectionStatus") String connectionStatus,
                                             @Param("status") String status);

    /**
     * 根据电票状态查询
     * 
     * @param eticketStatus 电票状态
     * @return 电票账户列表
     */
    List<TcEticketAccount> selectByEticketStatus(@Param("eticketStatus") String eticketStatus);

    /**
     * 根据连接状态查询
     * 
     * @param connectionStatus 连接状态
     * @return 电票账户列表
     */
    List<TcEticketAccount> selectByConnectionStatus(@Param("connectionStatus") String connectionStatus);

    /**
     * 查询活跃的电票账户
     * 
     * @return 活跃的电票账户列表
     */
    List<TcEticketAccount> selectActiveAccounts();

    /**
     * 根据业务范围查询
     * 
     * @param businessScope 业务范围
     * @return 电票账户列表
     */
    List<TcEticketAccount> selectByBusinessScope(@Param("businessScope") String businessScope);

    /**
     * 更新连接状态
     * 
     * @param id 账户ID
     * @param connectionStatus 连接状态
     * @param lastConnectTime 最后连接时间
     * @return 更新数量
     */
    int updateConnectionStatus(@Param("id") String id,
                               @Param("connectionStatus") String connectionStatus,
                               @Param("lastConnectTime") String lastConnectTime);

    /**
     * 更新电票状态
     * 
     * @param id 账户ID
     * @param eticketStatus 电票状态
     * @param updateUser 更新人
     * @return 更新数量
     */
    int updateEticketStatus(@Param("id") String id,
                            @Param("eticketStatus") String eticketStatus,
                            @Param("updateUser") String updateUser);

    /**
     * 统计各银行电票账户数量
     * 
     * @return 银行统计结果
     */
    List<java.util.Map<String, Object>> countByBankCode();

    /**
     * 统计各电票状态数量
     * 
     * @return 电票状态统计结果
     */
    List<java.util.Map<String, Object>> countByEticketStatus();

    /**
     * 统计各连接状态数量
     *
     * @return 连接状态统计结果
     */
    List<java.util.Map<String, Object>> countByConnectionStatus();

    /**
     * 根据条件查询电票账户列表(用于分页)
     *
     * @param accountNo 账户号码
     * @param accountName 账户名称
     * @param eTicketType 电票类型
     * @param accountType 账户类型
     * @param bankCode 银行代码
     * @param status 状态
     * @return 电票账户列表
     */
    List<TcEticketAccount> selectListWithConditions(@Param("accountNo") String accountNo,
                                                     @Param("accountName") String accountName,
                                                     @Param("eTicketType") String eTicketType,
                                                     @Param("accountType") String accountType,
                                                     @Param("bankCode") String bankCode,
                                                     @Param("status") String status);
}
