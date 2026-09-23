package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFundAllocation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资金下拨Mapper接口
 * @author Claude
 * @date 2026-01-20
 */
@Mapper
public interface TblFundAllocationMapper extends BaseMapper<TblFundAllocation> {

    int insertAllocation(TblFundAllocation allocation);
}

