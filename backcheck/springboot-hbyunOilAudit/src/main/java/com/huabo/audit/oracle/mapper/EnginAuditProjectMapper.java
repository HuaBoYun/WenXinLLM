package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;

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
 * @create: 2023-10-14 10:43
 **/
public interface EnginAuditProjectMapper extends BaseMapper<TblYqnsEnginAuditProjectEntity> {

    @Select("SELECT ENGIN_PROJECT_SEQUENCE.NEXTVAL FROM DUAL")
    Long getNextSequenceValue();

    
    @Update("UPDATE TBL_YQNS_ENGIN_AUDIT_PROJECT SET XFKSRYIDS = #{ids},XFKSRYNAMES=#{names} WHERE ID = #{id}")
    void xfksry(String id,String ids,String names);
    
    @Update("UPDATE TBL_YQNS_ENGIN_AUDIT_PROJECT SET XFSMZRYIDS = #{ids},XFXMZRYNAMES=#{names} WHERE ID = #{id}")
    void xfxmzry(String id,String ids,String names);
    
    
    @Update("UPDATE TBL_YQNS_ENGIN_AUDIT_PROJECT SET FPKSRYIDS = #{ids},FPKSRYNAMES=#{names} WHERE ID = #{id}")
    void fpksry(String id,String ids,String names);
    
    @Delete("DELETE FROM TBL_YQNS_ENGIN_AUDIT_PROJECT  WHERE ID = #{id}")
    void deleteone(Long id);

    @Select("SELECT * FROM TBL_YQNS_ENGIN_AUDIT_PROJECT WHERE ISSYNCGC IS NULL OR ISSYNCGC != 1")
	List<TblYqnsEnginAuditProjectEntity> selectSyncConstructionProject() throws Exception;

    @Select("SELECT * FROM TBL_YQNS_ENGIN_AUDIT_PROJECT WHERE ISSYNCJG IS NULL OR ISSYNCJG != 1")
	List<TblYqnsEnginAuditProjectEntity> selectSyncConstructionProjectJg();
    
    
    @Update("UPDATE TBL_YQNS_ENGIN_AUDIT_PROJECT SET ASSISTAPPROVERID = #{ids},ASSISTAPPROVER=#{names} WHERE ID = #{id}")
    void fpkzsrys(String id,String ids,String names);
    
    
    @Update("UPDATE TBL_YQNS_ENGIN_AUDIT_PROJECT SET XFSTATUS=1 WHERE ID in (${ids})")
    void xmzsb(String ids);
    
    @Update("UPDATE TBL_YQNS_ENGIN_AUDIT_PROJECT SET XFSTATUS=2 WHERE ID in (${ids})")
    void xmzsbth(String ids);
    
    
    @Update("UPDATE TBL_YQNS_ENGIN_AUDIT_PROJECT SET STATUS=2 WHERE ID in (${ids})")
    void tzstatus(String ids);
    
    
    @Select("SELECT * FROM TBL_YQNS_ENGIN_AUDIT_PROJECT where id IN (SELECT ENGID FROM TBL_YQNS_ENGINTB_GL WHERE TBID=#{fromid})")
	List<TblYqnsEnginAuditProjectEntity> findByTbidall(String fromid);

    @Update("UPDATE TBL_YQNS_ENGIN_AUDIT_PROJECT SET FPSLKRYID = #{fpslkryid},FPSLKRYNAME=#{fpslkryname} WHERE ID = #{id}")
	void fpslkry(@Param("id")String id,@Param("fpslkryid") String fpslkryid,@Param("fpslkryname") String fpslkryname) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_ENGIN_AUDIT_PROJECT WHERE ID = (SELECT XMAPBID FROM TBL_YQNS_IMPLEMENT_PLAN WHERE ID = #{projectId})")
	TblYqnsEnginAuditProjectEntity selectBySjbg(@Param("projectId")BigDecimal projectId);
}
