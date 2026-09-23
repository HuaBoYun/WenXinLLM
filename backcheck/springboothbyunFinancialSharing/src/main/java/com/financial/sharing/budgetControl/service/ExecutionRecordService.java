package com.financial.sharing.budgetControl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.budgetControl.dto.ExecutionRecordQueryParam;
import com.financial.sharing.budgetControl.entity.TblExecutionRecord;
import com.financial.sharing.util.MyJsonBean;

/**
 * 执行记录Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface ExecutionRecordService extends IService<TblExecutionRecord> {

    /**
     * 分页查询执行记录
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean queryPage(ExecutionRecordQueryParam param);

    /**
     * 根据ID查询执行记录
     *
     * @param recordId 记录ID
     * @return 执行记录
     */
    MyJsonBean queryById(String recordId);

    /**
     * 查询执行记录统计
     *
     * @param param 查询参数
     * @return 统计结果
     */
    MyJsonBean queryStatistics(ExecutionRecordQueryParam param);

    /**
     * 导出执行记录
     *
     * @param param 查询参数
     * @return 导出结果
     */
    MyJsonBean exportRecords(ExecutionRecordQueryParam param);
}

