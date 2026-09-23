package com.huabo.audit.oracle.mapper;

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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjQuestionEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.vo.AuditQuestionVo;
import com.huabo.audit.oracle.vo.TblNbsjQuestionVo;
import com.huabo.audit.util.PageInfo;

public interface TblNbsjQuestionMapper extends BaseMapper<TblNbsjQuestionEntity>{

	List<TblNbsjQuestionEntity> getAuditQuestionList(AuditQuestionVo auditQuestion);
	
	@Select("SELECT * from TBL_NBSJ_QUESTION WHERE QUESTIONID= #{questionid} ")
    TblNbsjQuestionEntity getById(String questionid);
    
	@Select("select * from TBL_NBSJ_QUESTION where SHEETID = #{sheetId}")
	List<TblNbsjQuestionEntity> findNbsjQuestionBySheetIdSp(Integer sheetId) throws Exception;
	
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjQuestionMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjQuestionEntity> pageInfo,TblNbsjQuestionVo tblNbsjQuestionVo,Integer projectId) throws Exception;

    @Select("SELECT TNA.*,SHEET.BUSINESSAFFILIATION,SHEET.AUDITDISCOVERABLE,SHEET.SHEETCODE,STAFF.REALNAME, ORG.ORGNAME "
    		+ "FROM TBL_NBSJ_QUESTION TNA "
    		+ "LEFT JOIN TBL_NBSJ_SHEET SHEET ON SHEET.SHEETID = TNA.SHEETID "
			+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = SHEET.AUDITORG "
			+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = SHEET.CREATESTAFF "
    		+ "WHERE TNA.QUESTIONID = #{questionid}")
    @Results({
    	@Result(column="QUESTIONID",property="questionId"),
    	@Result(column="AUDITUNIT",property="auditUnit"),
    	@Result(column="AUDITEDUNIT",property="auditedUnit"),
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="FINDPEOPLE",property="findPeople"),
    	@Result(column="STATUS",property="status"),
    	
    	@Result(column="BUSINESSAFFILIATION",property="nbsjSheet.businessAffiliation"),
    	@Result(column="AUDITDISCOVERABLE",property="nbsjSheet.auditDiscoverable"),
    	@Result(column="SHEETCODE",property="nbsjSheet.sheetCode"),
    	@Result(column="REALNAME",property="nbsjSheet.createStaff.realname"),
    	@Result(column="ORGNAME",property="nbsjSheet.organization.orgname"),
    	
    })
   	TblNbsjQuestionEntity selectById(@Param("questionid") Integer questionid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjQuestionMapperSqlConfig.class)
    @Results({
    	@Result(column="QUESTIONID",property="questionId"),
    	@Result(column="AUDITUNIT",property="auditUnit"),
    	@Result(column="AUDITEDUNIT",property="auditedUnit"),
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="FINDPEOPLE",property="findPeople"),
    	@Result(column="STATUS",property="status"),
    	
    	@Result(column="BUSINESSAFFILIATION",property="nbsjSheet.businessAffiliation"),
    	@Result(column="AUDITDISCOVERABLE",property="nbsjSheet.auditDiscoverable"),
    	@Result(column="SHEETCODE",property="nbsjSheet.sheetCode"),
    	@Result(column="HZDG",property="nbsjSheet.hzdg"),
    	@Result(column="REALNAME",property="nbsjSheet.createStaff.realname"),
    	@Result(column="ORGNAME",property="nbsjSheet.organization.orgname"),
    	@Result(column="ORGIDS",property="nbsjSheet.orgIds"),
    	@Result(column="ORGIDNAMES",property="nbsjSheet.orgIdNames"),
    	
    })
	List<TblNbsjQuestionEntity> selectListByPageInfo(PageInfo<TblNbsjQuestionEntity> pageInfo,TblNbsjQuestionVo tblNbsjQuestionVo,Integer projectId) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_QUESTION WHERE QUESTIONID = #{questionid}")
    void deleteById(Integer QUESTIONID) throws Exception;
    
    @Delete("DELETE FROM TBL_NBSJ_QUESTION WHERE SHEETID = #{sheetid}")
    void deleteBySheet(Integer sheetid) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjQuestionMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjQuestionEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblNbsjQuestionMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="questionId", keyColumn="QUESTIONID")
	void insertEntity(TblNbsjQuestionEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjQuestionMapperSqlConfig.class)
	void updateEntity(TblNbsjQuestionEntity plan) throws Exception;
    
    
    @Select("select * from TBL_NBSJ_QUESTION WHERE QUESTIONID in (SELECT QUESTIONID from TBL_NBSJ_QUESTIONAFFIRM WHERE FACTID= #{factid})")
    List<TblNbsjQuestionEntity> getAuditQuestionByfactidList(@Param("factid") String factid) throws Exception;
    
    
    
    
    @Select("select count(q.QUESTIONID) from TBL_NBSJ_QUESTION  q"
    		+ " left join TBL_NBSJ_SHEET  s on  q.sheetid = s.sheetid"
    		+ " where s.projectId = #{projectid}")
    Integer getQuestionCount(Integer projectid) throws Exception;
    
    
    
    
    
    
    
    
    
    
    //==
    @SelectProvider(method="selectConfirmationCountByPageInfo",type=TblNbsjQuestionMapperSqlConfig.class)
   	Integer selectConfirmationCountByPageInfo(PageInfo<TblNbsjQuestionEntity> pageInfo,TblNbsjQuestionVo tblNbsjQuestionVo,Integer projectId) throws Exception;
    
    @SelectProvider(method="selectConfirmationListByPageInfo",type=TblNbsjQuestionMapperSqlConfig.class)
    @Results({
    	@Result(column="QUESTIONID",property="questionId"),
    	@Result(column="AUDITUNIT",property="auditUnit"),
    	@Result(column="AUDITEDUNIT",property="auditedUnit"),
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="FINDPEOPLE",property="findPeople"),
    	@Result(column="STATUS",property="status"),
    	
    	@Result(column="BUSINESSAFFILIATION",property="nbsjSheet.businessAffiliation"),
    	@Result(column="AUDITDISCOVERABLE",property="nbsjSheet.auditDiscoverable"),
    	@Result(column="SHEETCODE",property="nbsjSheet.sheetCode"),
    	@Result(column="REALNAME",property="nbsjSheet.createStaff.realname"),
    	@Result(column="ORGNAME",property="nbsjSheet.organization.orgname"),
    })
	List<TblNbsjQuestionEntity> selectConfirmationListByPageInfo(PageInfo<TblNbsjQuestionEntity> pageInfo,TblNbsjQuestionVo tblNbsjQuestionVo,Integer projectId) throws Exception;
    
    
    @SelectProvider(method="selectConfirmationListByFact",type=TblNbsjQuestionMapperSqlConfig.class)
    @Results({
    	@Result(column="QUESTIONID",property="questionId"),
    	@Result(column="AUDITUNIT",property="auditUnit"),
    	@Result(column="AUDITEDUNIT",property="auditedUnit"),
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="FINDPEOPLE",property="findPeople"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="BUSINESSAFFILIATION",property="nbsjSheet.businessAffiliation"),
    	@Result(column="AUDITDISCOVERABLE",property="nbsjSheet.auditDiscoverable"),
    	@Result(column="SHEETCODE",property="nbsjSheet.sheetCode"),
    	@Result(column="REALNAME",property="nbsjSheet.createStaff.realname"),
    	@Result(column="ORGNAME",property="nbsjSheet.organization.orgname"),
    })
	List<TblNbsjQuestionEntity> selectConfirmationListByFact(PageInfo<TblNbsjQuestionEntity> pageInfo,Integer factid) throws Exception;

    @Update("UPDATE TBL_NBSJ_QUESTION SET STATUS = #{statusyes} WHERE QUESTIONID = #{questionId}")
	void updateState(Integer questionId, Integer statusyes);

	
    @SelectProvider(method="getNbsjQuestList",type=TblNbsjQuestionMapperSqlConfig.class)
    List<TblNbsjSheetEntity>  getNbsjQuestList(TblNbsjQuestionVo tblNbsjQuestionVo,Integer projectId) throws Exception;
    
    
    
    @Update("UPDATE TBL_NBSJ_QUESTION SET  STATUS=2 WHERE SHEETID in ( SELECT SHEETID FROM TBL_NBSJ_SHEET WHERE STATE=6 AND RISKLEVEL='是' )  AND STATUS!=2")
    void updateBySheet() throws Exception;

}
