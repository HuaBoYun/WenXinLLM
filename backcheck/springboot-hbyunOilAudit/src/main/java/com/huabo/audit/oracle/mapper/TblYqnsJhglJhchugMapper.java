package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcg;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhchug;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JHCHUG(计划管理计划初稿)】的数据库操作Mapper
 * @Entity TblYqnsJhglJhchug
 */
public interface TblYqnsJhglJhchugMapper extends BaseMapper<TblYqnsJhglJhchug> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsJhglJhchugMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsJhglJhchug> pageInfo, TblYqnsJhglJhchug vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsJhglJhchugMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsJhglJhchug> selectListByPageInfo(PageInfo<TblYqnsJhglJhchug> pageInfo, TblYqnsJhglJhchug vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_JHGL_JHCHUG_ATT " +
            " WHERE jhchugid = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_JHGL_JHCHUG_ATT WHERE jhchugid = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_JHGL_JHCHUG_ATT(jhchugid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);

}




