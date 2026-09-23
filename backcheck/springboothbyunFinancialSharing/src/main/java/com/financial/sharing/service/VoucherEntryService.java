package com.financial.sharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.oracle.entity.VoucherEntryEntity;

import java.util.List;
import java.util.Map;
import com.hbfk.entity.TblStaffUtil;

/**
 * 凭证分录服务接口
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
public interface VoucherEntryService extends IService<VoucherEntryEntity> {

    /**
     * 根据凭证ID查询分录列表
     *
     * @param voucherId 凭证ID
     * @return 分录列表
     */
    List<VoucherEntryEntity> getEntriesByVoucherId(Long voucherId);

    /**
     * 保存凭证分录
     *
     * @param voucherId 凭证ID
     * @param entries 分录列表
     * @param currentUser 当前用户
     * @return 保存结果
     */
    boolean saveVoucherEntries(Long voucherId, List<Map<String, Object>> entries, TblStaffUtil currentUser);

    /**
     * 更新凭证分录
     *
     * @param voucherId 凭证ID
     * @param entries 分录列表
     * @param currentUser 当前用户
     * @return 更新结果
     */
    boolean updateVoucherEntries(Long voucherId, List<Map<String, Object>> entries, TblStaffUtil currentUser);

    /**
     * 根据凭证ID删除分录
     *
     * @param voucherId 凭证ID
     * @return 删除结果
     */
    boolean deleteByVoucherId(Long voucherId);

    /**
     * 批量删除分录
     *
     * @param entryIds 分录ID列表
     * @return 删除结果
     */
    boolean batchDeleteEntries(List<Long> entryIds);

    /**
     * 校验分录数据
     *
     * @param entries 分录列表
     * @return 校验结果
     */
    boolean validateEntries(List<Map<String, Object>> entries);

    /**
     * 计算分录金额
     *
     * @param entries 分录列表
     * @return 金额统计
     */
    Map<String, Object> calculateEntryAmount(List<Map<String, Object>> entries);

    /**
     * 获取分录详情
     *
     * @param entryId 分录ID
     * @return 分录详情
     */
    VoucherEntryEntity getEntryDetail(Long entryId);

    /**
     * 复制分录
     *
     * @param sourceVoucherId 源凭证ID
     * @param targetVoucherId 目标凭证ID
     * @param currentUser 当前用户
     * @return 复制结果
     */
    boolean copyEntries(Long sourceVoucherId, Long targetVoucherId, TblStaffUtil currentUser);
}