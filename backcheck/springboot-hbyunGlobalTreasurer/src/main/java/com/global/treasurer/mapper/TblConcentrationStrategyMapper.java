package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblConcentrationStrategy;
import org.apache.ibatis.annotations.Mapper;

/**
 * 归集策略Mapper接口
 * @author Claude
 * @date 2026-01-20
 */
@Mapper
public interface TblConcentrationStrategyMapper extends BaseMapper<TblConcentrationStrategy> {
    int insertStrategy(TblConcentrationStrategy strategy);
}

