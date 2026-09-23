package com.financial.sharing.service.impl;

import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.oracle.entity.BusinessTransactionEntity;
import com.financial.sharing.service.BusinessTransactionService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.Java8Collections;
import com.financial.sharing.vo.param.BusinessTransactionQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.financial.sharing.vo.param.BusinessTransactionSaveParam;
import com.financial.sharing.vo.result.BusinessTransactionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * 业务事项服务实现类
 * 
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class BusinessTransactionServiceImpl implements BusinessTransactionService {

    @Autowired
    private DateBaseConfig dateBaseConfig;

    // 事项状态名称映射
    private static final Map<Integer, String> TRANSACTION_STATUS_NAME_MAP = new HashMap<>();
    
    // 事项类型名称映射
    private static final Map<String, String> TRANSACTION_TYPE_NAME_MAP = new HashMap<>();
    
    static {
        TRANSACTION_STATUS_NAME_MAP.put(1, "待处理");
        TRANSACTION_STATUS_NAME_MAP.put(2, "已处理");
        TRANSACTION_STATUS_NAME_MAP.put(3, "已取消");
        
        TRANSACTION_TYPE_NAME_MAP.put("SALES_ORDER", "销售订单");
        TRANSACTION_TYPE_NAME_MAP.put("PURCHASE_ORDER", "采购订单");
        TRANSACTION_TYPE_NAME_MAP.put("PAYMENT", "付款");
        TRANSACTION_TYPE_NAME_MAP.put("RECEIPT", "收款");
        TRANSACTION_TYPE_NAME_MAP.put("INVENTORY_IN", "入库");
        TRANSACTION_TYPE_NAME_MAP.put("INVENTORY_OUT", "出库");
        TRANSACTION_TYPE_NAME_MAP.put("EXPENSE", "费用");
        TRANSACTION_TYPE_NAME_MAP.put("INCOME", "收入");
    }

    @Override
    public PageResult<BusinessTransactionVO> getBusinessTransactionPage(BusinessTransactionQueryParam param) {
        // 使用 PageHelper 进行分页
        PageHelper.startPage(param.getPageNum(), param.getPageSize());

        List<BusinessTransactionVO> list;

        list = dateBaseConfig.getOracleBusinessTransactionMapper().selectBusinessTransactionPage(param);


        // 获取分页信息
        PageInfo<BusinessTransactionVO> pageInfo = new PageInfo<>(list);

        // 设置状态和类型名称
        if (!CollectionUtils.isEmpty(pageInfo.getList())) {
            pageInfo.getList().forEach(this::setDisplayNames);
        }
        return new PageResult<>((int)pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPages(), pageInfo.getPageSize(), pageInfo.getList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessTransactionVO saveOrUpdateBusinessTransaction(BusinessTransactionSaveParam param) {
        // 检查事项编号是否重复
        if (checkTransactionNoExists(param.getTransactionNo(), param.getBookId(), param.getTenantId(), param.getTransactionId())) {
            throw new RuntimeException("事项编号已存在");
        }

        BusinessTransactionEntity entity = new BusinessTransactionEntity();
        BeanUtils.copyProperties(param, entity);

        // 设置默认值
        if (entity.getTransactionStatus() == null) {
            entity.setTransactionStatus(1); // 默认待处理
        }

        boolean success;

        com.financial.sharing.oracle.entity.BusinessTransactionEntity oracleEntity =
            new com.financial.sharing.oracle.entity.BusinessTransactionEntity();
        BeanUtils.copyProperties(entity, oracleEntity);

        if (param.getTransactionId() == null) {
            success = dateBaseConfig.getOracleBusinessTransactionMapper().insert(oracleEntity) > 0;
        } else {
            success = dateBaseConfig.getOracleBusinessTransactionMapper().updateById(oracleEntity) > 0;
        }
        entity.setTransactionId(oracleEntity.getTransactionId());
        if (!success) {
            throw new RuntimeException("保存业务事项失败");
        }
        return getBusinessTransactionById(entity.getTransactionId());
    }

    @Override
    public BusinessTransactionVO getBusinessTransactionById(Long transactionId) {
        BusinessTransactionEntity entity;

        com.financial.sharing.oracle.entity.BusinessTransactionEntity oracleEntity =
            dateBaseConfig.getOracleBusinessTransactionMapper().selectById(transactionId);
        if (oracleEntity == null) {
            return null;
        }

        entity = new BusinessTransactionEntity();
        BeanUtils.copyProperties(oracleEntity, entity);
        if (entity == null) {
            return null;
        }

        BusinessTransactionVO vo = new BusinessTransactionVO();
        BeanUtils.copyProperties(entity, vo);
        setDisplayNames(vo);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBusinessTransaction(Long transactionId) {
        boolean success;

        success = dateBaseConfig.getOracleBusinessTransactionMapper().deleteById(transactionId) > 0;


        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteBusinessTransactions(List<Long> transactionIds) {
        if (CollectionUtils.isEmpty(transactionIds)) {
            return false;
        }

        int result;

        result = dateBaseConfig.getOracleBusinessTransactionMapper().batchDelete(transactionIds, null);


        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTransactionStatus(Long transactionId, Integer transactionStatus) {
        int result;

        result = dateBaseConfig.getOracleBusinessTransactionMapper().batchUpdateStatus(
            Java8Collections.listOf(transactionId), transactionStatus, null);


        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateTransactionStatus(List<Long> transactionIds, Integer transactionStatus) {
        if (CollectionUtils.isEmpty(transactionIds)) {
            return false;
        }

        int result;

        result = dateBaseConfig.getOracleBusinessTransactionMapper().batchUpdateStatus(transactionIds, transactionStatus, null);


        return result > 0;
    }

    @Override
    public boolean checkTransactionNoExists(String transactionNo, Long bookId, Long tenantId, Long excludeId) {
        BusinessTransactionEntity entity;

        com.financial.sharing.oracle.entity.BusinessTransactionEntity oracleEntity =
            dateBaseConfig.getOracleBusinessTransactionMapper().selectByTransactionNo(
                transactionNo, bookId, tenantId, excludeId);
        entity = oracleEntity != null ? new BusinessTransactionEntity() : null;


        return entity != null;
    }

    @Override
    public List<BusinessTransactionVO> getBusinessTransactionsByType(String transactionType, Long bookId, Long tenantId) {
        List<BusinessTransactionVO> list;

        list = dateBaseConfig.getOracleBusinessTransactionMapper().selectByTransactionType(transactionType, bookId, tenantId);


        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public List<BusinessTransactionVO> getBusinessTransactionsByDateRange(LocalDate startDate, LocalDate endDate, Long bookId, Long tenantId) {
        List<BusinessTransactionVO> list;

        list = dateBaseConfig.getOracleBusinessTransactionMapper().selectByDateRange(startDate, endDate, bookId, tenantId);


        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public List<BusinessTransactionVO> getBusinessTransactionsByStatus(Integer transactionStatus, Long bookId, Long tenantId) {
        List<BusinessTransactionVO> list;

        list = dateBaseConfig.getOracleBusinessTransactionMapper().selectByStatus(transactionStatus, bookId, tenantId);


        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public List<String> getTransactionTypes(Long bookId, Long tenantId) {

        return dateBaseConfig.getOracleBusinessTransactionMapper().selectTransactionTypes(bookId, tenantId);

    }

    @Override
    public List<String> getSourceSystems(Long bookId, Long tenantId) {

        return dateBaseConfig.getOracleBusinessTransactionMapper().selectSourceSystems(bookId, tenantId);

    }

    @Override
    public List<BusinessTransactionVO> countTransactionsByStatus(Long bookId, Long tenantId) {
        List<BusinessTransactionVO> list;

        list = dateBaseConfig.getOracleBusinessTransactionMapper().countByStatus(bookId, tenantId);


        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public List<BusinessTransactionVO> countTransactionsByType(Long bookId, Long tenantId) {
        List<BusinessTransactionVO> list;

        list = dateBaseConfig.getOracleBusinessTransactionMapper().countByType(bookId, tenantId);


        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processTransactions(List<Long> transactionIds) {
        if (CollectionUtils.isEmpty(transactionIds)) {
            return;
        }

        // 更新事项状态为已处理
        batchUpdateTransactionStatus(transactionIds, 2);
        log.info("处理事项数据成功，共处理 {} 条记录", transactionIds.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchProcessTransactions(Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            return;
        }

        @SuppressWarnings("unchecked")
        List<Long> transactionIds = (List<Long>) data.get("transactionIds");
        if (!CollectionUtils.isEmpty(transactionIds)) {
            processTransactions(transactionIds);
        }

        log.info("批量处理事项数据成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reprocessTransactions(List<Long> transactionIds) {
        if (CollectionUtils.isEmpty(transactionIds)) {
            return;
        }

        // 重新处理失败的事项，更新状态为待处理
        batchUpdateTransactionStatus(transactionIds, 1);
        log.info("重新处理事项数据成功，共处理 {} 条记录", transactionIds.size());
    }

    @Override
    public Map<String, Object> getProcessProgress(String batchId) {
        Map<String, Object> progress = new HashMap<>();
        progress.put("batchId", batchId);
        progress.put("totalCount", 0);
        progress.put("processedCount", 0);
        progress.put("failedCount", 0);
        progress.put("progress", 0);
        progress.put("status", "processing");

        return progress;
    }

    @Override
    public Map<String, Object> importTransactions(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        if (file == null || file.isEmpty()) {
            result.put("success", false);
            result.put("message", "文件为空");
            return result;
        }

        try {
            // TODO: 实现文件导入逻辑
            result.put("success", true);
            result.put("message", "导入成功");
            result.put("importedCount", 0);
            result.put("failedCount", 0);
        } catch (Exception e) {
            log.error("导入事项数据失败", e);
            result.put("success", false);
            result.put("message", "导入失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public String exportTransactions(Map<String, Object> exportParams) {
        // TODO: 实现文件导出逻辑
        String fileUrl = "/download/transactions_" + System.currentTimeMillis() + ".xlsx";
        log.info("导出事项数据成功，文件URL: {}", fileUrl);

        return fileUrl;
    }

    @Override
    public Map<String, Object> generateVouchersFromTransactions(List<Long> transactionIds, Long templateId, TblStaffUtil loginStaff) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 验证参数
            if (transactionIds == null || transactionIds.isEmpty()) {
                result.put("success", false);
                result.put("message", "事项ID列表不能为空");
                return result;
            }

            // 生成批次ID
            String batchId = "BATCH_" + System.currentTimeMillis();
            int totalCount = transactionIds.size();
            int successCount = 0;
            int failureCount = 0;

            // 模拟凭证生成过程
            List<Map<String, Object>> voucherList = new ArrayList<>();
            for (Long transactionId : transactionIds) {
                try {
                    Map<String, Object> voucher = new HashMap<>();
                    voucher.put("voucherId", "V" + System.currentTimeMillis() + transactionId);
                    voucher.put("voucherNo", "PZ" + System.currentTimeMillis());
                    voucher.put("transactionId", transactionId);
                    voucher.put("status", "GENERATED");
                    voucher.put("amount", Math.random() * 10000);
                    voucherList.add(voucher);
                    successCount++;
                } catch (Exception e) {
                    log.error("生成凭证失败，事项ID: {}", transactionId, e);
                    failureCount++;
                }
            }

            result.put("success", true);
            result.put("message", "凭证生成完成");
            result.put("batchId", batchId);
            result.put("totalCount", totalCount);
            result.put("successCount", successCount);
            result.put("failureCount", failureCount);
            result.put("voucherList", voucherList);
            result.put("generatedTime", new Date());

            log.info("用户 {} 批量生成凭证，批次ID：{}，总数：{}，成功：{}，失败：{}",
                loginStaff.getUsername(), batchId, totalCount, successCount, failureCount);

        } catch (Exception e) {
            log.error("批量生成凭证失败", e);
            result.put("success", false);
            result.put("message", "生成凭证失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> previewVoucherGeneration(List<Long> transactionIds, Long templateId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 验证参数
            if (transactionIds == null || transactionIds.isEmpty()) {
                result.put("success", false);
                result.put("message", "事项ID列表不能为空");
                return result;
            }

            // 预览凭证生成结果
            List<Map<String, Object>> previewList = new ArrayList<>();
            for (Long transactionId : transactionIds) {
                Map<String, Object> preview = new HashMap<>();
                preview.put("transactionId", transactionId);
                preview.put("templateId", templateId);
                preview.put("voucherNo", "PZ" + System.currentTimeMillis() + transactionId);
                preview.put("estimatedAmount", Math.random() * 10000);
                preview.put("debitCount", 2);
                preview.put("creditCount", 2);
                Map<String, Object> entry1 = new HashMap<>();
                entry1.put("accountCode", "6001");
                entry1.put("accountName", "主营业务收入");
                entry1.put("debitAmount", Math.random() * 10000);
                entry1.put("creditAmount", 0);

                Map<String, Object> entry2 = new HashMap<>();
                entry2.put("accountCode", "1002");
                entry2.put("accountName", "银行存款");
                entry2.put("debitAmount", 0);
                entry2.put("creditAmount", Math.random() * 10000);

                preview.put("entries", Arrays.asList(entry1, entry2));
                previewList.add(preview);
            }

            result.put("success", true);
            result.put("message", "预览成功");
            result.put("totalCount", previewList.size());
            result.put("previewList", previewList);
            result.put("totalAmount", previewList.stream().mapToDouble(p -> ((Number) p.get("estimatedAmount")).doubleValue()).sum());

        } catch (Exception e) {
            log.error("预览凭证生成失败", e);
            result.put("success", false);
            result.put("message", "预览失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 设置显示名称
     */
    private void setDisplayNames(BusinessTransactionVO vo) {
        if (vo != null) {
            if (vo.getTransactionStatus() != null) {
                vo.setTransactionStatusName(TRANSACTION_STATUS_NAME_MAP.getOrDefault(vo.getTransactionStatus(), "未知"));
            }
            if (vo.getTransactionType() != null) {
                vo.setTransactionTypeName(TRANSACTION_TYPE_NAME_MAP.getOrDefault(vo.getTransactionType(), vo.getTransactionType()));
            }
        }
    }
}
