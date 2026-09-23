package com.financial.sharing.service;

import com.financial.sharing.vo.param.TrialBalanceParam;
import com.financial.sharing.vo.result.TrialBalanceResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 试算平衡服务接口
 *
 * @author system
 * @since 2024-12-19
 */
public interface TrialBalanceService {

    /**
     * 生成试算平衡表
     *
     * @param param 试算平衡参数
     * @return 试算平衡结果
     */
    TrialBalanceResult generateTrialBalance(TrialBalanceParam param);

    /**
     * 试算平衡校验
     *
     * @param param 试算平衡参数
     * @return 校验结果
     */
    TrialBalanceResult checkBalance(TrialBalanceParam param);

    /**
     * 生成平衡报告
     *
     * @param param 试算平衡参数
     * @param response HTTP响应
     */
    void generateBalanceReport(TrialBalanceParam param, HttpServletResponse response);

    /**
     * 异步生成试算平衡表
     *
     * @param param 试算平衡参数
     * @return 任务ID
     */
    String asyncGenerateTrialBalance(TrialBalanceParam param);

    /**
     * 查询试算平衡进度
     *
     * @param taskId 任务ID
     * @return 进度信息
     */
    Object getTrialBalanceProgress(String taskId);

    /**
     * 获取历史试算平衡记录
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param period 会计期间
     * @return 历史记录列表
     */
    java.util.List<TrialBalanceResult> getTrialBalanceHistory(Long bookId, Long tenantId, String period);

    /**
     * 删除试算平衡记录
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean deleteTrialBalance(String taskId);

    /**
     * 获取可用的会计期间列表
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 期间列表
     */
    List<String> getAvailablePeriods(Long bookId, Long tenantId);

    /**
     * 导出试算平衡数据
     *
     * @param param 试算平衡参数
     * @param response HTTP响应
     */
    void exportTrialBalance(TrialBalanceParam param, HttpServletResponse response);
}