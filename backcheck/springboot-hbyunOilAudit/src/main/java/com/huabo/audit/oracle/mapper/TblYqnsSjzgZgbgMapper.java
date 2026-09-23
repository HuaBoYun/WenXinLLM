package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjzgZgbg;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_ZGBGINFO(审计整改报告表)】的数据库操作Mapper
 * @createDate 2023-10-8 16:46:40
 * @Entity TblYqnsSjzgZgbg
 */
public interface TblYqnsSjzgZgbgMapper extends BaseMapper<TblYqnsSjzgZgbg> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSjzgZgbgMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSjzgZgbg> pageInfo, TblYqnsSjzgZgbg vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSjzgZgbgMapperSqlConfig.class)
    @Results({
            @Result(column = "document", property = "document"),
            @Result(column = "title", property = "title"),
            @Result(column = "cjr", property = "cjr"),
            @Result(column = "cjsj", property = "cjsj"),
            @Result(column = "gxr", property = "gxr"),
    })
    List<TblYqnsSjzgZgbg> selectListByPageInfo(PageInfo<TblYqnsSjzgZgbg> pageInfo, TblYqnsSjzgZgbg vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJZG_ZGBG_ATTACH " +
            " WHERE ZGBGID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJZG_ZGBG_ATTACH WHERE ZGBGID = #{id}")
    void deleteAttByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJZG_ZGBG_ATTACH WHERE ATTID = #{attid}")
    void deleteAttById(String attid);

    @Insert("INSERT INTO TBL_YQNS_SJZG_ZGBG_ATTACH(zgbgid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);

    @Select("SELECT * FROM TBL_YQNS_SJZG_ZGBGINFO WHERE WTZGID = #{wtzgid} ORDER BY CJSJ ASC ")
	List<TblYqnsSjzgZgbg> selectListByZg(@Param("wtzgid")BigDecimal wtzgid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SJZG_ZGBGINFO WHERE WTZGID = #{wtzgid} ")
	void deleteByWtzgid(@Param("wtzgid")BigDecimal wtzgid);

    @Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_SJZG_ZGBG_ATTACH WHERE ZGBGID IN (SELECT ZGBGID FROM TBL_YQNS_SJZG_ZGBGINFO WHERE WTZGID = #{wtzgid}))")
	void deleteAttachmentByWtzzgId(@Param("wtzgid")BigDecimal wtzgid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SJZG_ZGBG_ATTACH WHERE ZGBGID IN (SELECT ZGBGID FROM TBL_YQNS_SJZG_ZGBGINFO WHERE WTZGID = #{wtzgid})")
	void deleteAttachmentRelaByWtzzgId(BigDecimal wtzgid) throws Exception;

    @Select("INSERT INTO TBL_YQNS_SJZG_ZGBG_ATTACH(ZGBGID,ATTID) SELECT #{zgbgid},ATTID FROM TBL_YQNS_SJZG_ZGBG_ATTACH WHERE ZGBGID = #{oldId}")
	void copyFileRealtion(@Param("zgbgid")BigDecimal zgbgid,@Param("oldId") BigDecimal oldId);

    @Select("SELECT COUNT(0) FROM TBL_YQNS_SJZG_ZGBG_ATTACH WHERE ATTID = #{attid}")
	Integer selectFileRelationCount(@Param("attid")String attid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SJZG_ZGBG_ATTACH WHERE ATTID = #{attid} AND ZGBGID = #{zgbgid}")
	void deleteFileRelation(@Param("attid") String attid,@Param("zgbgid") BigDecimal zgbgid) throws Exception;
}




