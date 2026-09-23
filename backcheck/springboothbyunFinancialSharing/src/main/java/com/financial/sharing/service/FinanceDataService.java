package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;

import java.util.Map;

/**
 * 财务数据服务接口
 * 处理会计科目表、凭证库、辅助账等功能
 * 
 * @author system
 * @date 2024-12-19
 */
public interface FinanceDataService {

    // ==================== 会计科目表 ====================

    /**
     * 会计科目表
     */
    PageResult<Map<String, Object>> getKmList(Map<String, Object> params);

    /**
     * 凭证库
     */
    PageResult<Map<String, Object>> getPzkList(Map<String, Object> params);

    // ==================== 辅助账管理 ====================

    /**
     * 获取辅助账左侧树信息表
     */
    PageResult<Map<String, Object>> getAuxiliaryInfoTree(Map<String, Object> params);

    /**
     * 获取辅助账余额表
     */
    PageResult<Map<String, Object>> getAuxiliaryBalanceTable(Map<String, Object> params);

    /**
     * 获取辅助账信息表
     */
    PageResult<Map<String, Object>> getAuxiliaryInfoTable(Map<String, Object> params);

    /**
     * 获取辅助账总表
     */
    PageResult<Map<String, Object>> getAuxiliaryTotalTable(Map<String, Object> params);

    // ==================== 账簿管理 ====================

    /**
     * 获取明细账
     */
    PageResult<Map<String, Object>> getAuxiliaryDetailTable(Map<String, Object> params);

    /**
     * 获取日记账
     */
    PageResult<Map<String, Object>> getDiaryBookList(Map<String, Object> params);

    /**
     * 获取总分类账
     */
    PageResult<Map<String, Object>> getTotalAccountList(Map<String, Object> params);

    /**
     * 余额表
     */
    PageResult<Map<String, Object>> getYebList(Map<String, Object> params);

    /**
     * 凭证明细信息
     */
    PageResult<Map<String, Object>> getFinanceDataList(Map<String, Object> params);
}