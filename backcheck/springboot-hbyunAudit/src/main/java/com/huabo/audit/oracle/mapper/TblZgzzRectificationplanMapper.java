package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.entity.TblZgzzRectificationplan;
import com.huabo.audit.oracle.vo.TblZgzzProjectVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationplanVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * <p>
 * 整改F方案表 Mapper 接口
 * </p>
 *
 * @author LHP
 * @since 2023-11-22
 */
public interface TblZgzzRectificationplanMapper extends Mapper<TblZgzzRectificationplan> {

	@SelectProvider(method="selectPageInfoNkProjectList",type=TblZgzzRectificationplanMapperSqlConfig.class)
    @Results({
    	@Result(column="TESTPLANID",property="planId"),
    	@Result(column="PLANNUMBER",property="planCode"),
    	@Result(column="PLANNAME",property="planName"),
    })
	List<TblZgzzProjectVo> selectPageInfoNkProjectList(TblZgzzProjectVo project);

	@SelectProvider(method="checkRepeatPlanCode",type=TblZgzzRectificationplanMapperSqlConfig.class)
	Integer checkRepeatPlanCode(TblZgzzRectificationplan rectification) throws Exception;

	@SelectProvider(method="checkRepeatPlanName",type=TblZgzzRectificationplanMapperSqlConfig.class)
	Integer checkRepeatPlanName(TblZgzzRectificationplan rectification) throws Exception;

	@Insert("INSERT INTO TBL_RECTIFICATION_ATT(ATTID,PLANID) VALUES (#{attId},#{planId})")
	void insertAttFileRelation(@Param("planId")String planId,@Param("attId") String attId) throws Exception;

	@Select("SELECT TZR.*,CASE WHEN TZR.PLANTYPE = 1 THEN TNP.PRJOECTNAME WHEN TZR.PLANTYPE = 2 THEN TTP.PLANNAME WHEN TZR.PLANTYPE = 3 OR TZR.PLANTYPE = 4 THEN TNW.PROJECTNAME ELSE '' END AS PROJECTNAME ,ZRR.REALNAME AS ZRRREALNAME , CTS.REALNAME AS CREATESTAFFNAME,HTS.REALNAME AS HANDLERNAME FROM TBL_ZGZZ_RECTIFICATIONPLAN TZR "
			+ " LEFT JOIN TBL_NBSJ_PROJECT TNP ON TZR.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON TZR.PROJECTID = TTP.TESTPLANID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON TZR.PROJECTID = TNW.PROJECTID LEFT JOIN TBL_STAFF ZRR ON TZR.RESPONSE = ZRR.STAFFID LEFT JOIN TBL_STAFF CTS ON TZR.CREATESTAFF = CTS.STAFFID "
			+ " LEFT JOIN TBL_STAFF HTS ON TZR.HANDLERID = HTS.STAFFID WHERE TZR.PLANID = #{planId}")
	TblZgzzRectificationplanVo selectEntityById(@Param("planId") String planId) throws Exception;

	@Delete("DELETE FROM TBL_RECTIFICATION_ATT WHERE ATTID = #{attId} AND PLANID = #{planId}")
	void deleteRectificationFileRela(@Param("planId") String planId,@Param("attId") String attId);

	@Delete("DELETE FROM TBL_RECTIFICATION_ISSUES WHERE ISSUESID = #{issuesId} AND PLANID = #{planId}")
	void deleteRectificationIssuesRela(@Param("planId") String planId,@Param("issuesId") String issuesId);

	@Delete("DELETE FROM TBL_RECTIFICATION_ATT WHERE PLANID = #{planId}")
	void deleteRectificationFileRelaByRid(@Param("planId") String planId) throws Exception;

	@Delete("DELETE FROM TBL_RECTIFICATION_ISSUES WHERE PLANID = #{planId}")
	void deleteRectificationIssuesRelaByRid(@Param("planId") String planId) throws Exception;

	@Select("SELECT STATUS FROM TBL_ZGZZ_RECTIFICATIONPLAN WHERE PLANID = #{planId}")
	Integer selectStatusById(@Param("planId") String planId) throws Exception;

	@SelectProvider(method="selectPageInfoList",type=TblZgzzRectificationplanMapperSqlConfig.class)
	List<TblZgzzRectificationplanVo> selectPageInfoList(TblZgzzRectificationplanVo plan,String sqlStr,TblStaffUtil loginStaff);

	@Delete("DELETE FROM TBL_RELATION_SHEET WHERE FORMID IN (SELECT RELAID FROM TBL_RECTIFICATION_ISSUES WHERE PLANID = #{planId}) AND OBJTYPE = #{objType} AND FORMTYPE = #{formtype} AND FORMCOL = #{formcol}")
	void deleteRelationSheet(@Param("planId")String planId,@Param("formtype") String formtype,@Param("formcol") String formcol,@Param("objType") String objType) throws Exception;

	@SelectProvider(method="selectAllListByExportt",type=TblZgzzRectificationplanMapperSqlConfig.class)
	List<TblZgzzRectificationplanVo> selectAllListByExportt(TblZgzzRectificationplanVo plan);

	@Select("SELECT * FROM TBL_ZGZZ_RECTIFICATIONPLAN WHERE PLANID IN (SELECT PLANID FROM TBL_ZGZZ_REPORTPLAN WHERE REPORTID = #{reportid})")
	List<TblZgzzRectificationplanVo> selectPlanListByReportId(@Param("reportid") String reportid) throws Exception;

	@Update("UPDATE TBL_ZGZZ_RECTIFICATIONPLAN SET STATUS = #{status} WHERE PLANID = #{planId}")
	void updateStatusById(@Param("planId")String planId, @Param("status")int status);

}
