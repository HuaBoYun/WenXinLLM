package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.ArBadDebtProvisionEntity;
import com.financial.sharing.vo.param.ArBadDebtProvisionQueryParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 坏账准备Mapper接口
 * @author system
 * @since 2026-01-04
 */
public interface ArBadDebtProvisionMapper extends BaseMapper<ArBadDebtProvisionEntity> {

    /**
     * 分页查询坏账准备
     */
    IPage<ArBadDebtProvisionEntity> selectProvisionPage(Page<ArBadDebtProvisionEntity> page, 
                                                        @Param("param") ArBadDebtProvisionQueryParam param);

    /**
     * 查询坏账准备列表（配合PageHelper使用）
     */
    List<ArBadDebtProvisionEntity> selectProvisionList(@Param("param") ArBadDebtProvisionQueryParam param);

    /**
     * 根据客户ID查询坏账准备
     */
    List<ArBadDebtProvisionEntity> selectByCustomerId(@Param("customerId") String customerId, @Param("tenantId") Long tenantId);

    /**
     * 查询期间内的坏账准备
     */
    List<ArBadDebtProvisionEntity> selectByPeriod(@Param("startDate") LocalDate startDate, 
                                                   @Param("endDate") LocalDate endDate,
                                                   @Param("tenantId") Long tenantId);

    /**
     * 批量插入坏账准备
     */
    int batchInsert(@Param("provisions") List<ArBadDebtProvisionEntity> provisions);

    /**
     * 更新计提状态
     */
    int updateProvisionStatus(@Param("provisionId") String provisionId, @Param("status") Integer status);

    /**
     * 查询坏账准备统计
     */
    Map<String, Object> selectProvisionStatistics(@Param("param") ArBadDebtProvisionQueryParam param);

    /**
     * 查询坏账准备余额
     */
    BigDecimal selectProvisionBalance(@Param("tenantId") Long tenantId, @Param("asOfDate") LocalDate asOfDate);
}

