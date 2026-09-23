package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjzgWtzg;
import com.huabo.audit.oracle.entity.TblYqnsSjzgYsnr;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_YSNR(移送内容表)】的数据库操作Mapper
 * @createDate 2023-09-27 16:46:40
 * @Entity TblYqnsSjzgYsnr
 */
public interface TblYqnsSjzgYsnrMapper extends BaseMapper<TblYqnsSjzgYsnr> {

    @Insert("INSERT INTO TBL_YQNS_YSNRATT(YSID,ATTID) VALUES (#{ysnrid},#{attId})")
	void saveAttId(@Param("ysnrid") BigDecimal ysnrid,@Param("attId") String attId);

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_YSNRATT " +
            " WHERE YSID = #{ysnrid})")
	List<TblAttachment> selectAttachmentListByPk(@Param("ysnrid")BigDecimal ysnrid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_YSNRATT WHERE ATTID = #{attid}")
	void deleteAttById(@Param("attid")String attid);

    @Select("INSERT INTO TBL_YQNS_YSNRATT(YSID,ATTID)  SELECT #{ysnrid},ATTID FROM TBL_YQNS_YSNRATT WHERE YSID = #{oldSjysId}")
	void copyFileRelation(@Param("ysnrid")BigDecimal ysnrid,@Param("oldSjysId") BigDecimal oldSjysId) throws Exception;

    @Select("SELECT COUNT(0) FROM TBL_YQNS_YSNRATT WHERE ATTID = #{attid}")
	Integer selectFileRelationCount(@Param("attid")String attid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_YSNRATT WHERE ATTID = #{attid} AND YSID = #{ysnrid}")
	void deleteFileRelation(@Param("attid")String attid,@Param("ysnrid") BigDecimal ysnrid) throws Exception;

    @Select("SELECT TYSY.*,TYSW.DQZJJJCGTYPE,TYSW.DQZJJJCG,TYSW.DQQTJJCGTYPE,TYSW.DQQTJJCG FROM TBL_YQNS_SJZG_WTZG TYSW " + 
    		"LEFT JOIN TBL_YQNS_SJZG_YSNR TYSY ON TYSW.YSNRID = TYSY.YSNRID WHERE TYSW.PROJECTID = #{projectId} AND TYSW.YSNRID IS NOT NULL AND TYSW.SFYS = '1'")
	List<TblYqnsSjzgYsnr> selectListByProjectId(@Param("projectId") BigDecimal projectId) throws Exception;

}




