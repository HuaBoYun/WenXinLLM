package com.global.treasurer.service;

import com.global.treasurer.entity.TblFinancingBasicParams;

import java.util.List;

/**
 * 融资基础参数Service接口
 *
 * @author 华博云开发团队
 * @since 2026-02-04
 */
public interface FinancingBasicParamsService {

    /**
     * 查询融资基础参数列表
     */
    List<TblFinancingBasicParams> getParamsList(String paramName, String paramType, String isEnabled);

    /**
     * 根据ID查询融资基础参数
     */
    TblFinancingBasicParams getParamsById(Long id);

    /**
     * 保存融资基础参数
     */
    void saveParams(TblFinancingBasicParams params);

    /**
     * 更新融资基础参数
     */
    void updateParams(TblFinancingBasicParams params);

    /**
     * 删除融资基础参数
     */
    void deleteParams(Long id);
}

