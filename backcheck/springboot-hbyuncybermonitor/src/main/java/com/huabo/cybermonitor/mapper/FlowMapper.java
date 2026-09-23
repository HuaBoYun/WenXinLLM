package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.Controlmatrix;
import com.huabo.cybermonitor.entity.Flow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.Riskevent;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-08-11
 */
public interface FlowMapper extends BaseMapper<Flow> {


    @Select("SELECT * " +
            "FROM TBL_FLOW  t WHERE t.FLOWID in ( " +
            "    select flowid from TBL_INDICATOR_FLOW where INDICATORID = #{id})")
    List<Flow> showListWithIndicatorid(@Param("id") BigDecimal id);

    @Select("select * from TBL_CONTROLMATRIX where CONMATID in (select  CONMATID from TBL_INDICATOR_CM where INDICATORID = #{indicatorid})")
    List<Controlmatrix> findAllControlmatrix(@Param("indicatorid") BigDecimal indicatorid);

    @Select("select * from TBL_RISKEVENT where RISEVEID in (select RISEVEID from TBL_INDICATOR_RISKEVENT  where INDICATORID = #{indicatorid})")
    List<Riskevent> findAllRISKEVENT(@Param("indicatorid") BigDecimal indicatorid);
}
