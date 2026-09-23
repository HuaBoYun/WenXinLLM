package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsFgldhz;
import com.huabo.audit.oracle.entity.TblYqnsProposalNoticeEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Classname TblYqnsProposalNoticeMapper
 * @Description TODO 央企模块-计划编制-审计立项建议通知
 * @Date  2023/10/28 14:40
 * @Created by GJ.C
 */
public interface TblYqnsProposalNoticeMapper extends BaseMapper<TblYqnsProposalNoticeEntity> {

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_PROPOSAL_NOTICE_ATT " +
            " WHERE NOTICEID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_PROPOSAL_NOTICE_ATT WHERE NOTICEID = #{id}")
    void deleteAttByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_PROPOSAL_NOTICE_ATT WHERE ATTID = #{attid}")
    void deleteAttById(String attid);

    @Insert("INSERT INTO TBL_YQNS_PROPOSAL_NOTICE_ATT(NOTICEID,ATTID) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsProposalNoticeMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsProposalNoticeEntity> pageInfo, TblYqnsProposalNoticeEntity vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsProposalNoticeMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsProposalNoticeEntity> selectListByPageInfo(PageInfo<TblYqnsProposalNoticeEntity> pageInfo, TblYqnsProposalNoticeEntity vo) throws Exception;

}