package com.huabo.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.BdFinanceplan;
import com.huabo.finance.entity.TblSysScheduledTask;
import com.huabo.finance.vo.BdFinanceplanVo;
import com.huabo.finance.vo.TblSysScheduledTaskVo;

/**
 * <p>
 * 公司采集配置方案信息表 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-12
 */
public interface ScheduledTaskService extends IService<TblSysScheduledTask> {

	JsonBean add(TblSysScheduledTask tst) throws Exception;

	JsonBean modify(TblSysScheduledTask tst) throws Exception;

	JsonBean delete(String taskId) throws Exception;

	JsonBean getlist(TblSysScheduledTaskVo vo) throws Exception;

	JsonBean getOne(String taskId) throws Exception;

	JsonBean task_status(String taskId, Integer status) throws Exception;

}
