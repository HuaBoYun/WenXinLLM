package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjzgSjzgtj;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_SJZGTJ(审计整改统计表)】的数据库操作Mapper
 * @createDate 2023-09-27 16:46:40
 * @Entity TblYqnsSjzgSjzgtj
 */
public interface TblYqnsSjzgSjzgtjMapper extends BaseMapper<TblYqnsSjzgSjzgtj> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSjzgSjzgtjMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSjzgSjzgtj> pageInfo, TblYqnsSjzgSjzgtj vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSjzgSjzgtjMapperSqlConfig.class)
    @Results({
            @Result(column = "sjzgtjid", property = "sjzgtjid"),
            @Result(column = "zdgzzds", property = "zdgzzds"),
            @Result(column = "xggzzds", property = "xggzzds"),
            @Result(column = "ysclsx", property = "ysclsx"),
            @Result(column = "sjsfjgysbg", property = "sjsfjgysbg"),
            @Result(column = "ysclje", property = "ysclje"),
            @Result(column = "sjsfjgysbgr", property = "sjsfjgysbgr"),
            @Result(column = "ysclr", property = "ysclr"),
            @Result(column = "yscllsqk", property = "yscllsqk"),
            @Result(column = "qzsjzwcf", property = "qzsjzwcf"),
            @Result(column = "qzsjnbjlcf", property = "qzsjnbjlcf"),
            @Result(column = "qtjjcf", property = "qtjjcf"),
            @Result(column = "cjr", property = "cjr"),
            @Result(column = "cjsj", property = "cjsj"),
    })
    List<TblYqnsSjzgSjzgtj> selectListByPageInfo(PageInfo<TblYqnsSjzgSjzgtj> pageInfo, TblYqnsSjzgSjzgtj vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJZG_SJZGTJ_ATTACH " +
            " WHERE SJZGTJID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJZG_SJZGTJ_ATTACH WHERE SJZGTJID = #{id}")
    void deleteAttByPk(BigDecimal id);

    @Delete("DELETE FROM TBL_YQNS_SJZG_SJZGTJ_ATTACH WHERE ATTID = #{attid}")
    void deleteAttById(String attid);

    @Insert("INSERT INTO TBL_YQNS_SJZG_SJZGTJ_ATTACH(SJZGTJID,ATTID) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);

    @Select("SELECT * FROM TBL_YQNS_SJZG_SJZGTJ WHERE WTZGID = #{wtzgid} ")
	List<TblYqnsSjzgSjzgtj> selectListByZg(@Param("wtzgid") BigDecimal wtzgid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SJZG_SJZGTJ WHERE WTZGID = #{wtzgid} ")
	void deleteByWtzgid(BigDecimal wtzgid) throws Exception;

    @Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_SJZG_SJZGTJ_ATTACH WHERE SJZGTJID IN (SELECT SJZGTJID FROM TBL_YQNS_SJZG_SJZGTJ WHERE WTZGID = #{wtzgid} ))")
	void deleteAttachmentByWtzzgId(BigDecimal wtzgid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SJZG_SJZGTJ_ATTACH WHERE SJZGTJID IN ( SELECT SJZGTJID FROM TBL_YQNS_SJZG_SJZGTJ WHERE WTZGID = #{wtzgid} )")
	void deleteAttachmentRelaByWtzzgId(BigDecimal wtzgid) throws Exception;

    @Select("INSERT INTO TBL_YQNS_SJZG_SJZGTJ_ATTACH(SJZGTJID,ATTID) SELECT #{sjzgtjid},ATTID FROM TBL_YQNS_SJZG_SJZGTJ_ATTACH WHERE SJZGTJID = #{oldId}")
	void copyFileRelation(@Param("sjzgtjid") BigDecimal sjzgtjid,@Param("oldId") BigDecimal oldId) throws Exception;

    @Select("SELECT COUNT(0) FROM TBL_YQNS_SJZG_SJZGTJ_ATTACH WHERE ATTID = #{attid}")
	Integer selectFilRelationCount(@Param("attid") String attid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SJZG_SJZGTJ_ATTACH WHERE SJZGTJID = #{sjzgtjid} AND ATTID = #{attid}")
	void deleteFileRelation(@Param("attid")String attid,@Param("sjzgtjid") BigDecimal sjzgtjid) throws Exception;
}




