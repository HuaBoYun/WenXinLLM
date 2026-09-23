package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.BusinessTransactionQueryParam;
import com.financial.sharing.vo.param.BusinessTransactionSaveParam;
import com.financial.sharing.vo.result.BusinessTransactionVO;
import com.hbfk.entity.TblStaffUtil;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 业务事项服务接口
 * 
 * @author system
 * @since 2024-12-19
 */
public interface BusinessTransactionService {

    /**
     * 分页查询业务事项
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<BusinessTransactionVO> getBusinessTransactionPage(BusinessTransactionQueryParam param);

    /**
     * 保存或更新业务事项
     * 
     * @param param 保存参数
     * @return 保存结果
     */
    BusinessTransactionVO saveOrUpdateBusinessTransaction(BusinessTransactionSaveParam param);

    /**
     * 根据ID查询业务事项详情
     * 
     * @param transactionId 事项ID
     * @return 业务事项详情
     */
    BusinessTransactionVO getBusinessTransactionById(Long transactionId);

    /**
     * 删除业务事项
     * 
     * @param transactionId 事项ID
     * @return 是否成功
     */
    boolean deleteBusinessTransaction(Long transactionId);

    /**
     * 批量删除业务事项
     * 
     * @param transactionIds 事项ID列表
     * @return 是否成功
     */
    boolean batchDeleteBusinessTransactions(List<Long> transactionIds);

    /**
     * 更新事项状态
     * 
     * @param transactionId 事项ID
     * @param transactionStatus 事项状态
     * @return 是否成功
     */
    boolean updateTransactionStatus(Long transactionId, Integer transactionStatus);

    /**
     * 批量更新事项状态
     * 
     * @param transactionIds 事项ID列表
     * @param transactionStatus 事项状态
     * @return 是否成功
     */
    boolean batchUpdateTransactionStatus(List<Long> transactionIds, Integer transactionStatus);

    /**
     * 检查事项编号是否存在
     * 
     * @param transactionNo 事项编号
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkTransactionNoExists(String transactionNo, Long bookId, Long tenantId, Long excludeId);

    /**
     * 根据事项类型查询业务事项列表
     * 
     * @param transactionType 事项类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 业务事项列表
     */
    List<BusinessTransactionVO> getBusinessTransactionsByType(String transactionType, Long bookId, Long tenantId);

    /**
     * 根据日期范围查询业务事项列表
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 业务事项列表
     */
    List<BusinessTransactionVO> getBusinessTransactionsByDateRange(LocalDate startDate, LocalDate endDate, Long bookId, Long tenantId);

    /**
     * 根据状态查询业务事项列表
     * 
     * @param transactionStatus 事项状态
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 业务事项列表
     */
    List<BusinessTransactionVO> getBusinessTransactionsByStatus(Integer transactionStatus, Long bookId, Long tenantId);

    /**
     * 获取事项类型列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 事项类型列表
     */
    List<String> getTransactionTypes(Long bookId, Long tenantId);

    /**
     * 获取来源系统列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 来源系统列表
     */
    List<String> getSourceSystems(Long bookId, Long tenantId);

    /**
     * 统计事项数量按状态分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<BusinessTransactionVO> countTransactionsByStatus(Long bookId, Long tenantId);

    /**
     * 统计事项数量按类型分组
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<BusinessTransactionVO> countTransactionsByType(Long bookId, Long tenantId);

    /**
     * 处理事项数据
     *
     * @param transactionIds 事项ID列表
     */
    void processTransactions(List<Long> transactionIds);

    /**
     * 批量处理事项数据
     *
     * @param data 处理参数
     */
    void batchProcessTransactions(Map<String, Object> data);

    /**
     * 重新处理失败的事项数据
     *
     * @param transactionIds 事项ID列表
     */
    void reprocessTransactions(List<Long> transactionIds);

    /**
     * 获取事项处理进度
     *
     * @param batchId 批次ID
     * @return 处理进度信息
     */
    Map<String, Object> getProcessProgress(String batchId);

    /**
     * 导入事项数据
     *
     * @param file 上传的文件
     * @return 导入结果
     */
    Map<String, Object> importTransactions(MultipartFile file);

    /**
     * 导出事项数据
     *
     * @param exportParams 导出参数
     * @return 文件URL
     */
    String exportTransactions(Map<String, Object> exportParams);

    /**
     * 根据业务事项自动生成凭证
     *
     * @param transactionIds 事项ID列表
     * @param templateId 凭证模板ID
     * @param loginStaff 登录用户信息
     * @return 凭证生成结果
     */
    Map<String, Object> generateVouchersFromTransactions(List<Long> transactionIds, Long templateId, TblStaffUtil loginStaff);

    /**
     * 预览凭证生成结果
     *
     * @param transactionIds 事项ID列表
     * @param templateId 凭证模板ID
     * @return 预览结果
     */
    Map<String, Object> previewVoucherGeneration(List<Long> transactionIds, Long templateId);
}
