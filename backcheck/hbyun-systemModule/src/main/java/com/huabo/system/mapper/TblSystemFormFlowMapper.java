package com.huabo.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblSystemFormFlow;

public interface TblSystemFormFlowMapper extends BaseMapper<TblSystemFormFlow> {

	@Select("SELECT * FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{id}")
	TblSystemFormFlow selectByYmformId(String id) throws Exception;
	
	@Select("SELECT * FROM TBL_SYSTEM_FORMFLOW WHERE FLOWID = #{flowId} AND YMFORMID = #{processId}")
	TblSystemFormFlow selectFormIdByProcessId(@Param("flowId")String flowId,@Param("processId") String processId) throws Exception;

	@Select("SELECT FLOWID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{id}")
	String selectFlowIdByTaskId(String id) throws Exception;

}
