package com.financial.sharing.oracle.mapper;

import com.financial.sharing.oracle.entity.CostAllocationEntity;
import com.financial.sharing.oracle.entity.CostAllocationDetailEntity;
import com.financial.sharing.vo.param.CostAllocationQueryParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 成本分摊Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-06
 */
public interface CostAllocationMapper {

    // ==================== 主表操作 ====================

    /**
     * 插入成本分摊主记录
     *
     * @param entity 成本分摊实体
     * @return 影响行数
     */
    int insertCostAllocation(CostAllocationEntity entity);

    /**
     * 根据ID查询成本分摊主记录
     *
     * @param allocationId 分摊ID
     * @return 成本分摊实体
     */
    CostAllocationEntity selectCostAllocationById(@Param("allocationId") Long allocationId);

    /**
     * 根据分摊单号查询成本分摊主记录
     *
     * @param allocationNo 分摊单号
     * @return 成本分摊实体
     */
    CostAllocationEntity selectCostAllocationByNo(@Param("allocationNo") String allocationNo);

    /**
     * 分页查询成本分摊列表（返回Map格式，便于前端显示）
     *
     * @param param 查询参数
     * @param offset 分页偏移量
     * @param pageSize 页面大小
     * @return 成本分摊列表
     */
    List<Map<String, Object>> selectCostAllocationPage(@Param("param") CostAllocationQueryParam param,
                                                      @Param("offset") Integer offset,
                                                      @Param("pageSize") Integer pageSize);

    /**
     * 统计成本分摊总数
     *
     * @param param 查询参数
     * @return 总记录数
     */
    Long selectCostAllocationCount(@Param("param") CostAllocationQueryParam param);

    /**
     * 更新成本分摊主记录
     *
     * @param entity 成本分摊实体
     * @return 影响行数
     */
    int updateCostAllocation(CostAllocationEntity entity);

    /**
     * 批量更新分摊状态
     *
     * @param allocationIds 分摊ID列表
     * @param status 新状态
     * @param updater 更新人
     * @return 影响行数
     */
    int batchUpdateAllocationStatus(@Param("allocationIds") List<Long> allocationIds,
                                    @Param("status") Integer status,
                                    @Param("updater") Long updater);

    /**
     * 批量更新成本分摊状态和日期
     *
     * @param allocationIds 分摊ID列表
     * @param status 新状态
     * @param allocationDate 分摊日期
     * @param updater 更新人
     * @return 影响行数
     */
    int batchUpdateAllocationStatusWithDate(@Param("allocationIds") List<Long> allocationIds,
                                           @Param("status") Integer status,
                                           @Param("allocationDate") String allocationDate,
                                           @Param("updater") Long updater);

    /**
     * 软删除成本分摊主记录
     *
     * @param allocationId 分摊ID
     * @param updater 更新人
     * @return 影响行数
     */
    int deleteCostAllocationById(@Param("allocationId") Long allocationId,
                                @Param("updater") Long updater);

    /**
     * 批量软删除成本分摊主记录
     *
     * @param allocationIds 分摊ID列表
     * @param updater 更新人
     * @return 影响行数
     */
    int batchDeleteCostAllocation(@Param("allocationIds") List<Long> allocationIds,
                                 @Param("updater") Long updater);

    // ==================== 明细表操作 ====================

    /**
     * 插入成本分摊明细记录
     *
     * @param entity 成本分摊明细实体
     * @return 影响行数
     */
    int insertCostAllocationDetail(CostAllocationDetailEntity entity);

    /**
     * 批量插入成本分摊明细记录
     *
     * @param details 明细实体列表
     * @return 影响行数
     */
    int batchInsertAllocationDetails(@Param("details") List<CostAllocationDetailEntity> details);

    /**
     * 根据分摊ID查询明细列表
     *
     * @param allocationId 分摊ID
     * @return 明细列表
     */
    List<CostAllocationDetailEntity> selectDetailsByAllocationId(@Param("allocationId") Long allocationId);

    /**
     * 根据ID查询明细记录
     *
     * @param detailId 明细ID
     * @return 明细实体
     */
    CostAllocationDetailEntity selectDetailById(@Param("detailId") Long detailId);

    /**
     * 分页查询成本分摊明细列表（返回Map格式，便于前端显示）
     *
     * @param allocationId 分摊ID
     * @param offset 分页偏移量
     * @param pageSize 页面大小
     * @return 明细列表
     */
    List<Map<String, Object>> selectAllocationDetailPage(@Param("allocationId") Long allocationId,
                                                         @Param("offset") Integer offset,
                                                         @Param("pageSize") Integer pageSize);

    /**
     * 统计明细总数
     *
     * @param allocationId 分摊ID
     * @return 总记录数
     */
    Long selectAllocationDetailCount(@Param("allocationId") Long allocationId);

    /**
     * 更新成本分摊明细记录
     *
     * @param entity 明细实体
     * @return 影响行数
     */
    int updateCostAllocationDetail(CostAllocationDetailEntity entity);

    /**
     * 删除成本分摊明细记录（根据分摊ID）
     *
     * @param allocationId 分摊ID
     * @param updater 更新人
     * @return 影响行数
     */
    int deleteDetailsByAllocationId(@Param("allocationId") Long allocationId,
                                    @Param("updater") Long updater);

    // ==================== 统计查询 ====================

    /**
     * 查询成本分摊统计信息
     *
     * @param param 查询参数
     * @return 统计信息
     */
    Map<String, Object> selectAllocationStats(@Param("param") CostAllocationQueryParam param);

    /**
     * 查询成本分摊概览数据
     *
     * @param period 分摊期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 概览数据
     */
    Map<String, Object> selectAllocationOverview(@Param("period") String period,
                                                @Param("bookId") String bookId,
                                                @Param("tenantId") String tenantId);

    /**
     * 查询指定期间的分摊总额
     *
     * @param period 分摊期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 分摊总额
     */
    BigDecimal selectTotalAmountByPeriod(@Param("period") String period,
                                        @Param("bookId") String bookId,
                                        @Param("tenantId") String tenantId);

    /**
     * 查询各分摊方法的统计数量
     *
     * @param param 查询参数
     * @return 各方法的统计数量
     */
    List<Map<String, Object>> selectAllocationMethodStats(@Param("param") CostAllocationQueryParam param);

    /**
     * 查询各状态的统计数量
     *
     * @param param 查询参数
     * @return 各状态的统计数量
     */
    List<Map<String, Object>> selectAllocationStatusStats(@Param("param") CostAllocationQueryParam param);

    // ==================== 复杂查询 ====================

    /**
     * 查询成本中心分摊历史
     *
     * @param centerId 成本中心ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param limit 限制数量
     * @return 分摊历史
     */
    List<Map<String, Object>> selectCenterAllocationHistory(@Param("centerId") String centerId,
                                                           @Param("bookId") String bookId,
                                                           @Param("tenantId") String tenantId,
                                                           @Param("limit") Integer limit);

    /**
     * 查询成本分摊趋势数据
     *
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 趋势数据
     */
    List<Map<String, Object>> selectAllocationTrend(@Param("startPeriod") String startPeriod,
                                                   @Param("endPeriod") String endPeriod,
                                                   @Param("bookId") String bookId,
                                                   @Param("tenantId") String tenantId);

    /**
     * 查询源成本中心的分摊去向
     *
     * @param sourceCenterId 源成本中心ID
     * @param period 分摊期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 分摊去向数据
     */
    List<Map<String, Object>> selectSourceCenterAllocation(@Param("sourceCenterId") String sourceCenterId,
                                                          @Param("period") String period,
                                                          @Param("bookId") String bookId,
                                                          @Param("tenantId") String tenantId);

    /**
     * 查询目标成本中心的分摊来源
     *
     * @param targetCenterId 目标成本中心ID
     * @param period 分摊期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 分摊来源数据
     */
    List<Map<String, Object>> selectTargetCenterAllocation(@Param("targetCenterId") String targetCenterId,
                                                          @Param("period") String period,
                                                          @Param("bookId") String bookId,
                                                          @Param("tenantId") String tenantId);

    // ==================== 验证查询 ====================

    /**
     * 检查分摊单号是否存在
     *
     * @param allocationNo 分摊单号
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 存在记录数
     */
    int checkAllocationNoExists(@Param("allocationNo") String allocationNo,
                               @Param("bookId") String bookId,
                               @Param("tenantId") String tenantId);

    /**
     * 检查成本中心是否存在分摊记录
     *
     * @param centerId 成本中心ID
     * @param period 分摊期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 记录数
     */
    int checkCenterAllocationExists(@Param("centerId") String centerId,
                                   @Param("period") String period,
                                   @Param("bookId") String bookId,
                                   @Param("tenantId") String tenantId);

    /**
     * 检查成本中心ID是否存在（外键约束要求）
     *
     * @param centerIds 成本中心ID列表
     * @return 存在的成本中心ID列表
     */
    List<Map<String, Object>> checkCostCenterIds(@Param("centerIds") List<String> centerIds);

    // ==================== 维护操作 ====================

    /**
     * 清理指定日期之前的已删除数据
     *
     * @param beforeDate 指定日期
     * @return 清理的记录数
     */
    int cleanupDeletedData(@Param("beforeDate") String beforeDate);

    /**
     * 重建索引统计信息
     */
    void rebuildIndexStats();

    /**
     * 更新分摊记录的凭证ID
     *
     * @param allocationId 分摊ID
     * @param voucherId 凭证ID
     * @param updater 更新人
     * @return 影响行数
     */
    int updateVoucherId(@Param("allocationId") Long allocationId,
                       @Param("voucherId") Long voucherId,
                       @Param("updater") Long updater);

    /**
     * 查询成本分摊列表用于导出
     */
    List<Map<String, Object>> selectCostAllocationListForExport(@Param("allocationPeriod") String allocationPeriod,
                                                                 @Param("bookId") Long bookId,
                                                                 @Param("tenantId") Long tenantId);
}