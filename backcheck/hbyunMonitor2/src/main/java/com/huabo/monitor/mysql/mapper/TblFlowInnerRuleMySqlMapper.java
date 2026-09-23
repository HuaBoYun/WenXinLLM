package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblFlowInnerRuleMySql;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface TblFlowInnerRuleMySqlMapper extends BaseMapper<TblFlowInnerRuleMySql> {

    @Insert("INSERT INTO TBL_FLOW_INNERRULE (INNRULID,FLOWID) VALUES (#{innrulid},#{flowid})")
    void insertRule(TblFlowInnerRuleMySql inner);

    //
//    @Select("SELECT * FROM TBL_FLOW_INNERRULE fi where fi.FLOWID= #{flowid} and fi.INNRULID= #{innerid}")
//    List<TblFlowInnerRule> selectFlowInner(String flowid, String innerid);
    @Select("SELECT * FROM TBL_FLOW_INNERRULE fi where fi.FLOWID= #{flowid} and fi.INNRULID= #{innerid}")
    TblFlowInnerRuleMySql selectFlowInner(String flowid, String innerid);

    @Delete("DELETE FROM TBL_FLOW_INNERRULE WHERE FLOWID = #{flowid} AND INNRULID = #{innrulid}")
    void delteTblFlowInnerRule(TblFlowInnerRuleMySql inner);
}
