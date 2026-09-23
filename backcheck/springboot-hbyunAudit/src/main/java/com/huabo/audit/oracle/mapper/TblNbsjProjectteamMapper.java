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
import org.apache.ibatis.annotations.UpdateProvider;

import com.huabo.audit.oracle.entity.TblNbsjProjectTeamEntity;
import com.huabo.audit.oracle.entity.TblNbsjTeamstaffEntity;

public interface TblNbsjProjectteamMapper extends tk.mybatis.mapper.common.Mapper<TblNbsjProjectTeamEntity>{

	int saveProjectTeamInfo(Integer projectId, Integer teamId);

	int deleteProjectTeamInfo(Integer projectId, Integer teamId);
	
	
	
	//==
	@Select("SELECT TNA.* "
    		+ " FROM TBL_NBSJ_PROJECTTEAM TNA "
    		+ " LEFT JOIN TBL_NBSJ_PRO_TEAM T ON TNA.TEAMID = T.TEAMID "
    		+ " WHERE T.PROJECTID = #{projectid} ")
    @Results({
    	@Result(column="TEAMID",property="teamId"),
    	@Result(column="TEAMNAME",property="teamName"),
    })
    List<TblNbsjProjectTeamEntity> selectListTeamByProjectId(@Param("projectid") BigDecimal projectid) throws Exception;
	
	@Select("SELECT TS.REALNAME,TNS.STAFFID,TNS.STAFFTYPE,TNA.*,CUSR.REALNAME CREATEUSERNAME,TNS.ID "
    		+ " FROM TBL_NBSJ_PROJECTTEAM TNA "
    		+ " LEFT JOIN TBL_NBSJ_PRO_TEAM T ON TNA.TEAMID = T.TEAMID "
			+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID "
			+ " LEFT JOIN TBL_STAFF TS ON TNS.STAFFID = TS.STAFFID "
			+ " LEFT JOIN TBL_STAFF CUSR ON TNA.CREATEUSERID = CUSR.STAFFID "
    		+ " WHERE T.PROJECTID = #{projectid} "
    		+ " ORDER BY TNA.TEAMID,TNS.ID ")
    @Results({
    	@Result(column="TEAMID",property="teamid"),
    	@Result(column="STAFFID",property="staffid"),
    	@Result(column="STAFFTYPE",property="stafftype"),
    	@Result(column="REALNAME",property="staff.realname"),
    	@Result(column="CREATEUSERNAME",property="createUserName"),
    	@Result(column="CREATETIME",property="createTime"),
    	@Result(column="TEAMNAME",property="teamName"),
    	@Result(column="ID",property="id"),
    })
    List<TblNbsjTeamstaffEntity> selectListTeamZuYuanByProjectId(@Param("projectid") BigDecimal projectid) throws Exception;
	
	
	
	@InsertProvider(method="insertEntity",type=TblNbsjProjectTeamMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="teamId", keyColumn="TEAMID")
	void insertEntity(TblNbsjProjectTeamEntity plan) throws Exception;
	
	@UpdateProvider(method="updateEntity",type=TblNbsjProjectTeamMapperSqlConfig.class)
	void updateEntity(TblNbsjProjectTeamEntity plan) throws Exception;
	
	
	
	//==
	@Select("SELECT TNA.* "
    		+ " FROM TBL_NBSJ_PROJECTTEAM TNA "
//    		+ " LEFT JOIN TBL_NBSJ_PRO_TEAM T ON TNA.TEAMID = T.TEAMID "
    		+ " WHERE TNA.TEAMID = #{teamId} ")
    @Results({
    	@Result(column="TEAMID",property="teamId"),
    	@Result(column="TEAMNAME",property="teamName"),
    })
    TblNbsjProjectTeamEntity selectTeamByTeamId(@Param("teamId") BigDecimal teamId) throws Exception;
	
	@Select("SELECT TS.REALNAME,TNS.STAFFID,TNS.STAFFTYPE,TNA.TEAMID "
    		+ " FROM TBL_NBSJ_PROJECTTEAM TNA "
    		+ " LEFT JOIN TBL_NBSJ_PRO_TEAM T ON TNA.TEAMID = T.TEAMID "
			+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID "
			+ " LEFT JOIN TBL_STAFF TS ON TNS.STAFFID = TS.STAFFID "
    		+ " WHERE T.TEAMID = #{teamId} ")
    @Results({
    	@Result(column="TEAMID",property="teamid"),
    	@Result(column="STAFFID",property="staffid"),
    	@Result(column="STAFFTYPE",property="stafftype"),
    	@Result(column="REALNAME",property="staff.realname"),
    })
    List<TblNbsjTeamstaffEntity> selectListTeamZuYuanByTeamId(@Param("teamId") BigDecimal teamId) throws Exception;
	
	//==
	@InsertProvider(method="insertProTeam",type=TblNbsjProjectTeamMapperSqlConfig.class)
	void insertProTeam(BigDecimal teamId, BigDecimal projectid) throws Exception;
//    @Options(useGeneratedKeys=true, keyProperty="teamId", keyColumn="TEAMID")
	
	
//	@Insert("INSTER INTO TBL_NBSJ_PRO_TEAM(TEAMID,PROJECTID) VALUES (#{teamId},#{projectid})")
//	void insertProTeam(Integer teamId,Integer projectid) throws Exception;
	
	@Delete("DELETE FROM TBL_NBSJ_PROJECTTEAM WHERE TEAMID = #{teamId}")
    void deletePJEByTeamId(BigDecimal teamId) throws Exception;
	
	@Delete("DELETE FROM TBL_NBSJ_PRO_TEAM WHERE TEAMID = #{teamId}")
    void deletePTByTeamId(BigDecimal teamId) throws Exception;
	
	@Delete("DELETE FROM TBL_NBSJ_TEAMSTAFF WHERE TEAMID = #{teamId}")
    void deleteTSByTeamId(BigDecimal teamId) throws Exception;
	

}
