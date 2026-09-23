package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.Organization;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.Staff;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ccc
 * @since 2022-07-15
 */
public interface IStaffService extends IService<Staff> {

    IPage findAllPageBeanPid(IPage page,Organization organization);

    JsonBean userList(String pid, String type, String token, Integer pageNo, Integer pageSize) throws Exception;

    String findRealNameById(String staffid);
    
    String selectNamesByids(String ids) throws Exception;
    
    String selectIdsByNames(String ids,BigDecimal orgid) throws Exception;

    String selectNamesByNames(String ids,BigDecimal orgid) throws Exception;

    String selectNameByids(BigDecimal id) throws Exception;
    
    List<Staff> getStaffsByOrgids(String ids,List<BigDecimal> orgids) throws Exception;

}
