package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjzgGzzdqksm;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.xa.XAException;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_GZZDQKSM(规章制度情况说明表)】的数据库操作Mapper
 * @createDate 2023-09-27 16:46:40
 * @Entity TblYqnsSjzgGzzdqksm
 */
public interface TblYqnsSjzgGzzdqksmMapper extends BaseMapper<TblYqnsSjzgGzzdqksm> {

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJZG_GZZDQKSM_ATTACH " +
            " WHERE GZZDQKID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJZG_GZZDQKSM_ATTACH WHERE GZZDQKID = #{id}")
    void deleteAttByPk(BigDecimal id);

    @Delete("DELETE FROM TBL_YQNS_SJZG_GZZDQKSM_ATTACH WHERE ATTID = #{attid}")
    void deleteAttById(String attid);

    @Insert("INSERT INTO TBL_YQNS_SJZG_GZZDQKSM_ATTACH(GZZDQKID,ATTID) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);

    @Select("SELECT * FROM TBL_YQNS_SJZG_GZZDQKSM WHERE WTZGID = #{wtzgid} ")
	TblYqnsSjzgGzzdqksm selectUniqueByZg(@Param("wtzgid")BigDecimal wtzgid) throws Exception;

    @Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_SJZG_GZZDQKSM_ATTACH WHERE GZZDQKID IN (SELECT GZZDQKSMID FROM TBL_YQNS_SJZG_GZZDQKSM WHERE WTZGID = #{wtzgid}) )")
	void deleteAttachmentByWtzzgId(@Param("wtzgid")BigDecimal wtzgid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SJZG_GZZDQKSM_ATTACH WHERE GZZDQKID IN (SELECT GZZDQKSMID FROM TBL_YQNS_SJZG_GZZDQKSM WHERE WTZGID = #{wtzgid}) ")
	void deleteAttachmentRelaByWtzzgId(@Param("wtzgid")BigDecimal wtzgid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SJZG_GZZDQKSM WHERE WTZGID = #{wtzgid}")
	void deleteByWtzgid(@Param("wtzgid")BigDecimal wtzgid) throws Exception;

    @Select("INSERT INTO TBL_YQNS_SJZG_GZZDQKSM_ATTACH(GZZDQKID,ATTID) SELECT #{gzzdqksmid},ATTID FROM TBL_YQNS_SJZG_GZZDQKSM_ATTACH WHERE GZZDQKID = #{oldId} ")
	void copyFileRelation(@Param("gzzdqksmid")BigDecimal gzzdqksmid, @Param("oldId")BigDecimal oldId) throws Exception;

    @Select("SELECT COUNT(0) FROM TBL_YQNS_SJZG_GZZDQKSM_ATTACH WHERE ATTID = #{attid}")
	Integer selectFileRelationCount(@Param("attid")String attid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SJZG_GZZDQKSM_ATTACH WHERE ATTID = #{attid} AND GZZDQKID = #{gzzdqksmid}")
	void deleteFileRelation(@Param("attid") String attid,@Param("gzzdqksmid") BigDecimal gzzdqksmid) throws Exception;

}




