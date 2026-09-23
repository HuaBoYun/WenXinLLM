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
import com.huabo.audit.oracle.entity.TblYqnsWgzzWtdz;
import com.huabo.audit.util.PageInfo;

/**
 * 问题定责Mapper
 */
public interface TblYqnsWgzzWtdzMapper extends tk.mybatis.mapper.common.Mapper<TblYqnsWgzzWtdz> {

    @SelectProvider(method="getByWtdzList",type=TblYqnsWgzzWtdzMapperSqlConfig.class)
    @Results({
            @Result(column="ID",property="id"),
    })
    List<TblYqnsWgzzWtdz> getByWtdzList(PageInfo<TblYqnsWgzzWtdz> pageInfo, TblYqnsWgzzWtdz tblYqnsWgzzWtdz);
    
    @SelectProvider(method="getByWtdzCount",type=TblYqnsWgzzWtdzMapperSqlConfig.class)
    Integer getByWtdzCount(PageInfo<TblYqnsWgzzWtdz> pageInfo, TblYqnsWgzzWtdz tblYqnsWgzzWtdz);


    @Select("select TNA.*,ORG.ORGNAME editorgname,wthc.hcname,wthc.hcnumber,STAFF.REALNAME editstaffname from TBL_YQNS_WGZZ_WTDZ TNA "
    		+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.EDITORGID "
    		+ "LEFT JOIN TBL_YQNS_WGZZ_WTHC wthc ON wthc.ID = TNA.HCID "
    		+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.CREATOR "
    		+ "where TNA.ID = #{id} ")
    @Results({
            @Result(column="ID",property="id"),
    })
    TblYqnsWgzzWtdz wtdzDetail(@Param("id") BigDecimal id);

    @Delete("delete from TBL_YQNS_WGZZ_WTDZ where ID = #{id}")
    Integer wtdzDelete(@Param("id") BigDecimal id);

    @Insert("insert into TBL_YQNS_WGZZ_WTDZ_ATT (ID,ATTID) values(#{id},#{attid})")
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

    @Delete("DELETE FROM TBL_YQNS_WGZZ_WTDZ_ATT WHERE ATTID=#{attid}")
    void deleteSHBGFileInfoByAttId(BigDecimal attid);

    @Delete("delete from TBL_YQNS_WGZZ_WTDZ_ATT where ID =#{id} ")
    void deletefile(@Param("id") BigDecimal id);

    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_WGZZ_WTDZ_ATT WHERE ID = #{id})")
    List<TblAttachment> findAttachmentListByWgbg(BigDecimal id);
    
    
    @Select("select count(*) from TBL_YQNS_WGZZ_WTDZ_ISSUE where ID=#{id} and staffid=#{staffid}")
    Integer checkIssue(Integer id,Integer staffid);
    
    @Insert("INSERT INTO TBL_YQNS_WGZZ_WTDZ_ISSUE(STAFFID,ID) VALUES(#{staffid},#{id})")
    public void saveIssue(String staffid,String id);

}
