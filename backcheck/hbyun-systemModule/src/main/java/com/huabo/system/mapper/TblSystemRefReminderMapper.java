package com.huabo.system.mapper;

import com.huabo.system.entity.TblSystemRefReminder;
import com.huabo.system.vo.param.TblSystemRefReminderQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblSystemRefReminderMapper extends Mapper<TblSystemRefReminder> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblSystemRefReminder> getList(@Param("param") TblSystemRefReminderQueryParam param);
}