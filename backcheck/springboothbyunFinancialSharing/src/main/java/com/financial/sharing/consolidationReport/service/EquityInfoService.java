package com.financial.sharing.consolidationReport.service;

import com.financial.sharing.consolidationReport.dto.EquityInfoQueryParam;
import com.financial.sharing.consolidationReport.entity.TblEquityInfo;

import java.util.List;

/**
 * 股权信息Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface EquityInfoService {

    /**
     * 查询股权信息列表
     * 
     * @param param 查询参数
     * @return 股权信息列表
     */
    List<TblEquityInfo> getEquityList(EquityInfoQueryParam param);

    /**
     * 根据ID查询股权信息
     * 
     * @param equityId 股权信息ID
     * @return 股权信息
     */
    TblEquityInfo getEquityById(String equityId);

    /**
     * 新增股权信息
     * 
     * @param equity 股权信息
     */
    void saveEquity(TblEquityInfo equity);

    /**
     * 修改股权信息
     * 
     * @param equity 股权信息
     */
    void updateEquity(TblEquityInfo equity);

    /**
     * 删除股权信息
     * 
     * @param equityId 股权信息ID
     */
    void deleteEquity(String equityId);

    /**
     * 更新股权信息状态
     * 
     * @param equityId 股权信息ID
     * @param isActive 是否启用
     */
    void updateEquityStatus(String equityId, String isActive);

    /**
     * 根据模型ID查询股权信息列表
     * 
     * @param modelId 模型ID
     * @return 股权信息列表
     */
    List<TblEquityInfo> getEquityListByModelId(String modelId);
}

