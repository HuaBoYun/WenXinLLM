package com.huabo.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblContractTypeof;
import com.huabo.system.entity.TblSystemImportLog;
import com.huabo.system.mappersql.TblContractTypeofMapperSqlConfig;
import com.huabo.system.mappersql.TblSystemImportLogMapperSqlConfig;

public interface TblSystemImportLogMapper extends BaseMapper<TblSystemImportLog> {

	@SelectProvider(type = TblSystemImportLogMapperSqlConfig.class , method = "selectImportLogPage")
	@Results({
		@Result(column = "REALNAME", property = "staffName")
	})
	IPage<TblSystemImportLog> selectImportLogPage(IPage<TblSystemImportLog> page, String createTime, Integer importType) throws Exception;


}
