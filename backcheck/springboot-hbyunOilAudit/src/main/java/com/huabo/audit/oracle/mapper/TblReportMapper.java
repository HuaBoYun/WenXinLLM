package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblReportEntity;
import com.huabo.audit.oracle.vo.TblReportVo;

public interface TblReportMapper extends tk.mybatis.mapper.common.Mapper<TblReportEntity> {
	@Select("SELECT * from TBL_REPORT WHERE REPORTID= #{reportid} ")
    TblReportEntity getById(String reportid);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblReportMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblReportEntity> pageInfo,TblReportVo tblReportVo,Integer orgid,Integer projectId) throws Exception;

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
   	TblReportEntity selectById(@Param("reportid") Integer reportid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblReportMapperSqlConfig.class)
    @Results({
    	@Result(column="REPORTID",property="reportid"),
    	@Result(column="REPORTNAME",property="reportname"),
    	@Result(column="REPORTTIME",property="reporttime"),
    	@Result(column="REPORTTYPE",property="reporttype"),
    	@Result(column="REPORTMODE",property="reportmode"),
    	@Result(column="REPORTER",property="reporter"),
    	@Result(column="REPORTDEPARTMENT",property="reportdepartment"),
    })
	List<TblReportEntity> selectListByPageInfo(PageInfo<TblReportEntity> pageInfo,TblReportVo tblReportVo,Integer orgid,Integer projectId) throws Exception;

    @Delete("DELETE FROM TBL_REPORT WHERE REPORTID = #{reportid}")
    void deleteById(Integer reportid) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblReportMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblReportEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblReportMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="reportid", keyColumn="REPORTID")
	void insertEntity(TblReportEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblReportMapperSqlConfig.class)
	void updateEntity(TblReportEntity plan) throws Exception;

	//先删除
	@Delete("DELETE FROM TBL_LEGAL_REPORT_ATT WHERE REPORTID=#{reportId}")
	void deleteAttmentRelationREPORT(@io.lettuce.core.dynamic.annotation.Param("reportId")Integer reportId);

	//再添加
	@Insert("INSERT INTO TBL_LEGAL_REPORT_ATT(REPORTID,ATTID) VALUES(#{reportId},#{id})")
	void insertAttmentRelationREPORT(@io.lettuce.core.dynamic.annotation.Param("id")String id, @io.lettuce.core.dynamic.annotation.Param("reportId")Integer reportId);

	//==
	@Select("SELECT * from TBL_REPORT WHERE 1=1 "
			+ " AND PROJECTID = #{projectid} "
			+ " AND reportstatus!=3 ")
	List<TblReportEntity> findNoReportByProjectId(Integer projectid);
	
	@Delete("DELETE FROM TBL_LEGAL_REPORT_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(Integer attid);
}
