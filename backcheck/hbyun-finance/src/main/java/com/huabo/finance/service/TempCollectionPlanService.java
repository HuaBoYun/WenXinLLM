package com.huabo.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.TempCollectionPlan;

/**
 * 临时采集方案服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface TempCollectionPlanService extends IService<TempCollectionPlan> {

    /**
     * 保存临时采集方案
     */
    JsonBean saveTempPlan(TblStaffUtil staff, TempCollectionPlan plan) throws Exception;

    /**
     * 根据任务ID获取临时方案
     */
    JsonBean getTempPlanByTaskId(String taskId) throws Exception;

    /**
     * 删除临时方案
     */
    JsonBean deleteTempPlan(String planId) throws Exception;

    /**
     * 查询保留的临时方案列表
     */
    JsonBean getKeepPlanList(TblStaffUtil staff) throws Exception;
}

