package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingConfigDTO;
import com.global.treasurer.dto.FinancingConfigQueryDTO;
import com.global.treasurer.entity.TblFinancingBasicParams;

/**
 * 融资配置Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-13
 */
public interface FinancingConfigService extends IService<TblFinancingBasicParams> {

    /**
     * 分页查询融资配置列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblFinancingBasicParams> getConfigList(FinancingConfigQueryDTO queryDTO);

    /**
     * 根据ID获取配置详情
     *
     * @param paramId 参数ID
     * @return 配置详情
     */
    TblFinancingBasicParams getConfigById(Long paramId);

    /**
     * 创建融资配置
     *
     * @param dto 配置信息
     * @return 创建后的配置
     */
    TblFinancingBasicParams createConfig(FinancingConfigDTO dto);

    /**
     * 更新融资配置
     *
     * @param dto 配置信息
     * @return 更新后的配置
     */
    TblFinancingBasicParams updateConfig(FinancingConfigDTO dto);

    /**
     * 删除融资配置
     *
     * @param paramId 参数ID
     * @return 是否成功
     */
    boolean deleteConfig(Long paramId);

    /**
     * 更新配置状态
     *
     * @param paramId 参数ID
     * @param status 状态(ENABLE-启用,DISABLE-禁用)
     * @return 是否成功
     */
    boolean updateStatus(Long paramId, String status);
}
