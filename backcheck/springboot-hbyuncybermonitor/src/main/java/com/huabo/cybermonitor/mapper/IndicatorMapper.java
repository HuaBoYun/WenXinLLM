package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.Indicator;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
public interface IndicatorMapper extends BaseMapper<Indicator> {

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = ( SELECT orgid FROM TBL_ORGANIZATION WHERE ORGNAME = '行业' AND ORGTYPE = 100 ) limit 1")
    List<Organization> QueryFATHERORGID();

    @Select("select * from TBL_INDICATOR where INDICATORID in (select INDICATORID from TBL_MONITOR_SOLUTION_INDICATOR where SOLUTIONID = #{id})")
    List<Indicator> showAllWithYj(@Param("id") String id);

    @Select("SELECT COUNT(*) from TBL_INDICATOR  where INDICATORCODE = #{number} ")
    Integer listBySqlPageCount(@Param("number") String number);
}
