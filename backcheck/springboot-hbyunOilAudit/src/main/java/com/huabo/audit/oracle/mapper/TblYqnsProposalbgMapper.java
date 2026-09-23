package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsProposalbg;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;

public interface TblYqnsProposalbgMapper extends BaseMapper<TblYqnsProposalbg> {
	
	
    @Delete("DELETE FROM TBL_YQNS_PROPOSALBG WHERE BGID = #{bgid}")
    void deleteoneById(BigDecimal bgid) throws Exception;


    @Select("select *  FROM TBL_YQNS_PROPOSALBG WHERE BGID = #{bgid}") 
    TblYqnsProposalbg selectById(BigDecimal bgid) throws Exception;


    @Insert("INSERT INTO TBL_YQNS_PROPOSALBG_ATT(BGID,ATTID) VALUES (#{bgid},#{aid})")
    void insertAttInfoAtt( BigDecimal bgid, String aid) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_PROPOSALBG_ATT where  ATTID=#{aid}")
    void deleteAttInfoAtt( String aid) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_PROPOSALBG_ATT where BGID=#{bgid}")
    void deleteAttInfoAttByxm(BigDecimal bgid) throws Exception;


    @Select("SELECT MAX(ISSUEDATE) FROM TBL_YQNS_PROPOSALBG WHERE BGID IN (SELECT SJYJJDID FROM TBL_YQNS_WTZG_SJYJJDS WHERE WTZGID = #{wtzgid} )")
	Date selectMaxIssuesDate(@Param("wtzgid")BigDecimal wtzgid) throws Exception;


}
