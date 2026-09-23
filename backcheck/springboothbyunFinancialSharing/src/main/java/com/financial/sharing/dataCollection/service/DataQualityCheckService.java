package com.financial.sharing.dataCollection.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.financial.sharing.dataCollection.dto.DataQualityCheckQueryParam;
import com.financial.sharing.dataCollection.dto.ExecuteQualityCheckParam;
import com.financial.sharing.dataCollection.entity.TblDataQualityCheck;
import com.financial.sharing.dataCollection.entity.TblDataQualityCheckDetail;

import java.util.List;

/**
 * 数据质量检查服务接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface DataQualityCheckService {

    /**
     * 分页查询质量检查记录
     *
     * @param param 查询参数
     * @param orgId 组织ID
     * @return 分页结果
     */
    IPage<TblDataQualityCheck> queryPage(DataQualityCheckQueryParam param, Long orgId);

    /**
     * 根据ID查询质量检查记录
     *
     * @param checkId 检查ID
     * @param orgId 组织ID
     * @return 质量检查记录
     */
    TblDataQualityCheck queryById(Long checkId, Long orgId);

    /**
     * 查询检查明细
     *
     * @param checkId 检查ID
     * @param orgId 组织ID
     * @return 明细列表
     */
    List<TblDataQualityCheckDetail> queryDetails(Long checkId, Long orgId);

    /**
     * 执行质量检查
     *
     * @param param 检查参数
     * @param orgId 组织ID
     * @param userId 用户ID
     * @return 检查ID
     */
    Long executeQualityCheck(ExecuteQualityCheckParam param, Long orgId, String userId);

    /**
     * 删除质量检查记录
     *
     * @param checkId 检查ID
     * @param orgId 组织ID
     * @return 是否成功
     */
    boolean deleteCheck(Long checkId, Long orgId);

    /**
     * 批量删除质量检查记录
     *
     * @param checkIds 检查ID列表
     * @param orgId 组织ID
     * @return 删除数量
     */
    int batchDeleteCheck(List<Long> checkIds, Long orgId);
}

