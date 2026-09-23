package com.huabo.system.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFlowInnerRule;

public interface TblFlowInnerRuleMapper extends BaseMapper<TblFlowInnerRule> {

    @Insert("INSERT INTO TBL_FLOW_INNERRULE (INNRULID,FLOWID) VALUES (#{innrulid},#{flowid})")
    void insertRule(TblFlowInnerRule inner);
//
//    @Select("SELECT * FROM TBL_FLOW_INNERRULE fi where fi.FLOWID= #{flowid} and fi.INNRULID= #{innerid}")
//    List<TblFlowInnerRule> selectFlowInner(String flowid, String innerid);
    @Select("SELECT * FROM TBL_FLOW_INNERRULE fi where fi.FLOWID= #{flowid} and fi.INNRULID= #{innerid}")
    TblFlowInnerRule selectFlowInner(String flowid, String innerid);

    @Delete("DELETE FROM TBL_FLOW_INNERRULE WHERE FLOWID = #{flowid} AND INNRULID = #{innrulid}")
    void delteTblFlowInnerRule(TblFlowInnerRule inner);
}
