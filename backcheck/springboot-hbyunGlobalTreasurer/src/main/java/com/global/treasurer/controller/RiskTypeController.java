package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RiskTypeDTO;
import com.global.treasurer.dto.RiskTypeQueryDTO;
import com.global.treasurer.dto.export.ExportRiskTypeDTO;
import com.global.treasurer.entity.TblRiskType;
import com.global.treasurer.service.IRiskTypeService;
import com.global.treasurer.util.excel.ExcelExport;
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
import java.util.stream.Collectors;

/**
 * 风险类型管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
@RestController
@RequestMapping("/risk-management/risk-types")
@Api(tags = "风险类型管理")
public class RiskTypeController {
    @Autowired
    private IRiskTypeService riskTypeService;

    @Autowired
    private UserProvider userProvider;

    /**
     * 分页查询风险类型列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "分页查询风险类型列表", notes = "根据条件分页查询风险类型")
    public String listRiskType(RiskTypeQueryDTO queryDTO, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            PageInfo<TblRiskType> pageInfo = riskTypeService.selectRiskTypeList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询风险类型
     */
    @GetMapping("/{typeId}")
    @ApiOperation(value = "根据ID查询风险类型", notes = "根据类型ID查询风险类型详情")
    public String getRiskType(@PathVariable Long typeId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            TblRiskType riskType = riskTypeService.selectRiskTypeById(typeId);
            if (riskType == null) {
                return JsonBean.error("风险类型不存在");
            }
            return JsonBean.success(riskType);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增风险类型
     */
    @PostMapping
    @ApiOperation(value = "新增风险类型", notes = "新增风险类型信息")
    public String addRiskType(@Valid @FlexibleRequestBody RiskTypeDTO dto, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            TblRiskType riskType = riskTypeService.insertRiskType(dto);
            return JsonBean.success("新增成功", riskType);
        } catch (Exception e) {
            return JsonBean.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改风险类型
     */
    @PutMapping
    @ApiOperation(value = "修改风险类型", notes = "修改风险类型信息")
    public String updateRiskType(@Valid @FlexibleRequestBody RiskTypeDTO dto, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            TblRiskType riskType = riskTypeService.updateRiskType(dto);
            return JsonBean.success("修改成功", riskType);
        } catch (Exception e) {
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除风险类型
     */
    @DeleteMapping("/{typeIds}")
    @ApiOperation(value = "删除风险类型", notes = "根据ID删除风险类型，支持批量删除（逗号分隔）")
    public String delRiskType(@PathVariable Long[] typeIds, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            int successCount = 0;
            for (Long typeId : typeIds) {
                if (riskTypeService.deleteRiskType(typeId)) {
                    successCount++;
                }
            }
            return successCount > 0 ? JsonBean.success("删除成功，共删除" + successCount + "条") : JsonBean.error("删除失败");
        } catch (Exception e) {
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 切换风险类型状态
     */
    @PutMapping("/{typeId}/status")
    @ApiOperation(value = "切换风险类型状态", notes = "启用或禁用风险类型")
    public String toggleRiskTypeStatus(@PathVariable Long typeId, @RequestParam Map<String, Integer> statusMap, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            Integer status = statusMap.get("status");
            boolean result = riskTypeService.toggleStatus(typeId, status);
            return result ? JsonBean.success("状态更新成功") : JsonBean.error("状态更新失败");
        } catch (Exception e) {
            return JsonBean.error("状态更新失败: " + e.getMessage());
        }
    }

    /**
     * 获取风险统计信息
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "获取风险统计信息", notes = "获取风险类型统计数据")
    public String getRiskStatistics(@RequestParam(required = false) Long orgId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            Map<String, Object> statistics = riskTypeService.getStatistics(orgId);
            return JsonBean.success(statistics);
        } catch (Exception e) {
            return JsonBean.error("获取统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 导出风险类型数据
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出风险类型", notes = "导出风险类型数据到Excel")
    public void exportRiskType(RiskTypeQueryDTO queryDTO, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("用户已失效"));
                return;
            }
            List<TblRiskType> list = riskTypeService.selectRiskTypeExportList(queryDTO);
            List<ExportRiskTypeDTO> exportList = list.stream()
                    .map(ExportRiskTypeDTO::fromEntity)
                    .collect(Collectors.toList());
            String filename = "风险类型数据_" + System.currentTimeMillis() + ".xlsx";
            try (ExcelExport export = new ExcelExport("风险类型数据", ExportRiskTypeDTO.class)) {
                export.setDataList(exportList).write(response, filename);
            }
        } catch (Exception e) {
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("导出失败: " + e.getMessage()));
            } catch (Exception ex) {
                // ignore
            }
        }
    }
}

