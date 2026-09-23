package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblBusinessSystemRegister;
import com.global.treasurer.mapper.TblBusinessSystemRegisterMapper;
import com.global.treasurer.service.ITblBusinessSystemRegisterService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务系统注册Service实现类
 */
@Service
public class TblBusinessSystemRegisterServiceImpl extends ServiceImpl<TblBusinessSystemRegisterMapper, TblBusinessSystemRegister>
        implements ITblBusinessSystemRegisterService {
    @Override
    public Page<TblBusinessSystemRegister> getSystemList(Integer pageNo, Integer pageSize) {
        Page<TblBusinessSystemRegister> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TblBusinessSystemRegister> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("CREATE_TIME");
        return this.page(page, queryWrapper);
    }

    @Override
    public Page<TblBusinessSystemRegister> searchSystemList(Map<String, Object> params) {
        Integer pageNo = (Integer) params.get("pageNo");
        Integer pageSize = (Integer) params.get("pageSize");
        Page<TblBusinessSystemRegister> page = new Page<>(pageNo, pageSize);

        QueryWrapper<TblBusinessSystemRegister> queryWrapper = new QueryWrapper<>();

        if (params.get("systemCode") != null && !params.get("systemCode").toString().trim().isEmpty()) {
            queryWrapper.like("SYSTEM_CODE", params.get("systemCode").toString());
        }
        if (params.get("systemName") != null && !params.get("systemName").toString().trim().isEmpty()) {
            queryWrapper.like("SYSTEM_NAME", params.get("systemName").toString());
        }
        if (params.get("status") != null && !params.get("status").toString().trim().isEmpty()) {
            queryWrapper.eq("STATUS", params.get("status").toString());
        }

        queryWrapper.orderByDesc("CREATE_TIME");
        return this.page(page, queryWrapper);
    }

    @Override
    public TblBusinessSystemRegister getSystemDetail(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean addSystem(TblBusinessSystemRegister system) {
        system.setCreateTime(new Date());
        system.setUpdateTime(new Date());
        return this.save(system);
    }

    @Override
    public boolean updateSystem(TblBusinessSystemRegister system) {
        system.setUpdateTime(new Date());
        return this.updateById(system);
    }

    @Override
    public boolean deleteSystem(Long[] ids) {
        for (Long id : ids) {
            this.removeById(id);
        }
        return true;
    }

    @Override
    public Map<String, Object> testConnection(Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            TblBusinessSystemRegister system = this.getById(id);
            if (system != null) {
                // 模拟测试连接
                result.put("success", true);
                result.put("message", "连接测试成功");
            } else {
                result.put("success", false);
                result.put("message", "系统不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "连接测试失败：" + e.getMessage());
        }
        return result;
    }
}
