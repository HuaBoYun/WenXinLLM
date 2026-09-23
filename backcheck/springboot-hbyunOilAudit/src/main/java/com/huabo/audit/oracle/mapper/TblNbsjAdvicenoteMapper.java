package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteEntity;
import com.huabo.audit.oracle.entity.TblYqnsAdviceAprEntity;
import com.huabo.audit.oracle.entity.TblYqnsAdvicenoteEntity;
import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteVo;
import com.huabo.audit.util.PageInfo;

public interface TblNbsjAdvicenoteMapper extends tk.mybatis.mapper.common.Mapper<TblYqnsAdvicenoteEntity> {
    @Delete("DELETE from TBL_NBSJ_ADVICENOTE WHERE ADVICEID= #{adviceid} ")
    void deletebynoteid(String adviceid);

    @Select("SELECT * from TBL_NBSJ_ADVICENOTE WHERE PROJECTID= #{projectId} ")
    List<TblNbsjAdvicenoteEntity> findAll(String projectId);

    @Select("SELECT * from TBL_NBSJ_ADVICENOTE WHERE NOTEID = #{noteid} ")
    TblNbsjAdvicenoteEntity get(String noteid);

    @Select("SELECT * from TBL_NBSJ_ADVICENOTE WHERE PROJECTID= #{projectId} ")
    List<TblNbsjAdvicenoteEntity> isNoteCode(String code, String projectId);


    //==
    @Select("SELECT * from TBL_NBSJ_ADVICENOTE WHERE ADVICEID= #{adviceid} ")
    TblNbsjAdvicenoteEntity getById(String adviceid);

    @SelectProvider(method = "selectCountByPageInfo", type = TblNbsjAdvicenoteMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblNbsjAdvicenoteEntity> pageInfo, TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo) throws Exception;

    @Select("SELECT TNA.* FROM TBL_YQNS_ADVICENOTE TNA  WHERE TNA.ADVICEID = #{adviceid}")
    @Results({
            @Result(column = "ADVICEID", property = "adviceid"),
            @Result(column = "CREATRTIME", property = "creatrtime"),
            @Result(column = "ADVICECOED", property = "advicecoed"),
            @Result(column = "ADVICENAME", property = "advicename"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CONTENT", property = "content"),
            @Result(column = "DES", property = "des"),
            @Result(column = "SJSSTIME", property = "sjsstime"),
            @Result(column = "TEAMLEADER", property = "teamleader"),
            @Result(column = "MAINREVIEWER", property = "mainreviewer"),
            @Result(column = "HELPREVIEWER", property = "helpreviewer"),
            @Result(column = "OPERATOR", property = "operator"),
            @Result(column = "DEPARTMENT", property = "department"),
            @Result(column = "PROPOSAL", property = "proposal"),
            @Result(column = "ORGIDS", property = "orgids"),
            @Result(column = "PROJECTNAME", property = "projectname"),
            @Result(column = "TZSPID", property = "tzspid"),
            @Result(column = "FZSTAFFIDS", property = "fzstaffids"),
            @Result(column = "FZNAMES", property = "fznames"),
            @Result(column = "CREATESTAFFID", property = "createstaffid"),
            @Result(column = "ASSISTAPPROVERID", property = "assistapproverid"),
            @Result(column = "ZSSTAFFID", property = "zsstaffid"),
            @Result(column = "ZCSTAFFID", property = "zcstaffid"),

    })
    TblYqnsAdvicenoteEntity selectById(@Param("adviceid") BigDecimal adviceid) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblNbsjAdvicenoteMapperSqlConfig.class)
    @Results({
            @Result(column = "ADVICEID", property = "adviceid"),
            @Result(column = "CREATRTIME", property = "creatrtime"),
            @Result(column = "ADVICECOED", property = "advicecoed"),
            @Result(column = "ADVICENAME", property = "advicename"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CONTENT", property = "content"),
            @Result(column = "DES", property = "des"),
            @Result(column = "PROJECTNAME", property = "projectname"),
            @Result(column = "TZSPID", property = "tzspid"),
            @Result(column = "FZSTAFFIDS", property = "fzstaffids"),
            @Result(column = "FZNAMES", property = "fznames"),
            @Result(column = "ASSISTAPPROVERID", property = "assistapproverid"),
            @Result(column = "ZSSTAFFID", property = "zsstaffid"),
            @Result(column = "ZCSTAFFID", property = "zcstaffid"),
            @Result(column = "CREATESTAFFID", property = "createstaffid"), 
            @Result(column = "REALNAME", property = "tblCreater.realname"),
    })
    List<TblYqnsAdvicenoteEntity> selectListByPageInfo(PageInfo<TblYqnsAdvicenoteEntity> pageInfo, TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo,BigDecimal staffid,String ids) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_ADVICENOTE WHERE ADVICEID = #{adviceid}")
    void deleteById(BigDecimal adviceid) throws Exception;

    @SelectProvider(method = "selectPlanCodeByOrgid", type = TblNbsjAdvicenoteMapperSqlConfig.class)
    Integer selectPlanCodeByOrgid(TblYqnsAdvicenoteEntity plan) throws Exception;

    @InsertProvider(method = "insertEntity", type = TblNbsjAdvicenoteMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "adviceid", keyColumn = "ADVICEID")
    void insertEntity(TblNbsjAdvicenoteEntity plan) throws Exception;

    @UpdateProvider(method = "updateEntity", type = TblNbsjAdvicenoteMapperSqlConfig.class)
    void updateEntity(TblNbsjAdvicenoteEntity plan) throws Exception;

    @Update("UPDATE TBL_NBSJ_ADVICENOTE SET status=1 WHERE ADVICEID = #{adviceid}")
    void calcelById(BigDecimal adviceid) throws Exception;


    @Delete("DELETE FROM TBL_LEGAL_ADVICE_ATT WHERE attid=#{attid}")
    void deleteFileInfoByAttId(Integer attid);

    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_ADVICE_ATT WHERE ADVICEID = #{adviceid})")
    List<TblAttachment> selectAtt(BigDecimal adviceid);

    @SelectProvider(method = "selectAprListByPageInfo", type = TblNbsjAdvicenoteMapperSqlConfig.class)
    @Results({ 
            @Result(column = "ADVICEID", property = "adviceid"),
            @Result(column = "CREATETIME", property = "creatrtime"),
            @Result(column = "ADVICENAME", property = "advicename"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATESTAFFID", property = "createstaffid"),
            @Result(column = "SJSSTIME", property = "sjsstime"),
            @Result(column = "TEAMLEADER", property = "teamleader"),
            @Result(column = "MAINREVIEWER", property = "mainreviewer"),
            @Result(column = "HELPREVIEWER", property = "helpreviewer"),
            @Result(column = "OPERATOR", property = "operator"),
            @Result(column = "projectname", property = "projectname"),
            @Result(column = "FZSTAFFIDS", property = "fzstaffids"),
            @Result(column = "FZNAMES", property = "fznames"),
            @Result(column = "NO", property = "no"),
    })
    List<TblYqnsAdviceAprEntity> selectAprListByPageInfo(PageInfo<TblYqnsAdviceAprEntity> pageInfo, String advicename,BigDecimal staffid,String ids,String xctype) throws Exception;

    @Select("SELECT TNA.* FROM TBL_YQNS_ADVICEAPR TNA  WHERE TNA.ADVICEID = #{adviceid}")
    @Results({
            @Result(column = "ADVICEID", property = "adviceid"),
            @Result(column = "CREATETIME", property = "creatrtime"),
            @Result(column = "ADVICENAME", property = "advicename"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATESTAFFID", property = "createstaffid"),
            @Result(column = "SJSSTIME", property = "sjsstime"),
            @Result(column = "TEAMLEADER", property = "teamleader"),
            @Result(column = "MAINREVIEWER", property = "mainreviewer"),
            @Result(column = "HELPREVIEWER", property = "helpreviewer"),
            @Result(column = "OPERATOR", property = "operator"),
            @Result(column = "NO", property = "no"),

    })
    TblYqnsAdviceAprEntity selectAprById(@Param("adviceid") BigDecimal adviceid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_ADVICEAPR WHERE ADVICEID = #{adviceid}")
    void deleteAprById(BigDecimal adviceid) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_ADVICENOTE WHERE adviceid IN (SELECT SJTZSID FROM TBL_YQNS_WTZG_SJTZS WHERE WTZGID = #{wtzgid})")
    @Results({
            @Result(column = "ADVICEID", property = "adviceid"),
            @Result(column = "CREATETIME", property = "creatrtime"),
            @Result(column = "ADVICENAME", property = "advicename"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATESTAFFID", property = "createstaffid"),
            @Result(column = "SJSSTIME", property = "sjsstime"),
            @Result(column = "TEAMLEADER", property = "teamleader"),
            @Result(column = "MAINREVIEWER", property = "mainreviewer"),
            @Result(column = "HELPREVIEWER", property = "helpreviewer"),
            @Result(column = "OPERATOR", property = "operator"),
            @Result(column = "NO", property = "no"),
            @Result(column = "PROJECTNAME", property = "projectname"),
            @Result(column = "TZSPID", property = "tzspid"),
            @Result(column = "FZSTAFFIDS", property = "fzstaffids"),
            @Result(column = "FZNAMES", property = "fznames"),
    })
    List<TblYqnsAdvicenoteEntity> selectListByWtzg(BigDecimal wtzgid) throws Exception;
    
    
    @SelectProvider(method = "selectAprListByspPageInfo", type = TblNbsjAdvicenoteMapperSqlConfig.class)
    @Results({
            @Result(column = "ADVICEID", property = "adviceid"), 
            @Result(column = "CREATETIME", property = "creatrtime"),
            @Result(column = "ADVICENAME", property = "advicename"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATESTAFFID", property = "createstaffid"),
            @Result(column = "SJSSTIME", property = "sjsstime"),
            @Result(column = "TEAMLEADER", property = "teamleader"),
            @Result(column = "MAINREVIEWER", property = "mainreviewer"),
            @Result(column = "HELPREVIEWER", property = "helpreviewer"),
            @Result(column = "OPERATOR", property = "operator"),
            @Result(column = "NO", property = "no"),
            @Result(column = "ORGNAME", property = "orgName"),
            @Result(column = "projectname", property = "projectname"),
    })
    List<TblYqnsAdviceAprEntity> selectAprListByspPageInfo(PageInfo<TblYqnsAdviceAprEntity> pageInfo, String advicename,BigDecimal staffid,String ids) throws Exception;
    
    

}
