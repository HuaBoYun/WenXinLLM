package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjReformSolution;

import cn.hutool.json.JSONObject;

public interface TblNbsjReformSolutionMapper extends BaseMapper<TblNbsjReformSolution>{
	
	 @Delete("DELETE FROM TBL_NBSJ_REFORM_SOLUTION WHERE SOLUTIONID = #{solutionid}")
	 void deleteSolutionById(@Param("solutionid")BigDecimal solutionid) throws Exception;
	 
	 
	 @InsertProvider(method="insertEntity",type=TblNbsjReformSolutionMapperSqlConfig.class)
	 @Options(useGeneratedKeys=true, keyProperty="solutionid", keyColumn="SOLUTIONID")
	 void insertEntity(TblNbsjReformSolution solution) throws Exception;
	 
	 
	 
	 @UpdateProvider(method="updateEntity",type=TblNbsjReformSolutionMapperSqlConfig.class)
	 void updateEntity(TblNbsjReformSolution solution) throws Exception;
	  
	 
	 @SelectProvider(method="selectNbsjReformSolutionByPageInfo",type=TblNbsjReformSolutionMapperSqlConfig.class)
	 @Results({
	 	@Result(column="SOLUTIONID",property="solutionid"),
	 	@Result(column="SOLUTIONCODE",property="solutioncode"),
	 	@Result(column="SOLUTIONNAME",property="solutionname"),
	 	@Result(column="enddate",property="enddate"),
	 	@Result(column="RUNSTATUS",property="runstatus"),
	 	@Result(column="STAFFID",property="createStaff.staffid",id=true),
	 	@Result(column="REALNAME",property="createStaff.realname"),
	 	@Result(column="zgstaffid",property="reformUser.staffid",id=true),
	 	@Result(column="zgname",property="reformUser.realname"),
	 	@Result(column="zgstatus",property="zgstatus"),
	 })
	List<TblNbsjReformSolution> selectNbsjReformSolutionByPageInfo(PageInfo<TblNbsjReformSolution> pageInfo, BigDecimal orgId, TblNbsjReformSolution solution,String type) throws Exception;

	/**
	 *
	 * @param orgId
	 * @param re
	 * @param type
	 * @return
	 * @throws Exception
	 */
	List<TblNbsjReformSolution> findNbsjReformSolution(@Param("orgId") BigDecimal orgId, @Param("re") TblNbsjReformSolution re,@Param("type") String type) throws Exception;
	 
	 
	 @Select("SELECT s.*,sta.REALNAME,sta.STAFFID,ST.REALNAME zgname,ST.STAFFID zgstaffid,PR.PROJECTID,PR.PRJOECTNAME  FROM TBL_NBSJ_REFORM_SOLUTION s  LEFT JOIN TBL_NBSJ_PROJECT pr on s.PROJECTID=PR.PROJECTID LEFT JOIN TBL_STAFF sta on s.STAFFID=STA.STAFFID  LEFT JOIN TBL_STAFF st ON s.reformuserid = ST.STAFFID WHERE SOLUTIONID = #{solutionid}")
	 @Results({
	 	@Result(column="SOLUTIONID",property="solutionid"),
	 	@Result(column="SOLUTIONCODE",property="solutioncode"),
	 	@Result(column="SOLUTIONNAME",property="solutionname"),
	 	@Result(column="enddate",property="enddate"),
	 	@Result(column="RUNSTATUS",property="runstatus"),
	 	@Result(column="STAFFID",property="createStaff.staffid",id=true),
	 	@Result(column="REALNAME",property="createStaff.realname"),
	 	@Result(column="zgstaffid",property="reformUser.staffid",id=true),
	 	@Result(column="zgname",property="reformUser.realname"),
		@Result(column="BSJDWZRRID",property="bsjdwzrrid"),
		@Result(column="BSJDWZFR",property="bsjdwzfr"),
	 	@Result(column="zgstatus",property="zgstatus"),
	 	@Result(column="PROJECTID",property="relatedProject.projectId"),
	 	@Result(column="PRJOECTNAME",property="relatedProject.prjoectName"),
	 })
	 TblNbsjReformSolution selectNbsjReformSolutionListByID(@Param("solutionid") BigDecimal solutionid) throws Exception;
	 
	 
	  @Insert("INSERT INTO TBL_NBSJ_REFORM_SOLUTION_ATT(SOLUTIONID,ATTID) VALUES (#{solutionid},#{aid})")
	  void insertAttInfoAtt(@Param("solutionid")BigDecimal solutionid,@Param("aid") String aid) throws Exception;

	  
	  @Insert("DELETE FROM TBL_NBSJ_REFORM_SOLUTION_ATT where  SOLUTIONID=#{solutionid}")
	  void deleteAttInfoAttBySolutionid(@Param("solutionid") String solutionid) throws Exception;
	  
	  
	  @Insert("DELETE FROM TBL_NBSJ_REFORM_SOLUTION_ATT where  ATTID=#{aid}")
	  void deleteAttInfoAttid(@Param("aid") String aid) throws Exception;

	  @SelectProvider(method="selectNbsjReformSolutionByCount",type=TblNbsjReformSolutionMapperSqlConfig.class)
	  Integer selectNbsjReformSolutionByCount(BigDecimal orgId, TblNbsjReformSolution solution,String type) throws Exception;
	  
	  
	  @SelectProvider(method="selectNbsjReformSolutionByjg",type=TblNbsjReformSolutionMapperSqlConfig.class)
	  List<JSONObject> findAlls2(BigDecimal solutionid) throws Exception;
		 
	  
	  
	 @Select("SELECT * FROM TBL_NBSJ_REFORM_SOLUTION  WHERE SOLUTIONID = #{solutionid} and RUNSTATUS=2 ")
	 TblNbsjReformSolution selectNbsjReformSolutionListByIDnew(@Param("solutionid") BigDecimal solutionid) throws Exception;
		
	 @Select("SELECT * FROM TBL_NBSJ_REFORM_SOLUTION  WHERE SOLUTIONID = #{solutionid} ")
	 TblNbsjReformSolution selectNbsjReformSolutiByI(@Param("solutionid") BigDecimal solutionid) throws Exception;
		  
		 
}
