package com.global.treasurer.controller;

import com.hbfk.util.JsonBean;
import com.global.treasurer.dto.CreditLimitDTO;
import com.global.treasurer.dto.CreditLimitQueryDTO;
import com.global.treasurer.dto.FinancialLeaseDTO;
import com.global.treasurer.dto.FinancialLeaseQueryDTO;
import com.global.treasurer.dto.FinancingPlanQueryDTO;
import com.global.treasurer.entity.TblCreditLimit;
import com.global.treasurer.entity.TblFinancialLease;
import com.global.treasurer.entity.TblFinancingPlan;
import com.global.treasurer.entity.TblGuaranteeApplication;
import com.global.treasurer.entity.TblLeaseAsset;
import com.global.treasurer.entity.TblLeasePayment;
import com.global.treasurer.entity.TblLeaseReturn;
import com.global.treasurer.mapper.CreditLimitMapper;
import com.global.treasurer.mapper.FinancingPlanMapper;
import com.global.treasurer.mapper.GuaranteeApplicationMapper;
import com.global.treasurer.service.CreditLimitService;
import com.global.treasurer.service.FinancialLeaseService;
import com.global.treasurer.service.FinancingMonitoringService;
import com.global.treasurer.service.FinancingPlanService;
import com.global.treasurer.service.GuaranteeMonitorService;
import com.global.treasurer.service.LeaseAssetService;
import com.global.treasurer.service.LeasePaymentService;
import com.global.treasurer.service.LeaseReturnService;
import com.global.treasurer.exception.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
// import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // 已移除,使用手动声明
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;

/**
 * 融资管理模块Controller
 * 融资管理相关接口，连接数据库查询真实数据
 */
// // 已移除,使用手动声明
@RestController
@RequestMapping("/financial/rzgl")
@Api(tags = "融资管理模块")
public class RzglController {

    private static final Logger log = LoggerFactory.getLogger(RzglController.class);

    @Autowired
    private GuaranteeMonitorService guaranteeMonitorService;

    @Autowired
    private FinancingMonitoringService financingMonitoringService;

    @Autowired
    private CreditLimitService creditLimitService;

    @Autowired
    private CreditLimitMapper creditLimitMapper;

    @Autowired
    private GuaranteeApplicationMapper guaranteeApplicationMapper;

    @Autowired
    private FinancingPlanService financingPlanService;

    @Autowired
    private FinancingPlanMapper financingPlanMapper;

    @Autowired
    private FinancialLeaseService financialLeaseService;

    @Autowired
    private LeaseAssetService leaseAssetService;

    @Autowired
    private LeasePaymentService leasePaymentService;

    @Autowired
    private LeaseReturnService leaseReturnService;

    // ==================== 担保申请管理 ====================

    @PostMapping("/guarantee/application/page")
    @ApiOperation("担保申请分页查询")
    public String getGuaranteeApplicationPage(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "20") Integer limit,
            @ApiParam(value = "申请编号") @RequestParam(required = false) String applicationNo,
            @ApiParam(value = "担保类型") @RequestParam(required = false) String guaranteeType,
            @ApiParam(value = "申请状态") @RequestParam(required = false) String applicationStatus) {
        try {
            // 使用PageHelper进行分页
            PageHelper.startPage(page, limit);

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            params.put("applicationNo", applicationNo);
            params.put("guaranteeType", guaranteeType);
            params.put("applicationStatus", applicationStatus);

            // 查询数据库
            List<TblGuaranteeApplication> list = guaranteeApplicationMapper.selectApplicationList(params);
            PageInfo<TblGuaranteeApplication> pageInfo = new PageInfo<>(list);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", page);
            data.put("pageSize", limit);

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("担保申请分页查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @GetMapping("/guarantee/application/detail")
    @ApiOperation("担保申请详情")
    public String getGuaranteeApplicationDetail(@RequestParam Long id) {
        try {
            TblGuaranteeApplication application = guaranteeApplicationMapper.selectApplicationById(id);
            if (application != null) {
                return new JsonBean(1, "查询成功", application).toString();
            } else {
                return new JsonBean(0, "未找到对应的担保申请记录", null).toString();
            }
        } catch (Exception e) {
            log.error("担保申请详情查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @GetMapping("/guarantee/application/{id}")
    @ApiOperation("根据ID查询担保申请详情")
    public String getGuaranteeApplicationById(@PathVariable String id) {
        try {
            log.info("查询担保申请详情, 传入ID字符串: [{}]", id);
            Long applicationId = Long.parseLong(id);
            log.info("转换后的Long ID: [{}]", applicationId);

            // 先查询所有记录，打印所有ID进行对比
            Map<String, Object> params = new java.util.HashMap<>();
            java.util.List<TblGuaranteeApplication> allList = guaranteeApplicationMapper.selectApplicationList(params);
            log.info("数据库中所有记录数: {}", allList.size());
            for (TblGuaranteeApplication app : allList) {
                log.info("数据库记录 - ID: [{}], 编号: [{}], ID匹配: {}",
                    app.getApplicationId(),
                    app.getApplicationNo(),
                    app.getApplicationId() != null && app.getApplicationId().equals(applicationId));
            }

            TblGuaranteeApplication application = guaranteeApplicationMapper.selectApplicationById(applicationId);
            log.info("查询结果: {}", application);
            if (application != null) {
                return new JsonBean(1, "查询成功", application).toString();
            } else {
                // 返回数据库中所有记录的ID供调试
                String allIds = allList.stream()
                    .map(a -> String.valueOf(a.getApplicationId()))
                    .collect(java.util.stream.Collectors.joining(", "));
                return new JsonBean(0, "未找到ID=" + id + "的记录，数据库中共有" + allList.size() + "条记录，ID列表: " + allIds, null).toString();
            }
        } catch (NumberFormatException e) {
            log.error("担保申请ID格式错误, id: {}", id, e);
            return new JsonBean(0, "ID格式错误", null).toString();
        } catch (Exception e) {
            log.error("担保申请详情查询失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/guarantee/application")
    @ApiOperation("新增担保申请")
    public String addGuaranteeApplication(@FlexibleRequestBody TblGuaranteeApplication application) {
        try {
            log.info("新增担保申请, 接收到的数据: {}", application);
            // 生成主键ID（使用雪花算法）
            if (application.getApplicationId() == null) {
                Long newId = com.baomidou.mybatisplus.core.toolkit.IdWorker.getId();
                application.setApplicationId(newId);
                log.info("生成的主键ID: {}", newId);
            }
            // 设置默认值
            if (application.getApplicationStatus() == null) {
                application.setApplicationStatus("DRAFT");
            }
            application.setDeleteFlag(0);
            application.setCreatedTime(new java.util.Date());
            application.setUpdatedTime(new java.util.Date());
            // 生成申请编号
            if (application.getApplicationNo() == null || application.getApplicationNo().isEmpty()) {
                application.setApplicationNo("GA" + new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()) + String.format("%04d", (int)(Math.random() * 10000)));
            }
            // 字段映射：前端 guaranteedParty -> 数据库 GUARANTOR_NAME
            if (application.getGuaranteedParty() != null && application.getGuarantorName() == null) {
                application.setGuarantorName(application.getGuaranteedParty());
            }
            // 设置申请日期
            if (application.getApplyDate() == null) {
                application.setApplyDate(new java.util.Date());
            }
            log.info("准备插入数据库, applicationId: {}, applicationNo: {}", application.getApplicationId(), application.getApplicationNo());
            guaranteeApplicationMapper.insert(application);
            log.info("插入成功, 返回的applicationId: {}", application.getApplicationId());
            return new JsonBean(1, "新增成功", application).toString();
        } catch (Exception e) {
            log.error("新增担保申请失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toString();
        }
    }

    @PutMapping("/guarantee/application")
    @ApiOperation("修改担保申请")
    public String updateGuaranteeApplication(@FlexibleRequestBody TblGuaranteeApplication application) {
        try {
            log.info("修改担保申请, applicationId: {}, guaranteeType: {}, guaranteeAmount: {}, guaranteedParty: {}, guarantorName: {}",
                application.getApplicationId(), application.getGuaranteeType(), application.getGuaranteeAmount(),
                application.getGuaranteedParty(), application.getGuarantorName());
            application.setUpdatedTime(new java.util.Date());
            // 字段映射：前端 guaranteedParty -> 数据库 GUARANTOR_NAME
            if (application.getGuaranteedParty() != null && application.getGuarantorName() == null) {
                application.setGuarantorName(application.getGuaranteedParty());
                log.info("字段映射: guaranteedParty -> guarantorName = {}", application.getGuarantorName());
            }
            // 使用自定义的 updateApplication 方法，避免与 MyBatis-Plus 的 updateById 冲突
            int rows = guaranteeApplicationMapper.updateApplication(application);
            log.info("更新影响行数: {}", rows);
            if (rows > 0) {
                return new JsonBean(1, "修改成功", application).toString();
            } else {
                return new JsonBean(0, "修改失败: 未找到对应记录或无数据变更", null).toString();
            }
        } catch (Exception e) {
            log.error("修改担保申请失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toString();
        }
    }

    @DeleteMapping("/guarantee/application/{id}")
    @ApiOperation("删除担保申请")
    public String deleteGuaranteeApplication(@PathVariable String id) {
        try {
            log.info("删除担保申请, id: {}", id);
            Long applicationId = Long.parseLong(id);
            // 逻辑删除
            TblGuaranteeApplication application = guaranteeApplicationMapper.selectApplicationById(applicationId);
            if (application != null) {
                application.setDeleteFlag(1);
                application.setUpdatedTime(new java.util.Date());
                // 使用自定义的 updateApplication 方法
                guaranteeApplicationMapper.updateApplication(application);
                return new JsonBean(1, "删除成功").toString();
            } else {
                return new JsonBean(0, "未找到对应的担保申请记录").toString();
            }
        } catch (Exception e) {
            log.error("删除担保申请失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toString();
        }
    }

    @DeleteMapping("/guarantee/application/batch")
    @ApiOperation("批量删除担保申请")
    public String batchDeleteGuaranteeApplication(@RequestParam(value = "ids", required = false) List<String> ids) {
        try {
            log.info("批量删除担保申请, ids: {}", ids);
            for (String id : ids) {
                Long applicationId = Long.parseLong(id);
                TblGuaranteeApplication application = guaranteeApplicationMapper.selectApplicationById(applicationId);
                if (application != null) {
                    application.setDeleteFlag(1);
                    application.setUpdatedTime(new java.util.Date());
                    // 使用自定义的 updateApplication 方法
                    guaranteeApplicationMapper.updateApplication(application);
                }
            }
            return new JsonBean(1, "批量删除成功").toString();
        } catch (Exception e) {
            log.error("批量删除担保申请失败, ids: {}", ids, e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/guarantee/application/{id}/submit")
    @ApiOperation("提交担保申请审批")
    public String submitGuaranteeApplication(@PathVariable String id) {
        try {
            log.info("提交担保申请审批, id: {}", id);
            Long applicationId = Long.parseLong(id);
            guaranteeApplicationMapper.updateApplicationStatus(applicationId, "PENDING_APPROVAL");
            return new JsonBean(1, "提交成功").toString();
        } catch (Exception e) {
            log.error("提交担保申请审批失败, id: {}", id, e);
            return new JsonBean(0, "提交失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/guarantee/application/{id}/approve")
    @ApiOperation("审批通过担保申请")
    public String approveGuaranteeApplication(@PathVariable String id, @RequestParam(required = false) String approvalComments) {
        try {
            log.info("审批通过担保申请, id: {}, comments: {}", id, approvalComments);
            Long applicationId = Long.parseLong(id);
            guaranteeApplicationMapper.updateApplicationStatus(applicationId, "APPROVED");
            return new JsonBean(1, "审批通过").toString();
        } catch (Exception e) {
            log.error("审批通过担保申请失败, id: {}", id, e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/guarantee/application/{id}/reject")
    @ApiOperation("审批拒绝担保申请")
    public String rejectGuaranteeApplication(@PathVariable String id, @RequestParam(required = false) String rejectReason) {
        try {
            log.info("审批拒绝担保申请, id: {}, reason: {}", id, rejectReason);
            Long applicationId = Long.parseLong(id);
            guaranteeApplicationMapper.updateApplicationStatus(applicationId, "REJECTED");
            return new JsonBean(1, "已拒绝").toString();
        } catch (Exception e) {
            log.error("审批拒绝担保申请失败, id: {}", id, e);
            return new JsonBean(0, "拒绝失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/guarantee/application/{id}/withdraw")
    @ApiOperation("撤回担保申请")
    public String withdrawGuaranteeApplication(@PathVariable String id) {
        try {
            log.info("撤回担保申请, id: {}", id);
            Long applicationId = Long.parseLong(id);
            guaranteeApplicationMapper.updateApplicationStatus(applicationId, "DRAFT");
            return new JsonBean(1, "撤回成功").toString();
        } catch (Exception e) {
            log.error("撤回担保申请失败, id: {}", id, e);
            return new JsonBean(0, "撤回失败: " + e.getMessage(), null).toString();
        }
    }

    // ==================== 担保合同管理 ====================

    @PostMapping("/guarantee/contract/page")
    @ApiOperation("担保合同分页查询")
    public String getGuaranteeContractPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        Map<String, Object> data = new HashMap<>();
        data.put("tlist", new ArrayList<>());
        data.put("totalRecord", 0);
        data.put("pageNo", page);
        data.put("pageSize", limit);
        return new JsonBean(1, "查询成功（临时数据）", data).toString();
    }

    @GetMapping("/guarantee/contract/detail")
    @ApiOperation("担保合同详情")
    public String getGuaranteeContractDetail(@RequestParam Long id) {
        return new JsonBean(1, "查询成功（临时数据）", new HashMap<>()).toString();
    }

    // ==================== 授信申请管理 ====================
    // 注意：授信申请相关接口已迁移到 CreditApplicationController，避免路径冲突

    // ==================== 授信合同管理 ====================
    // 注意：授信合同相关接口已迁移到 CreditContractController，避免路径冲突

    // ==================== 授信额度管理 ====================

    @PostMapping("/credit/limit/page")
    @ApiOperation("授信额度分页查询")
    public String getCreditLimitPage(@FlexibleRequestBody CreditLimitQueryDTO queryDTO) {
        try {
            log.info("授信额度分页查询, queryDTO: {}", queryDTO);
            PageInfo<TblCreditLimit> pageInfo = creditLimitService.getLimitList(queryDTO);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("授信额度分页查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/limit/detail")
    @ApiOperation("授信额度详情")
    public String getCreditLimitDetail(@RequestParam Long id) {
        try {
            log.info("查询授信额度详情, id: {}", id);
            TblCreditLimit limit = creditLimitService.getLimitById(id);
            return new JsonBean(1, "查询成功", limit).toString();
        } catch (Exception e) {
            log.error("查询授信额度详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping({"/credit/limit/add", "/credit/limit"})
    @ApiOperation("新增授信额度")
    public String addCreditLimit(@FlexibleRequestBody CreditLimitDTO dto) {
        try {
            log.info("新增授信额度, dto: {}", dto);
            TblCreditLimit limit = creditLimitService.saveLimit(dto);
            return new JsonBean(1, "新增成功", limit).toString();
        } catch (Exception e) {
            log.error("新增授信额度失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/credit/limit/adjust")
    @ApiOperation("调整授信额度")
    public String adjustCreditLimit(@FlexibleRequestBody CreditLimitDTO dto) {
        try {
            log.info("调整授信额度, dto: {}", dto);
            TblCreditLimit limit = creditLimitService.saveLimit(dto);
            return new JsonBean(1, "调整成功", limit).toString();
        } catch (Exception e) {
            log.error("调整授信额度失败", e);
            return new JsonBean(0, "调整失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/credit/limit/freeze/{id}")
    @ApiOperation("冻结授信额度")
    public String freezeCreditLimit(@PathVariable Long id) {
        try {
            log.info("冻结授信额度, id: {}", id);
            // TODO: 功能待实现，需要传入冻结金额参数
            return new JsonBean(1, "冻结成功（功能待完善）").toString();
        } catch (Exception e) {
            log.error("冻结授信额度失败, id: {}", id, e);
            return new JsonBean(0, "冻结失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/credit/limit/unfreeze/{id}")
    @ApiOperation("解冻授信额度")
    public String unfreezeCreditLimit(@PathVariable Long id) {
        try {
            log.info("解冻授信额度, id: {}", id);
            // TODO: 功能待实现，需要传入解冻金额参数
            return new JsonBean(1, "解冻成功（功能待完善）").toString();
        } catch (Exception e) {
            log.error("解冻授信额度失败, id: {}", id, e);
            return new JsonBean(0, "解冻失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/limit/{id}")
    @ApiOperation("根据ID查询授信额度详情")
    public String getCreditLimitById(@PathVariable Long id) {
        try {
            log.info("查询授信额度详情, id: {}", id);
            TblCreditLimit limit = creditLimitService.getLimitById(id);
            return new JsonBean(1, "查询成功", limit).toString();
        } catch (Exception e) {
            log.error("查询授信额度详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/credit/limit/{id}")
    @ApiOperation("更新授信额度")
    public String updateCreditLimit(@PathVariable Long id, @FlexibleRequestBody CreditLimitDTO dto) {
        try {
            log.info("更新授信额度, id: {}, dto: {}", id, dto);
            if (dto.getLimitId() == null) {
                dto.setLimitId(id);
            }
            TblCreditLimit limit = creditLimitService.saveLimit(dto);
            return new JsonBean(1, "更新成功", limit).toString();
        } catch (Exception e) {
            log.error("更新授信额度失败, id: {}", id, e);
            return new JsonBean(0, "更新失败: " + e.getMessage()).toString();
        }
    }

    @DeleteMapping("/credit/limit/{id}")
    @ApiOperation("删除授信额度")
    public String deleteCreditLimit(@PathVariable Long id) {
        try {
            log.info("删除授信额度, id: {}", id);
            creditLimitService.deleteLimit(id);
            return new JsonBean(1, "删除成功").toString();
        } catch (Exception e) {
            log.error("删除授信额度失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage()).toString();
        }
    }

    @DeleteMapping("/credit/limit/batch")
    @ApiOperation("批量删除授信额度")
    public String batchDeleteCreditLimit(@RequestParam(value = "ids", required = false) List<Long> ids) {
        try {
            log.info("批量删除授信额度, ids: {}", ids);
            creditLimitService.batchDeleteLimits(ids);
            return new JsonBean(1, "批量删除成功").toString();
        } catch (Exception e) {
            log.error("批量删除授信额度失败, ids: {}", ids, e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/limit/contract/{contractId}")
    @ApiOperation("根据合同ID查询授信额度列表")
    public String getCreditLimitByContract(@PathVariable Long contractId) {
        try {
            log.info("根据合同ID查询授信额度, contractId: {}", contractId);
            java.util.List<TblCreditLimit> limits = creditLimitService.getLimitsByContractId(contractId);
            return new JsonBean(1, "查询成功", limits).toString();
        } catch (Exception e) {
            log.error("根据合同ID查询授信额度失败, contractId: {}", contractId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/limit/company/{companyId}")
    @ApiOperation("根据公司ID查询授信额度列表")
    public String getCreditLimitByCompany(@PathVariable Long companyId) {
        try {
            log.info("根据公司ID查询授信额度, companyId: {}", companyId);
            // 使用分页查询，设置公司ID条件
            CreditLimitQueryDTO queryDTO = new CreditLimitQueryDTO();
            queryDTO.setCompanyId(companyId);
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(100);
            PageInfo<TblCreditLimit> pageInfo = creditLimitService.getLimitList(queryDTO);
            return new JsonBean(1, "查询成功", pageInfo.getList()).toString();
        } catch (Exception e) {
            log.error("根据公司ID查询授信额度失败, companyId: {}", companyId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/limit/export")
    @ApiOperation("导出授信额度列表")
    public String exportCreditLimits(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            log.info("导出授信额度列表, pageNum: {}, pageSize: {}", pageNum, pageSize);
            CreditLimitQueryDTO queryDTO = new CreditLimitQueryDTO();
            queryDTO.setPageNum(pageNum);
            queryDTO.setPageSize(1000); // 导出时获取更多数据
            PageInfo<TblCreditLimit> pageInfo = creditLimitService.getLimitList(queryDTO);
            return new JsonBean(1, "导出成功", pageInfo.getList()).toString();
        } catch (Exception e) {
            log.error("导出授信额度列表失败", e);
            return new JsonBean(0, "导出失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/limit/{id}/use")
    @ApiOperation("使用授信额度")
    public String useCreditLimit(
            @PathVariable Long id,
            @RequestParam java.math.BigDecimal amount,
            @RequestParam(required = false) String purpose) {
        try {
            log.info("使用授信额度, id: {}, amount: {}, purpose: {}", id, amount, purpose);
            // 获取当前额度信息
            TblCreditLimit limit = creditLimitService.getLimitById(id);
            if (limit == null) {
                return new JsonBean(0, "授信额度不存在").toString();
            }
            // 检查可用额度是否足够
            java.math.BigDecimal availableLimit = limit.getAvailableLimit();
            if (availableLimit == null || availableLimit.compareTo(amount) < 0) {
                return new JsonBean(0, "可用额度不足").toString();
            }
            // 更新已用额度
            java.math.BigDecimal newUsedLimit = (limit.getUsedLimit() != null ? limit.getUsedLimit() : java.math.BigDecimal.ZERO).add(amount);
            creditLimitMapper.updateUsedLimit(id, newUsedLimit);
            return new JsonBean(1, "额度使用成功").toString();
        } catch (Exception e) {
            log.error("使用授信额度失败, id: {}", id, e);
            return new JsonBean(0, "使用额度失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/limit/{id}/repay")
    @ApiOperation("归还授信额度")
    public String repayCreditLimit(
            @PathVariable Long id,
            @RequestParam java.math.BigDecimal amount) {
        try {
            log.info("归还授信额度, id: {}, amount: {}", id, amount);
            // 获取当前额度信息
            TblCreditLimit limit = creditLimitService.getLimitById(id);
            if (limit == null) {
                return new JsonBean(0, "授信额度不存在").toString();
            }
            // 检查归还金额是否超过已用额度
            java.math.BigDecimal usedLimit = limit.getUsedLimit() != null ? limit.getUsedLimit() : java.math.BigDecimal.ZERO;
            if (amount.compareTo(usedLimit) > 0) {
                return new JsonBean(0, "归还金额不能超过已用额度").toString();
            }
            // 更新已用额度
            java.math.BigDecimal newUsedLimit = usedLimit.subtract(amount);
            creditLimitMapper.updateUsedLimit(id, newUsedLimit);
            return new JsonBean(1, "额度归还成功").toString();
        } catch (Exception e) {
            log.error("归还授信额度失败, id: {}", id, e);
            return new JsonBean(0, "归还额度失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/limit/{id}/suspend")
    @ApiOperation("暂停授信额度")
    public String suspendCreditLimit(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        try {
            log.info("暂停授信额度, id: {}, reason: {}", id, reason);
            creditLimitMapper.updateLimitStatus(id, "SUSPENDED");
            return new JsonBean(1, "额度暂停成功").toString();
        } catch (Exception e) {
            log.error("暂停授信额度失败, id: {}", id, e);
            return new JsonBean(0, "暂停额度失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/limit/{id}/activate")
    @ApiOperation("激活授信额度")
    public String activateCreditLimit(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        try {
            log.info("激活授信额度, id: {}, reason: {}", id, reason);
            creditLimitMapper.updateLimitStatus(id, "ACTIVE");
            return new JsonBean(1, "额度激活成功").toString();
        } catch (Exception e) {
            log.error("激活授信额度失败, id: {}", id, e);
            return new JsonBean(0, "激活额度失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/limit/{id}/cancel")
    @ApiOperation("取消授信额度")
    public String cancelCreditLimit(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        try {
            log.info("取消授信额度, id: {}, reason: {}", id, reason);
            creditLimitMapper.updateLimitStatus(id, "CANCELLED");
            return new JsonBean(1, "额度取消成功").toString();
        } catch (Exception e) {
            log.error("取消授信额度失败, id: {}", id, e);
            return new JsonBean(0, "取消额度失败: " + e.getMessage()).toString();
        }
    }

    // ==================== 授信评估管理 ====================

    // 使用静态变量存储模拟数据，以便修改后能够保持
    private static List<Map<String, Object>> assessmentDataStore = null;

    // 初始化模拟数据
    private List<Map<String, Object>> getAssessmentDataStore() {
        if (assessmentDataStore == null) {
            assessmentDataStore = new ArrayList<>();

            // 示例数据1
            Map<String, Object> item1 = new HashMap<>();
            item1.put("assessmentId", 1L);
            item1.put("assessmentNo", "CA202501001");
            item1.put("companyId", 1L);
            item1.put("companyName", "示例公司A");
            item1.put("assessmentType", "INITIAL");
            item1.put("creditRating", "A");
            item1.put("riskLevel", "LOW");
            item1.put("recommendedLimit", 1000000);
            item1.put("assessmentStatus", "APPROVED");
            item1.put("assessorId", 101L);
            item1.put("assessorName", "评估员A");
            item1.put("assessmentDate", "2025-01-15");
            item1.put("financialIndicators", "资产负债率: 45%, 流动比率: 1.8, 速动比率: 1.2");
            item1.put("businessIndicators", "营业收入增长率: 15%, 净利润率: 8%, 市场占有率: 12%");
            item1.put("riskIndicators", "信用风险: 低, 市场风险: 中, 操作风险: 低");
            item1.put("assessmentConclusion", "该公司财务状况良好，经营稳定，建议给予授信支持。");
            item1.put("assessmentRecommendations", "建议授信额度100万元，期限1年，利率按基准利率执行。");
            item1.put("createdTime", System.currentTimeMillis());
            assessmentDataStore.add(item1);

            // 示例数据2
            Map<String, Object> item2 = new HashMap<>();
            item2.put("assessmentId", 2L);
            item2.put("assessmentNo", "CA202501002");
            item2.put("companyId", 2L);
            item2.put("companyName", "示例公司B");
            item2.put("assessmentType", "PERIODIC");
            item2.put("creditRating", "B");
            item2.put("riskLevel", "MEDIUM");
            item2.put("recommendedLimit", 500000);
            item2.put("assessmentStatus", "PENDING");
            item2.put("assessorId", 102L);
            item2.put("assessorName", "评估员B");
            item2.put("assessmentDate", "2025-01-20");
            item2.put("financialIndicators", "资产负债率: 55%, 流动比率: 1.5, 速动比率: 1.0");
            item2.put("businessIndicators", "营业收入增长率: 10%, 净利润率: 5%, 市场占有率: 8%");
            item2.put("riskIndicators", "信用风险: 中, 市场风险: 中, 操作风险: 低");
            item2.put("assessmentConclusion", "该公司财务状况一般，需要进一步观察。");
            item2.put("assessmentRecommendations", "建议授信额度50万元，期限6个月，需提供担保。");
            item2.put("createdTime", System.currentTimeMillis());
            assessmentDataStore.add(item2);

            // 示例数据3
            Map<String, Object> item3 = new HashMap<>();
            item3.put("assessmentId", 3L);
            item3.put("assessmentNo", "CA202501003");
            item3.put("companyId", 3L);
            item3.put("companyName", "示例公司C");
            item3.put("assessmentType", "INITIAL");
            item3.put("creditRating", "C");
            item3.put("riskLevel", "HIGH");
            item3.put("recommendedLimit", 200000);
            item3.put("assessmentStatus", "REJECTED");
            item3.put("assessorId", 103L);
            item3.put("assessorName", "评估员C");
            item3.put("assessmentDate", "2025-01-25");
            item3.put("financialIndicators", "资产负债率: 70%, 流动比率: 1.0, 速动比率: 0.6");
            item3.put("businessIndicators", "营业收入增长率: -5%, 净利润率: 2%, 市场占有率: 3%");
            item3.put("riskIndicators", "信用风险: 高, 市场风险: 高, 操作风险: 中");
            item3.put("assessmentConclusion", "该公司财务状况较差，风险较高，不建议授信。");
            item3.put("assessmentRecommendations", "建议暂不授信，待公司财务状况改善后再行评估。");
            item3.put("createdTime", System.currentTimeMillis());
            assessmentDataStore.add(item3);
        }
        return assessmentDataStore;
    }

    @PostMapping("/credit/assessment/page")
    @ApiOperation("授信评估分页查询")
    public String getCreditAssessmentPage(@RequestParam(required = false) Map<String, Object> params) {
        try {
            log.info("授信评估分页查询, params: {}", params);

            // 获取数据存储
            List<Map<String, Object>> allList = getAssessmentDataStore();

            // 根据查询参数过滤数据
            List<Map<String, Object>> filteredList = new ArrayList<>();
            for (Map<String, Object> item : allList) {
                boolean match = true;

                if (params != null) {
                    // 按评估编号过滤
                    String assessmentNo = (String) params.get("assessmentNo");
                    if (assessmentNo != null && !assessmentNo.isEmpty()) {
                        String itemNo = (String) item.get("assessmentNo");
                        if (itemNo == null || !itemNo.contains(assessmentNo)) {
                            match = false;
                        }
                    }

                    // 按公司名称过滤
                    String companyName = (String) params.get("companyName");
                    if (match && companyName != null && !companyName.isEmpty()) {
                        String itemCompanyName = (String) item.get("companyName");
                        if (itemCompanyName == null || !itemCompanyName.contains(companyName)) {
                            match = false;
                        }
                    }

                    // 按评估类型过滤
                    String assessmentType = (String) params.get("assessmentType");
                    if (match && assessmentType != null && !assessmentType.isEmpty()) {
                        String itemType = (String) item.get("assessmentType");
                        if (itemType == null || !itemType.equals(assessmentType)) {
                            match = false;
                        }
                    }

                    // 按评估状态过滤
                    String assessmentStatus = (String) params.get("assessmentStatus");
                    if (match && assessmentStatus != null && !assessmentStatus.isEmpty()) {
                        String itemStatus = (String) item.get("assessmentStatus");
                        if (itemStatus == null || !itemStatus.equals(assessmentStatus)) {
                            match = false;
                        }
                    }

                    // 按信用评级过滤
                    String creditRating = (String) params.get("creditRating");
                    if (match && creditRating != null && !creditRating.isEmpty()) {
                        String itemRating = (String) item.get("creditRating");
                        if (itemRating == null || !itemRating.equals(creditRating)) {
                            match = false;
                        }
                    }

                    // 按风险等级过滤
                    String riskLevel = (String) params.get("riskLevel");
                    if (match && riskLevel != null && !riskLevel.isEmpty()) {
                        String itemRiskLevel = (String) item.get("riskLevel");
                        if (itemRiskLevel == null || !itemRiskLevel.equals(riskLevel)) {
                            match = false;
                        }
                    }
                }

                if (match) {
                    filteredList.add(item);
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", filteredList);
            data.put("totalRecord", filteredList.size());
            data.put("pageNo", params != null ? params.getOrDefault("pageNum", 1) : 1);
            data.put("pageSize", params != null ? params.getOrDefault("pageSize", 10) : 10);

            log.info("授信评估查询结果: 总数={}, 过滤后={}", allList.size(), filteredList.size());
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("授信评估分页查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/assessment/{id}")
    @ApiOperation("根据ID查询授信评估详情")
    public String getCreditAssessmentById(@PathVariable Long id) {
        try {
            log.info("查询授信评估详情, id: {}", id);
            // 从数据存储中查找
            List<Map<String, Object>> dataStore = getAssessmentDataStore();
            for (Map<String, Object> item : dataStore) {
                Object itemId = item.get("assessmentId");
                if (itemId != null && itemId.toString().equals(id.toString())) {
                    log.info("找到评估数据: {}", item);
                    return new JsonBean(1, "查询成功", item).toString();
                }
            }
            return new JsonBean(0, "未找到ID为" + id + "的评估记录").toString();
        } catch (Exception e) {
            log.error("查询授信评估详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/assessment")
    @ApiOperation("新增授信评估")
    public String addCreditAssessment(@RequestParam Map<String, Object> data) {
        try {
            log.info("新增授信评估, data: {}", data);
            // 生成新ID
            List<Map<String, Object>> dataStore = getAssessmentDataStore();
            long maxId = 0;
            for (Map<String, Object> item : dataStore) {
                Object itemId = item.get("assessmentId");
                if (itemId != null) {
                    long id = Long.parseLong(itemId.toString());
                    if (id > maxId) maxId = id;
                }
            }
            data.put("assessmentId", maxId + 1);
            data.put("createdTime", System.currentTimeMillis());
            dataStore.add(data);
            log.info("新增评估成功, id: {}", maxId + 1);
            return new JsonBean(1, "新增成功").toString();
        } catch (Exception e) {
            log.error("新增授信评估失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/credit/assessment/{id}")
    @ApiOperation("更新授信评估")
    public String updateCreditAssessment(@PathVariable Long id, @RequestParam Map<String, Object> data) {
        try {
            log.info("更新授信评估, id: {}, data: {}", id, data);
            // 从数据存储中查找并更新
            List<Map<String, Object>> dataStore = getAssessmentDataStore();
            for (int i = 0; i < dataStore.size(); i++) {
                Map<String, Object> item = dataStore.get(i);
                Object itemId = item.get("assessmentId");
                if (itemId != null && itemId.toString().equals(id.toString())) {
                    // 保留原有的assessmentId
                    data.put("assessmentId", id);
                    data.put("updatedTime", System.currentTimeMillis());
                    // 更新数据
                    dataStore.set(i, data);
                    log.info("更新评估成功, id: {}", id);
                    return new JsonBean(1, "更新成功").toString();
                }
            }
            return new JsonBean(0, "未找到ID为" + id + "的评估记录").toString();
        } catch (Exception e) {
            log.error("更新授信评估失败, id: {}", id, e);
            return new JsonBean(0, "更新失败: " + e.getMessage()).toString();
        }
    }

    @DeleteMapping("/credit/assessment/{id}")
    @ApiOperation("删除授信评估")
    public String deleteCreditAssessment(@PathVariable Long id) {
        try {
            log.info("删除授信评估, id: {}", id);
            // 从数据存储中查找并删除
            List<Map<String, Object>> dataStore = getAssessmentDataStore();
            for (int i = 0; i < dataStore.size(); i++) {
                Map<String, Object> item = dataStore.get(i);
                Object itemId = item.get("assessmentId");
                if (itemId != null && itemId.toString().equals(id.toString())) {
                    dataStore.remove(i);
                    log.info("删除评估成功, id: {}", id);
                    return new JsonBean(1, "删除成功").toString();
                }
            }
            return new JsonBean(0, "未找到ID为" + id + "的评估记录").toString();
        } catch (Exception e) {
            log.error("删除授信评估失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/assessment/statistics")
    @ApiOperation("授信评估统计")
    public String getCreditAssessmentStatistics() {
        try {
            log.info("查询授信评估统计");
            Map<String, Object> data = new HashMap<>();
            data.put("totalCount", 10);
            data.put("pendingCount", 3);
            data.put("approvedCount", 5);
            data.put("rejectedCount", 2);
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("查询授信评估统计失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/assessment/{id}/submit")
    @ApiOperation("提交授信评估")
    public String submitCreditAssessment(
            @PathVariable Long id,
            @RequestParam(required = false) String submitter) {
        try {
            log.info("提交授信评估, id: {}, submitter: {}", id, submitter);
            // TODO: 实现真实的提交逻辑，更新状态为COMPLETED
            return new JsonBean(1, "提交成功").toString();
        } catch (Exception e) {
            log.error("提交授信评估失败, id: {}", id, e);
            return new JsonBean(0, "提交失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/assessment/{id}/approve")
    @ApiOperation("批准授信评估")
    public String approveCreditAssessment(
            @PathVariable Long id,
            @RequestParam(required = false) String approver) {
        try {
            log.info("批准授信评估, id: {}, approver: {}", id, approver);
            // TODO: 实现真实的批准逻辑，更新状态为APPROVED
            return new JsonBean(1, "批准成功").toString();
        } catch (Exception e) {
            log.error("批准授信评估失败, id: {}", id, e);
            return new JsonBean(0, "批准失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/assessment/{id}/reject")
    @ApiOperation("拒绝授信评估")
    public String rejectCreditAssessment(
            @PathVariable Long id,
            @RequestParam(required = false) String rejectReason,
            @RequestParam(required = false) String rejector) {
        try {
            log.info("拒绝授信评估, id: {}, reason: {}, rejector: {}", id, rejectReason, rejector);
            // TODO: 实现真实的拒绝逻辑，更新状态为REJECTED
            return new JsonBean(1, "拒绝成功").toString();
        } catch (Exception e) {
            log.error("拒绝授信评估失败, id: {}", id, e);
            return new JsonBean(0, "拒绝失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/assessment/{id}/withdraw")
    @ApiOperation("撤回授信评估")
    public String withdrawCreditAssessment(
            @PathVariable Long id,
            @RequestParam(required = false) String withdrawReason) {
        try {
            log.info("撤回授信评估, id: {}, reason: {}", id, withdrawReason);
            // TODO: 实现真实的撤回逻辑，更新状态为DRAFT
            return new JsonBean(1, "撤回成功").toString();
        } catch (Exception e) {
            log.error("撤回授信评估失败, id: {}", id, e);
            return new JsonBean(0, "撤回失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/assessment/{id}/copy")
    @ApiOperation("复制授信评估")
    public String copyCreditAssessment(
            @PathVariable Long id,
            @RequestParam(required = false) Long targetCompanyId) {
        try {
            log.info("复制授信评估, id: {}, targetCompanyId: {}", id, targetCompanyId);
            // TODO: 实现真实的复制逻辑
            return new JsonBean(1, "复制成功").toString();
        } catch (Exception e) {
            log.error("复制授信评估失败, id: {}", id, e);
            return new JsonBean(0, "复制失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/assessment/company/{companyId}/history")
    @ApiOperation("查询公司评估历史")
    public String getCreditAssessmentHistory(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("查询公司评估历史, companyId: {}, limit: {}", companyId, limit);
            // 返回模拟数据
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> item = new HashMap<>();
            item.put("assessmentId", 1L);
            item.put("assessmentNo", "CA202501001");
            item.put("assessmentDate", "2025-01-15");
            item.put("creditRating", "A");
            item.put("riskLevel", "LOW");
            list.add(item);
            return new JsonBean(1, "查询成功", list).toString();
        } catch (Exception e) {
            log.error("查询公司评估历史失败, companyId: {}", companyId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    // ==================== 融资计划管理 ====================

    @PostMapping("/financing-plan/list")
    @ApiOperation("融资计划分页查询")
    public String getFinancingPlanPage(@FlexibleRequestBody FinancingPlanQueryDTO queryDTO) {
        try {
            log.info("融资计划分页查询, queryDTO: {}", queryDTO);
            PageInfo<TblFinancingPlan> pageInfo = financingPlanService.getPlanList(queryDTO);
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("融资计划分页查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/financing-plan/detail/{planId}")
    @ApiOperation("融资计划详情")
    public String getFinancingPlanDetail(@PathVariable Long planId) {
        try {
            log.info("查询融资计划详情, planId: {}", planId);
            TblFinancingPlan plan = financingPlanService.getPlanById(planId);
            return new JsonBean(1, "查询成功", plan).toString();
        } catch (ServiceException e) {
            log.warn("融资计划不存在, planId: {}", planId);
            return new JsonBean(0, e.getMessage()).toString();
        } catch (Exception e) {
            log.error("查询融资计划详情失败, planId: {}", planId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/financing-plan/create")
    @ApiOperation("创建融资计划")
    public String createFinancingPlan(@FlexibleRequestBody TblFinancingPlan plan) {
        try {
            log.info("创建融资计划, plan: {}", plan);
            // 设置默认值
            plan.setPlanStatus("DRAFT");
            plan.setDeleteFlag(0);
            plan.setCreatedTime(new Date());
            // 生成计划编号
            plan.setPlanNo("FP" + System.currentTimeMillis());
            financingPlanMapper.insert(plan);
            return new JsonBean(1, "创建成功", plan).toString();
        } catch (Exception e) {
            log.error("创建融资计划失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/financing-plan/update")
    @ApiOperation("更新融资计划")
    public String updateFinancingPlan(@FlexibleRequestBody TblFinancingPlan plan) {
        try {
            log.info("更新融资计划, planId: {}", plan.getPlanId());
            if (plan.getPlanId() == null) {
                return new JsonBean(0, "计划ID不能为空").toString();
            }
            // 检查计划是否存在
            TblFinancingPlan existPlan = financingPlanService.getPlanById(plan.getPlanId());
            if (!"DRAFT".equals(existPlan.getPlanStatus()) && !"REJECTED".equals(existPlan.getPlanStatus())) {
                return new JsonBean(0, "只能编辑草稿或已拒绝状态的计划").toString();
            }
            plan.setUpdatedTime(new Date());
            financingPlanMapper.updateById(plan);
            return new JsonBean(1, "更新成功", plan).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage()).toString();
        } catch (Exception e) {
            log.error("更新融资计划失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage()).toString();
        }
    }

    @DeleteMapping("/financing-plan/delete/{planId}")
    @ApiOperation("删除融资计划")
    public String deleteFinancingPlan(@PathVariable Long planId) {
        try {
            log.info("删除融资计划, planId: {}", planId);
            financingPlanService.deletePlan(planId);
            return new JsonBean(1, "删除成功").toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage()).toString();
        } catch (Exception e) {
            log.error("删除融资计划失败, planId: {}", planId, e);
            return new JsonBean(0, "删除失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/financing-plan/submit/{planId}")
    @ApiOperation("提交融资计划审批")
    public String submitFinancingPlan(@PathVariable Long planId) {
        try {
            log.info("提交融资计划审批, planId: {}", planId);
            financingPlanService.submitForApproval(planId);
            return new JsonBean(1, "提交成功").toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage()).toString();
        } catch (Exception e) {
            log.error("提交融资计划审批失败, planId: {}", planId, e);
            return new JsonBean(0, "提交失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/financing-plan/approve/{planId}")
    @ApiOperation("审批通过融资计划")
    public String approveFinancingPlan(@PathVariable Long planId, @RequestParam(required = false) Map<String, String> params) {
        try {
            log.info("审批通过融资计划, planId: {}", planId);
            String comments = params != null ? params.get("comments") : "";
            financingPlanService.approve(planId, comments);
            return new JsonBean(1, "审批通过").toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage()).toString();
        } catch (Exception e) {
            log.error("审批融资计划失败, planId: {}", planId, e);
            return new JsonBean(0, "审批失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/financing-plan/reject/{planId}")
    @ApiOperation("审批拒绝融资计划")
    public String rejectFinancingPlan(@PathVariable Long planId, @RequestParam(required = false) Map<String, String> params) {
        try {
            log.info("审批拒绝融资计划, planId: {}", planId);
            String comments = params != null ? params.get("comments") : "";
            financingPlanService.reject(planId, comments);
            return new JsonBean(1, "已拒绝").toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage()).toString();
        } catch (Exception e) {
            log.error("拒绝融资计划失败, planId: {}", planId, e);
            return new JsonBean(0, "拒绝失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/financing-plan/execute/{planId}")
    @ApiOperation("执行融资计划")
    public String executeFinancingPlan(@PathVariable Long planId) {
        try {
            log.info("执行融资计划, planId: {}", planId);
            TblFinancingPlan plan = financingPlanService.getPlanById(planId);
            if (!"APPROVED".equals(plan.getPlanStatus())) {
                return new JsonBean(0, "只能执行已审批通过的计划").toString();
            }
            financingPlanMapper.updatePlanStatus(planId, "EXECUTING");
            return new JsonBean(1, "执行成功").toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage()).toString();
        } catch (Exception e) {
            log.error("执行融资计划失败, planId: {}", planId, e);
            return new JsonBean(0, "执行失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/financing-plan/cancel/{planId}")
    @ApiOperation("取消融资计划")
    public String cancelFinancingPlan(@PathVariable Long planId) {
        try {
            log.info("取消融资计划, planId: {}", planId);
            TblFinancingPlan plan = financingPlanService.getPlanById(planId);
            if ("COMPLETED".equals(plan.getPlanStatus()) || "CANCELLED".equals(plan.getPlanStatus())) {
                return new JsonBean(0, "已完成或已取消的计划不能再取消").toString();
            }
            financingPlanMapper.updatePlanStatus(planId, "CANCELLED");
            return new JsonBean(1, "取消成功").toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage()).toString();
        } catch (Exception e) {
            log.error("取消融资计划失败, planId: {}", planId, e);
            return new JsonBean(0, "取消失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/financing-plan/copy/{planId}")
    @ApiOperation("复制融资计划")
    public String copyFinancingPlan(@PathVariable Long planId) {
        try {
            log.info("复制融资计划, planId: {}", planId);
            TblFinancingPlan sourcePlan = financingPlanService.getPlanById(planId);
            if (sourcePlan == null) {
                return new JsonBean(0, "源计划不存在").toString();
            }
            TblFinancingPlan newPlan = new TblFinancingPlan();
            // 复制属性（使用正确的字段名）
            newPlan.setPlanName(sourcePlan.getPlanName() + "_副本");
            newPlan.setPlanType(sourcePlan.getPlanType());
            newPlan.setPlanYear(sourcePlan.getPlanYear());
            newPlan.setCompanyId(sourcePlan.getCompanyId());
            newPlan.setCompanyName(sourcePlan.getCompanyName());
            newPlan.setPlanAmount(sourcePlan.getPlanAmount());
            newPlan.setCurrencyCode(sourcePlan.getCurrencyCode());
            newPlan.setStartDate(sourcePlan.getStartDate());
            newPlan.setEndDate(sourcePlan.getEndDate());
            newPlan.setDescription(sourcePlan.getDescription());
            // 设置新计划的默认值
            newPlan.setPlanNo("FP" + System.currentTimeMillis());
            newPlan.setPlanStatus("DRAFT");
            newPlan.setDeleteFlag(0);
            newPlan.setCreatedTime(new Date());
            financingPlanMapper.insert(newPlan);
            return new JsonBean(1, "复制成功", newPlan).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage()).toString();
        } catch (Exception e) {
            log.error("复制融资计划失败, planId: {}", planId, e);
            return new JsonBean(0, "复制失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/financing-plan/import")
    @ApiOperation("批量导入融资计划")
    public String importFinancingPlan(@RequestParam("file") MultipartFile file) {
        try {
            log.info("批量导入融资计划, fileName: {}", file.getOriginalFilename());
            if (file.isEmpty()) {
                return new JsonBean(0, "上传文件不能为空").toString();
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                return new JsonBean(0, "只支持 xlsx 或 xls 格式文件").toString();
            }

            // 解析Excel文件并导入数据
            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();

            try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0);
                int lastRowNum = sheet.getLastRowNum();

                // 从第二行开始读取（第一行是表头）
                for (int i = 1; i <= lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    try {
                        TblFinancingPlan plan = new TblFinancingPlan();

                        // 计划名称
                        Cell nameCell = row.getCell(0);
                        if (nameCell != null) {
                            plan.setPlanName(getCellStringValue(nameCell));
                        }

                        // 计划类型
                        Cell typeCell = row.getCell(1);
                        if (typeCell != null) {
                            plan.setPlanType(getCellStringValue(typeCell));
                        }

                        // 计划年度
                        Cell yearCell = row.getCell(2);
                        if (yearCell != null) {
                            plan.setPlanYear(Integer.parseInt(getCellStringValue(yearCell)));
                        }

                        // 计划金额
                        Cell amountCell = row.getCell(3);
                        if (amountCell != null) {
                            plan.setPlanAmount(new BigDecimal(getCellStringValue(amountCell)));
                        }

                        // 币种
                        Cell currencyCell = row.getCell(4);
                        if (currencyCell != null) {
                            plan.setCurrencyCode(getCellStringValue(currencyCell));
                        } else {
                            plan.setCurrencyCode("CNY");
                        }

                        // 开始日期
                        Cell startDateCell = row.getCell(5);
                        if (startDateCell != null) {
                            plan.setStartDate(parseDate(getCellStringValue(startDateCell)));
                        }

                        // 结束日期
                        Cell endDateCell = row.getCell(6);
                        if (endDateCell != null) {
                            plan.setEndDate(parseDate(getCellStringValue(endDateCell)));
                        }

                        // 公司名称
                        Cell companyCell = row.getCell(7);
                        if (companyCell != null) {
                            plan.setCompanyName(getCellStringValue(companyCell));
                        }

                        // 描述
                        Cell descCell = row.getCell(8);
                        if (descCell != null) {
                            plan.setDescription(getCellStringValue(descCell));
                        }

                        // 设置默认值
                        plan.setPlanNo(generatePlanNo());
                        plan.setPlanStatus("DRAFT");
                        plan.setCreatedTime(new Date());
                        plan.setUpdatedTime(new Date());

                        // 保存到数据库
                        financingPlanMapper.insert(plan);
                        successCount++;
                    } catch (Exception e) {
                        failCount++;
                        errorMessages.add("第" + (i + 1) + "行导入失败: " + e.getMessage());
                        log.warn("导入第{}行数据失败", i + 1, e);
                    }
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("totalCount", successCount + failCount);
            if (!errorMessages.isEmpty()) {
                result.put("errors", errorMessages.size() > 10 ? errorMessages.subList(0, 10) : errorMessages);
            }

            return new JsonBean(1, "导入完成", result).toString();
        } catch (Exception e) {
            log.error("批量导入融资计划失败", e);
            return new JsonBean(0, "导入失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 获取单元格字符串值
     */
    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(cell.getDateCellValue());
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    /**
     * 解析日期字符串
     */
    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成计划编号
     */
    private String generatePlanNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "FP" + sdf.format(new Date()) + String.format("%04d", (int)(Math.random() * 10000));
    }

    @GetMapping("/financing-plan/template")
    @ApiOperation("下载融资计划导入模板")
    public void downloadFinancingPlanTemplate(HttpServletResponse response) {
        try {
            log.info("下载融资计划导入模板");

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("融资计划导入模板.xlsx", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            // 生成简单的CSV格式模板（实际项目中应使用POI生成Excel）
            StringBuilder sb = new StringBuilder();
            sb.append("计划名称,计划类型(1-年度/2-季度/3-月度/4-临时),计划年度,计划金额,币种(CNY/USD/EUR),开始日期(yyyy-MM-dd),结束日期(yyyy-MM-dd),公司名称,描述\n");
            sb.append("示例计划,1,2026,10000000,CNY,2026-01-01,2026-12-31,示例公司,这是一个示例计划\n");

            OutputStream out = response.getOutputStream();
            out.write(sb.toString().getBytes("UTF-8"));
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("下载融资计划导入模板失败", e);
        }
    }

    @GetMapping("/financing-plan/statistics")
    @ApiOperation("融资计划统计数据")
    public String getFinancingPlanStatistics(
            @RequestParam(required = false) Integer planYear,
            @RequestParam(required = false) Long companyId) {
        try {
            log.info("查询融资计划统计数据, planYear: {}, companyId: {}", planYear, companyId);
            Map<String, Object> statistics = financingPlanService.getYearSummary(planYear, companyId);
            if (statistics == null) {
                statistics = new HashMap<>();
            }
            // 补充默认值
            statistics.putIfAbsent("totalPlans", 0);
            statistics.putIfAbsent("executingPlans", 0);
            statistics.putIfAbsent("totalAmount", BigDecimal.ZERO);
            statistics.putIfAbsent("completionRate", BigDecimal.ZERO);
            return new JsonBean(1, "查询成功", statistics).toString();
        } catch (Exception e) {
            log.error("查询融资计划统计数据失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/financing-plan/chart/type-distribution")
    @ApiOperation("融资类型分布图表数据")
    public String getFinancingTypeDistribution(
            @RequestParam(required = false) Integer planYear,
            @RequestParam(required = false) Long companyId) {
        try {
            log.info("查询融资类型分布, planYear: {}, companyId: {}", planYear, companyId);
            // 查询各状态的计划数量
            List<Map<String, Object>> distribution = new ArrayList<>();
            String[] statuses = {"DRAFT", "PENDING", "APPROVED", "REJECTED", "EXECUTING", "COMPLETED", "CANCELLED"};
            String[] statusNames = {"草稿", "待审批", "已审批", "已拒绝", "执行中", "已完成", "已取消"};
            for (int i = 0; i < statuses.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", statusNames[i]);
                item.put("value", financingPlanMapper.countByStatus(statuses[i], planYear, companyId));
                distribution.add(item);
            }
            return new JsonBean(1, "查询成功", distribution).toString();
        } catch (Exception e) {
            log.error("查询融资类型分布失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/financing-plan/chart/trend")
    @ApiOperation("融资计划执行趋势图表数据")
    public String getFinancingPlanTrend(
            @RequestParam(required = false) Integer planYear,
            @RequestParam(required = false) Integer months) {
        try {
            log.info("查询融资计划执行趋势, planYear: {}, months: {}", planYear, months);
            List<Map<String, Object>> trend = financingPlanMapper.selectMonthlyTrend(planYear, months);
            if (trend == null) {
                trend = new ArrayList<>();
            }
            return new JsonBean(1, "查询成功", trend).toString();
        } catch (Exception e) {
            log.error("查询融资计划执行趋势失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/financing-plan/export")
    @ApiOperation("导出融资计划数据")
    public void exportFinancingPlan(
            @RequestParam(required = false) String planNo,
            @RequestParam(required = false) String financingType,
            @RequestParam(required = false) String planStatus,
            HttpServletResponse response) {
        try {
            log.info("导出融资计划数据, planNo: {}, financingType: {}, planStatus: {}", planNo, financingType, planStatus);

            // 查询数据
            Map<String, Object> params = new HashMap<>();
            params.put("planNo", planNo);
            params.put("planType", financingType);
            params.put("planStatus", planStatus);
            List<TblFinancingPlan> planList = financingPlanMapper.selectPlanList(params);

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("融资计划数据_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            // 生成CSV格式数据（实际项目中应使用POI生成Excel）
            StringBuilder sb = new StringBuilder();
            // 表头
            sb.append("计划编号,计划名称,计划类型,计划年度,计划金额,币种,计划状态,开始日期,结束日期,公司名称,描述,创建人,创建时间\n");

            // 数据行
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
            for (TblFinancingPlan plan : planList) {
                sb.append(plan.getPlanNo() != null ? plan.getPlanNo() : "").append(",");
                sb.append(plan.getPlanName() != null ? plan.getPlanName() : "").append(",");
                sb.append(getPlanTypeText(plan.getPlanType())).append(",");
                sb.append(plan.getPlanYear() != null ? plan.getPlanYear() : "").append(",");
                sb.append(plan.getPlanAmount() != null ? plan.getPlanAmount() : "").append(",");
                sb.append(plan.getCurrencyCode() != null ? plan.getCurrencyCode() : "").append(",");
                sb.append(getPlanStatusText(plan.getPlanStatus())).append(",");
                sb.append(plan.getStartDate() != null ? dateSdf.format(plan.getStartDate()) : "").append(",");
                sb.append(plan.getEndDate() != null ? dateSdf.format(plan.getEndDate()) : "").append(",");
                sb.append(plan.getCompanyName() != null ? plan.getCompanyName() : "").append(",");
                sb.append(plan.getDescription() != null ? plan.getDescription().replace(",", "，") : "").append(",");
                sb.append(plan.getCreatedBy() != null ? plan.getCreatedBy() : "").append(",");
                sb.append(plan.getCreatedTime() != null ? sdf.format(plan.getCreatedTime()) : "").append("\n");
            }

            OutputStream out = response.getOutputStream();
            out.write(sb.toString().getBytes("UTF-8"));
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("导出融资计划数据失败", e);
        }
    }

    @GetMapping("/financing-plan/history/{planId}")
    @ApiOperation("获取融资计划操作历史记录")
    public String getFinancingPlanHistory(@PathVariable Long planId) {
        try {
            log.info("查询融资计划操作历史, planId: {}", planId);

            // 查询计划是否存在
            TblFinancingPlan plan = financingPlanService.getPlanById(planId);
            if (plan == null) {
                return new JsonBean(0, "融资计划不存在").toString();
            }

            // 构建历史记录（基于计划的状态变更记录）
            List<Map<String, Object>> historyList = new ArrayList<>();

            // 创建记录
            Map<String, Object> createRecord = new HashMap<>();
            createRecord.put("operationType", "创建计划");
            createRecord.put("operatorName", plan.getCreatedBy());
            createRecord.put("operateTime", plan.getCreatedTime());
            createRecord.put("comments", "创建融资计划");
            historyList.add(createRecord);

            // 如果有更新记录
            if (plan.getUpdatedTime() != null && plan.getUpdatedBy() != null) {
                Map<String, Object> updateRecord = new HashMap<>();
                updateRecord.put("operationType", "更新计划");
                updateRecord.put("operatorName", plan.getUpdatedBy());
                updateRecord.put("operateTime", plan.getUpdatedTime());
                updateRecord.put("comments", "更新融资计划信息");
                historyList.add(updateRecord);
            }

            // 根据状态添加相应的操作记录
            if ("PENDING".equals(plan.getPlanStatus()) || "APPROVED".equals(plan.getPlanStatus())
                    || "REJECTED".equals(plan.getPlanStatus()) || "EXECUTING".equals(plan.getPlanStatus())
                    || "COMPLETED".equals(plan.getPlanStatus()) || "CANCELLED".equals(plan.getPlanStatus())) {
                Map<String, Object> statusRecord = new HashMap<>();
                statusRecord.put("operationType", "状态变更为: " + getPlanStatusText(plan.getPlanStatus()));
                statusRecord.put("operatorName", plan.getUpdatedBy() != null ? plan.getUpdatedBy() : plan.getCreatedBy());
                statusRecord.put("operateTime", plan.getUpdatedTime() != null ? plan.getUpdatedTime() : plan.getCreatedTime());
                statusRecord.put("comments", plan.getApprovalComments());
                historyList.add(statusRecord);
            }

            return new JsonBean(1, "查询成功", historyList).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage()).toString();
        } catch (Exception e) {
            log.error("查询融资计划操作历史失败, planId: {}", planId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 获取计划类型文本
     */
    private String getPlanTypeText(String planType) {
        if (planType == null) return "";
        switch (planType) {
            case "1": return "年度计划";
            case "2": return "季度计划";
            case "3": return "月度计划";
            case "4": return "临时计划";
            default: return planType;
        }
    }

    /**
     * 获取计划状态文本
     */
    private String getPlanStatusText(String status) {
        if (status == null) return "";
        switch (status) {
            case "DRAFT": return "草稿";
            case "PENDING": return "待审批";
            case "APPROVED": return "已审批";
            case "REJECTED": return "已拒绝";
            case "EXECUTING": return "执行中";
            case "COMPLETED": return "已完成";
            case "CANCELLED": return "已取消";
            default: return status;
        }
    }

    // ==================== 银行贷款管理 ====================
    // 注意：银行贷款接口已迁移到 BankLoanController，以下临时接口已注释
    // @PostMapping("/bank-loan/list")
    // @ApiOperation("银行贷款分页查询")
    // public String getBankLoanPage(...) { ... }
    // @GetMapping("/bank-loan/detail")
    // public String getBankLoanDetail(...) { ... }

    // ==================== 票据贴现管理 ====================

    @PostMapping("/bill-discount/list")
    @ApiOperation("票据贴现分页查询")
    public String getBillDiscountPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        Map<String, Object> data = new HashMap<>();
        data.put("tlist", new ArrayList<>());
        data.put("totalRecord", 0);
        data.put("pageNo", page);
        data.put("pageSize", limit);
        return new JsonBean(1, "查询成功（临时数据）", data).toString();
    }

    @GetMapping("/bill-discount/detail")
    @ApiOperation("票据贴现详情")
    public String getBillDiscountDetail(@RequestParam Long id) {
        return new JsonBean(1, "查询成功（临时数据）", new HashMap<>()).toString();
    }

    // ==================== 银行承兑管理 ====================

    @PostMapping("/bill-acceptance/list")
    @ApiOperation("银行承兑分页查询")
    public String getBillAcceptancePage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        Map<String, Object> data = new HashMap<>();
        data.put("tlist", new ArrayList<>());
        data.put("totalRecord", 0);
        data.put("pageNo", page);
        data.put("pageSize", limit);
        return new JsonBean(1, "查询成功（临时数据）", data).toString();
    }

    @GetMapping("/bill-acceptance/detail")
    @ApiOperation("银行承兑详情")
    public String getBillAcceptanceDetail(@RequestParam Long id) {
        return new JsonBean(1, "查询成功（临时数据）", new HashMap<>()).toString();
    }

    // ==================== 融资租赁管理 ====================

    @PostMapping("/financial-lease/list")
    @ApiOperation("融资租赁分页查询")
    public String getFinancialLeasePage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(required = false) String leasingCompany,
            @RequestParam(required = false) String leasingType,
            @RequestParam(required = false) String applicationStatus,
            @RequestParam(required = false) String leaseNo) {
        try {
            FinancialLeaseQueryDTO queryDTO = new FinancialLeaseQueryDTO();
            queryDTO.setPageNum(page);
            queryDTO.setPageSize(limit);
            queryDTO.setLeasingCompany(leasingCompany);
            queryDTO.setLeasingType(leasingType);
            queryDTO.setApplicationStatus(applicationStatus);
            queryDTO.setLeaseNo(leaseNo);

            PageInfo<TblFinancialLease> pageInfo = financialLeaseService.getLeaseList(queryDTO);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", page);
            data.put("pageSize", limit);
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("融资租赁分页查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @GetMapping("/financial-lease/detail")
    @ApiOperation("融资租赁详情")
    public String getFinancialLeaseDetail(@RequestParam Long id) {
        try {
            TblFinancialLease lease = financialLeaseService.getLeaseById(id);
            return new JsonBean(1, "查询成功", lease).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("融资租赁详情查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/financial-lease/create")
    @ApiOperation("新增融资租赁")
    public String createFinancialLease(@FlexibleRequestBody FinancialLeaseDTO dto) {
        try {
            TblFinancialLease lease = financialLeaseService.saveLease(dto);
            return new JsonBean(1, "新增成功", lease).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("新增融资租赁失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/financial-lease/update")
    @ApiOperation("更新融资租赁")
    public String updateFinancialLease(@FlexibleRequestBody FinancialLeaseDTO dto) {
        try {
            if (dto.getLeaseId() == null) {
                return new JsonBean(0, "租赁ID不能为空", null).toString();
            }
            TblFinancialLease lease = financialLeaseService.saveLease(dto);
            return new JsonBean(1, "更新成功", lease).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("更新融资租赁失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/financial-lease/delete/{id}")
    @ApiOperation("删除融资租赁")
    public String deleteFinancialLease(@PathVariable Long id) {
        try {
            financialLeaseService.deleteLease(id);
            return new JsonBean(1, "删除成功", null).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("删除融资租赁失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/financial-lease/{id}/submit")
    @ApiOperation("提交融资租赁审批")
    public String submitFinancialLease(@PathVariable Long id) {
        try {
            financialLeaseService.submitForApproval(id);
            return new JsonBean(1, "提交成功", null).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("提交融资租赁审批失败", e);
            return new JsonBean(0, "提交失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/financial-lease/approve")
    @ApiOperation("审批融资租赁")
    public String approveFinancialLease(@RequestParam Long id, @RequestParam(required = false) String comments) {
        try {
            financialLeaseService.approve(id, comments);
            return new JsonBean(1, "审批通过", null).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("审批融资租赁失败", e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/financial-lease/reject")
    @ApiOperation("拒绝融资租赁")
    public String rejectFinancialLease(@RequestParam Long id, @RequestParam(required = false) String comments) {
        try {
            financialLeaseService.reject(id, comments);
            return new JsonBean(1, "已拒绝", null).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("拒绝融资租赁失败", e);
            return new JsonBean(0, "拒绝失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/financial-lease/{id}/activate")
    @ApiOperation("激活融资租赁（签约）")
    public String activateFinancialLease(@PathVariable Long id) {
        try {
            financialLeaseService.activateLease(id);
            return new JsonBean(1, "激活成功", null).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("激活融资租赁失败", e);
            return new JsonBean(0, "激活失败: " + e.getMessage(), null).toString();
        }
    }

    @GetMapping("/financial-lease/overview")
    @ApiOperation("融资租赁统计概览")
    public String getFinancialLeaseOverview() {
        try {
            Map<String, Object> statistics = financialLeaseService.getOverviewStatistics();
            return new JsonBean(1, "查询成功", statistics).toString();
        } catch (Exception e) {
            log.error("获取融资租赁统计概览失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @GetMapping("/financial-lease/statistics/type-distribution")
    @ApiOperation("融资租赁类型分布统计")
    public String getFinancialLeaseTypeDistribution() {
        try {
            List<Map<String, Object>> distribution = financialLeaseService.getTypeDistribution();
            return new JsonBean(1, "查询成功", distribution).toString();
        } catch (Exception e) {
            log.error("获取融资租赁类型分布统计失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @GetMapping("/financial-lease/statistics/trend")
    @ApiOperation("融资租赁申请趋势统计")
    public String getFinancialLeaseTrend(@RequestParam(defaultValue = "1Y") String period) {
        try {
            Map<String, Object> trend = financialLeaseService.getTrendStatistics(period);
            return new JsonBean(1, "查询成功", trend).toString();
        } catch (Exception e) {
            log.error("获取融资租赁申请趋势统计失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/financial-lease/export")
    @ApiOperation("导出融资租赁数据")
    public void exportFinancialLease(
            @RequestParam(required = false) String leasingCompany,
            @RequestParam(required = false) String leasingType,
            @RequestParam(required = false) String applicationStatus,
            @RequestParam(required = false) String leaseNo,
            HttpServletResponse response) {
        try {
            // 构建查询条件
            FinancialLeaseQueryDTO queryDTO = new FinancialLeaseQueryDTO();
            queryDTO.setLeasingCompany(leasingCompany);
            queryDTO.setLeasingType(leasingType);
            queryDTO.setApplicationStatus(applicationStatus);
            queryDTO.setLeaseNo(leaseNo);

            // 查询数据（不分页，导出全部）
            List<TblFinancialLease> list = financialLeaseService.getLeaseListForExport(queryDTO);

            // 转换为导出DTO
            List<com.global.treasurer.dto.export.ExportFinancialLeaseDTO> exportList = list.stream()
                    .map(com.global.treasurer.dto.export.ExportFinancialLeaseDTO::fromEntity)
                    .collect(java.util.stream.Collectors.toList());

            // 生成Excel并导出
            String filename = "融资租赁数据_" + System.currentTimeMillis() + ".xlsx";
            try (com.global.treasurer.util.excel.ExcelExport export =
                    new com.global.treasurer.util.excel.ExcelExport("融资租赁数据",
                            com.global.treasurer.dto.export.ExportFinancialLeaseDTO.class)) {
                export.setDataList(exportList).write(response, filename);
            }
        } catch (Exception e) {
            log.error("导出融资租赁数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage(), null).toString());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    @GetMapping("/financial-lease/export/template")
    @ApiOperation("下载融资租赁导入模板")
    public void downloadFinancialLeaseTemplate(HttpServletResponse response) {
        try {
            String filename = "融资租赁导入模板.xlsx";
            try (com.global.treasurer.util.excel.ExcelExport export =
                    new com.global.treasurer.util.excel.ExcelExport("融资租赁导入模板",
                            com.global.treasurer.dto.export.ExportFinancialLeaseDTO.class)) {
                export.setDataList(new ArrayList<>()).write(response, filename);
            }
        } catch (Exception e) {
            log.error("下载融资租赁导入模板失败", e);
        }
    }

    // ==================== 债券融资管理 ====================
    // 注意：债券融资功能已迁移到 BondIssuanceController
    // 相关接口路径：/financial/rzgl/bond-financing/**
    // 以下方法已废弃，仅保留用于向后兼容提示

    @PostMapping("/bond-financing-legacy")
    @ApiOperation("债券融资接口已迁移（请使用BondIssuanceController）")
    @Deprecated
    public String bondFinancingLegacyNotice() {
        return new JsonBean(0, "债券融资功能已迁移到 BondIssuanceController，请使用路径: /financial/rzgl/bond-financing/*", null).toString();
    }

    // ==================== 质押融资管理 ====================

    @PostMapping("/pledge-financing/list")
    @ApiOperation("质押融资分页查询")
    public String getPledgeFinancingPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        Map<String, Object> data = new HashMap<>();
        data.put("tlist", new ArrayList<>());
        data.put("totalRecord", 0);
        data.put("pageNo", page);
        data.put("pageSize", limit);
        return new JsonBean(1, "查询成功（临时数据）", data).toString();
    }

    @GetMapping("/pledge-financing/detail")
    @ApiOperation("质押融资详情")
    public String getPledgeFinancingDetail(@RequestParam Long id) {
        return new JsonBean(1, "查询成功（临时数据）", new HashMap<>()).toString();
    }

    // ==================== 融资基础参数管理 ====================

    @PostMapping("/financing-basic-params/list")
    @ApiOperation("融资基础参数分页查询")
    public String getFinancingBasicParamsPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        Map<String, Object> data = new HashMap<>();
        data.put("tlist", new ArrayList<>());
        data.put("totalRecord", 0);
        data.put("pageNo", page);
        data.put("pageSize", limit);
        return new JsonBean(1, "查询成功（临时数据）", data).toString();
    }

    @GetMapping("/financing-basic-params/detail")
    @ApiOperation("融资基础参数详情")
    public String getFinancingBasicParamsDetail(@RequestParam Long id) {
        return new JsonBean(1, "查询成功（临时数据）", new HashMap<>()).toString();
    }

    // ==================== 担保物管理 ====================

    @PostMapping("/collateral/page")
    @ApiOperation("担保物分页查询")
    public String getCollateralPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        Map<String, Object> data = new HashMap<>();
        data.put("tlist", new ArrayList<>());
        data.put("totalRecord", 0);
        data.put("pageNo", page);
        data.put("pageSize", limit);
        return new JsonBean(1, "查询成功（临时数据）", data).toString();
    }

    @GetMapping("/collateral/detail")
    @ApiOperation("担保物详情")
    public String getCollateralDetail(@RequestParam Long id) {
        return new JsonBean(1, "查询成功（临时数据）", new HashMap<>()).toString();
    }

    // ==================== 融资监控管理 ====================

    @PostMapping("/financing-monitoring/page")
    @ApiOperation("融资监控分页查询")
    public String getFinancingMonitoringPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        Map<String, Object> data = new HashMap<>();
        data.put("tlist", new ArrayList<>());
        data.put("totalRecord", 0);
        data.put("pageNo", page);
        data.put("pageSize", limit);
        return new JsonBean(1, "查询成功（临时数据）", data).toString();
    }

    @GetMapping("/financing-monitoring/detail")
    @ApiOperation("融资监控详情")
    public String getFinancingMonitoringDetail(@RequestParam Long id) {
        return new JsonBean(1, "查询成功（临时数据）", new HashMap<>()).toString();
    }

    // ==================== 担保监控统计 ====================

    @GetMapping("/guarantee/monitoring/statistics")
    @ApiOperation("担保监控统计")
    public String getGuaranteeMonitoringStatistics() {
        try {
            log.info("查询担保监控统计");

            // 调用Service从数据库查询真实数据
            Map<String, Object> data = guaranteeMonitorService.getGuaranteeDashboard(null);

            log.info("担保监控统计查询成功");
            return new JsonBean(1, "查询成功", data).toString();

        } catch (Exception e) {
            log.error("查询担保监控统计失败", e);
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("totalGuarantee", 0);
            errorData.put("activeGuarantee", 0);
            errorData.put("totalAmount", 0);
            errorData.put("usedAmount", 0);
            errorData.put("error", e.getMessage());
            return new JsonBean(0, "查询失败: " + e.getMessage(), errorData).toString();
        }
    }

    // ==================== 授信监控管理 ====================

    // 使用静态变量存储监控预警数据
    private static List<Map<String, Object>> monitoringDataStore = null;

    // 初始化监控预警模拟数据
    private List<Map<String, Object>> getMonitoringDataStore() {
        if (monitoringDataStore == null) {
            monitoringDataStore = new ArrayList<>();

            // 示例数据1 - 额度超限预警
            Map<String, Object> item1 = new HashMap<>();
            item1.put("alertId", 1L);
            item1.put("alertNo", "ALT202501001");
            item1.put("companyId", 1L);
            item1.put("companyName", "示例公司A");
            item1.put("limitId", 1L);
            item1.put("limitCode", "CL202501001");
            item1.put("alertType", "LIMIT_EXCEED");
            item1.put("alertLevel", "HIGH");
            item1.put("alertStatus", "ACTIVE");
            item1.put("alertTitle", "授信额度使用超过90%");
            item1.put("alertContent", "示例公司A的授信额度使用率已达95%，请及时关注。");
            item1.put("alertDate", "2025-01-15");
            item1.put("alertTime", "2025-01-15 10:30:00");
            item1.put("currentValue", 950000);
            item1.put("thresholdValue", 900000);
            item1.put("triggerConditions", "授信额度使用率超过90%时触发预警");
            item1.put("suggestedActions", "建议与客户沟通，评估是否需要增加授信额度或调整还款计划");
            item1.put("createdTime", System.currentTimeMillis());
            monitoringDataStore.add(item1);

            // 示例数据2 - 到期预警
            Map<String, Object> item2 = new HashMap<>();
            item2.put("alertId", 2L);
            item2.put("alertNo", "ALT202501002");
            item2.put("companyId", 2L);
            item2.put("companyName", "示例公司B");
            item2.put("limitId", 2L);
            item2.put("limitCode", "CL202501002");
            item2.put("alertType", "EXPIRY_WARNING");
            item2.put("alertLevel", "MEDIUM");
            item2.put("alertStatus", "PENDING");
            item2.put("alertTitle", "授信即将到期");
            item2.put("alertContent", "示例公司B的授信将于30天后到期，请及时续期。");
            item2.put("alertDate", "2025-01-20");
            item2.put("alertTime", "2025-01-20 14:00:00");
            item2.put("currentValue", 30);
            item2.put("thresholdValue", 30);
            item2.put("triggerConditions", "授信到期前30天触发预警");
            item2.put("suggestedActions", "建议提前与客户沟通续期事宜，准备续期材料");
            item2.put("createdTime", System.currentTimeMillis());
            monitoringDataStore.add(item2);

            // 示例数据3 - 严重预警
            Map<String, Object> item3 = new HashMap<>();
            item3.put("alertId", 3L);
            item3.put("alertNo", "ALT202501003");
            item3.put("companyId", 3L);
            item3.put("companyName", "示例公司C");
            item3.put("limitId", 3L);
            item3.put("limitCode", "CL202501003");
            item3.put("alertType", "RISK_ALERT");
            item3.put("alertLevel", "CRITICAL");
            item3.put("alertStatus", "ACTIVE");
            item3.put("alertTitle", "信用评级下调");
            item3.put("alertContent", "示例公司C的信用评级从B下调至C，风险等级提升。");
            item3.put("alertDate", "2025-01-25");
            item3.put("alertTime", "2025-01-25 09:00:00");
            item3.put("currentValue", 3);
            item3.put("thresholdValue", 2);
            item3.put("triggerConditions", "信用评级下调时触发预警");
            item3.put("suggestedActions", "建议重新评估客户风险，考虑调整授信额度或加强监控");
            item3.put("createdTime", System.currentTimeMillis());
            monitoringDataStore.add(item3);

            // 示例数据4 - 已处理预警
            Map<String, Object> item4 = new HashMap<>();
            item4.put("alertId", 4L);
            item4.put("alertNo", "ALT202501004");
            item4.put("companyId", 1L);
            item4.put("companyName", "示例公司A");
            item4.put("limitId", 1L);
            item4.put("limitCode", "CL202501001");
            item4.put("alertType", "LIMIT_EXCEED");
            item4.put("alertLevel", "LOW");
            item4.put("alertStatus", "HANDLED");
            item4.put("alertTitle", "额度使用超过80%");
            item4.put("alertContent", "示例公司A的授信额度使用率已达85%。");
            item4.put("alertDate", "2025-01-10");
            item4.put("alertTime", "2025-01-10 16:00:00");
            item4.put("handlerId", 101L);
            item4.put("handlerName", "处理员A");
            item4.put("handleTime", "2025-01-11 10:00:00");
            item4.put("handleComments", "已与客户沟通，计划增加授信额度。");
            item4.put("triggerConditions", "授信额度使用率超过80%时触发预警");
            item4.put("suggestedActions", "建议关注客户资金使用情况");
            item4.put("createdTime", System.currentTimeMillis());
            monitoringDataStore.add(item4);
        }
        return monitoringDataStore;
    }

    @PostMapping("/credit/monitoring/page")
    @ApiOperation("授信监控预警分页查询")
    public String getCreditMonitoringPage(@RequestParam(required = false) Map<String, Object> params) {
        try {
            log.info("授信监控预警分页查询, params: {}", params);

            List<Map<String, Object>> allList = getMonitoringDataStore();

            // 根据查询参数过滤数据
            List<Map<String, Object>> filteredList = new ArrayList<>();
            for (Map<String, Object> item : allList) {
                boolean match = true;

                if (params != null) {
                    // 按预警编号过滤
                    String alertNo = (String) params.get("alertNo");
                    if (alertNo != null && !alertNo.isEmpty()) {
                        String itemNo = (String) item.get("alertNo");
                        if (itemNo == null || !itemNo.contains(alertNo)) {
                            match = false;
                        }
                    }

                    // 按公司名称过滤
                    String companyName = (String) params.get("companyName");
                    if (match && companyName != null && !companyName.isEmpty()) {
                        String itemCompanyName = (String) item.get("companyName");
                        if (itemCompanyName == null || !itemCompanyName.contains(companyName)) {
                            match = false;
                        }
                    }

                    // 按预警类型过滤
                    String alertType = (String) params.get("alertType");
                    if (match && alertType != null && !alertType.isEmpty()) {
                        String itemType = (String) item.get("alertType");
                        if (itemType == null || !itemType.equals(alertType)) {
                            match = false;
                        }
                    }

                    // 按预警级别过滤
                    String alertLevel = (String) params.get("alertLevel");
                    if (match && alertLevel != null && !alertLevel.isEmpty()) {
                        String itemLevel = (String) item.get("alertLevel");
                        if (itemLevel == null || !itemLevel.equals(alertLevel)) {
                            match = false;
                        }
                    }

                    // 按预警状态过滤
                    String alertStatus = (String) params.get("alertStatus");
                    if (match && alertStatus != null && !alertStatus.isEmpty()) {
                        String itemStatus = (String) item.get("alertStatus");
                        if (itemStatus == null || !itemStatus.equals(alertStatus)) {
                            match = false;
                        }
                    }
                }

                if (match) {
                    filteredList.add(item);
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", filteredList);
            data.put("totalRecord", filteredList.size());
            data.put("pageNo", params != null ? params.getOrDefault("pageNum", 1) : 1);
            data.put("pageSize", params != null ? params.getOrDefault("pageSize", 10) : 10);

            log.info("授信监控预警查询结果: 总数={}", filteredList.size());
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("授信监控预警分页查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/monitoring/critical")
    @ApiOperation("查询严重预警")
    public String getCriticalCreditMonitoring() {
        try {
            log.info("查询严重预警");

            List<Map<String, Object>> allList = getMonitoringDataStore();
            List<Map<String, Object>> criticalList = new ArrayList<>();

            for (Map<String, Object> item : allList) {
                String alertLevel = (String) item.get("alertLevel");
                String alertStatus = (String) item.get("alertStatus");
                // 只返回严重级别且未处理的预警
                if ("CRITICAL".equals(alertLevel) && !"CLOSED".equals(alertStatus)) {
                    criticalList.add(item);
                }
            }

            return new JsonBean(1, "查询成功", criticalList).toString();
        } catch (Exception e) {
            log.error("查询严重预警失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/monitoring/active")
    @ApiOperation("查询活跃预警")
    public String getActiveCreditMonitoring() {
        try {
            log.info("查询活跃预警");

            List<Map<String, Object>> allList = getMonitoringDataStore();
            List<Map<String, Object>> activeList = new ArrayList<>();

            for (Map<String, Object> item : allList) {
                String alertStatus = (String) item.get("alertStatus");
                if ("ACTIVE".equals(alertStatus)) {
                    activeList.add(item);
                }
            }

            return new JsonBean(1, "查询成功", activeList).toString();
        } catch (Exception e) {
            log.error("查询活跃预警失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/monitoring/high-level")
    @ApiOperation("查询高级别预警")
    public String getHighLevelCreditMonitoring() {
        try {
            log.info("查询高级别预警");

            List<Map<String, Object>> allList = getMonitoringDataStore();
            List<Map<String, Object>> highLevelList = new ArrayList<>();

            for (Map<String, Object> item : allList) {
                String alertLevel = (String) item.get("alertLevel");
                if ("HIGH".equals(alertLevel) || "CRITICAL".equals(alertLevel)) {
                    highLevelList.add(item);
                }
            }

            return new JsonBean(1, "查询成功", highLevelList).toString();
        } catch (Exception e) {
            log.error("查询高级别预警失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/monitoring/unhandled")
    @ApiOperation("查询未处理预警")
    public String getUnhandledCreditMonitoring() {
        try {
            log.info("查询未处理预警");

            List<Map<String, Object>> allList = getMonitoringDataStore();
            List<Map<String, Object>> unhandledList = new ArrayList<>();

            for (Map<String, Object> item : allList) {
                String alertStatus = (String) item.get("alertStatus");
                if ("ACTIVE".equals(alertStatus) || "PENDING".equals(alertStatus)) {
                    unhandledList.add(item);
                }
            }

            return new JsonBean(1, "查询成功", unhandledList).toString();
        } catch (Exception e) {
            log.error("查询未处理预警失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/credit/monitoring/{id}")
    @ApiOperation("根据ID查询预警详情")
    public String getCreditMonitoringById(@PathVariable Long id) {
        try {
            log.info("查询预警详情, id: {}", id);

            List<Map<String, Object>> dataStore = getMonitoringDataStore();
            for (Map<String, Object> item : dataStore) {
                Object itemId = item.get("alertId");
                if (itemId != null && itemId.toString().equals(id.toString())) {
                    return new JsonBean(1, "查询成功", item).toString();
                }
            }
            return new JsonBean(0, "未找到ID为" + id + "的预警记录").toString();
        } catch (Exception e) {
            log.error("查询预警详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/monitoring/{id}/handle")
    @ApiOperation("处理预警")
    public String handleCreditMonitoring(
            @PathVariable Long id,
            @RequestParam(required = false) Long handlerId,
            @RequestParam(required = false) String handleComments) {
        try {
            log.info("处理预警, id: {}, handlerId: {}, comments: {}", id, handlerId, handleComments);

            List<Map<String, Object>> dataStore = getMonitoringDataStore();
            for (Map<String, Object> item : dataStore) {
                Object itemId = item.get("alertId");
                if (itemId != null && itemId.toString().equals(id.toString())) {
                    item.put("alertStatus", "HANDLED");
                    item.put("handlerId", handlerId);
                    item.put("handleComments", handleComments);
                    item.put("handleTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
                    return new JsonBean(1, "处理成功").toString();
                }
            }
            return new JsonBean(0, "未找到ID为" + id + "的预警记录").toString();
        } catch (Exception e) {
            log.error("处理预警失败, id: {}", id, e);
            return new JsonBean(0, "处理失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/monitoring/{id}/close")
    @ApiOperation("关闭预警")
    public String closeCreditMonitoring(
            @PathVariable Long id,
            @RequestParam(required = false) Long handlerId,
            @RequestParam(required = false) String handleComments) {
        try {
            log.info("关闭预警, id: {}, handlerId: {}, comments: {}", id, handlerId, handleComments);

            List<Map<String, Object>> dataStore = getMonitoringDataStore();
            for (Map<String, Object> item : dataStore) {
                Object itemId = item.get("alertId");
                if (itemId != null && itemId.toString().equals(id.toString())) {
                    item.put("alertStatus", "CLOSED");
                    item.put("handlerId", handlerId);
                    item.put("handleComments", handleComments);
                    item.put("closeTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
                    return new JsonBean(1, "关闭成功").toString();
                }
            }
            return new JsonBean(0, "未找到ID为" + id + "的预警记录").toString();
        } catch (Exception e) {
            log.error("关闭预警失败, id: {}", id, e);
            return new JsonBean(0, "关闭失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/credit/monitoring/{id}/reactivate")
    @ApiOperation("重新激活预警")
    public String reactivateCreditMonitoring(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        try {
            log.info("重新激活预警, id: {}, reason: {}", id, reason);

            List<Map<String, Object>> dataStore = getMonitoringDataStore();
            for (Map<String, Object> item : dataStore) {
                Object itemId = item.get("alertId");
                if (itemId != null && itemId.toString().equals(id.toString())) {
                    item.put("alertStatus", "ACTIVE");
                    item.put("reactivateReason", reason);
                    item.put("reactivateTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
                    return new JsonBean(1, "重新激活成功").toString();
                }
            }
            return new JsonBean(0, "未找到ID为" + id + "的预警记录").toString();
        } catch (Exception e) {
            log.error("重新激活预警失败, id: {}", id, e);
            return new JsonBean(0, "重新激活失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/credit/monitoring/{id}")
    @ApiOperation("修改预警")
    public String updateCreditMonitoring(
            @PathVariable Long id,
            @RequestParam Map<String, Object> data) {
        try {
            log.info("修改预警, id: {}, data: {}", id, data);

            List<Map<String, Object>> dataStore = getMonitoringDataStore();
            for (Map<String, Object> item : dataStore) {
                Object itemId = item.get("alertId");
                if (itemId != null && itemId.toString().equals(id.toString())) {
                    // 更新字段
                    if (data.containsKey("alertType")) item.put("alertType", data.get("alertType"));
                    if (data.containsKey("alertLevel")) item.put("alertLevel", data.get("alertLevel"));
                    if (data.containsKey("alertStatus")) item.put("alertStatus", data.get("alertStatus"));
                    if (data.containsKey("alertTitle")) item.put("alertTitle", data.get("alertTitle"));
                    if (data.containsKey("alertContent")) item.put("alertContent", data.get("alertContent"));
                    if (data.containsKey("companyName")) item.put("companyName", data.get("companyName"));
                    item.put("updateTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
                    return new JsonBean(1, "修改成功").toString();
                }
            }
            return new JsonBean(0, "未找到ID为" + id + "的预警记录").toString();
        } catch (Exception e) {
            log.error("修改预警失败, id: {}", id, e);
            return new JsonBean(0, "修改失败: " + e.getMessage()).toString();
        }
    }

    @DeleteMapping("/credit/monitoring/{id}")
    @ApiOperation("删除预警")
    public String deleteCreditMonitoring(@PathVariable String id) {
        try {
            log.info("删除预警, id: {}", id);

            List<Map<String, Object>> dataStore = getMonitoringDataStore();
            String[] ids = id.split(",");
            int deletedCount = 0;

            for (String alertId : ids) {
                for (int i = dataStore.size() - 1; i >= 0; i--) {
                    Object itemId = dataStore.get(i).get("alertId");
                    if (itemId != null && itemId.toString().equals(alertId.trim())) {
                        dataStore.remove(i);
                        deletedCount++;
                        break;
                    }
                }
            }

            if (deletedCount > 0) {
                return new JsonBean(1, "删除成功，共删除" + deletedCount + "条记录").toString();
            }
            return new JsonBean(0, "未找到要删除的预警记录").toString();
        } catch (Exception e) {
            log.error("删除预警失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage()).toString();
        }
    }

    // ==================== 授信监控统计 ====================

    @GetMapping("/credit/monitoring/statistics")
    @ApiOperation("授信监控统计")
    public String getCreditMonitoringStatistics(@RequestParam(required = false) Long orgid) {
        try {
            log.info("查询授信监控统计, orgid: {}", orgid);

            // 调用Service从数据库查询真实数据
            Map<String, Object> data = financingMonitoringService.getDashboardData();

            log.info("授信监控统计查询成功, data: {}", data);
            return new JsonBean(1, "查询成功", data).toString();

        } catch (Exception e) {
            log.error("查询授信监控统计失败, orgid: {}", orgid, e);
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("totalCredit", 0);
            errorData.put("activeCredit", 0);
            errorData.put("totalLimit", 0);
            errorData.put("usedLimit", 0);
            errorData.put("orgId", orgid != null ? orgid : 0);
            errorData.put("error", e.getMessage());
            return new JsonBean(0, "查询失败: " + e.getMessage(), errorData).toString();
        }
    }

    // ==================== 租赁资产管理 ====================

    @PostMapping("/lease/asset/list")
    @ApiOperation("获取租赁资产列表")
    public String getLeaseAssetList(
            @ApiParam(value = "租赁ID", required = true) @RequestParam Long leaseId) {
        try {
            log.info("获取租赁资产列表, leaseId: {}", leaseId);
            List<TblLeaseAsset> assets = leaseAssetService.getAssetsByLeaseId(leaseId);
            Map<String, Object> stats = leaseAssetService.getAssetStatistics(leaseId);

            Map<String, Object> data = new HashMap<>();
            data.put("list", assets);
            data.put("statistics", stats);

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("获取租赁资产列表失败, leaseId: {}", leaseId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/asset/detail")
    @ApiOperation("获取资产详情")
    public String getLeaseAssetDetail(
            @ApiParam(value = "资产ID", required = true) @RequestParam Long assetId) {
        try {
            TblLeaseAsset asset = leaseAssetService.getAssetById(assetId);
            return new JsonBean(1, "查询成功", asset).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("获取资产详情失败, assetId: {}", assetId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/asset/save")
    @ApiOperation("保存租赁资产")
    public String saveLeaseAsset(@FlexibleRequestBody TblLeaseAsset asset) {
        try {
            log.info("保存租赁资产, leaseId: {}, assetName: {}", asset.getLeaseId(), asset.getAssetName());
            TblLeaseAsset saved = leaseAssetService.saveAsset(asset);
            return new JsonBean(1, "保存成功", saved).toString();
        } catch (Exception e) {
            log.error("保存租赁资产失败", e);
            return new JsonBean(0, "保存失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/asset/delete")
    @ApiOperation("删除租赁资产")
    public String deleteLeaseAsset(
            @ApiParam(value = "资产ID", required = true) @RequestParam Long assetId) {
        try {
            leaseAssetService.deleteAsset(assetId);
            return new JsonBean(1, "删除成功", null).toString();
        } catch (Exception e) {
            log.error("删除租赁资产失败, assetId: {}", assetId, e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/asset/batchDelete")
    @ApiOperation("批量删除租赁资产")
    public String batchDeleteLeaseAssets(@RequestParam(value = "assetIds", required = false) List<Long> assetIds) {
        try {
            leaseAssetService.batchDeleteAssets(assetIds);
            return new JsonBean(1, "删除成功", null).toString();
        } catch (Exception e) {
            log.error("批量删除租赁资产失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toString();
        }
    }

    // ==================== 租金计划管理 ====================

    @PostMapping("/lease/payment/list")
    @ApiOperation("获取租金计划列表")
    public String getLeasePaymentList(
            @ApiParam(value = "租赁ID", required = true) @RequestParam Long leaseId) {
        try {
            log.info("获取租金计划列表, leaseId: {}", leaseId);
            List<TblLeasePayment> payments = leasePaymentService.getPaymentsByLeaseId(leaseId);
            Map<String, Object> summary = leasePaymentService.getPaymentSummary(leaseId);

            Map<String, Object> data = new HashMap<>();
            data.put("list", payments);
            data.put("summary", summary);

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("获取租金计划列表失败, leaseId: {}", leaseId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/payment/confirm")
    @ApiOperation("确认付款")
    public String confirmLeasePayment(
            @ApiParam(value = "付款ID", required = true) @RequestParam Long paymentId,
            @ApiParam(value = "实付金额") @RequestParam(required = false) BigDecimal paidAmount) {
        try {
            log.info("确认付款, paymentId: {}, paidAmount: {}", paymentId, paidAmount);
            TblLeasePayment payment = leasePaymentService.getPaymentById(paymentId);
            BigDecimal amount = paidAmount != null ? paidAmount : payment.getAmount();
            leasePaymentService.confirmPayment(paymentId, amount, new Date());
            return new JsonBean(1, "付款确认成功", null).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("确认付款失败, paymentId: {}", paymentId, e);
            return new JsonBean(0, "付款确认失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/payment/batchConfirm")
    @ApiOperation("批量确认付款")
    public String batchConfirmLeasePayments(@RequestParam(value = "paymentIds", required = false) List<Long> paymentIds) {
        try {
            log.info("批量确认付款, count: {}", paymentIds.size());
            leasePaymentService.batchConfirmPayments(paymentIds, new Date());
            return new JsonBean(1, "批量付款确认成功", null).toString();
        } catch (Exception e) {
            log.error("批量确认付款失败", e);
            return new JsonBean(0, "批量付款确认失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/payment/summary")
    @ApiOperation("获取租金汇总信息")
    public String getLeasePaymentSummary(
            @ApiParam(value = "租赁ID", required = true) @RequestParam Long leaseId) {
        try {
            Map<String, Object> summary = leasePaymentService.getPaymentSummary(leaseId);
            return new JsonBean(1, "查询成功", summary).toString();
        } catch (Exception e) {
            log.error("获取租金汇总信息失败, leaseId: {}", leaseId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/payment/overdue")
    @ApiOperation("获取逾期租金列表")
    public String getOverduePayments(
            @ApiParam(value = "租赁ID", required = true) @RequestParam Long leaseId) {
        try {
            List<TblLeasePayment> overduePayments = leasePaymentService.getOverduePayments(leaseId);
            return new JsonBean(1, "查询成功", overduePayments).toString();
        } catch (Exception e) {
            log.error("获取逾期租金列表失败, leaseId: {}", leaseId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    // ==================== 退租管理 ====================

    @PostMapping("/lease/return/detail")
    @ApiOperation("获取退租申请详情")
    public String getLeaseReturnDetail(
            @ApiParam(value = "租赁ID", required = true) @RequestParam Long leaseId) {
        try {
            TblLeaseReturn leaseReturn = leaseReturnService.getReturnByLeaseId(leaseId);
            return new JsonBean(1, "查询成功", leaseReturn).toString();
        } catch (Exception e) {
            log.error("获取退租申请详情失败, leaseId: {}", leaseId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/return/save")
    @ApiOperation("保存退租申请（草稿）")
    public String saveLeaseReturn(@FlexibleRequestBody TblLeaseReturn leaseReturn) {
        try {
            log.info("保存退租申请, leaseId: {}", leaseReturn.getLeaseId());
            TblLeaseReturn saved = leaseReturnService.saveReturn(leaseReturn);
            return new JsonBean(1, "保存成功", saved).toString();
        } catch (Exception e) {
            log.error("保存退租申请失败", e);
            return new JsonBean(0, "保存失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/return/submit")
    @ApiOperation("提交退租申请")
    public String submitLeaseReturn(
            @ApiParam(value = "退租ID", required = true) @RequestParam Long returnId) {
        try {
            log.info("提交退租申请, returnId: {}", returnId);
            leaseReturnService.submitReturn(returnId);
            return new JsonBean(1, "提交成功", null).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("提交退租申请失败, returnId: {}", returnId, e);
            return new JsonBean(0, "提交失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/return/approve")
    @ApiOperation("审批通过退租申请")
    public String approveLeaseReturn(
            @ApiParam(value = "退租ID", required = true) @RequestParam Long returnId,
            @ApiParam(value = "审批意见") @RequestParam(required = false) String comments) {
        try {
            log.info("审批通过退租申请, returnId: {}", returnId);
            leaseReturnService.approveReturn(returnId, comments, 1L); // TODO: 获取当前用户ID
            return new JsonBean(1, "审批通过", null).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("审批退租申请失败, returnId: {}", returnId, e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/return/reject")
    @ApiOperation("拒绝退租申请")
    public String rejectLeaseReturn(
            @ApiParam(value = "退租ID", required = true) @RequestParam Long returnId,
            @ApiParam(value = "拒绝原因", required = true) @RequestParam String comments) {
        try {
            log.info("拒绝退租申请, returnId: {}", returnId);
            leaseReturnService.rejectReturn(returnId, comments, 1L); // TODO: 获取当前用户ID
            return new JsonBean(1, "已拒绝", null).toString();
        } catch (ServiceException e) {
            return new JsonBean(0, e.getMessage(), null).toString();
        } catch (Exception e) {
            log.error("拒绝退租申请失败, returnId: {}", returnId, e);
            return new JsonBean(0, "拒绝失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/return/calculateFees")
    @ApiOperation("计算退租费用")
    public String calculateReturnFees(
            @ApiParam(value = "租赁ID", required = true) @RequestParam Long leaseId,
            @ApiParam(value = "退租类型", required = true) @RequestParam String returnType,
            @ApiParam(value = "资产处置方式") @RequestParam(required = false) String assetDisposal) {
        try {
            Map<String, Object> fees = leaseReturnService.calculateReturnFees(leaseId, returnType, assetDisposal);
            return new JsonBean(1, "计算成功", fees).toString();
        } catch (Exception e) {
            log.error("计算退租费用失败, leaseId: {}", leaseId, e);
            return new JsonBean(0, "计算失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/lease/return/pendingList")
    @ApiOperation("获取待审批退租申请列表")
    public String getPendingReturnList() {
        try {
            List<TblLeaseReturn> list = leaseReturnService.getPendingApprovalList();
            return new JsonBean(1, "查询成功", list).toString();
        } catch (Exception e) {
            log.error("获取待审批退租申请列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }
}
