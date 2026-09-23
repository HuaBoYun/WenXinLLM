package com.huabo.etl.service;

import com.huabo.etl.domain.KettleTransLog;

import java.util.List;

/**
 * @ClassName : IKettleTransLogService
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2022-12-03 17:06:16
 */
public interface IKettleTransLogService {
    List<KettleTransLog> selectKettleTransLogList(KettleTransLog kettleTransLog);
}
