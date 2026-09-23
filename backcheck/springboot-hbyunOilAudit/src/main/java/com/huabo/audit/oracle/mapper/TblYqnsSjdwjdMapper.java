package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsSjdwjd;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

public interface TblYqnsSjdwjdMapper extends BaseMapper<TblYqnsSjdwjd> {
	
	
    @Delete("DELETE FROM TBL_YQNS_LEAVE_AUDIT_JD3L WHERE JDID = #{jdid}")
    void deleteoneById(BigDecimal jdid) throws Exception;



    @Select("select *  FROM TBL_YQNS_LEAVE_AUDIT_JD3L WHERE JDID = #{jdid}")
    TblYqnsSjdwjd selectById(BigDecimal jdid) throws Exception;


    @Insert("INSERT INTO TBL_YQNS_LEAVE_AUDIT_JD3L_GL(JDID,NRID) VALUES (#{jdid},#{id})")
    void insertglnr( BigDecimal jdid, String id) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_LEAVE_AUDIT_JD3L_GL where  JDID=#{jdid}")
    void deleteglnr(BigDecimal jdid) throws Exception;

 




}
