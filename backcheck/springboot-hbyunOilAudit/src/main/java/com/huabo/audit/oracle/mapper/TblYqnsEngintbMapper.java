package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsEngintb;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

public interface TblYqnsEngintbMapper extends BaseMapper<TblYqnsEngintb> {
	
	
    @Delete("DELETE FROM TBL_YQNS_ENGINTB WHERE TBID = #{tbid}")
    void deleteoneById(BigDecimal tbid) throws Exception;



    @Select("select *  FROM TBL_YQNS_ENGINTB WHERE TBID = #{tbid}")
    TblYqnsEngintb selectById(BigDecimal tbid) throws Exception;


    @Insert("INSERT INTO TBL_YQNS_ENGINTB_GL(TBID,ENGID) VALUES (#{tbid},#{id})")
    void insertglnr( BigDecimal tbid, String id) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_ENGINTB_GL where  TBID=#{tbid}")
    void deleteglnr(BigDecimal tbid) throws Exception;
    
    @Insert("INSERT INTO TBL_YQNS_ENGINTB_ATT(TBID,ATTID) VALUES (#{tbid},#{attid})")
   	void insertAttInfoForPlan(BigDecimal tbid, String attid) throws Exception;
       
       @Delete("DELETE FROM TBL_YQNS_ENGINTB_ATT WHERE ATTID=#{attid}")
   	void deleteFileInfoByAttId(String attid);

       
       @Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
   	void deleteEntity(String attid) throws Exception;


}
