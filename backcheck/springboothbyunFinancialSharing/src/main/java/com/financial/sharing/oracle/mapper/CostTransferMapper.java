package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.CostTransferEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 成本结转 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2026-01-26
 */
@Component("oracleCostTransferMapper")
public interface CostTransferMapper extends BaseMapper<CostTransferEntity> {

    /**
     * 分页查询成本结转列表
     *
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<Map<String, Object>> selectTransferPage(Page<Map<String, Object>> page, @Param("param") Map<String, Object> param);

    /**
     * 根据ID查询结转详情
     *
     * @param param 查询参数（包含transferId和tenantId）
     * @return 结转详情
     */
    Map<String, Object> selectTransferById(@Param("param") Map<String, Object> param);

    /**
     * 根据结转单号查询结转记录
     *
     * @param transferNo 结转单号
     * @param tenantId 租户ID
     * @return 结转记录
     */
    CostTransferEntity selectByTransferNo(@Param("transferNo") String transferNo, @Param("tenantId") Long tenantId);

    /**
     * 根据结转期间查询结转列表
     *
     * @param transferPeriod 结转期间
     * @param tenantId 租户ID
     * @return 结转列表
     */
    List<CostTransferEntity> selectByPeriod(@Param("transferPeriod") String transferPeriod, @Param("tenantId") Long tenantId);

    /**
     * 根据结转类型查询结转列表
     *
     * @param transferType 结转类型
     * @param tenantId 租户ID
     * @return 结转列表
     */
    List<CostTransferEntity> selectByType(@Param("transferType") String transferType, @Param("tenantId") Long tenantId);

    /**
     * 根据结转状态查询结转列表
     *
     * @param transferStatus 结转状态
     * @param tenantId 租户ID
     * @return 结转列表
     */
    List<CostTransferEntity> selectByStatus(@Param("transferStatus") Integer transferStatus, @Param("tenantId") Long tenantId);

    /**
     * 批量执行成本结转
     *
     * @param transferIds 结转ID列表
     * @param transferStatus 结转状态
     * @param updaterId 更新人ID
     * @return 更新数量
     */
    int batchExecuteTransfer(@Param("transferIds") List<Long> transferIds,
                            @Param("transferStatus") Integer transferStatus,
                            @Param("updaterId") String updaterId);

    /**
     * 批量撤销成本结转
     *
     * @param transferIds 结转ID列表
     * @param updaterId 更新人ID
     * @return 更新数量
     */
    int batchRevokeTransfer(@Param("transferIds") List<Long> transferIds, @Param("updaterId") String updaterId);

    /**
     * 统计成本结转数据
     *
     * @param param 查询参数
     * @return 统计结果
     */
    Map<String, Object> countTransferStatistics(@Param("param") Map<String, Object> param);
}

