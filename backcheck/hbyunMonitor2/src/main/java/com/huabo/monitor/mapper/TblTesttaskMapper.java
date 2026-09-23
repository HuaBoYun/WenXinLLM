package com.huabo.monitor.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.entity.TblTesttask;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-09-08
 */
public interface TblTesttaskMapper extends BaseMapper<TblTesttask> {

	List<Map<String,Object>> findAllnoSjResultNew(@Param("planid")BigDecimal planid);
	
	List<Map<String,Object>> findAllnoSjTrack2New(@Param("selectProjectid")BigDecimal selectProjectid);

	
	
    @Select("${sql}")
    List<TblTesttask> getListBySql(@Param("sql") String sql);
    
    @Delete("DELETE FROM TBL_TESTTASK where testtaskid=#{testtaskid}")
    void TblTesttask(@Param("testtaskid")BigDecimal testtaskid);

    @InsertProvider(type=TblTesttaskMapperSqlConfig.class,method="insertEntity")
    @Options(useGeneratedKeys=true, keyProperty="testtaskid", keyColumn="TESTTASKID")
	void insertEntity(TblTesttask tblTesttask) throws Exception;

    @UpdateProvider(type=TblTesttaskMapperSqlConfig.class,method="updateEntity")
	void updateEntity(TblTesttask tblTesttask) throws Exception;
}
