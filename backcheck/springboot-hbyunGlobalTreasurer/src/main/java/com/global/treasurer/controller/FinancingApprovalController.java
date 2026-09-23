package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingApprovalDTO;
import com.global.treasurer.dto.FinancingApprovalQueryDTO;
import com.global.treasurer.entity.TblFinancingApproval;
import com.global.treasurer.service.FinancingApprovalService;
import com.hbfk.util.JsonBean;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资审批Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Controller
@RequestMapping({"/rzgl/financing-approval", "/financial/rzgl/financing-approval", "/centralaudit/rzgl/financing-approval"})
@Api(tags = "融资审批管理")
public class FinancingApprovalController {
    private static final Logger log = LoggerFactory.getLogger(FinancingApprovalController.class);

    @Resource
    private FinancingApprovalService financingApprovalService;

    @Resource
    private UserProvider userProvider;

    @PostMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询审批列表")
    public String getApprovalList(FinancingApprovalQueryDTO queryDTO,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            PageInfo<TblFinancingApproval> pageInfo = financingApprovalService.getApprovalList(queryDTO);

            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询融资审批列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    @ApiOperation("根据ID获取审批详情")
    public String getApprovalById(@PathVariable Long id,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblFinancingApproval approval = financingApprovalService.getApprovalById(id);
            return new JsonBean(1, "成功", approval).toJson();
        } catch (Exception e) {
            log.error("获取融资审批详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/submit")
    @ResponseBody
    @ApiOperation("提交审批")
    public String submitApproval(FinancingApprovalDTO dto,
                                 @RequestHeader(value = "token", required = false) String token,
                                 HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblFinancingApproval approval = financingApprovalService.submitApproval(dto);
            return new JsonBean(1, "提交审批成功", approval).toJson();
        } catch (Exception e) {
            log.error("提交审批失败", e);
            return new JsonBean(0, "提交失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/approve")
    @ResponseBody
    @ApiOperation("审批通过")
    public String approve(@RequestParam Long approvalId,
                          @RequestParam(required = false) String comments,
                          @RequestHeader(value = "token", required = false) String token,
                          HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            financingApprovalService.approve(approvalId, comments);
            return new JsonBean(1, "审批通过成功", null).toJson();
        } catch (Exception e) {
            log.error("审批通过失败", e);
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/reject")
    @ResponseBody
    @ApiOperation("审批拒绝")
    public String reject(@RequestParam Long approvalId,
                         @RequestParam(required = false) String comments,
                         @RequestHeader(value = "token", required = false) String token,
                         HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            financingApprovalService.reject(approvalId, comments);
            return new JsonBean(1, "审批拒绝成功", null).toJson();
        } catch (Exception e) {
            log.error("审批拒绝失败", e);
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/history")
    @ResponseBody
    @ApiOperation("审批历史")
    public String getApprovalHistory(@RequestParam Long approvalId,
                                      @RequestHeader(value = "token", required = false) String token,
                                      HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<Map<String, Object>> history = financingApprovalService.getApprovalHistory(approvalId);
            return new JsonBean(1, "成功", history).toJson();
        } catch (Exception e) {
            log.error("查询审批历史失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batchApprove")
    @ResponseBody
    @ApiOperation("批量审批")
    public String batchApprove(@RequestParam List<Long> approvalIds,
                               @RequestParam Boolean approved,
                               @RequestParam(required = false) String comments,
                               @RequestHeader(value = "token", required = false) String token,
                               HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            financingApprovalService.batchApprove(approvalIds, approved, comments);
            return new JsonBean(1, "批量审批成功", null).toJson();
        } catch (Exception e) {
            log.error("批量审批失败", e);
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/cancel")
    @ResponseBody
    @ApiOperation("撤销审批")
    public String cancelApproval(@RequestParam Long approvalId,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            financingApprovalService.cancelApproval(approvalId);
            return new JsonBean(1, "撤销审批成功", null).toJson();
        } catch (Exception e) {
            log.error("撤销审批失败", e);
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }
}
