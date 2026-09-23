package com.financial.sharing.controller;

import com.financial.sharing.service.RevenueBatchSettlementService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.RevenueBatchSettlementQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 收入批量结账Controller
 * 
 * @author AI Agent
 * @since 2025-11-29
 */
@Slf4j
@Api(tags = "收入批量结账管理")
@RestController
@RequestMapping("/revenue/batch-settlement")
public class RevenueBatchSettlementController {
    
    @Autowired
    private RevenueBatchSettlementService revenueBatchSettlementService;
    
    @ApiOperation("分页查询收入批量结账列表")
    @GetMapping("/getList")
    public MyJsonBean<PageResult<Map<String, Object>>> getSettlementList(RevenueBatchSettlementQueryParam param) {
        log.info("查询收入批量结账列表，参数: {}", param);
        try {
            PageResult<Map<String, Object>> result = revenueBatchSettlementService.getSettlementPage(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询收入批量结账列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("获取结账详情")
    @GetMapping("/detail/{settlementId}")
    public MyJsonBean<Map<String, Object>> getSettlementDetail(
            @ApiParam("结账ID") @PathVariable Long settlementId) {
        log.info("查询结账详情，ID: {}", settlementId);
        try {
            Map<String, Object> result = revenueBatchSettlementService.getSettlementById(settlementId);
            if (result == null) {
                return MyJsonBean.errorData("结账记录不存在");
            }
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询结账详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("执行结账")
    @PostMapping("/doSettle")
    public MyJsonBean<String> doSettlement(@RequestBody Map<String, Object> params) {
        log.info("执行结账，参数: {}", params);
        try {
            Long settlementId = Long.valueOf(params.get("settlementId").toString());
            Long userId = params.get("userId") != null ? Long.valueOf(params.get("userId").toString()) : null;
            String userName = params.get("userName") != null ? params.get("userName").toString() : "";
            
            boolean success = revenueBatchSettlementService.doSettlement(settlementId, userId, userName);
            if (success) {
                return MyJsonBean.successData("结账成功");
            } else {
                return MyJsonBean.errorData("结账失败");
            }
        } catch (Exception e) {
            log.error("执行结账失败", e);
            return MyJsonBean.errorData("结账失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("取消结账")
    @PostMapping("/cancelSettle")
    public MyJsonBean<String> cancelSettlement(@RequestBody Map<String, Object> params) {
        log.info("取消结账，参数: {}", params);
        try {
            Long settlementId = Long.valueOf(params.get("settlementId").toString());
            Long userId = params.get("userId") != null ? Long.valueOf(params.get("userId").toString()) : null;
            String userName = params.get("userName") != null ? params.get("userName").toString() : "";
            
            boolean success = revenueBatchSettlementService.cancelSettlement(settlementId, userId, userName);
            if (success) {
                return MyJsonBean.successData("取消结账成功");
            } else {
                return MyJsonBean.errorData("取消结账失败");
            }
        } catch (Exception e) {
            log.error("取消结账失败", e);
            return MyJsonBean.errorData("取消结账失败: " + e.getMessage());
        }
    }
}

