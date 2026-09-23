package com.global.treasurer.controller;

import com.global.treasurer.entity.FundTransfer;
import com.global.treasurer.service.FundTransferService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 资金调拨控制器
 *
 * @author AI Developer
 * @date 2025-01-15
 */
@RestController
@RequestMapping("/cash/transfer")
@Api(tags = "资金调拨管理")
public class FundTransferController {
    @Resource
    private FundTransferService fundTransferService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询资金调拨列表(GET方式,兼容前端请求)
     */
    @GetMapping("/page")
    @ApiOperation("分页查询资金调拨列表")
    public String getTransferPageGet(@RequestParam(required = false) Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = fundTransferService.getTransferPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 分页查询资金调拨列表(POST方式)
     */
    @PostMapping("/page")
    @ApiOperation("分页查询资金调拨列表")
    public String getTransferPage(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = fundTransferService.getTransferPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询资金调拨详情
     */
    @GetMapping("/{transferId}")
    @ApiOperation("获取资金调拨详情")
    public String getTransfer(@PathVariable Long transferId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            FundTransfer transfer = fundTransferService.getTransferById(transferId);
            return JsonBean.success(transfer);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建资金调拨单
     */
    @PostMapping
    @ApiOperation("创建资金调拨单")
    public String createTransfer(@FlexibleRequestBody FundTransfer transfer) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundTransferService.createTransfer(transfer);
            if (result > 0) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新资金调拨单
     */
    @PutMapping
    @ApiOperation("更新资金调拨单")
    public String updateTransfer(@FlexibleRequestBody FundTransfer transfer) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundTransferService.updateTransfer(transfer);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除资金调拨单
     */
    @DeleteMapping
    @ApiOperation("批量删除资金调拨单")
    public String deleteTransfer(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = (List<Long>) params.get("ids");
            int result = fundTransferService.batchDelete(ids);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量提交审批
     */
    @PutMapping("/submit")
    @ApiOperation("批量提交审批")
    public String submitTransfer(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = (List<Long>) params.get("ids");
            int result = fundTransferService.batchSubmit(ids, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("提交成功");
            } else {
                return new JsonBean(0, "提交失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "提交失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量执行调拨
     */
    @PutMapping("/execute")
    @ApiOperation("批量执行调拨")
    public String executeTransfer(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = (List<Long>) params.get("ids");
            int result = fundTransferService.batchExecute(ids, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("执行成功");
            } else {
                return new JsonBean(0, "执行失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "执行失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取调拨概览统计(GET方式,兼容前端请求)
     */
    @GetMapping("/statistics")
    @ApiOperation("获取调拨概览统计")
    public String getTransferStatisticsGet(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = fundTransferService.getTransferStatistics(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取调拨概览统计(POST方式)
     */
    @PostMapping("/statistics")
    @ApiOperation("获取调拨概览统计")
    public String getTransferStatisticsPost(@RequestParam(required = false) Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = fundTransferService.getTransferStatistics(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量审批调拨单
     */
    @PutMapping("/batch-approve")
    @ApiOperation("批量审批调拨单")
    public String batchApproveTransfer(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = (List<Long>) params.get("ids");
            String approvalComment = params.get("approvalComment") != null ? params.get("approvalComment").toString() : "";
            int result = fundTransferService.batchApprove(ids, loginStaff.getRealname(), approvalComment);
            if (result > 0) {
                return JsonBean.success("审批成功");
            } else {
                return new JsonBean(0, "审批失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 取消资金调拨
     */
    @PutMapping("/{transferId}/cancel")
    @ApiOperation("取消资金调拨")
    public String cancelTransfer(@PathVariable Long transferId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundTransferService.cancelTransfer(transferId, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("取消成功");
            } else {
                return new JsonBean(0, "取消失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "取消失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批资金调拨
     */
    @PutMapping("/{transferId}/approve")
    @ApiOperation("审批资金调拨")
    public String approveTransfer(@PathVariable Long transferId, @RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String status = params.get("status") != null ? params.get("status").toString() : "APPROVED";
            String approvalComment = params.get("approvalComment") != null ? params.get("approvalComment").toString() : "";

            int result = fundTransferService.approveTransfer(transferId, status, loginStaff.getRealname(), approvalComment);
            if (result > 0) {
                return JsonBean.success("审批成功");
            } else {
                return new JsonBean(0, "审批失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 执行资金调拨
     */
    @PutMapping("/{transferId}/execute")
    @ApiOperation("执行资金调拨")
    public String executeTransfer(@PathVariable Long transferId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundTransferService.executeTransfer(transferId, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("执行成功");
            } else {
                return new JsonBean(0, "执行失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "执行失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取可用账户列表(GET方式,兼容前端请求)
     */
    @GetMapping("/available-accounts")
    @ApiOperation("获取可用账户列表")
    public String getAvailableAccountsGet(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Map<String, Object>> result = fundTransferService.getAvailableAccounts(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}
