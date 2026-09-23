package com.huabo.system.mapper;

import com.huabo.system.entity.TblSystemRefReminderAccess;
import com.huabo.system.vo.param.TblSystemRefReminderAccessQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblSystemRefReminderAccessMapper extends Mapper<TblSystemRefReminderAccess> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblSystemRefReminderAccess> getList(@Param("param") TblSystemRefReminderAccessQueryParam param);
}