package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RiskAssessmentDTO;
import com.global.treasurer.dto.RiskAssessmentQueryDTO;
import com.global.treasurer.entity.TblRiskAssessment;
import com.global.treasurer.service.IRiskAssessmentService;
import com.hbfk.util.user.UserProvider;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 * 风险评估管理Controller
 * 对应已存在的 TBL_RISK_ASSESSMENT 表
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
@RestController
@RequestMapping("/risk-management/assessment")
@Api(tags = "风险评估管理")
public class RiskAssessmentController {
    @Autowired
    private IRiskAssessmentService riskAssessmentService;

    @Autowired
    private UserProvider userProvider;

    /**
     * 分页查询风险评估记录
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询风险评估记录", notes = "根据条件分页查询风险评估记录")
    public String getRiskAssessmentPage(RiskAssessmentQueryDTO queryDTO, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            PageInfo<TblRiskAssessment> pageInfo = riskAssessmentService.selectRiskAssessmentList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询风险评估记录
     */
    @GetMapping("/{riskId}")
    @ApiOperation(value = "根据ID查询风险评估记录", notes = "根据ID查询风险评估详情")
    public String getRiskAssessment(@PathVariable String riskId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            TblRiskAssessment assessment = riskAssessmentService.selectRiskAssessmentById(riskId);
            if (assessment == null) {
                return JsonBean.error("风险评估记录不存在");
            }
            return JsonBean.success(assessment);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建风险评估
     */
    @PostMapping
    @ApiOperation(value = "创建风险评估", notes = "新增风险评估记录")
    public String createRiskAssessment(@Valid @FlexibleRequestBody RiskAssessmentDTO dto, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            TblRiskAssessment assessment = riskAssessmentService.insertRiskAssessment(dto);
            return JsonBean.success("创建成功", assessment);
        } catch (Exception e) {
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新风险评估
     */
    @PutMapping
    @ApiOperation(value = "更新风险评估", notes = "修改风险评估记录")
    public String updateRiskAssessment(@Valid @FlexibleRequestBody RiskAssessmentDTO dto, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            TblRiskAssessment assessment = riskAssessmentService.updateRiskAssessment(dto);
            return JsonBean.success("更新成功", assessment);
        } catch (Exception e) {
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除风险评估记录
     */
    @DeleteMapping("/{riskId}")
    @ApiOperation(value = "删除风险评估记录", notes = "根据ID删除风险评估记录")
    public String deleteRiskAssessment(@PathVariable String riskId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            boolean result = riskAssessmentService.deleteRiskAssessment(riskId);
            return result ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (Exception e) {
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取风险分布分析
     */
    @GetMapping("/analysis/distribution")
    @ApiOperation(value = "获取风险分布分析", notes = "获取风险评估分布统计数据")
    public String getRiskDistributionAnalysis(@RequestParam(required = false) String enterpriseId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            Map<String, Object> analysis = riskAssessmentService.getDistributionAnalysis(enterpriseId);
            return JsonBean.success(analysis);
        } catch (Exception e) {
            return JsonBean.error("获取分析数据失败: " + e.getMessage());
        }
    }

    /**
     * 导出风险评估报表
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出风险评估报表", notes = "导出风险评估数据到Excel")
    public void exportRiskAssessment(RiskAssessmentQueryDTO queryDTO, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("用户已失效"));
                return;
            }
            riskAssessmentService.exportRiskAssessment(queryDTO, response);
        } catch (Exception e) {
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("导出失败: " + e.getMessage()));
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    @PostMapping("/import")
    @ApiOperation(value = "批量导入风险评估", notes = "通过Excel文件批量导入风险评估记录")
    public String importRiskAssessment(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            if (file.isEmpty()) {
                return JsonBean.error("请选择要导入的文件");
            }
            Map<String, Object> result = riskAssessmentService.importRiskAssessment(file);
            return JsonBean.success("导入完成", result);
        } catch (Exception e) {
            return JsonBean.error("导入失败: " + e.getMessage());
        }
    }

    @GetMapping("/{riskId}/report")
    @ApiOperation(value = "生成风险评估报告", notes = "根据ID生成风险评估报告Excel")
    public void generateReport(@PathVariable String riskId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("用户已失效"));
                return;
            }
            riskAssessmentService.generateReport(riskId, response);
        } catch (Exception e) {
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("生成报告失败: " + e.getMessage()));
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    @GetMapping("/{riskId}/history")
    @ApiOperation(value = "获取评估历史", notes = "获取风险评估记录的变更历史")
    public String getHistory(@PathVariable String riskId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            List<Map<String, Object>> history = riskAssessmentService.getHistory(riskId);
            return JsonBean.success(history);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}

