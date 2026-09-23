package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.TblNbsjProject;
import com.huabo.cybermonitor.vo.TblnbsjProjectVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TblNbsjProjectMapper extends BaseMapper<TblNbsjProject> {
	


	@Select("SELECT * from TBL_NBSJ_PROJECT WHERE PROJECTID= #{projectId} ")
	TblNbsjProject getById(Integer projectId);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
    @Results({
    	@Result(column="PROJECTID",property="projectId"),
    	@Result(column="PRJOECTNAME",property="prjoectName"),
    	@Result(column="PLANYEAR",property="planYear"),
    	@Result(column="PROJECTSOURCE",property="projectSource"),
    	@Result(column="STARTDATE",property="startDate"),
    	@Result(column="ENDDATE",property="endDate"),
    	@Result(column="AUDITTYPE",property="auditType"),
    	@Result(column="PROJECTCODE",property="projectCode"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="PMID",property="pmId"),
    	@Result(column="REALNAME",property="pmStaff.realname"),
    	@Result(column="ORGNAME",property="auditOrgInfo.orgname"),
    })
	List<TblNbsjProject> selectListByPageInfo(PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo tblnbsjProjectVo) throws Exception;

    @Select("SELECT TNA.*,TS.REALNAME,PLAN.PLANNAME,TEMP.TEMPLETENAME,TEMPZY.TEMPLETENAME TEMPLETENAMEZY,"
    		+ "TEMP.TEMPLETEID TEMPSJID,TEMPZY.TEMPLETEID TEMPZYID,createUser.REALNAME CREATEUSERNAME,"
    		+ "auditOrg.ORGNAME AUDITORGNAME,auditStaff.REALNAME AUDITSTAFFNAME"
    		+ " FROM TBL_NBSJ_PROJECT TNA "
    		+ " LEFT JOIN TBL_NBSJ_AUDITPLAN PLAN ON PLAN.PLANID = TNA.PLANID "
    		+ " LEFT JOIN TBL_NBSJ_TEMPLETE TEMP ON TEMP.TEMPLETEID = TNA.TEMPID "
    		+ " LEFT JOIN TBL_NBSJ_TEMPLETE TEMPZY ON TEMPZY.TEMPLETEID = TNA.TEMPZYID "
			+ " LEFT JOIN TBL_STAFF TS ON TNA.PMID = TS.STAFFID "
			+ " LEFT JOIN TBL_STAFF createUser ON TNA.CREATESTAFFID = createUser.STAFFID "
			+ " LEFT JOIN TBL_ORGANIZATION auditOrg ON TNA.AUDITORGID = auditOrg.ORGID "
			+ " LEFT JOIN TBL_STAFF auditStaff ON TNA.AUDITSTAFFID = auditStaff.STAFFID "
    		+ " WHERE TNA.PROJECTID = #{projectId} ")
    @Results({
    	@Result(column="PROJECTID",property="projectId"),
    	@Result(column="PRJOECTNAME",property="prjoectName"),
    	@Result(column="PPROJECTNAME",property="pprojectName"),
    	@Result(column="PLANYEAR",property="planYear"),
    	@Result(column="PROJECTSOURCE",property="projectSource"),
    	@Result(column="STARTDATE",property="startDate"),
    	@Result(column="ENDDATE",property="endDate"),
    	@Result(column="AUDITTYPE",property="auditType"),
    	@Result(column="PROJECTCODE",property="projectCode"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="PMID",property="pmId"),
    	@Result(column="REALNAME",property="pmStaff.realname"),
    	
    	@Result(column="PLANID",property="tblnbsjPlan.planid"),
    	@Result(column="PLANNAME",property="tblnbsjPlan.planname"),
    	@Result(column="TEMPLETENAME",property="tbltemplete.templeteName"),
    	@Result(column="TEMPLETENAMEZY",property="tbltempletezy.templeteName"),
    	@Result(column="TEMPSJID",property="tbltemplete.templeteId"),
    	@Result(column="TEMPZYID",property="tbltempletezy.templeteId"),
    	
    	@Result(column="CREATEUSERNAME",property="createUserName"),
    	@Result(column="PRO_SJFS",property="proSjfs"),
    	@Result(column="PRO_DESC",property="proDesc"),
    	
    	@Result(column="TARGETNAME",property="targetName"),
    	
    	@Result(column="AUDITORGNAME",property="auditOrgName"),
    	@Result(column="AUDITSTAFFNAME",property="auditStaffName"),
    	
    })
	TblNbsjProject selectPJById(@Param("projectId") Integer projectId) throws Exception;

}
