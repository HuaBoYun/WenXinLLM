package com.financial.sharing.budgetControl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.budgetControl.dto.MappingConfigQueryParam;
import com.financial.sharing.budgetControl.entity.TblMappingConfig;
import com.financial.sharing.util.MyJsonBean;

/**
 * 映射配置Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface MappingConfigService extends IService<TblMappingConfig> {

    /**
     * 分页查询映射配置
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean queryPage(MappingConfigQueryParam param);

    /**
     * 根据ID查询映射配置
     *
     * @param mappingId 映射ID
     * @return 映射配置
     */
    MyJsonBean queryById(String mappingId);

    /**
     * 新增映射配置
     *
     * @param config 映射配置
     * @return 操作结果
     */
    MyJsonBean add(TblMappingConfig config);

    /**
     * 修改映射配置
     *
     * @param config 映射配置
     * @return 操作结果
     */
    MyJsonBean modify(TblMappingConfig config);

    /**
     * 删除映射配置
     *
     * @param mappingId 映射ID
     * @return 操作结果
     */
    MyJsonBean remove(String mappingId);

    /**
     * 根据来源系统查询映射配置
     *
     * @param sourceSystem 来源系统
     * @param orgId 组织ID
     * @return 映射配置列表
     */
    MyJsonBean queryBySourceSystem(String sourceSystem, String orgId);
}

