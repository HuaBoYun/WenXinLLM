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
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbkzRiskEntity;
import com.huabo.audit.oracle.entity.TblNbsjRisktolerability;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.vo.TblNbkzRiskVo;
import com.huabo.audit.util.PageInfo;

public interface TblNbkzRiskMapper extends BaseMapper<TblNbkzRiskEntity>{
	 
	  //==
	  @Select("SELECT * from TBL_NBSJ_RISK WHERE RISKID= #{riskid} ")
	    TblNbkzRiskEntity getById(String riskid);
	    
	    @SelectProvider(method="selectCountByPageInfo",type=TblNbkzRiskMapperSqlConfig.class)
	   	Integer selectCountByPageInfo(PageInfo<TblNbkzRiskEntity> pageInfo,TblNbkzRiskVo tblNbkzRiskVo,Integer orgid) throws Exception;

	    @Select("SELECT TNA.*,STAFF.REALNAME,ORG.ORGNAME "
	    		+ " FROM TBL_NBSJ_RISK TNA "
	    		+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.CREATEORID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.ORGID "
	    		+ " WHERE TNA.RISKID = #{riskid}")
	    @Results({
	    	@Result(column="RISKID",property="riskid"),
	    	@Result(column="RISKNAME",property="riskname"),
	    	@Result(column="RISKNUMBER",property="risknumber"),
	    	@Result(column="OCCUREDDATE",property="occureddate"),
	    	@Result(column="DISCOVEREDDATE",property="discovereddate"),
	    	@Result(column="SYSORGID",property="sysOrgid"),
	    	@Result(column="ORGID",property="orgid"),
	    	@Result(column="ORGNAME",property="tblOrganiDem.orgname"),
	    	@Result(column="REALNAME",property="tblStaff.realname"),
	    })
	   	TblNbkzRiskEntity selectById(@Param("riskid") BigDecimal riskid) throws Exception;
	    
	    @SelectProvider(method="selectListByPageInfo",type=TblNbkzRiskMapperSqlConfig.class)
	    @Results({
	    	@Result(column="RISKID",property="riskid"),
	    	@Result(column="RISKNAME",property="riskname"),
	    	@Result(column="RISKNUMBER",property="risknumber"),
	    	@Result(column="OCCIREDDATE",property="occureddate"),
	    	@Result(column="DISCOVEREDDATE",property="discovereddate"),
	    	@Result(column="SYSORGID",property="sysOrgid"),
	    	@Result(column="ORGID",property="orgid"),
	    	@Result(column="ORGNAME",property="tblOrganiDem.orgname"),
	    	@Result(column="REALNAME",property="tblStaff.realname"),
	    })
		List<TblNbkzRiskEntity> selectListByPageInfo(PageInfo<TblNbkzRiskEntity> pageInfo,TblNbkzRiskVo tblNbkzRiskVo,Integer orgid) throws Exception;

	    @Delete("DELETE FROM TBL_NBSJ_RISK WHERE RISKID = #{riskid}")
	    void deleteById(BigDecimal riskid) throws Exception;

	    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbkzRiskMapperSqlConfig.class)
		Integer selectPlanCodeByOrgid(TblNbkzRiskEntity plan) throws Exception;
	    
	    @InsertProvider(method="insertEntity",type=TblNbkzRiskMapperSqlConfig.class)
	    @Options(useGeneratedKeys=true, keyProperty="riskid", keyColumn="RISKID")
		void insertEntity(TblNbkzRiskEntity plan) throws Exception;

	    @UpdateProvider(method="updateEntity",type=TblNbkzRiskMapperSqlConfig.class)
		void updateEntity(TblNbkzRiskEntity plan) throws Exception;


	//==风险发现-附件列表============BEGIN
	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_RISK_ATT WHERE RISKID = #{riskId})")
	List<TblAttachment> risk_file_list(BigDecimal riskId);

	//先删除
	@Delete("DELETE FROM TBL_LEGAL_RISK_ATT WHERE RISKID=#{riskId}")
	void deleteAttmentRelationRISK(@Param("riskId") BigDecimal riskId);

	//再添加
	@Insert("INSERT INTO TBL_LEGAL_RISK_ATT(RISKID,ATTID) VALUES(#{riskId},#{id})")
	void insertAttmentRelationRISK(@Param("id")String id, @Param("riskId")BigDecimal riskId);
	
	
	
	
	
	//==风险容忍度===================BEGIN=====
	@InsertProvider(method="insertToleEntity",type=TblNbkzRiskMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="toleid", keyColumn="TOLEID")
	void insertToleEntity(TblNbsjRisktolerability plan) throws Exception;

    @UpdateProvider(method="updateToleEntity",type=TblNbkzRiskMapperSqlConfig.class)
	void updateToleEntity(TblNbsjRisktolerability plan) throws Exception;
    
    @Delete("DELETE FROM TBL_NBSJ_RISKTOLERABILITY WHERE TOLEID = #{toleid}")
    void deletefxrrdById(BigDecimal toleid) throws Exception;
    
    @SelectProvider(method="selectFxrrdListByLink",type=TblNbkzRiskMapperSqlConfig.class)
	List<TblNbsjRisktolerability> selectFxrrdListByLink(PageInfo<TblNbsjRisktolerability> pageInfo,BigDecimal riskid) throws Exception;
  //==风险容忍度===================END=====
    
    
    
    //==
    @SelectProvider(method="selectListOrgInId",type=TblNbkzRiskMapperSqlConfig.class)
	List<TblOrganization> selectListOrgInId(@Param("sysorgid") String sysorgid) throws Exception;
    
    
    
    @Delete("DELETE FROM TBL_LEGAL_RISK_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(BigDecimal attid);

    
    @Results({
    	@Result(column="ORGNAME",property="tblOrganiDem.orgname"),
    	@Result(column="REALNAME",property="tblStaff.realname"),
    })
	List<TblNbkzRiskEntity> selectListByPageInfoXml(@Param("tblNbkzRiskVo") TblNbkzRiskVo tblNbkzRiskVo);
	
	
	//先删除
		@Delete("DELETE FROM TBL_NBSJ_RISKTOLERABILITY WHERE RISKID=#{riskId}")
		void deleterds(BigDecimal riskId);
		
		
		
		
		
	
		 @SelectProvider(method="selectListByPageInfolist",type=TblNbkzRiskMapperSqlConfig.class)
		    @Results({
		    	@Result(column="RISKID",property="riskid"),
		    	@Result(column="RISKNAME",property="riskname"),
		    	@Result(column="RISKNUMBER",property="risknumber"),
		    	@Result(column="OCCIREDDATE",property="occureddate"),
		    	@Result(column="DISCOVEREDDATE",property="discovereddate"),
		    	@Result(column="SYSORGID",property="sysOrgid"),
		    	@Result(column="ORGID",property="orgid"),
		    	@Result(column="ORGNAME",property="tblOrganiDem.orgname"),
		    	@Result(column="REALNAME",property="tblStaff.realname"),
		    })
			List<TblNbkzRiskEntity> selectListByPageInfolist(TblNbkzRiskVo tblNbkzRiskVo,BigDecimal orgid) throws Exception;

}
