package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.util.PageInfo;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblNbsjAdvicenotechangeMapper extends tk.mybatis.mapper.common.Mapper<TblYqnsAdvicenoteChangeEntity>{
	@Delete("DELETE from TBL_YQNS_ADVICENOTE_CHANGE WHERE CHANGEID= #{changeid} ")
    void deletebychangeid(Integer changeid);
	
	@Select("SELECT * from TBL_YQNS_ADVICENOTE_CHANGE WHERE PROJECTID= #{projectId} ")
    List<TblNbsjAdvicenoteChangeEntity> findAll(String projectId);

	//==
	@Select("SELECT * from TBL_YQNS_ADVICENOTE_CHANGE WHERE CHANGEID = #{changeid} ")
	TblYqnsAdvicenoteChangeEntity getById(Integer changeid);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjAdvicenotechangeMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjAdvicenoteChangeEntity> pageInfo,TblNbsjAdvicenoteChangeEntity tblNbsjAdvicenoteVo) throws Exception;

    @SelectProvider(method="selectListByPageInfo",type=TblNbsjAdvicenotechangeMapperSqlConfig.class)
    @Results({
    	@Result(column="CHANGEID",property="changeid"),
			@Result(column="CHANGEID",property="changeid"),
    	@Result(column="CREATRTIME",property="creatrtime"),
    	@Result(column="PROGECTID",property="progectid"),
    	@Result(column="CHANGETHING",property="changething"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="CHANGEBEFORE",property="changebefore"),
    	@Result(column="CHANGEAFTER",property="changeafter"),
    	@Result(column="CHANGEREASON",property="changereason"),
    	@Result(column="CREATESTAFFID",property="createstaffid"),
			@Result(column="JBR",property="jbr"),
			@Result(column="ORGID",property="orgid"),
			@Result(column="ADVICEID",property="adviceid"),
			@Result(column="CHANGETIME",property="changetime"),
			@Result(column="ORGNAME",property="orgname"),
			@Result(column="PROJECT_NAME",property="projectName"),

	})
	List<TblYqnsAdvicenoteChangeEntity> selectListByPageInfo(PageInfo<TblYqnsAdvicenoteChangeEntity> pageInfo, TblYqnsAdvicenoteChangeEntity tblYqnsAdvicenoteChange) throws Exception;


	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_ADVICE_ATT WHERE ADVICEID = #{adviceid})")
	List<TblAttachment> selectAtt(BigDecimal adviceid);
	
	
	@Select("SELECT * from TBL_YQNS_ADVICENOTE_CHANGE WHERE ADVICEID= #{adviceid} ")
    List<TblNbsjAdvicenoteChangeEntity> findbyadviceidAll(BigDecimal adviceid);

	
}
