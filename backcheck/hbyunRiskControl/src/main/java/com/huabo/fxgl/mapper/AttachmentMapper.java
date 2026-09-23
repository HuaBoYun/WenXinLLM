package com.huabo.fxgl.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.Attachment;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@Repository
public interface AttachmentMapper extends BaseMapper<Attachment> {
	
    List<Attachment> getAttList(@Param("table")String table,@Param("idname")String idname,@Param("id")BigDecimal id);
	
    @Select("SELECT	* FROM	TBL_ATTACHMENT WHERE	ATTID IN (	SELECT	ATTID	FROM	TBL_RISK_ATT	WHERE	RISKID =#{riskid})")
    List<Attachment> findtTblAttachmentByRisk(@Param("riskid") String riskid);

    @Select("select * from TBL_ATTACHMENT where ATTID in (select ATTID from TBL_RISK_RECSOL_ATT where RECTSOLID = #{param})")
    List<Attachment> findtTblAttachmentByRectsolid(String rectsolid);

    @Select("select * from TBL_ATTACHMENT where ATTID in (select ATTID from TBL_WORKSHEET_ATT where WORKSHEETID = #{id} )")
    List<Attachment> findAllByTblWorksheet(@Param("id") String id);

    @Select("select * from TBL_ATTACHMENT where ATTID in (select ATTID from TBL_RISK_COPINGATT where RISKCOPINGID = #{copingId})")
    List<Attachment> findAttachmentByCoping(String copingId);

    @Select("select attid,attname,attpath,attsize,uploadtime, uploader  from TBL_ATTACHMENT a  where ATTID in (select ATTID from TBL_RISK_ASSPLAN_Att where assplanid = #{planid})")
    Set<Attachment> getRiskAssplanAttList(String planid);
    
    @Select("select attid,attname,attpath,attsize,uploadtime, uploader  from TBL_ATTACHMENT a  where ATTID in (select ATTID from TBL_RISK_GROUPPLAN_Att where id = #{planid})")
    Set<Attachment> getRiskGroupPlanAttList(String planid);

    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_RISK_ATT WHERE RISKID = #{riskid})")
    List<Attachment> findAttListByRisk(String riskid);

    @Delete("DELETE FROM TBL_RISK_ATT WHERE  RISKID = #{attid} ")
    void deleteAtt(String attid);
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_RISK_REPORTING_ATT WHERE REPORTINGID = #{id})")
    List<Attachment> selectAttListByReportin(@Param("id")BigDecimal id);

    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_RISK_MONTHLY_EVALUATION_ATT WHERE EVALUATIONID = #{id})")
    List<Attachment> selectAttListByMonthlyEvaluation(@Param("id")String id);
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID = #{attId} ")
    Attachment selectEntityById(@Param("attId")BigDecimal attId);

    @Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
    void deleteEntity(BigDecimal attid);

    @Select("SELECT LEVELID FROM TBL_SECRECT_LEVEL WHERE LEVELTYPE = 4 AND LEVELNAME =#{attachmentLevel}")
    BigDecimal selectattachmentLevel(@io.lettuce.core.dynamic.annotation.Param("attachmentLevel")String attachmentLevel) throws Exception;

    @Select("SELECT COUNT(*) FROM TBL_SECRECT_LEVEL WHERE LEVELTYPE = 2 AND LEVELNAME = #{formlevel} AND  SECRECYMENUSCOPE LIKE concat('%',#{attachmentLevelId},'%')")
    Integer selectattachmentList(@io.lettuce.core.dynamic.annotation.Param("formlevel")String formlevel, @io.lettuce.core.dynamic.annotation.Param("attachmentLevelId")String attachmentLevelId) throws Exception;

}
