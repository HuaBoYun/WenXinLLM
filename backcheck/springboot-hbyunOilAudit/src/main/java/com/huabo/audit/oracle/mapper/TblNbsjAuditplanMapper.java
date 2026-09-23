package com.huabo.audit.oracle.mapper;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.vo.TblNbsjAuditPlanVo;

/**
 * 描述: 计划编号Mapper
 * author: lyz
 * date: 2022-04-13
*/


public interface TblNbsjAuditplanMapper extends BaseMapper<TblNbsjAuditplan> {
    @Select("SELECT COUNT(*) FROM  TBL_NBSJ_AUDITPLAN WHERE AUDITORGID = #{origid}")
    Integer planManagePageListCount(String orgid);
    
    @SelectProvider(method="selectNbsjAuditPlanCountByPageInfo",type=TblNbsjAuditplanMapperSqlConfig.class)
   	Integer selectNbsjAuditPlanCountByPageInfo(PageInfo<TblNbsjAuditplan> pageInfo, String planStartDate,String planEndDate, BigDecimal orgid, TblNbsjAuditPlanVo plan) throws Exception;

    @Select("SELECT TNA.PLANID,TNA.PLANCODE,TNA.PLANNAME,TNA.PALNYEAR,TNA.PLANTYPE,TNA.PALNCOST,TNA.STARTTIME,TNA.ENDTIME,TNA.REMARKS,TNA.CREATETIME,TNA.STATUS,TNA.OPINIONSTATUS,TNA.ISAUDITOR,PRINCIPAL.STAFFID AS PRISTAFFID,PRINCIPAL.REALNAME AS PRIREALNAME,LEADER.STAFFID LEASTAFFID,LEADER.REALNAME LEAREALNAME,AUDITORG.ORGID AUDORGID,AUDITORG.ORGNAME AUDORGNAME,CRESTAFF.STAFFID CRESTAFFID,CRESTAFF.REALNAME CREREALNAME FROM TBL_NBSJ_AUDITPLAN TNA LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID WHERE TNA.PLANID = #{planid}")
    @Results({
    	@Result(column="PLANID",property="planid"),
    	@Result(column="PLANCODE",property="plancode"),
    	@Result(column="PLANNAME",property="planname"),
    	@Result(column="PALNYEAR",property="palnyear"),
    	@Result(column="PLANTYPE",property="plantype"),
    	@Result(column="PALNCOST",property="palncost"),
    	@Result(column="STARTTIME",property="starttime"),
    	@Result(column="ENDTIME",property="endtime"),
    	@Result(column="REMARKS",property="remarks"),
    	@Result(column="CREATESTAFFID",property="createstaffid"),
    	@Result(column="CREATETIME",property="createtime"),
    	@Result(column="UPDATETIMR",property="updatetimr"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="OPINIONSTATUS",property="opinionstatus"),
    	@Result(column="ISAUDITOR",property="isauditor"),
    	@Result(column="PRISTAFFID",property="principalStaff.staffid",id=true),
    	@Result(column="PRIREALNAME",property="principalStaff.realname"),
    	@Result(column="LEASTAFFID",property="leaderStaff.staffid",id=true),
    	@Result(column="LEAREALNAME",property="leaderStaff.realname"),
    	@Result(column="AUDORGID",property="auditOrgInfo.orgid",id=true),
    	@Result(column="AUDORGNAME",property="auditOrgInfo.orgname"),
    	@Result(column="CRESTAFFID",property="createStaff.staffid",id=true),
    	@Result(column="CREREALNAME",property="createStaff.realname"),
    })
   	TblNbsjAuditplan selectNbsjAuditPlanEntityById(@Param("planid") Integer planid) throws Exception;
    
    @SelectProvider(method="selectNbsjAuditPlanListByPageInfo",type=TblNbsjAuditplanMapperSqlConfig.class)
    @Results({
    	@Result(column="PLANID",property="planid"),
    	@Result(column="PLANCODE",property="plancode"),
    	@Result(column="PLANNAME",property="planname"),
    	@Result(column="PALNYEAR",property="palnyear"),
    	@Result(column="PLANTYPE",property="plantype"),
    	@Result(column="PALNCOST",property="palncost"),
    	@Result(column="STARTTIME",property="starttime"),
    	@Result(column="ENDTIME",property="endtime"),
    	@Result(column="REMARKS",property="remarks"),
    	@Result(column="CREATESTAFFID",property="createstaffid"),
    	@Result(column="CREATETIME",property="createtime"),
    	@Result(column="UPDATETIMR",property="updatetimr"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="OPINIONSTATUS",property="opinionstatus"),
    	@Result(column="ISAUDITOR",property="isauditor"),
    	@Result(column="PRISTAFFID",property="principalStaff.staffid",id=true),
    	@Result(column="PRIREALNAME",property="principalStaff.realname"),
    	@Result(column="LEASTAFFID",property="leaderStaff.staffid",id=true),
    	@Result(column="LEAREALNAME",property="leaderStaff.realname"),
    	@Result(column="AUDORGID",property="auditOrgInfo.orgid",id=true),
    	@Result(column="ORGNAME",property="auditOrgInfo.orgname"),
    	@Result(column="CRESTAFFID",property="createStaff.staffid",id=true),
    	@Result(column="CREREALNAME",property="createStaff.realname"),
    })
	List<TblNbsjAuditplan> selectNbsjAuditPlanListByPageInfo(PageInfo<TblNbsjAuditplan> pageInfo, String planStartDate, String planEndDate, BigDecimal orgId, TblNbsjAuditPlanVo plan) throws Exception;

	/**
	 *
	 * @param planStartDate
	 * @param planEndDate
	 * @param orgId
	 * @param plan
	 * @return
	 * @throws Exception
	 */
	List<TblNbsjAuditplan> findList(String planStartDate, String planEndDate, BigDecimal orgId, TblNbsjAuditPlanVo plan) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_AUDITPLAN WHERE PLANID = #{planId}")
    void deleteAuditPlanEntityById(@Param("planId")Integer planId) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjAuditplanMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjAuditplan plan) throws Exception;

    @InsertProvider(method="insertEntity",type=TblNbsjAuditplanMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="planid", keyColumn="PLANID")
	void insertEntity(TblNbsjAuditplan plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjAuditplanMapperSqlConfig.class)
	void updateEntity(TblNbsjAuditplan plan) throws Exception;

    @Insert("INSERT INTO TBL_NBSJ_AUDITPLANATT(PLANID,ATTID) VALUES (#{planid},#{aid})")
	void insertAttInfoForPlan(@Param("planid")BigDecimal planid,@Param("aid") String aid) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_AUDITPLANATT WHERE ATTID = #{attid}")
	void deleteFileInfoByAttId(@Param("attid")BigDecimal attid) throws Exception;

    @Select("SELECT ATTID FROM TBL_NBSJ_AUDITPLANATT WHERE PLANID = #{planId}")
	List<String> findAttIdListByPlanId(@Param("planId")Integer planId) throws Exception;

    @SelectProvider(method="selectAuditPlanByIds",type=TblNbsjAuditplanMapperSqlConfig.class)
	List<TblNbsjAuditplan> selectAuditPlanByIds(String planId, boolean stateFlag) throws Exception;

    @SelectProvider(method="selectAuditPlanListView",type=TblNbsjAuditplanMapperSqlConfig.class)
     @Results({
     	@Result(column="PLANID",property="planid"),
     	@Result(column="PLANCODE",property="plancode"),
     	@Result(column="PLANNAME",property="planname"),
     	@Result(column="PALNYEAR",property="palnyear"),
     	@Result(column="TOTALITEM",property="totalItem"),
     	@Result(column="UNENFORCEDITEM",property="unenforcedItem"),
     	@Result(column="CONDUCTITEM",property="condectItem"),
     	@Result(column="COMPLETEITEM",property="completeItem"),
     })
	List<TblNbsjAuditplan> selectAuditPlanListView(PageInfo<TblNbsjAuditplan> pageInfo) throws Exception;

    @SelectProvider(method="selectAuditPlanCountView",type=TblNbsjAuditplanMapperSqlConfig.class)
	Integer selectAuditPlanCountView(PageInfo<TblNbsjAuditplan> pageInfo) throws Exception;

    @Select("SELECT * FROM TBL_NBSJ_AUDITPLAN WHERE OPINIONSTATUS in(3,5) and AUDITORGID = #{orgid} ORDER BY PLANID DESC")
	List<TblNbsjAuditplan> selectPlanListForMergeNbsjProject(BigDecimal orgid) throws Exception;
    
    @Select("SELECT * FROM TBL_NBSJ_AUDITPLAN WHERE PLANTYPE=#{plantype} and PALNYEAR = #{palnyear} AND AUDITORGID=#{orgid} ORDER BY PLANID DESC")
	List<TblNbsjAuditplan> selectPlanListByyear(String palnyear,String plantype,BigDecimal orgid) throws Exception;
    
    
    @Select("SELECT * FROM TBL_NBSJ_AUDITPLAN WHERE PLANTYPE=#{plantype} and PALNYEAR = #{palnyear} AND AUDITORGID=#{orgid} and (OPINIONSTATUS is null or OPINIONSTATUS=0) ORDER BY PLANID DESC")
	List<TblNbsjAuditplan> selectPlanListByyeartz(String palnyear,String plantype,BigDecimal orgid) throws Exception;
    
    @Select("SELECT * FROM TBL_NBSJ_AUDITPLAN WHERE planId=#{planId} ")
   	List<TblNbsjAuditplan> selectPlanListPlanId(Integer planId) throws Exception;
    
    
    
    //========计划自增编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(PLANCODE,INSTR(PLANCODE,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_AUDITPLAN "
			+ " WHERE 1=1 ")
	String selectMaxPlanCode(String planCode) throws Exception;
    
    //========项目自增编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(PROJECTCODE,INSTR(PROJECTCODE,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_PROJECT "
			+ " WHERE 1=1 ")
	String selectMaxProjectCode(String projectCode) throws Exception;
    
    //=======审计通知自动编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(ADVICECOED,INSTR(ADVICECOED,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_ADVICENOTE "
			+ " WHERE 1=1 ")
	String selectMaxAdviceCode(String advicecoed) throws Exception;
    
    //=======疑点管理自动编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(DPNUMBER,INSTR(DPNUMBER,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_DOUBTFULPOINT "
			+ " WHERE 1=1 ")
	String selectMaxDpnumber(String dpnumber) throws Exception;
    
    //=======我的底稿自动编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(SHEETCODE,INSTR(SHEETCODE,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_SHEET "
			+ " WHERE 1=1 ")
	String selectMaxSheetcode(String sheetcode) throws Exception;
    
    //=======审计建议书自动编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(PROCODE,INSTR(PROCODE,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_PROPOSAL "
			+ " WHERE 1=1 ")
	String selectMaxProcode(String procode) throws Exception;
    
    //=======缺陷管理自动编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(BUGNUMBER,INSTR(BUGNUMBER,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_BUG "
			+ " WHERE 1=1 ")
	String selectMaxBugnumber(String bugnumber) throws Exception;
    
    //=======风险发现自动编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(RISKNUMBER,INSTR(RISKNUMBER,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_RISK "
			+ " WHERE 1=1 ")
	String selectMaxRisknumber(String risknumber) throws Exception;
    
    //=======整改方案自动编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(SOLUTIONCODE,INSTR(SOLUTIONCODE,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_REFORM_SOLUTION "
			+ " WHERE 1=1 ")
	String selectMaxSolutioncode(String solutioncode) throws Exception;
    
    //=======审计模板自动编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(templeteCode,INSTR(templeteCode,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_TEMPLETE "
			+ " WHERE 1=1 ")
	String selectMaxTempleteCode(String templeteCode) throws Exception;
    
    //=======管理制度自动编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(RULECODE,INSTR(RULECODE,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_INNERRULE "
			+ " WHERE 1=1 ")
	String selectMaxRulecode(String rulecode) throws Exception;
    
    //=======项目资料自动编号
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(PROJECT_DATAPRE_ID,INSTR(PROJECT_DATAPRE_ID,'-',-1)+1)))  "
			+ " FROM TBL_PROJECT_DATAPRE "
			+ " WHERE 1=1 ")
	String selectMaxProjectDatapreId(String projectDatapreId) throws Exception;
    
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(MBCODE,INSTR(MBCODE,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_MB "
			+ " WHERE 1=1 ")
	String selectMaxMbcodes(String mbcode) throws Exception;
    
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(CODE,INSTR(CODE,'-',-1)+1)))  "
			+ " FROM TBL_NBSJ_SJJYK "
			+ " WHERE 1=1 ")
	String selectMaxCode(String code) throws Exception;
    
    
}