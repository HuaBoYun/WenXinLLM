package com.huabo.contract.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.CounterpartInfo;
import com.huabo.contract.service.CounterpartInfoService;
import com.huabo.contract.vo.CounterpartInfoQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 相对方信息管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/counterpart/info")
@Tag(name="相对方信息管理",description="相对方信息管理")
@Validated
public class CounterpartInfoController {

    @Autowired
    private CounterpartInfoService counterpartInfoService;

    /**
     * 分页查询相对方信息列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询相对方信息列表", description = "支持多条件查询和分页")
    public String getCounterpartInfoList(@RequestBody CounterpartInfoQueryParam param) {
        try {
            log.info("分页查询相对方信息列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<CounterpartInfo> pageInfo = counterpartInfoService.getCounterpartInfoList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("查询相对方信息列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取相对方信息详情
     *
     * @param id 主键ID
     * @return 相对方信息详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取相对方信息详情", description = "根据ID获取详细信息")
    public String getCounterpartInfoById(@PathVariable Long id) {
        try {
            log.info("获取相对方信息详情，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            CounterpartInfo counterpartInfo = counterpartInfoService.getCounterpartInfoById(id);
            if (counterpartInfo == null) {
                return JsonBean.error("相对方信息不存在");
            }

            return JsonBean.success("查询成功", counterpartInfo);
        } catch (Exception e) {
            log.error("获取相对方信息详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存相对方信息（新增或修改）
     *
     * @param counterpartInfo 相对方信息
     * @return 保存结果
     */
    @PostMapping("/save")
    @Operation(summary = "保存相对方信息", description = "新增或修改相对方信息")
    public String saveCounterpartInfo(@RequestBody CounterpartInfo counterpartInfo) {
        try {
            log.info("保存相对方信息，相对方信息：{}", counterpartInfo);

            // 校验必填字段
            if (!StringUtils.hasText(counterpartInfo.getCompanyName())) {
                return JsonBean.error("公司名称不能为空");
            }

            // 校验公司名称唯一性
            if (StringUtils.hasText(counterpartInfo.getCompanyName())) {
                boolean exists = counterpartInfoService.existsCompanyName(
                    counterpartInfo.getCompanyName(), counterpartInfo.getId());
                if (exists) {
                    return JsonBean.error("公司名称已存在，请重新输入");
                }
            }

            // 校验公司代码唯一性
            if (StringUtils.hasText(counterpartInfo.getCompanyCode())) {
                boolean exists = counterpartInfoService.existsCompanyCode(
                    counterpartInfo.getCompanyCode(), counterpartInfo.getId());
                if (exists) {
                    return JsonBean.error("公司代码已存在，请重新输入");
                }
            }

            boolean result = counterpartInfoService.saveCounterpartInfo(counterpartInfo);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存相对方信息失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 删除相对方信息
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除相对方信息", description = "根据ID删除相对方信息")
    public String deleteCounterpartInfo(@PathVariable Long id) {
        try {
            log.info("删除相对方信息，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean result = counterpartInfoService.deleteCounterpartInfo(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除相对方信息失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除相对方信息
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除相对方信息", description = "根据ID列表批量删除相对方信息")
    public String batchDeleteCounterpartInfo(@RequestBody List<Long> ids) {
        try {
            log.info("批量删除相对方信息，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }

            boolean result = counterpartInfoService.batchDeleteCounterpartInfo(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除相对方信息失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 获取黑名单相对方列表
     *
     * @return 黑名单相对方列表
     */
    @GetMapping("/blacklistCounterparts")
    @Operation(summary = "获取黑名单相对方列表", description = "获取所有黑名单相对方")
    public String getBlacklistCounterparts() {
        try {
            log.info("获取黑名单相对方列表");

            List<CounterpartInfo> counterparts = counterpartInfoService.getBlacklistCounterparts();
            return JsonBean.success("查询成功", counterparts);
        } catch (Exception e) {
            log.error("获取黑名单相对方列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取高信用等级相对方列表
     *
     * @return 高信用等级相对方列表
     */
    @GetMapping("/highCreditCounterparts")
    @Operation(summary = "获取高信用等级相对方列表", description = "获取所有高信用等级相对方")
    public String getHighCreditCounterparts() {
        try {
            log.info("获取高信用等级相对方列表");

            List<CounterpartInfo> counterparts = counterpartInfoService.getHighCreditCounterparts();
            return JsonBean.success("查询成功", counterparts);
        } catch (Exception e) {
            log.error("获取高信用等级相对方列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取低信用等级相对方列表
     *
     * @return 低信用等级相对方列表
     */
    @GetMapping("/lowCreditCounterparts")
    @Operation(summary = "获取低信用等级相对方列表", description = "获取所有低信用等级相对方")
    public String getLowCreditCounterparts() {
        try {
            log.info("获取低信用等级相对方列表");

            List<CounterpartInfo> counterparts = counterpartInfoService.getLowCreditCounterparts();
            return JsonBean.success("查询成功", counterparts);
        } catch (Exception e) {
            log.error("获取低信用等级相对方列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取大型企业相对方列表
     *
     * @return 大型企业相对方列表
     */
    @GetMapping("/largeEnterpriseCounterparts")
    @Operation(summary = "获取大型企业相对方列表", description = "获取所有大型企业相对方")
    public String getLargeEnterpriseCounterparts() {
        try {
            log.info("获取大型企业相对方列表");

            List<CounterpartInfo> counterparts = counterpartInfoService.getLargeEnterpriseCounterparts();
            return JsonBean.success("查询成功", counterparts);
        } catch (Exception e) {
            log.error("获取大型企业相对方列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 更新黑名单状态
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/updateBlacklistFlag")
    @Operation(summary = "更新黑名单状态", description = "更新相对方的黑名单状态")
    public String updateBlacklistFlag(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Integer blacklistFlag = (Integer) request.get("blacklistFlag");

            log.info("更新黑名单状态，ID：{}，黑名单状态：{}", id, blacklistFlag);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }
            if (blacklistFlag == null) {
                return JsonBean.error("黑名单状态不能为空");
            }

            boolean result = counterpartInfoService.updateBlacklistFlag(id, blacklistFlag);
            if (result) {
                return JsonBean.success("状态更新成功");
            } else {
                return JsonBean.error("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新黑名单状态失败", e);
            return JsonBean.error("状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新黑名单状态
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/batchUpdateBlacklistFlag")
    @Operation(summary = "批量更新黑名单状态", description = "批量更新相对方的黑名单状态")
    public String batchUpdateBlacklistFlag(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) request.get("ids");
            Integer blacklistFlag = (Integer) request.get("blacklistFlag");

            log.info("批量更新黑名单状态，ID列表：{}，黑名单状态：{}", ids, blacklistFlag);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }
            if (blacklistFlag == null) {
                return JsonBean.error("黑名单状态不能为空");
            }

            boolean result = counterpartInfoService.batchUpdateBlacklistFlag(ids, blacklistFlag);
            if (result) {
                return JsonBean.success("批量状态更新成功");
            } else {
                return JsonBean.error("批量状态更新失败");
            }
        } catch (Exception e) {
            log.error("批量更新黑名单状态失败", e);
            return JsonBean.error("批量状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 更新信用等级
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/updateCreditRating")
    @Operation(summary = "更新信用等级", description = "更新相对方的信用等级")
    public String updateCreditRating(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            String creditRating = (String) request.get("creditRating");

            log.info("更新信用等级，ID：{}，信用等级：{}", id, creditRating);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }
            if (!StringUtils.hasText(creditRating)) {
                return JsonBean.error("信用等级不能为空");
            }

            boolean result = counterpartInfoService.updateCreditRating(id, creditRating);
            if (result) {
                return JsonBean.success("信用等级更新成功");
            } else {
                return JsonBean.error("信用等级更新失败");
            }
        } catch (Exception e) {
            log.error("更新信用等级失败", e);
            return JsonBean.error("信用等级更新失败：" + e.getMessage());
        }
    }

    /**
     * 模糊搜索相对方信息
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 相对方信息列表
     */
    @GetMapping("/search")
    @Operation(summary = "搜索相对方信息", description = "根据关键词模糊搜索相对方信息")
    public String searchCounterparts(@RequestParam String keyword, 
                                   @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("搜索相对方信息，关键词：{}，限制数量：{}", keyword, limit);

            if (!StringUtils.hasText(keyword)) {
                return JsonBean.error("搜索关键词不能为空");
            }

            List<CounterpartInfo> counterparts = counterpartInfoService.searchCounterparts(keyword, limit);
            return JsonBean.success("搜索成功", counterparts);
        } catch (Exception e) {
            log.error("搜索相对方信息失败", e);
            return JsonBean.error("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 获取相对方统计数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics")
    @Operation(summary = "获取相对方统计数据", description = "获取相对方的统计信息")
    public String getCounterpartStatistics(@RequestBody CounterpartInfoQueryParam param) {
        try {
            log.info("获取相对方统计数据，参数：{}", param);

            Map<String, Object> statistics = counterpartInfoService.getCounterpartStatistics(param);
            return JsonBean.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取相对方统计数据失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取信用等级分布统计
     *
     * @param param 查询参数
     * @return 信用等级分布
     */
    @PostMapping("/creditRatingDistribution")
    @Operation(summary = "获取信用等级分布统计", description = "统计各信用等级的分布情况")
    public String getCreditRatingDistribution(@RequestBody CounterpartInfoQueryParam param) {
        try {
            log.info("获取信用等级分布统计，参数：{}", param);

            List<Map<String, Object>> distribution = counterpartInfoService.getCreditRatingDistribution(param);
            return JsonBean.success("查询成功", distribution);
        } catch (Exception e) {
            log.error("获取信用等级分布统计失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }
}
