package com.huabo.etl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huabo.etl.domain.KettleTransLog;
import com.huabo.etl.mapper.KettleTransLogMapper;
import com.huabo.etl.service.IKettleTransLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : KettleTransLogServiceImpl
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2022-12-03 17:06:57
 */
@Service
public class KettleTransLogServiceImpl implements IKettleTransLogService {


    @Autowired
    private KettleTransLogMapper kettleTransLogMapper;

    @Override
    public List<KettleTransLog> selectKettleTransLogList(KettleTransLog kettleTransLog) {
        LambdaQueryWrapper<KettleTransLog> query = Wrappers.<KettleTransLog>lambdaQuery();
        query.eq(KettleTransLog::getTransname, kettleTransLog.getTransname());
        query.orderByDesc(KettleTransLog::getEnddate);
        return kettleTransLogMapper.selectList(query);
    }
}
