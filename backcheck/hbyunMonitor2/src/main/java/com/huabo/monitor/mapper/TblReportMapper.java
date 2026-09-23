package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblReport;
import com.huabo.monitor.vo.param.TblReportParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-09-17
 */
public interface TblReportMapper extends BaseMapper<TblReport> {

	List<TblReport> findList(@Param("queryParam") TblReportParam queryParam, @Param("sql") String sql);

	
    @Select("${sql}")
    IPage<TblReport> getPage(IPage<TblReport> page, @Param("sql") String sql);
}
