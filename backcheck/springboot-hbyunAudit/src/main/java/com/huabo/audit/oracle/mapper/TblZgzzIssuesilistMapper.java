package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.audit.oracle.entity.TblBeforeZgzzListEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblZgzzIssuesilist;
import com.huabo.audit.oracle.vo.TblZgzzIssuesilistVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * <p>
 * 整改清单表 Mapper 接口
 * </p>
 *
 * @author LHP
 * @since 2023-11-16
 */
public interface TblZgzzIssuesilistMapper extends Mapper<TblZgzzIssuesilist> {

	/**
	 * 保存整改清单与附件的关系
	 * @param attid
	 * @param issuesId
	 * @throws Exception
	 */
	@Insert("INSERT INTO TBL_ISSUES_ATT(ATTID,ISSUESID) VALUES(#{attid},#{issuesId})")
	void saveFileRelation(@Param("attid")String attid,@Param("issuesId") String issuesId) throws Exception;

	/**
	 * 根据主键查询实体
	 * @param issuesId
	 * @return
	 * @throws Exception
	 */
	@Select("SELECT TZI.*,CTS.REALNAME AS CREATESTAFFNAME,RST.REALNAME AS RESPONSIBLEPERSONNAME,RORG.ORGNAME AS RESPONSIBLEDEPTNAME ,CASE WHEN AUDITOBJECTTYPE = 3 THEN ATS.REALNAME ELSE AOG.ORGNAME END AS AUDITOBJECTNAME,"
			+ " CASE WHEN ISSUESTYPE = 1 THEN TNP.PROJECTCODE WHEN ISSUESTYPE = 2 THEN TTP.PLANNUMBER WHEN ISSUESTYPE = 3 OR ISSUESTYPE = 4  THEN TNW.PROJECTCODE ELSE '' END AS PROJECTNO,"
			+ " CASE WHEN ISSUESTYPE = 1 THEN TNP.PRJOECTNAME WHEN ISSUESTYPE = 2 THEN TTP.PLANNAME WHEN ISSUESTYPE = 3 OR ISSUESTYPE = 4 THEN TNW.PROJECTNAME ELSE '' END AS PROJECTNAME"
			+ " FROM TBL_ZGZZ_ISSUESILIST TZI LEFT JOIN TBL_STAFF CTS ON TZI.CREATESTAFF = CTS.STAFFID LEFT JOIN TBL_STAFF ATS ON TZI.AUDITOBJECTID = ATS.STAFFID"
			+ " LEFT JOIN TBL_ORGANIZATION AOG ON TZI.AUDITOBJECTID = AOG.ORGID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON TZI.PROJECTID = TNW.PROJECTID"
			+ " LEFT JOIN TBL_NBSJ_PROJECT TNP ON TZI.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON TZI.PROJECTID = TTP.TESTPLANID "
			+ " LEFT JOIN TBL_STAFF RST ON TZI.RESPONSIBLEPERSON = RST.STAFFID LEFT JOIN TBL_ORGANIZATION RORG ON TZI.RESPONSIBLEDEPT = RORG.ORGID "
			+ " WHERE TZI.ISSUESID = #{issuesId}")
	TblZgzzIssuesilistVo selectEntityById(@Param("issuesId") String issuesId) throws Exception;

	/**
	 * 删除附件关系
	 * @param issuesId
	 * @throws Exception
	 */
	@Delete("DELETE FROM TBL_ISSUES_ATT WHERE ISSUESID = #{issuesId}")
	void deleteFileRelation(@Param("issuesId")String issuesId) throws Exception;

	/**
	 * 整改追责问题清单分页查询语句
	 * @param loginStaff 
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@SelectProvider(method="selectListByPageInfo",type=TblZgzzIssuesilistMapperSqlConfig.class)
	List<TblZgzzIssuesilistVo> selectListByPageInfo(TblZgzzIssuesilistVo condition, TblStaffUtil loginStaff);

	/**
	 * 整改问题清单导出获取总记录数
	 * @param issues
	 * @param staffId 
	 * @return
	 */
	@SelectProvider(method="selectAllListByIssues",type=TblZgzzIssuesilistMapperSqlConfig.class)
	List<TblZgzzIssuesilistVo> selectAllListByIssues(TblZgzzIssuesilistVo issues, TblStaffUtil loginStaff);

	/**
	 * 验证业务编号是否重复
	 * @param issuesCode 业务编号
	 * @param questionId 
	 * @param issuesId  整改清单主键
	 * @return
	 * @throws Exception
	 */
	@SelectProvider(method="selectCountByIssuesCode",type=TblZgzzIssuesilistMapperSqlConfig.class)
	Integer selectCountByIssuesCode(String issuesCode, BigDecimal questionId, String issuesId) throws Exception;

	/**
	 * 通过整改清单主键和附件主键 删除附件关系 
	 * @param issuesId
	 * @throws Exception
	 */
	@Delete("DELETE FROM TBL_ISSUES_ATT WHERE ISSUESID = #{issuesId} AND ATTID = #{attId}")
	void deleteFileRelationByAttId(@Param("issuesId")String issuesId,@Param("attId") String attId) throws Exception;

	/**
	 * 整改清单 关联 整改方案是 修改整改清单状态为 开始整改，将之前的状态存储为历史状态，方便后续取消关联时还原状态值
	 * @param issuesId 		整改清单主键
	 * @throws Exception
	 */
	@Update("UPDATE TBL_ZGZZ_ISSUESILIST SET HISTORYSTATUS = STATUS ,  STATUS = #{status} WHERE ISSUESID = #{issuesId}")
	void updateStatusBySavePlan(@Param("issuesId")String issuesId,@Param("status")Integer status) throws Exception;

	/**
	 * 整改方案取消关联整改清单，还原关联的整改清单的状态
	 * @param issuesId		整改清单主键
	 * @throws Exception
	 */
	@Update("UPDATE TBL_ZGZZ_ISSUESILIST SET STATUS = HISTORYSTATUS WHERE ISSUESID = #{issuesId}")
	void deleteRelationPlanRecoverStatus(@Param("issuesId")String issuesId) throws Exception;

	/**
	 * 整改方案删除时 ，还原所有关联的整改清单的状态
	 * @param planId		整改方案主键
	 * @throws Exception
	 */
	@Update("UPDATE TBL_ZGZZ_ISSUESILIST SET STATUS = HISTORYSTATUS WHERE ISSUESID IN (SELECT ISSUESID FROM TBL_RECTIFICATION_ISSUES WHERE PLANID = #{planId})")
	void deleteRelationPlanRecoverListStatus(@Param("planId")String planId) throws Exception;

	/**
	 * 根据整改落实信息主键获取 整改清单主键
	 * @param implId	整改落实主键
	 * @return String 整改清单主键
	 * @throws Exception
	 */
	@Select("SELECT ISSUESID FROM TBL_RECTIFICATION_ISSUES WHERE RELAID = ( SELECT RELAID FROM TBL_ZGZZ_RECTIFICATIONIMPL WHERE IMPLID = #{implId} )")
	String selectIssuesIdByImpld(@Param("implId") String implId) throws Exception;

	/**
	 *     根据整改报告内容 获取整改清单列表页信息；
	 * @param reportid
	 * @return
	 */
	@Select("SELECT TZI.*,RST.REALNAME AS RESPONSIBLEPERSONNAME,RORG.ORGNAME AS RESPONSIBLEDEPTNAME,TZRI.PLANID," + 
			" CASE WHEN ISSUESTYPE = 1 THEN TNP.PROJECTCODE WHEN ISSUESTYPE = 2 THEN TTP.PLANNUMBER WHEN ISSUESTYPE = 3 THEN TNW.PROJECTCODE ELSE '' END AS PROJECTNO, " + 
			" CASE WHEN ISSUESTYPE = 1 THEN TNP.PRJOECTNAME WHEN ISSUESTYPE = 2 THEN TTP.PLANNAME WHEN ISSUESTYPE = 3 THEN TNW.PROJECTNAME ELSE '' END AS PROJECTNAME, " + 
			" CASE WHEN AUDITOBJECTTYPE = 3 THEN ATS.REALNAME ELSE AOG.ORGNAME END AS AUDITOBJECTNAME,CTS.REALNAME AS CREATESTAFFNAME " + 
			" FROM TBL_ZGZZ_ISSUESILIST TZI LEFT JOIN TBL_STAFF CTS ON TZI.CREATESTAFF = CTS.STAFFID LEFT JOIN TBL_STAFF ATS ON TZI.AUDITOBJECTID = ATS.STAFFID " + 
			" LEFT JOIN TBL_ORGANIZATION AOG ON TZI.AUDITOBJECTID = AOG.ORGID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON TZI.PROJECTID = TNW.PROJECTID " + 
			" LEFT JOIN TBL_NBSJ_PROJECT TNP ON TZI.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON TZI.PROJECTID = TTP.TESTPLANID " + 
			" LEFT JOIN TBL_STAFF RST ON TZI.RESPONSIBLEPERSON = RST.STAFFID LEFT JOIN TBL_ORGANIZATION RORG ON TZI.RESPONSIBLEDEPT = RORG.ORGID " + 
			" LEFT JOIN TBL_ZGZZ_REPORTISSUES TZRI ON TZI.ISSUESID = TZRI.ISSUESID AND REPORTID = #{reportid} WHERE TZRI.REPORTID = #{reportid} ")
	List<TblZgzzIssuesilistVo> selectAllListByReportId(@Param("reportid") String reportid);

	@SelectProvider(method="selectNoChooseAuditOrg",type=TblZgzzIssuesilistMapperSqlConfig.class)
	List<TblOrganization> selectNoChooseAuditOrg(String orgIds, BigDecimal auditOrgId, BigDecimal sheetId) throws Exception;

	@Select("UPDATE TBL_ZGZZ_ISSUESILIST SET HISTORYSTATUS = STATUS ,  STATUS = #{status} WHERE STATUS = 7 AND ISSUESID IN (SELECT ISSUESID FROM TBL_RECTIFICATION_ISSUES WHERE PLANID = #{planId})")
	void updateStatusByClosePlan(@Param("planId")BigDecimal planId,@Param("status") Integer status);

	//获取往期列表清单里的Id
	@Select("SELECT MAX(ID) FROM TBL_BEFOREZGZZ_LIST WHERE ID LIKE #{ZGBG}")
	String getMaxId(String ZGBG);

	//获取往期列表清单里的Id
	@Insert("INSERT INTO TBL_BEFOREZGZZ_LIST (ID,FIRSTORG,SPECIFICDEPT,SOURCEPROBLEM,ISSUANCEYEAR," +
			"PROBLEMTYPE,FIRSTTITLE,SECONDTITLE,THIRDTITLE,AUDITREPORTDESC,SPECIFICDEPTLIST,PROBLEMDESC," +
			"PROBLEMMONEY,SUPERMANAGDEPT,CORTYPE,LAWSPOCOR,CORREQUIRE,CORTIME,CORSTANDARD,CORMEASURE," +
			"COMPLETIONTIME,FIRSTPERSON,ASSISTLEADER,LEADERPERSON,COOPERSON,AUDITPERSON,TAKEMEASURE," +
			"ITEMQUANTITY,PROCORMONEY,RECOMONEY,RETURNMONEY,OVERALLAMOUNT,ACCDISBUR,REFUNDTAXES," +
			"ADJUSTSTATEMENT,ADJUSTMONEY,SUPPAMOUNT,WAY,AMOUNT,LANDAREA,MINERESOURCE,UNIT,FAMILY," +
			"NUMBERPEOPLE,HOUSING,CLEARFORM,ACCNUMBERPEO,ACCQUANTITY,FILENAME,NOTRECTREASON,CORSTATUS" +
			") VALUES (#{tblBeforeZgzzListEntity.ID},#{tblBeforeZgzzListEntity.FIRSTORG},#{tblBeforeZgzzListEntity.SPECIFICDEPT}," +
			" #{tblBeforeZgzzListEntity.SOURCEPROBLEM}, #{tblBeforeZgzzListEntity.ISSUANCEYEAR}," +
			"#{tblBeforeZgzzListEntity.PROBLEMTYPE},#{tblBeforeZgzzListEntity.FIRSTTITLE}," +
			"#{tblBeforeZgzzListEntity.SECONDTITLE},#{tblBeforeZgzzListEntity.THIRDTITLE}," +
			"#{tblBeforeZgzzListEntity.AUDITREPORTDESC},#{tblBeforeZgzzListEntity.SPECIFICDEPTLIST},"+
			"#{tblBeforeZgzzListEntity.PROBLEMDESC},#{tblBeforeZgzzListEntity.PROBLEMMONEY}," +
			"#{tblBeforeZgzzListEntity.SUPERMANAGDEPT},#{tblBeforeZgzzListEntity.CORTYPE}," +
			"#{tblBeforeZgzzListEntity.LAWSPOCOR},#{tblBeforeZgzzListEntity.CORREQUIRE}," +
			"#{tblBeforeZgzzListEntity.CORTIME},#{tblBeforeZgzzListEntity.CORSTANDARD}," +
			"#{tblBeforeZgzzListEntity.CORMEASURE},#{tblBeforeZgzzListEntity.COMPLETIONTIME}," +
			"#{tblBeforeZgzzListEntity.FIRSTPERSON},#{tblBeforeZgzzListEntity.ASSISTLEADER}," +
			"#{tblBeforeZgzzListEntity.LEADERPERSON},#{tblBeforeZgzzListEntity.COOPERSON}," +
			"#{tblBeforeZgzzListEntity.AUDITPERSON},#{tblBeforeZgzzListEntity.TAKEMEASURE}," +
			"#{tblBeforeZgzzListEntity.ITEMQUANTITY},#{tblBeforeZgzzListEntity.PROCORMONEY}," +
			"#{tblBeforeZgzzListEntity.RECOMONEY},#{tblBeforeZgzzListEntity.RETURNMONEY}," +
			"#{tblBeforeZgzzListEntity.OVERALLAMOUNT},#{tblBeforeZgzzListEntity.ACCDISBUR}," +
			"#{tblBeforeZgzzListEntity.REFUNDTAXES},#{tblBeforeZgzzListEntity.ADJUSTSTATEMENT},#{tblBeforeZgzzListEntity.ADJUSTMONEY}," +
			"#{tblBeforeZgzzListEntity.SUPPAMOUNT},#{tblBeforeZgzzListEntity.WAY}," +
			"#{tblBeforeZgzzListEntity.AMOUNT},#{tblBeforeZgzzListEntity.LANDAREA}," +
			"#{tblBeforeZgzzListEntity.MINERESOURCE},#{tblBeforeZgzzListEntity.UNIT}," +
			"#{tblBeforeZgzzListEntity.FAMILY},#{tblBeforeZgzzListEntity.NUMBERPEOPLE}," +
			"#{tblBeforeZgzzListEntity.HOUSING},#{tblBeforeZgzzListEntity.CLEARFORM}," +
			"#{tblBeforeZgzzListEntity.ACCNUMBERPEO},#{tblBeforeZgzzListEntity.ACCQUANTITY}," +
			"#{tblBeforeZgzzListEntity.FILENAME},#{tblBeforeZgzzListEntity.NOTRECTREASON},#{tblBeforeZgzzListEntity.CORSTATUS}) ")
	void saveBeforeZgzzList(@Param("tblBeforeZgzzListEntity")TblBeforeZgzzListEntity tblBeforeZgzzListEntity);


	@SelectProvider(method="getBeforeZgzzList",type=TblZgzzIssuesilistMapperSqlConfig.class)
	List<TblBeforeZgzzListEntity> getBeforeZgzzList(TblBeforeZgzzListEntity tblBeforeZgzzListEntity);

	@Select("SELECT COUNT(*) FROM TBL_ORGANIZATION WHERE ORGNAME = #{OrgName};")
	Integer selectOrgCount(String OrgName);

	@Select("SELECT COUNT(*) FROM TBL_STAFF WHERE REALNAME = #{Name};")
	Integer selectauditStaffId(String Name);

	@Delete("DELETE FROM TBL_BEFOREZGZZ_LIST WHERE ID = #{ID}")
	void deleteBeforeList(String ID);

}
