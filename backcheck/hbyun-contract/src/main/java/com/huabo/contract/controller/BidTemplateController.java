package com.huabo.contract.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.BidTemplate;
import com.huabo.contract.service.BidTemplateService;
import com.huabo.contract.vo.BidTemplateQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 标书模板管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/bidding/template")
@Tag(name="标书模板管理",description="标书模板管理")
@Validated
public class BidTemplateController {

    @Autowired
    private BidTemplateService bidTemplateService;

    /**
     * 分页查询标书模板列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询标书模板列表", description = "支持多条件查询和分页")
    public String getBidTemplateList(@RequestBody BidTemplateQueryParam param) {
        try {
            log.info("分页查询标书模板列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<BidTemplate> pageInfo = bidTemplateService.getBidTemplateList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("查询标书模板列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取标书模板详情
     *
     * @param id 模板ID
     * @return 标书模板详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取标书模板详情", description = "根据ID获取详细信息")
    public String getBidTemplateById(@PathVariable Long id) {
        try {
            log.info("获取标书模板详情，ID：{}", id);

            if (id == null) {
                return JsonBean.error("模板ID不能为空");
            }

            BidTemplate bidTemplate = bidTemplateService.getBidTemplateById(id);
            if (bidTemplate == null) {
                return JsonBean.error("标书模板不存在");
            }

            return JsonBean.success("查询成功", bidTemplate);
        } catch (Exception e) {
            log.error("获取标书模板详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存标书模板（新增或修改）
     *
     * @param bidTemplate 标书模板
     * @return 保存结果
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.PUT})
    @Operation(summary = "保存标书模板", description = "新增或修改标书模板")
    public String saveBidTemplate(@RequestBody BidTemplate bidTemplate) {
        try {
            log.info("保存标书模板，模板：{}", bidTemplate);

            // 校验必填字段
            if (!StringUtils.hasText(bidTemplate.getTemplateName())) {
                return JsonBean.error("模板名称不能为空");
            }

            // 校验模板编号唯一性
            if (StringUtils.hasText(bidTemplate.getTemplateNo())) {
                boolean exists = bidTemplateService.existsTemplateNo(
                    bidTemplate.getTemplateNo(), bidTemplate.getId());
                if (exists) {
                    return JsonBean.error("模板编号已存在，请重新输入");
                }
            }

            // 设置创建/更新信息
            if (bidTemplate.getId() == null) {
                bidTemplate.setCreateTime(new Date());
                bidTemplate.setUpdateTime(new Date());
                bidTemplate.setUsageCount(0);
                bidTemplate.setDownloadCount(0);
                bidTemplate.setRatingScore(0.0);
                bidTemplate.setRatingCount(0);
                bidTemplate.setIsEnabled(1);
                bidTemplate.setTemplateStatus(1);
            } else {
                bidTemplate.setUpdateTime(new Date());
            }

            boolean result = bidTemplateService.saveBidTemplate(bidTemplate);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存标书模板失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 更新标书模板
     *
     * @param id 模板ID
     * @param bidTemplate 标书模板
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新标书模板", description = "根据ID更新标书模板")
    public String updateBidTemplate(@PathVariable Long id, @RequestBody BidTemplate bidTemplate) {
        try {
            log.info("更新标书模板，ID：{}，模板：{}", id, bidTemplate);

            if (id == null) {
                return JsonBean.error("模板ID不能为空");
            }

            // 设置ID和更新时间
            bidTemplate.setId(id);
            bidTemplate.setUpdateTime(new Date());

            boolean result = bidTemplateService.saveBidTemplate(bidTemplate);
            if (result) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新标书模板失败", e);
            return JsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除标书模板
     *
     * @param id 模板ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除标书模板", description = "根据ID删除标书模板")
    public String deleteBidTemplate(@PathVariable Long id) {
        try {
            log.info("删除标书模板，ID：{}", id);

            if (id == null) {
                return JsonBean.error("模板ID不能为空");
            }

            boolean result = bidTemplateService.deleteBidTemplate(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除标书模板失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除标书模板
     *
     * @param ids 模板ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除标书模板", description = "根据ID列表批量删除标书模板")
    public String batchDeleteBidTemplate(@RequestBody List<Long> ids) {
        try {
            log.info("批量删除标书模板，IDs：{}", ids);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("请选择要删除的模板");
            }

            boolean result = bidTemplateService.batchDeleteBidTemplate(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除标书模板失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据模板类型获取模板列表
     *
     * @param templateType 模板类型
     * @return 模板列表
     */
    @GetMapping("/byType/{templateType}")
    @Operation(summary = "根据模板类型获取模板列表", description = "获取指定类型的所有模板")
    public String getBidTemplatesByType(@PathVariable Integer templateType) {
        try {
            log.info("根据模板类型获取模板列表，类型：{}", templateType);

            if (templateType == null) {
                return JsonBean.error("模板类型不能为空");
            }

            List<BidTemplate> templates = bidTemplateService.getBidTemplatesByType(templateType);
            return JsonBean.success("查询成功", templates);
        } catch (Exception e) {
            log.error("根据模板类型获取模板列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 启用/禁用标书模板
     *
     * @param id 模板ID
     * @param enabled 是否启用
     * @return 更新结果
     */
    @PostMapping("/updateStatus/{id}")
    @Operation(summary = "启用/禁用标书模板", description = "更新模板的启用状态")
    public String updateTemplateStatus(@PathVariable Long id, @RequestParam Integer enabled) {
        try {
            log.info("更新标书模板状态，ID：{}，启用状态：{}", id, enabled);

            if (id == null) {
                return JsonBean.error("模板ID不能为空");
            }

            boolean result = bidTemplateService.updateTemplateStatus(id, enabled);
            if (result) {
                return JsonBean.success("状态更新成功");
            } else {
                return JsonBean.error("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新标书模板状态失败", e);
            return JsonBean.error("状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 复制标书模板
     *
     * @param id 源模板ID
     * @return 复制结果
     */
    @PostMapping("/copy/{id}")
    @Operation(summary = "复制标书模板", description = "复制现有模板创建新模板")
    public String copyBidTemplate(@PathVariable Long id) {
        try {
            log.info("复制标书模板，源ID：{}", id);

            if (id == null) {
                return JsonBean.error("源模板ID不能为空");
            }

            BidTemplate newTemplate = bidTemplateService.copyBidTemplate(id);
            if (newTemplate != null) {
                return JsonBean.success("复制成功", newTemplate);
            } else {
                return JsonBean.error("复制失败");
            }
        } catch (Exception e) {
            log.error("复制标书模板失败", e);
            return JsonBean.error("复制失败：" + e.getMessage());
        }
    }

    /**
     * 获取模板统计信息
     *
     * @return 统计数据
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取模板统计信息", description = "获取模板的统计数据")
    public String getTemplateStatistics() {
        try {
            log.info("获取模板统计信息");

            Map<String, Object> statistics = bidTemplateService.getTemplateStatistics();
            return JsonBean.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取模板统计信息失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 生成模板编号
     *
     * @return 模板编号
     */
    @GetMapping("/generateTemplateNo")
    @Operation(summary = "生成模板编号", description = "自动生成模板编号")
    public String generateTemplateNo() {
        try {
            log.info("生成模板编号");

            String templateNo = bidTemplateService.generateTemplateNo();
            return JsonBean.success("生成成功", templateNo);
        } catch (Exception e) {
            log.error("生成模板编号失败", e);
            return JsonBean.error("生成失败：" + e.getMessage());
        }
    }
}
