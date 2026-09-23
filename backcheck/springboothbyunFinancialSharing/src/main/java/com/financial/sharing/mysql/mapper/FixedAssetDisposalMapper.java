package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.FixedAssetDisposalEntity;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 固定资产处置Mapper
 * @author system
 * @since 2026-01-21
 */
public interface FixedAssetDisposalMapper extends BaseMapper<FixedAssetDisposalEntity> {

    /**
     * 分页查询资产处置列表
     * @param disposalNo 处置单号
     * @param assetCode 资产编码
     * @param disposalType 处置类型
     * @param status 状态
     * @param tenantId 租户ID
     * @return 资产处置列表
     */
    List<FixedAssetDisposalEntity> selectPageList(@Param("disposalNo") String disposalNo,
                                                  @Param("assetCode") String assetCode,
                                                  @Param("disposalType") String disposalType,
                                                  @Param("status") String status,
                                                  @Param("tenantId") Long tenantId);

    /**
     * 查询资产处置详情
     * @param disposalId 处置ID
     * @param tenantId 租户ID
     * @return 资产处置详情
     */
    FixedAssetDisposalEntity selectDetailById(@Param("disposalId") String disposalId,
                                              @Param("tenantId") Long tenantId);

    /**
     * 统计资产处置汇总信息
     * @param tenantId 租户ID
     * @param disposalDateStart 开始日期
     * @param disposalDateEnd 结束日期
     * @return 统计数据
     */
    Map<String, Object> selectDisposalSummary(@Param("tenantId") Long tenantId,
                                              @Param("disposalDateStart") LocalDate disposalDateStart,
                                              @Param("disposalDateEnd") LocalDate disposalDateEnd);

    /**
     * 更新处置状态
     * @param disposalId 处置ID
     * @param status 状态
     * @param updateBy 更新人
     * @return 影响行数
     */
    int updateStatus(@Param("disposalId") String disposalId,
                    @Param("status") String status,
                    @Param("updateBy") String updateBy);
}

