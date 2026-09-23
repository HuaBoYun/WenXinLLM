package com.huabo.contract.controller;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.QualityInspection;
import com.huabo.contract.service.QualityInspectionService;
import com.huabo.contract.vo.QualityInspectionQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 质量检查管理控制器
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Tag(name="质量检查管理",description="质量检查管理")
@RestController
@RequestMapping("/quality/inspection")
@RequiredArgsConstructor
public class QualityInspectionController {

    private final QualityInspectionService qualityInspectionService;

    @Operation(summary = "分页查询质量检查记录列表")
    @PostMapping("/page")
    public String getQualityInspectionPage(@RequestBody QualityInspectionQueryParam queryParam) {
        try {
            IPage<QualityInspection> page = qualityInspectionService.getQualityInspectionPage(queryParam);
            return JsonBean.success(page, page.getRecords());
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询质量检查记录详情")
    @PostMapping("/detail/{id}")
    public String getQualityInspectionById(@Parameter(description="质量检查记录ID") @PathVariable Long id) {
        try {
            QualityInspection qualityInspection = qualityInspectionService.getById(id);
            return JsonBean.success("查询成功", qualityInspection);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增质量检查记录")
    @PostMapping("/create")
    public String saveQualityInspection(@RequestBody QualityInspection qualityInspection) {
        try {
            // 数据类型转换处理
            convertDataTypes(qualityInspection);

            // 验证质量检查记录信息
            if (!qualityInspectionService.validateInspectionInfo(qualityInspection)) {
                return JsonBean.error("质量检查记录信息验证失败");
            }

            // 生成检查编号
            if (qualityInspection.getInspectionNo() == null || qualityInspection.getInspectionNo().isEmpty()) {
                qualityInspection.setInspectionNo(qualityInspectionService.generateInspectionNo());
            }

            // 设置创建时间
            qualityInspection.setCreateTime(new Date());
            qualityInspection.setUpdateTime(new Date());

            boolean result = qualityInspectionService.save(qualityInspection);
            return result ? JsonBean.success("质量检查记录创建成功") : JsonBean.error("质量检查记录创建失败");
        } catch (Exception e) {
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改质量检查记录")
    @PostMapping("/update")
    public String updateQualityInspection(@RequestBody QualityInspection qualityInspection) {
        try {
            // 数据类型转换处理
            convertDataTypes(qualityInspection);

            // 验证质量检查记录信息
            if (!qualityInspectionService.validateInspectionInfo(qualityInspection)) {
                return JsonBean.error("质量检查记录信息验证失败");
            }

            // 设置更新时间
            qualityInspection.setUpdateTime(new Date());

            boolean result = qualityInspectionService.updateById(qualityInspection);
            return result ? JsonBean.success("质量检查记录更新成功") : JsonBean.error("质量检查记录更新失败");
        } catch (Exception e) {
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除质量检查记录")
    @PostMapping("/delete/{id}")
    public String deleteQualityInspection(@Parameter(description="质量检查记录ID") @PathVariable Long id) {
        try {
            boolean result = qualityInspectionService.removeById(id);
            return result ? JsonBean.success("质量检查记录删除成功") : JsonBean.error("质量检查记录删除失败");
        } catch (Exception e) {
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除质量检查记录")
    @PostMapping("/batch-delete")
    public String batchDeleteQualityInspection(@RequestBody List<Long> ids) {
        try {
            boolean result = qualityInspectionService.removeByIds(ids);
            return result ? JsonBean.success("批量删除成功") : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据项目ID查询质量检查记录列表")
    @PostMapping("/project/{projectId}")
    public String getByProjectId(@Parameter(description="项目ID") @PathVariable Long projectId) {
        try {
            List<QualityInspection> list = qualityInspectionService.getByProjectId(projectId);
            return JsonBean.success("查询成功", list);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "质量问题整改")
    @PostMapping("/rectify/{id}")
    public String rectifyIssues(@Parameter(description="质量检查记录ID") @PathVariable Long id,
                               @RequestBody QualityInspection rectificationInfo) {
        try {
            Boolean result = qualityInspectionService.rectifyIssues(
                id,
                rectificationInfo.getRectificationDescription(),
                rectificationInfo.getRectificationPersonId(),
                1L // 默认更新人ID，实际应从当前登录用户获取
            );
            return result ? JsonBean.success("整改信息更新成功") : JsonBean.error("整改信息更新失败");
        } catch (Exception e) {
            return JsonBean.error("整改失败: " + e.getMessage());
        }
    }

    @Operation(summary = "质量检查统计")
    @PostMapping("/statistics")
    public String getQualityInspectionStatistics(@RequestParam(required = false) Long projectId,
                                                 @RequestParam(required = false) String startDate,
                                                 @RequestParam(required = false) String endDate) {
        try {
            // 这里可以根据参数进行统计查询
            List<QualityInspection> statistics = qualityInspectionService.getQualityInspectionStatistics();
            return JsonBean.success("统计查询成功", statistics);
        } catch (Exception e) {
            return JsonBean.error("统计查询失败: " + e.getMessage());
        }
    }

    /**
     * 数据类型转换处理
     * 将前端传递的Integer类型转换为Short类型，以匹配达梦数据库的SMALLINT类型
     */
    private void convertDataTypes(QualityInspection qualityInspection) {
        // 处理检查类型
        if (qualityInspection.getInspectionType() == null) {
            qualityInspection.setInspectionType((short) 1); // 默认自检
        }

        // 处理检查结论
        if (qualityInspection.getCheckResult() == null) {
            qualityInspection.setCheckResult((short) 3); // 默认待整改
        }

        // 处理整改状态
        if (qualityInspection.getRectificationStatus() == null) {
            qualityInspection.setRectificationStatus((short) 1); // 默认待整改
        }

        // 处理复查结果
        if (qualityInspection.getRecheckResult() == null) {
            qualityInspection.setRecheckResult((short) 0); // 默认未复查
        }
    }

}
