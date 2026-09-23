package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsOtherAudit;

public interface TblYqnsOtherAuditMapper extends BaseMapper<TblYqnsOtherAudit> {

	@Insert("INSERT INTO TBL_YQNS_OTHERAUDIT_ATT(AUDITID,ATTID) VALUES (#{auditId},#{attid})")
	void insertAttRelation(@Param("auditId")BigDecimal auditId,@Param("attid") String attid) throws Exception;

	@Select(" SELECT TYO.*,CTS.REALNAME AS CREATESTAFFNAME,IMORG.ORGNAME AS IMPLORGNAME,IMDEP.ORGNAME AS ITEMDEPTNAME,UNIT.ORGNAME AS PROJECTINITUNITNAME FROM TBL_YQNS_OTHERAUDIT TYO LEFT JOIN TBL_STAFF CTS ON TYO.CREATESTAFFID = CTS.STAFFID" + 
			" LEFT JOIN TBL_ORGANIZATION IMORG ON TYO.IMPLORGID = IMORG.ORGID LEFT JOIN TBL_ORGANIZATION IMDEP ON TYO.ITEMDEPTID = IMDEP.ORGID LEFT JOIN TBL_ORGANIZATION UNIT ON TYO.PROJECTINITUNIT = UNIT.ORGID" + 
			" WHERE TYO.AUDITID = #{auditId}")
	TblYqnsOtherAudit selectEntityById(@Param("auditId")BigDecimal auditId) throws Exception;

	@Select("SELECT ORGNAME FROM TBL_ORGANIZATION WHERE ORGID IN (${auditOrgidStrs})")
	List<String> selectAuditOrgNameList(@Param("auditOrgidStrs")String auditOrgidStrs) throws Exception;

	@Delete("DELETE FROM TBL_YQNS_OTHERAUDIT_ATT WHERE ATTID = #{attId}")
	void deletAttRealtionByAttId(@Param("attId")BigDecimal attId) throws Exception;

	@Delete("DELETE FROM TBL_YQNS_OTHERAUDIT_ATT WHERE AUDITID = #{auditId}")
	void deletAttRealtionByAuditId(@Param("auditId")BigDecimal auditId) throws Exception;

	@Update("UPDATE TBL_YQNS_OTHERAUDIT SET DRAFTPLANID = #{jhcgid} WHERE AUDITID IN (${otherAuditIdsStrs})")
	void updateJhcgIdByIds(@Param("jhcgid")BigDecimal jhcgid,@Param("otherAuditIdsStrs") String otherAuditIdsStrs) throws Exception;

	@Select(" SELECT TYO.*,CTS.REALNAME AS CREATESTAFFNAME,IMORG.ORGNAME AS IMPLORGNAME,IMDEP.ORGNAME AS ITEMDEPTNAME,UNIT.ORGNAME AS PROJECTINITUNITNAME FROM TBL_YQNS_OTHERAUDIT TYO LEFT JOIN TBL_STAFF CTS ON TYO.CREATESTAFFID = CTS.STAFFID" + 
			" LEFT JOIN TBL_ORGANIZATION IMORG ON TYO.IMPLORGID = IMORG.ORGID LEFT JOIN TBL_ORGANIZATION IMDEP ON TYO.ITEMDEPTID = IMDEP.ORGID LEFT JOIN TBL_ORGANIZATION UNIT ON TYO.PROJECTINITUNIT = UNIT.ORGID" + 
			" WHERE TYO.DRAFTPLANID = #{jhcgid}")
	List<TblYqnsOtherAudit> selectListByDraftPlanId(@Param("jhcgid")BigDecimal jhcgid) throws Exception; 

	@Select(" SELECT TYO.*,CTS.REALNAME AS CREATESTAFFNAME,IMORG.ORGNAME AS IMPLORGNAME,IMDEP.ORGNAME AS ITEMDEPTNAME,UNIT.ORGNAME AS PROJECTINITUNITNAME FROM TBL_YQNS_OTHERAUDIT TYO LEFT JOIN TBL_STAFF CTS ON TYO.CREATESTAFFID = CTS.STAFFID" + 
			" LEFT JOIN TBL_ORGANIZATION IMORG ON TYO.IMPLORGID = IMORG.ORGID LEFT JOIN TBL_ORGANIZATION IMDEP ON TYO.ITEMDEPTID = IMDEP.ORGID LEFT JOIN TBL_ORGANIZATION UNIT ON TYO.PROJECTINITUNIT = UNIT.ORGID" + 
			" WHERE TYO.DRAFTPLANID = #{jhcgid} AND FIRSTDRAFTPLANID IS NULL")
	List<TblYqnsOtherAudit> selectListByFirstDraftPlanId(@Param("jhcgid")BigDecimal jhcgid) throws Exception;

	@Select(" SELECT TYO.*,CTS.REALNAME AS CREATESTAFFNAME,IMORG.ORGNAME AS IMPLORGNAME,IMDEP.ORGNAME AS ITEMDEPTNAME,UNIT.ORGNAME AS PROJECTINITUNITNAME FROM TBL_YQNS_OTHERAUDIT TYO LEFT JOIN TBL_STAFF CTS ON TYO.CREATESTAFFID = CTS.STAFFID" + 
			" LEFT JOIN TBL_ORGANIZATION IMORG ON TYO.IMPLORGID = IMORG.ORGID LEFT JOIN TBL_ORGANIZATION IMDEP ON TYO.ITEMDEPTID = IMDEP.ORGID LEFT JOIN TBL_ORGANIZATION UNIT ON TYO.PROJECTINITUNIT = UNIT.ORGID" + 
			" WHERE TYO.DRAFTPLANID = #{jhchugid} AND SECONDRAFTPLANID IS NULL")
	List<TblYqnsOtherAudit> selectListBySecondDraftPlanId(@Param("jhchugid")BigDecimal jhchugid) throws Exception;
	
	@Select(" SELECT TYO.*,CTS.REALNAME AS CREATESTAFFNAME,IMORG.ORGNAME AS IMPLORGNAME,IMDEP.ORGNAME AS ITEMDEPTNAME,UNIT.ORGNAME AS PROJECTINITUNITNAME FROM TBL_YQNS_OTHERAUDIT TYO LEFT JOIN TBL_STAFF CTS ON TYO.CREATESTAFFID = CTS.STAFFID" + 
			" LEFT JOIN TBL_ORGANIZATION IMORG ON TYO.IMPLORGID = IMORG.ORGID LEFT JOIN TBL_ORGANIZATION IMDEP ON TYO.ITEMDEPTID = IMDEP.ORGID LEFT JOIN TBL_ORGANIZATION UNIT ON TYO.PROJECTINITUNIT = UNIT.ORGID" + 
			" WHERE TYO.AUDITID IN (${otherAuditIdsStrs})")
	List<TblYqnsOtherAudit> selectListByAuditIds(@Param("otherAuditIdsStrs")String otherAuditIdsStrs) throws Exception;

	@Update("UPDATE TBL_YQNS_OTHERAUDIT SET DRAFTPLANID = #{jhchugid} , FIRSTDRAFTPLANID = #{ischoose} WHERE AUDITID IN (${updateIds})")
	void updateJhchugIdByIds(@Param("jhchugid")BigDecimal jhchugid, @Param("updateIds")String updateIds,@Param("ischoose") int ischoose);

	@Update("UPDATE TBL_YQNS_OTHERAUDIT SET DRAFTPLANID = #{jhchugid} , SECONDRAFTPLANID = #{ischoose} WHERE AUDITID IN (${updateIds})")
	void updateJhzgIdByIds(@Param("jhchugid")BigDecimal jhchugid, @Param("updateIds")String updateIds,@Param("ischoose") int ischoose);

	@Delete("DELETE FROM TBL_YQNS_OTHERAUDIT WHERE DRAFTPLANID = #{jhid}")
	void deleteByDraftPladId(@Param("jhid")BigDecimal jhid) throws Exception;

	@SelectProvider(type = TblYqnsOtherAuditMapperSqlConfig.class , method = "selectListByDraftPlanIdForChoose")
	List<TblYqnsOtherAudit> selectListByDraftPlanIdForChoose(BigDecimal draftPlanId, Integer chooseType,
			String auditIdStrs) throws Exception;

	@SelectProvider(type = TblYqnsOtherAuditMapperSqlConfig.class , method = "findListByAnalysis")
	List<TblYqnsOtherAudit> findListByAnalysis(Integer xmnd, String auditItemName);

}