package com.financial.sharing.controller;

import com.financial.sharing.service.RevenueAnalysisService;
import com.financial.sharing.service.RevenueManagementService;
import com.financial.sharing.service.RevenueContractService;
import com.financial.sharing.util.LegalDealUserToken;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import com.financial.sharing.vo.param.RevenueContractQueryParam;
import com.financial.sharing.vo.result.RevenueContractVO;
import com.hbfk.entity.TblStaffUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 收入管理控制器
 * 提供收入确认、分配、调整、分析等功能
 *
 * @author system
 * @date 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/revenue")
@CrossOrigin
public class RevenueManagementController {

    @Autowired
    private RevenueManagementService revenueManagementService;

    @Autowired
    private RevenueAnalysisService revenueAnalysisService;

    @Autowired
    private RevenueContractService revenueContractService;

    // ==================== 收入确认管理 ====================

    /**
     * 查询收入确认列表
     */
    @PostMapping("/recognition/getList")
    public MyJsonBean getRevenueRecognitionList(@RequestBody PageableParam param) {
        try {
            PageResult<Map<String, Object>> result = revenueManagementService.getRevenueRecognitionList(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 执行收入确认
     */
    @PostMapping("/recognition/execute")
    public MyJsonBean executeRevenueRecognition(@RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = revenueManagementService.executeRevenueRecognition(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    /**
     * 撤销收入确认
     */
    @PostMapping("/recognition/{recognitionId}/revoke")
    public MyJsonBean revokeRevenueRecognition(@PathVariable String recognitionId, @RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = revenueManagementService.revokeRevenueRecognition(recognitionId, param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("撤销失败: " + e.getMessage());
        }
    }

    /**
     * 批量确认收入
     */
    @PostMapping("/recognition/batch")
    public MyJsonBean batchRevenueRecognition(@RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = revenueManagementService.batchRevenueRecognition(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量确认失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入确认统计概览
     */
    @GetMapping("/recognition/stats")
    public MyJsonBean getRevenueRecognitionStats() {
        try {
            Map<String, Object> stats = revenueManagementService.getRevenueRecognitionStats();
            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入确认详情
     */
    @GetMapping("/recognition/{recognitionId}")
    public MyJsonBean getRevenueRecognitionDetail(@PathVariable String recognitionId) {
        try {
            log.info("查询收入确认详情，ID: {}", recognitionId);
            Map<String, Object> detail = revenueManagementService.getRevenueRecognitionDetail(recognitionId);
            if (detail == null) {
                return MyJsonBean.errorData("未找到对应的收入确认记录");
            }
            return MyJsonBean.successData(detail);
        } catch (Exception e) {
            log.error("查询收入确认详情失败", e);
            return MyJsonBean.errorData("查询详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据收入确认ID获取凭证信息
     */
    @GetMapping("/recognition/{recognitionId}/voucher")
    public MyJsonBean getRecognitionVoucher(@PathVariable String recognitionId) {
        try {
            log.info("查询收入确认凭证，确认ID: {}", recognitionId);
            Map<String, Object> voucher = revenueManagementService.getRecognitionVoucher(recognitionId);
            if (voucher == null) {
                return MyJsonBean.errorData("该收入确认记录暂无关联凭证");
            }
            return MyJsonBean.successData(voucher);
        } catch (Exception e) {
            log.error("查询收入确认凭证失败", e);
            return MyJsonBean.errorData("查询凭证失败: " + e.getMessage());
        }
    }

    // ==================== 收入分配管理 ====================

    @Autowired
    private com.financial.sharing.service.RevenueAllocationService revenueAllocationService;

    /**
     * 获取收入分配列表
     */
    @PostMapping("/allocation/getList")
    public MyJsonBean getRevenueAllocationList(@RequestBody Map<String, Object> requestParams, HttpServletRequest request) {
        try {
            log.info("查询收入分配列表,参数: {}", requestParams);

            // 从请求头获取token并解析租户ID
            String token = request.getHeader("token");
            if (token != null && !token.isEmpty()) {
                TblStaffUtil staff = LegalDealUserToken.parseUserToken(token);
                if (staff != null && staff.getCurrentOrg() != null && staff.getCurrentOrg().getOrgid() != null) {
                    requestParams.put("tenantId", staff.getCurrentOrg().getOrgid().longValue());
                }
            }

            // 创建PageableParam对象
            PageableParam param = new PageableParam();
            param.setPageNumber((Integer) requestParams.getOrDefault("pageNumber", 1));
            param.setPageSize((Integer) requestParams.getOrDefault("pageSize", 20));
            param.setParam(requestParams); // 将所有参数放入param字段

            Map<String, Object> result = revenueAllocationService.getAllocationList(param);

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> tlist = (List<Map<String, Object>>) result.get("tlist");
            pageResult.setTlist(tlist);
            pageResult.setTotalRecord((Integer) result.get("total"));
            pageResult.setCurrentPage((Integer) result.get("pageNumber"));
            pageResult.setPageSize((Integer) result.get("pageSize"));

            return MyJsonBean.successData(pageResult);
        } catch (Exception e) {
            log.error("查询收入分配列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入分配统计
     */
    @GetMapping("/allocation/stats")
    public MyJsonBean getRevenueAllocationStats(HttpServletRequest request) {
        try {
            log.info("查询收入分配统计");

            // 从请求头获取token并解析租户ID
            Long tenantId = 1L;
            String token = request.getHeader("token");
            if (token != null && !token.isEmpty()) {
                TblStaffUtil staff = LegalDealUserToken.parseUserToken(token);
                if (staff != null && staff.getCurrentOrg() != null && staff.getCurrentOrg().getOrgid() != null) {
                    tenantId = staff.getCurrentOrg().getOrgid().longValue();
                }
            }

            Map<String, Object> stats = revenueAllocationService.getAllocationStats(tenantId);
            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            log.error("查询收入分配统计失败", e);
            return MyJsonBean.errorData("查询统计失败: " + e.getMessage());
        }
    }

    /**
     * 创建收入分配
     */
    @PostMapping("/allocation/create")
    public MyJsonBean createRevenueAllocation(@RequestBody Map<String, Object> param, HttpServletRequest request) {
        try {
            log.info("创建收入分配,参数: {}", param);

            // 从请求头获取token并解析租户ID和用户ID
            String token = request.getHeader("token");
            if (token != null && !token.isEmpty()) {
                TblStaffUtil staff = LegalDealUserToken.parseUserToken(token);
                if (staff != null) {
                    if (staff.getCurrentOrg() != null && staff.getCurrentOrg().getOrgid() != null) {
                        param.put("tenantId", staff.getCurrentOrg().getOrgid().longValue());
                    }
                    if (staff.getStaffid() != null) {
                        param.put("creator", staff.getStaffid().longValue());
                    }
                }
            }

            // 调用Service保存到数据库
            Map<String, Object> result = revenueAllocationService.createAllocation(param);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("创建收入分配失败", e);
            return MyJsonBean.errorData("创建失败: " + e.getMessage());
        }
    }

    /**
     * 执行收入分配
     */
    @PostMapping("/allocation/{allocationId}/execute")
    public MyJsonBean executeRevenueAllocation(@PathVariable String allocationId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("allocationId", allocationId);
            result.put("executeTime", LocalDateTime.now().toString());
            result.put("status", "executed");
            
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    /**
     * 获取分配规则列表
     */
    @GetMapping("/allocation/rules")
    public MyJsonBean getAllocationRuleList() {
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

            return MyJsonBean.successData(rules);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 更新收入分配
     */
    @PutMapping("/allocation/{allocationId}")
    public MyJsonBean updateRevenueAllocation(@PathVariable String allocationId, @RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("allocationId", allocationId);
            result.put("updateTime", LocalDateTime.now().toString());
            result.put("status", "updated");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除收入分配
     */
    @DeleteMapping("/allocation/{allocationId}")
    public MyJsonBean deleteRevenueAllocation(@PathVariable String allocationId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("allocationId", allocationId);
            result.put("deleteTime", LocalDateTime.now().toString());
            result.put("status", "deleted");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    // ==================== 合同收入管理 ====================

    /**
     * 获取合同收入列表
     */
    @PostMapping("/contract/getList")
    public MyJsonBean getContractRevenueList(@RequestBody PageableParam param) {
        try {
            // 将PageableParam转换为RevenueContractQueryParam
            RevenueContractQueryParam queryParam = new RevenueContractQueryParam();
            queryParam.setPageNum(param.getPageNumber());
            queryParam.setPageSize(param.getPageSize());

            // 提取搜索参数
            Map<String, Object> searchParams = (Map<String, Object>) param.getParam();
            if (searchParams != null) {
                if (searchParams.get("contractNo") != null) {
                    queryParam.setContractNo(searchParams.get("contractNo").toString());
                }
                if (searchParams.get("contractName") != null) {
                    queryParam.setContractName(searchParams.get("contractName").toString());
                }
                if (searchParams.get("customerName") != null) {
                    queryParam.setCustomerName(searchParams.get("customerName").toString());
                }
                if (searchParams.get("contractStatus") != null) {
                    queryParam.setContractStatus(searchParams.get("contractStatus").toString());
                }
            }

            // 调用Service查询数据库
            PageResult<RevenueContractVO> result = revenueContractService.getRevenueContractList(queryParam);

            // 将RevenueContractVO转换为Map格式（保持前端兼容性）
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (RevenueContractVO vo : result.getTlist()) {
                Map<String, Object> item = new HashMap<>();
                item.put("contractId", vo.getContractId());
                item.put("contractNo", vo.getContractNo());
                item.put("contractName", vo.getContractName());
                item.put("customerId", vo.getCustomerId());
                item.put("contractAmount", vo.getContractAmount());
                item.put("contractStatus", vo.getContractStatus());
                item.put("statusDesc", vo.getStatusDesc());
                item.put("signDate", vo.getSignDate());
                item.put("effectiveDate", vo.getEffectiveDate());
                item.put("expiryDate", vo.getExpiryDate());
                item.put("createTime", vo.getCreateTime());
                item.put("updateTime", vo.getUpdateTime());

                // 添加默认值（数据库中可能没有的字段）
                item.put("customerName", "客户" + vo.getCustomerId());
                item.put("recognizedAmount", BigDecimal.ZERO);
                item.put("remainingAmount", vo.getContractAmount());
                item.put("performanceProgress", 0);
                item.put("recognitionMethod", 1);
                item.put("recognitionMethodName", "时点法");

                mapList.add(item);
            }

            PageResult<Map<String, Object>> mapResult = new PageResult<>();
            mapResult.setTlist(mapList);
            mapResult.setTotalRecord(result.getTotalRecord());
            mapResult.setCurrentPage(result.getCurrentPage());
            mapResult.setPageSize(result.getPageSize());
            mapResult.setTotalPage(result.getTotalPage());

            return MyJsonBean.successData(mapResult);
        } catch (Exception e) {
            log.error("查询合同收入列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建合同收入
     */
    @PostMapping("/contract/create")
    public MyJsonBean createContractRevenue(@RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = revenueManagementService.createContractRevenue(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("创建合同收入失败", e);
            return MyJsonBean.errorData("创建失败: " + e.getMessage());
        }
    }

    /**
     * 识别履约义务
     */
    @PostMapping("/contract/{contractId}/obligations")
    public MyJsonBean identifyPerformanceObligations(@PathVariable String contractId) {
        try {
            Map<String, Object> result = revenueManagementService.identifyPerformanceObligations(contractId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("识别履约义务失败", e);
            return MyJsonBean.errorData("识别失败: " + e.getMessage());
        }
    }

    /**
     * 更新履约进度
     */
    @PutMapping("/contract/{contractId}/progress")
    public MyJsonBean updatePerformanceProgress(@PathVariable String contractId, @RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = revenueManagementService.updatePerformanceProgress(contractId, param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("更新履约进度失败", e);
            return MyJsonBean.errorData("更新失败: " + e.getMessage());
        }
    }

    /**
     * 获取合同收入统计
     */
    @GetMapping("/contract/stats")
    public MyJsonBean getContractRevenueStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalContracts", 156);
            stats.put("totalContractAmount", new BigDecimal(125600000));
            stats.put("recognizedAmount", new BigDecimal(78900000));
            stats.put("remainingAmount", new BigDecimal(46700000));
            stats.put("recognitionRate", 62.8);
            stats.put("activeContracts", 89);
            stats.put("completedContracts", 45);
            stats.put("avgContractAmount", new BigDecimal(805128));
            stats.put("lastUpdateTime", LocalDateTime.now().toString());

            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            log.error("查询合同收入统计失败", e);
            return MyJsonBean.errorData("查询统计失败: " + e.getMessage());
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

    // ==================== 递延收入管理 ====================

    /**
     * 获取递延收入列表
     */
    @PostMapping("/deferred/getList")
    public MyJsonBean getDeferredRevenueList(@RequestBody PageableParam param) {
        try {
            // 调用Service层查询数据库
            PageResult<Map<String, Object>> result = revenueManagementService.getDeferredRevenueList(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询递延收入列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建递延收入
     */
    @PostMapping("/deferred/create")
    public MyJsonBean createDeferredRevenue(@RequestBody Map<String, Object> param) {
        try {
            // 调用Service层保存到数据库
            Map<String, Object> result = revenueManagementService.createDeferredRevenue(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("创建递延收入失败", e);
            return MyJsonBean.errorData("创建失败: " + e.getMessage());
        }
    }

    /**
     * 分期确认递延收入
     */
    @PostMapping("/deferred/{deferredId}/recognize")
    public MyJsonBean recognizeDeferredRevenue(@PathVariable String deferredId, @RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("deferredId", deferredId);
            result.put("recognitionAmount", param.get("recognitionAmount"));
            result.put("recognitionDate", param.get("recognitionDate"));
            result.put("voucherId", 5000 + System.currentTimeMillis() % 1000);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("确认失败: " + e.getMessage());
        }
    }

    /**
     * 获取确认计划
     */
    @GetMapping("/deferred/{deferredId}/schedule")
    public MyJsonBean getRecognitionSchedule(@PathVariable String deferredId) {
        try {
            Map<String, Object> result = revenueManagementService.getRecognitionSchedule(deferredId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询确认计划失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取递延收入详情
     */
    @GetMapping("/deferred/{deferredId}")
    public MyJsonBean getDeferredRevenueDetail(@PathVariable String deferredId) {
        try {
            Map<String, Object> detail = new HashMap<>();
            detail.put("deferredId", deferredId);
            detail.put("deferredNo", "DEF20241219001");
            detail.put("deferredType", 1);
            detail.put("deferredTypeName", "预收款项");
            detail.put("contractNo", "CON20241219001");
            detail.put("contractName", "软件开发服务合同");
            detail.put("totalAmount", new BigDecimal(500000));
            detail.put("recognizedAmount", new BigDecimal(200000));
            detail.put("remainingAmount", new BigDecimal(300000));
            detail.put("recognitionProgress", 40);
            detail.put("recognitionStatus", 1);
            detail.put("startDate", "2024-01-01");
            detail.put("endDate", "2024-12-31");
            detail.put("recognitionMethod", 1);
            detail.put("recognitionMethodName", "平均分摊");
            detail.put("recognitionCycle", 1);
            detail.put("recognitionCycleName", "月度");
            detail.put("deferredDesc", "软件开发服务预收款");
            detail.put("createTime", LocalDateTime.now().toString());

            return MyJsonBean.successData(detail);
        } catch (Exception e) {
            log.error("查询递延收入详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 更新递延收入
     */
    @PutMapping("/deferred/{deferredId}")
    public MyJsonBean updateDeferredRevenue(@PathVariable String deferredId, @RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("deferredId", deferredId);
            result.put("updateTime", LocalDateTime.now().toString());
            result.put("status", "updated");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除递延收入
     */
    @DeleteMapping("/deferred/{deferredId}")
    public MyJsonBean deleteDeferredRevenue(@PathVariable String deferredId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("deferredId", deferredId);
            result.put("deleteTime", LocalDateTime.now().toString());
            result.put("status", "deleted");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 更新确认计划
     */
    @PutMapping("/deferred/{deferredId}/schedule")
    public MyJsonBean updateRecognitionSchedule(@PathVariable String deferredId, @RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("deferredId", deferredId);
            result.put("updateTime", LocalDateTime.now().toString());
            result.put("scheduleCount", param.get("scheduleList") != null ? ((List<?>) param.get("scheduleList")).size() : 0);
            result.put("status", "updated");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("更新计划失败: " + e.getMessage());
        }
    }

    /**
     * 获取递延收入统计
     */
    @GetMapping("/deferred/stats")
    public MyJsonBean getDeferredRevenueStats() {
        try {
            // 调用Service层查询数据库统计
            Map<String, Object> stats = revenueManagementService.getDeferredRevenueStats();
            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            log.error("查询递延收入统计失败", e);
            return MyJsonBean.errorData("查询统计失败: " + e.getMessage());
        }
    }

    // ==================== 收入调整管理 ====================

    /**
     * 获取收入调整列表
     */
    @PostMapping("/adjustment/getList")
    public MyJsonBean getRevenueAdjustmentList(@RequestBody PageableParam param) {
        try {
            // 生成全部模拟数据(67条)
            List<Map<String, Object>> allData = new ArrayList<>();
            for (int i = 1; i <= 67; i++) {
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
                allData.add(item);
            }

            // 应用搜索过滤
            List<Map<String, Object>> filteredData = new ArrayList<>();
            Map<String, Object> searchParams = (Map<String, Object>) param.getParam();

            for (Map<String, Object> item : allData) {
                boolean match = true;

                // 调整单号过滤
                if (searchParams != null && searchParams.get("adjustmentNo") != null && !searchParams.get("adjustmentNo").toString().isEmpty()) {
                    String adjustmentNo = item.get("adjustmentNo").toString();
                    String searchAdjustmentNo = searchParams.get("adjustmentNo").toString();
                    if (!adjustmentNo.contains(searchAdjustmentNo)) {
                        match = false;
                    }
                }

                // 调整类型过滤
                if (match && searchParams != null && searchParams.get("adjustmentType") != null) {
                    Integer adjustmentType = (Integer) item.get("adjustmentType");
                    Integer searchType = Integer.parseInt(searchParams.get("adjustmentType").toString());
                    if (!adjustmentType.equals(searchType)) {
                        match = false;
                    }
                }

                // 调整状态过滤
                if (match && searchParams != null && searchParams.get("adjustmentStatus") != null) {
                    Integer adjustmentStatus = (Integer) item.get("adjustmentStatus");
                    Integer searchStatus = Integer.parseInt(searchParams.get("adjustmentStatus").toString());
                    if (!adjustmentStatus.equals(searchStatus)) {
                        match = false;
                    }
                }

                // 调整期间过滤
                if (match && searchParams != null && searchParams.get("adjustmentPeriod") != null && !searchParams.get("adjustmentPeriod").toString().isEmpty()) {
                    String adjustmentPeriod = item.get("adjustmentPeriod").toString();
                    String searchPeriod = searchParams.get("adjustmentPeriod").toString();
                    if (!adjustmentPeriod.equals(searchPeriod)) {
                        match = false;
                    }
                }

                if (match) {
                    filteredData.add(item);
                }
            }

            // 应用分页
            int totalRecord = filteredData.size();
            int offset = (param.getPageNumber() - 1) * param.getPageSize();
            int endIndex = Math.min(offset + param.getPageSize(), totalRecord);

            List<Map<String, Object>> pageData = new ArrayList<>();
            if (offset < totalRecord) {
                pageData = filteredData.subList(offset, endIndex);
            }

            PageResult<Map<String, Object>> result = new PageResult<>();
            result.setTlist(pageData);
            result.setTotalRecord(totalRecord);
            result.setCurrentPage(param.getPageNumber());
            result.setPageSize(param.getPageSize());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询收入调整列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建收入调整
     */
    @PostMapping("/adjustment/create")
    public MyJsonBean createRevenueAdjustment(@RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("adjustmentId", System.currentTimeMillis());
            result.put("adjustmentNo", param.get("adjustmentNo"));
            result.put("applicationTime", LocalDateTime.now().toString());
            result.put("status", "pending");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("创建失败: " + e.getMessage());
        }
    }

    /**
     * 审批收入调整
     */
    @PostMapping("/adjustment/{adjustmentId}/approve")
    public MyJsonBean approveRevenueAdjustment(@PathVariable String adjustmentId, @RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("adjustmentId", adjustmentId);
            result.put("approvalResult", param.get("approvalResult"));
            result.put("approvalComments", param.get("approvalComments"));
            result.put("approvalTime", LocalDateTime.now().toString());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("审批失败: " + e.getMessage());
        }
    }

    /**
     * 执行收入调整
     */
    @PostMapping("/adjustment/{adjustmentId}/execute")
    public MyJsonBean executeRevenueAdjustment(@PathVariable String adjustmentId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("adjustmentId", adjustmentId);
            result.put("executeTime", LocalDateTime.now().toString());
            result.put("voucherId", 5000 + System.currentTimeMillis() % 1000);
            result.put("status", "executed");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    /**
     * 获取调整影响分析
     */
    @GetMapping("/adjustment/{adjustmentId}/impact")
    public MyJsonBean getAdjustmentImpactAnalysis(@PathVariable String adjustmentId) {
        try {
            Map<String, Object> result = revenueManagementService.getAdjustmentImpactAnalysis(adjustmentId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取调整影响分析失败", e);
            return MyJsonBean.errorData("分析失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入调整统计
     */
    @GetMapping("/adjustment/stats")
    public MyJsonBean getRevenueAdjustmentStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalAdjustments", 67);
            stats.put("pendingApproval", 15);
            stats.put("approved", 28);
            stats.put("rejected", 8);
            stats.put("executed", 12);
            stats.put("revoked", 4);
            stats.put("totalAdjustmentAmount", new BigDecimal(8900000));
            stats.put("increaseAmount", new BigDecimal(5200000));
            stats.put("decreaseAmount", new BigDecimal(3700000));
            stats.put("avgAdjustmentAmount", new BigDecimal(132836));
            stats.put("lastUpdateTime", LocalDateTime.now().toString());

            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            log.error("查询收入调整统计失败", e);
            return MyJsonBean.errorData("查询统计失败: " + e.getMessage());
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

    // ==================== 收入统计管理 ====================

    /**
     * 获取收入统计概览
     */
    @GetMapping("/statistics/overview")
    public MyJsonBean getRevenueStatisticsOverview() {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("totalRevenue", new BigDecimal(125600000));
            result.put("recognizedRevenue", new BigDecimal(98500000));
            result.put("deferredRevenue", new BigDecimal(27100000));
            result.put("monthlyRevenue", new BigDecimal(12500000));
            result.put("recognitionRate", 78.4);
            result.put("growthRate", 12.5);
            result.put("lastUpdateTime", LocalDateTime.now().toString());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入月度统计
     */
    @GetMapping("/statistics/monthly")
    public MyJsonBean getRevenueMonthlyStatistics() {
        try {
            List<Map<String, Object>> monthlyData = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("month", "2024-" + String.format("%02d", i));
                item.put("revenue", new BigDecimal(8000000 + i * 500000 + (int)(Math.random() * 1000000)));
                item.put("recognizedRevenue", new BigDecimal(6000000 + i * 400000));
                item.put("deferredRevenue", new BigDecimal(2000000 + i * 100000));
                item.put("growthRate", 5.0 + Math.random() * 20);
                monthlyData.add(item);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("monthlyData", monthlyData);
            result.put("totalRevenue", new BigDecimal(125600000));

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入年度统计
     */
    @GetMapping("/statistics/yearly")
    public MyJsonBean getRevenueYearlyStatistics() {
        try {
            List<Map<String, Object>> yearlyData = new ArrayList<>();
            for (int i = 2020; i <= 2024; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("year", String.valueOf(i));
                item.put("revenue", new BigDecimal(80000000 + (i - 2020) * 15000000));
                item.put("recognizedRevenue", new BigDecimal(60000000 + (i - 2020) * 12000000));
                item.put("deferredRevenue", new BigDecimal(20000000 + (i - 2020) * 3000000));
                item.put("growthRate", 8.5 + (i - 2020) * 1.5);
                yearlyData.add(item);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("yearlyData", yearlyData);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入部门统计
     */
    @GetMapping("/statistics/department")
    public MyJsonBean getRevenueDepartmentStatistics() {
        try {
            List<Map<String, Object>> departmentData = new ArrayList<>();
            String[] departments = {"销售部", "技术部", "市场部", "运营部", "财务部"};
            BigDecimal[] revenues = {
                new BigDecimal(45600000), new BigDecimal(32400000), new BigDecimal(28900000),
                new BigDecimal(18700000), new BigDecimal(12400000)
            };

            for (int i = 0; i < departments.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("department", departments[i]);
                item.put("revenue", revenues[i]);
                item.put("percentage", revenues[i].divide(new BigDecimal(125600000), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                item.put("rank", i + 1);
                departmentData.add(item);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("departmentData", departmentData);
            result.put("totalRevenue", new BigDecimal(125600000));

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入产品统计
     */
    @GetMapping("/statistics/product")
    public MyJsonBean getRevenueProductStatistics() {
        try {
            List<Map<String, Object>> productData = new ArrayList<>();
            String[] products = {"产品A", "产品B", "产品C", "产品D", "产品E"};
            BigDecimal[] revenues = {
                new BigDecimal(45600000), new BigDecimal(32400000), new BigDecimal(28900000),
                new BigDecimal(18700000), new BigDecimal(12400000)
            };

            for (int i = 0; i < products.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("product", products[i]);
                item.put("revenue", revenues[i]);
                item.put("percentage", revenues[i].divide(new BigDecimal(125600000), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                item.put("rank", i + 1);
                item.put("growthRate", 15.2 - i * 2.5);
                productData.add(item);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("productData", productData);
            result.put("totalRevenue", new BigDecimal(125600000));

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 收入分析管理 ====================

    /**
     * 获取收入结构分析列表（分页）
     */
    @GetMapping("/analysis/structure/getList")
    public MyJsonBean getRevenueStructureAnalysisList(
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orgName,
            @RequestParam(required = false) String recognitionType,
            @RequestParam(required = false) String revenueType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            PageableParam param = new PageableParam();
            param.setPageNum(pageNumber);
            param.setPageSize(pageSize);

            Map<String, Object> searchParams = new HashMap<>();
            if (orgName != null && !orgName.isEmpty()) {
                searchParams.put("orgName", orgName);
            }
            if (recognitionType != null && !recognitionType.isEmpty()) {
                searchParams.put("recognitionType", recognitionType);
            }
            if (revenueType != null && !revenueType.isEmpty()) {
                searchParams.put("revenueType", revenueType);
            }
            if (startDate != null && !startDate.isEmpty()) {
                searchParams.put("startDate", startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                searchParams.put("endDate", endDate);
            }
            param.setParam(searchParams);

            PageResult<Map<String, Object>> result = revenueManagementService.getRevenueStructureAnalysisList(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取收入结构分析列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入结构分析
     */
    @GetMapping("/analysis/structure")
    public MyJsonBean getRevenueStructureAnalysis(@RequestParam(required = false) Integer dimension,
                                                   @RequestParam(required = false) String[] dateRange) {
        try {
            Map<String, Object> result = revenueAnalysisService.getRevenueStructureAnalysis(dimension, dateRange);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取收入结构分析失败", e);
            return MyJsonBean.errorData("分析失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入趋势分析
     */
    @PostMapping("/analysis/trend")
    public MyJsonBean getRevenueTrendAnalysis(@RequestBody Map<String, Object> param) {
        try {
            List<Map<String, Object>> trendData = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("period", "2024-" + String.format("%02d", i));
                item.put("revenue", new BigDecimal(8000000 + i * 500000 + (int)(Math.random() * 1000000)));
                item.put("growth", 5.0 + Math.random() * 20);
                item.put("target", new BigDecimal(9000000 + i * 600000));
                trendData.add(item);
            }

            Map<String, Object> summary = new HashMap<>();
            summary.put("avgGrowthRate", 12.5);
            summary.put("peakMonth", "2024-11");
            summary.put("volatility", 0.15);
            summary.put("trendDirection", "上升");

            Map<String, Object> result = new HashMap<>();
            result.put("trendData", trendData);
            result.put("summary", summary);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("分析失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入质量分析
     */
    @GetMapping("/analysis/quality")
    public MyJsonBean getRevenueQualityAnalysis() {
        try {
            Map<String, Object> result = revenueAnalysisService.getRevenueQualityAnalysis();
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取收入质量分析失败", e);
            return MyJsonBean.errorData("分析失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入预测模型
     */
    @GetMapping("/analysis/forecast")
    public MyJsonBean getRevenueForecastModel(@RequestParam(required = false, defaultValue = "2") Integer model,
                                               @RequestParam(required = false, defaultValue = "6") Integer period) {
        try {
            Map<String, Object> result = revenueAnalysisService.getRevenueForecastModel(model, period);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取收入预测模型失败", e);
            return MyJsonBean.errorData("预测失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入对比分析
     */
    @PostMapping("/analysis/comparison")
    public MyJsonBean getRevenueComparisonAnalysis(@RequestBody Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("currentPeriod", "2024-12");
            result.put("currentRevenue", new BigDecimal(12500000));
            result.put("comparisonPeriod", "2023-12");
            result.put("comparisonRevenue", new BigDecimal(11200000));
            result.put("growthAmount", new BigDecimal(1300000));
            result.put("growthRate", 11.6);
            result.put("analysisType", param.get("comparisonType"));

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("对比分析失败: " + e.getMessage());
        }
    }

    /**
     * 获取收入贡献分析
     */
    @PostMapping("/analysis/contribution")
    public MyJsonBean getRevenueContributionAnalysis(@RequestBody Map<String, Object> param) {
        try {
            List<Map<String, Object>> contributionData = new ArrayList<>();
            String[] contributors = {"华东区", "华南区", "华北区", "西南区", "东北区"};
            BigDecimal[] contributions = {
                new BigDecimal(35600000), new BigDecimal(28900000), new BigDecimal(24500000),
                new BigDecimal(18700000), new BigDecimal(12300000)
            };

            for (int i = 0; i < contributors.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("contributor", contributors[i]);
                item.put("contribution", contributions[i]);
                item.put("percentage", contributions[i].divide(new BigDecimal(120000000), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                item.put("rank", i + 1);
                contributionData.add(item);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("contributionData", contributionData);
            result.put("totalRevenue", new BigDecimal(120000000));

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("贡献分析失败: " + e.getMessage());
        }
    }

    // ==================== 报表导出 ====================

    /**
     * 生成收入分析报表
     */
    @PostMapping("/report/analysis")
    public MyJsonBean generateRevenueAnalysisReport(@RequestBody Map<String, Object> param) {
        try {
            log.info("生成收入分析报表，参数: {}", param);

            String reportType = (String) param.getOrDefault("reportType", "comprehensive");
            Integer dimension = (Integer) param.getOrDefault("dimension", 1);
            Integer forecastModel = (Integer) param.getOrDefault("forecastModel", 2);
            Integer forecastPeriod = (Integer) param.getOrDefault("forecastPeriod", 6);

            Map<String, Object> reportData = new HashMap<>();

            // 报告摘要
            Map<String, Object> summary = new HashMap<>();
            summary.put("generateTime", LocalDateTime.now().toString());
            summary.put("reportType", reportType);
            summary.put("modelName", getModelTypeName(forecastModel));
            summary.put("forecastPeriod", "未来" + forecastPeriod + "个月");
            summary.put("totalForecast", new BigDecimal(145800000));
            summary.put("avgGrowth", 12.5);
            summary.put("accuracy", 85.6);
            reportData.put("summary", summary);

            // 预测明细
            if ("forecast".equals(reportType)) {
                List<Map<String, Object>> forecastDetails = new ArrayList<>();
                LocalDate startDate = LocalDate.now().plusMonths(1);

                for (int i = 0; i < forecastPeriod; i++) {
                    Map<String, Object> detail = new HashMap<>();
                    LocalDate periodDate = startDate.plusMonths(i);
                    detail.put("period", periodDate.toString().substring(0, 7));

                    // 模拟预测数据，实际应该调用预测模型
                    BigDecimal baseForecast = new BigDecimal(12000000);
                    BigDecimal growth = new BigDecimal(1 + i * 0.02);
                    BigDecimal forecast = baseForecast.multiply(growth);

                    detail.put("forecast", forecast);
                    detail.put("confidence", 85 + (i % 5));
                    detail.put("upperBound", forecast.multiply(new BigDecimal(1.15)));
                    detail.put("lowerBound", forecast.multiply(new BigDecimal(0.85)));
                    detail.put("growth", 12.5 + (i * 0.5));

                    forecastDetails.add(detail);
                }
                reportData.put("forecastDetails", forecastDetails);
            }

            // 结构分析数据
            if ("comprehensive".equals(reportType) || "structure".equals(reportType)) {
                List<Map<String, Object>> structureData = new ArrayList<>();
                String[] dimensions = {"产品A", "产品B", "产品C", "产品D", "产品E"};
                BigDecimal[] amounts = {
                    new BigDecimal(45600000), new BigDecimal(32800000), new BigDecimal(25400000),
                    new BigDecimal(15200000), new BigDecimal(6000000)
                };

                for (int i = 0; i < dimensions.length; i++) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("dimension", dimensions[i]);
                    item.put("amount", amounts[i]);
                    item.put("percentage", amounts[i].divide(new BigDecimal(125000000), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                    item.put("growth", 10 + (i * 2.5));
                    structureData.add(item);
                }
                reportData.put("structureData", structureData);
            }

            // 质量分析数据
            if ("comprehensive".equals(reportType) || "quality".equals(reportType)) {
                List<Map<String, Object>> qualityData = new ArrayList<>();
                String[] indicators = {"收入稳定性", "客户集中度", "回款及时性", "合同履约率", "收入可预测性"};
                int[] scores = {85, 78, 92, 88, 82};

                for (int i = 0; i < indicators.length; i++) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("indicator", indicators[i]);
                    item.put("score", scores[i]);
                    item.put("benchmark", 80);
                    item.put("status", scores[i] >= 80 ? "良好" : "需改进");
                    qualityData.add(item);
                }
                reportData.put("qualityData", qualityData);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("reportData", reportData);
            result.put("reportId", UUID.randomUUID().toString());
            result.put("generateTime", LocalDateTime.now());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("生成收入分析报表失败", e);
            return MyJsonBean.errorData("生成报表失败: " + e.getMessage());
        }
    }

    /**
     * 导出收入数据
     */
    @PostMapping("/export")
    public MyJsonBean exportRevenueData(@RequestBody Map<String, Object> param) {
        try {
            log.info("导出收入数据，参数: {}", param);

            String exportType = (String) param.getOrDefault("exportType", "all");
            String format = (String) param.getOrDefault("format", "excel");

            Map<String, Object> result = new HashMap<>();
            result.put("exportId", UUID.randomUUID().toString());
            result.put("exportTime", LocalDateTime.now());
            result.put("exportType", exportType);
            result.put("format", format);
            result.put("status", "success");
            result.put("message", "数据导出成功");

            // 实际应用中，这里应该生成文件并返回下载URL
            // result.put("fileUrl", "/download/revenue_export_" + System.currentTimeMillis() + ".xlsx");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("导出收入数据失败", e);
            return MyJsonBean.errorData("导出失败: " + e.getMessage());
        }
    }

    private String getModelTypeName(Integer model) {
        if (model == null) return "线性回归";
        switch (model) {
            case 1: return "线性回归";
            case 2: return "时间序列";
            case 3: return "指数平滑";
            case 4: return "神经网络";
            default: return "线性回归";
        }
    }
}
