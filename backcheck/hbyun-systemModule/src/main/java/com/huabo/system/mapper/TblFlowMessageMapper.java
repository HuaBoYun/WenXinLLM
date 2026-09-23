package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFlowMessage;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblFlowMessageMapper extends BaseMapper<TblFlowMessage> {

	@Insert("INSERT INTO TBL_FLOW_MESSAGE(MESSAGEID, MESSAGETIEL, SPONSOR, RECIPIENT, ISREAD, CREATETIME, PROCESSID, OPERATORID, FLOWID, FLOWTASKID, THISSTEPID, TASKNODEID) VALUES (#{messageId}, #{messageTiel}, #{sponsor}, #{recipient}, #{isRead}, SYSDATE, #{processId}, #{operatorId}, #{flowId}, #{flowTaskId}, #{thisStepId}, #{taskNodeId})")
	@Options(useGeneratedKeys=true, keyProperty="messageId", keyColumn="MESSAGEID")
	void insertReturnId(TblFlowMessage message) throws Exception;

	@Select("SELECT MAX(MESSAGEID) AS MESSAGEID, MESSAGETIEL, SPONSOR, RECIPIENT, ISREAD, PROCESSID, OPERATORID, FLOWID, FLOWTASKID, THISSTEPID, TASKNODEID FROM TBL_FLOW_MESSAGE WHERE RECIPIENT = #{staffid} AND ISREAD = 0 GROUP BY MESSAGETIEL, SPONSOR, RECIPIENT, ISREAD, PROCESSID, OPERATORID, FLOWID, FLOWTASKID, THISSTEPID, TASKNODEID ORDER BY MESSAGEID DESC")
	List<TblFlowMessage> selectPressInfoList(BigDecimal staffid);

	@Select("SELECT COUNT(0) FROM TBL_FLOW_MESSAGE WHERE RECIPIENT = #{staffid} AND FLOWID = #{flowId} AND ISREAD = 0 AND PROCESSID = #{processId} ")
	Integer selectPressCount(String flowId, String processId, BigDecimal staffid);

	@Update("UPDATE TBL_FLOW_MESSAGE SET ISREAD = 1 WHERE RECIPIENT = #{staffid} AND FLOWID = #{flowId} AND PROCESSID = #{processId} ")
	void updateIsReadStatus(String flowId, String processId, BigDecimal staffid);

	@Select("SELECT * FROM TBL_FLOW_MESSAGE WHERE FLOWID = #{flowId} AND FLOWTASKID = #{id} AND ISREAD = 0")
	List<TblFlowMessage> selectNoDealMessageList(@Param("id") String id,@Param("flowId") String flowId) throws Exception;

	@Update("UPDATE TBL_FLOW_MESSAGE SET ISREAD = 1 WHERE FLOWID = #{flowId} AND FLOWTASKID = #{id} AND ISREAD = 0")
	void updateNoDealMessageListToDeal(@Param("id") String id,@Param("flowId") String flowId) throws Exception;

	@Delete("DELETE FROM TBL_FLOW_MESSAGE WHERE FLOWID = #{flowId} AND FLOWTASKID = #{id} AND ISREAD = 0")
	void deleteNoDealMessageInfo(String id, String flowId);

	@Delete("DELETE FROM TBL_FLOW_MESSAGE WHERE FLOWTASKID = #{ymFromId}")
	void deleteInfoByProcessId(@Param("ymFromId")String ymFromId);

}
