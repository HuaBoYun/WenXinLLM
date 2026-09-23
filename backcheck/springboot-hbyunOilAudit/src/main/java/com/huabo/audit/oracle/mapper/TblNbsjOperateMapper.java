package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;
import com.huabo.audit.oracle.entity.TblNbsjAuthorizationEntity;
import com.huabo.audit.oracle.entity.TblNbsjOperateEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.oracle.vo.TblNbsjOperateVo;
import com.huabo.audit.util.PageInfo;

public interface TblNbsjOperateMapper extends BaseMapper<TblNbsjOperateEntity>{
	@SelectProvider(method="selectCountByPageInfo",type=TblNbsjOperateMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,TblNbsjOperateVo tblNbsjOperateVo) throws Exception;

	@Select("SELECT * FROM TBL_NBSJ_AUDITPROGRAM TNA "
    		+ " WHERE TNA.PROGRAMID = #{programid}")
    @Results({
//    	@Result(column="OPERATEID",property="operateid"),
//    	@Result(column="FINISHTIME",property="finishtime"),
//    	@Result(column="FINISH",property="finish"),
//    	
//    	@Result(column="BUSINESSTYPE",property="businessType"),
//    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
//    	@Result(column="SUDITPROCESS",property="suditProcess"),
    })
    TblAduitProGramEntity selectById(@Param("programid") Integer programid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjOperateMapperSqlConfig.class)
    @Results({
    	@Result(column="OPERATEID",property="operateid"),
    	@Result(column="FINISHTIME",property="finishtime"),
    	@Result(column="FINISH",property="finish"),
    	
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="PROGRAMID",property="programid"),
    })
	List<TblNbsjOperateEntity> selectListByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,TblNbsjOperateVo tblNbsjOperateVo) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjOperateMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjOperateEntity plan) throws Exception;
    
    @Update("UPDATE TBL_NBSJ_OPERATE SET FINISH='1',FINISHTIME=SYSDATE WHERE OPERATEID = #{operateid}")
    void finalById(Integer operateid) throws Exception;
    
    
    
    //==
    @SelectProvider(method="selectAllCountByPageInfo",type=TblNbsjOperateMapperSqlConfig.class)
   	Integer selectAllCountByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,Integer projectid,String businessType,Integer targetId) throws Exception;

    @SelectProvider(method="selectAllListByPageInfo",type=TblNbsjOperateMapperSqlConfig.class)
    @Results({
    	@Result(column="OPERATEID",property="operateid"),
    	@Result(column="FINISHTIME",property="finishtime"),
    	@Result(column="FINISH",property="finish"),
    	
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="PROGRAMID",property="programid"),
    	
//    	@Result(column="REALNAME",property="authorization.teamStaff.staff.realname"),
    })
	List<TblNbsjOperateEntity> selectAllListByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,Integer projectid,String businessType,Integer targetId) throws Exception;
    
    
    //==
    @SelectProvider(method="selectRWFPCountByPageInfo",type=TblNbsjOperateMapperSqlConfig.class)
   	Integer selectRWFPCountByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,Integer tempId,Integer targetId) throws Exception;

    @SelectProvider(method="selectRWFPListByPageInfo",type=TblNbsjOperateMapperSqlConfig.class)
    @Results({
    	@Result(column="OPERATEID",property="operateid"),
    	@Result(column="FINISHTIME",property="finishtime"),
    	@Result(column="FINISH",property="finish"),
    	
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="PROGRAMID",property="programid"),
    	@Result(column="REALNAME",property="renyuan"),
    	
    	@Result(column="RISKSOURCE",property="riskSource"),
    	@Result(column="RISKPOINT",property="riskPoint"),
    	@Result(column="CONTROL",property="control"),
    	
//    	@Result(column="REALNAME",property="authorization.teamStaff.staff.realname"),
    })
	List<TblNbsjOperateEntity> selectRWFPListByPageInfo(PageInfo<TblNbsjOperateEntity> pageInfo,BigDecimal tempId,BigDecimal targetId) throws Exception;
    
    
    
    
    @Select("SELECT count (op.OPERATEID) FROM TBL_NBSJ_OPERATE op"
    		+ " LEFT JOIN TBL_NBSJ_AUTHORIZATION auth ON op. AUTHID = auth. AUTHID"
    		+ " LEFT JOIN TBL_NBSJ_AUDITPROGRAM audi ON audi.programid = auth.programid"
    		+ " LEFT JOIN TBL_NBSJ_TARGETTYPE target ON target.targetId = audi.targetId"
    		+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF team ON team. ID = auth.TEAMSTAFFID"
    		+ " WHERE auth.projectid = #{projectid}")
    Integer getCount(Integer projectid) throws Exception;
    
    
    @Select("SELECT count(*) FROM   TBL_NBSJ_OPERATE  WHERE AUTHID in   (SELECT AUTHID from TBL_NBSJ_AUTHORIZATION where PROGRAMID in (${ids})) AND FINISH=1")
    Integer getCountrw(String ids) throws Exception;
    
    
    
    
    
    
    
    
    @SelectProvider(method="selectRWFPListCon",type=TblNbsjOperateMapperSqlConfig.class)
    @Results({
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="PROGRAMID",property="programId"),
    	@Result(column="RISKSOURCE",property="riskSource"),
//    	@Result(column="REALNAME",property="renyuan"),
    })
	List<TblAduitProGramEntity> selectRWFPListCon(PageInfo<TblAduitProGramEntity> pageInfo,Integer projectid,Integer targetId) throws Exception;
    
    @Select("SELECT * FROM TBL_NBSJ_AUTHORIZATION TNA "
    		+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF team on team.id = TNA.TEAMSTAFFID "
			+ " LEFT JOIN TBL_STAFF staff on staff.staffid = team.staffid "
    		+ " WHERE TNA.programid = #{programId} "
    		+ " AND TNA.projectid = #{projectid} ")
    @Results({
    	@Result(column="REALNAME",property="authStaff.realname"),
    })
    List<TblNbsjAuthorizationEntity> getNbsjAuthorByPG(Integer projectid,Integer programId) throws Exception;
    
    
    
    
    @Select("SELECT * FROM TBL_NBSJ_OPERATE TNA "
    		+ " WHERE TNA.AUTHID = #{authId}")
    List<TblNbsjOperateEntity> findByAuthId(Integer authId) throws Exception;
    
    @Delete("DELETE FROM TBL_NBSJ_OPERATE WHERE operateid = #{operateid}")
    void deleteByOpid(Integer operateid) throws Exception;

    @InsertProvider(method="insertEntity",type=TblNbsjOperateMapperSqlConfig.class)
	void insertEntity(TblNbsjOperateEntity nbsjOperate) throws Exception;
    
    
    @SelectProvider(method="findPorgramByUser",type=TblNbsjOperateMapperSqlConfig.class)
	List<TblNbsjOperateEntity> findPorgramByUser(PageInfo<TblNbsjOperateEntity> pageInfo,Integer targetId,Integer projectId,BigDecimal staffid,String bsunitname) throws Exception;

    @SelectProvider(method="findCountPorgramByUser",type=TblNbsjOperateMapperSqlConfig.class)
 	Integer findCountPorgramByUser(PageInfo<TblNbsjOperateEntity> pageInfo,Integer targetId,Integer projectId,BigDecimal staffid,String bsunitname) throws Exception;
 
    //我的底稿选择业务单元  修改状态为执行中
    @Update("UPDATE TBL_NBSJ_OPERATE SET FINISH='2'  WHERE OPERATEID = #{operateid}")
    void InExecuion(Integer operateid) throws Exception;
    
}
