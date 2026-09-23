package com.financial.sharing.dataCollection.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.dataCollection.dto.CollectionLogQueryParam;
import com.financial.sharing.dataCollection.entity.TblCollectionLog;
import com.financial.sharing.util.MyJsonBean;

/**
 * 归集日志Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface CollectionLogService {

    /**
     * 分页查询归集日志
     *
     * @param param 查询参数
     * @param orgId 组织ID
     * @return 分页结果
     */
    MyJsonBean queryPage(CollectionLogQueryParam param, String orgId);

    /**
     * 根据ID查询归集日志
     *
     * @param logId 日志ID
     * @param orgId 组织ID
     * @return 归集日志
     */
    TblCollectionLog queryById(String logId, String orgId);

    /**
     * 保存归集日志
     *
     * @param log 归集日志
     * @return 操作结果
     */
    MyJsonBean saveCollectionLog(TblCollectionLog log);

    /**
     * 删除归集日志
     *
     * @param logId 日志ID
     * @param orgId 组织ID
     * @return 操作结果
     */
    MyJsonBean deleteCollectionLog(String logId, String orgId);

    /**
     * 批量删除归集日志
     *
     * @param logIds 日志ID列表
     * @param orgId 组织ID
     * @return 操作结果
     */
    MyJsonBean batchDeleteCollectionLog(String[] logIds, String orgId);

    /**
     * 清理历史日志
     *
     * @param days 保留天数
     * @param orgId 组织ID
     * @return 操作结果
     */
    MyJsonBean cleanHistoryLogs(Integer days, String orgId);
}

