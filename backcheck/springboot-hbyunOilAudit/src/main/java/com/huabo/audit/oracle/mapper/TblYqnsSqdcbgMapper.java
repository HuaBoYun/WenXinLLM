package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSqdcbg;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_SQDCBG(审前调查报告表)】的数据库操作Mapper
 * @Entity TblYqnsSqdcbg
 */
public interface TblYqnsSqdcbgMapper extends BaseMapper<TblYqnsSqdcbg> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSqdcbgMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSqdcbg> pageInfo, TblYqnsSqdcbg vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSqdcbgMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsSqdcbg> selectListByPageInfo(PageInfo<TblYqnsSqdcbg> pageInfo, TblYqnsSqdcbg vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SQDCBG_ATT " +
            " WHERE SQDCBGID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SQDCBG_ATT WHERE SQDCBGID = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_SQDCBG_ATT(SQDCBGID,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}




