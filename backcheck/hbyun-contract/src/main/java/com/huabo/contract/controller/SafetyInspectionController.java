package com.huabo.contract.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.SafetyInspection;
import com.huabo.contract.service.SafetyInspectionService;
import com.huabo.contract.vo.SafetyInspectionQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 安全检查管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Tag(name="安全检查管理",description="安全检查管理")
@RestController
@RequestMapping("/safety/inspection")
@RequiredArgsConstructor
public class SafetyInspectionController {

    private final SafetyInspectionService safetyInspectionService;

    @Operation(summary = "分页查询安全检查记录列表")
    @PostMapping("/page")
    public String getSafetyInspectionPage(@RequestBody SafetyInspectionQueryParam queryParam) {
        try {
            IPage<SafetyInspection> page = safetyInspectionService.getSafetyInspectionPage(queryParam);
            return JsonBean.success(page, page.getRecords());
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询安全检查详情")
    @GetMapping("/{id}")
    public String getSafetyInspectionById(@Parameter(description="安全检查记录ID") @PathVariable Long id) {
        try {
            SafetyInspection safetyInspection = safetyInspectionService.getById(id);
            if (safetyInspection == null) {
                return JsonBean.error("安全检查记录不存在");
            }
            return JsonBean.success("查询成功", safetyInspection);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增安全检查记录")
    @PostMapping("/create")
    public String saveSafetyInspection(@RequestBody SafetyInspection safetyInspection) {
        try {
            // 数据类型转换处理
            convertDataTypes(safetyInspection);

            // 验证安全检查记录信息
            if (!safetyInspectionService.validateInspectionInfo(safetyInspection)) {
                return JsonBean.error("安全检查记录信息验证失败");
            }

            // 生成检查编号
            if (safetyInspection.getInspectionNo() == null || safetyInspection.getInspectionNo().isEmpty()) {
                safetyInspection.setInspectionNo(safetyInspectionService.generateInspectionNo());
            }

            // 设置创建时间
            safetyInspection.setCreateTime(new Date());
            safetyInspection.setUpdateTime(new Date());

            boolean result = safetyInspectionService.save(safetyInspection);
            return result ? JsonBean.success("安全检查记录创建成功") : JsonBean.error("安全检查记录创建失败");
        } catch (Exception e) {
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改安全检查记录")
    @PostMapping("/update")
    public String updateSafetyInspection(@RequestBody SafetyInspection safetyInspection) {
        try {
            // 数据类型转换处理
            convertDataTypes(safetyInspection);

            // 验证安全检查记录信息
            if (!safetyInspectionService.validateInspectionInfo(safetyInspection)) {
                return JsonBean.error("安全检查记录信息验证失败");
            }

            // 设置更新时间
            safetyInspection.setUpdateTime(new Date());

            boolean result = safetyInspectionService.updateById(safetyInspection);
            return result ? JsonBean.success("安全检查记录更新成功") : JsonBean.error("安全检查记录更新失败");
        } catch (Exception e) {
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除安全检查记录")
    @PostMapping("/batchDelete")
    public String batchDeleteSafetyInspection(@RequestBody java.util.List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("请选择要删除的记录");
            }

            boolean result = safetyInspectionService.removeByIds(ids);
            return result ? JsonBean.success("批量删除成功") : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "安全隐患整改")
    @PostMapping("/rectify/{id}")
    public String rectifyHazards(@Parameter(description="安全检查记录ID") @PathVariable Long id,
                                @RequestBody SafetyInspection rectificationInfo) {
        try {
            Boolean result = safetyInspectionService.rectifyHazards(
                id,
                rectificationInfo.getRectificationMeasures(),
                rectificationInfo.getRectificationPersonId(),
                1L // 默认更新人ID，实际应从当前登录用户获取
            );
            return result ? JsonBean.success("整改信息更新成功") : JsonBean.error("整改信息更新失败");
        } catch (Exception e) {
            return JsonBean.error("整改失败: " + e.getMessage());
        }
    }

    @Operation(summary = "安全检查验证")
    @PostMapping("/verify/{id}")
    public String verifySafetyInspection(@Parameter(description="安全检查记录ID") @PathVariable Long id,
                                        @RequestBody SafetyInspection verificationInfo) {
        try {
            Boolean result = safetyInspectionService.verifyInspection(
                id,
                verificationInfo.getVerificationResult(),
                verificationInfo.getVerificationComments(),
                1L // 默认验证人ID，实际应从当前登录用户获取
            );
            return result ? JsonBean.success("验证完成") : JsonBean.error("验证失败");
        } catch (Exception e) {
            return JsonBean.error("验证失败: " + e.getMessage());
        }
    }

    /**
     * 数据类型转换处理
     * 将前端传递的Integer类型转换为Short类型，以匹配达梦数据库的SMALLINT类型
     */
    private void convertDataTypes(SafetyInspection safetyInspection) {
        // 处理检查类型
        if (safetyInspection.getInspectionType() == null) {
            safetyInspection.setInspectionType((short) 1); // 默认日常检查
        }

        // 处理隐患等级
        if (safetyInspection.getHazardLevel() == null) {
            safetyInspection.setHazardLevel((short) 1); // 默认一般隐患
        }

        // 处理紧急程度
        if (safetyInspection.getEmergencyLevel() == null) {
            safetyInspection.setEmergencyLevel((short) 1); // 默认一般紧急
        }

        // 处理整改状态
        if (safetyInspection.getRectificationStatus() == null) {
            safetyInspection.setRectificationStatus((short) 1); // 默认待整改
        }

        // 处理验证结果
        if (safetyInspection.getVerificationResult() == null) {
            safetyInspection.setVerificationResult((short) 0); // 默认未验证
        }
    }
}
