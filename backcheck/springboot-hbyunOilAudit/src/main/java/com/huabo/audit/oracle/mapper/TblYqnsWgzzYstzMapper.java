package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsWgzzYstz;
import com.huabo.audit.util.PageInfo;

/**
 * 移送台账Mapper
 */
public interface TblYqnsWgzzYstzMapper extends tk.mybatis.mapper.common.Mapper<TblYqnsWgzzYstz> {

    @SelectProvider(method="getByYstzList",type=TblYqnsWgzzYstzMapperSqlConfig.class)
    @Results({
            @Result(column="ID",property="id"),
    })
    List<TblYqnsWgzzYstz> getByYstzList(PageInfo<TblYqnsWgzzYstz> pageInfo, TblYqnsWgzzYstz tblYqnsWgzzYstz);
    
    @SelectProvider(method="getByYstzCount",type=TblYqnsWgzzYstzMapperSqlConfig.class)
    Integer getByYstzCount(PageInfo<TblYqnsWgzzYstz> pageInfo, TblYqnsWgzzYstz tblYqnsWgzzYstz);


    @Select("select TNA.*,ORG.ORGNAME imporgname,IMP.PROJECT_NAME projectname from TBL_YQNS_WGZZ_YSTZ TNA "
    		+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.IMPORGID   "
            + "LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN IMP ON IMP.ID = TNA.PROJECTID   "
    		+ "where TNA.ID = #{id} ")
    @Results({
            @Result(column="ID",property="id"),
    })
    TblYqnsWgzzYstz ystzDetail(@Param("id") BigDecimal id);

    @Delete("delete from TBL_YQNS_WGZZ_YSTZ where ID = #{id}")
    Integer ystzDelete(@Param("id") BigDecimal id);

    @Insert("insert into TBL_YQNS_WGZZ_YSTZ_ATT (ID,ATTID) values(#{id},#{attid})")
    Integer insertSHBGAttInfoForPlan(@Param("id")BigDecimal id, @Param("attid") String attid);

    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER,ISPYTHONFLAG FROM TBL_ATTACHMENT WHERE ATTID = #{attId} ")
    @Results({
            @Result(column="ATTID",property="attid"),
            @Result(column="ATTNAME",property="attname"),
            @Result(column="ATTPATH",property="attpath"),
            @Result(column="ATTSIZE",property="attsize"),
            @Result(column="MEMO",property="memo"),
            @Result(column="UPLOADTIME",property="uploadtime"),
            @Result(column="UPLOADER",property="uploader"),
            @Result(column="ISPYTHONFLAG",property="ispythonflag"),
    })
    TblAttachment selectEntityById(@Param("attId") String attId) throws Exception;

    @Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
    void deleteEntity(@Param("attid") BigDecimal attid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_WGZZ_YSTZ_ATT WHERE ATTID=#{attid}")
    void deleteSHBGFileInfoByAttId(BigDecimal attid);

    @Delete("delete from TBL_YQNS_WGZZ_YSTZ_ATT where ID =#{id} ")
    void deletefile(@Param("id") BigDecimal id);

    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_WGZZ_YSTZ_ATT WHERE ID = #{id})")
    List<TblAttachment> findAttachmentListByWgbg(BigDecimal id);

}
