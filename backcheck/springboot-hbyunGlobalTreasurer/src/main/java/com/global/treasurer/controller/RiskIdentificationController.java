package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BatchAssessDTO;
import com.global.treasurer.dto.RiskIdentificationDTO;
import com.global.treasurer.dto.RiskIdentificationQueryDTO;
import com.global.treasurer.entity.TblRiskIdentification;
import com.global.treasurer.service.IRiskIdentificationService;
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

/**
 * 风险识别管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
@RestController
@RequestMapping("/risk-management/identification")
@Api(tags = "风险识别管理")
public class RiskIdentificationController {
    @Autowired
    private IRiskIdentificationService riskIdentificationService;

    @Autowired
    private UserProvider userProvider;

    /**
     * 分页查询风险识别记录
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询风险识别记录", notes = "根据条件分页查询风险识别记录")
    public String getRiskIdentificationPage(RiskIdentificationQueryDTO queryDTO, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            PageInfo<TblRiskIdentification> pageInfo = riskIdentificationService.selectRiskIdentificationList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询风险识别记录
     */
    @GetMapping("/{identificationId}")
    @ApiOperation(value = "根据ID查询风险识别记录", notes = "根据ID查询风险识别详情")
    public String getRiskIdentification(@PathVariable Long identificationId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            TblRiskIdentification identification = riskIdentificationService.selectRiskIdentificationById(identificationId);
            if (identification == null) {
                return JsonBean.error("风险识别记录不存在");
            }
            return JsonBean.success(identification);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建风险识别记录
     */
    @PostMapping
    @ApiOperation(value = "创建风险识别记录", notes = "新增风险识别记录")
    public String createRiskIdentification(@Valid @FlexibleRequestBody RiskIdentificationDTO dto, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            TblRiskIdentification identification = riskIdentificationService.insertRiskIdentification(dto);
            return JsonBean.success("创建成功", identification);
        } catch (Exception e) {
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新风险识别记录
     */
    @PutMapping
    @ApiOperation(value = "更新风险识别记录", notes = "修改风险识别记录")
    public String updateRiskIdentification(@Valid @FlexibleRequestBody RiskIdentificationDTO dto, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            TblRiskIdentification identification = riskIdentificationService.updateRiskIdentification(dto);
            return JsonBean.success("更新成功", identification);
        } catch (Exception e) {
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除风险识别记录
     */
    @DeleteMapping("/{identificationId}")
    @ApiOperation(value = "删除风险识别记录", notes = "根据ID删除风险识别记录")
    public String deleteRiskIdentification(@PathVariable Long identificationId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            boolean result = riskIdentificationService.deleteRiskIdentification(identificationId);
            return result ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (Exception e) {
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 导出风险识别报表
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出风险识别报表", notes = "导出风险识别数据到Excel")
    public void exportRiskIdentification(RiskIdentificationQueryDTO queryDTO, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("用户已失效"));
                return;
            }
            riskIdentificationService.exportRiskIdentification(queryDTO, response);
        } catch (Exception e) {
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("导出失败: " + e.getMessage()));
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    @PostMapping("/batch-assess")
    @ApiOperation(value = "批量评估", notes = "批量将风险识别记录转为评估状态")
    public String batchAssess(@FlexibleRequestBody BatchAssessDTO dto, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            List<Long> ids = dto.getIds();
            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("请选择要评估的记录");
            }
            Map<String, Object> result = riskIdentificationService.batchAssess(ids);
            return JsonBean.success("批量评估完成", result);
        } catch (Exception e) {
            return JsonBean.error("批量评估失败: " + e.getMessage());
        }
    }

    @PostMapping("/{identificationId}/close")
    @ApiOperation(value = "关闭风险", notes = "关闭指定的风险识别记录")
    public String closeRisk(@PathVariable Long identificationId,
                            @FlexibleRequestBody Map<String, String> body,
                            HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            String reason = body != null ? body.get("reason") : "";
            TblRiskIdentification result = riskIdentificationService.closeRisk(identificationId, reason);
            return JsonBean.success("关闭成功", result);
        } catch (Exception e) {
            return JsonBean.error("关闭失败: " + e.getMessage());
        }
    }

    @GetMapping("/{identificationId}/history")
    @ApiOperation(value = "获取变更历史", notes = "获取风险识别记录的变更历史")
    public String getHistory(@PathVariable Long identificationId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            List<Map<String, Object>> history = riskIdentificationService.getHistory(identificationId);
            return JsonBean.success(history);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}

