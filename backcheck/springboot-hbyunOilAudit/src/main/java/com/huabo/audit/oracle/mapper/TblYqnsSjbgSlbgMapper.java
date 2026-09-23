package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjbgSlbg;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJBG_SLBG(审理报告表)】的数据库操作Mapper
 * @createDate 2023-09-27 16:46:40
 * @Entity TblYqnsSjbgSlbg
 */
public interface TblYqnsSjbgSlbgMapper extends BaseMapper<TblYqnsSjbgSlbg> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSjbgSlbgMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSjbgSlbg> pageInfo, TblYqnsSjbgSlbg vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSjbgSlbgMapperSqlConfig.class)
    @Results({
            @Result(column = "slbgid", property = "slbgid"),
            @Result(column = "document", property = "document"),
            @Result(column = "title", property = "title"),
            @Result(column = "cjr", property = "cjr"),
            @Result(column = "cjsj", property = "cjsj"),
            @Result(column = "gxr", property = "gxr"),
    })
    List<TblYqnsSjbgSlbg> selectListByPageInfo(PageInfo<TblYqnsSjbgSlbg> pageInfo, TblYqnsSjbgSlbg vo) throws Exception;

//    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
//            " FROM TBL_ATTACHMENT WHERE" +
//            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJBG_SLBGATTACH " +
//            " WHERE SLBGID = #{id})")
@Select(" SELECT ta.ATTID,ta.ATTNAME,ta.ATTPATH,ta.ATTSIZE,ta.MEMO,ta.UPLOADTIME,ta.UPLOADER, " +
        " tyss.TYPE AS type" +
        " FROM TBL_ATTACHMENT ta" +
        " LEFT JOIN TBL_YQNS_SJBG_SLBGATTACH tyss ON tyss.ATTID = ta.ATTID" +
        " WHERE tyss.SLBGID = #{id}")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJBG_SLBGATTACH WHERE SLBGID = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_SJBG_SLBGATTACH(slbgid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
    @Insert("INSERT INTO TBL_YQNS_SJBG_SLBGATTACH(slbgid,attid,type) VALUES(#{id},#{attid},#{type})")
    void saveAtt2(String id, String attid, String type);
}




