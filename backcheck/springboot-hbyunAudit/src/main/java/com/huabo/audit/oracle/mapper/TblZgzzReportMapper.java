package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.datetime.BigDateTimeValueType;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblZgzzReport;
import com.huabo.audit.oracle.vo.TblZgzzReportVo;

import tk.mybatis.mapper.common.Mapper;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LHP
 * @since 2024-02-21
 */
public interface TblZgzzReportMapper extends Mapper<TblZgzzReport> {
	/**
	 * 保存整改报告与附件的关系
	 * @param attId		--附件主键
	 * @param reportid	--整改报告主键
	 * @throws Exception
	 */
	@Insert("INSERT INTO TBL_ZGZZREPORT_ATT(ATTID,REPORTID) VALUES (#{attId},#{reportid})")
	void saveFileRelation(@Param("attId") String attId,@Param("reportid") String reportid) throws Exception;

	@Delete("DELETE FROM TBL_ZGZZ_REPORTISSUES WHERE REPORTID = #{reportid}")
	void removeIssuesRelation(@Param("reportid") String reportid) throws Exception;

	@Delete("DELETE FROM TBL_ZGZZ_REPORTPLAN WHERE REPORTID = #{reportid}")
	void removerectificationPlanRelation(@Param("reportid") String reportid) throws Exception;

	@Insert("INSERT INTO TBL_ZGZZ_REPORTPLAN(REPORTID,PLANID) VALUES (#{reportid},#{planId})")
	void saveRectificationPlanRelation(@Param("reportid") String reportid,@Param("planId") String planId) throws Exception;

	@Insert("INSERT INTO TBL_ZGZZ_REPORTISSUES(REPORTID,ISSUESID,PLANID,CREATESTAFF) VALUES (#{reportid},#{issId},#{planId},#{staffId})")
	void saveIssuesRelation(@Param("reportid") String reportid,@Param("planId") String planId,@Param("issId") String issId,@Param("staffId") BigDecimal staffId) throws Exception;

	@Select("SELECT TZR.*,TS.REALNAME AS CREATESTAFFNAME,DEPT.ORGNAME AS DEPTNAME FROM TBL_ZGZZ_REPORT TZR LEFT JOIN TBL_STAFF TS ON TZR.CREATESTAFF = TS.STAFFID LEFT JOIN TBL_ORGANIZATION DEPT ON TZR.LINKDEPT = DEPT.ORGID WHERE TZR.REPORTID = #{reportid} ")
	@Results({
		@Result(column="CREATESTAFFNAME",property="createStaffName"),
		@Result(column="DEPTNAME",property="linkDeptName"),
	})
	TblZgzzReportVo selectEntityById(@Param("reportid") String reportid) throws Exception;

	@Delete("DELETE FROM TBL_ZGZZREPORT_ATT WHERE REPORTID = #{reportid} ")
	void deleteFileRelation(@Param("reportid") String reportid) throws Exception;

	@Delete("DELETE FROM TBL_ZGZZREPORT_ATT WHERE REPORTID = #{reportid} AND ATTID = #{attId} ")
	void deleteFileRelationByAttId(@Param("reportid") String reportid,@Param("attId") String attId) throws Exception;

	@SelectProvider(method="selectListByPageInfo",type=TblZgzzReportMapperSqlConfig.class)
	List<TblZgzzReportVo> selectListByPageInfo(TblZgzzReportVo report,TblStaffUtil loginStaff);

	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_ZGZZ_REPORT_ATT WHERE reportid = #{reportid})")
    List<TblAttachment> selectReportMeetFileList(String reportid) throws Exception;
	
	@Insert("insert into TBL_ZGZZ_REPORT_ATT (reportid,ATTID) values(#{reportid},#{attid})")
    Integer insertMeetAttInfo(String reportid, String attid);
	
	@Delete("DELETE FROM TBL_ZGZZ_REPORT_ATT WHERE ATTID=#{attid}")
    void deleteMeetFileInfoByAttId(String attid);
	
	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER,ISPYTHONFLAG FROM TBL_ATTACHMENT WHERE ATTID = #{attId} ")
    @Results({
            @Result(column="ATTID",property="attid"),
            @Result(column="ATTNAME",property="attname"),
            @Result(column="ATTPATH",property="attpath"),
            @Result(column="ATTSIZE",property="attsize"),
            @Result(column="MEMO",property="memo"),
            @Result(column="UPLOADTIME",property="uploadtime"),
            @Result(column="UPLOADER",property="uploader"),
            @Result(column="ISPYTHONFLAG",property="ispythonflag"),
    })
    TblAttachment selectAttEntityById(@Param("attId") String attId) throws Exception;
	
	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
    void deleteEntity(@Param("attid") BigDecimal attid) throws Exception;
	
}
