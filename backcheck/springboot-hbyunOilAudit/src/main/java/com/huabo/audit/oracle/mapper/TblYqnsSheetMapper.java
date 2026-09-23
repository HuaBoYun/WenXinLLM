package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.*;

import com.huabo.audit.oracle.entity.TblYqnsSheet;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.hbfk.util.PageInfo;


public interface TblYqnsSheetMapper extends tk.mybatis.mapper.common.Mapper<TblYqnsSheet> {
	@Select("SELECT * from TBL_NBSJ_SHEET WHERE SHEETID= #{sheetid} ")
    TblYqnsSheet getById(String sheetid);
    

    @SelectProvider(method="selectCountByPageInfo",type=TblYqnsSheetMapperSqlConfig.class)
	Integer selectCountByPageInfo(PageInfo<TblYqnsSheet> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;
    
    @Select("SELECT TNA.*,PRINCIPAL.REALNAME,pro.PRJOECTNAME projectname "
    		+ " FROM TBL_YQNS_SHEET TNA "
    		+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
    		+ "LEFT JOIN TBL_NBSJ_PROJECT pro ON TNA.PROJECTID = pro.PROJECTID "
    		+ " WHERE TNA.SHEETID = #{sheetid}")
    @Results({
    	@Result(column="SHEETID",property="sheetid"),
    	@Result(column="SHEETCODE",property="sheetcode"), 
    	@Result(column="AUDITMATTERS",property="auditmatters"),
    	@Result(column="PROJECTID",property="projectid"),
    	@Result(column="CREATETIME",property="createtime"),
    	@Result(column="STATE",property="state"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="ORGIDS",property="orgids"),
    	@Result(column="ORGIDNAMES",property="orgidnames"),
    	@Result(column="AUDITCONCLUSION",property="auditconclusion"),
    	@Result(column="AUDITHANDLING",property="audithandling"),
    	@Result(column="AUDITMATTERSSP",property="auditmatterssp"),
    	@Result(column="AUDITCONCLUSIONSP",property="auditconclusionsp"),
    	@Result(column="AUDITHANDLINGSP",property="audithandlingsp"),
    	@Result(column="AUDITMATTERSJH",property="auditmattersjh"),
    	@Result(column="AUDITCONCLUSIONJH",property="auditconclusionjh"),
    	@Result(column="AUDITHANDLINGJH",property="audithandlingjh"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="CREATESTAFF",property="createstaff"),
    	@Result(column="projectname",property="projectname"),
    	
    	
    })
   	TblYqnsSheet selectById(BigDecimal sheetid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblYqnsSheetMapperSqlConfig.class)
    @Results({
    	@Result(column="SHEETID",property="sheetid"),
    	@Result(column="SHEETCODE",property="sheetcode"), 
    	@Result(column="AUDITMATTERS",property="auditmatters"),
    	@Result(column="PROJECTID",property="projectid"),
    	@Result(column="CREATETIME",property="createtime"),
    	@Result(column="STATE",property="state"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="ORGIDS",property="orgids"),
    	@Result(column="ORGIDNAMES",property="orgidnames"),
    	@Result(column="AUDITCONCLUSION",property="auditconclusion"),
    	@Result(column="AUDITHANDLING",property="audithandling"),
    	@Result(column="AUDITMATTERSSP",property="auditmatterssp"),
    	@Result(column="AUDITCONCLUSIONSP",property="auditconclusionsp"),
    	@Result(column="AUDITHANDLINGSP",property="audithandlingsp"),
    	@Result(column="AUDITMATTERSJH",property="auditmattersjh"),
    	@Result(column="AUDITCONCLUSIONJH",property="auditconclusionjh"),
    	@Result(column="AUDITHANDLINGJH",property="audithandlingjh"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="CREATESTAFF",property="createstaff"),
    	@Result(column="projectname",property="projectname"),
    	
    })
	List<TblYqnsSheet> selectListByPageInfo(PageInfo<TblYqnsSheet> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SHEET WHERE SHEETID = #{sheetid}")
    void deleteById(BigDecimal sheetid) throws Exception;

 

    //
    @SelectProvider(method="getExportList",type=TblYqnsSheetMapperSqlConfig.class)
    @Results({
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="SHEETCODE",property="sheetCode"),
    	@Result(column="SHEETNAME",property="sheetName"),
    	@Result(column="SHEETTARGET",property="sheetTarget"),
    	@Result(column="AUDITORG",property="auditOrg"),
    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
    	@Result(column="BUSINESSAFFILIATION",property="businessAffiliation"),
    	@Result(column="APPROVER",property="approver"),
    	@Result(column="RISKLEVEL",property="riskLevel"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="TARGETNAME",property="targetName"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="PRJOECTNAME",property="projectName"),
    	@Result(column="ORGIDNAMES",property="orgIdNames"),
    	
    })
	List<TblYqnsSheet> getExportList(BigDecimal projectid) throws Exception;

    @Insert("INSERT INTO TBL_YQNS_SHEET_ATT(SHEETID, ATTID) VALUES (#{sheetid}, #{attId})")
	void insetFileRelation(String attId, BigDecimal sheetid) throws Exception;
    
    
    @Insert("DELETE FROM TBL_YQNS_SHEET_ATT WHERE SHEETID = #{sheetid}")
	void delFileRelation( BigDecimal sheetid) throws Exception;
    
     
}
