package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhchugMx;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JHCHUG_MX(计划管理计划初稿明细)】的数据库操作Mapper
 * @Entity TblYqnsJhglJhchugMx
 */
public interface TblYqnsJhglJhchugMxMapper extends BaseMapper<TblYqnsJhglJhchugMx> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsJhglJhchugMxMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsJhglJhchugMx> pageInfo, TblYqnsJhglJhchugMx vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsJhglJhchugMxMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsJhglJhchugMx> selectListByPageInfo(PageInfo<TblYqnsJhglJhchugMx> pageInfo, TblYqnsJhglJhchugMx vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_JHGL_JHCHUG_MX_ATT " +
            " WHERE jhchugmxid = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_JHGL_JHCHUG_MX_ATT WHERE jhchugmxid = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_JHGL_JHCHUG_MX_ATT(jhchugmxid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}




