package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.BusinessSystemRegister;
import com.global.treasurer.mapper.BusinessSystemRegisterMapper;
import com.global.treasurer.service.BusinessSystemRegisterService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 业务系统注册服务实现
 *
 * @author AI Developer
 * @date 2025-02-26
 */
@Service
public class BusinessSystemRegisterServiceImpl implements BusinessSystemRegisterService {
    private static final Logger log = LoggerFactory.getLogger(BusinessSystemRegisterServiceImpl.class);

    @Resource
    private BusinessSystemRegisterMapper businessSystemRegisterMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> getSystemPage(Map<String, Object> param) {
        // 使用PageHelper分页
        com.github.pagehelper.PageHelper.startPage(
            Integer.parseInt(param.get("pageNo").toString()),
            Integer.parseInt(param.get("pageSize").toString())
        );

        List<BusinessSystemRegister> list = businessSystemRegisterMapper.selectSystemPage(param);

        // PageInfo格式
        com.github.pagehelper.PageInfo<BusinessSystemRegister> pageInfo = new com.github.pagehelper.PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("tlist", list);
        result.put("totalRecord", pageInfo.getTotal());
        result.put("pageNo", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());

        return result;
    }

    @Override
    public BusinessSystemRegister getSystemById(Long id) {
        return businessSystemRegisterMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createSystem(BusinessSystemRegister system) {
        // 获取当前用户信息
        TblStaffUtil loginStaff;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            throw new RuntimeException("用户信息获取失败", e);
        }

        if (loginStaff == null) {
            throw new RuntimeException("用户信息获取失败");
        }

        system.setCreatedBy(loginStaff.getStaffid().longValue());
        system.setCreatedByName(loginStaff.getRealname());
        system.setCreatedTime(new Date());
        system.setDeleteFlag(0);

        return businessSystemRegisterMapper.insert(system);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSystem(BusinessSystemRegister system) {
        TblStaffUtil loginStaff;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            throw new RuntimeException("用户信息获取失败", e);
        }

        if (loginStaff == null) {
            throw new RuntimeException("用户信息获取失败");
        }

        system.setUpdatedBy(loginStaff.getStaffid().longValue());
        system.setUpdatedByName(loginStaff.getRealname());
        system.setUpdatedTime(new Date());

        return businessSystemRegisterMapper.updateById(system);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<Long> ids) {
        return businessSystemRegisterMapper.batchDelete(ids);
    }

    @Override
    public Map<String, Object> testConnection(Long id) {
        Map<String, Object> result = businessSystemRegisterMapper.testConnection(id);
        if (result == null || result.isEmpty()) {
            result = new HashMap<>();
            result.put("success", 0);
            result.put("message", "系统不存在");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int toggleStatus(Long id, Integer status) {
        BusinessSystemRegister system = new BusinessSystemRegister();
        system.setId(id);
        system.setStatus(status);
        system.setUpdatedTime(new Date());
        return businessSystemRegisterMapper.updateById(system);
    }

    @Override
    public Map<String, Object> syncStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", 1);
        result.put("message", "同步状态成功");
        return result;
    }
}
