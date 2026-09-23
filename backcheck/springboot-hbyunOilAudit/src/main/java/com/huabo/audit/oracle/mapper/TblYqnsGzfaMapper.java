package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsGzfa;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_GZFA(工作方案表)】的数据库操作Mapper
 * @Entity TblYqnsGzfa
 */
public interface TblYqnsGzfaMapper extends BaseMapper<TblYqnsGzfa> {
    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsGzfaMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsGzfa> selectListByPageInfo(PageInfo<TblYqnsGzfa> pageInfo, TblYqnsGzfa vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_GZFA_ATT " +
            " WHERE gzfaid = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_GZFA_ATT WHERE gzfaid = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_GZFA_ATT(gzfaid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}




