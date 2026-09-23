package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.TblAssetQuality;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资产质量评估 Mapper接口
 */
@Mapper
public interface AssetQualityMapper extends BaseMapper<TblAssetQuality> {
}
