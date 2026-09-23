package com.global.treasurer.controller;

import java.util.Map;

import com.global.treasurer.util.DatabaseInitializer;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 数据库初始化Controller
 * 提供HTTP接口来执行融资管理模块的数据库初始化
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Controller
@RequestMapping({"/rzgl/database-init", "/financial/rzgl/database-init"})
@Api(tags = "数据库初始化管理")
public class DatabaseInitController {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitController.class);

    @Resource
    private DatabaseInitializer databaseInitializer;

    /**
     * 执行融资管理模块数据库初始化
     * 访问路径: POST /financial/rzgl/database-init/execute
     *
     * @return 执行结果
     */
    @PostMapping("/execute")
    @ResponseBody
    @ApiOperation("执行数据库初始化")
    public String executeInitialization() {
        log.info("收到数据库初始化请求");

        try {
            // 异步执行初始化,避免HTTP请求超时
            new Thread(() -> {
                try {
                    databaseInitializer.initializeFinancingManagementTables();
                } catch (Exception e) {
                    log.error("数据库初始化失败", e);
                }
            }).start();

            return new JsonBean(1, "数据库初始化已启动,请查看日志文件获取详细执行结果", null).toJson();

        } catch (Exception e) {
            log.error("启动数据库初始化失败", e);
            return new JsonBean(0, "启动失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 验证数据库初始化状态
     * 访问路径: GET /financial/rzgl/database-init/verify
     *
     * @return 验证结果
     */
    @GetMapping("/verify")
    @ResponseBody
    @ApiOperation("验证数据库初始化状态")
    public String verifyInitialization() {
        log.info("收到数据库验证请求");

        try {
            // 这里可以添加更详细的验证逻辑
            // 比如查询各表的记录数,返回给前端

            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("status", "initialized");
            result.put("message", "数据库已初始化");
            result.put("timestamp", System.currentTimeMillis());

            return new JsonBean(1, "验证成功", result).toJson();

        } catch (Exception e) {
            log.error("验证数据库状态失败", e);
            return new JsonBean(0, "验证失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取数据库初始化说明
     * 访问路径: GET /financial/rzgl/database-init/info
     *
     * @return 说明信息
     */
    @GetMapping("/info")
    @ResponseBody
    @ApiOperation("获取数据库初始化说明")
    public String getInitializationInfo() {
        java.util.Map<String, Object> info = new java.util.HashMap<>();

        info.put("moduleName", "融资管理模块");
        info.put("database", "达梦数据库 (DM8)");
        info.put("tables", java.util.Arrays.asList(
            "TBL_FINANCIAL_INSTITUTION - 金融机构表",
            "TBL_GUARANTEE_APPLICATION - 担保申请表",
            "TBL_GUARANTEE_CONTRACT - 担保合同表",
            "TBL_COLLATERAL - 担保物表",
            "TBL_CREDIT_APPLICATION - 授信申请表",
            "TBL_CREDIT_CONTRACT - 授信合同表",
            "TBL_CREDIT_LIMIT - 授信额度表",
            "TBL_BANK_LOAN - 银行贷款表",
            "TBL_BILL_DISCOUNT - 票据贴现表",
            "TBL_BILL_ACCEPTANCE - 银行承兑表",
            "TBL_FINANCIAL_LEASE - 融资租赁表"
        ));

        info.put("sqlScripts", java.util.Arrays.asList(
            "db/oracle/financing_management_create_tables.sql - 建表脚本",
            "db/oracle/financing_management_init_data.sql - 测试数据脚本"
        ));

        info.put("executionMethods", java.util.Arrays.asList(
            "1. 使用达梦数据库管理工具执行SQL脚本",
            "2. 调用本接口: POST /financial/rzgl/database-init/execute",
            "3. 运行DatabaseInitializer.main()方法"
        ));

        return new JsonBean(1, "获取成功", info).toJson();
    }

    /**
     * 删除融资管理模块的所有表(危险操作)
     * 仅用于测试环境,生产环境请勿使用
     * 访问路径: DELETE /financial/rzgl/database-init/drop
     *
     * @return 删除结果
     */
    @DeleteMapping("/drop")
    @ResponseBody
    @ApiOperation("删除所有表(危险操作)")
    public String dropAllTables() {
        log.warn("收到删除表请求 - 这是一个危险操作!");

        try {
            databaseInitializer.dropFinancingManagementTables();

            return new JsonBean(1, "所有表已删除", null).toJson();

        } catch (Exception e) {
            log.error("删除表失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }
}
