package com.huabo.system.mapper;

import com.huabo.system.entity.SystemAgentInfo;
import com.huabo.system.vo.param.SystemAgentModuleParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SystemAgentInfoMapper extends Mapper<SystemAgentInfo> {

	/**
	 * 更新空模块
	 * @param ids
	 */
	void updateNull(@Param("ids") List<Long> ids);

	/**
	 * 不同模块下的智能体-列表
	 * @param param
	 * @param roleIdsTrsList
	 * @return
	 */
	List<SystemAgentInfo> findSystemAgentModuleList(@Param("param") SystemAgentModuleParam param,
			@Param("roleIdsTrsList") List<String> roleIdsTrsList);
}