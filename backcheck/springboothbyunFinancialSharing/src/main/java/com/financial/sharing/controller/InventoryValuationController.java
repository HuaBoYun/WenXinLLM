package com.financial.sharing.controller;

import com.financial.sharing.service.InventoryValuationService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存货计价管理控制器
 * 
 * @author Financial Sharing System
 * @since 2026-01-27
 */
@Slf4j
@Api(tags = "存货计价管理")
@RestController
@RequestMapping("/cwgxAi/inventory/valuation")
@CrossOrigin
public class InventoryValuationController {

    @Autowired
    private InventoryValuationService inventoryValuationService;

    /**
     * 查询存货计价列表
     */
    @ApiOperation("查询存货计价列表")
    @PostMapping("/getList")
    public MyJsonBean<PageResult<Map<String, Object>>> getList(@RequestBody Map<String, Object> param) {
        try {
            log.info("查询存货计价列表，参数: {}", param);
            PageResult<Map<String, Object>> pageResult = inventoryValuationService.getValuationPage(param);
            return MyJsonBean.successData("操作成功", pageResult);
        } catch (Exception e) {
            log.error("查询存货计价列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询存货计价详情
     */
    @ApiOperation("根据ID查询存货计价详情")
    @PostMapping("/getById")
    public MyJsonBean<Map<String, Object>> getById(@RequestBody Map<String, Object> param) {
        try {
            Long valuationId = Long.valueOf(param.get("valuationId").toString());
            log.info("查询存货计价详情，ID: {}", valuationId);
            Map<String, Object> result = inventoryValuationService.getValuationById(valuationId);
            if (result == null) {
                return MyJsonBean.errorData("数据不存在");
            }
            return MyJsonBean.successData("操作成功", result);
        } catch (Exception e) {
            log.error("查询存货计价详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 保存或更新存货计价
     */
    @ApiOperation("保存或更新存货计价")
    @PostMapping("/saveOrUpdate")
    public MyJsonBean<Map<String, Object>> saveOrUpdate(@RequestBody Map<String, Object> param) {
        try {
            log.info("保存或更新存货计价，参数: {}", param);
            boolean success = inventoryValuationService.saveOrUpdateValuation(param);

            if (success) {
                Map<String, Object> result = new HashMap<>();
                result.put("action", param.get("valuationId") != null ? "更新" : "新增");
                result.put("valuationId", param.get("valuationId"));
                return MyJsonBean.successData(param.get("valuationId") != null ? "更新成功" : "新增成功", result);
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            log.error("保存或更新存货计价失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 删除存货计价
     */
    @ApiOperation("删除存货计价")
    @PostMapping("/delete")
    public MyJsonBean<Boolean> delete(@RequestBody Map<String, Object> param) {
        try {
            Long valuationId = Long.valueOf(param.get("valuationId").toString());
            log.info("删除存货计价，ID: {}", valuationId);
            boolean success = inventoryValuationService.deleteValuation(valuationId);
            if (success) {
                return MyJsonBean.successData("删除成功", true);
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除存货计价失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除存货计价
     */
    @ApiOperation("批量删除存货计价")
    @PostMapping("/batchDelete")
    public MyJsonBean<Boolean> batchDelete(@RequestBody Map<String, Object> param) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> valuationIds = (List<Long>) param.get("valuationIds");
            log.info("批量删除存货计价，IDs: {}", valuationIds);
            boolean success = inventoryValuationService.batchDeleteValuation(valuationIds);
            if (success) {
                return MyJsonBean.successData("批量删除成功", true);
            } else {
                return MyJsonBean.errorData("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除存货计价失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取仓库列表
     */
    @ApiOperation("获取仓库列表")
    @GetMapping("/warehouseList")
    public MyJsonBean<List<Map<String, Object>>> getWarehouseList() {
        try {
            log.info("查询仓库列表");
            List<Map<String, Object>> list = inventoryValuationService.getWarehouseList();
            return MyJsonBean.successData("操作成功", list);
        } catch (Exception e) {
            log.error("查询仓库列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}

