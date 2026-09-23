package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjFactbookEntity;
import com.huabo.audit.oracle.vo.TblNbsjFactbookVo;

public interface TblNbsjFactbookMapper extends BaseMapper<TblNbsjFactbookEntity>{
	
	@Select("SELECT * from TBL_NBSJ_FACTBOOK WHERE FACTID= #{factid} ")
    TblNbsjFactbookEntity getById(String factid);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjFactbookMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjFactbookEntity> pageInfo,TblNbsjFactbookVo tblNbsjFactbookVo) throws Exception;

    @Select("SELECT TNA.*,STAFF.REALNAME,PJ.PRJOECTNAME,PJ.PROJECTCODE "
    		+ "FROM TBL_NBSJ_FACTBOOK TNA "
    		+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.FACTSTAFFID "
			+ "LEFT JOIN TBL_NBSJ_PROJECT PJ ON PJ.PROJECTID = TNA.PROJECTID "
    		+ " WHERE TNA.FACTID = #{factid}")
    @Results({
    	@Result(column="FACTID",property="factid"),
    	@Result(column="FACTCODE",property="factcode"),
    	@Result(column="FACTNAME",property="factname"),
    	@Result(column="CREATETIME",property="createtime"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="DESCRIBE",property="describe"),
    	@Result(column="REALNAME",property="tblStaffByFactstaffid.realname"),
    	@Result(column="PRJOECTNAME",property="tblNbsjProject.prjoectName"),
    	@Result(column="PROJECTCODE",property="tblNbsjProject.projectCode"),
    })
   	TblNbsjFactbookEntity selectById(@Param("factid") Integer factid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjFactbookMapperSqlConfig.class)
    @Results({
    	@Result(column="FACTID",property="factid"),
    	@Result(column="FACTCODE",property="factcode"),
    	@Result(column="FACTNAME",property="factname"),
    	@Result(column="CREATETIME",property="createtime"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="DESCRIBE",property="describe"),
    	@Result(column="REALNAME",property="tblStaffByFactstaffid.realname"),
    	@Result(column="PRJOECTNAME",property="tblNbsjProject.prjoectName"),
    	
    })
	List<TblNbsjFactbookEntity> selectListByPageInfo(PageInfo<TblNbsjFactbookEntity> pageInfo, TblNbsjFactbookVo tblNbsjFactbookVo) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_FACTBOOK WHERE FACTID = #{factid}")
    void deleteById(Integer FACTID) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjFactbookMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjFactbookEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblNbsjFactbookMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="factid", keyColumn="FACTID")
	void insertEntity(TblNbsjFactbookEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjFactbookMapperSqlConfig.class)
	void updateEntity(TblNbsjFactbookEntity plan) throws Exception;
    

    @Delete("DELETE FROM TBL_NBSJ_FACTBOOKATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(BigDecimal attid);
}
