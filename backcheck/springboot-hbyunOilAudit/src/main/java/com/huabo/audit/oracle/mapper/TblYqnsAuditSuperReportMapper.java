package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.BalanceProjectEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsAuditOverseeRecordsEntity;
import com.huabo.audit.oracle.entity.TblYqnsAuditSuperReport;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TblYqnsAuditSuperReportMapper extends BaseMapper<TblYqnsAuditSuperReport> {

//    /**
//     * 查询列表
//     * @param entity
//     * @return
//     */
//    @SelectProvider(method = "selectReportList", type = TblYqnsAuditSuperReportMapperSqlConfig.class)
//    List<TblYqnsAuditSuperReport> selectReportList(PageInfo<TblYqnsAuditSuperReport> pageInfo, TblYqnsAuditSuperReport entity) throws Exception;

    /**
     * 获取单条报告详情
     * @param id
     * @return
     */
    @Select("SELECT * FROM TBL_YQNS_AUDIT_SUPER_REPORT WHERE ID = #{id}")
/*
    @ResultMap(value= "tblYqnsAuditSuperReportMap")
*/
    TblYqnsAuditSuperReport selectReportById(Long id) throws Exception;

    /**
     * 新增一条审计督导报告
     * @param tblYqnsAuditSuperReport
     * @return
     */
    @InsertProvider(method="saveReport", type=TblYqnsAuditSuperReportMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void saveReport(TblYqnsAuditSuperReport tblYqnsAuditSuperReport) throws Exception;

    /**
     * 修改审计督导报告
     * @param tblYqnsAuditSuperReport
     * @return
     */
    @UpdateProvider(method="updateReport", type=TblYqnsAuditSuperReportMapperSqlConfig.class)
    void updateReport(TblYqnsAuditSuperReport tblYqnsAuditSuperReport) throws Exception;

    /**
     * 删除审计督导报告
     * @param ids
     * @return
     */
    @DeleteProvider(method="deleteByIds", type=TblYqnsAuditSuperReportMapperSqlConfig.class)
    void deleteReoprt(String ids) throws Exception;


 /*   @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SUPER_REPORT_ATTACH " +
            " WHERE REVIEWID = #{id})")*/

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SUPER_REPORT_ATTACH " +
            " WHERE REPORTID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SUPER_REPORT_ATTACH WHERE REPORTID = #{id}")
    void deleteAttByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SUPER_REPORT_ATTACH WHERE ATTID = #{attid}")
    void deleteAttById(String attid);

    @Insert("INSERT INTO TBL_YQNS_SUPER_REPORT_ATTACH(REPORTID,ATTID) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}
