package com.global.treasurer.controller;

import com.global.treasurer.entity.TblCloudConnectionContract;
import com.global.treasurer.service.CloudConnectionContractService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 云连接合同管理Controller
 * 匹配前端API路径: /qqsk/settlement/cloud-connection-contract/*
 *
 * @author 华博云开发团队
 * @since 2025-01-27
 */
@RestController
@RequestMapping({"/settlement/cloud-connection-contract", "/settlement/cloud-contract"})
@Api(tags = "云连接合同管理")
public class CloudConnectionContractController {
    private static final Logger log = LoggerFactory.getLogger(CloudConnectionContractController.class);

    @Resource
    private CloudConnectionContractService contractService;
    @Resource
    private UserProvider userProvider;

    @GetMapping("/page")
    @ApiOperation("分页查询云连接合同")
    public String getCloudContractPage(@RequestParam Map<String, Object> params) {
        try {
            log.info("分页查询云连接合同, 参数: {}", params);
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> result = contractService.getCloudContractPage(params);
            return JsonBean.success(result);
        } catch (NumberFormatException e) {
            log.error("参数类型转换异常: {}", e.getMessage());
            log.error("请求参数详情", e);
            return new JsonBean(0, "参数格式错误: " + e.getMessage(), null).toJson();
        } catch (Exception e) {
            log.error("查询云连接合同失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("获取云连接合同详情")
    public String getDetail(@PathVariable Long id) {
        try {
            // 验证id参数
            if (id == null || id <= 0) {
                log.warn("获取合同详情，无效的id参数: {}", id);
                return new JsonBean(400, "无效的合同ID", null).toJson();
            }
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblCloudConnectionContract contract = contractService.getById(id);
            if (contract == null) {
                return new JsonBean(0, "合同不存在", null).toJson();
            }
            return JsonBean.success(contract);
        } catch (NumberFormatException e) {
            log.error("合同ID格式错误: {}", e.getMessage());
            return new JsonBean(0, "合同ID格式错误", null).toJson();
        } catch (Exception e) {
            log.error("获取云连接合同详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ApiOperation("新增云连接合同")
    public String add(@RequestBody TblCloudConnectionContract contract) {
        try {
            log.info("========== 新增云连接合同开始 ==========");
            log.info("接收到的合同数据: {}", contract);
            log.info("合同编号: {}", contract.getContractNumber());
            log.info("合同名称: {}", contract.getContractName());
            log.info("服务提供商: {}", contract.getServiceProvider());
            log.info("服务类型: {}", contract.getServiceType());

            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                log.warn("用户未登录或已失效");
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("当前登录用户: {} (ID: {})", loginStaff.getUsername(), loginStaff.getStaffid());

            // 设置创建信息
            contract.setCreatedBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            contract.setCreatedByName(loginStaff.getUsername());
            contract.setCreatedTime(new Date());
            contract.setUpdatedBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            contract.setUpdatedByName(loginStaff.getUsername());
            contract.setUpdatedTime(new Date());
            contract.setDeleteFlag(0);
            contract.setIsEnabled(1);

            log.info("准备保存合同数据 - 合同编号: {}, 合同名称: {}", contract.getContractNumber(), contract.getContractName());

            boolean result = contractService.save(contract);
            if (result) {
                log.info("合同创建成功, ID: {}", contract.getContractId());
                return JsonBean.success("创建成功");
            } else {
                log.error("合同创建失败，数据库操作返回false");
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("新增云连接合同失败", e);
            log.error("异常详情: {}", e.getMessage());
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        } finally {
            log.info("========== 新增云连接合同结束 ==========");
        }
    }

    @PutMapping("")
    @ApiOperation("更新云连接合同")
    public String update(@RequestBody TblCloudConnectionContract contract) {
        try {
            log.info("========== 更新云连接合同开始 ==========");
            log.info("接收到的合同数据: {}", contract);
            log.info("合同ID: {}", contract.getContractId());

            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                log.warn("用户未登录或已失效");
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("当前登录用户: {} (ID: {})", loginStaff.getUsername(), loginStaff.getStaffid());

            contract.setUpdatedBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            contract.setUpdatedByName(loginStaff.getUsername());
            contract.setUpdatedTime(new Date());

            boolean result = contractService.updateById(contract);
            if (result) {
                log.info("合同更新成功, ID: {}", contract.getContractId());
                return JsonBean.success("更新成功");
            } else {
                log.error("合同更新失败，数据库操作返回false");
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新云连接合同失败", e);
            log.error("异常详情: {}", e.getMessage());
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        } finally {
            log.info("========== 更新云连接合同结束 ==========");
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除云连接合同")
    public String delete(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            boolean result = contractService.removeById(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除云连接合同失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除云连接合同")
    public String batchDelete(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) params.get("ids");
            boolean result = contractService.removeByIds(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return new JsonBean(0, "批量删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("批量删除云连接合同失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取合同统计")
    public String getCloudContractStatistics(@RequestParam(required = false) Long orgId) {
        try {
            log.info("获取合同统计, orgId: {}", orgId);
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            // 统一使用 orgId = 1，因为数据库中的合同数据都是 ORG_ID = 1
            if (orgId == null) {
                orgId = 1L;
            }
            // 验证orgId参数
            if (orgId != null && orgId <= 0) {
                log.warn("获取合同统计，无效的orgId参数: {}", orgId);
                return new JsonBean(400, "无效的组织ID", null).toJson();
            }
            log.info("使用orgId: {}", orgId);
            Map<String, Object> statistics = contractService.getCloudContractStatistics(orgId);
            return JsonBean.success(statistics);
        } catch (NumberFormatException e) {
            log.error("参数类型转换异常: {}", e.getMessage());
            log.error("请求参数详情", e);
            return new JsonBean(0, "参数格式错误: " + e.getMessage(), null).toJson();
        } catch (Exception e) {
            log.error("获取合同统计失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/renewal-alert")
    @ApiOperation("获取续约提醒")
    public String getRenewalAlert() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<TblCloudConnectionContract> alerts = contractService.getRenewalAlerts();
            return JsonBean.success(alerts);
        } catch (Exception e) {
            log.error("获取续约提醒失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出云连接合同")
    public String export(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<TblCloudConnectionContract> list = contractService.exportContracts(params);
            Map<String, Object> result = new HashMap<>();
            result.put("data", list);
            result.put("exportTime", new Date());
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("导出云连接合同失败", e);
            return new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/toggle/{id}")
    @ApiOperation("启用/禁用合同")
    public String toggleEnabled(@PathVariable Long id, @RequestParam Integer enabled) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblCloudConnectionContract contract = new TblCloudConnectionContract();
            contract.setContractId(id);
            contract.setIsEnabled(enabled);
            contract.setUpdatedBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            contract.setUpdatedByName(loginStaff.getUsername());
            contract.setUpdatedTime(new Date());
            boolean result = contractService.updateById(contract);
            if (result) {
                return JsonBean.success("操作成功");
            } else {
                return new JsonBean(0, "操作失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("启用/禁用云连接合同失败", e);
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/status/{id}")
    @ApiOperation("合同状态变更")
    public String changeContractStatus(@PathVariable Long id, @RequestParam String status, @RequestParam(required = false) String reason) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblCloudConnectionContract contract = new TblCloudConnectionContract();
            contract.setContractId(id);
            contract.setContractStatus(status);
            contract.setRemark(reason);
            contract.setUpdatedBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            contract.setUpdatedByName(loginStaff.getUsername());
            contract.setUpdatedTime(new Date());
            boolean result = contractService.updateById(contract);
            if (result) {
                return JsonBean.success("状态变更成功");
            } else {
                return new JsonBean(0, "状态变更失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("变更云连接合同状态失败", e);
            return new JsonBean(0, "状态变更失败: " + e.getMessage(), null).toJson();
        }
    }
}
