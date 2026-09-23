package com.financial.sharing.oracle.mapper;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 成本归集Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
public interface CostCollectionMapper {

    /**
     * 分页查询成本归集列表
     */
    List<Map<String, Object>> selectCostCollectionListWithPagination(@Param("bookId") Long bookId,
                                                                     @Param("tenantId") Long tenantId,
                                                                     @Param("collectionPeriod") String collectionPeriod,
                                                                     @Param("costCenterId") Long costCenterId,
                                                                     @Param("collectionType") Integer collectionType,
                                                                     @Param("collectionStatus") Integer collectionStatus,
                                                                     @Param("collectionNo") String collectionNo,
                                                                     @Param("offset") int offset,
                                                                     @Param("limit") int limit);

    /**
     * 查询成本归集列表总数
     */
    int countCostCollectionList(@Param("bookId") Long bookId,
                               @Param("tenantId") Long tenantId,
                               @Param("collectionPeriod") String collectionPeriod,
                               @Param("costCenterId") Long costCenterId,
                               @Param("collectionType") Integer collectionType,
                               @Param("collectionStatus") Integer collectionStatus,
                               @Param("collectionNo") String collectionNo);

    /**
     * 根据ID查询成本归集详情
     */
    Map<String, Object> selectCostCollectionDetail(@Param("collectionId") Long collectionId);

    /**
     * 查询成本归集明细列表
     */
    List<Map<String, Object>> selectCostCollectionDetailList(@Param("collectionId") Long collectionId);

    /**
     * 插入成本归集记录
     */
    int insertCostCollection(@Param("collectionId") Long collectionId,
                           @Param("collectionNo") String collectionNo,
                           @Param("collectionPeriod") String collectionPeriod,
                           @Param("costCenterId") Long costCenterId,
                           @Param("collectionType") Integer collectionType,
                           @Param("collectionAmount") BigDecimal collectionAmount,
                           @Param("collectionMethod") Integer collectionMethod,
                           @Param("collectionDate") String collectionDate,
                           @Param("bookId") Long bookId,
                           @Param("tenantId") Long tenantId,
                           @Param("creatorId") Long creatorId,
                           @Param("remark") String remark);

    /**
     * 更新成本归集记录
     */
    int updateCostCollection(@Param("collectionId") Long collectionId,
                           @Param("collectionStatus") Integer collectionStatus,
                           @Param("collectionAmount") BigDecimal collectionAmount,
                           @Param("collectionDate") String collectionDate,
                           @Param("updaterId") Long updaterId);

    /**
     * 审核成本归集记录
     */
    int auditCostCollection(@Param("collectionId") Long collectionId,
                          @Param("collectionStatus") Integer collectionStatus,
                          @Param("auditorId") Long auditorId,
                          @Param("auditorName") String auditorName,
                          @Param("auditTime") LocalDateTime auditTime,
                          @Param("auditRemark") String auditRemark,
                          @Param("updaterId") Long updaterId);

    /**
     * 删除成本归集记录（逻辑删除）
     */
    int deleteCostCollection(@Param("collectionId") Long collectionId, @Param("updaterId") Long updaterId);

    /**
     * 批量删除成本归集记录（逻辑删除）
     */
    int batchDeleteCostCollection(@Param("collectionIds") List<Long> collectionIds, @Param("updaterId") Long updaterId);

    /**
     * 批量审核成本归集记录
     */
    int batchAuditCostCollection(@Param("collectionIds") List<Long> collectionIds,
                               @Param("collectionStatus") Integer collectionStatus,
                               @Param("auditorId") Long auditorId,
                               @Param("auditorName") String auditorName,
                               @Param("auditTime") LocalDateTime auditTime,
                               @Param("auditRemark") String auditRemark,
                               @Param("updaterId") Long updaterId);

    /**
     * 获取成本归集统计数据
     */
    Map<String, Object> selectCostCollectionStats(@Param("period") String period, @Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 插入成本归集明细记录
     */
    int insertCostCollectionDetail(@Param("detailId") Long detailId,
                                 @Param("collectionId") Long collectionId,
                                 @Param("costElementCode") String costElementCode,
                                 @Param("costElementName") String costElementName,
                                 @Param("voucherId") Long voucherId,
                                 @Param("voucherNo") String voucherNo,
                                 @Param("accountCode") String accountCode,
                                 @Param("accountName") String accountName,
                                 @Param("originalAmount") BigDecimal originalAmount,
                                 @Param("allocationRate") BigDecimal allocationRate,
                                 @Param("allocatedAmount") BigDecimal allocatedAmount,
                                 @Param("departmentName") String departmentName,
                                 @Param("projectName") String projectName,
                                 @Param("businessType") String businessType,
                                 @Param("bookId") Long bookId,
                                 @Param("tenantId") Long tenantId,
                                 @Param("remark") String remark);

    /**
     * 删除成本归集明细记录（逻辑删除）
     */
    int deleteCostCollectionDetail(@Param("collectionId") Long collectionId);

    /**
     * 检查归集单号是否存在
     */
    int checkCollectionNoExists(@Param("collectionNo") String collectionNo, @Param("collectionId") Long collectionId,
                               @Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 根据期间和成本中心查询归集记录
     */
    List<Map<String, Object>> selectCostCollectionByPeriodAndCenter(@Param("collectionPeriod") String collectionPeriod,
                                                                   @Param("costCenterId") Long costCenterId,
                                                                   @Param("collectionType") Integer collectionType,
                                                                   @Param("bookId") Long bookId,
                                                                   @Param("tenantId") Long tenantId);

    /**
     * 根据期间查询各成本中心归集汇总
     */
    List<Map<String, Object>> selectCostCollectionSummaryByPeriod(@Param("collectionPeriod") String collectionPeriod,
                                                                @Param("bookId") Long bookId,
                                                                @Param("tenantId") Long tenantId);

    /**
     * 查询可用的成本中心列表
     */
    List<Map<String, Object>> selectAvailableCostCenters(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 验证成本中心是否存在
     */
    int validateCostCenterExists(@Param("costCenterId") Long costCenterId, @Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 查询成本归集列表用于导出
     */
    List<Map<String, Object>> selectCostCollectionListForExport(@Param("period") String period,
                                                                 @Param("bookId") Long bookId,
                                                                 @Param("tenantId") Long tenantId);
}