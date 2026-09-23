package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFinancingBasicParams;
import org.apache.ibatis.annotations.Mapper;

/**
 * 融资配置Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-13
 */
@Mapper
public interface FinancingConfigMapper extends BaseMapper<TblFinancingBasicParams> {
}
