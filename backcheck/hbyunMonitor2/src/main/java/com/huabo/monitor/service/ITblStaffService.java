package com.huabo.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblAssessMarkVo;
import com.huabo.monitor.entity.TblStaff;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

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
     
     List<TblStaff> findStaffByOrgid(TblStaff queryParam);
     
     String selectNamesByids(String ids) throws Exception;
     
     String selectNameByids(BigDecimal id) throws Exception;

}
