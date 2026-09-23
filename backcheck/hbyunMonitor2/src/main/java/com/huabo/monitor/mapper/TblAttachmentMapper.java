package com.huabo.monitor.mapper;
import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.DELETE;

import org.apache.ibatis.annotations.*;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblAttachment;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-08-29
 */
@Mapper
public interface TblAttachmentMapper extends BaseMapper<TblAttachment> {

@Select("SELECT	* FROM	TBL_ATTACHMENT WHERE	ATTID IN (	SELECT	ATTID	FROM	${table}	WHERE	${idname} =#{id})")
    List<TblAttachment> getAttList(@Param("table")String table,@Param("idname")String idname,@Param("id")BigDecimal id);

	
     @Select("${sql}")
     List<TblAttachment> getListBySql(@Param("sql") String sql);
     
     @SelectProvider(method="selectListByPageInfo",type=TblAttachmentMapperSqlConfig.class)
     @Results({
     	@Result(column="ATTNAME",property="attname"),
     	@Result(column="ATTSIZE",property="attsize"),
     	@Result(column="UPLOADTIME",property="uploadtime"),
     	@Result(column="UPLOADER",property="uploader"),
     })
 	List<TblAttachment> selectListByPageInfo(PageInfo<TblAttachment> pageInfo,String attname,BigDecimal orgid) throws Exception;
     
   //==附件列表
 	@SelectProvider(method="selectCountByPageInfo",type=TblAttachmentMapperSqlConfig.class)
    	Integer selectCountByPageInfo(PageInfo<TblAttachment> pageInfo, String attname, BigDecimal orgid) throws Exception;
 	
 	@UpdateProvider(method = "updateTblBugsInTblAttachment", type = TblAttachmentMapperSqlConfig.class)
    void updateTblBugsInTblAttachment(TblAttachment attachment);


	@InsertProvider(type=TblAttachmentMapperSqlConfig.class,method="insertEntity")
	@Options(useGeneratedKeys=true, keyProperty="attid", keyColumn="ATTID")
	void insertEntity(TblAttachment tblAttachmentEntity) throws Exception;

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE attid=#{attid}")
	void deleteEntity(@Param("attid")BigDecimal attid);
	
	@Insert("INSERT INTO Tbl_TASKFIND_ATT(ATTID,FINDID) VALUES(#{attid},#{findid})")
	void insertTaskFindAtt(@Param("attid")BigDecimal attid,@Param("findid")BigDecimal findid);
	
	
	@Insert("INSERT INTO TBL_GROUPTESTPLAN_ATT(ATTID,ID) VALUES(#{attid},#{id})")
	void insertGroupTestPlanAtt(@Param("attid")BigDecimal attid,@Param("id")BigDecimal id);
	
	@Delete("DELETE FROM TBL_GROUPTESTPLAN_ATT WHERE ATTID=#{attid} ")
	void deleteGroupTestPlanAtt(@Param("attid")BigDecimal attid);
	 
	
	@Delete("DELETE FROM Tbl_TASKFIND_ATT WHERE ATTID=#{attid} ")
	void deleteTaskFindAtt(@Param("attid")BigDecimal attid);

	@Select("SELECT LEVELID FROM TBL_SECRECT_LEVEL WHERE LEVELTYPE = 4 AND LEVELNAME =#{attachmentLevel}")
	BigDecimal selectattachmentLevel(@Param("attachmentLevel")String attachmentLevel) throws Exception;

	@Select("SELECT COUNT(*) FROM TBL_SECRECT_LEVEL WHERE LEVELTYPE = 2 AND LEVELNAME = #{formlevel} AND  SECRECYMENUSCOPE LIKE concat('%',#{attachmentLevelId},'%')")
	Integer selectattachmentList(@Param("formlevel")String formlevel, @Param("attachmentLevelId")String attachmentLevelId) throws Exception;

	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID=#{id}")
	TblAttachment getOne(@Param("id")String id);
}
