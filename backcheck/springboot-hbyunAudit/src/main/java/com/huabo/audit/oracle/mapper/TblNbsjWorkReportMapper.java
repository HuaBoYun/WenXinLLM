package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.entity.TblNbsjWorkreport;
import com.huabo.audit.oracle.vo.TblNbsjWorkReportVo;
import com.huabo.audit.util.PageInfo;

public interface TblNbsjWorkReportMapper extends tk.mybatis.mapper.common.Mapper<TblNbsjWorkreport> {
	@Select("SELECT * from TBL_NBSJ_WORKREPORT WHERE REPORTID= #{reportid} ")
    TblNbsjWorkreport getById(String reportid);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjWorkReportMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjWorkreport> pageInfo,TblNbsjWorkReportVo tblNbsjWorkReportVo,TblStaffUtil loginStaff) throws Exception;

    
    List<TblNbsjWorkreport> findList(@Param("tblNbsjWorkReportVo") TblNbsjWorkReportVo tblNbsjWorkReportVo);
    
    
    
    
    @Select("SELECT TNA.*,STAFF.REALNAME,ORG.ORGNAME "
    		+ "FROM TBL_NBSJ_WORKREPORT TNA "
    		+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.REPORTERID "
			+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.REPORTDEPARTMENTID "
    		+ " WHERE TNA.REPORTID = #{reportid}")
    @Results({
    	@Result(column="REPORTID",property="reportid"),
    	@Result(column="REPORTNAME",property="reportname"),
    	@Result(column="REPORTTIME",property="reporttime"),
    	@Result(column="REPORTTYPE",property="reporttype"),
    	@Result(column="REPORTMODE",property="reportmode"),
//    	@Result(column="REPORTER",property="reporter"),
//    	@Result(column="REPORTDEPARTMENT",property="reportdepartement"),
    	@Result(column="REPORTTEMPID",property="reporttempid"),
    	@Result(column="REPORTSTATUS",property="reportstatus"),
    	@Result(column="MEMO",property="memo"),
    	@Result(column="REALNAME",property="reporter.realname"),
    	@Result(column="ORGNAME",property="reportdepartment.orgname"),
    	@Result(column="REPORTER",property="reporter.staffid"),
    	@Result(column="REPORTDEPARTMENT",property="reportdepartment.orgid"),
    	@Result(column="PROJECTID",property="projectid"),
    })
   	TblNbsjWorkreport selectById(@Param("reportid") BigDecimal reportid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjWorkReportMapperSqlConfig.class)
    @Results({
    	@Result(column="REPORTID",property="reportid"),
    	@Result(column="REPORTNAME",property="reportname"),
    	@Result(column="REPORTTIME",property="reporttime"),
    	@Result(column="REPORTTYPE",property="reporttype"),
    	@Result(column="REPORTMODE",property="reportmode"),
//    	@Result(column="REPORTER",property="reporter"),
//    	@Result(column="REPORTDEPARTMENT",property="reportdepartement"),
    	@Result(column="REPORTTEMPID",property="reporttempid"),
    	@Result(column="REPORTSTATUS",property="reportstatus"),
    	@Result(column="MEMO",property="memo"),
    	@Result(column="REALNAME",property="reporter.realname"),
    	@Result(column="CREATESTAFFID",property="createstaffid"),
    	@Result(column="ORGNAME",property="reportdepartment.orgname"),
    })
	List<TblNbsjWorkreport> selectListByPageInfo(PageInfo<TblNbsjWorkreport> pageInfo,TblNbsjWorkReportVo tblNbsjWorkReportVo,TblStaffUtil loginStaff) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_WORKREPORT WHERE REPORTID = #{reportid}")
    void deleteById(BigDecimal reportid) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjWorkReportMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjWorkreport plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblNbsjWorkReportMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="reportid", keyColumn="REPORTID")
	void insertEntity(TblNbsjWorkreport plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjWorkReportMapperSqlConfig.class)
	void updateEntity(TblNbsjWorkreport plan) throws Exception;
    
    
    
    @Delete("DELETE FROM TBL_LEGAL_WORKREPORT_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(BigDecimal attid);
}
