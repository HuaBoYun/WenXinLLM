package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.MonitorSolutionresult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.MonitorSolutionresultVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
public interface MonitorSolutionresultMapper extends BaseMapper<MonitorSolutionresult> {






    @Select("${sql}")
    <p extends IPage<MonitorSolutionresultVo>>  p getSolutionresultVoPage(p page ,@Param("sql") String sql );




}
