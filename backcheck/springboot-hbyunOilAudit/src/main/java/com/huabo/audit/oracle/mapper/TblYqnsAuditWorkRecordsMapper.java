package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsAuditWorkRecordsEntity;
import org.apache.ibatis.annotations.Select;
import org.springframework.ws.mime.Attachment;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Classname TblYqnsAuditWorkRecordsMapper
 * @Description TODO 审计工作记录
 * @Date 2023/10/9 10:16
 * @Created by GJ.C
 */
public interface TblYqnsAuditWorkRecordsMapper extends Mapper<TblYqnsAuditWorkRecordsEntity> {


    /**
     * 查询审计工作记录
     * @param entity 
     * @return
     */
    List<TblYqnsAuditWorkRecordsEntity> selectRecordsList(TblYqnsAuditWorkRecordsEntity entity);


    /**
     * 获取单独一个审计记录
     * @param id
     * @return
     */
    TblYqnsAuditWorkRecordsEntity selectRecordsById(Long id);


    /**
     * 新增审计记录
     * @param entity
     * @return
     */
    Integer insertEntity(TblYqnsAuditWorkRecordsEntity entity);


    /**
     * 修改审计记录
     * @param entity
     * @return
     */
    Integer updateEntity(TblYqnsAuditWorkRecordsEntity entity);


    /**
     * 直接删除数据
     * @param id
     * @return
     */
    Integer deleteEntity(Long id);


    /**
     * 获取自增Id
     * @return
     */
    Long getIdSequence();


    void insertATT(Long id, String aid);

    void deleteATT(Long id);

    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_WORK_RECORDS_ATT WHERE RECORDSID = #{id})")
    List<TblAttachment> selectAtt(BigDecimal id);

    @Select("SELECT * FROM TBL_YQNS_AUDIT_WORK_RECORDS WHERE ID IN (SELECT WORKREPORTID FROM TBL_YQNS_DRAFTREALWORKREPORT WHERE MANUSCRIPTID = #{id})")
	List<TblYqnsAuditWorkRecordsEntity> selectListByRealMyDraft(Long id) throws Exception;
}