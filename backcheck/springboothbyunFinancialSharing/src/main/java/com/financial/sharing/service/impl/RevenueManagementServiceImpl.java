package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.entity.RevenueContractEntity;
import com.financial.sharing.oracle.mapper.DeferredRevenueMapper;
import com.financial.sharing.oracle.mapper.RevenueContractMapper;
import com.financial.sharing.oracle.mapper.RevenueRecognitionMapper;
import com.financial.sharing.service.RevenueManagementService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 收入管理服务实现类
 * 提供收入确认、分配、调整、分析等功能
 * 支持达梦数据库和MySQL数据库
 *
 * @author system
 * @date 2024-12-19
 */
@Slf4j
@Service
public class RevenueManagementServiceImpl implements RevenueManagementService {

    @Autowired
    @Qualifier("oracleRevenueRecognitionMapper")
    private RevenueRecognitionMapper revenueRecognitionMapper;

    @Autowired
    @Qualifier("oracleRevenueContractMapper")
    private RevenueContractMapper revenueContractMapper;

    @Autowired
    @Qualifier("oracleDeferredRevenueMapper")
    private DeferredRevenueMapper deferredRevenueMapper;

    // ==================== 收入确认管理 ====================

    @Override
    public PageResult<Map<String, Object>> getRevenueRecognitionList(PageableParam param) {
        try {
            log.info("开始查询收入确认列表，参数: {}", param);

            // 检查Mapper是否注入
            if (revenueRecognitionMapper == null) {
                log.error("revenueRecognitionMapper未正确注入");
                throw new RuntimeException("系统配置错误：Mapper未正确注入");
            }

            // 提取搜索参数
            Map<String, Object> searchParams = (Map<String, Object>) param.getParam();
            if (searchParams == null) {
                searchParams = new HashMap<>();
            }

            log.info("查询参数: {}", searchParams);

            // 查询所有数据
            List<Map<String, Object>> allRecords = revenueRecognitionMapper.selectRevenueRecognitionList(searchParams);

            // 检查查询结果是否为null
            if (allRecords == null) {
                log.warn("查询结果为null，返回空列表");
                allRecords = new ArrayList<>();
            }

            log.info("查询成功，总记录数: {}", allRecords.size());

            // 转换字段名为小写驼峰命名（兼容达梦数据库返回大写字段名的情况）
            for (Map<String, Object> item : allRecords) {
                convertRecognitionFieldNamesToCamelCase(item);
            }

            // 手动分页
            int pageNum = param.getPageNumber() != null ? param.getPageNumber() : 1;
            int pageSize = param.getPageSize() != null ? param.getPageSize() : 10;
            int total = allRecords.size();
            int totalPages = (total + pageSize - 1) / pageSize;

            // 计算分页数据
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);

            List<Map<String, Object>> pagedRecords;
            if (fromIndex >= total) {
                pagedRecords = new ArrayList<>();
            } else {
                pagedRecords = allRecords.subList(fromIndex, toIndex);
            }

            // 构建返回结果
            PageResult<Map<String, Object>> result = new PageResult<>();
            result.setTlist(pagedRecords);
            result.setTotalRecord(total);
            result.setCurrentPage(pageNum);
            result.setPageSize(pageSize);
            result.setTotalPage(totalPages);

            return result;
        } catch (Exception e) {
            log.error("查询收入确认列表失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executeRevenueRecognition(Map<String, Object> param) {
        try {
            log.info("开始执行收入确认，参数: {}", param);

            // 检查Mapper是否注入
            if (revenueRecognitionMapper == null) {
                log.error("revenueRecognitionMapper未正确注入");
                throw new RuntimeException("系统配置错误：Mapper未正确注入");
            }

            // 创建收入确认实体
            com.financial.sharing.oracle.entity.RevenueRecognitionEntity entity =
                new com.financial.sharing.oracle.entity.RevenueRecognitionEntity();

            // 设置合同ID
            if (param.get("contractId") != null && !"".equals(param.get("contractId").toString())) {
                entity.setContractId(Long.parseLong(param.get("contractId").toString()));
            }

            // 设置确认类型（默认为2-手动确认）
            if (param.get("recognitionType") != null && !"".equals(param.get("recognitionType").toString())) {
                entity.setRecognitionType(Integer.parseInt(param.get("recognitionType").toString()));
            } else {
                entity.setRecognitionType(2); // 手动确认
            }

            // 设置确认期间
            String recognitionPeriod = null;
            if (param.get("recognitionPeriod") != null && !"".equals(param.get("recognitionPeriod").toString())) {
                recognitionPeriod = param.get("recognitionPeriod").toString();
            } else {
                // 默认使用当前年月
                recognitionPeriod = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            }
            entity.setRecognitionPeriod(recognitionPeriod);

            // 设置确认日期
            entity.setRecognitionDate(LocalDate.now());

            // 设置确认金额
            BigDecimal recognitionAmount = BigDecimal.ZERO;
            if (param.get("recognitionAmount") != null) {
                Object amount = param.get("recognitionAmount");
                if (amount instanceof BigDecimal) {
                    recognitionAmount = (BigDecimal) amount;
                } else if (amount instanceof Number) {
                    recognitionAmount = new BigDecimal(amount.toString());
                } else if (!"".equals(amount.toString())) {
                    recognitionAmount = new BigDecimal(amount.toString());
                }
            }
            entity.setRecognitionAmount(recognitionAmount);

            // 设置累计确认金额（初始等于确认金额）
            entity.setCumulativeAmount(recognitionAmount);

            // 设置确认比例
            if (param.get("recognitionRate") != null) {
                Object rate = param.get("recognitionRate");
                if (rate instanceof BigDecimal) {
                    entity.setRecognitionRate((BigDecimal) rate);
                } else if (rate instanceof Number) {
                    entity.setRecognitionRate(new BigDecimal(rate.toString()));
                } else if (!"".equals(rate.toString())) {
                    entity.setRecognitionRate(new BigDecimal(rate.toString()));
                }
            } else {
                entity.setRecognitionRate(BigDecimal.ZERO);
            }

            // 设置履约进度
            if (param.get("performanceProgress") != null) {
                Object progress = param.get("performanceProgress");
                if (progress instanceof BigDecimal) {
                    entity.setPerformanceProgress((BigDecimal) progress);
                } else if (progress instanceof Number) {
                    entity.setPerformanceProgress(new BigDecimal(progress.toString()));
                } else if (!"".equals(progress.toString())) {
                    entity.setPerformanceProgress(new BigDecimal(progress.toString()));
                }
            } else {
                entity.setPerformanceProgress(BigDecimal.ZERO);
            }

            // 设置确认依据
            if (param.get("recognitionBasis") != null) {
                entity.setRecognitionBasis(param.get("recognitionBasis").toString());
            }

            // 生成凭证ID
            Long voucherId = 5000L + System.currentTimeMillis() % 10000;
            entity.setVoucherId(voucherId);

            // 设置账簿ID和租户ID
            if (param.get("bookId") != null && !"".equals(param.get("bookId").toString())) {
                entity.setBookId(Long.parseLong(param.get("bookId").toString()));
            } else {
                entity.setBookId(1L);
            }
            if (param.get("tenantId") != null && !"".equals(param.get("tenantId").toString())) {
                entity.setTenantId(Long.parseLong(param.get("tenantId").toString()));
            } else {
                entity.setTenantId(1L);
            }

            // 设置基本字段
            entity.setVersion(1);
            entity.setIsDeleted(0);
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());

            // 保存到数据库
            log.info("准备保存收入确认记录到数据库: {}", entity);
            int rows = revenueRecognitionMapper.insert(entity);

            if (rows > 0) {
                log.info("收入确认记录保存成功，recognitionId: {}", entity.getRecognitionId());

                // 构建返回结果
                Map<String, Object> result = new HashMap<>();
                result.put("recognitionId", entity.getRecognitionId());
                result.put("recognitionBatchId", "REC_" + entity.getRecognitionId());
                result.put("recognitionPeriod", recognitionPeriod);
                result.put("totalRecognitionAmount", recognitionAmount);
                result.put("processedContractCount", 1);
                result.put("generatedVoucherIds", Arrays.asList(voucherId));
                result.put("createTime", entity.getCreateTime().toString());

                return result;
            } else {
                throw new RuntimeException("保存收入确认记录失败");
            }
        } catch (Exception e) {
            log.error("执行收入确认失败", e);
            throw new RuntimeException("执行失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> revokeRevenueRecognition(String recognitionId, Map<String, Object> param) {
        try {
            // TODO: 实现真实业务逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("recognitionId", recognitionId);
            result.put("revokeTime", LocalDateTime.now().toString());
            result.put("reason", param.get("reason"));
            
            return result;
        } catch (Exception e) {
            log.error("撤销收入确认失败", e);
            throw new RuntimeException("撤销失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> batchRevenueRecognition(Map<String, Object> param) {
        try {
            // TODO: 实现真实业务逻辑
            // 支持 ids 或 recognitionIds 两种参数名
            List<String> recognitionIds = (List<String>) param.get("ids");
            if (recognitionIds == null) {
                recognitionIds = (List<String>) param.get("recognitionIds");
            }

            // 如果参数为空，返回错误
            if (recognitionIds == null || recognitionIds.isEmpty()) {
                Map<String, Object> result = new HashMap<>();
                result.put("batchId", "BATCH_" + System.currentTimeMillis());
                result.put("processedCount", 0);
                result.put("successCount", 0);
                result.put("failedCount", 0);
                return result;
            }

            Map<String, Object> result = new HashMap<>();
            result.put("batchId", "BATCH_" + System.currentTimeMillis());
            result.put("processedCount", recognitionIds.size());
            result.put("successCount", recognitionIds.size());
            result.put("failedCount", 0);

            return result;
        } catch (Exception e) {
            log.error("批量确认收入失败", e);
            throw new RuntimeException("批量确认失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getRevenueRecognitionStats() {
        try {
            // TODO: 实现真实统计逻辑
            Map<String, Object> stats = new HashMap<>();
            stats.put("pendingCount", 45);
            stats.put("confirmedCount", 156);
            stats.put("totalAmount", new BigDecimal(12500000));
            stats.put("recognitionRate", 77.6);

            return stats;
        } catch (Exception e) {
            log.error("查询统计失败", e);
            throw new RuntimeException("查询统计失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getRevenueRecognitionDetail(String recognitionId) {
        try {
            log.info("查询收入确认详情，ID: {}", recognitionId);

            // 从数据库查询详情
            Map<String, Object> detail = revenueRecognitionMapper.selectRevenueRecognitionById(Long.parseLong(recognitionId));

            if (detail == null) {
                log.warn("未找到收入确认记录，ID: {}", recognitionId);
                return null;
            }

            // 转换字段名为小写驼峰命名
            convertRecognitionFieldNamesToCamelCase(detail);

            return detail;
        } catch (Exception e) {
            log.error("查询收入确认详情失败", e);
            throw new RuntimeException("查询详情失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getRecognitionVoucher(String recognitionId) {
        try {
            log.info("查询收入确认凭证，确认ID: {}", recognitionId);

            // 从数据库查询凭证信息
            Map<String, Object> voucher = revenueRecognitionMapper.selectVoucherByRecognitionId(Long.parseLong(recognitionId));

            if (voucher == null) {
                log.warn("未找到关联凭证，确认ID: {}", recognitionId);
                return null;
            }

            // 转换字段名为小写驼峰命名
            convertVoucherFieldNamesToCamelCase(voucher);

            return voucher;
        } catch (Exception e) {
            // 检查是否是表不存在的错误
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("T_VOUCHER") || errorMsg.contains("无效的表或视图名"))) {
                log.warn("凭证表不存在，确认ID: {}", recognitionId);
                // 返回null表示没有凭证数据，而不是抛出异常
                return null;
            }
            log.error("查询收入确认凭证失败", e);
            throw new RuntimeException("查询凭证失败: " + e.getMessage());
        }
    }

    /**
     * 转换凭证字段名为小写驼峰命名
     */
    private void convertVoucherFieldNamesToCamelCase(Map<String, Object> item) {
        // 凭证ID
        if (item.containsKey("VOUCHER_ID")) {
            item.put("voucherId", item.remove("VOUCHER_ID"));
        }
        // 凭证编号
        if (item.containsKey("VOUCHER_NO")) {
            item.put("voucherNo", item.remove("VOUCHER_NO"));
        }
        // 凭证类型
        if (item.containsKey("VOUCHER_TYPE")) {
            item.put("voucherType", item.remove("VOUCHER_TYPE"));
        }
        // 凭证日期
        if (item.containsKey("VOUCHER_DATE")) {
            item.put("voucherDate", item.remove("VOUCHER_DATE"));
        }
        // 会计期间
        if (item.containsKey("ACCOUNTING_PERIOD")) {
            item.put("accountingPeriod", item.remove("ACCOUNTING_PERIOD"));
        }
        // 借方金额
        if (item.containsKey("DEBIT_AMOUNT")) {
            item.put("debitAmount", item.remove("DEBIT_AMOUNT"));
        }
        // 贷方金额
        if (item.containsKey("CREDIT_AMOUNT")) {
            item.put("creditAmount", item.remove("CREDIT_AMOUNT"));
        }
        // 摘要
        if (item.containsKey("SUMMARY")) {
            item.put("summary", item.remove("SUMMARY"));
        }
        // 凭证状态
        if (item.containsKey("VOUCHER_STATUS")) {
            item.put("voucherStatus", item.remove("VOUCHER_STATUS"));
        }
        // 制单人
        if (item.containsKey("CREATOR_NAME")) {
            item.put("creatorName", item.remove("CREATOR_NAME"));
        }
        // 创建时间
        if (item.containsKey("CREATE_TIME")) {
            item.put("createTime", item.remove("CREATE_TIME"));
        }
        // 审核人
        if (item.containsKey("AUDITOR_NAME")) {
            item.put("auditorName", item.remove("AUDITOR_NAME"));
        }
        // 审核时间
        if (item.containsKey("AUDIT_TIME")) {
            item.put("auditTime", item.remove("AUDIT_TIME"));
        }
        // 凭证分录
        if (item.containsKey("ENTRIES")) {
            item.put("entries", item.remove("ENTRIES"));
        }
    }

    // ==================== 收入分配管理 ====================

    @Override
    public PageResult<Map<String, Object>> getRevenueAllocationList(PageableParam param) {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 1; i <= param.getPageSize(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("allocationId", 18000 + i);
                item.put("allocationNo", "ALLOC202412190" + String.format("%03d", i));
                item.put("allocationType", (i % 4) + 1);
                item.put("allocationTypeName", getAllocationTypeName((i % 4) + 1));
                item.put("allocationPeriod", "2024-12");
                item.put("totalAmount", new BigDecimal(100000 + i * 5000));
                item.put("allocatedAmount", new BigDecimal(80000 + i * 4000));
                item.put("remainingAmount", new BigDecimal(20000 + i * 1000));
                item.put("allocationStatus", i % 2);
                item.put("allocationDate", LocalDate.now().toString());
                item.put("createTime", LocalDateTime.now().toString());
                list.add(item);
            }

            PageResult<Map<String, Object>> result = new PageResult<>();
            result.setTlist(list);
            result.setTotalRecord(89);
            result.setCurrentPage(param.getPageNumber());
            result.setPageSize(param.getPageSize());

            return result;
        } catch (Exception e) {
            log.error("查询收入分配列表失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> createRevenueAllocation(Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("allocationId", System.currentTimeMillis());
            result.put("allocationNo", param.get("allocationNo"));
            result.put("createTime", LocalDateTime.now().toString());

            return result;
        } catch (Exception e) {
            log.error("创建收入分配失败", e);
            throw new RuntimeException("创建失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> executeRevenueAllocation(String allocationId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("allocationId", allocationId);
            result.put("executeTime", LocalDateTime.now().toString());
            result.put("status", "executed");

            return result;
        } catch (Exception e) {
            log.error("执行收入分配失败", e);
            throw new RuntimeException("执行失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAllocationRuleList() {
        try {
            List<Map<String, Object>> rules = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                Map<String, Object> rule = new HashMap<>();
                rule.put("ruleId", 9000 + i);
                rule.put("ruleName", "分配规则" + i);
                rule.put("ruleType", (i % 3) + 1);
                rule.put("isEnabled", true);
                rules.add(rule);
            }
            return rules;
        } catch (Exception e) {
            log.error("查询分配规则失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    // ==================== 合同收入管理 ====================

    @Override
    public PageResult<Map<String, Object>> getContractRevenueList(PageableParam param) {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 1; i <= param.getPageSize(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("contractId", 7000 + i);
                item.put("contractNo", "CON202412190" + String.format("%03d", i));
                item.put("contractName", "软件开发合同" + i);
                item.put("customerName", "客户" + (char)('A' + (i % 5)));
                item.put("contractAmount", new BigDecimal(500000 + i * 10000));
                item.put("recognizedAmount", new BigDecimal(300000 + i * 6000));
                item.put("remainingAmount", new BigDecimal(200000 + i * 4000));
                item.put("performanceProgress", 60 + (i % 40));
                item.put("contractStatus", (i % 4) + 1);
                item.put("recognitionMethod", (i % 3) + 1);
                item.put("recognitionMethodName", getRecognitionMethodName((i % 3) + 1));
                item.put("signDate", LocalDate.now().minusDays(i * 10).toString());
                list.add(item);
            }

            PageResult<Map<String, Object>> result = new PageResult<>();
            result.setTlist(list);
            result.setTotalRecord(156);
            result.setCurrentPage(param.getPageNumber());
            result.setPageSize(param.getPageSize());

            return result;
        } catch (Exception e) {
            log.error("查询合同收入列表失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createContractRevenue(Map<String, Object> param) {
        try {
            log.info("开始创建合同收入, 参数: {}", param);

            // 检查Mapper是否注入
            if (revenueContractMapper == null) {
                log.error("revenueContractMapper未正确注入");
                throw new RuntimeException("系统配置错误：RevenueContractMapper未正确注入");
            }

            // 创建实体对象
            RevenueContractEntity entity = new RevenueContractEntity();

            // 设置合同编号
            entity.setContractNo(getStringValue(param, "contractNo"));

            // 设置合同名称
            entity.setContractName(getStringValue(param, "contractName"));

            // 设置客户ID
            Object customerIdObj = param.get("customerId");
            if (customerIdObj != null && !"".equals(customerIdObj.toString())) {
                entity.setCustomerId(Long.parseLong(customerIdObj.toString()));
            }

            // 设置合同金额
            Object amountObj = param.get("contractAmount");
            if (amountObj != null) {
                entity.setContractAmount(new BigDecimal(amountObj.toString()));
            }

            // 设置合同状态，默认为待生效(1)
            Object statusObj = param.get("contractStatus");
            if (statusObj != null) {
                entity.setContractStatus(Integer.parseInt(statusObj.toString()));
            } else {
                entity.setContractStatus(1);
            }

            // 设置日期字段
            entity.setSignDate(parseLocalDate(param.get("signDate")));
            entity.setEffectiveDate(parseLocalDate(param.get("effectiveDate")));
            entity.setExpiryDate(parseLocalDate(param.get("expiryDate")));

            // 设置确认方法
            Object methodObj = param.get("recognitionMethod");
            if (methodObj != null && !"".equals(methodObj.toString())) {
                entity.setRecognitionMethod(Integer.parseInt(methodObj.toString()));
            }

            // 设置履约义务
            entity.setPerformanceObligations(getStringValue(param, "performanceObligations"));

            // 设置合同描述
            entity.setContractDesc(getStringValue(param, "contractDesc"));

            // 设置账簿ID和租户ID（必填字段，如果未提供则使用默认值）
            Object bookIdObj = param.get("bookId");
            if (bookIdObj != null && !"".equals(bookIdObj.toString())) {
                entity.setBookId(Long.parseLong(bookIdObj.toString()));
            } else {
                // 默认账簿ID为1
                entity.setBookId(1L);
            }

            Object tenantIdObj = param.get("tenantId");
            if (tenantIdObj != null && !"".equals(tenantIdObj.toString())) {
                entity.setTenantId(Long.parseLong(tenantIdObj.toString()));
            } else {
                // 默认租户ID为1
                entity.setTenantId(1L);
            }

            // 设置创建时间
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());

            // 设置删除标识
            entity.setIsDeleted(0);

            // 设置版本号
            entity.setVersion(1);

            // 保存到数据库
            log.info("准备保存合同到数据库: {}", entity);
            int insertResult = revenueContractMapper.insert(entity);
            log.info("合同保存结果: {}, contractId: {}", insertResult, entity.getContractId());

            if (insertResult <= 0) {
                throw new RuntimeException("保存合同失败");
            }

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("contractId", entity.getContractId());
            result.put("contractNo", entity.getContractNo());
            result.put("createTime", entity.getCreateTime().toString());

            log.info("创建合同收入成功, contractId: {}", entity.getContractId());
            return result;
        } catch (Exception e) {
            log.error("创建合同收入失败", e);
            throw new RuntimeException("创建失败: " + e.getMessage());
        }
    }

    /**
     * 获取字符串值
     */
    private String getStringValue(Map<String, Object> param, String key) {
        Object value = param.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * 解析日期
     */
    private LocalDate parseLocalDate(Object dateObj) {
        if (dateObj == null || "".equals(dateObj.toString())) {
            return null;
        }
        try {
            String dateStr = dateObj.toString();
            // 支持多种日期格式
            if (dateStr.contains("T")) {
                // ISO格式: 2024-12-19T00:00:00
                return LocalDate.parse(dateStr.substring(0, 10));
            } else if (dateStr.length() == 10) {
                // 标准格式: 2024-12-19
                return LocalDate.parse(dateStr);
            } else {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        } catch (Exception e) {
            log.warn("日期解析失败: {}", dateObj, e);
            return null;
        }
    }

    @Override
    public Map<String, Object> identifyPerformanceObligations(String contractId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("contractId", contractId);
            result.put("obligationsCount", 3);
            result.put("identifyTime", LocalDateTime.now().toString());

            List<Map<String, Object>> obligations = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                Map<String, Object> obligation = new HashMap<>();
                obligation.put("obligationId", i);
                obligation.put("obligationName", "履约义务" + i);
                obligation.put("obligationAmount", new BigDecimal(100000 + i * 50000));
                obligations.add(obligation);
            }
            result.put("obligations", obligations);

            return result;
        } catch (Exception e) {
            log.error("识别履约义务失败", e);
            throw new RuntimeException("识别失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> updatePerformanceProgress(String contractId, Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("contractId", contractId);
            result.put("oldProgress", 60);
            result.put("newProgress", param.get("performanceProgress"));
            result.put("updateTime", LocalDateTime.now().toString());

            return result;
        } catch (Exception e) {
            log.error("更新履约进度失败", e);
            throw new RuntimeException("更新失败: " + e.getMessage());
        }
    }

    // ==================== 递延收入管理 ====================

    @Override
    public PageResult<Map<String, Object>> getDeferredRevenueList(PageableParam param) {
        try {
            log.info("开始查询递延收入列表，参数: {}", param);

            // 检查Mapper是否注入
            if (deferredRevenueMapper == null) {
                log.error("deferredRevenueMapper未正确注入");
                throw new RuntimeException("系统配置错误：Mapper未正确注入");
            }

            // 提取搜索参数
            Map<String, Object> searchParams = (Map<String, Object>) param.getParam();
            if (searchParams == null) {
                searchParams = new HashMap<>();
            }

            log.info("递延收入查询参数: {}", searchParams);

            // 查询所有数据（使用返回List的方法）
            List<Map<String, Object>> allRecords = deferredRevenueMapper.selectDeferredRevenueMapList(searchParams);

            // 检查查询结果
            if (allRecords == null) {
                log.warn("递延收入查询结果为null，返回空列表");
                allRecords = new ArrayList<>();
            }

            log.info("递延收入查询成功，总记录数: {}", allRecords.size());

            // 手动分页
            int pageNum = param.getPageNumber() != null ? param.getPageNumber() : 1;
            int pageSize = param.getPageSize() != null ? param.getPageSize() : 10;
            int total = allRecords.size();

            // 计算分页数据
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);

            List<Map<String, Object>> pagedRecords;
            if (fromIndex >= total) {
                pagedRecords = new ArrayList<>();
            } else {
                pagedRecords = allRecords.subList(fromIndex, toIndex);
            }

            // 构建返回结果
            PageResult<Map<String, Object>> result = new PageResult<>();
            result.setTlist(pagedRecords);
            result.setTotalRecord(total);
            result.setCurrentPage(pageNum);
            result.setPageSize(pageSize);

            return result;
        } catch (Exception e) {
            log.error("查询递延收入列表失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createDeferredRevenue(Map<String, Object> param) {
        try {
            log.info("开始创建递延收入，参数: {}", param);

            // 检查Mapper是否注入
            if (deferredRevenueMapper == null) {
                log.error("deferredRevenueMapper未正确注入");
                throw new RuntimeException("系统配置错误：Mapper未正确注入");
            }

            // 创建实体对象
            com.financial.sharing.oracle.entity.DeferredRevenueEntity entity =
                new com.financial.sharing.oracle.entity.DeferredRevenueEntity();

            // 设置基本信息 - 如果没有传递递延单号，则自动生成
            String deferredNo = param.get("deferredNo") != null ? param.get("deferredNo").toString() : null;
            if (deferredNo == null || deferredNo.isEmpty()) {
                // 自动生成递延单号：DR + 年月日 + 4位随机数（总长度14个字符）
                deferredNo = "DR" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"))
                    + String.format("%04d", (int)(Math.random() * 10000));
            }
            // 确保递延单号不超过20个字符
            if (deferredNo.length() > 20) {
                deferredNo = deferredNo.substring(0, 20);
            }
            entity.setDeferredNo(deferredNo);

            // 设置合同ID
            if (param.get("contractId") != null) {
                entity.setContractId(Long.parseLong(param.get("contractId").toString()));
            }

            // 设置递延类型
            if (param.get("deferredType") != null) {
                Object deferredType = param.get("deferredType");
                if (deferredType instanceof Integer) {
                    entity.setDeferredType((Integer) deferredType);
                } else if (deferredType instanceof String && !((String) deferredType).isEmpty()) {
                    entity.setDeferredType(Integer.parseInt((String) deferredType));
                }
            }

            // 设置递延期间（必填字段，数据库字段长度限制为10个字符）
            String deferredPeriod = null;
            if (param.get("deferredPeriod") != null && !param.get("deferredPeriod").toString().isEmpty()) {
                deferredPeriod = param.get("deferredPeriod").toString();
            } else {
                // 如果没有提供递延期间，使用当前年月（格式：YYYYMM，6个字符）
                deferredPeriod = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
            }
            // 确保递延期间不超过10个字符（数据库字段VARCHAR(10)限制）
            if (deferredPeriod != null && deferredPeriod.length() > 10) {
                deferredPeriod = deferredPeriod.substring(0, 10);
            }
            entity.setDeferredPeriod(deferredPeriod);

            // 设置金额
            BigDecimal totalAmount = BigDecimal.ZERO;
            if (param.get("totalAmount") != null) {
                Object amount = param.get("totalAmount");
                if (amount instanceof BigDecimal) {
                    totalAmount = (BigDecimal) amount;
                } else if (amount instanceof Number) {
                    totalAmount = new BigDecimal(amount.toString());
                } else if (amount instanceof String && !((String) amount).isEmpty()) {
                    totalAmount = new BigDecimal((String) amount);
                }
            }
            entity.setOriginalAmount(totalAmount);
            entity.setDeferredAmount(totalAmount);
            entity.setRecognizedAmount(BigDecimal.ZERO);
            entity.setRemainingAmount(totalAmount);

            // 设置日期
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (param.get("startDate") != null && !param.get("startDate").toString().isEmpty()) {
                entity.setStartDate(LocalDate.parse(param.get("startDate").toString(), dateFormatter));
            }
            if (param.get("endDate") != null && !param.get("endDate").toString().isEmpty()) {
                entity.setEndDate(LocalDate.parse(param.get("endDate").toString(), dateFormatter));
            }

            // 设置确认方法
            if (param.get("recognitionMethod") != null) {
                Object method = param.get("recognitionMethod");
                if (method instanceof Integer) {
                    entity.setRecognitionMethod((Integer) method);
                } else if (method instanceof String && !((String) method).isEmpty()) {
                    entity.setRecognitionMethod(Integer.parseInt((String) method));
                }
            }

            // 设置账簿ID和租户ID（必填字段，如果未提供则使用默认值）
            Object bookIdObj = param.get("bookId");
            if (bookIdObj != null && !"".equals(bookIdObj.toString())) {
                entity.setBookId(Long.parseLong(bookIdObj.toString()));
            } else {
                // 默认账簿ID为1
                entity.setBookId(1L);
            }

            Object tenantIdObj = param.get("tenantId");
            if (tenantIdObj != null && !"".equals(tenantIdObj.toString())) {
                entity.setTenantId(Long.parseLong(tenantIdObj.toString()));
            } else {
                // 默认租户ID为1
                entity.setTenantId(1L);
            }

            // 设置状态为待确认
            entity.setDeferredStatus(0);
            entity.setIsDeleted(0);
            entity.setVersion(1);
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());

            // 设置创建人和更新人（设置为null，避免字符串截断问题）
            entity.setCreator(null);
            entity.setUpdater(null);

            // 打印所有字段值用于调试
            log.info("准备插入递延收入数据:");
            log.info("  deferredNo: {} (长度: {})", entity.getDeferredNo(),
                entity.getDeferredNo() != null ? entity.getDeferredNo().length() : 0);
            log.info("  deferredPeriod: {} (长度: {})", entity.getDeferredPeriod(),
                entity.getDeferredPeriod() != null ? entity.getDeferredPeriod().length() : 0);
            log.info("  contractId: {}", entity.getContractId());
            log.info("  deferredType: {}", entity.getDeferredType());
            log.info("  originalAmount: {}", entity.getOriginalAmount());
            log.info("  startDate: {}", entity.getStartDate());
            log.info("  endDate: {}", entity.getEndDate());

            // 保存到数据库
            int rows = deferredRevenueMapper.insert(entity);

            if (rows > 0) {
                log.info("递延收入创建成功，ID: {}", entity.getDeferredId());

                Map<String, Object> result = new HashMap<>();
                result.put("deferredId", entity.getDeferredId());
                result.put("deferredNo", entity.getDeferredNo());
                result.put("createTime", entity.getCreateTime().toString());
                return result;
            } else {
                throw new RuntimeException("保存数据失败");
            }
        } catch (Exception e) {
            log.error("创建递延收入失败", e);
            throw new RuntimeException("创建失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> recognizeDeferredRevenue(String deferredId, Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("deferredId", deferredId);
            result.put("recognitionAmount", param.get("recognitionAmount"));
            result.put("recognitionDate", param.get("recognitionDate"));
            result.put("voucherId", 5000 + System.currentTimeMillis() % 1000);

            return result;
        } catch (Exception e) {
            log.error("确认递延收入失败", e);
            throw new RuntimeException("确认失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getRecognitionSchedule(String deferredId) {
        try {
            List<Map<String, Object>> scheduleList = new ArrayList<>();
            for (int i = 1; i <= 6; i++) {
                Map<String, Object> schedule = new HashMap<>();
                schedule.put("period", "2024-" + String.format("%02d", 6 + i));
                schedule.put("plannedAmount", new BigDecimal(20000 + i * 1000));
                schedule.put("actualAmount", i <= 3 ? new BigDecimal(20000 + i * 1000) : BigDecimal.ZERO);
                schedule.put("recognitionDate", i <= 3 ? LocalDate.now().minusDays((4-i) * 30).toString() : null);
                schedule.put("status", i <= 3 ? 1 : 0);
                schedule.put("remarks", "第" + i + "期确认");
                scheduleList.add(schedule);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("scheduleList", scheduleList);

            return result;
        } catch (Exception e) {
            log.error("查询确认计划失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getDeferredRevenueStats() {
        try {
            log.info("开始查询递延收入统计");

            // 检查Mapper是否注入
            if (deferredRevenueMapper == null) {
                log.error("deferredRevenueMapper未正确注入");
                throw new RuntimeException("系统配置错误：Mapper未正确注入");
            }

            // 查询数据库统计
            Map<String, Object> queryParam = new HashMap<>();
            Map<String, Object> stats = deferredRevenueMapper.selectDeferredRevenueStats(queryParam);

            // 处理null情况
            if (stats == null) {
                stats = new HashMap<>();
                stats.put("totalDeferred", 0);
                stats.put("totalDeferredAmount", BigDecimal.ZERO);
                stats.put("recognizedAmount", BigDecimal.ZERO);
                stats.put("remainingAmount", BigDecimal.ZERO);
            }

            // 添加更新时间
            stats.put("lastUpdateTime", LocalDateTime.now().toString());

            log.info("递延收入统计查询成功: {}", stats);

            return stats;
        } catch (Exception e) {
            log.error("查询递延收入统计失败", e);
            throw new RuntimeException("查询统计失败: " + e.getMessage());
        }
    }

    // ==================== 收入调整管理 ====================

    @Override
    public PageResult<Map<String, Object>> getRevenueAdjustmentList(PageableParam param) {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 1; i <= param.getPageSize(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("adjustmentId", 20000 + i);
                item.put("adjustmentNo", "ADJ202412190" + String.format("%03d", i));
                item.put("adjustmentType", (i % 4) + 1);
                item.put("adjustmentTypeName", getAdjustmentTypeName((i % 4) + 1));
                item.put("originalAmount", new BigDecimal(100000 + i * 5000));
                item.put("adjustmentAmount", new BigDecimal((i % 2 == 0 ? 1 : -1) * (5000 + i * 500)));
                item.put("adjustedAmount", new BigDecimal(100000 + i * 5000 + (i % 2 == 0 ? 1 : -1) * (5000 + i * 500)));
                item.put("adjustmentStatus", i % 5);
                item.put("adjustmentPeriod", "2024-12");
                item.put("applicant", "用户" + (i % 5 + 1));
                item.put("applicationDate", LocalDate.now().minusDays(i).toString());
                item.put("adjustmentDesc", "调整原因说明" + i);
                list.add(item);
            }

            PageResult<Map<String, Object>> result = new PageResult<>();
            result.setTlist(list);
            result.setTotalRecord(67);
            result.setCurrentPage(param.getPageNumber());
            result.setPageSize(param.getPageSize());

            return result;
        } catch (Exception e) {
            log.error("查询收入调整列表失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> createRevenueAdjustment(Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("adjustmentId", System.currentTimeMillis());
            result.put("adjustmentNo", param.get("adjustmentNo"));
            result.put("applicationTime", LocalDateTime.now().toString());
            result.put("status", "pending");

            return result;
        } catch (Exception e) {
            log.error("创建收入调整失败", e);
            throw new RuntimeException("创建失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> approveRevenueAdjustment(String adjustmentId, Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("adjustmentId", adjustmentId);
            result.put("approvalResult", param.get("approvalResult"));
            result.put("approvalComments", param.get("approvalComments"));
            result.put("approvalTime", LocalDateTime.now().toString());

            return result;
        } catch (Exception e) {
            log.error("审批收入调整失败", e);
            throw new RuntimeException("审批失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> executeRevenueAdjustment(String adjustmentId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("adjustmentId", adjustmentId);
            result.put("executeTime", LocalDateTime.now().toString());
            result.put("voucherId", 5000 + System.currentTimeMillis() % 1000);
            result.put("status", "executed");

            return result;
        } catch (Exception e) {
            log.error("执行收入调整失败", e);
            throw new RuntimeException("执行失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getAdjustmentImpactAnalysis(String adjustmentId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("adjustmentId", adjustmentId);
            result.put("impactAmount", new BigDecimal(15000));
            result.put("affectedPeriods", Arrays.asList("2024-12", "2025-01"));
            result.put("affectedSubjects", Arrays.asList("主营业务收入", "应收账款"));
            result.put("riskLevel", "中等");
            result.put("suggestions", Arrays.asList("建议在月末执行", "需要更新相关报表"));

            return result;
        } catch (Exception e) {
            log.error("获取调整影响分析失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private String getRecognitionTypeName(int type) {
        switch (type) {
            case 1: return "按时间确认";
            case 2: return "按进度确认";
            case 3: return "按事件确认";
            default: return "未知";
        }
    }

    private String getAllocationTypeName(int type) {
        switch (type) {
            case 1: return "部门分配";
            case 2: return "产品分配";
            case 3: return "项目分配";
            case 4: return "客户分配";
            default: return "未知";
        }
    }

    private String getRecognitionMethodName(int method) {
        switch (method) {
            case 1: return "按时间";
            case 2: return "按进度";
            case 3: return "按事件";
            default: return "未知";
        }
    }

    private String getDeferredTypeName(int type) {
        switch (type) {
            case 1: return "预收款项";
            case 2: return "递延收入";
            case 3: return "合同负债";
            default: return "未知";
        }
    }

    private String getAdjustmentTypeName(int type) {
        switch (type) {
            case 1: return "收入增加";
            case 2: return "收入减少";
            case 3: return "收入冲回";
            case 4: return "收入重分类";
            default: return "未知";
        }
    }

    /**
     * 将收入确认Map中的大写字段名转换为小写驼峰命名
     * 兼容达梦数据库返回大写字段名的情况
     * 例如: RECOGNITION_ID -> recognitionId, RECOGNITIONTYPE -> recognitionType
     */
    private void convertRecognitionFieldNamesToCamelCase(Map<String, Object> item) {
        // 定义需要转换的字段映射 (大写 -> 小写驼峰)
        Map<String, String> fieldMappings = new HashMap<>();
        // 收入确认相关字段
        fieldMappings.put("RECOGNITIONID", "recognitionId");
        fieldMappings.put("RECOGNITION_ID", "recognitionId");
        fieldMappings.put("CONTRACTID", "contractId");
        fieldMappings.put("CONTRACT_ID", "contractId");
        fieldMappings.put("CONTRACTNO", "contractNo");
        fieldMappings.put("CONTRACT_NO", "contractNo");
        fieldMappings.put("CONTRACTNAME", "contractName");
        fieldMappings.put("CONTRACT_NAME", "contractName");
        fieldMappings.put("RECOGNITIONPERIOD", "recognitionPeriod");
        fieldMappings.put("RECOGNITION_PERIOD", "recognitionPeriod");
        fieldMappings.put("RECOGNITIONDATE", "recognitionDate");
        fieldMappings.put("RECOGNITION_DATE", "recognitionDate");
        fieldMappings.put("RECOGNITIONAMOUNT", "recognitionAmount");
        fieldMappings.put("RECOGNITION_AMOUNT", "recognitionAmount");
        fieldMappings.put("CUMULATIVEAMOUNT", "cumulativeAmount");
        fieldMappings.put("CUMULATIVE_AMOUNT", "cumulativeAmount");
        fieldMappings.put("RECOGNITIONRATE", "recognitionRate");
        fieldMappings.put("RECOGNITION_RATE", "recognitionRate");
        fieldMappings.put("PERFORMANCEPROGRESS", "performanceProgress");
        fieldMappings.put("PERFORMANCE_PROGRESS", "performanceProgress");
        fieldMappings.put("RECOGNITIONTYPE", "recognitionType");
        fieldMappings.put("RECOGNITION_TYPE", "recognitionType");
        fieldMappings.put("RECOGNITIONTYPENAME", "recognitionTypeName");
        fieldMappings.put("RECOGNITION_TYPE_NAME", "recognitionTypeName");
        fieldMappings.put("RECOGNITIONBASIS", "recognitionBasis");
        fieldMappings.put("RECOGNITION_BASIS", "recognitionBasis");
        fieldMappings.put("VOUCHERID", "voucherId");
        fieldMappings.put("VOUCHER_ID", "voucherId");
        fieldMappings.put("CREATETIME", "createTime");
        fieldMappings.put("CREATE_TIME", "createTime");
        fieldMappings.put("UPDATETIME", "updateTime");
        fieldMappings.put("UPDATE_TIME", "updateTime");
        // 前端需要但可能数据库没有的字段
        fieldMappings.put("RECOGNITIONSTATUS", "recognitionStatus");
        fieldMappings.put("RECOGNITION_STATUS", "recognitionStatus");
        fieldMappings.put("DEFERREDREVENUE", "deferredRevenue");
        fieldMappings.put("DEFERRED_REVENUE", "deferredRevenue");
        fieldMappings.put("CUMULATIVERECOGNITION", "cumulativeRecognition");
        fieldMappings.put("CUMULATIVE_RECOGNITION", "cumulativeRecognition");
        fieldMappings.put("REMAININGREVENUE", "remainingRevenue");
        fieldMappings.put("REMAINING_REVENUE", "remainingRevenue");

        // 遍历映射，如果存在大写字段名，则添加对应的小写驼峰字段
        for (Map.Entry<String, String> mapping : fieldMappings.entrySet()) {
            String upperKey = mapping.getKey();
            String camelKey = mapping.getValue();
            if (item.containsKey(upperKey) && !item.containsKey(camelKey)) {
                item.put(camelKey, item.get(upperKey));
            }
        }
    }

    // ==================== 收入结构分析管理 ====================

    @Override
    public PageResult<Map<String, Object>> getRevenueStructureAnalysisList(PageableParam param) {
        try {
            log.info("开始查询收入结构分析列表，参数: {}", param);

            // 从数据库查询收入结构分析数据
            List<Map<String, Object>> dataList = new ArrayList<>();

            // 提取搜索参数
            Map<String, Object> searchParams = (Map<String, Object>) param.getParam();
            String orgName = searchParams != null ? (String) searchParams.get("orgName") : null;
            String recognitionType = searchParams != null ? (String) searchParams.get("recognitionType") : null;

            // 从数据库获取收入确认数据并进行结构分析
            List<Map<String, Object>> recognitionData = revenueRecognitionMapper.selectRevenueRecognitionList(searchParams);

            if (recognitionData != null && !recognitionData.isEmpty()) {
                // 按财务组织分组统计
                Map<String, Map<String, Object>> groupedData = new LinkedHashMap<>();

                for (Map<String, Object> record : recognitionData) {
                    // 字段名转换
                    normalizeFieldNames(record);

                    String orgNameKey = getStringValue(record, "orgName", "未知组织");
                    String revenueType = getStringValue(record, "revenueType", "其他");
                    String recognitionTypeName = getStringValue(record, "recognitionTypeName", "未分类");

                    String key = orgNameKey + "_" + revenueType + "_" + recognitionTypeName;

                    if (!groupedData.containsKey(key)) {
                        Map<String, Object> item = new LinkedHashMap<>();
                        item.put("id", System.currentTimeMillis() + groupedData.size());
                        item.put("orgName", orgNameKey);
                        item.put("recognitionType", recognitionTypeName);
                        item.put("revenueType", revenueType);
                        item.put("materialCategory", getStringValue(record, "materialCategory", ""));
                        item.put("customerCategory", getStringValue(record, "customerCategory", ""));
                        item.put("customerIndustry", getStringValue(record, "customerIndustry", ""));
                        item.put("customerRegion", getStringValue(record, "customerRegion", ""));
                        item.put("unitName", getStringValue(record, "unitName", "元"));
                        item.put("quantity", 0);
                        item.put("currencyName", getStringValue(record, "currencyName", "人民币"));
                        item.put("originalNoTaxAmount", BigDecimal.ZERO);
                        item.put("originalTaxAmount", BigDecimal.ZERO);
                        item.put("originalTotalAmount", BigDecimal.ZERO);
                        item.put("localNoTaxAmount", BigDecimal.ZERO);
                        item.put("localTaxAmount", BigDecimal.ZERO);
                        item.put("localTotalAmount", BigDecimal.ZERO);
                        groupedData.put(key, item);
                    }

                    // 累加金额
                    Map<String, Object> item = groupedData.get(key);
                    BigDecimal amount = getBigDecimalValue(record, "recognitionAmount");
                    BigDecimal taxAmount = amount.multiply(new BigDecimal("0.13"));

                    item.put("quantity", ((Integer) item.get("quantity")) + 1);
                    item.put("originalNoTaxAmount", ((BigDecimal) item.get("originalNoTaxAmount")).add(amount));
                    item.put("originalTaxAmount", ((BigDecimal) item.get("originalTaxAmount")).add(taxAmount));
                    item.put("originalTotalAmount", ((BigDecimal) item.get("originalTotalAmount")).add(amount.add(taxAmount)));
                    item.put("localNoTaxAmount", ((BigDecimal) item.get("localNoTaxAmount")).add(amount));
                    item.put("localTaxAmount", ((BigDecimal) item.get("localTaxAmount")).add(taxAmount));
                    item.put("localTotalAmount", ((BigDecimal) item.get("localTotalAmount")).add(amount.add(taxAmount)));
                }

                dataList.addAll(groupedData.values());
            }

            // 分页处理
            int pageNum = param.getPageNum() != null ? param.getPageNum() : 1;
            int pageSize = param.getPageSize() != null ? param.getPageSize() : 10;
            int total = dataList.size();
            int startIndex = (pageNum - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, total);

            List<Map<String, Object>> pageData = new ArrayList<>();
            if (startIndex < total) {
                pageData = dataList.subList(startIndex, endIndex);
            }

            PageResult<Map<String, Object>> result = new PageResult<>();
            result.setTlist(pageData);
            result.setTotalRecord(total);
            result.setCurrentPage(pageNum);
            result.setPageSize(pageSize);
            result.setTotalPage((total + pageSize - 1) / pageSize);

            log.info("收入结构分析列表查询完成，总记录数: {}", total);
            return result;

        } catch (Exception e) {
            log.error("查询收入结构分析列表失败", e);
            throw new RuntimeException("查询收入结构分析列表失败: " + e.getMessage());
        }
    }

    /**
     * 将数据库返回的大写字段名转换为驼峰命名
     */
    private void normalizeFieldNames(Map<String, Object> record) {
        Map<String, String> fieldMapping = new HashMap<>();
        fieldMapping.put("ORG_NAME", "orgName");
        fieldMapping.put("ORGNAME", "orgName");
        fieldMapping.put("REVENUE_TYPE", "revenueType");
        fieldMapping.put("REVENUETYPE", "revenueType");
        fieldMapping.put("RECOGNITION_TYPE_NAME", "recognitionTypeName");
        fieldMapping.put("RECOGNITIONTYPENAME", "recognitionTypeName");
        fieldMapping.put("RECOGNITION_TYPE", "recognitionType");
        fieldMapping.put("RECOGNITIONTYPE", "recognitionType");
        fieldMapping.put("MATERIAL_CATEGORY", "materialCategory");
        fieldMapping.put("MATERIALCATEGORY", "materialCategory");
        fieldMapping.put("CUSTOMER_CATEGORY", "customerCategory");
        fieldMapping.put("CUSTOMERCATEGORY", "customerCategory");
        fieldMapping.put("CUSTOMER_INDUSTRY", "customerIndustry");
        fieldMapping.put("CUSTOMERINDUSTRY", "customerIndustry");
        fieldMapping.put("CUSTOMER_REGION", "customerRegion");
        fieldMapping.put("CUSTOMERREGION", "customerRegion");
        fieldMapping.put("UNIT_NAME", "unitName");
        fieldMapping.put("UNITNAME", "unitName");
        fieldMapping.put("CURRENCY_NAME", "currencyName");
        fieldMapping.put("CURRENCYNAME", "currencyName");
        fieldMapping.put("RECOGNITION_AMOUNT", "recognitionAmount");
        fieldMapping.put("RECOGNITIONAMOUNT", "recognitionAmount");

        for (Map.Entry<String, String> entry : fieldMapping.entrySet()) {
            String upperKey = entry.getKey();
            String camelKey = entry.getValue();
            if (record.containsKey(upperKey) && !record.containsKey(camelKey)) {
                record.put(camelKey, record.get(upperKey));
            }
        }
    }

    private String getStringValue(Map<String, Object> map, String key, String defaultValue) {
        Object value = map.get(key);
        if (value == null) {
            // 尝试大写形式
            value = map.get(key.toUpperCase());
        }
        return value != null ? value.toString() : defaultValue;
    }

    private BigDecimal getBigDecimalValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            value = map.get(key.toUpperCase());
        }
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        try {
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}

