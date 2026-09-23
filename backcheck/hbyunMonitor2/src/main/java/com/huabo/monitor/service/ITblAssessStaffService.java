package com.huabo.monitor.service;

import com.huabo.monitor.entity.TblAssessStaff;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
public interface ITblAssessStaffService extends IService<TblAssessStaff> {


    void updateAttidNullById(BigDecimal bigDecimal);
    
    
    
}
