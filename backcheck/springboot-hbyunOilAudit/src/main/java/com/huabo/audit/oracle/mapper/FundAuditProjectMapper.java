package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectEntity;

import io.lettuce.core.dynamic.annotation.Param;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-14 23:59
 **/
public interface FundAuditProjectMapper extends BaseMapper<TblYqnsFundAuditProjectEntity> {
 
    @Select("SELECT FUND_AUDIT_PROJECT_SEQUENCE.NEXTVAL FROM DUAL")
    Long getNextSequenceValue();
    
    @Select("SELECT * from TBL_YQNS_FUND_AUDIT_PROJECT WHERE id in (SELECT FUNID from TBL_YQNS_FUNDTB_GL where TBID=#{tbid})")
    List<TblYqnsFundAuditProjectEntity> findbytbidall(BigDecimal tbid);

    
    
    @Update("UPDATE TBL_YQNS_FUND_AUDIT_PROJECT SET XFKSRYIDS = #{ids},XFKSRYNAMES=#{names} WHERE ID = #{id}")
    void xfksry(String id,String ids,String names);
    
    @Update("UPDATE TBL_YQNS_FUND_AUDIT_PROJECT SET XFSMZRYIDS = #{ids},XFXMZRYNAMES=#{names} WHERE ID = #{id}")
    void xfxmzry(String id,String ids,String names);
    
    
    
    
    @Update("UPDATE TBL_YQNS_FUND_AUDIT_PROJECT SET FPKSRYIDS = #{ids},FPKSRYNAMES=#{names} WHERE ID = #{id}")
    void fpksry(String id,String ids,String names);
    
    @Delete("DELETE FROM TBL_YQNS_FUND_AUDIT_PROJECT  WHERE ID = #{id}")
    void deleteone(Long id);
    
    @Update("UPDATE TBL_YQNS_FUND_AUDIT_PROJECT SET ASSISTAPPROVERID = #{ids},ASSISTAPPROVER=#{names} WHERE ID = #{id}")
    void fpkzsrys(String id,String ids,String names);
    
    @Update("UPDATE TBL_YQNS_FUND_AUDIT_PROJECT SET XFSTATUS=1 WHERE ID in (${ids})")
    void xmzsb(String ids);
    
    @Update("UPDATE TBL_YQNS_FUND_AUDIT_PROJECT SET XFSTATUS=2 WHERE ID in (${ids})")
    void xmzsbth(String ids);
    
    @Select("SELECT * from TBL_YQNS_FUND_AUDIT_PROJECT WHERE AUDITUNITID=#{orgid} AND ROWNUM=1 ORDER BY CREATETIME DESC")
    TblYqnsFundAuditProjectEntity findbyOrgidLast(String orgid);
    
    @Update("UPDATE TBL_YQNS_FUND_AUDIT_PROJECT SET STATUS=2 WHERE ID in (${ids})")
    void tzstatus(String ids);
    
    
    @Select("SELECT DISTINCT * FROM TBL_YQNS_FUND_AUDIT_PROJECT where id IN (SELECT FUNID FROM TBL_YQNS_FUNDTB_GL WHERE TBID=#{fromid})")
  	List<TblYqnsFundAuditProjectEntity> findByTbidall(String fromid);

    @Update("UPDATE TBL_YQNS_FUND_AUDIT_PROJECT SET FPSLKRYID = #{fpslkryid},FPSLKRYNAME=#{fpslkryname} WHERE ID = #{id}")
	void fpslkry(@Param("id")String id,@Param("fpslkryid") String fpslkryid,@Param("fpslkryname") String fpslkryname) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_FUND_AUDIT_PROJECT WHERE ID = (SELECT XMAPBID FROM TBL_YQNS_IMPLEMENT_PLAN WHERE ID = #{projectId})")
	TblYqnsFundAuditProjectEntity selectBySjbg(@Param("projectId")BigDecimal projectId);
}
