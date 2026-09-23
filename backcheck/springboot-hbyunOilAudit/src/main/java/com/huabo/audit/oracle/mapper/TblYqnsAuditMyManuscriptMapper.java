package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsAuditMyManuscriptEntity;
import com.huabo.audit.vo.result.FlowTaskInfo;

import lombok.Delegate;

/**
 * @Classname TblYqnsAuditMyManuscriptMapper
 * @Description TODO 央企内审-基础配置-我的底稿
 * @Date 2023/10/18 10:16
 * @Created by GJ.C
 */
public interface TblYqnsAuditMyManuscriptMapper extends BaseMapper<TblYqnsAuditMyManuscriptEntity> {


    //=======我的底稿自动编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(SHEETCODE,INSTR(DRAFTNUMBER,'-',-1)+1)))  "
            + " FROM TBL_YQNS_AUDIT_MY_MANUSCRIPT "
            + " WHERE 1=1 ")
    String selectMaxDraftNumber() throws Exception;
    
    @Insert("INSERT INTO TBL_YQNS_MY_MANUSCRIPT_ATT(ID, ATTID) VALUES (#{id}, #{attId})")
	void insetFileRelation(String attId, String id) throws Exception;
    
    @Insert("DELETE FROM TBL_YQNS_MY_MANUSCRIPT_ATT WHERE ID = #{id}")
	void delFileRelation( String id) throws Exception;
    
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_MY_MANUSCRIPT_ATT WHERE ID = #{id})")
    List<TblAttachment> findAttachmentListByMuManuAtt(String id);

    @Select("SELECT REALNAME FROM TBL_STAFF WHERE STAFFID = #{id} ") 
    String selectRealnameById(String id);

    @Insert("INSERT INTO TBL_YQNS_DRAFTREALWORKREPORT(WORKREPORTID,MANUSCRIPTID) VALUES (#{rid},#{id})")
	void insertRealRpoertDraft(@Param("rid") String rid,@Param("id") Long id) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_DRAFTREALWORKREPORT WHERE MANUSCRIPTID = #{id}")
	void deleteRelaWorkRpeort(Long id) throws Exception;
    
    
    @Select("SELECT * FROM TBL_YQNS_AUDIT_MY_MANUSCRIPT WHERE PROJECTID = #{projectid}    and PROBLEMDRAFT=#{probleMdraft} ")
    List<TblYqnsAuditMyManuscriptEntity> findbyPorjectid(BigDecimal projectid,String probleMdraft);
    
    @Update("UPDATE TBL_YQNS_AUDIT_MY_MANUSCRIPT SET  DRAFTNUMBER=#{draftNumber}   WHERE ID=#{id}  ")
    void updateBycode(Long id,String draftNumber) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_DRAFTREALWORKREPORT WHERE MANUSCRIPTID = #{myDraftId} AND WORKREPORTID = #{workReprotId}")
	void deleteMyDraftWorkReportRela(@Param("workReprotId")Long workReprotId,@Param("myDraftId") Long myDraftId) throws Exception;
    
    
    @Select("SELECT TFT.CURRENTSTAFFID,TS.REALNAME AS CURRENTSTAFFNAME , TFT.CREATETIME,TFT.COMMONT ,TFT.THISSTEPID,TFT.FROMID FROM TBL_FLOW_TASKINFO TFT LEFT JOIN TBL_STAFF TS ON TFT.CURRENTSTAFFID = TS.STAFFID" + 
    		" WHERE TFT.FLOWID IN (SELECT YMWORKFROM FROM TBL_SYSTEM_YMWORK WHERE TABLEID = #{sheetid}) AND TAKSID IN (" + 
    		" SELECT MAX(TAKSID) FROM TBL_FLOW_TASKINFO WHERE CURRENTROLE = #{stepName} AND FROMID IN (${dgIds}) GROUP BY FROMID,CURRENTROLE)" + 
    		" ORDER BY TFT.TASKGROUPID,TFT.TAKSID DESC")
	List<FlowTaskInfo> selectApprovalInfoList(@Param("sheetid")String sheetid,@Param("dgIds") String dgIds,@Param("stepName") String stepName);
}