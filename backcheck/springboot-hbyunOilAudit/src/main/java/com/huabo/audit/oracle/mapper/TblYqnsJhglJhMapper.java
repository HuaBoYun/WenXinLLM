package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsJhglJh;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JH(计划管理计划)】的数据库操作Mapper
 * @Entity TblYqnsJhglJh
 */
public interface TblYqnsJhglJhMapper extends BaseMapper<TblYqnsJhglJh> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsJhglJhMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsJhglJh> pageInfo, TblYqnsJhglJh vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsJhglJhMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsJhglJh> selectListByPageInfo(PageInfo<TblYqnsJhglJh> pageInfo, TblYqnsJhglJh vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_JHGL_JH_ATT " +
            " WHERE jhid = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_JHGL_JH_ATT WHERE jhid = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_JHGL_JH_ATT(jhid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);

}




