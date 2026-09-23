package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcgMx;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JHCG_MX(计划管理计划草稿明细)】的数据库操作Mapper
 * @Entity TblYqnsJhglJhcgMx
 */
public interface TblYqnsJhglJhcgMxMapper extends BaseMapper<TblYqnsJhglJhcgMx> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsJhglJhcgMxMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsJhglJhcgMx> pageInfo, TblYqnsJhglJhcgMx vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsJhglJhcgMxMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsJhglJhcgMx> selectListByPageInfo(PageInfo<TblYqnsJhglJhcgMx> pageInfo, TblYqnsJhglJhcgMx vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_JHGL_JHCG_MX_ATT " +
            " WHERE jhcgmxid = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_JHGL_JHCG_MX_ATT WHERE jhcgmxid = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_JHGL_JHCG_MX_ATT(jhcgmxid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}




