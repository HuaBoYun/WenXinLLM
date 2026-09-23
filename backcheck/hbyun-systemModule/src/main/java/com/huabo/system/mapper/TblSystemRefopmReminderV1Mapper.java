package com.huabo.system.mapper;


import com.huabo.system.entity.TblSystemRefopmReminderV;
import com.huabo.system.vo.result.RefReminderListResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblSystemRefopmReminderV1Mapper extends Mapper<TblSystemRefopmReminderV> {

	/**
	 * 业务表查询
	 * @param reminderBusinessTable
	 * @param reminderBusinessStaff
	 * @param reminderBusinessTime
	 * @param reminderBusinessContent
	 * @return
	 */
	List<RefReminderListResult> findBusinessList(@Param("reminderBusinessTable") String reminderBusinessTable, @Param("reminderBusinessId") String reminderBusinessId,
			@Param("reminderBusinessStaff") String reminderBusinessStaff, @Param("reminderBusinessTime") String reminderBusinessTime,
			@Param("reminderBusinessContent") String reminderBusinessContent);
}