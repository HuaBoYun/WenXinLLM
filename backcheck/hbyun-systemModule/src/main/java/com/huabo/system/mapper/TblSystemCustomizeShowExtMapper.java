package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblSystemCustomizeShowExt;
import com.huabo.system.vo.param.CustomizeShowExtPreviewDetailsQueryParam;
import com.huabo.system.vo.result.TblSystemCustomizeShowExtResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblSystemCustomizeShowExtMapper extends BaseMapper<TblSystemCustomizeShowExt> {

	/**
	 * 预览展示-详情
	 * @param param
	 * @return
	 */
	//	@SelectProvider(type = TblSystemCustomizeShowExtMapperSqlConfig.class , method = "getCustomizeShowExtPreview")
	List<TblSystemCustomizeShowExtResult> getCustomizeShowExtPreview(@Param("param") CustomizeShowExtPreviewDetailsQueryParam param);
}