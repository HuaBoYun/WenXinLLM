package com.financial.sharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.entity.TblProjectSettlement;
import java.util.List;

/**
 * 项目结算Service接口
 */
public interface TblProjectSettlementService extends IService<TblProjectSettlement> {
    
    /**
     * 根据项目ID查询结算记录
     */
    List<TblProjectSettlement> getByProjectId(String projectId);
    
    /**
     * 根据结算状态查询
     */
    List<TblProjectSettlement> getBySettlementStatus(String settlementStatus);
    
    /**
     * 获取项目的最新结算记录
     */
    TblProjectSettlement getLatestSettlement(String projectId);
}

