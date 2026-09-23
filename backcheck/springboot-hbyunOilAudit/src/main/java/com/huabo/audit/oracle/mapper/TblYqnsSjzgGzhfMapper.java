package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjzgGzhf;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_GZHF(跟踪回访表)】的数据库操作Mapper
 * @createDate 2023-09-27 16:46:40
 * @Entity TblYqnsSjzgGzhf
 */
public interface TblYqnsSjzgGzhfMapper extends BaseMapper<TblYqnsSjzgGzhf> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSjzgGzhfMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSjzgGzhf> pageInfo, TblYqnsSjzgGzhf vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSjzgGzhfMapperSqlConfig.class)
    @Results({
            @Result(column = "gzhfid", property = "gzhfid"),
            @Result(column = "ysnrid", property = "ysnrid"),
            @Result(column = "xmbzhbm", property = "xmbzhbm"),
            @Result(column = "xmid", property = "xmid"),
            @Result(column = "lxdwzzjgbm", property = "lxdwzzjgbm"),
            @Result(column = "lxdw", property = "lxdw"),
            @Result(column = "xmmc", property = "xmmc"),
            @Result(column = "wtje", property = "wtje"),
            @Result(column = "sjje", property = "sjje"),
            @Result(column = "clyj", property = "clyj"),
            @Result(column = "zgdcqtbm", property = "zgdcqtbm"),
            @Result(column = "zgzt", property = "zgzt"),
            @Result(column = "zgje", property = "zgje"),
            @Result(column = "wtdxbh", property = "wtdxbh"),
            @Result(column = "zgms", property = "zgms"),
            @Result(column = "bbnd", property = "bbnd"),
            @Result(column = "ejdw", property = "ejdw"),
            @Result(column = "sftjje", property = "sftjje"),
            @Result(column = "xmxz", property = "xmxz"),
            @Result(column = "sfjw", property = "sfjw"),
            @Result(column = "zjjjcg", property = "zjjjcg"),
            @Result(column = "dxfgyj", property = "dxfgyj"),
            @Result(column = "sfys", property = "sfys"),
            @Result(column = "cjr", property = "cjr"),
            @Result(column = "cjsj", property = "cjsj"),
    })
    List<TblYqnsSjzgGzhf> selectListByPageInfo(PageInfo<TblYqnsSjzgGzhf> pageInfo, TblYqnsSjzgGzhf vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJZG_GZHF_ATTACH " +
            " WHERE GZHFID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJZG_GZHF_ATTACH WHERE GZHFID = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_SJZG_GZHF_ATTACH(gzhfid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}




