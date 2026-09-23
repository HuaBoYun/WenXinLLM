package com.huabo.system.oracle.service;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblSystemCustomizeScene;
import com.huabo.system.vo.param.TblSystemCustomizeSceneQueryParam;

public interface ScheduledTaskService {

	/**
	 * 每天 0点 开始调用 设置需要超时提醒的合同落实信息 和 合同收付款信息 
	 * 默认 超时时间 为  3天  7天  15天  三次提醒，15天后每天提醒；
	 */
	JsonBean setCurrentContractDataRemind() throws Exception;

	/**
	 * 
	 * 获取当前用户 当天需要提醒的合同相关预警信息
	 */
	JsonBean getRemindInfoListAll(String token) throws Exception;

}
