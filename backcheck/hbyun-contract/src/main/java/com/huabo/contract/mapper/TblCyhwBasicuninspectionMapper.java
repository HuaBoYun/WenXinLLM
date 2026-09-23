package com.huabo.contract.mapper;

import org.apache.ibatis.annotations.InsertProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblCyhwBasicuninspection;
import com.huabo.contract.mappersql.TblCyhwBasicuninspectionMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-08
 */
public interface TblCyhwBasicuninspectionMapper extends BaseMapper<TblCyhwBasicuninspection> {

	@InsertProvider(method="insertBybudget",type=TblCyhwBasicuninspectionMapperSqlConfig.class)
	void insertBybudget(TblCyhwBasicuninspection tcp) throws Exception;

}
