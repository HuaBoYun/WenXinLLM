package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblInternalLoan;
import org.apache.ibatis.annotations.Mapper;

/**
 * 内部借贷Mapper接口
 * @author Claude
 * @date 2026-01-20
 */
@Mapper
public interface TblInternalLoanMapper extends BaseMapper<TblInternalLoan> {

    /**
     * 自定义插入，显式指定 jdbcType，兼容达梦数据库 JDBC 驱动
     */
    int insertLoan(TblInternalLoan loan);
}

