package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.*;

import com.huabo.audit.oracle.entity.TblYqnsResult;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.hbfk.util.PageInfo;


public interface TblYqnsResultMapper extends tk.mybatis.mapper.common.Mapper<TblYqnsResult> {
	@Select("SELECT * from TBL_YQNS_LETTER WHERE LETTERID= #{letterid} ")
    TblYqnsResult getById(String sheetid);
    

    @SelectProvider(method="selectCountByPageInfo",type=TblYqnsResultMapperSqlConfig.class)
	Integer selectCountByPageInfo(PageInfo<TblYqnsResult> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;
    
    @Select("SELECT TNA.*,PRINCIPAL.REALNAME,TNA.PROJECTNAME projectname "
    		+ " FROM TBL_YQNS_RESULT TNA "
    		+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
    		+ "LEFT JOIN TBL_NBSJ_PROJECT pro ON TNA.PROJECTID = pro.PROJECTID " 
    		+ " WHERE TNA.RESULTID = #{resultid}")
    @Results({
    	@Result(column="RESULTID",property="resultid"),
    	@Result(column="RESULTCODE",property="resultcode"), 
    	@Result(column="PROJECTID",property="projectid"),
    	@Result(column="CREATETIME",property="createtime"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="CREATESTAFF",property="createstaff"),
    	@Result(column="projectname",property="projectname"),
    	@Result(column="ORGIDS",property="orgids"),
    	@Result(column="ORGIDNAMES",property="orgidnames"),
    	@Result(column="CONTRACTCODE",property="contractcode"),
    	@Result(column="CONTRACTNAME",property="contractname"),
    	@Result(column="CONTRACTMONEY",property="contractmoney"),
    	@Result(column="SGORGID",property="sgorgid"),
    	@Result(column="SGORGNAME",property="sgorgname"),
    	@Result(column="HZMONEY",property="hzmoney"),
    	@Result(column="HJMONEY",property="hjmoney"),
    	@Result(column="SDMONEY",property="sdmoney"),
    	@Result(column="RESULTIDS",property="resultids"),
    	@Result(column="AUDITOPINION",property="auditopinion"),
    })
   	TblYqnsResult selectById(BigDecimal resultid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblYqnsResultMapperSqlConfig.class)
    @Results({
    	@Result(column="RESULTID",property="resultid"),
    	@Result(column="RESULTCODE",property="resultcode"), 
    	@Result(column="PROJECTID",property="projectid"),
    	@Result(column="CREATETIME",property="createtime"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="CREATESTAFF",property="createstaff"),
    	@Result(column="projectname",property="projectname"),
    	@Result(column="ORGIDS",property="orgids"),
    	@Result(column="ORGIDNAMES",property="orgidnames"),
    	@Result(column="CONTRACTCODE",property="contractcode"),
    	@Result(column="CONTRACTNAME",property="contractname"),
    	@Result(column="CONTRACTMONEY",property="contractmoney"), 
    	@Result(column="SGORGID",property="sgorgid"),
    	@Result(column="SGORGNAME",property="sgorgname"),
    	@Result(column="HZMONEY",property="hzmoney"),
    	@Result(column="HJMONEY",property="hjmoney"),
    	@Result(column="SDMONEY",property="sdmoney"),
    	@Result(column="RESULTIDS",property="resultids"),
    	
    })
	List<TblYqnsResult> selectListByPageInfo(PageInfo<TblYqnsResult> pageInfo, TBlNbsjSheetVo tBlNbsjSheetVo, List<String> idList) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_RESULT WHERE RESULTID = #{resultid}")
    void deleteById(BigDecimal resultid) throws Exception;

    @Insert("INSERT INTO TBL_YQNS_RESULT_ATT(RESULTID, ATTID) VALUES (#{resultid}, #{attId})")
	void insetFileRelation(String attId, BigDecimal resultid) throws Exception;
    
    @Insert("DELETE FROM TBL_YQNS_RESULT_ATT WHERE RESULTID = #{resultid}")
	void delFileRelation( BigDecimal bigDecimal) throws Exception;


    @Select("SELECT TNA.*,PRINCIPAL.REALNAME FROM TBL_YQNS_RESULT TNA LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF WHERE TNA.RESULTIDS = #{resultids} ")
    @Results({
    	@Result(column="RESULTID",property="resultid"),
    	@Result(column="RESULTCODE",property="resultcode"), 
    	@Result(column="PROJECTID",property="projectid"),
    	@Result(column="CREATETIME",property="createtime"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="CREATESTAFF",property="createstaff"),
    	@Result(column="projectname",property="projectname"),
    	@Result(column="ORGIDS",property="orgids"),
    	@Result(column="ORGIDNAMES",property="orgidnames"),
    	@Result(column="CONTRACTCODE",property="contractcode"),
    	@Result(column="CONTRACTNAME",property="contractname"),
    	@Result(column="CONTRACTMONEY",property="contractmoney"),
    	@Result(column="SGORGID",property="sgorgid"),
    	@Result(column="SGORGNAME",property="sgorgname"),
    	@Result(column="HZMONEY",property="hzmoney"),
    	@Result(column="HJMONEY",property="hjmoney"),
    	@Result(column="SDMONEY",property="sdmoney"),
    	@Result(column="RESULTIDS",property="resultids"),
    })
	List<TblYqnsResult> selectByFahterResultIds(BigDecimal resultids);
     
     
    
//    @Select("select RS.RESULTCODE,RS.CONTRACTCODE,RS.CONTRACTNAME,RS.SGORGNAME,RS.CONTRACTMONEY,RS.SDMONEY,RS.HJMONEY,round(RS.HJMONEY/RS.CONTRACTNAME,2) hjl,jc.RWNAMES,"
//    		+ "SJ.GCLJS,SJ.DETY,SJ.WZJG,SJ.QTSJ,SJ.XCSC "
//    		+ "  from TBL_YQNS_RESULT rs  LEFT JOIN TBL_YQNS_GCXMZJ_ZJB jc ON JC.GCXMZJZJBID=RS.TEMPLATEID  "
//    		+ " LEFT JOIN TBL_YQNS_SJSS_WDRW_SJNR sj on sj.TEMPLATEID=JC.GCXMZJZJBID WHERE jc.RWNAMES  is not NULL ")
    @SelectProvider(method="findbyhj",type=TblYqnsResultMapperSqlConfig.class)
    @Results({
		@Result(column="RESULTID",property="resultid"),
    	@Result(column="RESULTCODE",property="resultcode"), 
    	@Result(column="CONTRACTCODE",property="contractcode"),
    	@Result(column="CONTRACTNAME",property="contractname"),
    	@Result(column="CONTRACTMONEY",property="contractmoney"),
    	@Result(column="SGORGNAME",property="sgorgname"),
    	@Result(column="HZMONEY",property="hzmoney"),
    	@Result(column="HJMONEY",property="hjmoney"),
    	@Result(column="SDMONEY",property="sdmoney"),
    	@Result(column="hjl",property="hjl"),
    	@Result(column="RWNAMES",property="rwnames"),
    })
	List<TblYqnsResult> findbyhj(TBlNbsjSheetVo tBlNbsjSheetVo, List<String> idList);
   
    
}
