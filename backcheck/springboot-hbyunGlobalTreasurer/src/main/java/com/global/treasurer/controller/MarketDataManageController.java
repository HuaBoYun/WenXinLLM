package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtMarketData;
import com.global.treasurer.mapper.TblGtMarketDataMapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 市场数据管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-01-30
 */
@RestController
@RequestMapping({"/treasury/marketDataManage", "/financial/treasury/marketDataManage"})
@Api(tags = "市场数据管理")
public class MarketDataManageController {
    private static final Logger log = LoggerFactory.getLogger(MarketDataManageController.class);

    @Resource
    private TblGtMarketDataMapper tblGtMarketDataMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 1. 分页查询市场数据
     */
    @GetMapping({"/page", "/list"})
    @ApiOperation("分页查询市场数据列表")
    public String getMarketDataPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("数据类型") @RequestParam(required = false) String dataType,
            @ApiParam("数据代码") @RequestParam(required = false) String symbol,
            @ApiParam("数据来源") @RequestParam(required = false) String dataSource,
            @ApiParam("状态") @RequestParam(required = false) Integer status) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("查询市场数据列表, 参数: pageNo={}, pageSize={}, dataType={}, symbol={}, dataSource={}, status={}",
                    pageNo, pageSize, dataType, symbol, dataSource, status);

            // 构建分页对象
            Page<TblGtMarketData> page = new Page<>(pageNo, pageSize);

            // 构建查询条件
            QueryWrapper<TblGtMarketData> queryWrapper = new QueryWrapper<>();

            // 数据类型模糊查询
            if (StringUtils.hasText(dataType)) {
                queryWrapper.like("DATA_TYPE", dataType);
            }

            // 数据代码模糊查询
            if (StringUtils.hasText(symbol)) {
                queryWrapper.like("SYMBOL", symbol);
            }

            // 数据来源模糊查询
            if (StringUtils.hasText(dataSource)) {
                queryWrapper.like("DATA_SOURCE", dataSource);
            }

            // 状态精确查询
            if (status != null) {
                queryWrapper.eq("STATUS", status);
            }

            // 按创建时间倒序
            queryWrapper.orderByDesc("CREATE_TIME");

            // 执行分页查询
            IPage<TblGtMarketData> resultPage = tblGtMarketDataMapper.selectPage(page, queryWrapper);

            // 构建返回结果
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", resultPage.getRecords());
            data.put("totalRecord", resultPage.getTotal());
            data.put("pageNo", pageNo);
            data.put("pageSize", pageSize);

            log.info("查询市场数据成功, 共{}条记录", resultPage.getTotal());
            return new JsonBean(1, "查询成功", data).toJson();

        } catch (Exception e) {
            log.error("查询市场数据列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 2. 新增市场数据
     */
    @PostMapping
    @ApiOperation("新增市场数据")
    public String addMarketData(@FlexibleRequestBody TblGtMarketData marketData) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("新增市场数据, 参数: {}", marketData);

            // 设置创建信息
            marketData.setCreateTime(new Date());
            marketData.setCreateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().toString() : "system");
            marketData.setUpdateTime(new Date());
            marketData.setUpdateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().toString() : "system");

            // 设置默认值
            if (marketData.getIsActive() == null) {
                marketData.setIsActive(1);
            }
            if (marketData.getStatus() == null) {
                marketData.setStatus(1);
            }

            // 插入数据
            int result = tblGtMarketDataMapper.insert(marketData);

            if (result > 0) {
                log.info("新增市场数据成功, ID={}", marketData.getDataId());
                return new JsonBean(1, "新增成功", marketData).toJson();
            } else {
                log.warn("新增市场数据失败");
                return new JsonBean(0, "新增失败", null).toJson();
            }

        } catch (Exception e) {
            log.error("新增市场数据失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 3. 更新市场数据
     */
    @PutMapping
    @ApiOperation("更新市场数据")
    public String updateMarketData(@FlexibleRequestBody TblGtMarketData marketData) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (marketData.getDataId() == null) {
                return new JsonBean(0, "数据ID不能为空", null).toJson();
            }

            log.info("更新市场数据, 参数: {}", marketData);

            // 设置更新信息
            marketData.setUpdateTime(new Date());
            marketData.setUpdateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().toString() : "system");

            // 更新数据
            int result = tblGtMarketDataMapper.updateById(marketData);

            if (result > 0) {
                log.info("更新市场数据成功, ID={}", marketData.getDataId());
                return new JsonBean(1, "更新成功", marketData).toJson();
            } else {
                log.warn("更新市场数据失败, ID={}", marketData.getDataId());
                return new JsonBean(0, "更新失败,数据不存在", null).toJson();
            }

        } catch (Exception e) {
            log.error("更新市场数据失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 4. 删除市场数据(根据ID)
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除市场数据")
    public String deleteMarketData(
            @ApiParam("数据ID") @PathVariable Long id) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("删除市场数据, ID={}", id);

            // 删除数据
            int result = tblGtMarketDataMapper.deleteById(id);

            if (result > 0) {
                log.info("删除市场数据成功, ID={}", id);
                return new JsonBean(1, "删除成功", null).toJson();
            } else {
                log.warn("删除市场数据失败, ID={}", id);
                return new JsonBean(0, "删除失败,数据不存在", null).toJson();
            }

        } catch (Exception e) {
            log.error("删除市场数据失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 5. 批量删除市场数据
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除市场数据")
    public String batchDeleteMarketData(
            @ApiParam("数据ID列表") @RequestParam(value = "ids", required = false) List<Long> ids) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (ids == null || ids.isEmpty()) {
                return new JsonBean(0, "请选择要删除的数据", null).toJson();
            }

            log.info("批量删除市场数据, IDs={}", ids);

            // 批量删除
            int result = tblGtMarketDataMapper.deleteBatchIds(ids);

            if (result > 0) {
                log.info("批量删除市场数据成功, 删除{}条", result);
                return new JsonBean(1, "成功删除" + result + "条数据", result).toJson();
            } else {
                log.warn("批量删除市场数据失败");
                return new JsonBean(0, "删除失败", null).toJson();
            }

        } catch (Exception e) {
            log.error("批量删除市场数据失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 6. 切换状态(启用/禁用)
     */
    @PutMapping("/status")
    @ApiOperation("切换状态")
    public String toggleStatus(
            @ApiParam("数据ID") @RequestParam Long id,
            @ApiParam("状态") @RequestParam Integer status) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("切换市场数据状态, ID={}, status={}", id, status);

            // 查询原数据
            TblGtMarketData marketData = tblGtMarketDataMapper.selectById(id);
            if (marketData == null) {
                return new JsonBean(0, "数据不存在", null).toJson();
            }

            // 更新状态
            marketData.setStatus(status);
            marketData.setUpdateTime(new Date());
            marketData.setUpdateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().toString() : "system");

            int result = tblGtMarketDataMapper.updateById(marketData);

            if (result > 0) {
                log.info("切换状态成功, ID={}, status={}", id, status);
                return new JsonBean(1, "状态切换成功", marketData).toJson();
            } else {
                log.warn("切换状态失败");
                return new JsonBean(0, "状态切换失败", null).toJson();
            }

        } catch (Exception e) {
            log.error("切换状态失败", e);
            return new JsonBean(0, "状态切换失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 7. 根据ID查询市场数据
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询市场数据")
    public String getMarketDataById(
            @ApiParam("数据ID") @PathVariable Long id) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("查询市场数据详情, ID={}", id);

            // 查询数据
            TblGtMarketData marketData = tblGtMarketDataMapper.selectById(id);

            if (marketData != null) {
                log.info("查询市场数据详情成功, ID={}", id);
                return new JsonBean(1, "查询成功", marketData).toJson();
            } else {
                log.warn("查询市场数据详情失败, 数据不存在, ID={}", id);
                return new JsonBean(0, "数据不存在", null).toJson();
            }

        } catch (Exception e) {
            log.error("查询市场数据详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 8. 获取实时市场数据
     */
    @GetMapping("/realTime")
    @ApiOperation("获取实时市场数据")
    public String getRealTimeData(
            @ApiParam("数据类型") @RequestParam(required = false) String dataType,
            @ApiParam("数据代码") @RequestParam(required = false) String symbol) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("获取实时市场数据, dataType={}, symbol={}", dataType, symbol);

            // 构建查询条件
            QueryWrapper<TblGtMarketData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("IS_ACTIVE", 1);
            queryWrapper.eq("STATUS", 1);

            if (StringUtils.hasText(dataType)) {
                queryWrapper.eq("DATA_TYPE", dataType);
            }

            if (StringUtils.hasText(symbol)) {
                queryWrapper.eq("SYMBOL", symbol);
            }

            // 按数据时间倒序,取最新数据
            queryWrapper.orderByDesc("DATA_TIME");

            // 查询最新数据
            List<TblGtMarketData> dataList = tblGtMarketDataMapper.selectList(queryWrapper);

            // 按数据代码分组,取每组的最新一条
            Map<String, TblGtMarketData> realTimeDataMap = dataList.stream()
                    .collect(Collectors.toMap(
                            TblGtMarketData::getSymbol,
                            data -> data,
                            (existing, replacement) -> existing
                    ));

            List<TblGtMarketData> realTimeDataList = new ArrayList<>(realTimeDataMap.values());

            log.info("获取实时市场数据成功, 共{}条", realTimeDataList.size());
            return new JsonBean(1, "查询成功", realTimeDataList).toJson();

        } catch (Exception e) {
            log.error("获取实时市场数据失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 9. 同步市场数据
     */
    @PostMapping("/sync")
    @ApiOperation("同步市场数据")
    public String syncMarketData(
            @ApiParam("数据来源") @RequestParam String dataSource,
            @ApiParam("数据类型") @RequestParam(required = false) String dataType) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("同步市场数据, dataSource={}, dataType={}", dataSource, dataType);

            // TODO: 这里应该调用外部数据源的API进行同步
            // 目前先返回成功,实际项目中需要对接外部数据源

            Map<String, Object> result = new HashMap<>();
            result.put("dataSource", dataSource);
            result.put("dataType", dataType);
            result.put("syncTime", new Date());
            result.put("syncCount", 0);

            log.info("同步市场数据成功");
            return new JsonBean(1, "同步成功", result).toJson();

        } catch (Exception e) {
            log.error("同步市场数据失败", e);
            return new JsonBean(0, "同步失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 10. 获取统计信息
     */
    @GetMapping("/statistics")
    @ApiOperation("获取统计信息")
    public String getStatistics() {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("获取市场数据统计信息");

            // 查询全部数据
            QueryWrapper<TblGtMarketData> queryWrapper = new QueryWrapper<>();
            List<TblGtMarketData> allData = tblGtMarketDataMapper.selectList(queryWrapper);

            // 统计总数
            int totalCount = allData.size();

            // 按数据类型统计
            Map<String, Long> countByType = allData.stream()
                    .collect(Collectors.groupingBy(
                            data -> data.getDataType() != null ? data.getDataType() : "未知",
                            Collectors.counting()
                    ));

            // 按数据来源统计
            Map<String, Long> countBySource = allData.stream()
                    .collect(Collectors.groupingBy(
                            data -> data.getDataSource() != null ? data.getDataSource() : "未知",
                            Collectors.counting()
                    ));

            // 按状态统计
            long activeCount = allData.stream().filter(data -> data.getIsActive() != null && data.getIsActive() == 1).count();
            long inactiveCount = allData.stream().filter(data -> data.getIsActive() != null && data.getIsActive() == 0).count();

            // 构建统计结果
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalCount", totalCount);
            statistics.put("activeCount", activeCount);
            statistics.put("inactiveCount", inactiveCount);
            statistics.put("countByType", countByType);
            statistics.put("countBySource", countBySource);

            log.info("获取市场数据统计信息成功");
            return new JsonBean(1, "查询成功", statistics).toJson();

        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}
