package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblFlowInformInfo;
import com.huabo.system.mappersql.TblFlowInformInfoMapperSqlConfig;


public interface TblFlowInformInfoMapper extends BaseMapper<TblFlowInformInfo> {

	/**
	 * 
	 * @param createstaff 知会发起人
	 * @param informstaff 知会通知人
	 * @param flowId  流程信息主键
	 * @param id      流程任务主键
	 * @param formId  业务表单信息主键
	 * @param thisStepId  当前审批节点信息主键
	 * @param infoId 
	 */
	@InsertProvider(method = "insertEntity" , type = TblFlowInformInfoMapperSqlConfig.class)
	void insertEntity(BigDecimal createstaff,BigDecimal informstaff, String flowId, String id
			, BigDecimal formId, String thisStepId, BigDecimal infoId) throws Exception;

	@UpdateProvider(method = "updateEntity" , type = TblFlowInformInfoMapperSqlConfig.class)
	void updateEntity(BigDecimal staffid,String flowId, String id,BigDecimal formId) throws Exception;

	@SelectProvider(method="selectPageListByFlowInfo",type=TblFlowInformInfoMapperSqlConfig.class)
	IPage<TblFlowInformInfo> selectPageListByFlowInfo(IPage<TblFlowInformInfo> page, TblFlowInformInfo inform);

	@SelectProvider(method="selectListByLoginUser",type=TblFlowInformInfoMapperSqlConfig.class)
	List<TblFlowInformInfo> selectListByLoginUser(String taskIds, BigDecimal staffid) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_FLOW_INFORMINFO WHERE INFORMSTAFFID = #{staffid} AND ISREAD = 0")
	Integer selectNoReadCountyLoginUser(@Param("staffid")BigDecimal staffid) throws Exception;

	@Delete("DELETE FROM TBL_FLOW_INFORMINFO WHERE ID = #{ymFromId}")
	void deleteInfoByProcessId(@Param("ymFromId")String ymFromId) throws Exception;

}
