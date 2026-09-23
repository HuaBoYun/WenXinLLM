package com.financial.sharing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.dto.AccountingPeriodQueryParam;
import com.financial.sharing.dto.AccountingPeriodSaveParam;
import com.financial.sharing.oracle.entity.TblAccountingPeriod;
import com.financial.sharing.oracle.entity.TblAccountingPeriodLog;
import com.hbfk.entity.TblStaffUtil;

import java.util.List;
import java.util.Map;

/**
 * 会计期间服务接口
 *
 * @author system
 * @since 2025-12-08
 */
public interface AccountingPeriodService extends IService<TblAccountingPeriod> {

    /**
     * 分页查询会计期间
     *
     * @param param 查询参数
     * @return 会计期间分页数据
     */
    IPage<TblAccountingPeriod> getPeriodPage(AccountingPeriodQueryParam param);

    /**
     * 获取当前会计期间
     *
     * @param bookId   账簿ID
     * @param tenantId 租户ID
     * @return 当前会计期间
     */
    TblAccountingPeriod getCurrentPeriod(Long bookId, Long tenantId);

    /**
     * 保存或更新会计期间
     *
     * @param param    保存参数
     * @param userInfo 用户信息
     * @return 操作结果
     */
    TblAccountingPeriod saveOrUpdatePeriod(AccountingPeriodSaveParam param, TblStaffUtil userInfo);

    /**
     * 开启会计期间
     *
     * @param periodId 期间ID
     * @param userInfo 用户信息
     * @return 操作结果
     */
    boolean openPeriod(Long periodId, TblStaffUtil userInfo);

    /**
     * 关闭会计期间
     *
     * @param periodId 期间ID
     * @param userInfo 用户信息
     * @return 操作结果
     */
    boolean closePeriod(Long periodId, TblStaffUtil userInfo);

    /**
     * 锁定会计期间
     *
     * @param periodId 期间ID
     * @param userInfo 用户信息
     * @return 操作结果
     */
    boolean lockPeriod(Long periodId, TblStaffUtil userInfo);

    /**
     * 设置当前会计期间
     *
     * @param periodId 期间ID
     * @param userInfo 用户信息
     * @return 操作结果
     */
    boolean setCurrentPeriod(Long periodId, TblStaffUtil userInfo);

    /**
     * 批量创建年度期间
     *
     * @param yearNo   年度
     * @param bookId   账簿ID
     * @param tenantId 租户ID
     * @param userInfo 用户信息
     * @return 创建的期间列表
     */
    List<TblAccountingPeriod> batchCreateYearPeriods(Integer yearNo, Long bookId, Long tenantId, TblStaffUtil userInfo);

    /**
     * 获取期间状态列表
     *
     * @param bookId   账簿ID
     * @param tenantId 租户ID
     * @return 状态列表
     */
    List<String> getPeriodStatusList(Long bookId, Long tenantId);

    /**
     * 获取期间统计信息
     *
     * @param bookId   账簿ID
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> getPeriodStatistics(Long bookId, Long tenantId);

    /**
     * 根据年度和月份查询期间
     *
     * @param yearNo   年度
     * @param monthNo  月份
     * @param bookId   账簿ID
     * @param tenantId 租户ID
     * @return 会计期间
     */
    TblAccountingPeriod getPeriodByYearMonth(Integer yearNo, Integer monthNo, Long bookId, Long tenantId);

    /**
     * 获取期间变更日志
     *
     * @param periodId 期间ID
     * @return 日志列表
     */
    List<TblAccountingPeriodLog> getPeriodLogs(Long periodId);

    /**
     * 记录期间操作日志
     *
     * @param periodId      期间ID
     * @param oldStatus     原状态
     * @param newStatus     新状态
     * @param operationType 操作类型
     * @param reason        操作原因
     * @param userInfo      用户信息
     * @param bookId        账簿ID
     * @param tenantId      租户ID
     */
    void recordPeriodLog(Long periodId, String oldStatus, String newStatus, String operationType,
                        String reason, TblStaffUtil userInfo, Long bookId, Long tenantId);
}