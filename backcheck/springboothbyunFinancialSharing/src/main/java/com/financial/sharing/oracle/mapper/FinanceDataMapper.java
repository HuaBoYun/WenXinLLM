package com.financial.sharing.oracle.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 财务数据Mapper接口
 */
public interface FinanceDataMapper {
    
    /**
     * 查询数据列表
     */
    List<Map<String, Object>> selectDataList(@Param("params") Map<String, Object> params);
    
    /**
     * 根据ID查询数据详情
     */
    Map<String, Object> selectDataById(@Param("dataId") String dataId);
    
    /**
     * 插入数据
     */
    int insertData(@Param("data") Map<String, Object> data);
    
    /**
     * 更新数据
     */
    int updateData(@Param("data") Map<String, Object> data);
    
    /**
     * 删除数据
     */
    int deleteData(@Param("dataId") String dataId);
    
    /**
     * 查询数据统计
     */
    List<Map<String, Object>> selectDataStatistics(@Param("params") Map<String, Object> params);
    
    /**
     * 批量插入数据
     */
    int batchInsertData(@Param("dataList") List<Map<String, Object>> dataList);
    
    /**
     * 查询导出数据
     */
    List<Map<String, Object>> selectDataForExport(@Param("params") Map<String, Object> params);

    // ==================== 会计科目表相关方法 ====================

    /**
     * 查询会计科目列表
     */
    List<Map<String, Object>> selectKmList(@Param("params") Map<String, Object> params);

    /**
     * 统计会计科目总数
     */
    int countKmList(@Param("params") Map<String, Object> params);

    /**
     * 查询凭证库列表
     */
    List<Map<String, Object>> selectPzkList(@Param("params") Map<String, Object> params);

    /**
     * 统计凭证库总数
     */
    int countPzkList(@Param("params") Map<String, Object> params);

    // ==================== 辅助账管理相关方法 ====================

    /**
     * 查询辅助账信息树
     */
    List<Map<String, Object>> selectAuxiliaryInfoTree(@Param("params") Map<String, Object> params);

    /**
     * 统计辅助账信息树总数
     */
    int countAuxiliaryInfoTree(@Param("params") Map<String, Object> params);

    /**
     * 查询辅助账余额表
     */
    List<Map<String, Object>> selectAuxiliaryBalanceTable(@Param("params") Map<String, Object> params);

    /**
     * 统计辅助账余额表总数
     */
    int countAuxiliaryBalanceTable(@Param("params") Map<String, Object> params);

    /**
     * 查询辅助账信息表
     */
    List<Map<String, Object>> selectAuxiliaryInfoTable(@Param("params") Map<String, Object> params);

    /**
     * 统计辅助账信息表总数
     */
    int countAuxiliaryInfoTable(@Param("params") Map<String, Object> params);

    /**
     * 查询辅助账总表
     */
    List<Map<String, Object>> selectAuxiliaryTotalTable(@Param("params") Map<String, Object> params);

    /**
     * 统计辅助账总表总数
     */
    int countAuxiliaryTotalTable(@Param("params") Map<String, Object> params);

    // ==================== 账簿管理相关方法 ====================

    /**
     * 查询明细账
     */
    List<Map<String, Object>> selectAuxiliaryDetailTable(@Param("params") Map<String, Object> params);

    /**
     * 统计明细账总数
     */
    int countAuxiliaryDetailTable(@Param("params") Map<String, Object> params);

    /**
     * 查询日记账列表
     */
    List<Map<String, Object>> selectDiaryBookList(@Param("params") Map<String, Object> params);

    /**
     * 统计日记账总数
     */
    int countDiaryBookList(@Param("params") Map<String, Object> params);

    /**
     * 查询总分类账列表
     */
    List<Map<String, Object>> selectTotalAccountList(@Param("params") Map<String, Object> params);

    /**
     * 统计总分类账总数
     */
    int countTotalAccountList(@Param("params") Map<String, Object> params);

    /**
     * 查询余额表列表
     */
    List<Map<String, Object>> selectYebList(@Param("params") Map<String, Object> params);

    /**
     * 统计余额表总数
     */
    int countYebList(@Param("params") Map<String, Object> params);

    /**
     * 查询财务数据列表（凭证明细信息）
     */
    List<Map<String, Object>> selectFinanceDataList(@Param("params") Map<String, Object> params);

    /**
     * 统计财务数据总数（凭证明细信息）
     */
    int countFinanceDataList(@Param("params") Map<String, Object> params);
}