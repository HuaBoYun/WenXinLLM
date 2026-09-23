package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblUruleTask;

import java.math.BigDecimal;

public interface ScheduledTaskService extends IService<TblUruleTask> {

    JsonBean add(TblUruleTask tut) throws Exception;

    JsonBean modify(TblUruleTask tut) throws Exception;

    JsonBean delete(String taskId) throws Exception;

    JsonBean getlist(Integer pageNumber, Integer pageSize, String taskName) throws Exception;

    JsonBean getOne(String taskId) throws Exception;

    JsonBean task_status(String taskId, Integer status) throws Exception;

}