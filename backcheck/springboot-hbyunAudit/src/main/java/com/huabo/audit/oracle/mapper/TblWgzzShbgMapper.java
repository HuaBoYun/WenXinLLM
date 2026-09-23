package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblWgzzEntity;
import com.huabo.audit.oracle.entity.TblWgzzShbg;
import com.huabo.audit.oracle.entity.TblWgzzWghs;
import com.huabo.audit.util.PageInfo;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.mapper
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:11:42
 */
public interface TblWgzzShbgMapper extends tk.mybatis.mapper.common.Mapper<TblWgzzShbg> {

    @SelectProvider(method="getByWghsSHBGList",type=TblWgzzShbgMapperSqlConfig.class)
    @Results({
            @Result(column="ID",property="id"),
            @Result(column="CLUENABER",property="cluenaber"),
            @Result(column="VERIFYCONTENT",property="verifycontent"),
            @Result(column="CREATOR",property="creator"),
            @Result(column="IMPCREATEUSERNAME",property="impcreateusername"),
    })
    List<TblWgzzShbg> getByWghsSHBGList(PageInfo<TblWgzzShbg> pageInfo, String clueNaber,String verifycontent);

    @Select("select * from TBL_WGZZ_SHBG where ID =#{id} ")
    @Results({
            @Result(column="ID",property="id"),
            @Result(column="CLUENABER",property="cluenaber"),
            @Result(column="VERIFYCONTENT",property="verifycontent"),
            @Result(column="CREATOR",property="creator"),
            @Result(column="IMPCREATEUSERNAME",property="impcreateusername"),
    })
    TblWgzzShbg getBySHBGBHList(@Param("id") BigDecimal id);


    @SelectProvider(method="getByContSHBGList",type=TblWgzzShbgMapperSqlConfig.class)
    Integer getByContSHBGList(PageInfo<TblWgzzShbg> pageInfo, String clueNaber,String verifycontent);

    @Delete("delete from TBL_WGZZ_SHBG where ID = #{id}")
    Integer getBySHBGRemove(@Param("id") BigDecimal id);

    @Insert("insert into TBL_WGZZ_SHBG_ATT (ID,ATTID) values(#{id},#{attid})")
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

/*    @UpdateProvider(method="updateSelective",type=TblWgzzShbgMapperSqlConfig.class)
    void updateSelective(TblWgzzShbg plan) throws Exception;*/


    @Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
    void deleteEntity(@Param("attid") BigDecimal attid) throws Exception;

    @Delete("DELETE FROM TBL_WGZZ_SHBG_ATT WHERE ATTID=#{attid}")
    void deleteSHBGFileInfoByAttId(BigDecimal attid);

    //附件删除
    @Delete("delete from TBL_WGZZ_SHBG_ATT where ID =#{id} ")
    void deletefile(@Param("id") BigDecimal id);

    //==违规报告-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_WGZZ_SHBG_ATT WHERE ID = #{id})")
    List<TblAttachment> findAttachmentListByWgbg(BigDecimal id);
    
    
    
    @Insert("insert into TBL_WGZZ_SHBG_HYATT (ID,ATTID) values(#{id},#{attid})")
    Integer insertSHBGAttInfoHy(BigDecimal id, String attid);
    
    
    @Delete("DELETE FROM TBL_WGZZ_SHBG_HYATT WHERE ATTID=#{attid}")
    void deleteSHBGFileInfoByAttIdhy(BigDecimal attid);

    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_WGZZ_SHBG_HYATT WHERE ID = #{id})")
    List<TblAttachment> selectbyHy(BigDecimal id) throws Exception;
}
