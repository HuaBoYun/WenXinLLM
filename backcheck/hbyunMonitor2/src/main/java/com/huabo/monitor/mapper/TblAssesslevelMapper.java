package com.huabo.monitor.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblAssesslevel;
import com.huabo.monitor.entity.TblTestTemplate;
import com.huabo.monitor.entity.TblTestplan;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

public interface TblAssesslevelMapper extends BaseMapper<TblAssesslevel> {

    List<TblAssesslevel> getRegionNew(@Param("fen")Double fen,@Param("orgid")BigDecimal orgid);

    List<TblAssesslevel> selectPageInfo(@Param("sql") String sql);
	
    @Select("select * from TBL_ASSESSLEVEL t where t.levelupper >= #{fen} and TBLCOMANY= #{orgid} order by t.levelupper desc")
    List<TblAssesslevel> getRegion(@Param("fen")Double fen,@Param("orgid")BigDecimal orgid);
   
    
    @SelectProvider(type=TblAssesslevelMapperSqlConfig.class,method="selectPageInfo")
	List<TblAssesslevel> selectPageInfo(PageInfo<TblAssesslevel> pageInfo) throws Exception;

    
    @SelectProvider(type=TblAssesslevelMapperSqlConfig.class,method="selectPageCount")
   	Integer selectPageCount(PageInfo<TblAssesslevel> pageInfo) throws Exception;

}
