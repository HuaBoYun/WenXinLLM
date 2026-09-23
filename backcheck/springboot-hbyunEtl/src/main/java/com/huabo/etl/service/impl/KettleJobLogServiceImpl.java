package com.huabo.etl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huabo.etl.domain.KettleJobLog;
import com.huabo.etl.mapper.KettleJobLogMapper;
import com.huabo.etl.service.IKettleJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : KettleJobLogServiceImpl
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2022-12-03 16:58:39
 */
@Service
public class KettleJobLogServiceImpl implements IKettleJobLogService {

    @Autowired
    private KettleJobLogMapper kettleJobLogMapper;

    @Override
    public List<KettleJobLog> selectKettleJobLogList(KettleJobLog kettleJobLog) {
        LambdaQueryWrapper<KettleJobLog> query = Wrappers.<KettleJobLog>lambdaQuery();
        query.eq(KettleJobLog::getJobname, kettleJobLog.getJobname());
        query.orderByDesc(KettleJobLog::getEnddate);
        return kettleJobLogMapper.selectList(query);
    }
}
