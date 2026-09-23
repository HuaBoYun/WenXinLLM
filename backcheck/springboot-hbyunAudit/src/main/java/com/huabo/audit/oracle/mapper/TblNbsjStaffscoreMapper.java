package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblNbsjStaffscore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;

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


public interface TblNbsjStaffscoreMapper extends BaseMapper<TblNbsjStaffscore> {

    /**
     * 查询Staffscore
     */
	 @SelectProvider(method="selectNbsjStaffscoreByPageInfo",type=TblNbsjStaffscoreMapperSqlConfig.class)
	 @Results({
		 	@Result(column="staffScore_id",property="staffScoreid"),
		 	@Result(column="AUDITPROJECTNAME",property="auditProjectName"),
		 	@Result(column="PROJECTID",property="projectid"),
		 	@Result(column="TOTALSCORE",property="totalScore"),
		 	@Result(column="STATUS",property="status"),
		 	@Result(column="CREATESTAFFID",property="createstaffid"),
			@Result(column="AUDITORS",property="auditor.staffid",id=true),
			@Result(column="REALNAME",property="auditor.realname"),
		 })
	List<TblNbsjStaffscore> selectNbsjStaffscoreByPageInfo(PageInfo<TblNbsjStaffscore> pageInfo, BigDecimal orgId, TblNbsjStaffscore re,TblStaffUtil loginStaff) throws Exception;
	 
	 
	 

	 @Select("SELECT 	SS.STAFFSCORE_ID,STA.REALNAME,SS.AUDITORS,SS.AUDITPROJECTNAME,SS.TOTALSCORE,SS.PROJECTID,SS.STATUS,SS.SECRECTLEVELID,SS.STAFFSCOPEIDS,SS.STAFFSCOPENAMES "
	 		+ "FROM TBL_NBSJ_STAFFSCORE ss LEFT JOIN TBL_STAFF sta ON SS.auditors = sta.STAFFID where SS.STAFFSCORE_ID= #{staffScoreid}")
	 @Results({
		 	@Result(column="staffScore_id",property="staffScoreid"),
		 	@Result(column="AUDITPROJECTNAME",property="auditProjectName"),
		 	@Result(column="PROJECTID",property="projectid"),
		 	@Result(column="TOTALSCORE",property="totalScore"),
		 	@Result(column="STATUS",property="status"),
			@Result(column="AUDITORS",property="auditor.staffid",id=true),
			@Result(column="REALNAME",property="auditor.realname"),
		 })
	 TblNbsjStaffscore selectNbsjStaffscoreListByID(@Param("staffScoreid") BigDecimal staffScoreid) throws Exception;
	 

	@SelectProvider(method="selectNbsjStaffscoreByPageCount",type=TblNbsjStaffscoreMapperSqlConfig.class)
	Integer selectNbsjStaffscoreByPageCOunt( BigDecimal orgId, TblNbsjStaffscore re,TblStaffUtil loginStaff) throws Exception;
	
	
	 @Delete("DELETE FROM TBL_NBSJ_STAFFSCORE WHERE STAFFSCORE_ID = #{staffScoreid}")
	 void deleteStaffscoreById(@Param("staffScoreid")BigDecimal staffScoreid) throws Exception;
	 
	 
	 

    /**
     * 添加Staffscore
     * @return
     */
    @InsertProvider(method="insertEntity",type=TblNbsjStaffscoreMapperSqlConfig.class)
	 @Options(useGeneratedKeys=true, keyProperty="staffScoreid", keyColumn="staffScore_id")
	 void insertEntity(TblNbsjStaffscore re) throws Exception;

    /**
     * 修改Staffscore
     * @return
     */
    
	 @UpdateProvider(method="updateEntity",type=TblNbsjStaffscoreMapperSqlConfig.class)
	 void updateEntity(TblNbsjStaffscore re) throws Exception;
	 
	 @SelectProvider(method="selectNbsjStaffscoreList",type=TblNbsjStaffscoreMapperSqlConfig.class)
	 @Results({
		 	@Result(column="staffScore_id",property="staffScoreid"),
		 	@Result(column="AUDITPROJECTNAME",property="auditProjectName"),
		 	@Result(column="PROJECTID",property="projectid"),
		 	@Result(column="TOTALSCORE",property="totalScore"),
		 	@Result(column="STATUS",property="status"),
			@Result(column="AUDITORS",property="auditor.staffid",id=true),
			@Result(column="REALNAME",property="auditor.realname"),
		 })
	List<TblNbsjStaffscore> selectNbsjStaffscoreList(BigDecimal orgId, TblNbsjStaffscore re) throws Exception;
	 
	 
}
