package com.huabo.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblSystemCustomizeScene;
import com.huabo.system.mappersql.TblSystemCustomizeSceneMapperSqlConfig;
import com.huabo.system.vo.param.TblSystemCustomizeSceneQueryParam;

public interface TblSystemCustomizeSceneMapper extends BaseMapper<TblSystemCustomizeScene> {

	/**
	 * 查询
	 * @param param
	 * @return
	 */
	@SelectProvider(method = "getList",type = TblSystemCustomizeSceneMapperSqlConfig.class)
	IPage<TblSystemCustomizeScene> getList(IPage<TblSystemCustomizeScene> page,@Param("param") TblSystemCustomizeSceneQueryParam param);
}