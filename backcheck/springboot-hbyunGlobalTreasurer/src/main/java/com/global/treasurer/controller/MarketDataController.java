package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.MarketData;
import com.global.treasurer.service.xjgl.dataRulesManage.MarketDataService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
// import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // 已移除,使用手动声明
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 市场数据管理控制器
 * 匹配前端API路径: /qqsk/financial/marketData/*
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
// // 已移除,使用手动声明
@RestController
@RequestMapping({"/qqsk/financial/dataRulesManage/marketData", "/treasury/market-data", "/financial/treasuryCommon/marketData", "/financial/marketData"})
@Api(tags = "市场数据管理")
public class MarketDataController {

    private static final Logger log = LoggerFactory.getLogger(MarketDataController.class);

    @Resource
    private MarketDataService marketDataService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询市场数据
     */
    @GetMapping("/page")
    @ApiOperation("分页查询市场数据")
    public String getMarketDataPage(
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(required = false, defaultValue = "20") Integer limit,
            @ApiParam("数据类型") @RequestParam(required = false) String dataType,
            @ApiParam("数据代码") @RequestParam(required = false) String symbol,
            @ApiParam("数据来源") @RequestParam(required = false) String dataSource,
            @ApiParam("数据日期") @RequestParam(required = false) String dataDate,
            @ApiParam("是否有效") @RequestParam(required = false) Integer isActive) {

        try {
            // 构建查询条件
            QueryWrapper<MarketData> queryWrapper = new QueryWrapper<>();

            if (StringUtils.hasText(dataType)) {
                queryWrapper.eq("DATA_TYPE", dataType);
            }
            if (StringUtils.hasText(symbol)) {
                queryWrapper.like("SYMBOL", symbol);
            }
            if (StringUtils.hasText(dataSource)) {
                queryWrapper.eq("DATA_SOURCE", dataSource);
            }
            if (StringUtils.hasText(dataDate)) {
                queryWrapper.eq("DATA_DATE", dataDate);
            }
            if (isActive != null) {
                queryWrapper.eq("IS_ACTIVE", isActive);
            }

            // 按数据日期倒序
            queryWrapper.orderByDesc("DATA_DATE");

            // 分页查询（PageHelper，兼容达梦数据库）
            PageHelper.startPage(page, limit);
            List<MarketData> records = marketDataService.getBaseMapper().selectList(queryWrapper);
            PageInfo<MarketData> pageInfo = new PageInfo<>(records);

            // 构建返回结果
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());

            return new JsonBean(1, "成功", data).toJson();
        } catch (Exception e) {
            log.error("获取市场数据列表失败", e);
            return new JsonBean(0, "获取市场数据列表失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 查询市场数据列表(不分页)
     */
    @GetMapping("/list")
    @ApiOperation("查询市场数据列表")
    public String getMarketDataList(
            @ApiParam("数据类型") @RequestParam(required = false) String dataType,
            @ApiParam("数据代码") @RequestParam(required = false) String symbol) {

        try {
            QueryWrapper<MarketData> queryWrapper = new QueryWrapper<>();

            if (StringUtils.hasText(dataType)) {
                queryWrapper.eq("DATA_TYPE", dataType);
            }
            if (StringUtils.hasText(symbol)) {
                queryWrapper.eq("SYMBOL", symbol);
            }

            queryWrapper.eq("IS_ACTIVE", 1);
            queryWrapper.orderByDesc("DATA_DATE");

            return new JsonBean(1, "成功", marketDataService.list(queryWrapper)).toJson();
        } catch (Exception e) {
            log.error("获取市场数据列表失败", e);
            return new JsonBean(0, "获取市场数据列表失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询市场数据
     */
    @GetMapping("/{dataId}")
    @ApiOperation("根据ID查询市场数据")
    public String getMarketData(@PathVariable String dataId) {
        try {
            MarketData marketData = marketDataService.getById(dataId);
            if (marketData == null) {
                return new JsonBean(0, "市场数据不存在", null).toJson();
            }
            return new JsonBean(1, "成功", marketData).toJson();
        } catch (Exception e) {
            log.error("获取市场数据失败", e);
            return new JsonBean(0, "获取市场数据失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建市场数据
     */
    @PostMapping
    @ApiOperation("创建市场数据")
    public String createMarketData(@FlexibleRequestBody MarketData marketData) {
        try {
            // 生成UUID作为ID
            if (marketData.getDataId() == null || marketData.getDataId().isEmpty()) {
                marketData.setDataId(UUID.randomUUID().toString().replace("-", ""));
            }

            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser != null) {
                marketData.setCreateUser(currentUser.getRealname());
                marketData.setUpdateUser(currentUser.getRealname());
            } else {
                marketData.setCreateUser("system");
                marketData.setUpdateUser("system");
            }

            marketData.setCreateTime(new Date());
            marketData.setUpdateTime(new Date());

            // 设置默认值
            if (marketData.getIsActive() == null) {
                marketData.setIsActive(1);
            }

            boolean success = marketDataService.save(marketData);
            if (success) {
                return new JsonBean(1, "新增成功", marketData).toJson();
            } else {
                return new JsonBean(0, "新增失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("新增市场数据失败", e);
            return new JsonBean(0, "新增市场数据失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新市场数据
     */
    @PutMapping
    @ApiOperation("更新市场数据")
    public String updateMarketData(@FlexibleRequestBody MarketData marketData) {
        try {
            if (marketData.getDataId() == null || marketData.getDataId().isEmpty()) {
                return new JsonBean(0, "ID不能为空", null).toJson();
            }

            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser != null) {
                marketData.setUpdateUser(currentUser.getRealname());
            } else {
                marketData.setUpdateUser("system");
            }
            marketData.setUpdateTime(new Date());

            boolean success = marketDataService.updateById(marketData);
            if (success) {
                return new JsonBean(1, "更新成功", marketData).toJson();
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新市场数据失败", e);
            return new JsonBean(0, "更新市场数据失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除市场数据
     */
    @DeleteMapping("/{dataId}")
    @ApiOperation("删除市场数据")
    public String deleteMarketData(
            @PathVariable String dataId,
            @ApiParam("更新人") @RequestParam(required = false) String updateUser) {

        try {
            boolean success = marketDataService.removeById(dataId);
            if (success) {
                return new JsonBean(1, "删除成功", null).toJson();
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除市场数据失败", e);
            return new JsonBean(0, "删除市场数据失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 同步市场数据
     */
    @PostMapping("/sync")
    @ApiOperation("同步市场数据")
    public String syncMarketData(
            @ApiParam("数据类型") @RequestParam(required = false) String dataType,
            @ApiParam("更新人") @RequestParam(required = false) String updateUser) {

        try {
            // 这里可以实现实际的数据同步逻辑
            // 暂时返回成功
            Map<String, Object> result = new HashMap<>();
            result.put("syncTime", new Date());
            result.put("syncCount", 0);
            result.put("message", "同步成功");

            return new JsonBean(1, "同步成功", result).toJson();
        } catch (Exception e) {
            log.error("同步市场数据失败", e);
            return new JsonBean(0, "同步市场数据失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取市场数据统计信息
     */
    @GetMapping("/statistics")
    @ApiOperation("获取市场数据统计信息")
    public String getStatistics() {
        try {
            QueryWrapper<MarketData> queryWrapper = new QueryWrapper<>();
            List<MarketData> allData = marketDataService.list(queryWrapper);

            Map<String, Long> countByType = allData.stream()
                    .filter(data -> data.getDataType() != null)
                    .collect(Collectors.groupingBy(MarketData::getDataType, Collectors.counting()));

            long exchangeRateCount = countByType.getOrDefault("EXCHANGE_RATE", 0L);
            long interestRateCount = countByType.getOrDefault("INTEREST_RATE", 0L);
            long bondYieldCount = countByType.getOrDefault("BOND_YIELD", 0L);

            Date lastUpdate = allData.stream()
                    .map(MarketData::getUpdateTime)
                    .filter(Objects::nonNull)
                    .max(Date::compareTo)
                    .orElse(null);

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("exchangeRateCount", exchangeRateCount);
            statistics.put("interestRateCount", interestRateCount);
            statistics.put("bondYieldCount", bondYieldCount);
            statistics.put("totalCount", allData.size());
            statistics.put("countByType", countByType);
            statistics.put("lastUpdateTime", lastUpdate);

            return new JsonBean(1, "查询成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出市场数据
     */
    @GetMapping("/export")
    @ApiOperation("导出市场数据")
    public void exportMarketData(
            @ApiParam("数据类型") @RequestParam(required = false) String dataType,
            @ApiParam("数据代码") @RequestParam(required = false) String symbol,
            HttpServletResponse response) {

        try {
            QueryWrapper<MarketData> queryWrapper = new QueryWrapper<>();

            if (StringUtils.hasText(dataType)) {
                queryWrapper.eq("DATA_TYPE", dataType);
            }
            if (StringUtils.hasText(symbol)) {
                queryWrapper.like("SYMBOL", symbol);
            }

            queryWrapper.orderByDesc("DATA_DATE");

            List<MarketData> list = marketDataService.list(queryWrapper);

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(new JsonBean(1, "成功", list).toJson());
        } catch (Exception e) {
            log.error("导出市场数据失败", e);
        }
    }
}
