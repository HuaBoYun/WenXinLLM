package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteIssueEntity;
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

import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteEntity;
import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteVo;
import com.huabo.audit.util.PageInfo;

public interface TblNbsjAdvicenoteMapper extends tk.mybatis.mapper.common.Mapper<TblNbsjAdvicenoteEntity>{
	@Delete("DELETE from TBL_NBSJ_ADVICENOTE WHERE ADVICEID= #{adviceid} ")
    void deletebynoteid(String adviceid);
	
	@Select("SELECT * from TBL_NBSJ_ADVICENOTE WHERE PROJECTID= #{projectId} ")
    List<TblNbsjAdvicenoteEntity> findAll(String projectId);
	
	@Select("SELECT * from TBL_NBSJ_ADVICENOTE WHERE NOTEID = #{noteid} ")
    TblNbsjAdvicenoteEntity get(String noteid);
	
	@Select("SELECT * from TBL_NBSJ_ADVICENOTE WHERE PROJECTID= #{projectId} ")
    List<TblNbsjAdvicenoteEntity> isNoteCode(String code,String projectId);
	
	
	List<TblNbsjAdvicenoteEntity> findList(@Param("tblNbsjAdvicenoteVo") TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo);
	
	
	
	//==
	@Select("SELECT * from TBL_NBSJ_ADVICENOTE WHERE ADVICEID= #{adviceid} ") 
    TblNbsjAdvicenoteEntity getById(String adviceid);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjAdvicenoteMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjAdvicenoteEntity> pageInfo,TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo,TblStaffUtil loginStaff) throws Exception;

    @Select("SELECT TNA.* FROM TBL_NBSJ_ADVICENOTE TNA  WHERE TNA.ADVICEID = #{adviceid}")
    @Results({
    	@Result(column="ADVICEID",property="adviceid"),
    	@Result(column="CREATETIME",property="creatrtime"),
    	@Result(column="ADVICECOED",property="advicecoed"),
    	@Result(column="ADVICENAME",property="advicename"),
    	@Result(column="STATUS",property="status"),
//    	@Result(column="CONTENT",property="content"),
    	@Result(column="DES",property="des"),
    	@Result(column="CREATESTAFFID",property="createstaffid"),
    	
    })
   	TblNbsjAdvicenoteEntity selectById(@Param("adviceid") BigDecimal adviceid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjAdvicenoteMapperSqlConfig.class)
    @Results({
    	@Result(column="ADVICEID",property="adviceid"),
    	@Result(column="CREATRTIME",property="creatrtime"),
    	@Result(column="ADVICECOED",property="advicecoed"),
    	@Result(column="ADVICENAME",property="advicename"),
    	@Result(column="STATUS",property="status"),
//    	@Result(column="CONTENT",property="content"),
    	@Result(column="DES",property="des"),
    	@Result(column="CREATESTAFFID",property="createstaffid"),
    	@Result(column="REALNAME",property="tblCreater.realname"),
    })
	List<TblNbsjAdvicenoteEntity> selectListByPageInfo(PageInfo<TblNbsjAdvicenoteEntity> pageInfo,TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo,TblStaffUtil loginStaff) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_ADVICENOTE WHERE ADVICEID = #{adviceid}")
    void deleteById(BigDecimal adviceid) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjAdvicenoteMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjAdvicenoteEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblNbsjAdvicenoteMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="adviceid", keyColumn="ADVICEID")
	void insertEntity(TblNbsjAdvicenoteEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjAdvicenoteMapperSqlConfig.class)
	void updateEntity(TblNbsjAdvicenoteEntity plan) throws Exception;
    
    @Update("UPDATE TBL_NBSJ_ADVICENOTE SET status=1 WHERE ADVICEID = #{adviceid}")
    void calcelById(BigDecimal adviceid) throws Exception;
    
    
    @Delete("DELETE FROM TBL_LEGAL_ADVICE_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(BigDecimal attid);
    
    
    @Update("UPDATE TBL_NBSJ_ADVICENOTE SET XFRYIDS=#{userids},XFRYNAMES=#{usernames} WHERE ADVICEID = #{adviceid}")
    void xfry(String adviceid,String userids,String usernames) throws Exception;


	@Select("SELECT tsdt.*,ts.REALNAME as ReciverName FROM" +
			" (SELECT ts.REALNAME as createstaffName,tsd.CREATETIME,tsd.RECIVEDATE,tsd.RECIVER,tsd.ISREAD" +
			" FROM TBL_SYSTEM_DISTRIBUTION tsd LEFT JOIN TBL_STAFF ts ON tsd.CREATESTAFF= ts.staffID" +
			" WHERE DISTRIBUTIONTYPE = 'ZNSJSJTZS' AND FORMID = #{adviceId}) as tsdt left join TBL_STAFF ts ON tsdt.reciver= ts.staffID")
	List<TblNbsjAdvicenoteIssueEntity> selectListByIssuePageInfo(String adviceId);


	@Select("SELECT COUNT(0) FROM TBL_SYSTEM_DISTRIBUTION tsd LEFT JOIN TBL_STAFF ts ON tsd.CREATESTAFF= ts.staffID " +
			"WHERE DISTRIBUTIONTYPE = 'ZNSJSJTZS' AND FORMID = #{adviceId}")
	Integer selectCountIssueByPageInfo(PageInfo<TblNbsjAdvicenoteIssueEntity> pageInfo,String adviceId) throws Exception;

}
