package com.huabo.etl.service;

import com.huabo.etl.domain.KettleJobLog;

import java.util.List;

/**
 * @ClassName : IKettleJobLogService
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2022-12-03 16:57:35
 */
public interface IKettleJobLogService {
    public List<KettleJobLog> selectKettleJobLogList(KettleJobLog kettleJobLog);
}
