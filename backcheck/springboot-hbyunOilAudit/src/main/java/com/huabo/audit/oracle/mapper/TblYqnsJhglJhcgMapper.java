package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcg;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JHCG(计划管理计划草稿)】的数据库操作Mapper
 * @Entity TblYqnsJhglJhcg
 */
public interface TblYqnsJhglJhcgMapper extends BaseMapper<TblYqnsJhglJhcg> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsJhglJhcgMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsJhglJhcg> pageInfo, TblYqnsJhglJhcg vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsJhglJhcgMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsJhglJhcg> selectListByPageInfo(PageInfo<TblYqnsJhglJhcg> pageInfo, TblYqnsJhglJhcg vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_JHGL_JHCG_ATT " +
            " WHERE jhcgid = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_JHGL_JHCG_ATT WHERE jhcgid = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_JHGL_JHCG_ATT(jhcgid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);

}




