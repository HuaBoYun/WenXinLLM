package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.ArReceivableEntity;
import com.financial.sharing.vo.param.ArReceivableQueryParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 应收单据Mapper接口
 * @author system
 * @since 2026-01-04
 */
public interface ArReceivableMapper extends BaseMapper<ArReceivableEntity> {

    /**
     * 根据ID更新应收单据（自定义方法）
     */
    int updateReceivableById(ArReceivableEntity entity);

    /**
     * 分页查询应收单据
     */
    IPage<ArReceivableEntity> selectReceivablePage(Page<ArReceivableEntity> page, @Param("param") ArReceivableQueryParam param);

    /**
     * 查询应收单据列表（配合PageHelper使用）
     */
    List<ArReceivableEntity> selectReceivableList(@Param("param") ArReceivableQueryParam param);

    /**
     * 根据单据编号查询
     */
    ArReceivableEntity selectByDocumentNo(@Param("documentNo") String documentNo, @Param("tenantId") Long tenantId);

    /**
     * 查询客户应收单据
     */
    List<ArReceivableEntity> selectByCustomerId(@Param("customerId") String customerId, @Param("tenantId") Long tenantId);

    /**
     * 查询待核销的应收单据
     */
    List<ArReceivableEntity> selectPendingWriteOff(@Param("customerId") String customerId, @Param("tenantId") Long tenantId);

    /**
     * 更新已收金额
     */
    int updateReceivedAmount(@Param("receivableId") String receivableId, @Param("amount") BigDecimal amount);

    /**
     * 批量更新单据状态
     */
    int batchUpdateStatus(@Param("receivableIds") List<String> receivableIds, 
                          @Param("status") Integer status,
                          @Param("auditorId") String auditorId,
                          @Param("auditComments") String auditComments);

    /**
     * 查询逾期应收单据
     */
    List<ArReceivableEntity> selectOverdueReceivables(@Param("tenantId") Long tenantId, @Param("currentDate") LocalDate currentDate);

    /**
     * 查询应收统计
     */
    Map<String, Object> selectReceivableStatistics(@Param("param") ArReceivableQueryParam param);

    /**
     * 按业务类型统计
     */
    List<Map<String, Object>> selectStatisticsByBusinessType(@Param("param") ArReceivableQueryParam param);

    /**
     * 按客户统计
     */
    List<Map<String, Object>> selectStatisticsByCustomer(@Param("param") ArReceivableQueryParam param);

    /**
     * 生成单据编号
     */
    String generateDocumentNo(@Param("prefix") String prefix, @Param("tenantId") Long tenantId);

    // ==================== 应收分析统计方法 ====================

    /**
     * 查询应收总览数据
     */
    Map<String, Object> selectReceivableOverview(@Param("tenantId") Long tenantId);

    /**
     * 查询应收结构分析 - 按客户分布
     */
    List<Map<String, Object>> selectReceivableByCustomer(@Param("tenantId") Long tenantId, @Param("topN") Integer topN);

    /**
     * 查询应收结构分析 - 按账龄分布
     */
    List<Map<String, Object>> selectReceivableByAging(@Param("tenantId") Long tenantId, @Param("analysisDate") LocalDate analysisDate);

    /**
     * 查询应收结构分析 - 按业务类型分布
     */
    List<Map<String, Object>> selectReceivableByBusinessType(@Param("tenantId") Long tenantId);

    /**
     * 查询客户应收排名
     */
    List<Map<String, Object>> selectCustomerReceivableRanking(@Param("tenantId") Long tenantId,
                                                               @Param("rankingType") String rankingType,
                                                               @Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate,
                                                               @Param("topN") Integer topN);

    /**
     * 查询收款效率指标
     */
    Map<String, Object> selectCollectionEfficiencyMetrics(@Param("tenantId") Long tenantId,
                                                           @Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate);

    /**
     * 查询月度收款效率明细
     */
    List<Map<String, Object>> selectMonthlyCollectionEfficiency(@Param("tenantId") Long tenantId,
                                                                  @Param("startDate") LocalDate startDate,
                                                                  @Param("endDate") LocalDate endDate);
}

