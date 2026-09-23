package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFundPool;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资金池Mapper接口
 * @author Claude
 * @date 2026-01-20
 */
@Mapper
public interface TblFundPoolMapper extends BaseMapper<TblFundPool> {

    /**
     * 自定义insert，明确指定jdbcType解决达梦数据库类型映射问题
     */
    int insertFundPool(TblFundPool fundPool);
}

