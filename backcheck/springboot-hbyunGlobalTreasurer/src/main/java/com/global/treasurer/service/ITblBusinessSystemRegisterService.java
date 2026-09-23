package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblBusinessSystemRegister;

import java.util.Map;

/**
 * 业务系统注册Service接口
 */
public interface ITblBusinessSystemRegisterService extends IService<TblBusinessSystemRegister> {

    /**
     * 分页查询业务系统列表
     */
    Page<TblBusinessSystemRegister> getSystemList(Integer pageNo, Integer pageSize);

    /**
     * 搜索业务系统列表
     */
    Page<TblBusinessSystemRegister> searchSystemList(Map<String, Object> params);

    /**
     * 获取详情
     */
    TblBusinessSystemRegister getSystemDetail(Long id);

    /**
     * 新增业务系统
     */
    boolean addSystem(TblBusinessSystemRegister system);

    /**
     * 更新业务系统
     */
    boolean updateSystem(TblBusinessSystemRegister system);

    /**
     * 删除业务系统
     */
    boolean deleteSystem(Long[] ids);

    /**
     * 测试连接
     */
    Map<String, Object> testConnection(Long id);
}
