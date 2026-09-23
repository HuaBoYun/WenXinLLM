package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblSecurityParameter;

/**
 * 安全参数配置Service接口
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
public interface TblSecurityParameterService extends IService<TblSecurityParameter> {

    /**
     * 分页查询安全参数配置
     *
     * @param page 页码
     * @param limit 每页数量
     * @param paramCode 参数编码
     * @param paramName 参数名称
     * @param paramType 参数类型
     * @param isEnabled 是否启用
     * @return 分页结果
     */
    IPage<TblSecurityParameter> getSecurityParameterPage(Integer page, Integer limit,
                                                         String paramCode, String paramName,
                                                         String paramType, Integer isEnabled);
}
