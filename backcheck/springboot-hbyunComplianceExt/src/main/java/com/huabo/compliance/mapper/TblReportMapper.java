package com.huabo.compliance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.TblReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-09-17
 */
public interface TblReportMapper extends BaseMapper<TblReport> {


    @Select("${sql}")
    IPage<TblReport> getPage(IPage<TblReport> page, @Param("sql") String sql);
}
