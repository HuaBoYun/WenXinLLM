package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.MonitorModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-14
 */
public interface MonitorModelMapper extends BaseMapper<MonitorModel> {

    @Select("select count(*) from TBL_MONITOR_MODEL WHERE MODELCODE = #{number}"+
            "AND ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE  1=1 START WITH FATHERORGID= #{orgid} " +
            "AND ORGTYPE=0  CONNECT BY PRIOR ORGID = FATHERORGID UNION ALL SELECT #{orgid}  FROM DUAL)")
    int validateByNumberAndOrg(@Param("number") String number, @Param("orgid") String orgid);

    @Select("select count(mo.modelid) from TBL_MONITOR_MODEL  mo left join  TBL_MONITOR_SOLUTION_MODEL   sm on   mo.modelid = sm.modelid where sm.SOLUTIONID =#{modelId}")
    int count(@Param("modelId") BigDecimal modelId);


//    select * from (select rownum st,* from a) where st >=? and st < ?;

    @Select("select * from (select rownum st,mo.* from TBL_MONITOR_MODEL  mo left join  TBL_MONITOR_SOLUTION_MODEL   sm on   mo.modelid = sm.modelid where sm.SOLUTIONID =#{modelId}) t where t.st >= #{page} and t.st < #{limit}")
    List<Map<String,Object>> show(@Param("modelId")BigDecimal modelId,@Param("page")Long page, @Param("limit")Long limit);






}
