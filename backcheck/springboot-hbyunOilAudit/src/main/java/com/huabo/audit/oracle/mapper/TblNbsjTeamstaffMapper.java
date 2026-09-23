package com.huabo.audit.oracle.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjTeamstaffEntity;

import java.util.List;

public interface TblNbsjTeamstaffMapper extends BaseMapper<TblNbsjTeamstaffEntity> {

	int deleteByTeamId(Integer teamId);
	
	@InsertProvider(method="insertEntity",type=TblNbsjTeamstaffMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	void insertEntity(TblNbsjTeamstaffEntity plan) throws Exception;
	
	@UpdateProvider(method="updateEntity",type=TblNbsjTeamstaffMapperSqlConfig.class)
	void updateEntity(TblNbsjTeamstaffEntity plan) throws Exception;
	
	@Delete("DELETE FROM TBL_NBSJ_TEAMSTAFF WHERE TEAMID = #{teamid} ")
    void deleteZYByTeamId(Integer teamid) throws Exception;
	
	@Select("SELECT TNA.* FROM TBL_NBSJ_TEAMSTAFF TNA  WHERE TNA.ID = #{teamId}")
	TblNbsjTeamstaffEntity selectById(Integer teamId) throws Exception;

	
	
	@Select("SELECT count (*) FROM TBL_NBSJ_TEAMSTAFF op "
			+ " LEFT JOIN TBL_NBSJ_PRO_TEAM ptlink ON ptlink.TEAMID = op. TEAMID"
    		+ " WHERE ptlink.projectid = #{projectid}")
    Integer getCountByProject(Integer projectid) throws Exception;


	@Select("SELECT * FROM TBL_NBSJ_TEAMSTAFF " +
			"WHERE TEAMID IN (" +
			"SELECT TEAMID FROM TBL_NBSJ_PRO_TEAM " +
			"WHERE PROJECTID = #{projectid}) " +
			"AND STAFFTYPE=0")
	List<TblNbsjTeamstaffEntity> getByProjectid (Integer projectid) throws Exception;
	
}
