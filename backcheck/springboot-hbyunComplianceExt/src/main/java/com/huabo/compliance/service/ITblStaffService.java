package com.huabo.compliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.TblStaff;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
public interface ITblStaffService extends IService<TblStaff> {

     // 部门id 分页查用户列表
     void findStaffByOrgid(IPage<TblStaff> page,  BigDecimal orgid);
     
     TblStaff getStaff(BigDecimal staffid);

}
