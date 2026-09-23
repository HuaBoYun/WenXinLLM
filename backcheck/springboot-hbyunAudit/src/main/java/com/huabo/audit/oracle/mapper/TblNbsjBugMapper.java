package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjBug;
import com.huabo.audit.oracle.entity.TblNbsjBugCriterion;
import com.huabo.audit.oracle.entity.TblNbsjBugEntity;
import com.huabo.audit.oracle.entity.TblNbsjInnerrule;
import com.huabo.audit.oracle.entity.TblNbsjOuterruleEntity;
import com.huabo.audit.oracle.vo.TblNbsjBugVo;
import com.huabo.audit.util.PageInfo;
import tk.mybatis.mapper.common.Mapper;

public interface TblNbsjBugMapper extends BaseMapper<TblNbsjBug> {
	 
	  @Select("select * from TBL_NBSJ_BUG TB LEFT JOIN TBL_NBSJ_BUG_CRITERION TBC ON TB.BUGID =TBC.BUGID WHERE TBC.BUGCRIID =#{criterionId}")
	  List<TblNbsjBugEntity> findByCriterionId(@Param("criterionId")BigDecimal criterionId);


	//==缺陷管理-附件列表============BEGIN
	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_BUG_ATT WHERE BUGID = #{bugId})")
	List<TblAttachment> defectFileList(BigDecimal bugId);

	  
	  //==
	  @Select("SELECT * from TBL_NBSJ_BUG WHERE BUGID= #{bugid} ")
	    TblNbsjBugEntity getById(String bugid);
	    
	    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjBugMapperSqlConfig.class)
	   	Integer selectCountByPageInfo(PageInfo<TblNbsjBugEntity> pageInfo,TblNbsjBugVo tblNbsjBugVo,Integer orgid,Integer companyid) throws Exception;

	    @Select("SELECT TNA.*,cc.*,ORG.ORGNAME FROM TBL_NBSJ_BUG TNA "
	    		+ "LEFT JOIN TBL_NBSJ_BUG_CRITERION cir on TNA.BUGID=CIR.BUGID "
				+ "LEFT JOIN TBL_NBSJ_BUGCRITERION cc on cc.BUGCRIID=CIR.BUGCRIID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG on ORG.ORGID=TNA.bugdepartment "
	    		+ " WHERE TNA.BUGID = #{bugid}")
	    @Results({
	    	@Result(column="BUGID",property="bugid"),
	    	@Result(column="BUGNUMBER",property="bugnumber"),
	    	@Result(column="BUGDESCRIPTE",property="bugdescripte"),
	    	@Result(column="DISCOVERTIME",property="discovertime"),
	    	@Result(column="MEMO",property="memo"),
	    	@Result(column="BUGCRILEVEL",property="bugcrilevel"),
	    	@Result(column="BUGCRIID",property="bugcriid"),
	    	@Result(column="ORGNAME",property="orgname"),
	    })
	   	TblNbsjBugEntity selectById(@Param("bugid") BigDecimal bugid) throws Exception;
	    
	    @SelectProvider(method="selectListByPageInfo",type=TblNbsjBugMapperSqlConfig.class)
	    @Results({
	    	@Result(column="BUGID",property="bugid"),
	    	@Result(column="BUGNUMBER",property="bugnumber"),
	    	@Result(column="BUGDESCRIPTE",property="bugdescripte"),
	    	@Result(column="DISCOVERTIME",property="discovertime"),
	    	@Result(column="MEMO",property="memo"),
	    	@Result(column="BUGCRILEVEL",property="bugcrilevel"),
	    	@Result(column="ORGNAME",property="orgname"),
	    })
		List<TblNbsjBugEntity> selectListByPageInfo(PageInfo<TblNbsjBugEntity> pageInfo,TblNbsjBugVo tblNbsjBugVo,Integer orgid,Integer companyid) throws Exception;

	    @Delete("DELETE FROM TBL_NBSJ_BUG WHERE BUGID = #{bugid}")
	    void deleteById(BigDecimal bugid) throws Exception;

	    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjBugMapperSqlConfig.class)
		Integer selectPlanCodeByOrgid(TblNbsjBugEntity plan) throws Exception;
	    
	    @InsertProvider(method="insertEntity",type=TblNbsjBugMapperSqlConfig.class)
	    @Options(useGeneratedKeys=true, keyProperty="bugid", keyColumn="BUGID")
		void insertEntity(TblNbsjBugEntity plan) throws Exception;

	    @UpdateProvider(method="updateEntity",type=TblNbsjBugMapperSqlConfig.class)
		void updateEntity(TblNbsjBugEntity plan) throws Exception;

	//先删除
	@Delete("DELETE FROM TBL_LEGAL_BUG_ATT WHERE BUGID=#{bugId}")
	void deleteAttmentRelationBUG(@Param("bugId")BigDecimal bugId);

	//再添加
	@Insert("INSERT INTO TBL_LEGAL_BUG_ATT(BUGID,ATTID) VALUES(#{bugId},#{id})")
	void insertAttmentRelationBUG(@Param("id")String id, @Param("bugId")BigDecimal bugId);

	//o.orgname,
	@Select("SELECT BU.BUGNUMBER,c.BUGCRILEVEL,TO_CHAR(BU.DISCOVERTIME,'yyyy-MM-dd'),BU.DISCOVERPERSON,BU.BUGSOURCE,BU.BUGPROPERTY,( SELECT ORGNAME FROM TBL_ORGANIZATION org WHERE 1 = 1 AND ORGTYPE <> 0 AND orgtype < 100 START WITH ORGID = BU.BUGDEPARTMENT CONNECT BY PRIOR FATHERORGID = ORGID AND ROWNUM = 1)orgs,BU.NEEDREFORM,BU.BUSINESSDESCRIPTION,bu.bugdescripte FROM TBL_BUG bu LEFT JOIN TBL_BUG_CRITERION cir on BU.BUGID=CIR.BUGID left join TBL_BUGCRITERION c on cir.BUGCRIID = c.BUGCRIID LEFT JOIN TBL_ORGANIZATION O ON BU.BUGDEPARTMENT = O.ORGID WHERE (BUGDEPARTMENT in (select ORGID from TBL_ORGANIZATION where 1=1  and ORGTYPE=0 start with fatherorgid=#{orgId} connect by prior fatherorgid=ORGID) or BUGDEPARTMENT=#{orgId}) ORDER BY BU.BUGID desc ")
	List<Object[]> defect_file_export(@Param("orgId") BigDecimal orgId);

	
	
	
	
	//=====内规Begin
	@SelectProvider(method="selectInnerCommonListByPageInfo",type=TblNbsjBugMapperSqlConfig.class)
    @Results({
    	@Result(column="INNRULID",property="innrulid"),
    	@Result(column="RULENAME",property="rulename"),
    	@Result(column="RULENUMBER",property="rulenumber"),
    	@Result(column="PUBLISHORG",property="publishorg"),
    	@Result(column="PUBLISHDATE",property="publishdate"),
    	@Result(column="ORGNAME",property="orgname"),
    })
	List<TblNbsjInnerrule> selectInnerCommonListByPageInfo(PageInfo<TblNbsjInnerrule> pageInfo,Integer bugid,Integer orgid) throws Exception;
	
	@SelectProvider(method="selectInnerCommonCountByPageInfo",type=TblNbsjBugMapperSqlConfig.class)
   	Integer selectInnerCommonCountByPageInfo(PageInfo<TblNbsjInnerrule> pageInfo,Integer bugid,Integer orgid) throws Exception;
	
	@Select("select COUNT(*) from TBL_NBSJ_BUG_INNERRULE WHERE BUGID =#{bugid} AND INNRULID=#{innrulid} ")
	Integer findCountByBugInner(@Param("innrulid")String innrulid, @Param("bugid")BigDecimal bugid);
	
	@Insert("INSERT INTO TBL_NBSJ_BUG_INNERRULE(BUGID,INNRULID) VALUES(#{bugid},#{innrulid})")
	void insertBugInnrulids(@Param("innrulid")String innrulid, @Param("bugid")BigDecimal bugid);
	
	@SelectProvider(method="selectInnerCommonListByLink",type=TblNbsjBugMapperSqlConfig.class)
    @Results({
    	@Result(column="INNRULID",property="innrulid"),
    	@Result(column="RULENAME",property="rulename"),
    	@Result(column="RULENUMBER",property="rulenumber"),
    	@Result(column="PUBLISHORG",property="publishorg"),
    	@Result(column="PUBLISHDATE",property="publishdate"),
    	@Result(column="ORGNAME",property="orgname"),
    })
	List<TblNbsjInnerrule> selectInnerCommonListByLink(PageInfo<TblNbsjInnerrule> pageInfo,Integer bugid,Integer orgid) throws Exception;
	
	@Delete("DELETE FROM TBL_NBSJ_BUG_INNERRULE WHERE BUGID =#{bugid} AND INNRULID=#{innrulid}")
    void deleteInnerLinkById(BigDecimal bugid,String innrulid) throws Exception;
	//=====内规End
	
	
	//=====外规==========BEGIN==========
	@SelectProvider(method="selectOuterCommonListByPageInfo",type=TblNbsjBugMapperSqlConfig.class)
    @Results({
    	@Result(column="OUTRULID",property="outrulid"),
    	@Result(column="RULENAME",property="rulename"),
    	@Result(column="RULENUMBER",property="rulenumber"),
    	@Result(column="PUBLISHORG",property="publishorg"),
    	@Result(column="PUBLISHDATE",property="publishdate"),
    	@Result(column="ORGNAME",property="orgname"),
    })
	List<TblNbsjOuterruleEntity> selectOuterCommonListByPageInfo(PageInfo<TblNbsjOuterruleEntity> pageInfo,Integer bugid,Integer orgid) throws Exception;
	
	@SelectProvider(method="selectOuterCommonCountByPageInfo",type=TblNbsjBugMapperSqlConfig.class)
   	Integer selectOuterCommonCountByPageInfo(PageInfo<TblNbsjOuterruleEntity> pageInfo,Integer bugid,Integer orgid) throws Exception;
	
	@Select("select COUNT(*) from TBL_NBSJ_BUG_OUTERRULE WHERE BUGID =#{bugid} AND OUTRULID=#{outrulid} ")
	Integer findCountByBugOuter(@Param("outrulid")String outrulid, @Param("bugid")BigDecimal bugid);
	
//	@Insert("INSERT INTO TBL_NBSJ_BUG_OUTERRULE(BUGID,OUTRULID) VALUES(#{bugid},#{outrulid})")
//	void insertBugOutrulids(String outrulid, Integer bugid);
	@InsertProvider(method="insertBugOutrulids",type=TblNbsjBugMapperSqlConfig.class)
	void insertBugOutrulids(String outrulid, BigDecimal bugid) throws Exception;
	
	@SelectProvider(method="selectOuterCommonListByLink",type=TblNbsjBugMapperSqlConfig.class)
    @Results({
    	@Result(column="OUTRULID",property="outrulid"),
    	@Result(column="RULENAME",property="rulename"),
    	@Result(column="RULENUMBER",property="rulenumber"),
    	@Result(column="PUBLISHORG",property="publishorg"),
    	@Result(column="PUBLISHDATE",property="publishdate"),
    })
	List<TblNbsjOuterruleEntity> selectOuterCommonListByLink(PageInfo<TblNbsjOuterruleEntity> pageInfo,Integer bugid,Integer orgid) throws Exception;
	
	@Delete("DELETE FROM TBL_NBSJ_BUG_OUTERRULE WHERE BUGID =#{bugid} AND OUTRULID=#{outrulid}")
    void deleteOuterLinkById(BigDecimal bugid,String outrulid) throws Exception;
	//=====外规==========END==========
	
	
	//==关联缺陷==========BEGIN==========
	@Select("select COUNT(*) from TBL_NBSJ_BUG WHERE BUGID =#{child_bugid} AND FATHERBUGID=#{bugid} ")
	Integer findCountByChildBugid(@Param("child_bugid")String child_bugid, @Param("bugid")BigDecimal bugid);
	
	@Update("UPDATE TBL_NBSJ_BUG SET FATHERBUGID = #{bugid} WHERE BUGID=#{child_bugid} ")
	void updateBugFatherBugId(@Param("child_bugid")String child_bugid, @Param("bugid")BigDecimal bugid);
	
	@SelectProvider(method="selectDefectLinkList",type=TblNbsjBugMapperSqlConfig.class)
    @Results({
    	@Result(column="BUGID",property="bugid"),
    	@Result(column="BUGNUMBER",property="bugnumber"),
    	@Result(column="BUGDESCRIPTE",property="bugdescripte"),
    	@Result(column="DISCOVERTIME",property="discovertime"),
    	@Result(column="MEMO",property="memo"),
    })
	List<TblNbsjBugEntity> selectDefectLinkList(PageInfo<TblNbsjBugEntity> pageInfo,BigDecimal bugid) throws Exception;
	
	@Update("UPDATE TBL_NBSJ_BUG SET FATHERBUGID = '' WHERE BUGID=#{child_bugid} ")
	void updateBugFatherNullBygId(@Param("child_bugid")String child_bugid);
	//==关联缺陷==========END==========
	
	
	
	
	//==
	@Insert("INSERT INTO TBL_NBSJ_BUG_CRITERION(BUGID,bugcriid) VALUES(#{bugid},#{bugcriid})")
	void insertCriLink(BigDecimal bugid,BigDecimal bugcriid);
	
	@Delete("DELETE FROM TBL_NBSJ_BUG_CRITERION WHERE BUGID =#{bugid} AND bugcriid=#{bugcriid}")
    void deleteCriLink(BigDecimal bugid,BigDecimal bugcriid) throws Exception;
	
	
	@Delete("DELETE FROM TBL_NBSJ_BUG_CRITERION WHERE BUGID =#{bugid} ")
    void deleteCriLinkall(BigDecimal bugid) throws Exception;
	
	@Select(" SELECT bugcriid,bugcrilevel FROM TBL_NBSJ_BUGCRITERION where 1=1 and status = 2 and BUGCRIID in ( SELECT max(BUGCRIID) FROM TBL_NBSJ_BUGCRITERION where ORGID=#{orgid} AND bugtype=#{bugtype} GROUP BY BUGCRILEVEL )")
	List<TblNbsjBugCriterion> findBugCriterion(BigDecimal orgid,String bugtype);
	
	
	@Delete("DELETE FROM TBL_LEGAL_BUG_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(BigDecimal attid);

	@Insert("INSERT INTO TBL_NBSJ_SHEET_BUG(SHEETID,BUGID) VALUES(#{sheetid},#{bugid})")
	void insertSheetBug(BigDecimal  sheetid,String  bugid);
	@Delete("DELETE FROM TBL_NBSJ_SHEET_BUG WHERE SHEETID =#{sheetid}")
    void deleteSheetBug(BigDecimal sheetid) throws Exception;
	
	
	@Select(" SELECT TNA.*,cc.*,ORG.ORGNAME FROM TBL_NBSJ_BUG TNA "
			+ "LEFT JOIN TBL_NBSJ_BUG_CRITERION cir on TNA.BUGID=CIR.BUGID "
			+ "LEFT JOIN TBL_NBSJ_BUGCRITERION cc on cc.BUGCRIID=CIR.BUGCRIID "
			+ "LEFT JOIN TBL_ORGANIZATION ORG on ORG.ORGID=TNA.bugdepartment "
			+ " WHERE TNA.BUGID IN (SELECT BUGID FROM TBL_NBSJ_SHEET_BUG WHERE SHEETID =#{SHEETID})")
	@Results({
    	@Result(column="BUGCRILEVEL",property="bugcrilevel"),
    	@Result(column="ORGNAME",property="orgname"),
    })
	List<TblNbsjBugEntity> selectNbsjBugList(BigDecimal SHEETID);


	List<TblNbsjBugEntity> selectListByPageInfoXml(@Param("tblNbsjBugVo")TblNbsjBugVo tblNbsjBugVo,@Param("sql") String sql);

	List<TblNbsjBugEntity> selectDefectLinkListXml(@Param("bugid")BigDecimal bugid);


	@Select("SELECT BUGCRIID FROM TBL_NBSJ_BUGCRITERION WHERE BUGCRILEVEL = #{bugid} AND BUGTYPE = #{bugtype} AND ORGID = #{orgid}")
	String selectBugcriid(String bugid,String bugtype,BigDecimal orgid);
}
