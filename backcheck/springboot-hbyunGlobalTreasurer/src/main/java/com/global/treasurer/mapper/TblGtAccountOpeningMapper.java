package com.global.treasurer.mapper;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblGtAccountOpening;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

/**
 * 全球司库-开户申请Mapper接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@Mapper
public interface TblGtAccountOpeningMapper extends BaseMapper<TblGtAccountOpening> {

    /**
     * 分页查询开户申请列表
     *
     * @param accountName 账户名称
     * @param applicationStatus 申请状态
     * @param orgId 机构ID
     * @return 开户申请列表
     */
    List<TblGtAccountOpening> selectPageList(
            @Param("applicationNo") String applicationNo,
            @Param("accountName") String accountName,
            @Param("applicationStatus") String applicationStatus,
            @Param("bankCode") String bankCode,
            @Param("orgId") BigDecimal orgId
    );

    /**
     * 按状态统计开户申请数量
     *
     * @param orgId 机构ID
     * @return 各状态数量 Map
     */
    @Select("<script>" +
            "SELECT " +
            "SUM(CASE WHEN APPLICATION_STATUS = 'PENDING' THEN 1 ELSE 0 END) AS pendingCount, " +
            "SUM(CASE WHEN APPLICATION_STATUS = 'APPROVED' THEN 1 ELSE 0 END) AS approvedCount, " +
            "SUM(CASE WHEN APPLICATION_STATUS = 'REJECTED' THEN 1 ELSE 0 END) AS rejectedCount, " +
            "SUM(CASE WHEN APPLICATION_STATUS = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelledCount, " +
            "COUNT(*) AS totalCount " +
            "FROM TBL_GT_ACCOUNT_OPENING " +
            "<where>" +
            "<if test='orgId != null'>AND ORG_ID = #{orgId}</if>" +
            "</where>" +
            "</script>")
    Map<String, Object> selectStatusStatistics(@Param("orgId") BigDecimal orgId);
}
