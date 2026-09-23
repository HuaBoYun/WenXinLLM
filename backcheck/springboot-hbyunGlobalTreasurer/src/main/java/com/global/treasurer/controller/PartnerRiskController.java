package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcPartnerRiskList;
import com.global.treasurer.service.TcPartnerRiskService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 合作伙伴风险管理控制器
 * 
 * @author AI Assistant
 * @date 2025-01-26
 */
@RestController
@RequestMapping("/settlement/partner-risk-list")
@Api(tags = "合作伙伴风险管理")
public class PartnerRiskController {
    @Resource
    private TcPartnerRiskService partnerRiskService;

    @Resource
    private UserProvider userProvider;

    @PostMapping("/page")
    @ApiOperation("分页查询风险评估列表")
    public String getPage(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            
            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            
            PageInfo<TcPartnerRiskList> pageInfo = partnerRiskService.list(pageNum, pageSize, params);
            
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("获取风险评估详情")
    public String getDetail(@PathVariable String id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            
            TcPartnerRiskList riskList = partnerRiskService.getById(id);
            if (riskList == null) {
                return new JsonBean(0, "评估记录不存在", null).toJson();
            }
            return JsonBean.success(riskList);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ApiOperation("新增风险评估")
    public String add(@FlexibleRequestBody TcPartnerRiskList riskList) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            
            riskList.setCreateUser(loginStaff.getStaffid().toString());
            riskList.setUpdateUser(loginStaff.getStaffid().toString());
            
            boolean result = partnerRiskService.save(riskList);
            if (result) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ApiOperation("更新风险评估")
    public String update(@FlexibleRequestBody TcPartnerRiskList riskList) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            
            riskList.setUpdateUser(loginStaff.getStaffid().toString());
            
            boolean result = partnerRiskService.update(riskList);
            if (result) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除风险评估")
    public String delete(@PathVariable String id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean result = partnerRiskService.delete(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除风险评估")
    public String batchDelete(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            boolean result = partnerRiskService.batchDelete(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return new JsonBean(0, "批量删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batch-assess")
    @ApiOperation("批量评估")
    public String batchAssess(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            @SuppressWarnings("unchecked")
            List<String> partnerIds = (List<String>) params.get("partners");
            Map<String, Object> result = partnerRiskService.batchAssess(partnerIds, loginStaff.getStaffid().toString());
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "批量评估失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取风险统计信息")
    public String getStatistics() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> statistics = partnerRiskService.getRiskStatistics();
            return JsonBean.success(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "获取统计失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/alerts")
    @ApiOperation("获取风险预警列表")
    public String getAlerts() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<TcPartnerRiskList> alerts = partnerRiskService.getRiskAlerts();
            return JsonBean.success(alerts);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "获取预警失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/update-status")
    @ApiOperation("更新风险状态")
    public String updateStatus(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String id = (String) params.get("id");
            String riskStatus = (String) params.get("riskStatus");

            boolean result = partnerRiskService.updateRiskStatus(id, riskStatus, loginStaff.getStaffid().toString());
            if (result) {
                return JsonBean.success("状态更新成功");
            } else {
                return new JsonBean(0, "状态更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "状态更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出风险报告")
    public String export(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            PageInfo<TcPartnerRiskList> pageInfo = partnerRiskService.list(1, 10000, params);

            Map<String, Object> result = new HashMap<>();
            result.put("data", pageInfo.getList());
            result.put("exportTime", new Date());

            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/risk-levels")
    @ApiOperation("获取风险等级列表")
    public String getRiskLevels() {
        try {
            List<Map<String, String>> levels = new ArrayList<>();
            levels.add(createOption("HIGH", "高风险"));
            levels.add(createOption("MEDIUM", "中风险"));
            levels.add(createOption("LOW", "低风险"));
            return JsonBean.success(levels);
        } catch (Exception e) {
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/assessment-types")
    @ApiOperation("获取评估类型列表")
    public String getAssessmentTypes() {
        try {
            List<Map<String, String>> types = new ArrayList<>();
            types.add(createOption("WHITE", "白名单"));
            types.add(createOption("BLACK", "黑名单"));
            types.add(createOption("GRAY", "灰名单"));
            return JsonBean.success(types);
        } catch (Exception e) {
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    private Map<String, String> createOption(String value, String label) {
        Map<String, String> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }
}

