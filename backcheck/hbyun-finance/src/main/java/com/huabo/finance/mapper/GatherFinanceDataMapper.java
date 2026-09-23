package com.huabo.finance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.huabo.finance.mappersql.GatherFinanceDataMapperSqlConfig;

public interface GatherFinanceDataMapper {

	@Insert("${sql}")
	void executeInsertSql(@Param("sql")String sql) throws Exception;

	@Delete("${delSql}")
	void executeDeleteSql(@Param("delSql")String delSql) throws Exception;

	@Update("${updateSql}")
	void executeUpdateSql(@Param("updateSql")String updateSql) throws Exception;
	
	@SelectProvider(type = GatherFinanceDataMapperSqlConfig.class , method = "selectExistPrimaryKey")
	List<String> selectExistPrimaryKey(String primarycol, String pkValStr, String tableName, String fplanid) throws Exception;

	
	
}
