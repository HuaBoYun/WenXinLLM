package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjbgSjgzjl;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @description 审计工作记录Mapper
 */
public interface TblYqnsSjbgSjgzjlMapper extends BaseMapper<TblYqnsSjbgSjgzjl> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSjbgSjgzjlMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSjbgSjgzjl> pageInfo, TblYqnsSjbgSjgzjl vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSjbgSjgzjlMapperSqlConfig.class)
    @Results({

            @Result(column = "id", property = "id"),
            @Result(column = "cjr", property = "cjr"),
            @Result(column = "cjsj", property = "cjsj"),
    })
    List<TblYqnsSjbgSjgzjl> selectListByPageInfo(PageInfo<TblYqnsSjbgSjgzjl> pageInfo, TblYqnsSjbgSjgzjl vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJBG_SJGZJLATTACH " +
            " WHERE ID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJBG_SJGZJLATTACH WHERE ID = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_SJBG_SJGZJLATTACH(ID,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}




