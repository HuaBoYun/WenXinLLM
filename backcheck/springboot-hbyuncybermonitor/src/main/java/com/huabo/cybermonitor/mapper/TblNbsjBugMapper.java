package com.huabo.cybermonitor.mapper;

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
import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.TblAttachment;
import com.huabo.cybermonitor.entity.TblNbsjBugEntity;
import com.huabo.cybermonitor.entity.TblNbsjInnerrule;
import com.huabo.cybermonitor.entity.TblNbsjOuterruleEntity;
import com.huabo.cybermonitor.service.impl.TblNbsjBugCriterion;
import com.huabo.cybermonitor.vo.TblNbsjBugVo;

public interface TblNbsjBugMapper extends BaseMapper<TblNbsjBugEntity>{
	 
	  @Select("select * from TBL_YJPT_BUG TB LEFT JOIN TBL_YJPT_BUG_CRITERION TBC ON TB.BUGID =TBC.BUGID WHERE TBC.BUGCRIID =#{criterionId}")
	  List<TblNbsjBugEntity> findByCriterionId(@Param("criterionId")Integer criterionId);


	//==缺陷管理-附件列表============BEGIN
	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_BUG_ATT WHERE BUGID = #{bugId})")
	List<TblAttachment> defectFileList(Integer bugId);

	  
	  //==
	  @Select("SELECT * from TBL_YJPT_BUG WHERE BUGID= #{bugid} ")
	    TblNbsjBugEntity getById(String bugid);
	    
	    @SelectProvider(method="selectCountByPageInfo",type= TblNbsjBugMapperSqlConfig.class)
	   	Integer selectCountByPageInfo(PageInfo<TblNbsjBugEntity> pageInfo, TblNbsjBugVo tblNbsjBugVo, Integer orgid, Integer companyid) throws Exception;

	    @Select("SELECT TNA.*,cc.*,ORG.ORGNAME FROM TBL_YJPT_BUG TNA "
	    		+ "LEFT JOIN TBL_YJPT_BUG_CRITERION cir on TNA.BUGID=CIR.BUGID "
				+ "LEFT JOIN TBL_YJPT_BUGCRITERION cc on cc.BUGCRIID=CIR.BUGCRIID "
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
	   	TblNbsjBugEntity selectById(@Param("bugid") Integer bugid) throws Exception;
	    
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

	    @Delete("DELETE FROM TBL_YJPT_BUG WHERE BUGID = #{bugid}")
	    void deleteById(Integer bugid) throws Exception;

	    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjBugMapperSqlConfig.class)
		Integer selectPlanCodeByOrgid(TblNbsjBugEntity plan) throws Exception;
	    
	    @InsertProvider(method="insertEntity",type=TblNbsjBugMapperSqlConfig.class)
	    @Options(useGeneratedKeys=true, keyProperty="bugid", keyColumn="BUGID")
		void insertEntity(TblNbsjBugEntity plan) throws Exception;

	    @UpdateProvider(method="updateEntity",type=TblNbsjBugMapperSqlConfig.class)
		void updateEntity(TblNbsjBugEntity plan) throws Exception;

	//先删除
	@Delete("DELETE FROM TBL_LEGAL_BUG_ATT WHERE BUGID=#{bugId}")
	void deleteAttmentRelationBUG(@io.lettuce.core.dynamic.annotation.Param("bugId")Integer bugId);

	//再添加
	@Insert("INSERT INTO TBL_LEGAL_BUG_ATT(BUGID,ATTID) VALUES(#{bugId},#{id})")
	void insertAttmentRelationBUG(@io.lettuce.core.dynamic.annotation.Param("id")String id, @io.lettuce.core.dynamic.annotation.Param("bugId")Integer bugId);

	//o.orgname,
	@Select("SELECT BU.BUGNUMBER,c.BUGCRILEVEL,TO_CHAR(BU.DISCOVERTIME,'yyyy-MM-dd'),BU.DISCOVERPERSON,BU.BUGSOURCE,BU.BUGPROPERTY,( SELECT ORGNAME FROM TBL_ORGANIZATION org WHERE 1 = 1 AND ORGTYPE <> 0 AND orgtype < 100 START WITH ORGID = BU.BUGDEPARTMENT CONNECT BY PRIOR FATHERORGID = ORGID AND ROWNUM = 1)orgs,BU.NEEDREFORM,BU.BUSINESSDESCRIPTION,bu.bugdescripte FROM TBL_BUG bu LEFT JOIN TBL_BUG_CRITERION cir on BU.BUGID=CIR.BUGID left join TBL_BUGCRITERION c on cir.BUGCRIID = c.BUGCRIID LEFT JOIN TBL_ORGANIZATION O ON BU.BUGDEPARTMENT = O.ORGID WHERE (BUGDEPARTMENT in (select ORGID from TBL_ORGANIZATION where 1=1  and ORGTYPE=0 start with fatherorgid=#{orgId} connect by prior fatherorgid=ORGID) or BUGDEPARTMENT=#{orgId}) ORDER BY BU.BUGID desc ")
	List<Object[]> defect_file_export(@Param("orgId") Integer orgId);

	
	
	
	
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
	List<TblNbsjInnerrule> selectInnerCommonListByPageInfo(PageInfo<TblNbsjInnerrule> pageInfo, Integer bugid, Integer orgid) throws Exception;
	
	@SelectProvider(method="selectInnerCommonCountByPageInfo",type=TblNbsjBugMapperSqlConfig.class)
   	Integer selectInnerCommonCountByPageInfo(PageInfo<TblNbsjInnerrule> pageInfo,Integer bugid,Integer orgid) throws Exception;
	
	@Select("select COUNT(*) from TBL_YJPT_BUG_INNERRULE WHERE BUGID =#{bugid} AND INNRULID=#{innrulid} ")
	Integer findCountByBugInner(@Param("innrulid")String innrulid, @Param("bugid")Integer bugid);
	
	@Insert("INSERT INTO TBL_YJPT_BUG_INNERRULE(BUGID,INNRULID) VALUES(#{bugid},#{innrulid})")
	void insertBugInnrulids(@Param("innrulid")String innrulid, @Param("bugid")Integer bugid);
	
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
	
	@Delete("DELETE FROM TBL_YJPT_BUG_INNERRULE WHERE BUGID =#{bugid} AND INNRULID=#{innrulid}")
    void deleteInnerLinkById(Integer bugid,String innrulid) throws Exception;
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
	List<TblNbsjOuterruleEntity> selectOuterCommonListByPageInfo(PageInfo<TblNbsjOuterruleEntity> pageInfo, Integer bugid, Integer orgid) throws Exception;
	
	@SelectProvider(method="selectOuterCommonCountByPageInfo",type=TblNbsjBugMapperSqlConfig.class)
   	Integer selectOuterCommonCountByPageInfo(PageInfo<TblNbsjOuterruleEntity> pageInfo,Integer bugid,Integer orgid) throws Exception;
	
	@Select("select COUNT(*) from TBL_YJPT_BUG_OUTERRULE WHERE BUGID =#{bugid} AND OUTRULID=#{outrulid} ")
	Integer findCountByBugOuter(@Param("outrulid")String outrulid, @Param("bugid")Integer bugid);
	
//	@Insert("INSERT INTO TBL_YJPT_BUG_OUTERRULE(BUGID,OUTRULID) VALUES(#{bugid},#{outrulid})")
//	void insertBugOutrulids(String outrulid, Integer bugid);
	@InsertProvider(method="insertBugOutrulids",type=TblNbsjBugMapperSqlConfig.class)
	void insertBugOutrulids(String outrulid, Integer bugid) throws Exception;
	
	@SelectProvider(method="selectOuterCommonListByLink",type=TblNbsjBugMapperSqlConfig.class)
    @Results({
    	@Result(column="OUTRULID",property="outrulid"),
    	@Result(column="RULENAME",property="rulename"),
    	@Result(column="RULENUMBER",property="rulenumber"),
    	@Result(column="PUBLISHORG",property="publishorg"),
    	@Result(column="PUBLISHDATE",property="publishdate"),
    })
	List<TblNbsjOuterruleEntity> selectOuterCommonListByLink(PageInfo<TblNbsjOuterruleEntity> pageInfo,Integer bugid,Integer orgid) throws Exception;
	
	@Delete("DELETE FROM TBL_YJPT_BUG_OUTERRULE WHERE BUGID =#{bugid} AND OUTRULID=#{outrulid}")
    void deleteOuterLinkById(Integer bugid,String outrulid) throws Exception;
	//=====外规==========END==========
	
	
	//==关联缺陷==========BEGIN==========
	@Select("select COUNT(*) from TBL_YJPT_BUG WHERE BUGID =#{child_bugid} AND FATHERBUGID=#{bugid} ")
	Integer findCountByChildBugid(@Param("child_bugid")String child_bugid, @Param("bugid")Integer bugid);
	
	@Update("UPDATE TBL_YJPT_BUG SET FATHERBUGID = #{bugid} WHERE BUGID=#{child_bugid} ")
	void updateBugFatherBugId(@Param("child_bugid")String child_bugid, @Param("bugid")Integer bugid);
	
	@SelectProvider(method="selectDefectLinkList",type=TblNbsjBugMapperSqlConfig.class)
    @Results({
    	@Result(column="BUGID",property="bugid"),
    	@Result(column="BUGNUMBER",property="bugnumber"),
    	@Result(column="BUGDESCRIPTE",property="bugdescripte"),
    	@Result(column="DISCOVERTIME",property="discovertime"),
    	@Result(column="MEMO",property="memo"),
    })
	List<TblNbsjBugEntity> selectDefectLinkList(PageInfo<TblNbsjBugEntity> pageInfo,Integer bugid) throws Exception;
	
	@Update("UPDATE TBL_YJPT_BUG SET FATHERBUGID = '' WHERE BUGID=#{child_bugid} ")
	void updateBugFatherNullBygId(@Param("child_bugid")String child_bugid);
	//==关联缺陷==========END==========
	
	
	
	
	//==
	@Insert("INSERT INTO TBL_YJPT_BUG_CRITERION(BUGID,bugcriid) VALUES(#{bugid},#{bugcriid})")
	void insertCriLink(BigDecimal bugid,Integer bugcriid);
	
	@Delete("DELETE FROM TBL_YJPT_BUG_CRITERION WHERE BUGID =#{bugid} AND bugcriid=#{bugcriid}")
    void deleteCriLink(BigDecimal bugid,Integer bugcriid) throws Exception;
	
	@Select(" SELECT bugcriid,bugcrilevel FROM TBL_YJPT_BUGCRITERION where 1=1 and BUGCRIID in ( SELECT max(BUGCRIID) FROM TBL_YJPT_BUGCRITERION where ORGID=#{orgid} GROUP BY BUGCRILEVEL )")
	List<TblNbsjBugCriterion> findBugCriterion(Integer orgid);
	
	
	@Delete("DELETE FROM TBL_LEGAL_BUG_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(Integer attid);

}
