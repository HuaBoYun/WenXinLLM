package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblReportBug;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TblReportBugMapper extends BaseMapper<TblReportBug> {
 
	@Select(" SELECT * FROM TBL_REPORT_BUG WHERE REPORTID=#{id}")
	List<TblReportBug> getListByReportid(@Param("id")BigDecimal id);
	
	
	@Delete(" DELETE FROM TBL_REPORT_BUG WHERE REPORTID=#{reportid} and BUGID=#{bugid}")
	void deleteByEntityId(@Param("reportid")BigDecimal reportid,@Param("bugid")BigDecimal bugid);
	
	@Delete(" DELETE FROM TBL_REPORT_BUG WHERE REPORTID=#{reportid}")
	void deleteByReportId(@Param("reportid")BigDecimal reportid);
}