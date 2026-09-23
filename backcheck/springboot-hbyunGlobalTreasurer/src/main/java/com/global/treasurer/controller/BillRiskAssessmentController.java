package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillRiskAssessmentDTO;
import com.global.treasurer.dto.BillRiskAssessmentQueryDTO;
import com.global.treasurer.entity.TblBillRiskAssessment;
import com.global.treasurer.service.IBillRiskAssessmentService;
import com.global.treasurer.vo.BillRiskAssessmentVO;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.BizException;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 票据风险管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/bill/risk")
@Api(tags = "票据风险管理")
public class BillRiskAssessmentController {
    private static final Logger log = LoggerFactory.getLogger(BillRiskAssessmentController.class);

    @Autowired
    private IBillRiskAssessmentService billRiskAssessmentService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询风险评估
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询票据风险评估列表", notes = "分页查询票据风险评估列表")
    public String list(BillRiskAssessmentQueryDTO queryDTO, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            PageInfo<BillRiskAssessmentVO> pageInfo = billRiskAssessmentService.selectBillRiskAssessmentList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据风险评估列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询风险评估
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询票据风险评估", notes = "分页查询票据风险评估列表")
    public String page(BillRiskAssessmentQueryDTO queryDTO, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            PageInfo<BillRiskAssessmentVO> pageInfo = billRiskAssessmentService.selectBillRiskAssessmentList(queryDTO);
            // 构造符合前端要求的数据格式
            Map<String, Object> resultMap = new java.util.HashMap<>();
            resultMap.put("tlist", pageInfo.getList());
            resultMap.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(resultMap, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据风险评估列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取风险评估详情
     */
    @PostMapping("/detail/{assessmentId}")
    @ApiOperation(value = "获取票据风险评估详情", notes = "根据评估ID获取详细信息")
    public String detail(@PathVariable Long assessmentId, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            BillRiskAssessmentVO vo = billRiskAssessmentService.selectBillRiskAssessmentById(assessmentId);
            if (vo == null) {
                return JsonBean.error("风险评估记录不存在");
            }
            return JsonBean.success(vo);
        } catch (BizException e) {
            log.warn("获取票据风险评估详情失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取票据风险评估详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建风险评估
     */
    @PostMapping("/assessment/save")
    @ApiOperation(value = "新增票据风险评估", notes = "新增票据风险评估信息")
    public String save(@Valid @FlexibleRequestBody BillRiskAssessmentDTO dto, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            TblBillRiskAssessment risk = billRiskAssessmentService.insertBillRiskAssessment(dto);
            return JsonBean.success("风险评估创建成功", risk);
        } catch (BizException e) {
            log.warn("新增票据风险评估失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("新增票据风险评估失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 更新风险评估
     */
    @PostMapping("/assessment/update")
    @ApiOperation(value = "修改票据风险评估", notes = "修改票据风险评估信息")
    public String update(@Valid @FlexibleRequestBody BillRiskAssessmentDTO dto, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            if (dto.getRiskId() == null) {
                return JsonBean.error("风险ID不能为空");
            }
            TblBillRiskAssessment risk = billRiskAssessmentService.updateBillRiskAssessment(dto);
            return JsonBean.success("风险评估修改成功", risk);
        } catch (BizException e) {
            log.warn("修改票据风险评估失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改票据风险评估失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 获取风险预警
     */
    @PostMapping("/alerts")
    @ApiOperation(value = "获取票据风险预警", notes = "查询票据风险预警列表")
    public String getAlerts(Map<String, Object> params, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            List<Map<String, Object>> alerts = billRiskAssessmentService.getRiskAlerts(params);
            return JsonBean.success(alerts);
        } catch (Exception e) {
            log.error("获取票据风险预警失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 处置风险
     */
    @PostMapping("/dispose")
    @ApiOperation(value = "处置票据风险", notes = "处置票据风险")
    public String dispose(@RequestParam Map<String, Object> disposeData, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            boolean result = billRiskAssessmentService.disposeBillRisk(disposeData);
            return result ? JsonBean.success("风险处置成功") : JsonBean.error("风险处置失败");
        } catch (BizException e) {
            log.warn("处置票据风险失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("处置票据风险失败", e);
            return JsonBean.error("处置失败: " + e.getMessage());
        }
    }

    /**
     * 获取票据趋势
     */
    @PostMapping("/statistics/trend")
    @ApiOperation(value = "获取票据趋势分析", notes = "统计分析票据趋势")
    public String getTrend(Map<String, Object> params, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }

            Map<String, Object> trendData = billRiskAssessmentService.getBillTrend(params);
            return JsonBean.success(trendData);
        } catch (Exception e) {
            log.error("获取票据趋势分析失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建风险评估 (兼容前端路径)
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增票据风险评估", notes = "新增票据风险评估信息")
    public String saveRisk(@Valid @FlexibleRequestBody BillRiskAssessmentDTO dto, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }
            TblBillRiskAssessment risk = billRiskAssessmentService.insertBillRiskAssessment(dto);
            return JsonBean.success("风险评估创建成功", risk);
        } catch (BizException e) {
            log.warn("新增票据风险评估失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("新增票据风险评估失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 更新风险评估 (兼容前端路径)
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改票据风险评估", notes = "修改票据风险评估信息")
    public String updateRisk(@Valid @FlexibleRequestBody BillRiskAssessmentDTO dto, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }
            if (dto.getRiskId() == null) {
                return JsonBean.error("风险ID不能为空");
            }
            TblBillRiskAssessment risk = billRiskAssessmentService.updateBillRiskAssessment(dto);
            return JsonBean.success("风险评估修改成功", risk);
        } catch (BizException e) {
            log.warn("修改票据风险评估失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改票据风险评估失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除风险评估
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除票据风险评估", notes = "批量删除票据风险评估")
    public String deleteRisk(Long[] riskIds, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }
            boolean result = billRiskAssessmentService.deleteBillRiskAssessmentByIds(riskIds);
            return result ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (BizException e) {
            log.warn("删除票据风险评估失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除票据风险评估失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 标记预警为已处理
     */
    @PostMapping("/alert/resolve")
    @ApiOperation(value = "标记预警已处理", notes = "标记风险预警为已处理状态")
    public String resolveAlert(@RequestParam Map<String, Object> resolveData, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }
            boolean result = billRiskAssessmentService.resolveRiskAlert(resolveData);
            return result ? JsonBean.success("标记成功") : JsonBean.error("标记失败");
        } catch (BizException e) {
            log.warn("标记预警已处理失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("标记预警已处理失败", e);
            return JsonBean.error("标记失败: " + e.getMessage());
        }
    }

    /**
     * 导出风险报告
     */
    @PostMapping("/export")
    @ApiOperation(value = "导出风险报告", notes = "导出票据风险报告到Excel")
    public void export(BillRiskAssessmentQueryDTO queryDTO, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return;
            }
            billRiskAssessmentService.exportBillRiskAssessment(queryDTO, response);
        } catch (Exception e) {
            log.error("导出风险报告失败", e);
        }
    }

    /**
     * 获取风险统计数据
     */
    @PostMapping("/statistics")
    @ApiOperation(value = "获取风险统计", notes = "获取票据风险统计数据")
    public String statistics(BillRiskAssessmentQueryDTO queryDTO, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }
            Map<String, Object> statistics = billRiskAssessmentService.getBillRiskStatistics(queryDTO);
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取风险统计数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取风险趋势数据
     */
    @PostMapping("/trend")
    @ApiOperation(value = "获取风险趋势", notes = "获取票据风险趋势数据")
    public String getRiskTrend(Map<String, Object> params, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }
            Map<String, Object> trendData = billRiskAssessmentService.getRiskTrendData(params);
            return JsonBean.success(trendData);
        } catch (Exception e) {
            log.error("获取风险趋势数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取风险分布数据
     */
    @PostMapping("/distribution")
    @ApiOperation(value = "获取风险分布", notes = "获取票据风险分布数据")
    public String getDistribution(Map<String, Object> params, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }
            Map<String, Object> distributionData = billRiskAssessmentService.getRiskDistributionData(params);
            return JsonBean.success(distributionData);
        } catch (Exception e) {
            log.error("获取风险分布数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取评估历史记录
     */
    @PostMapping("/history/{identifier}")
    @ApiOperation(value = "获取评估历史", notes = "获取票据的风险评估历史记录，支持票据号码或评估ID")
    public String getHistory(@PathVariable String identifier, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return JsonBean.error("用户已失效");
            }
            List<Map<String, Object>> history = billRiskAssessmentService.getRiskAssessmentHistory(identifier);
            return JsonBean.success(history);
        } catch (Exception e) {
            log.error("获取评估历史记录失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}
