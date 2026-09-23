package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblBondIssuance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 债券发行Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface BondIssuanceMapper extends BaseMapper<TblBondIssuance> {

    List<TblBondIssuance> selectIssuanceList(Map<String, Object> params);

    TblBondIssuance selectIssuanceById(@Param("issuanceId") Long issuanceId);

    int updateIssuanceStatus(@Param("issuanceId") Long issuanceId, @Param("status") String status);

    int batchDeleteByIds(@Param("issuanceIds") List<Long> issuanceIds);

    List<TblBondIssuance> selectExpiringBonds(@Param("days") Integer days);

    Map<String, Object> selectIssuanceSummary(@Param("companyId") Long companyId);

    /**
     * 查询债券类型分布
     */
    List<Map<String, Object>> selectBondTypeDistribution(@Param("companyId") Long companyId);

    /**
     * 查询债券发行趋势
     */
    List<Map<String, Object>> selectIssuanceTrend(@Param("companyId") Long companyId, @Param("months") Integer months);

    /**
     * 查询所有债券用于导出
     */
    List<TblBondIssuance> selectAllForExport(Map<String, Object> params);
}

