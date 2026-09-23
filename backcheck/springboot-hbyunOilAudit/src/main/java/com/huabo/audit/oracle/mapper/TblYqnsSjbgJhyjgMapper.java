package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjbgJhyjg;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TTBL_YQNS_SJBG_JHYJG(交换意见稿表)】的数据库操作Mapper
 * @createDate 2023-09-27 16:46:40
 * @Entity TblYqnsSjbgJhyjg
 */
public interface TblYqnsSjbgJhyjgMapper extends BaseMapper<TblYqnsSjbgJhyjg> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSjbgJhyjgMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSjbgJhyjg> pageInfo, TblYqnsSjbgJhyjg vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSjbgJhyjgMapperSqlConfig.class)
    @Results({
            @Result(column = "jhyjgid", property = "jhyjgid"),
            @Result(column = "title", property = "title"),
            @Result(column = "cjr", property = "cjr"),
            @Result(column = "cjsj", property = "cjsj"),
            @Result(column = "gxr", property = "gxr"),
    })
    List<TblYqnsSjbgJhyjg> selectListByPageInfo(PageInfo<TblYqnsSjbgJhyjg> pageInfo, TblYqnsSjbgJhyjg vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJBG_JHYJGATTACH " +
            " WHERE JHYJGID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJBG_JHYJGATTACH WHERE JHYJGID = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_SJBG_JHYJGATTACH(jhyjgid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}




