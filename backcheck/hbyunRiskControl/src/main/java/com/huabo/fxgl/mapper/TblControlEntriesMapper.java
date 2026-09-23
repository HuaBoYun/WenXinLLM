package com.huabo.fxgl.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.TblControlEntries;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 */
@Repository
public interface TblControlEntriesMapper extends BaseMapper<TblControlEntries> {
	
	
	@Select("  select * from TBL_CONTROL_ENTRIES where conmatid=#{conmatid}")
    List<TblControlEntries> getList(@Param("conmatid")BigDecimal conmatid);
	
	@Select("  delete from TBL_CONTROL_ENTRIES where conmatid=#{conmatid}")
   void deleteEntries(@Param("conmatid")BigDecimal conmatid);
	
	@Delete("delete from TBL_CONTROL_ENTRIES where conmatid in (select CONMATID from TBL_RISK_COPING_CMATRIX where RISKCOPINGID = #{copingid})")
	void deleByCopingId(@Param("copingid") BigDecimal copingid);
	
     List<TblControlEntries> getGroupList(@Param("queryParam")TblControlEntries queryParam);
     
     List<TblControlEntries> getGroupList2(@Param("queryParam")TblControlEntries queryParam);

    
}