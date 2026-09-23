package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.FixedAssetRightOfUseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 使用权资产Mapper
 * @author system
 * @since 2026-01-21
 */
public interface FixedAssetRightOfUseMapper extends BaseMapper<FixedAssetRightOfUseEntity> {

    /**
     * 分页查询使用权资产列表
     * @param assetId 资产ID
     * @param leaseContractNo 租赁合同号
     * @param tenantId 租户ID
     * @return 使用权资产列表
     */
    List<FixedAssetRightOfUseEntity> selectPageList(@Param("assetId") String assetId,
                                                    @Param("leaseContractNo") String leaseContractNo,
                                                    @Param("tenantId") Long tenantId);
}

