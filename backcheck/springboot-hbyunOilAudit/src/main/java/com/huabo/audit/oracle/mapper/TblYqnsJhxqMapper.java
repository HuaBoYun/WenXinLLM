package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsJhxq;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHXQ(计划需求)】的数据库操作Mapper
 * @Entity TblYqnsJhxq
 */
public interface TblYqnsJhxqMapper extends BaseMapper<TblYqnsJhxq> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsJhxqMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsJhxq> pageInfo, TblYqnsJhxq vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsJhxqMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsJhxq> selectListByPageInfo(PageInfo<TblYqnsJhxq> pageInfo, TblYqnsJhxq vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_JHXQ_ATT " +
            " WHERE jhxqid = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_JHXQ_ATT WHERE jhxqid = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_JHXQ_ATT(jhxqid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);

}




