package com.huabo.system.mapper;

import com.huabo.system.entity.TblLanguageBasicConfig;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 基础语言配置表 Mapper 接口
 * </p>
 *
 * @author lhp
 * @since 2025-05-14
 */
public interface TblLanguageBasicConfigMapper extends BaseMapper<TblLanguageBasicConfig> {

	@SelectProvider(type = TblLanguageBasicConfigMapperSqlConfig.class , method = "selectPageList")
	IPage<TblLanguageBasicConfig> selectPageList(Page<TblLanguageBasicConfig> page, TblLanguageBasicConfig config) throws Exception;

	@SelectProvider(type = TblLanguageBasicConfigMapperSqlConfig.class , method = "selectConfigList")
	List<TblLanguageBasicConfig> selectConfigList(String infoid) throws Exception;

}
