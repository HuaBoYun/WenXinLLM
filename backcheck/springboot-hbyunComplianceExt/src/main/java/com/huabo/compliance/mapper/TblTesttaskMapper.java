package com.huabo.compliance.mapper;

import com.huabo.compliance.entity.TblTesttask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-09-08
 */
public interface TblTesttaskMapper extends BaseMapper<TblTesttask> {

    @Select("${sql}")
    List<TblTesttask> getListBySql(@Param("sql") String sql);
    
    @Delete("DELETE FROM TBL_COM_EXT_TESTTASK where testtaskid=#{testtaskid}")
    void TblTesttask(@Param("testtaskid")BigDecimal testtaskid);

    @InsertProvider(type=TblTesttaskMapperSqlConfig.class,method="insertEntity")
    @Options(useGeneratedKeys=true, keyProperty="testtaskid", keyColumn="TESTTASKID")
	void insertEntity(TblTesttask tblTesttask) throws Exception;

    @UpdateProvider(type=TblTesttaskMapperSqlConfig.class,method="updateEntity")
	void updateEntity(TblTesttask tblTesttask) throws Exception;
}
