package com.financial.sharing.controller;

import com.financial.sharing.service.FinanceConfigService;
import com.financial.sharing.util.LegalDealUserToken;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import com.hbfk.entity.TblStaffUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 财务配置控制器
 * 处理财务版本信息、数据源、采集方案等配置管理
 *
 * @author system
 * @date 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/config")
@CrossOrigin
public class FinanceConfigController {

    @Autowired
    private FinanceConfigService financeConfigService;

    // ==================== 财务版本信息管理 ====================

    /**
     * 获取财务版本信息分类
     */
    @GetMapping("/fversion/getParentList")
    public MyJsonBean getCwbbxxType() {
        try {
            return MyJsonBean.successData(financeConfigService.getCwbbxxType());
        } catch (Exception e) {
            log.error("获取财务版本信息分类失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取财务版本信息列表
     */
    @GetMapping("/fversion/getList")
    public MyJsonBean getCwbbxxList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeConfigService.getCwbbxxList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取财务版本信息列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增财务版本信息
     */
    @PostMapping("/fversion/save")
    public MyJsonBean saveCwbbxx(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeConfigService.saveCwbbxx(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("新增财务版本信息失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 删除财务版本信息
     */
    @GetMapping("/fversion/remove")
    public MyJsonBean deleteCwbbxx(@RequestParam Map<String, Object> params) {
        try {
            boolean result = financeConfigService.deleteCwbbxx(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("删除财务版本信息失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取财务版本信息详情
     */
    @GetMapping("/fversion/detail")
    public MyJsonBean getCwbbxxDetail(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeConfigService.getCwbbxxDetail(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取财务版本信息详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 数据源管理 ====================

    /**
     * 数据源列表
     */
    @GetMapping("/fdataSource/getList")
    public MyJsonBean getDataSourceList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeConfigService.getDataSourceList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取数据源列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 数据源详情
     */
    @GetMapping("/fdataSource/detail")
    public MyJsonBean getDataSourceDetail(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeConfigService.getDataSourceDetail(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取数据源详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 数据源新增
     */
    @PostMapping("/fdataSource/save")
    public MyJsonBean saveDataSource(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeConfigService.saveDataSource(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("新增数据源失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 数据源删除
     */
    @GetMapping("/fdataSource/remove")
    public MyJsonBean deleteDataSource(@RequestParam Map<String, Object> params) {
        try {
            boolean result = financeConfigService.deleteDataSource(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("删除数据源失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 数据源测试连接
     */
    @PostMapping("/fdataSource/testCon")
    public MyJsonBean testDataSource(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeConfigService.testDataSource(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("测试数据源连接失败", e);
            return MyJsonBean.errorData("测试失败: " + e.getMessage());
        }
    }

    // ==================== 采集方案管理 ====================

    /**
     * 采集方案列表
     */
    @GetMapping("/fplan/getList")
    public MyJsonBean getCjfaList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeConfigService.getCjfaList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取采集方案列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 采集方案详情
     */
    @GetMapping("/fplan/detail")
    public MyJsonBean getCjfaDetail(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeConfigService.getCjfaDetail(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取采集方案详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 采集方案新增
     */
    @PostMapping("/fplan/save")
    public MyJsonBean saveCjfa(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeConfigService.saveCjfa(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("新增采集方案失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 采集方案删除
     */
    @GetMapping("/fplan/remove")
    public MyJsonBean deleteCjfa(@RequestParam Map<String, Object> params) {
        try {
            boolean result = financeConfigService.deleteCjfa(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("删除采集方案失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    // ==================== 采集SQL管理 ====================

    /**
     * 新增的时候，调详情接口获取sql基本信息
     */
    @GetMapping("/fconfigSql/detail")
    public MyJsonBean getCjsqlAddDetail(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeConfigService.getCjsqlAddDetail(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取采集SQL基本信息失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 采集sql详情
     */
    @GetMapping("/fplanSql/detail")
    public MyJsonBean getCjsqlDetail(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = financeConfigService.getCjsqlDetail(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取采集SQL详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 采集sql列表
     */
    @GetMapping("/fplanSql/getList")
    public MyJsonBean getCjsqlList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeConfigService.getCjsqlList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取采集SQL列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 采集sql新增
     */
    @PostMapping("/fplanSql/save")
    public MyJsonBean saveCjsql(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeConfigService.saveCjsql(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("新增采集SQL失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 采集sql删除
     */
    @PostMapping("/fplanSql/remove")
    public MyJsonBean deleteCjsql(@RequestBody Map<String, Object> data) {
        try {
            boolean result = financeConfigService.deleteCjsql(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("删除采集SQL失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 采集sql测试连接
     */
    @PostMapping("/fplanSql/testSql")
    public MyJsonBean testCjsql(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeConfigService.testCjsql(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("测试采集SQL失败", e);
            return MyJsonBean.errorData("测试失败: " + e.getMessage());
        }
    }

    /**
     * 采集sql执行
     */
    @PostMapping("/fplanSql/excuteSql")
    public MyJsonBean executeCjsql(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = financeConfigService.executeCjsql(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("执行采集SQL失败", e);
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    // ==================== 离线工具下载 ====================

    /**
     * 下载离线工具
     */
    @GetMapping("/offline/download")
    public MyJsonBean downloadOfflineTool() {
        try {
            Map<String, Object> result = financeConfigService.downloadOfflineTool();
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("下载离线工具失败", e);
            return MyJsonBean.errorData("下载失败: " + e.getMessage());
        }
    }
}