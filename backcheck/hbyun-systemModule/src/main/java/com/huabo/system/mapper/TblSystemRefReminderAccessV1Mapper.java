package com.huabo.system.mapper;

import com.huabo.system.entity.TblSystemRefReminderAccessV;
import com.huabo.system.vo.param.TblSystemRefReminderAccessQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblSystemRefReminderAccessV1Mapper extends Mapper<TblSystemRefReminderAccessV> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblSystemRefReminderAccessV> getList(@Param("param") TblSystemRefReminderAccessQueryParam param);
}