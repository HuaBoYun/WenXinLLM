package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.TblNbsjSheetEntity;
import com.huabo.cybermonitor.entity.TblNbsjSheetReportEntity;
import com.huabo.cybermonitor.vo.TBlNbsjSheetVo;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblNbsjSheetMapper extends BaseMapper<TblNbsjSheetEntity> {
	@Select("SELECT * from TBL_YJPT_SHEET WHERE SHEETID= #{sheetid} ")
	TblNbsjSheetEntity getById(String sheetid);;
    
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjSheetMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo, TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;


    @Select("SELECT TNA.*,PRINCIPAL.REALNAME,ORG.ORGNAME "
    		+ " FROM TBL_YJPT_SHEET TNA "
    		+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
			+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
    		+ " WHERE TNA.SHEETID = #{sheetid}")
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
    	@Result(column="ORGNAME",property="orgname"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="CREATESTAFF",property="createStaff.staffid"),
    	
    	@Result(column="YJFH",property="yjfh"),
    	@Result(column="EJFH",property="ejfh"),
    	@Result(column="FIRSTSTAFFID",property="firststaffid"),
    	@Result(column="SECONDSTAFFID",property="secondstaffid"),
    	
    })
   	TblNbsjSheetEntity selectById(@Param("sheetid") Integer sheetid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjSheetMapperSqlConfig.class)
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
    	@Result(column="STATE",property="state"),
    	@Result(column="CREATESTAFF",property="createStaff.staffid"),
    	@Result(column="REALNAME",property="createStaff.realname"),
    	@Result(column="ORGNAME",property="orgname"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="YJFH",property="yjfh"),
    	@Result(column="EJFH",property="ejfh"),
    	
    })
	List<TblNbsjSheetEntity> selectListByPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;

	List<TblNbsjSheetReportEntity> selectListSheetReport(@Param("sheetid") Integer sheetid) throws Exception;

}
