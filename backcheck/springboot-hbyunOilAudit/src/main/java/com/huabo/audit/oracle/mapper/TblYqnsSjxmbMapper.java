package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjxmb;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_SJXMB(审计项目表)】的数据库操作Mapper
 * @Entity TblYqnsSjxmb
 */
public interface TblYqnsSjxmbMapper extends BaseMapper<TblYqnsSjxmb> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSjxmbMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSjxmb> pageInfo, TblYqnsSjxmb vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSjxmbMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsSjxmb> selectListByPageInfo(PageInfo<TblYqnsSjxmb> pageInfo, TblYqnsSjxmb vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJXMB_ATT " +
            " WHERE SJXMBID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJXMB_ATT WHERE SJXMBID = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_SJXMB_ATT(SJXMBID,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);

}




