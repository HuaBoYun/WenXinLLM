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
import com.huabo.audit.oracle.entity.TblWgzzWghs;
import com.huabo.audit.util.PageInfo;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.mapper
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:10:39
 */
public interface TblWghsMapper extends tk.mybatis.mapper.common.Mapper<TblWgzzWghs> {

    @SelectProvider(method="getByWghsList",type=TblWghsMapperConfig.class)
    @Results({
            @Result(column="ID",property="id"),
            @Result(column="CLUENABER",property="cluenaber"),
            @Result(column="VERIFYCONTENT",property="verifycontent"),
            @Result(column="CREATOR",property="creator"),
            @Result(column="IMPCREATEUSERNAME",property="impcreateusername"),
    })
    List<TblWgzzWghs> getByWghsList(PageInfo<TblWgzzWghs> pageInfo, String clueNaber,String verifycontent);

    @Select("select * from TBL_WGZZ_WGHS where ID = #{id}")
    @Results({
            @Result(column="ID",property="id"),
            @Result(column="CLUENABER",property="cluenaber"),
            @Result(column="VERIFYCONTENT",property="verifycontent"),
            @Result(column="CREATOR",property="creator"),
            @Result(column="IMPCREATEUSERNAME",property="impcreateusername"),
    })
    TblWgzzWghs getByWghsXQList(@Param("id") BigDecimal id);

    @SelectProvider(method="getByWghsBHList",type=TblWghsMapperConfig.class)
    @Results({
            @Result(column="ID",property="id"),
            @Result(column="CLUENABER",property="cluenaber"),
            @Result(column="VERIFYCONTENT",property="verifycontent"),
            @Result(column="CREATOR",property="creator"),
            @Result(column="IMPCREATEUSERNAME",property="impcreateusername"),
    })
    List<TblWgzzWghs> getByWghsBHList(PageInfo<TblWgzzWghs> pageInfo, String clueNaber);

    @SelectProvider(method="getByContWghsBHList",type=TblWghsMapperConfig.class)
    Integer getByContWghsBHList(PageInfo<TblWgzzWghs> pageInfo, String clueNaber);

    @SelectProvider(method="getByContWghsList",type=TblWghsMapperConfig.class)
    Integer getByContWghsList(PageInfo<TblWgzzWghs> pageInfo, String clueNaber,String verifycontent);

    @Select("select CLUENABER,VERIFYCONTENT from TBL_WGZZ_WGHS")
    List<TblWgzzWghs> selectByBhandNR(TblWgzzWghs tblWgzzWghs);

    @Delete("delete from TBL_WGZZ_WGHS where ID = #{id}")
    Integer getByWghsRemove(@Param("id") BigDecimal id);



    @Insert("insert into TBL_WGZZ_ATT (ID,ATTID) values(#{id},#{attid})")
    Integer insertAttInfoForPlan(@Param("id")BigDecimal id, @Param("attid") String attid);


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

  /*  //违规核实
    @UpdateProvider(method="updateEntity",type=TblWghsMapperConfig.class)
    void updateEntity(TblWgzzWghs plan) throws Exception;

    @InsertProvider(method="insertEntity",type=TblWghsMapperConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
    void insertEntity(TblWgzzWghs plan) throws Exception;*/


    @Delete("DELETE FROM TBL_WGZZ_ATT WHERE ATTID=#{attid}")
    void deleteFileInfoByAttId(BigDecimal attid);

    
    @Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
    void deleteEntity(@Param("attid")BigDecimal attid) throws Exception;



    //附件删除
    @Delete("delete from TBL_WGZZ_ATT where ID =#{id} ")
    void deletefile(@Param("id") BigDecimal id);
    
    
    //==违规报告-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_WGZZ_ATT WHERE ID = #{id})")
    List<TblAttachment> findAttachmentListByWghs(BigDecimal id);
}
