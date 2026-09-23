package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcPartnerBankAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 伙伴银行账户Mapper接口
 * 
 * @author AI Assistant
 * @date 2025-09-20
 */
@Mapper
public interface TcPartnerBankAccountMapper extends BaseMapper<TcPartnerBankAccount> {

    /**
     * 根据伙伴ID查询银行账户列表
     * 
     * @param partnerId 伙伴ID
     * @return 银行账户列表
     */
    List<TcPartnerBankAccount> selectByPartnerId(@Param("partnerId") String partnerId);

    /**
     * 根据银行代码查询账户列表
     * 
     * @param bankCode 银行代码
     * @return 账户列表
     */
    List<TcPartnerBankAccount> selectByBankCode(@Param("bankCode") String bankCode);

    /**
     * 根据账户号码查询账户信息
     * 
     * @param accountNumber 账户号码
     * @return 账户信息
     */
    TcPartnerBankAccount selectByAccountNumber(@Param("accountNumber") String accountNumber);

    /**
     * 检查账户号码是否存在
     * 
     * @param partnerId 伙伴ID
     * @param accountNumber 账户号码
     * @param bankCode 银行代码
     * @param excludeId 排除的ID
     * @return 存在数量
     */
    int checkAccountExists(@Param("partnerId") String partnerId, @Param("accountNumber") String accountNumber, 
                          @Param("bankCode") String bankCode, @Param("excludeId") String excludeId);

    /**
     * 根据账户状态查询账户列表
     * 
     * @param accountStatus 账户状态
     * @return 账户列表
     */
    List<TcPartnerBankAccount> selectByAccountStatus(@Param("accountStatus") String accountStatus);

    /**
     * 根据币种查询账户列表
     * 
     * @param currencyCode 币种代码
     * @return 账户列表
     */
    List<TcPartnerBankAccount> selectByCurrencyCode(@Param("currencyCode") String currencyCode);

    /**
     * 查询主账户列表
     * 
     * @param partnerId 伙伴ID
     * @return 主账户列表
     */
    List<TcPartnerBankAccount> selectPrimaryAccounts(@Param("partnerId") String partnerId);

    /**
     * 根据权限查询账户列表
     * 
     * @param partnerId 伙伴ID
     * @param permissionType 权限类型(RECEIVE-收款,PAY-付款,QUERY-查询,OPERATION-操作)
     * @return 账户列表
     */
    List<TcPartnerBankAccount> selectByPermission(@Param("partnerId") String partnerId, @Param("permissionType") String permissionType);

    /**
     * 批量更新账户状态
     * 
     * @param ids 账户ID列表
     * @param accountStatus 账户状态
     * @param updateUser 更新人
     * @return 更新数量
     */
    int batchUpdateAccountStatus(@Param("ids") List<String> ids, @Param("accountStatus") String accountStatus, @Param("updateUser") String updateUser);

    /**
     * 批量更新账户权限
     * 
     * @param ids 账户ID列表
     * @param permissionType 权限类型
     * @param permissionValue 权限值
     * @param updateUser 更新人
     * @return 更新数量
     */
    int batchUpdatePermission(@Param("ids") List<String> ids, @Param("permissionType") String permissionType, 
                             @Param("permissionValue") String permissionValue, @Param("updateUser") String updateUser);

    /**
     * 查询账户统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> selectAccountStatistics();

    /**
     * 根据账户类型查询账户列表
     * 
     * @param accountType 账户类型
     * @return 账户列表
     */
    List<TcPartnerBankAccount> selectByAccountType(@Param("accountType") String accountType);

    /**
     * 查询即将到期的账户
     * 
     * @param days 天数
     * @return 账户列表
     */
    List<TcPartnerBankAccount> selectExpiringAccounts(@Param("days") Integer days);

    /**
     * 多条件查询账户列表
     * 
     * @param params 查询参数
     * @return 账户列表
     */
    List<TcPartnerBankAccount> selectByMultipleConditions(@Param("params") Map<String, Object> params);

    /**
     * 设置主账户
     * 
     * @param partnerId 伙伴ID
     * @param accountId 账户ID
     * @param updateUser 更新人
     * @return 更新数量
     */
    int setPrimaryAccount(@Param("partnerId") String partnerId, @Param("accountId") String accountId, @Param("updateUser") String updateUser);
}
