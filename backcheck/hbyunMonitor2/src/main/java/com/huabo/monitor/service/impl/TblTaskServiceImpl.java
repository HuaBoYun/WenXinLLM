package com.huabo.monitor.service.impl;

import com.huabo.monitor.entity.TblTask;
import com.huabo.monitor.mapper.TblTaskMapper;
import com.huabo.monitor.service.ITblTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.redis.Random.RandomUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-08-30
 */
@Service

public class TblTaskServiceImpl extends ServiceImpl<TblTaskMapper, TblTask> implements ITblTaskService {

    @Resource
    TblTaskMapper taskMapper;

    @Override
    public List<BigDecimal> getStaff(BigDecimal assId) {
        return taskMapper.queryStaffIdsByAssid(assId);
    }

	@Override
	public void saveTask(TblTask task) {
		// TODO Auto-generated method stub
		task.setTaskid(RandomUtil.uuBigDecimalId());
		taskMapper.insert(task);
		
	}
}
