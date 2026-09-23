package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TcSecurityParameter;

import java.util.List;
import java.util.Map;

/**
 * 安全参数配置Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
public interface TcSecurityParameterService extends IService<TcSecurityParameter> {

    /**
     * 分页查询安全参数列表
     * @param params 查询参数
     * @return 安全参数列表
     */
    List<TcSecurityParameter> selectPageList(Map<String, Object> params);

    /**
     * 根据ID查询安全参数详情
     * @param id 主键ID
     * @return 安全参数详情
     */
    TcSecurityParameter selectDetailById(Long id);

    /**
     * 根据参数编码查询
     * @param paramCode 参数编码
     * @return 安全参数
     */
    TcSecurityParameter selectByParamCode(String paramCode);

    /**
     * 保存安全参数
     * @param entity 安全参数实体
     * @return 是否成功
     */
    boolean saveParameter(TcSecurityParameter entity);

    /**
     * 更新安全参数
     * @param entity 安全参数实体
     * @return 是否成功
     */
    boolean updateParameter(TcSecurityParameter entity);

    /**
     * 删除安全参数
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteParameter(Long id);
}

