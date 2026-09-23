package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblFlowIOuterruleMySql;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;


public interface TblFlowIOuterruleMySqlMapper extends BaseMapper<TblFlowIOuterruleMySql> {

    @Insert("insert into TBL_FLOW_OUTERRULE (OUTRULID,FLOWID) values (#{outrulid},#{flowid})")
    void insertinner(TblFlowIOuterruleMySql inner);

    @Delete("DELETE FROM TBL_FLOW_OUTERRULE WHERE OUTRULID = #{outrulid} AND FLOWID = #{flowid}")
    void delteTblFlowInnerRule(TblFlowIOuterruleMySql outer);

}
