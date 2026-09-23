package com.huabo.compliance.service.impl;

import com.huabo.compliance.entity.TblTask;
import com.huabo.compliance.mapper.TblTaskMapper;
import com.huabo.compliance.service.ITblTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}
