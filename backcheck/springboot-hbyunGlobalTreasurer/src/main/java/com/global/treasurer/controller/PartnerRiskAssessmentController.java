package com.global.treasurer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.annotation.FlexibleRequestBody;
import com.global.treasurer.entity.TcPartnerRiskAssessment;
import com.global.treasurer.service.TcPartnerRiskAssessmentService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 合作伙伴风险评估控制器
 * 
 * @author AI Assistant
 * @date 2026-03-05
 */
@RestController
@RequestMapping("/settlement/partner-risk")
@Api(tags = "合作伙伴风险评估管理")
public class PartnerRiskAssessmentController {
    
    @Resource
    private TcPartnerRiskAssessmentService assessmentService;
    
    @Resource
    private UserProvider userProvider;

    @PostMapping("/page")
    @ApiOperation("分页查询风险评估列表")
    public String getPage(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            PageInfo<TcPartnerRiskAssessment> pageInfo = assessmentService.list(pageNum, pageSize, params);

            // 转换列表数据（配置FastJSON序列化NULL值）
            List<Map<String, Object>> convertedList = new ArrayList<>();
            for (TcPartnerRiskAssessment assessment : pageInfo.getList()) {
                // 使用SerializerFeature.WriteMapNullValue来序列化null值
                String jsonStr = JSON.toJSONString(assessment, SerializerFeature.WriteMapNullValue);
                Map<String, Object> item = JSON.parseObject(jsonStr, Map.class);
                convertedList.add(item);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", convertedList);
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

            TcPartnerRiskAssessment assessment = assessmentService.getById(id);
            if (assessment == null) {
                return new JsonBean(0, "风险评估不存在", null).toJson();
            }

            return JsonBean.success(assessment);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ApiOperation("新增风险评估")
    public String add(@FlexibleRequestBody TcPartnerRiskAssessment assessment) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            assessment.setCreateUser(loginStaff.getStaffid().toString());
            assessment.setUpdateUser(loginStaff.getStaffid().toString());

            boolean result = assessmentService.save(assessment);
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
    public String update(@FlexibleRequestBody TcPartnerRiskAssessment assessment) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            assessment.setUpdateUser(loginStaff.getStaffid().toString());

            boolean result = assessmentService.update(assessment);
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

            boolean result = assessmentService.delete(id);
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

    @GetMapping("/statistics")
    @ApiOperation("获取风险统计信息")
    public String getStatistics() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> stats = assessmentService.getStatistics();
            return JsonBean.success(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/alerts")
    @ApiOperation("获取风险预警信息")
    public String getAlerts() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取高风险和预警状态的评估
            List<TcPartnerRiskAssessment> alerts = new ArrayList<>();
            alerts.addAll(assessmentService.getByRiskLevel("B"));
            alerts.addAll(assessmentService.getByRiskLevel("C"));
            alerts.addAll(assessmentService.getByRiskStatus("WARNING"));

            return JsonBean.success(alerts);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出风险报告")
    public String exportReport(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<TcPartnerRiskAssessment> list = assessmentService.exportReport(params);

            Map<String, Object> result = new HashMap<>();
            result.put("data", list);
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
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Map<String, Object>> levels = assessmentService.getRiskLevels();
            return JsonBean.success(levels);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/assessment-types")
    @ApiOperation("获取评估类型列表")
    public String getAssessmentTypes() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Map<String, Object>> types = assessmentService.getAssessmentTypes();
            return JsonBean.success(types);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/partners")
    @ApiOperation("获取合作伙伴列表")
    public String getPartners() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Map<String, Object>> partners = assessmentService.getPartners();
            return JsonBean.success(partners);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/update-status")
    @ApiOperation("更新风险状态")
    public String updateRiskStatus(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String id = params.get("id") != null ? params.get("id").toString() : null;
            String riskStatus = params.get("riskStatus") != null ? params.get("riskStatus").toString() : null;

            if (id == null || riskStatus == null) {
                return new JsonBean(0, "参数不完整", null).toJson();
            }

            boolean result = assessmentService.updateRiskStatus(id, riskStatus);
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

    @PostMapping("/batch-assess")
    @ApiOperation("批量风险评估")
    public String batchAssess(@FlexibleRequestBody List<TcPartnerRiskAssessment> assessments) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int successCount = 0;
            for (TcPartnerRiskAssessment assessment : assessments) {
                assessment.setCreateUser(loginStaff.getStaffid().toString());
                assessment.setUpdateUser(loginStaff.getStaffid().toString());
                if (assessmentService.save(assessment)) {
                    successCount++;
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("totalCount", assessments.size());

            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "批量评估失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除风险评估")
    public String batchDelete(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return new JsonBean(0, "请选择要删除的记录", null).toJson();
            }

            boolean result = assessmentService.batchDelete(ids);
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
}

