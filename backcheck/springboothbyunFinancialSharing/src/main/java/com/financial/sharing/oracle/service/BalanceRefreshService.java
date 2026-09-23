package com.financial.sharing.oracle.service;

import com.financial.sharing.dto.BalanceRefreshParam;
import com.financial.sharing.dto.BalanceQueryParam;
import com.financial.sharing.util.PageResult;
import com.hbfk.entity.TblStaffUtil;
import java.util.List;
import java.util.Map;

/**
 * 余额刷新服务接口
 *
 * @author system
 * @since 2024-12-07
 */
public interface BalanceRefreshService {

    /**
     * 刷新科目余额
     *
     * @param param 刷新参数
     * @param loginStaff 登录用户
     * @return 任务ID
     */
    String refreshBalance(BalanceRefreshParam param, TblStaffUtil loginStaff);

    /**
     * 批量刷新科目余额
     *
     * @param param 刷新参数
     * @param loginStaff 登录用户
     * @return 任务ID
     */
    String batchRefreshBalance(BalanceRefreshParam param, TblStaffUtil loginStaff);

    /**
     * 查询刷新进度
     *
     * @param taskId 任务ID
     * @return 进度信息
     */
    Map<String, Object> getRefreshProgress(String taskId);

    /**
     * 分页查询余额列表
     *
     * @param queryParam 查询参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> selectBalancePage(BalanceQueryParam queryParam);

    /**
     * 导出余额数据
     *
     * @param param 查询参数
     * @param loginStaff 登录用户
     * @return 导出结果
     */
    Map<String, Object> exportBalanceData(BalanceQueryParam param, TblStaffUtil loginStaff);

    /**
     * 余额数据校验
     *
     * @param param 查询参数
     * @param loginStaff 登录用户
     * @return 校验结果
     */
    Map<String, Object> validateBalanceData(BalanceQueryParam param, TblStaffUtil loginStaff);

    /**
     * 获取科目余额汇总信息
     *
     * @param param 查询参数
     * @return 汇总信息
     */
    Map<String, Object> getBalanceSummary(BalanceQueryParam param);

    /**
     * 计算科目余额
     *
     * @param bookId 账簿ID
     * @param accountCode 科目编码
     * @param period 期间
     * @param currencyCode 币种代码
     * @return 余额信息
     */
    Map<String, Object> calculateAccountBalance(Long bookId, String accountCode,
                                               String period, String currencyCode);

    /**
     * 获取余额刷新历史
     *
     * @param bookId 账簿ID
     * @param period 期间
     * @param limit 限制条数
     * @return 刷新历史
     */
    List<Map<String, Object>> getRefreshHistory(Long bookId, String period, Integer limit);

    /**
     * 取消刷新任务
     *
     * @param taskId 任务ID
     * @param loginStaff 登录用户
     * @return 取消结果
     */
    boolean cancelRefreshTask(String taskId, TblStaffUtil loginStaff);
}