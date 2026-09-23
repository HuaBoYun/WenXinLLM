package com.financial.sharing.service;

import com.github.pagehelper.PageInfo;
import com.financial.sharing.oracle.entity.AccountingVoucherEntity;
import com.financial.sharing.vo.param.AccountingVoucherQueryParam;
import com.financial.sharing.vo.param.VoucherSaveParam;
import com.financial.sharing.vo.result.AccountingVoucherVO;
import com.financial.sharing.vo.result.VoucherDetailVO;
import com.hbfk.entity.TblStaffUtil;

import java.util.List;
import java.util.Map;

/**
 * 会计凭证服务接口
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
public interface AccountingVoucherService {

    /**
     * 分页查询会计凭证
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<AccountingVoucherVO> getVoucherPage(AccountingVoucherQueryParam param);

    /**
     * 根据ID查询凭证详情
     *
     * @param voucherId 凭证ID
     * @return 凭证详情
     */
    VoucherDetailVO getVoucherDetail(Long voucherId);

    /**
     * 保存凭证
     *
     * @param param 保存参数
     * @param currentUser 当前用户
     * @return 凭证信息
     */
    AccountingVoucherEntity saveVoucher(VoucherSaveParam param, TblStaffUtil currentUser);

    /**
     * 更新凭证
     *
     * @param voucherId 凭证ID
     * @param param 更新参数
     * @param currentUser 当前用户
     * @return 凭证信息
     */
    AccountingVoucherEntity updateVoucher(Long voucherId, VoucherSaveParam param, TblStaffUtil currentUser);

    /**
     * 删除凭证
     *
     * @param voucherId 凭证ID
     * @param currentUser 当前用户
     * @return 删除结果
     */
    boolean deleteVoucher(Long voucherId, TblStaffUtil currentUser);

    /**
     * 批量删除凭证
     *
     * @param voucherIds 凭证ID列表
     * @param currentUser 当前用户
     * @return 删除结果
     */
    int batchDeleteVouchers(List<Long> voucherIds, TblStaffUtil currentUser);

    /**
     * 提交凭证
     *
     * @param voucherId 凭证ID
     * @param currentUser 当前用户
     * @return 操作结果
     */
    boolean submitVoucher(Long voucherId, TblStaffUtil currentUser);

    /**
     * 审核凭证
     *
     * @param voucherId 凭证ID
     * @param currentUser 当前用户
     * @return 操作结果
     */
    boolean reviewVoucher(Long voucherId, TblStaffUtil currentUser);

    /**
     * 审批凭证
     *
     * @param voucherId 凭证ID
     * @param approvalRemark 审批备注
     * @param currentUser 当前用户
     * @return 操作结果
     */
    boolean approveVoucher(Long voucherId, String approvalRemark, TblStaffUtil currentUser);

    /**
     * 驳回凭证
     *
     * @param voucherId 凭证ID
     * @param rejectReason 驳回原因
     * @param currentUser 当前用户
     * @return 操作结果
     */
    boolean rejectVoucher(Long voucherId, String rejectReason, TblStaffUtil currentUser);

    /**
     * 过账凭证
     *
     * @param voucherIds 凭证ID列表
     * @param currentUser 当前用户
     * @return 操作结果
     */
    Map<String, Object> postVouchers(List<Long> voucherIds, TblStaffUtil currentUser);

    /**
     * 反过账凭证
     *
     * @param voucherIds 凭证ID列表
     * @param reason 反过账原因
     * @param currentUser 当前用户
     * @return 操作结果
     */
    Map<String, Object> unpostVouchers(List<Long> voucherIds, String reason, TblStaffUtil currentUser);

    /**
     * 批量审批凭证
     *
     * @param voucherIds 凭证ID列表
     * @param approvalRemark 审批备注
     * @param currentUser 当前用户
     * @return 操作结果
     */
    Map<String, Object> batchApproveVouchers(List<Long> voucherIds, String approvalRemark, TblStaffUtil currentUser);

    /**
     * 更新凭证状态
     *
     * @param voucherId 凭证ID
     * @param status 状态
     * @param currentUser 当前用户
     * @return 操作结果
     */
    boolean updateVoucherStatus(Long voucherId, Integer status, TblStaffUtil currentUser);

    /**
     * 批量更新凭证状态
     *
     * @param voucherIds 凭证ID列表
     * @param status 状态
     * @param currentUser 当前用户
     * @return 操作结果
     */
    int batchUpdateVoucherStatus(List<Long> voucherIds, Integer status, TblStaffUtil currentUser);

    /**
     * 获取凭证统计数据
     *
     * @param param 查询参数
     * @param currentUser 当前用户
     * @return 统计数据
     */
    Map<String, Object> getVoucherStatistics(AccountingVoucherQueryParam param, TblStaffUtil currentUser);

    /**
     * 获取审批历史
     *
     * @param voucherId 凭证ID
     * @return 审批历史
     */
    List<Map<String, Object>> getApprovalHistory(Long voucherId);

    /**
     * 生成凭证编号
     *
     * @param voucherTypeId 凭证类型ID
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 凭证编号
     */
    String generateVoucherNo(Long voucherTypeId, String accountingPeriod, Long bookId, Long tenantId);

    /**
     * 校验借贷平衡
     *
     * @param entries 分录列表
     * @return 校验结果
     */
    boolean validateBalance(List<Map<String, Object>> entries);

    /**
     * 检查凭证是否可以操作
     *
     * @param voucherId 凭证ID
     * @param operation 操作类型
     * @param currentUser 当前用户
     * @return 检查结果
     */
    boolean canOperateVoucher(Long voucherId, String operation, TblStaffUtil currentUser);

    /**
     * 导入凭证数据
     *
     * @param inputStream 文件输入流
     * @param fileName 文件名
     * @param tenantId 租户ID
     * @return 导入结果 {successCount, failCount, errorList}
     */
    Map<String, Object> importVouchers(java.io.InputStream inputStream, String fileName, Long tenantId);

    /**
     * 分页查询反过账记录
     *
     * @param param 查询参数
     * @param currentUser 当前用户
     * @return 分页结果
     */
    Map<String, Object> getUnpostRecordPage(Map<String, Object> param, TblStaffUtil currentUser);
}