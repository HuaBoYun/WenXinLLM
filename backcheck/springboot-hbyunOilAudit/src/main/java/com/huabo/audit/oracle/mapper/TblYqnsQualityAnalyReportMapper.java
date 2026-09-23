package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsQualityAnalyReportEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Classname TblYqnsQualityAnalyReportMapper
 * @Description TODO 央企内审-审计实施-质量分析报告
 * @Date  2023/10/28 14:40
 * @Created by GJ.C
 */
public interface TblYqnsQualityAnalyReportMapper extends BaseMapper<TblYqnsQualityAnalyReportEntity> {

    /**
     * 查询关联的附件
     * @param id
     * @return
     */
    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_QUALITY_REPORT_ATTACH " +
            " WHERE REPORTID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    /**
     * 删除关联附件信息 通过id
     * @param id
     */
    @Delete("DELETE FROM TBL_YQNS_QUALITY_REPORT_ATTACH WHERE REPORTID = #{id}")
    void deleteAttByPk(String id);


    /**
     * 删除关联附件信息 通过附件id
     * @param attid
     */
    @Delete("DELETE FROM TBL_YQNS_QUALITY_REPORT_ATTACH WHERE ATTID = #{attid}")
    void deleteAttById(String attid);

    /**
     * 保存附件信息
     * @param id
     * @param attid
     */
    @Insert("INSERT INTO TBL_YQNS_QUALITY_REPORT_ATTACH(REPORTID,ATTID) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}