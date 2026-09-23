package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsXmsqsq;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

public interface TblYqnsXmsqsqMapper extends BaseMapper<TblYqnsXmsqsq> {
	
	
    @Delete("DELETE FROM TBL_YQNS_XMYQSQ WHERE XMDQID = #{xmdqid}")
    void deleteoneById(BigDecimal xmdqid) throws Exception;



    @Select("select *  FROM TBL_YQNS_XMYQSQ WHERE XMDQID = #{xmdqid}")
    TblYqnsXmsqsq selectById(BigDecimal xmdqid) throws Exception;


    @Insert("INSERT INTO TBL_YQNS_XMYQSQ_ATT(XMDQID,ATTID) VALUES (#{xmdqid},#{aid})")
    void insertAttInfoAtt( BigDecimal xmdqid, String aid) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_XMYQSQ_ATT where  ATTID=#{aid}")
    void deleteAttInfoAtt( String aid) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_XMYQSQ_ATT where XMDQID=#{xmdqid}")
    void deleteAttInfoAttByxm(BigDecimal xmdqid) throws Exception;




}
