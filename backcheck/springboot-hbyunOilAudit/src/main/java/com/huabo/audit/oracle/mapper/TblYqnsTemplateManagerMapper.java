package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsQualityAnalyReportEntity;
import com.huabo.audit.oracle.entity.TblYqnsTemplateManagerEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Classname TblYqnsTemplateManagerMapper
 * @Description TODO 央企内审-基础配置-模板管理
 * @Date  2023/10/28 14:40
 * @Created by GJ.C
 */
public interface TblYqnsTemplateManagerMapper extends BaseMapper<TblYqnsTemplateManagerEntity> {

    /**
     * 查询关联的附件
     * @param id
     * @return
     */
    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_TEMPLATE_MANAGER_ATT " +
            " WHERE TEMPLATEID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    /**
     * 删除关联附件信息 通过id
     * @param id
     */
    @Delete("DELETE FROM TBL_YQNS_TEMPLATE_MANAGER_ATT WHERE TEMPLATEID = #{id}")
    void deleteAttByPk(String id);


    /**
     * 删除关联附件信息 通过附件id
     * @param attid
     */
    @Delete("DELETE FROM TBL_YQNS_TEMPLATE_MANAGER_ATT WHERE ATTID = #{attid}")
    void deleteAttById(String attid);

    /**
     * 保存附件信息
     * @param id
     * @param attid
     */
    @Insert("INSERT INTO TBL_YQNS_TEMPLATE_MANAGER_ATT(TEMPLATEID,ATTID) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}