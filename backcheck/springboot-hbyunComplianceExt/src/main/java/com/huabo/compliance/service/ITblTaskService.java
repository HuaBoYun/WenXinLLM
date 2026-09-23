package com.huabo.compliance.service;

import com.huabo.compliance.entity.TblTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-08-30
 */
public interface ITblTaskService extends IService<TblTask> {


    List<BigDecimal>  getStaff(BigDecimal assId);
}
