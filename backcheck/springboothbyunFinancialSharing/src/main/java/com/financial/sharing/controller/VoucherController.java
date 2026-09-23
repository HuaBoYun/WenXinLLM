package com.financial.sharing.controller;

import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.TblOrganizationUtil;
import com.financial.sharing.util.JsonMapper;
import com.financial.sharing.oracle.entity.AccountingVoucherEntity;
import com.financial.sharing.service.AccountingVoucherService;
import com.financial.sharing.oracle.service.VoucherPostingLogService;
import com.financial.sharing.vo.param.AccountingVoucherQueryParam;
import com.financial.sharing.vo.param.VoucherSaveParam;
import com.financial.sharing.vo.result.AccountingVoucherVO;
import com.financial.sharing.vo.result.VoucherDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 凭证管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Api(tags = "凭证管理")
@RestController
@RequestMapping("/voucher")
@CrossOrigin
public class VoucherController {

    private static final Logger log = LoggerFactory.getLogger(VoucherController.class);

    @Resource
    private UserProvider userProvider;

    @Resource
    private AccountingVoucherService accountingVoucherService;

    @Resource
    private VoucherPostingLogService voucherPostingLogService;

    @ApiOperation("分页查询凭证列表")
    @PostMapping("/page")
    public String getVoucherPage(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            // 构建查询参数
            AccountingVoucherQueryParam queryParam = buildQueryParam(param, loginStaff);

            // 分页查询
            com.github.pagehelper.PageInfo<AccountingVoucherVO> pageInfo = accountingVoucherService.getVoucherPage(queryParam);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(data);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询凭证列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询凭证列表")
    @PostMapping("/getList")
    public String getVoucherList(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestBody Map<String, Object> param) {
        // 直接调用page方法，保持向后兼容
        return getVoucherPage(request, response, param);
    }

    @ApiOperation("保存凭证")
    @PostMapping("/save")
    public String saveVoucher(HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            // 转换参数
            VoucherSaveParam saveParam = convertToSaveParam(param);
            saveParam.setBookId(loginStaff.getLinkDetp().getOrgid().longValue());

            // 保存凭证
            AccountingVoucherEntity voucher = accountingVoucherService.saveVoucher(saveParam, loginStaff);

            Map<String, Object> result = new HashMap<>();
            result.put("voucherId", voucher.getVoucherId());
            result.put("voucherNo", voucher.getVoucherNo());
            result.put("status", voucher.getVoucherStatus());
            result.put("message", "凭证保存成功");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("保存成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("保存凭证失败", e);
            return createErrorResponse("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("新增凭证")
    @PostMapping("/create")
    public String createVoucher(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody Map<String, Object> param) {
        // 直接调用save方法，保持向后兼容
        return saveVoucher(request, response, param);
    }

    @ApiOperation("测试参数映射")
    @PostMapping("/testMapping")
    public String testMapping(@RequestBody Map<String, Object> param) {
        try {
            // 测试参数转换
            VoucherSaveParam saveParam = convertToSaveParam(param);

            Map<String, Object> result = new HashMap<>();
            result.put("voucherType", saveParam.getVoucherTypeId());
            result.put("voucherDate", saveParam.getVoucherDate());
            result.put("accountingPeriod", saveParam.getAccountingPeriod());
            result.put("voucherDesc", saveParam.getVoucherDesc());
            result.put("voucherStatus", saveParam.getVoucherStatus());
            result.put("attachmentCount", saveParam.getAttachmentCount());
            result.put("remark", saveParam.getRemark());
            result.put("entries", saveParam.getEntries());
            result.put("message", "参数映射成功");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("测试成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            return createErrorResponse("参数映射失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新凭证")
    @PutMapping("/update")
    public String updateVoucher(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            Long voucherId = Long.valueOf(param.get("voucherId").toString());
            if (voucherId == null) {
                return createErrorResponse("凭证ID不能为空");
            }

            // 转换参数
            VoucherSaveParam saveParam = convertToSaveParam(param);
            saveParam.setBookId(loginStaff.getLinkDetp().getOrgid().longValue());

            // 更新凭证
            AccountingVoucherEntity voucher = accountingVoucherService.updateVoucher(voucherId, saveParam, loginStaff);

            Map<String, Object> result = new HashMap<>();
            result.put("voucherId", voucher.getVoucherId());
            result.put("voucherNo", voucher.getVoucherNo());
            result.put("status", voucher.getVoucherStatus());
            result.put("message", "凭证更新成功");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("保存成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("更新凭证失败", e);
            return createErrorResponse("更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询凭证详情")
    @GetMapping("/{id}")
    public String getVoucherById(HttpServletRequest request,
                                HttpServletResponse response,
                                @PathVariable String id) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            Long voucherId = Long.valueOf(id);
            VoucherDetailVO voucher = accountingVoucherService.getVoucherDetail(voucherId);

            if (voucher == null) {
                return createErrorResponse("凭证不存在");
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(voucher);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询凭证详情失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除凭证")
    @DeleteMapping("/{id}")
    public String deleteVoucher(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String id) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            Long voucherId = Long.valueOf(id);
            boolean result = accountingVoucherService.deleteVoucher(voucherId, loginStaff);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("voucherId", voucherId);
            responseData.put("message", result ? "凭证删除成功" : "凭证删除失败");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(responseData);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("删除凭证失败", e);
            return createErrorResponse("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证审核")
    @PostMapping("/review")
    public String reviewVoucher(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            Long voucherId = Long.valueOf(param.get("voucherId").toString());
            boolean result = accountingVoucherService.reviewVoucher(voucherId, loginStaff);

            param.put("reviewedBy", loginStaff.getUsername());
            param.put("reviewTime", new Date());
            param.put("status", "REVIEWED");
            param.put("success", result);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(param);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("凭证审核失败", e);
            return createErrorResponse("审核失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证过账")
    @PostMapping("/post")
    public String postVouchers(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            @SuppressWarnings("unchecked")
            List<?> voucherIdList = (List<?>) param.get("voucherIds");
            if (CollectionUtils.isEmpty(voucherIdList)) {
                return createErrorResponse("凭证ID列表不能为空");
            }

            // 兼容数字和字符串两种类型
            List<Long> ids = voucherIdList.stream()
                    .map(id -> {
                        if (id instanceof Long) {
                            return (Long) id;
                        } else if (id instanceof Integer) {
                            return ((Integer) id).longValue();
                        } else if (id instanceof Number) {
                            return ((Number) id).longValue();
                        } else {
                            return Long.valueOf(id.toString());
                        }
                    })
                    .collect(Collectors.toList());

            log.info("用户 {} 开始过账凭证，凭证数量：{}", loginStaff.getStaffid(), ids.size());

            Map<String, Object> result = accountingVoucherService.postVouchers(ids, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("过账成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("凭证过账失败", e);
            return createErrorResponse("过账失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证反过账")
    @PostMapping("/unpost")
    public String unpostVouchers(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            @SuppressWarnings("unchecked")
            List<String> voucherIds = (List<String>) param.get("voucherIds");
            if (CollectionUtils.isEmpty(voucherIds)) {
                return createErrorResponse("凭证ID列表不能为空");
            }

            String reason = (String) param.get("reason");
            if (!StringUtils.hasText(reason)) {
                return createErrorResponse("反过账原因不能为空");
            }

            List<Long> ids = voucherIds.stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            log.info("用户 {} 开始反过账凭证，凭证数量：{}，原因：{}", loginStaff.getStaffid(), ids.size(), reason);

            Map<String, Object> result = accountingVoucherService.unpostVouchers(ids, reason, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("保存成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("凭证反过账失败", e);
            return createErrorResponse("反过账失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询过账状态")
    @GetMapping("/post/status")
    public String getPostStatus(HttpServletRequest request,
                                HttpServletResponse response,
                                @ApiParam(value = "凭证ID", required = false) @RequestParam(required = false) String voucherId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            if (!StringUtils.hasText(voucherId)) {
                return createErrorResponse("凭证ID不能为空");
            }

            Long id = Long.valueOf(voucherId);
            VoucherDetailVO voucherDetail = accountingVoucherService.getVoucherDetail(id);

            if (voucherDetail == null) {
                return createErrorResponse("凭证不存在");
            }

            Map<String, Object> status = new HashMap<>();
            status.put("voucherId", voucherId);
            status.put("postStatus", voucherDetail.getVoucherStatus());
            status.put("postStatusName", getStatusName(voucherDetail.getVoucherStatus()));
            status.put("postTime", voucherDetail.getUpdateTime());
            status.put("postedBy", voucherDetail.getUpdater());
            status.put("canUnpost", voucherDetail.getVoucherStatus() != null && voucherDetail.getVoucherStatus().equals(3)); // 已过账状态可以反过账

            JsonBean json = new JsonBean();
        json.setCode(1);
        json.setMsg("查询成功");
        json.setData(status);
        return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询过账状态失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证审批")
    @PostMapping("/approve")
    public String approveVoucher(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            Long voucherId = Long.valueOf(param.get("voucherId").toString());
            String approvalRemark = (String) param.get("approvalRemark");

            log.info("用户 {} 审批凭证，凭证ID：{}，备注：{}", loginStaff.getStaffid(), voucherId, approvalRemark);

            boolean result = accountingVoucherService.approveVoucher(voucherId, approvalRemark, loginStaff);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("voucherId", voucherId);
            responseData.put("status", "APPROVED");
            responseData.put("statusName", "已审批");
            responseData.put("approvedBy", loginStaff.getUsername());
            responseData.put("approvedTime", new Date());
            responseData.put("approvalRemark", approvalRemark);
            responseData.put("success", result);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(responseData);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("凭证审批失败", e);
            return createErrorResponse("审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证驳回")
    @PostMapping("/reject")
    public String rejectVoucher(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            Long voucherId = Long.valueOf(param.get("voucherId").toString());
            String rejectReason = (String) param.get("rejectReason");

            if (!StringUtils.hasText(rejectReason)) {
                return createErrorResponse("驳回原因不能为空");
            }

            log.info("用户 {} 驳回凭证，凭证ID：{}，原因：{}", loginStaff.getStaffid(), voucherId, rejectReason);

            boolean result = accountingVoucherService.rejectVoucher(voucherId, rejectReason, loginStaff);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("voucherId", voucherId);
            responseData.put("status", "REJECTED");
            responseData.put("statusName", "已驳回");
            responseData.put("rejectedBy", loginStaff.getUsername());
            responseData.put("rejectedTime", new Date());
            responseData.put("rejectReason", rejectReason);
            responseData.put("success", result);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(responseData);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("凭证驳回失败", e);
            return createErrorResponse("驳回失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证提交")
    @PostMapping("/submit")
    public String submitVoucher(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            Long voucherId = Long.valueOf(param.get("voucherId").toString());
            log.info("用户 {} 提交凭证，凭证ID：{}", loginStaff.getStaffid(), voucherId);

            boolean result = accountingVoucherService.submitVoucher(voucherId, loginStaff);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("voucherId", voucherId);
            responseData.put("status", "SUBMITTED");
            responseData.put("statusName", "已提交");
            responseData.put("submittedBy", loginStaff.getUsername());
            responseData.put("submittedTime", new Date());
            responseData.put("success", result);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(responseData);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("凭证提交失败", e);
            return createErrorResponse("提交失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询审批历史")
    @GetMapping("/approval-history/{id}")
    public String getApprovalHistory(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable String id) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            Long voucherId = Long.valueOf(id);
            List<Map<String, Object>> history = accountingVoucherService.getApprovalHistory(voucherId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(history);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询审批历史失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量审批")
    @PostMapping("/batch-approve")
    public String batchApproveVouchers(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            @SuppressWarnings("unchecked")
            List<String> voucherIds = (List<String>) param.get("voucherIds");
            if (CollectionUtils.isEmpty(voucherIds)) {
                return createErrorResponse("凭证ID列表不能为空");
            }

            List<Long> ids = voucherIds.stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            String approvalRemark = (String) param.get("approvalRemark");

            log.info("用户 {} 批量审批凭证，凭证数量：{}，备注：{}", loginStaff.getStaffid(), ids.size(), approvalRemark);

            Map<String, Object> result = accountingVoucherService.batchApproveVouchers(ids, approvalRemark, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("保存成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("批量审批失败", e);
            return createErrorResponse("批量审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除会计凭证")
    @DeleteMapping("/batch")
    public String batchDeleteVouchers(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            @SuppressWarnings("unchecked")
            List<String> voucherIds = (List<String>) param.get("voucherIds");
            if (CollectionUtils.isEmpty(voucherIds)) {
                return createErrorResponse("凭证ID列表不能为空");
            }

            List<Long> ids = voucherIds.stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            log.info("用户 {} 批量删除凭证，数量：{}", loginStaff.getStaffid(), ids.size());

            int count = accountingVoucherService.batchDeleteVouchers(ids, loginStaff);

            Map<String, Object> result = new HashMap<>();
            result.put("successCount", count);
            result.put("failCount", ids.size() - count);
            result.put("totalDeleted", count);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("保存成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("批量删除凭证失败", e);
            return createErrorResponse("批量删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出凭证数据")
    @PostMapping("/export")
    public String exportVouchers(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            log.info("用户 {} 导出凭证数据", loginStaff.getStaffid());

            Map<String, Object> result = new HashMap<>();
            result.put("taskId", "EXPORT" + System.currentTimeMillis());
            result.put("fileName", "凭证导出_" + new Date() + ".xlsx");
            result.put("status", "PROCESSING");
            result.put("estimatedTime", "2分钟");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("保存成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("导出凭证数据失败", e);
            return createErrorResponse("导出失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新凭证状态")
    @PutMapping("/{id}/status")
    public String updateVoucherStatus(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @PathVariable String id,
                                      @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            Long voucherId = Long.valueOf(id);
            Integer status = Integer.valueOf(param.get("status").toString());

            log.info("用户 {} 更新凭证状态，ID：{}，状态：{}", loginStaff.getStaffid(), voucherId, status);

            boolean result = accountingVoucherService.updateVoucherStatus(voucherId, status, loginStaff);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("voucherId", voucherId);
            responseData.put("newStatus", status);
            responseData.put("updatedBy", loginStaff.getUsername());
            responseData.put("updateTime", new Date());
            responseData.put("success", result);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(responseData);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("更新凭证状态失败", e);
            return createErrorResponse("状态更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量更新凭证状态")
    @PutMapping("/batch/status")
    public String batchUpdateVoucherStatus(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            @SuppressWarnings("unchecked")
            List<String> voucherIds = (List<String>) param.get("voucherIds");
            Integer status = Integer.valueOf(param.get("status").toString());

            if (CollectionUtils.isEmpty(voucherIds)) {
                return createErrorResponse("凭证ID列表不能为空");
            }

            List<Long> ids = voucherIds.stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            log.info("用户 {} 批量更新凭证状态，数量：{}，状态：{}", loginStaff.getStaffid(), ids.size(), status);

            int count = accountingVoucherService.batchUpdateVoucherStatus(ids, status, loginStaff);

            Map<String, Object> result = new HashMap<>();
            result.put("successCount", count);
            result.put("failCount", ids.size() - count);
            result.put("totalUpdated", count);
            result.put("status", status);
            result.put("updatedBy", loginStaff.getUsername());
            result.put("updateTime", new Date());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("保存成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("批量更新凭证状态失败", e);
            return createErrorResponse("批量状态更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取凭证统计数据")
    @PostMapping("/statistics")
    public String getVoucherStatistics(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            log.info("用户 {} 获取凭证统计数据", loginStaff.getStaffid());

            AccountingVoucherQueryParam queryParam = buildQueryParam(param, loginStaff);
            Map<String, Object> statistics = accountingVoucherService.getVoucherStatistics(queryParam, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(statistics);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("获取凭证统计数据失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    /**
     * 验证用户权限
     */
    private TblStaffUtil validateUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }
            return loginStaff;
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return null;
        }
    }

    /**
     * 构建查询参数
     */
    private AccountingVoucherQueryParam buildQueryParam(Map<String, Object> param, TblStaffUtil loginStaff) {
        AccountingVoucherQueryParam queryParam = new AccountingVoucherQueryParam();

        // 设置分页参数
        queryParam.setPageNo(param.get("pageNo") != null ? (Integer) param.get("pageNo") : 1);
        queryParam.setPageSize(param.get("pageSize") != null ? (Integer) param.get("pageSize") : 10);

        // 设置基础参数
        queryParam.setBookId(loginStaff.getLinkDetp().getOrgid().longValue());
        queryParam.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());

        // 设置查询条件
        if (param.containsKey("voucherNo")) {
            queryParam.setVoucherNo((String) param.get("voucherNo"));
        }
        if (param.containsKey("voucherTypeId")) {
            queryParam.setVoucherTypeId(Long.valueOf(param.get("voucherTypeId").toString()));
        }
        if (param.containsKey("accountingPeriod")) {
            queryParam.setAccountingPeriod((String) param.get("accountingPeriod"));
        }
        if (param.containsKey("voucherStatus")) {
            queryParam.setVoucherStatus(Integer.valueOf(param.get("voucherStatus").toString()));
        }
        if (param.containsKey("startDate")) {
            queryParam.setStartDate((String) param.get("startDate"));
        }
        if (param.containsKey("endDate")) {
            queryParam.setEndDate((String) param.get("endDate"));
        }

        return queryParam;
    }

    /**
     * 转换保存参数
     */
    private VoucherSaveParam convertToSaveParam(Map<String, Object> param) {
        VoucherSaveParam saveParam = new VoucherSaveParam();

        if (param.containsKey("voucherId")) {
            saveParam.setVoucherId(Long.valueOf(param.get("voucherId").toString()));
        }
        if (param.containsKey("voucherNo")) {
            saveParam.setVoucherNo((String) param.get("voucherNo"));
        }
        if (param.containsKey("voucherTypeId")) {
            saveParam.setVoucherTypeId(Long.valueOf(param.get("voucherTypeId").toString()));
        } else if (param.containsKey("voucherType")) {
            // 前端兼容字段名
            saveParam.setVoucherTypeId(Long.valueOf(param.get("voucherType").toString()));
        }
        if (param.containsKey("voucherDate")) {
            java.time.LocalDate voucherDate = java.time.LocalDate.parse(param.get("voucherDate").toString());
            saveParam.setVoucherDate(voucherDate);

            // 如果没有提供会计期间，根据凭证日期自动生成
            if (!param.containsKey("accountingPeriod")) {
                String accountingPeriod = voucherDate.getYear() + String.format("%02d", voucherDate.getMonthValue());
                saveParam.setAccountingPeriod(accountingPeriod);
                log.info("自动生成会计期间: {} (基于凭证日期: {})", accountingPeriod, voucherDate);
            } else {
                saveParam.setAccountingPeriod((String) param.get("accountingPeriod"));
            }
        } else if (param.containsKey("accountingPeriod")) {
            saveParam.setAccountingPeriod((String) param.get("accountingPeriod"));
        }
        if (param.containsKey("voucherDesc")) {
            saveParam.setVoucherDesc((String) param.get("voucherDesc"));
        } else if (param.containsKey("summary")) {
            // 前端兼容字段名
            saveParam.setVoucherDesc((String) param.get("summary"));
        }
        if (param.containsKey("sourceTransactionId")) {
            saveParam.setSourceTransactionId(Long.valueOf(param.get("sourceTransactionId").toString()));
        }
        if (param.containsKey("sourceSystem")) {
            saveParam.setSourceSystem((String) param.get("sourceSystem"));
        }
        if (param.containsKey("attachmentCount")) {
            saveParam.setAttachmentCount((Integer) param.get("attachmentCount"));
        }
        if (param.containsKey("remark")) {
            saveParam.setRemark((String) param.get("remark"));
        }
        if (param.containsKey("voucherStatus")) {
            saveParam.setVoucherStatus((Integer) param.get("voucherStatus"));
        } else if (param.containsKey("status")) {
            // 前端兼容字段名
            saveParam.setVoucherStatus((Integer) param.get("status"));
        }
        if (param.containsKey("entries")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> entries = (List<Map<String, Object>>) param.get("entries");
            saveParam.setEntries(entries);
        }

        return saveParam;
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(Integer status) {
        switch (status) {
            case 1: return "草稿";
            case 2: return "已保存";
            case 3: return "已提交";
            case 4: return "已审核";
            case 5: return "已过账";
            case 6: return "已取消";
            default: return "未知状态";
        }
    }

    /**
     * 创建测试用户（临时用于测试）
     */
    private TblStaffUtil createTestUser() {
        TblStaffUtil loginStaff = new TblStaffUtil();
        loginStaff.setStaffid(new java.math.BigDecimal(5555));
        loginStaff.setUsername("testuser");
        loginStaff.setRealname("测试用户");

        // 创建测试部门
        TblOrganizationUtil linkDept = new TblOrganizationUtil();
        linkDept.setOrgid(new java.math.BigDecimal(1));
        linkDept.setOrgname("测试部门");
        loginStaff.setLinkDetp(linkDept);

        // 创建测试组织
        TblOrganizationUtil currentOrg = new TblOrganizationUtil();
        currentOrg.setOrgid(new java.math.BigDecimal(1));
        currentOrg.setOrgname("测试组织");
        loginStaff.setCurrentOrg(currentOrg);

        return loginStaff;
    }

    /**
     * 创建错误响应
     */
    private String createErrorResponse(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return JsonMapper.toJson(json);
    }

    @ApiOperation("导入凭证数据")
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public String importVouchers(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            log.info("开始导入凭证，文件名：{}，大小：{}", file.getOriginalFilename(), file.getSize());

            // 验证文件
            if (file.isEmpty()) {
                return createErrorResponse("文件不能为空");
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                return createErrorResponse("只支持Excel文件格式(.xlsx或.xls)");
            }

            // 调用Service处理导入
            Map<String, Object> result = accountingVoucherService.importVouchers(
                file.getInputStream(),
                fileName,
                loginStaff.getLinkDetp().getOrgid().longValue()
            );

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("导入成功");
            json.setData(result);
            return JsonMapper.toJson(json);

        } catch (Exception e) {
            log.error("导入凭证失败", e);
            return createErrorResponse("导入失败: " + e.getMessage());
        }
    }

    @ApiOperation("下载凭证导入模板")
    @GetMapping("/import-template")
    public void downloadImportTemplate(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");

            String fileName = java.net.URLEncoder.encode("凭证导入模板", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 创建模板数据（这里可以集成EasyExcel或其他Excel工具）
            // 暂时返回空模板，前端可以使用
            log.info("下载凭证导入模板");

            // TODO: 使用EasyExcel生成标准模板
            // EasyExcel.write(response.getOutputStream(), VoucherImportDTO.class)
            //     .sheet("凭证数据")
            //     .doWrite(new ArrayList<>());

        } catch (Exception e) {
            log.error("下载模板失败", e);
        }
    }

    @ApiOperation("获取凭证过账日志")
    @GetMapping("/{voucherId}/posting-logs")
    public String getVoucherPostingLogs(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @PathVariable String voucherId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            if (!StringUtils.hasText(voucherId)) {
                return createErrorResponse("凭证ID不能为空");
            }

            log.info("查询凭证过账日志，凭证ID：{}", voucherId);

            // 从数据库查询过账日志
            Long id = Long.valueOf(voucherId);
            List<Map<String, Object>> postingLogs = voucherPostingLogService.getLogsByVoucherId(id);
            Long total = voucherPostingLogService.getLogsCountByVoucherId(id);

            // 添加操作类型名称
            for (Map<String, Object> logItem : postingLogs) {
                String operationType = (String) logItem.get("operationType");
                if ("POST".equals(operationType)) {
                    logItem.put("operationTypeName", "过账");
                } else if ("UNPOST".equals(operationType)) {
                    logItem.put("operationTypeName", "反过账");
                }

                String operationStatus = (String) logItem.get("operationStatus");
                if ("SUCCESS".equals(operationStatus)) {
                    logItem.put("statusName", "成功");
                } else if ("FAILED".equals(operationStatus)) {
                    logItem.put("statusName", "失败");
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("logs", postingLogs);
            data.put("total", total != null ? total : 0);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(data);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询凭证过账日志失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("分页查询反过账记录")
    @PostMapping("/unpost-record/page")
    public String getUnpostRecordPage(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            log.info("查询反过账记录，参数：{}", param);

            Map<String, Object> result = accountingVoucherService.getUnpostRecordPage(param, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询反过账记录失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }
}