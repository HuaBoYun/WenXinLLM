package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhMx;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JH_MX(计划管理计划明细)】的数据库操作Mapper
 * @Entity TblYqnsJhglJhMx
 */
public interface TblYqnsJhglJhMxMapper extends BaseMapper<TblYqnsJhglJhMx> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsJhglJhMxMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsJhglJhMx> pageInfo, TblYqnsJhglJhMx vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsJhglJhMxMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsJhglJhMx> selectListByPageInfo(PageInfo<TblYqnsJhglJhMx> pageInfo, TblYqnsJhglJhMx vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_JHGL_JH_MX_ATT " +
            " WHERE jhmxid = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_JHGL_JH_MX_ATT WHERE jhmxid = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_JHGL_JH_MX_ATT(jhmxid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);

}




