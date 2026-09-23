package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.*;


import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsLetter;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;


public interface TblYqnsLetterMapper extends tk.mybatis.mapper.common.Mapper<TblYqnsLetter> {
	@Select("SELECT * from TBL_YQNS_LETTER WHERE LETTERID= #{letterid} ")
    TblYqnsLetter getById(String sheetid);
    

    @SelectProvider(method="selectCountByPageInfo",type=TblYqnsLetterMapperSqlConfig.class)
	Integer selectCountByPageInfo(PageInfo<TblYqnsLetter> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;
    
    @Select("SELECT TNA.*,PRINCIPAL.REALNAME,pro.PRJOECTNAME projectname "
    		+ " FROM TBL_YQNS_LETTER TNA "
    		+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
    		+ "LEFT JOIN TBL_NBSJ_PROJECT pro ON TNA.PROJECTID = pro.PROJECTID "
    		+ " WHERE TNA.LETTERID = #{letterid}")
    @Results({
    	@Result(column="LETTERID",property="letterid"),
    	@Result(column="LETTERCODE",property="lettercode"),  
    	@Result(column="LETTERNAME",property="lettername"),
    	@Result(column="PROJECTID",property="projectid"),
    	@Result(column="CREATETIME",property="createtime"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="CREATESTAFF",property="createstaff"),
    	@Result(column="projectname",property="projectname"),
    	
    	
    	
    })
   	TblYqnsLetter selectById(BigDecimal letterid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblYqnsLetterMapperSqlConfig.class)
    @Results({ 
    	@Result(column="LETTERID",property="letterid"),
    	@Result(column="LETTERCODE",property="lettercode"), 
    	@Result(column="LETTERNAME",property="lettername"),
    	@Result(column="PROJECTID",property="projectid"),
    	@Result(column="CREATETIME",property="createtime"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="CREATESTAFF",property="createstaff"),
    	@Result(column="projectname",property="projectname"),
    	
    })
	List<TblYqnsLetter> selectListByPageInfo(PageInfo<TblYqnsLetter> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_LETTER WHERE LETTERID = #{letterid}")
    void deleteById(BigDecimal letterid) throws Exception;

 


    @Insert("INSERT INTO TBL_YQNS_LETTER_ATT(LETTERID, ATTID) VALUES (#{letterid}, #{attId})")
	void insetFileRelation(String attId, BigDecimal letterid) throws Exception;
    
    
    @Insert("DELETE FROM TBL_YQNS_LETTER_ATT WHERE LETTERID = #{letterid}")
	void delFileRelation( BigDecimal letterid) throws Exception;
    
     
}
