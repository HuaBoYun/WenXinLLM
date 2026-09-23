package com.global.treasurer.mapper;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblGtAccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 全球司库-账户信息Mapper接口
 *
 * @author AI Developer
 * @since 2026-01-15
 */
@Mapper
public interface TblGtAccountInfoMapper extends BaseMapper<TblGtAccountInfo> {

    /**
     * 分页查询账户信息列表
     *
     * @param orgId          机构ID
     * @param accountNumber  账户号码
     * @param accountName    账户名称
     * @param accountType    账户类型
     * @param bankCode       银行编码
     * @param currencyCode   币种代码
     * @param accountStatus  账户状态
     * @return 账户信息列表
     */
    @Select("<script>" +
            "SELECT * FROM TBL_GT_ACCOUNT_INFO " +
            "WHERE DELETE_FLAG = 0 " +
            "<if test='orgId != null'>" +
            "AND ORG_ID = #{orgId} " +
            "</if>" +
            "<if test='accountNumber != null and accountNumber != \"\"'>" +
            "AND ACCOUNT_NUMBER LIKE '%' || #{accountNumber} || '%' " +
            "</if>" +
            "<if test='accountName != null and accountName != \"\"'>" +
            "AND ACCOUNT_NAME LIKE '%' || #{accountName} || '%' " +
            "</if>" +
            "<if test='accountType != null and accountType != \"\"'>" +
            "AND ACCOUNT_TYPE = #{accountType} " +
            "</if>" +
            "<if test='bankCode != null and bankCode != \"\"'>" +
            "AND BANK_CODE = #{bankCode} " +
            "</if>" +
            "<if test='currencyCode != null and currencyCode != \"\"'>" +
            "AND CURRENCY_CODE = #{currencyCode} " +
            "</if>" +
            "<if test='accountStatus != null and accountStatus != \"\"'>" +
            "AND ACCOUNT_STATUS = #{accountStatus} " +
            "</if>" +
            "ORDER BY CREATE_TIME DESC" +
            "</script>")
    List<TblGtAccountInfo> selectAccountList(@Param("orgId") BigDecimal orgId,
                                            @Param("accountNumber") String accountNumber,
                                            @Param("accountName") String accountName,
                                            @Param("accountType") String accountType,
                                            @Param("bankCode") String bankCode,
                                            @Param("currencyCode") String currencyCode,
                                            @Param("accountStatus") String accountStatus);

    /**
     * 查询账户统计数据
     *
     * @param orgId 机构ID
     * @return 统计数据
     */
    @Select("SELECT " +
            "COUNT(*) as totalAccounts, " +
            "SUM(CASE WHEN ACCOUNT_STATUS = 'ACTIVE' THEN 1 ELSE 0 END) as activeAccounts, " +
            "SUM(COALESCE(BALANCE, 0)) as totalBalance " +
            "FROM TBL_GT_ACCOUNT_INFO " +
            "WHERE DELETE_FLAG = 0")
    Map<String, Object> selectAccountStats(@Param("orgId") BigDecimal orgId);

    /**
     * 查询余额概览
     *
     * @param orgId 机构ID
     * @return 余额概览数据
     */
    @Select("SELECT " +
            "SUM(COALESCE(AVAILABLE_BALANCE, 0)) as availableBalance, " +
            "SUM(COALESCE(FROZEN_BALANCE, 0)) as frozenBalance, " +
            "SUM(COALESCE(BALANCE, 0) - COALESCE(AVAILABLE_BALANCE, 0) - COALESCE(FROZEN_BALANCE, 0)) as transitBalance " +
            "FROM TBL_GT_ACCOUNT_INFO " +
            "WHERE DELETE_FLAG = 0")
    Map<String, Object> selectBalanceOverview(@Param("orgId") BigDecimal orgId);

    /**
     * 查询账户状态统计
     *
     * @param orgId 机构ID
     * @return 状态统计数据
     */
    @Select("SELECT " +
            "SUM(CASE WHEN ACCOUNT_STATUS = 'ACTIVE' THEN 1 ELSE 0 END) as active, " +
            "SUM(CASE WHEN ACCOUNT_STATUS = 'FROZEN' THEN 1 ELSE 0 END) as frozen, " +
            "SUM(CASE WHEN ACCOUNT_STATUS = 'CLOSED' THEN 1 ELSE 0 END) as closed " +
            "FROM TBL_GT_ACCOUNT_INFO " +
            "WHERE DELETE_FLAG = 0")
    Map<String, Object> selectStatusStats(@Param("orgId") BigDecimal orgId);

    /**
     * 查询银行分布
     *
     * @param orgId 机构ID
     * @return 银行分布列表
     */
    @Select("SELECT " +
            "BANK_CODE as bankCode, " +
            "BANK_NAME as bankName, " +
            "COUNT(*) as accountCount " +
            "FROM TBL_GT_ACCOUNT_INFO " +
            "WHERE DELETE_FLAG = 0 " +
            "GROUP BY BANK_CODE, BANK_NAME " +
            "ORDER BY accountCount DESC")
    List<Map<String, Object>> selectBankDistribution(@Param("orgId") BigDecimal orgId);

    /**
     * 取消默认账户标记
     *
     * @param orgId        机构ID
     * @param currencyCode 币种代码
     * @return 更新行数
     */
    @Update("UPDATE TBL_GT_ACCOUNT_INFO SET IS_DEFAULT = 0 " +
            "WHERE ORG_ID = #{orgId} AND CURRENCY_CODE = #{currencyCode} AND DELETE_FLAG = 0")
    int clearDefaultAccount(@Param("orgId") BigDecimal orgId, @Param("currencyCode") String currencyCode);

    /**
     * 设置默认账户
     *
     * @param accountId 账户ID
     * @return 更新行数
     */
    @Update("UPDATE TBL_GT_ACCOUNT_INFO SET IS_DEFAULT = 1 " +
            "WHERE ACCOUNT_ID = #{accountId} AND DELETE_FLAG = 0")
    int setDefaultAccount(@Param("accountId") BigDecimal accountId);

    /**
     * 检查账户号码是否唯一
     *
     * @param accountNumber 账户号码
     * @param orgId         机构 ID
     * @param excludeId     排除的账户 ID（用于编辑时排除自身）
     * @return 是否存在（true-唯一/不存在，false-已存在）
     */
    @Select("SELECT COUNT(*) FROM TBL_GT_ACCOUNT_INFO " +
            "WHERE ACCOUNT_NUMBER = #{accountNumber} " +
            "AND DELETE_FLAG = 0 " +
            "AND (#{orgId} IS NULL OR ORG_ID = #{orgId}) " +
            "AND (#{excludeId} IS NULL OR ACCOUNT_ID != #{excludeId})")
    int countByAccountNumber(@Param("accountNumber") String accountNumber,
                             @Param("orgId") BigDecimal orgId,
                             @Param("excludeId") BigDecimal excludeId);
}
