package com.global.treasurer.service;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblGtAccountInfo;

import java.util.List;
import java.util.Map;

/**
 * 全球司库-账户信息Service接口
 *
 * @author AI Developer
 * @since 2026-01-15
 */
public interface TblGtAccountInfoService extends IService<TblGtAccountInfo> {

    /**
     * 分页查询账户信息
     *
     * @param page    分页对象
     * @param orgId   机构ID
     * @param accountNumber   账户号码
     * @param accountName     账户名称
     * @param accountType     账户类型
     * @param bankCode        银行编码
     * @param currencyCode    币种代码
     * @param accountStatus   账户状态
     * @return 分页结果
     */
    IPage<TblGtAccountInfo> selectAccountPage(Page<TblGtAccountInfo> page,
                                              BigDecimal orgId,
                                              String accountNumber,
                                              String accountName,
                                              String accountType,
                                              String bankCode,
                                              String currencyCode,
                                              String accountStatus);

    /**
     * 查询账户统计数据
     *
     * @param orgId 机构ID
     * @return 统计数据
     */
    Map<String, Object> getAccountStats(BigDecimal orgId);

    /**
     * 查询余额概览
     *
     * @param orgId 机构ID
     * @return 余额概览
     */
    Map<String, Object> getBalanceOverview(BigDecimal orgId);

    /**
     * 查询账户状态统计
     *
     * @param orgId 机构ID
     * @return 状态统计
     */
    Map<String, Object> getStatusStats(BigDecimal orgId);

    /**
     * 查询银行分布
     *
     * @param orgId 机构ID
     * @return 银行分布列表
     */
    List<Map<String, Object>> getBankDistribution(BigDecimal orgId);

    /**
     * 激活账户
     *
     * @param accountId  账户ID
     * @param updateUser 更新人ID
     * @return 是否成功
     */
    boolean activateAccount(BigDecimal accountId, BigDecimal updateUser);

    /**
     * 冻结账户
     *
     * @param accountId  账户ID
     * @param updateUser 更新人ID
     * @return 是否成功
     */
    boolean freezeAccount(BigDecimal accountId, BigDecimal updateUser);

    /**
     * 解冻账户
     *
     * @param accountId  账户ID
     * @param updateUser 更新人ID
     * @return 是否成功
     */
    boolean unfreezeAccount(BigDecimal accountId, BigDecimal updateUser);

    /**
     * 关闭账户
     *
     * @param accountId  账户ID
     * @param updateUser 更新人ID
     * @return 是否成功
     */
    boolean closeAccount(BigDecimal accountId, BigDecimal updateUser);

    /**
     * 设置默认账户
     *
     * @param accountId     账户ID
     * @param currencyCode  币种代码
     * @param orgId         机构ID
     * @param updateUser    更新人ID
     * @return 是否成功
     */
    boolean setDefaultAccount(BigDecimal accountId, String currencyCode, BigDecimal orgId, BigDecimal updateUser);

    /**
     * 批量删除账户(软删除)
     *
     * @param accountIds 账户ID列表
     * @param userId     操作用户ID
     * @return 是否成功
     */
    boolean batchDeleteAccounts(List<BigDecimal> accountIds, BigDecimal userId);

    /**
     * 创建账户
     *
     * @param accountInfo 账户信息
     * @return 是否成功
     */
    boolean createAccount(TblGtAccountInfo accountInfo);

    /**
     * 更新账户信息
     *
     * @param accountInfo 账户信息
     * @return 是否成功
     */
    boolean updateAccountInfo(TblGtAccountInfo accountInfo);

    /**
     * 检查账户号码是否唯一
     *
     * @param accountNumber 账户号码
     * @param orgId         机构 ID
     * @param excludeId     排除的账户 ID（用于编辑时排除自身）
     * @return true-唯一/不存在，false-已存在
     */
    boolean checkAccountNumberUnique(String accountNumber, BigDecimal orgId, BigDecimal excludeId);

    /**
     * 查询账户列表（不分页，用于导出）
     *
     * @param orgId         机构ID
     * @param accountNumber 账户号码
     * @param accountName   账户名称
     * @param accountType   账户类型
     * @param bankCode      银行编码
     * @param currencyCode  币种代码
     * @param accountStatus 账户状态
     * @return 账户列表
     */
    List<TblGtAccountInfo> selectAccountList(BigDecimal orgId,
                                             String accountNumber,
                                             String accountName,
                                             String accountType,
                                             String bankCode,
                                             String currencyCode,
                                             String accountStatus);
}
