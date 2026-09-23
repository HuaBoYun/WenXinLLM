package com.financial.sharing.controller;

import com.financial.sharing.service.FinanceBuDataService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 财务业务数据控制器
 * 处理业务数据管理、定时任务等功能
 *
 * @author system
 * @date 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/budata")
@CrossOrigin
public class FinanceBuDataController {

    @Autowired
    private FinanceBuDataService financeBuDataService;

    // ==================== 业务数据管理 ====================

    /**
     * 业务数据列表
     */
    @GetMapping("/getTablInfoList")
    public MyJsonBean getYWSJList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeBuDataService.getYWSJList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取业务数据列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 业务数据新建
     */
    @PostMapping("/addTableInfo")
    public MyJsonBean getYWSJAdd(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeBuDataService.getYWSJAdd(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("新建业务数据失败", e);
            return MyJsonBean.errorData("新建失败: " + e.getMessage());
        }
    }

    /**
     * 业务数据编辑
     */
    @PostMapping("/modifyTableInfo")
    public MyJsonBean getYWSJEdit(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeBuDataService.getYWSJEdit(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("编辑业务数据失败", e);
            return MyJsonBean.errorData("编辑失败: " + e.getMessage());
        }
    }

    /**
     * 业务数据详情
     */
    @GetMapping("/getTableInfo")
    public MyJsonBean getYWSJDetail(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeBuDataService.getYWSJDetail(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取业务数据详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 业务数据列表删除
     */
    @GetMapping("/removeTableInfo")
    public MyJsonBean getYWSJDelete(@RequestParam Map<String, Object> params) {
        try {
            boolean result = financeBuDataService.getYWSJDelete(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("删除业务数据失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 业务数据列表发布
     */
    @GetMapping("/tableCreate")
    public MyJsonBean YWSJFabu(@RequestParam Map<String, Object> params) {
        try {
            boolean result = financeBuDataService.YWSJFabu(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("发布业务数据失败", e);
            return MyJsonBean.errorData("发布失败: " + e.getMessage());
        }
    }

    /**
     * 业务数据列表启用、禁用
     */
    @GetMapping("/tableFstatus")
    public MyJsonBean changeStatus(@RequestParam Map<String, Object> params) {
        try {
            boolean result = financeBuDataService.changeStatus(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("修改业务数据状态失败", e);
            return MyJsonBean.errorData("修改状态失败: " + e.getMessage());
        }
    }

    /**
     * 业务数据列表采集开始
     */
    @GetMapping("/gatherBussinessData")
    public MyJsonBean startCaiJi(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeBuDataService.startCaiJi(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("开始采集业务数据失败", e);
            return MyJsonBean.errorData("开始采集失败: " + e.getMessage());
        }
    }

    /**
     * 业务数据列表采集停止
     */
    @GetMapping("/stopGatherData")
    public MyJsonBean stopCaiJi(@RequestParam Map<String, Object> params) {
        try {
            boolean result = financeBuDataService.stopCaiJi(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("停止采集业务数据失败", e);
            return MyJsonBean.errorData("停止采集失败: " + e.getMessage());
        }
    }

    /**
     * sql测试
     */
    @PostMapping("/executeSql")
    public MyJsonBean sqlTest(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeBuDataService.sqlTest(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("SQL测试失败", e);
            return MyJsonBean.errorData("测试失败: " + e.getMessage());
        }
    }
}