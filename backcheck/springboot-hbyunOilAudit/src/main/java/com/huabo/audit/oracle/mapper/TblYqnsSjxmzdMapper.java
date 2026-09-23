package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjxmzd;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_SJXMZD(审计项目制度表)】的数据库操作Mapper
 * @Entity TblYqnsSjxmzd
 */
public interface TblYqnsSjxmzdMapper extends BaseMapper<TblYqnsSjxmzd> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSjxmzdMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSjxmzd> pageInfo, TblYqnsSjxmzd vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSjxmzdMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsSjxmzd> selectListByPageInfo(PageInfo<TblYqnsSjxmzd> pageInfo, TblYqnsSjxmzd vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJXMZD_ATT " +
            " WHERE sjxmzdid = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJXMZD_ATT WHERE sjxmzdid = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_SJXMZD_ATT(sjxmzdid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}




