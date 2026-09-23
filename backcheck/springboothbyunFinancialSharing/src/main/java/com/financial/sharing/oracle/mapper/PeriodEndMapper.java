package com.financial.sharing.oracle.mapper;

import com.financial.sharing.vo.result.PeriodEndClosingResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 期末处理Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
public interface PeriodEndMapper {

    /**
     * 获取期末检查项配置
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 检查项列表
     */
    List<Map<String, Object>> getCheckItemConfig(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 检查凭证完整性
     *
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 检查结果
     */
    Map<String, Object> checkVoucherIntegrity(@Param("accountingPeriod") String accountingPeriod,
                                                @Param("bookId") Long bookId,
                                                @Param("tenantId") Long tenantId);

    /**
     * 检查科目余额平衡
     *
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 检查结果
     */
    Map<String, Object> checkSubjectBalance(@Param("accountingPeriod") String accountingPeriod,
                                             @Param("bookId") Long bookId,
                                             @Param("tenantId") Long tenantId);

    /**
     * 检查银行对账情况
     *
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 检查结果
     */
    Map<String, Object> checkBankReconciliation(@Param("accountingPeriod") String accountingPeriod,
                                                  @Param("bookId") Long bookId,
                                                  @Param("tenantId") Long tenantId);

    /**
     * 检查往来款项
     *
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 检查结果
     */
    Map<String, Object> checkARAPBalance(@Param("accountingPeriod") String accountingPeriod,
                                          @Param("bookId") Long bookId,
                                          @Param("tenantId") Long tenantId);

    /**
     * 插入期末检查记录
     *
     * @param checkRecord 检查记录
     * @return 影响行数
     */
    int insertCheckRecord(Map<String, Object> checkRecord);

    /**
     * 更新期末检查记录
     *
     * @param checkRecord 检查记录
     * @return 影响行数
     */
    int updateCheckRecord(Map<String, Object> checkRecord);

    /**
     * 获取期间结账状态
     *
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @return 结账状态
     */
    String getPeriodClosingStatus(@Param("accountingPeriod") String accountingPeriod,
                                        @Param("bookId") Long bookId);

    /**
     * 插入结账记录
     *
     * @param closingRecord 结账记录
     * @return 影响行数
     */
    int insertClosingRecord(Map<String, Object> closingRecord);

    /**
     * 更新结账记录
     *
     * @param closingRecord 结账记录
     * @return 影响行数
     */
    int updateClosingRecord(Map<String, Object> closingRecord);

    /**
     * 获取结账历史记录
     *
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 历史记录列表
     */
    List<PeriodEndClosingResult> getClosingHistory(@Param("accountingPeriod") String accountingPeriod,
                                                    @Param("bookId") Long bookId,
                                                    @Param("pageNum") Integer pageNum,
                                                    @Param("pageSize") Integer pageSize);

    /**
     * 获取结账历史记录总数
     *
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @return 总记录数
     */
    Integer getClosingHistoryCount(@Param("accountingPeriod") String accountingPeriod,
                                            @Param("bookId") Long bookId);

    /**
     * 获取可用会计期间
     *
     * @param bookId 账簿ID
     * @return 期间列表
     */
    List<String> getAvailablePeriods(@Param("bookId") Long bookId);

    /**
     * 检查期间是否已结账
     *
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @return 是否已结账
     */
    Boolean isPeriodClosed(@Param("accountingPeriod") String accountingPeriod,
                               @Param("bookId") Long bookId);
}