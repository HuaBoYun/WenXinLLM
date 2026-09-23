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

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjCertificate;
import com.huabo.audit.oracle.vo.TblNbsjCertificateVo;

public interface TblNbsjCertificateMapper extends tk.mybatis.mapper.common.Mapper<TblNbsjCertificate>{
	
	
	
	List<TblNbsjCertificate> findList(@Param("tblNbsjCertificateVo") TblNbsjCertificateVo tblNbsjCertificateVo);
	
	List<TblNbsjCertificate> finddgList(@Param("tblNbsjCertificateVo") TblNbsjCertificateVo tblNbsjCertificateVo);
	
	
	@SelectProvider(method="selectNbsjCertificateListCountByPageInfo",type=TblNbsjCertificateMapperSqlConfig.class)
   	Integer selectNbsjCertificateListCountByPageInfo(PageInfo<TblNbsjCertificate> pageInfo,BigDecimal orgid,String projectName,String auditMatter,String auditAbstract,BigDecimal projectId) throws Exception;

    @SelectProvider(method="selectNbsjCertificateListByPageInfo",type=TblNbsjCertificateMapperSqlConfig.class)
    @Results({
    	@Result(column="SJRY",property="auditUserName"),
    	@Result(column="ORGNAME",property="auditOrgName"),
    	@Result(column="REALNAME",property="auditStaffName"),
    	@Result(column="PRJOECTNAME",property="projectName"),
    })
	List<TblNbsjCertificate> selectNbsjCertificateListByPageInfo(PageInfo<TblNbsjCertificate> pageInfo,BigDecimal orgid,String projectName,String auditMatter,String auditAbstract,BigDecimal projectId) throws Exception;
    
    
    @SelectProvider(method="selectNbsjCertificateListByPageInfoDg",type=TblNbsjCertificateMapperSqlConfig.class)
    @Results({
    	@Result(column="SJRY",property="auditUserName"),
    	@Result(column="ORGNAME",property="auditOrgName"),
    	@Result(column="REALNAME",property="auditStaffName"),
    	@Result(column="PRJOECTNAME",property="projectName"),
    })
	List<TblNbsjCertificate> selectNbsjCertificateListByPageInfoDg(PageInfo<TblNbsjCertificate> pageInfo,String sheetid) throws Exception;
    
    @SelectProvider(method="selectNbsjCertificateListCountByPageInfoDg",type=TblNbsjCertificateMapperSqlConfig.class)
   	Integer selectNbsjCertificateListCountByPageInfoDg(PageInfo<TblNbsjCertificate> pageInfo,String sheetid) throws Exception;

    
    
    @Select("SELECT TNA.*,TT.ORGNAME,TS.REALNAME,STAFF.REALNAME auditUserName,PJ.PRJOECTNAME "
    		+ "FROM TBL_NBSJ_CERTIFICATE TNA "
    		+ " LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.AUDITUSERID "
			+ " LEFT JOIN TBL_NBSJ_PROJECT PJ ON PJ.projectId =TNA.projectId "
			+ " LEFT JOIN TBL_ORGANIZATION TT ON PJ.AUDITORGID =TT.ORGID "
			+ " LEFT JOIN TBL_STAFF TS ON PJ.AUDITSTAFFID = TS.STAFFID "
    		+ " WHERE TNA.certificateId = #{certificateId}")
 /*   @Results({
    	@Result(column="SJRY",property="auditUserName"),
    	@Result(column="ORGNAME",property="auditOrgName"),
    	@Result(column="REALNAME",property="auditStaffName"),
    	@Result(column="PRJOECTNAME",property="projectName"),
    	
    })*/
    TblNbsjCertificate selectById(@Param("certificateId") BigDecimal certificateId) throws Exception;

    
    @Delete("DELETE FROM TBL_NBSJ_CERTIFICATE WHERE CERTIFICATEID = #{certificateId}")
    void deleteById(BigDecimal certificateId) throws Exception;

//    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjCertificateMapperSqlConfig.class)
//	Integer selectPlanCodeByOrgid(TblNbsjCertificate plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblNbsjCertificateMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="certificateId", keyColumn="CERTIFICATEID")
	void insertEntity(TblNbsjCertificate plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjCertificateMapperSqlConfig.class)
	void updateEntity(TblNbsjCertificate plan) throws Exception;
    
    
    @Delete("DELETE FROM TBL_NBSJ_CERTIFICATE_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(BigDecimal attid);
    
    @Delete("DELETE FROM tbl_SHEET_CERTIFICATE WHERE SHEETID=#{sheetId}")
	void deleteSheetCertificate(BigDecimal sheetId);
    
    @Insert("INSERT INTO tbl_SHEET_CERTIFICATE(SHEETID,CERTIFICATEID) VALUES (#{sheetid},#{cerid})")
    void insertSheetCertificate(BigDecimal sheetid,String cerid);
    
    
    //@SelectProvider(method="selectCertificateList",type=TblNbsjCertificateMapperSqlConfig.class)
	@Select("SELECT TNA.*,TT.ORGNAME,TS.REALNAME,STAFF.REALNAME SJRY,PJ.PRJOECTNAME\n" +
			"\t\t\t\t FROM TBL_NBSJ_CERTIFICATE TNA\n" +
			"\t\t\t\tLEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.AUDITUSERID\n" +
			"\t\t\t\t LEFT JOIN TBL_NBSJ_PROJECT PJ ON PJ.projectId =TNA.projectId \n" +
			"\t\t\t\t LEFT JOIN TBL_ORGANIZATION TT ON PJ.AUDITORGID =TT.ORGID \n" +
			"\t\t\t\t LEFT JOIN TBL_STAFF TS ON PJ.AUDITSTAFFID = TS.STAFFID \n" +
			"\t\t\t\tWHERE 1=1 and certificateid  in (select distinct certificateid from tbl_SHEET_CERTIFICATE where SHEETID=#{sheetid})")
    @Results({
    	@Result(column="SJRY",property="auditUserName"),
    	@Result(column="ORGNAME",property="auditOrgName"),
    	@Result(column="REALNAME",property="auditStaffName"),
    	@Result(column="PRJOECTNAME",property="projectName"),
    })
	List<TblNbsjCertificate> selectCertificateList(BigDecimal sheetid) throws Exception;

	List<TblNbsjCertificate> selectNbsjCertificateListByPageInfoDgXml(@Param("categories") TblNbsjCertificate categories,@Param("sheetid") String sheetid);

	List<TblNbsjCertificate> selectNbsjCertificateListByPageInfoXml(@Param("certificate") TblNbsjCertificate certificate,@Param("secrectSql") String secrectSql );

	@Insert("INSERT INTO TBL_NBSJ_CERTIFICATE_STAMPFILE(CERTIFICATEID,ATTID) VALUES (#{certificateId},#{attid})")
	void insertStampedDocumentFile(@Param("certificateId")BigDecimal certificateId,@Param("attid") String attid) throws Exception;

	@Insert("DELETE FROM  TBL_NBSJ_CERTIFICATE_STAMPFILE WHERE CERTIFICATEID = #{certificateId} AND ATTID = #{attid}")
	void deleteStampedDocumentFile(@Param("certificateId")BigDecimal certificateId,@Param("attid") String attid) throws Exception;

	//@Select("SELECT * FROM TBL_NBSJ_CERTIFICATE WHERE CERTIFICATEID IN (SELECT CERTIFICATEID FROM tbl_SHEET_CERTIFICATE WHERE SHEETID=#{sheetid})")
}
