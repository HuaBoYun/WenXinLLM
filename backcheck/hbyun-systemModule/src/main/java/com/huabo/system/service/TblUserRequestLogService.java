package com.huabo.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.system.entity.TblUserRequestLog;

/**
 * 用户请求日志 Service 接口
 * 
 * @author Augment Agent
 * @date 2025-10-20
 */
public interface TblUserRequestLogService extends IService<TblUserRequestLog> {

    /**
     * 保存用户请求日志
     * 
     * @param log 日志对象
     * @return 是否保存成功
     */
    boolean saveRequestLog(TblUserRequestLog log);

}

