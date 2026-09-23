package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblSealCombination;

/**
 * 印鉴组合配置Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
public interface TblSealCombinationService extends IService<TblSealCombination> {

    /**
     * 分页查询印鉴组合
     */
    IPage<TblSealCombination> getSealCombinationPage(Integer page, Integer limit,
            String combinationCode, String combinationName, String combinationType,
            String businessType, String authorityLevel);
}
