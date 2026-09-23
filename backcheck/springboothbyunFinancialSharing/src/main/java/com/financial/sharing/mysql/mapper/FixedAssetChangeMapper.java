package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.FixedAssetChangeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 固定资产变动Mapper
 * @author system
 * @since 2026-01-21
 */
public interface FixedAssetChangeMapper extends BaseMapper<FixedAssetChangeEntity> {

    /**
     * 分页查询资产变动列表
     * @param changeNo 变动单号
     * @param assetCode 资产编码
     * @param changeType 变动类型
     * @param status 状态
     * @param tenantId 租户ID
     * @return 资产变动列表
     */
    List<FixedAssetChangeEntity> selectPageList(@Param("changeNo") String changeNo,
                                                @Param("assetCode") String assetCode,
                                                @Param("changeType") String changeType,
                                                @Param("status") String status,
                                                @Param("tenantId") Long tenantId);

    /**
     * 查询资产变动详情
     * @param changeId 变动ID
     * @param tenantId 租户ID
     * @return 资产变动详情
     */
    FixedAssetChangeEntity selectDetailById(@Param("changeId") String changeId,
                                           @Param("tenantId") Long tenantId);

    /**
     * 统计资产变动数据
     * @param tenantId 租户ID
     * @return 统计数据
     */
    Map<String, Object> selectStatistics(@Param("tenantId") Long tenantId);

    /**
     * 生成变动单号
     * @param tenantId 租户ID
     * @return 变动单号
     */
    String generateChangeNo(@Param("tenantId") Long tenantId);

    /**
     * 批量更新变动状态
     * @param changeIds 变动ID列表
     * @param status 状态
     * @param tenantId 租户ID
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("changeIds") List<String> changeIds,
                         @Param("status") String status,
                         @Param("tenantId") Long tenantId);
}

