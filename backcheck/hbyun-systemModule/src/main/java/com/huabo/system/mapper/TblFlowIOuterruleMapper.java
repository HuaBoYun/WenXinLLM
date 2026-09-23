package com.huabo.system.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFlowIOuterrule;


public interface TblFlowIOuterruleMapper  extends BaseMapper<TblFlowIOuterrule> {

    @Insert("insert into TBL_FLOW_OUTERRULE (OUTRULID,FLOWID) values (#{outrulid},#{flowid})")
    void insertinner(TblFlowIOuterrule inner);

    @Delete("DELETE FROM TBL_FLOW_OUTERRULE WHERE OUTRULID = #{outrulid} AND FLOWID = #{flowid}")
    void delteTblFlowInnerRule(TblFlowIOuterrule outer);

}
