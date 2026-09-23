package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.dto.export.ExportAccountFreezeDTO;
import com.global.treasurer.entity.TblGtAccountFreeze;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 全球司库-账户冻结Service接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
public interface TblGtAccountFreezeService extends IService<TblGtAccountFreeze> {

    /**
     * 分页查询冻结记录列表
     */
    IPage<TblGtAccountFreeze> getPageList(Page<TblGtAccountFreeze> page,
                                          Long recordId,
                                          String accountNumber,
                                          String freezeType,
                                          String freezeStatus);

    /**
     * 新增冻结记录
     */
    boolean saveFreezeRecord(TblGtAccountFreeze entity);

    /**
     * 解冻操作
     */
    boolean unfreezeRecord(Long recordId, String unfreezeType, 
                          BigDecimal unfreezeAmount, String unfreezeReason);

    /**
     * 删除冻结记录
     */
    boolean deleteFreezeRecord(Long recordId);

    /**
     * 批量解冻
     */
    int batchUnfreeze(List<Long> recordIds);

    /**
     * 导出冻结记录列表
     */
    List<ExportAccountFreezeDTO> exportList(Long recordId, String accountNumber,
                                            String freezeType, String freezeStatus);

    /**
     * 获取统计数据
     */
    Map<String, Object> getStatistics();
}

