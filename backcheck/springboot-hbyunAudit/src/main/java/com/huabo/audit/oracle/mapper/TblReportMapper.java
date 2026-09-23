package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.*;

import com.huabo.audit.util.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.entity.TblReportEntity;
import com.huabo.audit.oracle.vo.TblReportVo;

public interface TblReportMapper extends tk.mybatis.mapper.common.Mapper<TblReportEntity> {
	@Select("SELECT * from TBL_REPORT WHERE REPORTID= #{reportid} ")
    TblReportEntity getById(String reportid);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblReportMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblReportEntity> pageInfo,TblReportVo tblReportVo,Integer orgid,Integer projectId,TblStaffUtil loginStaff ) throws Exception;

    @Select("SELECT TNA.* FROM TBL_REPORT TNA  WHERE TNA.REPORTID = #{reportid}")
    @Results({
    	@Result(column="REPORTID",property="reportid"),
    	@Result(column="REPORTNAME",property="reportname"),
    	@Result(column="REPORTTIME",property="reporttime"), 
    	@Result(column="REPORTTYPE",property="reporttype"),
    	@Result(column="REPORTMODE",property="reportmode"),
    	@Result(column="REPORTER",property="reporter"),
    	@Result(column="REPORTDEPARTMENT",property="reportdepartment"),
    	@Result(column="REPORTERID",property="reporterid"),
    	@Result(column="REPORTDEPARTMENTID",property="reportdepartmentid"),
    })
   	TblReportEntity selectById(@Param("reportid") BigDecimal reportid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblReportMapperSqlConfig.class)
    @Results({
    	@Result(column="REPORTID",property="reportid"),
    	@Result(column="REPORTNAME",property="reportname"),
    	@Result(column="REPORTTIME",property="reporttime"),
    	@Result(column="REPORTTYPE",property="reporttype"),
    	@Result(column="REPORTMODE",property="reportmode"),
    	@Result(column="REPORTER",property="reporter"),
    	@Result(column="REPORTDEPARTMENT",property="reportdepartment"),
    	@Result(column="CREATESTAFFID",property="createstaffid"),
    })
	List<TblReportEntity> selectListByPageInfo(PageInfo<TblReportEntity> pageInfo,TblReportVo tblReportVo,Integer orgid,Integer projectId,TblStaffUtil loginStaff ) throws Exception;

    @Delete("DELETE FROM TBL_REPORT WHERE REPORTID = #{reportid}")
    void deleteById(BigDecimal reportid) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblReportMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblReportEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblReportMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="reportid", keyColumn="REPORTID")
	void insertEntity(TblReportEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblReportMapperSqlConfig.class)
	void updateEntity(TblReportEntity plan) throws Exception;

	//先删除
	@Delete("DELETE FROM TBL_LEGAL_REPORT_ATT WHERE REPORTID=#{reportId} AND FILETYPE IS NULL")
	void deleteAttmentRelationREPORT(@Param("reportId")BigDecimal reportId);

	//再添加
	@Insert("INSERT INTO TBL_LEGAL_REPORT_ATT(REPORTID,ATTID) VALUES(#{reportId},#{id})")
	void insertAttmentRelationREPORT(@Param("id")String id, @Param("reportId")BigDecimal reportId);
	
	//复核意见稿-删除
	@Delete("DELETE FROM TBL_LEGAL_REPORT_ATT WHERE REPORTID=#{reportId} AND FILETYPE = 2")
	void deleteAttRelaOpinionREPORT(@Param("reportId")BigDecimal reportId);
	
	//复核意见稿-新增
	@Insert("INSERT INTO TBL_LEGAL_REPORT_ATT(REPORTID,ATTID,FILETYPE) VALUES(#{reportId},#{id},2)")
	void insertAttRelaOpinionREPORT(@Param("id")String id, @Param("reportId")BigDecimal reportId);
	
	//终稿-删除
	@Delete("DELETE FROM TBL_LEGAL_REPORT_ATT WHERE REPORTID=#{reportId} AND FILETYPE = 3")
	void deleteAttRelaFinalREPORT(@Param("reportId")BigDecimal reportId);
	
	//终稿-新增
	@Insert("INSERT INTO TBL_LEGAL_REPORT_ATT(REPORTID,ATTID,FILETYPE) VALUES(#{reportId},#{id},3)")
	void insertAttRelaFinalREPORT(@Param("id")String id, @Param("reportId")BigDecimal reportId);

	//==
	@Select("SELECT * from TBL_REPORT WHERE 1=1 "
			+ " AND PROJECTID = #{projectid} "
			+ " AND reportstatus!=3 ")
	List<TblReportEntity> findNoReportByProjectId(Integer projectid);
	
	@Delete("DELETE FROM TBL_LEGAL_REPORT_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(BigDecimal attid);
	
	
	@Update("UPDATE TBL_REPORT SET XFRYIDS=#{userids},XFRYNAMES=#{usernames} WHERE reportid = #{reportid}")
    void xfry(String reportid,String userids,String usernames) throws Exception;
    
}
