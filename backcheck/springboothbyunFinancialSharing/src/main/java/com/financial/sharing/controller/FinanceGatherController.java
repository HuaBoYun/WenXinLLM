package com.financial.sharing.controller;

import com.financial.sharing.service.FinanceGatherService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 财务采集控制器
 * 处理数据采集、采集方案、采集记录等功能
 *
 * @author system
 * @date 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/gather")
@CrossOrigin
public class FinanceGatherController {

    @Autowired
    private FinanceGatherService financeGatherService;

    // ==================== 数据采集管理 ====================

    /**
     * 执行抽取
     */
    @GetMapping("/exeUnique")
    public MyJsonBean executeChouqu(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeGatherService.executeChouqu(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("执行抽取失败", e);
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    /**
     * 停止采集
     */
    @GetMapping("/stopGather")
    public MyJsonBean stopChouqu(@RequestParam Map<String, Object> params) {
        try {
            boolean result = financeGatherService.stopChouqu(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("停止采集失败", e);
            return MyJsonBean.errorData("停止失败: " + e.getMessage());
        }
    }

    /**
     * 获取采集方案结构树
     */
    @GetMapping("/getFinancePlanTreeList")
    public MyJsonBean getFaTree(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeGatherService.getFaTree(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取采集方案结构树失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 停止方案采集
     */
    @GetMapping("/stopFinancePlanProcess")
    public MyJsonBean stopFaGather(@RequestParam Map<String, Object> params) {
        try {
            boolean result = financeGatherService.stopFaGather(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("停止方案采集失败", e);
            return MyJsonBean.errorData("停止失败: " + e.getMessage());
        }
    }

    /**
     * 开始方案采集
     */
    @GetMapping("/beginFinancePlan")
    public MyJsonBean startFaGather(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeGatherService.startFaGather(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("开始方案采集失败", e);
            return MyJsonBean.errorData("开始失败: " + e.getMessage());
        }
    }

    /**
     * 采集子方案采集状态
     */
    @GetMapping("/getFinancePlanStatus")
    public MyJsonBean getFaGatherStatus(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeGatherService.getFaGatherStatus(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取采集子方案采集状态失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 停止采集子方案
     */
    @GetMapping("/stopFinanceUniqueProcess")
    public MyJsonBean stopFaGatherSub(@RequestParam Map<String, Object> params) {
        try {
            boolean result = financeGatherService.stopFaGatherSub(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("停止采集子方案失败", e);
            return MyJsonBean.errorData("停止失败: " + e.getMessage());
        }
    }

    /**
     * 业务数据采集记录
     */
    @GetMapping("/getFinanceRecordPageList")
    public MyJsonBean CaiJiLog(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeGatherService.CaiJiLog(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取业务数据采集记录失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 业务数据采集记录当前的采集结果
     */
    @GetMapping("/getRecordDetail")
    public MyJsonBean CaiJiLogResult(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeGatherService.CaiJiLogResult(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取业务数据采集记录当前的采集结果失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}