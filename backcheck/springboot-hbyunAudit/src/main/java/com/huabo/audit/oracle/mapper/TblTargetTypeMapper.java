package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.huabo.audit.oracle.entity.TblTargetTypeEntity;

public interface TblTargetTypeMapper extends tk.mybatis.mapper.common.Mapper<TblTargetTypeEntity>{

	@Select("SELECT * from Tbl_NBSJ_TARGETTYPE t where t.parentId = #{parentId}  order by t.createTime ")
	List<TblTargetTypeEntity> getTree(BigDecimal parentId);
	
	@Select("SELECT * from Tbl_NBSJ_TARGETTYPE t "
			+ " LEFT JOIN TBL_NBSJ_TEMPLETE temp ON temp.templeteId = t.TEMPID   "
			+ " where temp.templeteId = #{tempId}   and  t.parentId is null ")
	List<TblTargetTypeEntity> getRoot(BigDecimal tempId);
	
	@Select("SELECT count(*) from Tbl_NBSJ_TARGETTYPE t "
			+ " LEFT JOIN TBL_NBSJ_TEMPLETE temp ON temp.templeteId = t.TEMPID   "
			+ " where temp.templeteId = #{tempId} "
			+ " AND t.parentId = #{targetId} ")
	Integer getCount(BigDecimal tempId,BigDecimal targetId);
	
	@Select("SELECT * from Tbl_NBSJ_TARGETTYPE t where t.targetId = #{nodeId} ")
	TblTargetTypeEntity getById(BigDecimal nodeId);
	
	@Select("SELECT	 DISTINCT target.* "
			+ "FROM	TBL_NBSJ_TARGETTYPE target "
			+ "LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON target.targetId = audi.targetId "
			+ "LEFT JOIN TBL_NBSJ_AUTHORIZATION auth on audi.programid = auth.programid "
			+ "LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id= auth.TEAMSTAFFID RIGHT "
			+ "JOIN TBL_NBSJ_OPERATE op on  auth. AUTHID = op. AUTHID "
			+ "WHERE  auth.projectid = #{projectId} "
			+ "and target.status=1 AND team.staffid = #{staffId}")
	List<TblTargetTypeEntity> getChildByUser(Integer projectId,BigDecimal staffId);
	
	@Select("SELECT	 DISTINCT target.* "
			+ "FROM	TBL_NBSJ_TARGETTYPE target "
			+ "LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON target.targetId = audi.targetId "
			+ "LEFT JOIN TBL_NBSJ_AUTHORIZATION auth on audi.programid = auth.programid "
			+ "LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id= auth.TEAMSTAFFID "
			+ "RIGHT JOIN TBL_NBSJ_OPERATE op on  auth. AUTHID = op. AUTHID "
			+ "WHERE  auth.projectid = #{projectId} "
			+ "and target.status=1 "
			+ "AND team.staffid = #{staffId} "
			+ "AND target.parentid = #{parentId}")
	List<TblTargetTypeEntity> getChildByUsers(BigDecimal parentId, Integer projectId,BigDecimal staffId);
	
	
	@Select("SELECT	* "
			+ "FROM	TBL_NBSJ_TARGETTYPE t "
			+ "WHERE  t.targetId = #{patentid}  order by t.createTime ")
	List<TblTargetTypeEntity> findByparent(BigDecimal patentid);
	
	@Select("SELECT * FROM TBL_NBSJ_TARGETTYPE where TEMPID=#{tempid} AND STATUS=0 and PARENTID is NULL")
	List<TblTargetTypeEntity> findByAllMB(String tempid);
	
	
	//==
	@InsertProvider(method="insertEntity",type=TblTargetTypeMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="targetId", keyColumn="TARGETID")
	void insertEntity(TblTargetTypeEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblTargetTypeMapperSqlConfig.class)
	void updateEntity(TblTargetTypeEntity plan) throws Exception;

    @Select("SELECT DISTINCT target.* FROM	TBL_NBSJ_TARGETTYPE target LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON target.targetId = audi.targetId LEFT JOIN TBL_NBSJ_AUTHORIZATION auth on audi.programid = auth.programid LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id= auth.TEAMSTAFFID WHERE  auth.projectid = #{projectId} AND target.PARENTID = #{targetId} AND team.staffid = #{staffid}")
	List<TblTargetTypeEntity> selectMyTarget(BigDecimal projectId, BigDecimal targetId, BigDecimal staffid) throws Exception;

    @Select("SELECT * FROM TBL_NBSJ_TARGETTYPE where PARENTID = #{parentid} ORDER BY CREATETIME")
	List<TblTargetTypeEntity> selectListByParentId(String parentid);
}
