package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjbgJjzrSjjgbg;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJBG_JJZRSJJGBG(经济责任审计结果报告定稿表)】的数据库操作Mapper
 * @createDate 2023-09-27 16:46:40
 * @Entity TblYqnsSjbgJjzrSjjgbg
 */
public interface TblYqnsSjbgJjzrSjjgbgMapper extends BaseMapper<TblYqnsSjbgJjzrSjjgbg> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSjbgJjzrSjjgbgMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSjbgJjzrSjjgbg> pageInfo, TblYqnsSjbgJjzrSjjgbg vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSjbgJjzrSjjgbgMapperSqlConfig.class)
    @Results({

            @Result(column = "jjzrsjjgbgid", property = "jjzrsjjgbgid"),
            @Result(column = "document", property = "document"),
            @Result(column = "title", property = "title"),
            @Result(column = "cjr", property = "cjr"),
            @Result(column = "cjsj", property = "cjsj"),
            @Result(column = "gxr", property = "gxr"),
    })
    List<TblYqnsSjbgJjzrSjjgbg> selectListByPageInfo(PageInfo<TblYqnsSjbgJjzrSjjgbg> pageInfo, TblYqnsSjbgJjzrSjjgbg vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJBG_JJZRSJJGBGATTACH " +
            " WHERE JJZRSJJGBGID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJBG_JJZRSJJGBGATTACH WHERE JJZRSJJGBGID = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_SJBG_JJZRSJJGBGATTACH(jjzrsjjgbgid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}




