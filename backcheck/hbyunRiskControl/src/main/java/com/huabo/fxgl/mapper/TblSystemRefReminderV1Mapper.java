package com.huabo.fxgl.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblSystemRefReminderV;
import com.huabo.fxgl.vo.BusinessTableInfoResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface TblSystemRefReminderV1Mapper extends BaseMapper<TblSystemRefReminderV> {

	/**
	 * 查询业务表编号跟名称
	 * @param reminderBusinessTableId
	 * @return
	 */
	@Select("        select ttt.risknumber as no,tt.THREE_RISK as name" + "        from TBL_RISK_MONTHLY_EVALUATION tt"
			+ "        inner join TBL_RISK ttt on tt.RISKID = ttt.RISKID" + "        where tt.ID = #{reminderBusinessTableId}")
	BusinessTableInfoResult getBusinessTableInfo(@Param("reminderBusinessTableId") Long reminderBusinessTableId);

	@Select("select * from TBL_SYSTEM_REF_REMINDER_V where REMINDERBUSINESSTABLEID = #{refopmId}")
	TblSystemRefReminderV getByrefopmId(Long refopmId);
	
	/**
	 * 获取名称
	 * @param reminderStaffId
	 * @return
	 */
	@Select("select REALNAME from TBL_STAFF where STAFFID = #{reminderStaffId}")
	String getStaffName(@Param("reminderStaffId") Long reminderStaffId);

	/**
	 * 查询业务表编号跟名称
	 * @param reminderBusinessTableId
	 * @return
	 */
	@Select("select tt.CONTROLNUMBER as no from TBL_CONTROLMATRIX tt" + " inner join TBL_CONTROL_ENTRIES ttt on ttt.CONMATID = tt.CONMATID where ttt.ID = #{reminderBusinessTableId}")
	BusinessTableInfoResult getBusinessTableInfo1(@Param("reminderBusinessTableId") Long reminderBusinessTableId);
}