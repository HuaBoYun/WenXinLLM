package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsOperate;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblYqnsOperateMapper extends BaseMapper<TblYqnsOperate> {
	 
	
    @Delete("DELETE FROM TBL_YQNS_OPERATE WHERE OPERID = #{operid}")
    void deleteoneById(BigDecimal operid) throws Exception;
    
    
    
    @Delete("DELETE FROM TBL_YQNS_OPERATE WHERE RWUSERID in (${userid}) and FORMID=#{fomrid} and (STATUS = 0 or STATUS is null) and PRAENTID is null ")
    void deleteoneByUserId(String userid,String fomrid) throws Exception; 
    
    @Delete("DELETE FROM TBL_YQNS_OPERATE WHERE SSMKID in (${ssmkid}) and (STATUS = 0 or STATUS is null) and PRAENTID is null ")
    void deleteoneBymkId(String ssmkid) throws Exception; 


    @Select("select *  FROM TBL_YQNS_OPERATE WHERE RWUSERID = ${userid}  and SSMKID = #{ssmkid} and (STATUS = 0 or STATUS is null)  order by OPERID asc  ")
    List<TblYqnsOperate> findbyByformid(String userid,String ssmkid) throws Exception;
    
    @Select("select *  FROM TBL_YQNS_OPERATE WHERE CREATESTAFFID = ${userid}  and SSMKID = #{ssmkid} and (STATUS = 0 or STATUS is null)  order by OPERID asc  ")
    List<TblYqnsOperate> findbyByCreateid(String userid,String ssmkid) throws Exception;
    
    @Select("select *  FROM TBL_YQNS_OPERATE WHERE RWUSERID = ${userid}  and SSMKID = #{ssmkid} and FORMNAME=#{formname}  and (STATUS = 0 or STATUS is null)  order by OPERID asc  ")
    List<TblYqnsOperate> findbyByformname(String userid,String ssmkid,String formname) throws Exception;
    
    @Select("select *  FROM TBL_YQNS_OPERATE WHERE RWUSERID = ${userid}  and SSMKID = #{ssmkid} and (STATUS = 0 or STATUS is null) and PRAENTID is null  order by OPERID asc  ")
    List<TblYqnsOperate> findbyByformidparentid(String userid,String ssmkid) throws Exception;

    @Select("select *  FROM TBL_YQNS_OPERATE WHERE OPERID = #{operid}")
    TblYqnsOperate selectById(BigDecimal operid) throws Exception;

    @Select("select *  FROM TBL_YQNS_OPERATE WHERE STATUS = ${status}  and RWUSERID = #{userid} order by OPERID asc  ")
    List<TblYqnsOperate> findbyStatus(String userid,Integer status) throws Exception;
    
    
    @Select("select *  FROM TBL_YQNS_OPERATE WHERE (STATUS = 0 or STATUS is null) and RWUSERID = #{userid}  order by OPERID asc  ")
    List<TblYqnsOperate> findbyStatusdb(String userid) throws Exception;
    
    @Select("select  TBID from TBL_YQNS_ENGINTB_GL  WHERE ENGID=#{xmid} and ROWNUM=1 ")
    String findXmidcount(String xmid);
    
    @Select("select  TBID from TBL_YQNS_FUNDTB_GL  WHERE FUNID=#{xmid} and ROWNUM=1 ")
    String findXmidcountcw(String xmid);
    
    
    @Select("select count(*)  FROM TBL_YQNS_OPERATE WHERE FORMID = #{formid} and (STATUS = 0 or STATUS is null) ")
    Integer findrwcount(String formid);
    
    @Select("select count(*)  FROM TBL_YQNS_OPERATE WHERE FORMID = #{formid} and RWUSERID = #{userid}  and (STATUS = 0 or STATUS is null) ")
    Integer findrwUseridcount(String formid,String userid);
    
    
    @Update("UPDATE TBL_YQNS_OPERATE SET  FORMID=#{fomrid}  WHERE SSMKID=#{mkid} AND STATUS=0 ")
    void updateByxmner(String fomrid,String mkid) throws Exception;
    
    
    @Select("select *  FROM TBL_YQNS_OPERATE WHERE  SSMKID in ( ${ssmkid} ) and (STATUS = 0 or STATUS is null) and PRAENTID is null  order by OPERID asc  ")
    List<TblYqnsOperate> findbyByformidpare(String ssmkid) throws Exception;
    
    @Select("select *  FROM TBL_YQNS_OPERATE WHERE (STATUS = 0 or STATUS is null) and RWUSERID = #{userid} and SSMKID = #{ssmkid} and FORMID = #{formid}   order by OPERID asc  ")
    List<TblYqnsOperate> findbyformidStatusdb(String userid,String ssmkid,String formid) throws Exception;
    
    
    @Update("UPDATE TBL_YQNS_OPERATE SET  STATUS=1   WHERE FORMID=#{fomrid}  ")
    void updateBystatus(String fomrid,String mkid) throws Exception;
    
    @Delete("DELETE FROM TBL_YQNS_OPERATE WHERE FORMID=#{fomrid} and (STATUS = 0 or STATUS is null) and PRAENTID is null ")
    void deleteoneByfromid(String fomrid) throws Exception;

 

    @Select("select OPT.*,STA.REALNAME   FROM TBL_YQNS_OPERATE opt LEFT JOIN TBL_STAFF sta on OPT.RWUSERID=STA.STAFFID WHERE (opt.STATUS = 0 or opt.STATUS is null) and opt.SSMKID in (${ssmkid})  order by opt.OPERID asc  ")
    List<TblYqnsOperate> findbyhzlist(String ssmkid) throws Exception;

}
