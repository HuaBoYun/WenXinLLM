package com.huabo.system.mapper;

import com.huabo.system.entity.TblSystemRefReminderV;
import com.huabo.system.vo.param.TblSystemRefReminderQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblSystemRefReminderV1Mapper extends Mapper<TblSystemRefReminderV> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblSystemRefReminderV> getList(@Param("param") TblSystemRefReminderQueryParam param);
}