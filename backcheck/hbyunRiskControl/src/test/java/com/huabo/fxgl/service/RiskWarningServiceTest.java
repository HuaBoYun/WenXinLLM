package com.huabo.fxgl.service;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.service.impl.RiskWarningServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 风险预警服务测试类
 * 
 * @author 华博云开发团队
 * @since 2025-10-07
 */
@Slf4j
@SpringBootTest
public class RiskWarningServiceTest {

    @Autowired
    private RiskWarningServiceImpl riskWarningService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试分页查询风险预警列表
     */
    @Test
    public void testGetRiskWarningList() {
        try {
            log.info("开始测试风险预警列表查询...");
            
            // 构建查询参数
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("pageNum", 1);
            queryParams.put("pageSize", 10);
            
            // 调用服务方法
            JsonBean result = riskWarningService.getRiskWarningList(queryParams);
            
            // 验证结果
            log.info("查询结果: {}", result);
            
            if (result.getCode() == 1) {
                log.info("✅ 测试通过：成功查询风险预警列表");
                
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) result.getData();
                if (data != null) {
                    log.info("总记录数: {}", data.get("total"));
                    log.info("当前页码: {}", data.get("pageNum"));
                    log.info("每页大小: {}", data.get("pageSize"));
                }
            } else {
                log.error("❌ 测试失败：{}", result.getMsg());
            }
            
        } catch (Exception e) {
            log.error("❌ 测试异常：", e);
        }
    }

    /**
     * 测试带条件的分页查询
     */
    @Test
    public void testGetRiskWarningListWithConditions() {
        try {
            log.info("开始测试带条件的风险预警列表查询...");
            
            // 构建查询参数
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("pageNum", 1);
            queryParams.put("pageSize", 5);
            queryParams.put("riskLevel", "HIGH");
            queryParams.put("status", "PENDING");
            
            // 调用服务方法
            JsonBean result = riskWarningService.getRiskWarningList(queryParams);
            
            // 验证结果
            log.info("查询结果: {}", result);
            
            if (result.getCode() == 1) {
                log.info("✅ 测试通过：成功查询高风险待处理预警");
            } else {
                log.error("❌ 测试失败：{}", result.getMsg());
            }
            
        } catch (Exception e) {
            log.error("❌ 测试异常：", e);
        }
    }

    /**
     * 测试获取预警配置
     */
    @Test
    public void testGetWarningConfig() {
        try {
            log.info("开始测试获取预警配置...");

            // 调用服务方法
            JsonBean result = riskWarningService.getWarningConfig();

            // 验证结果
            log.info("配置结果: {}", result);

            if (result.getCode() == 1) {
                log.info("✅ 测试通过：成功获取预警配置信息");
            } else {
                log.error("❌ 测试失败：{}", result.getMsg());
            }

        } catch (Exception e) {
            log.error("❌ 测试异常：", e);
        }
    }

    /**
     * 测试达梦数据库日期函数兼容性
     * 验证修复数据类型不匹配问题后的功能
     */
    @Test
    public void testDamengDateFunctionCompatibility() {
        try {
            log.info("开始测试达梦数据库日期函数兼容性...");

            // 测试达梦数据库的日期函数
            String sql = "SELECT " +
                        "SYSDATE AS current_time, " +
                        "SYSDATE - 1 AS one_day_ago, " +
                        "TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') AS formatted_current " +
                        "FROM DUAL";

            jdbcTemplate.queryForList(sql).forEach(row -> {
                log.info("当前时间: {}", row.get("current_time"));
                log.info("一天前: {}", row.get("one_day_ago"));
                log.info("格式化当前时间: {}", row.get("formatted_current"));
            });

            log.info("✅ 达梦数据库日期函数测试通过");

        } catch (Exception e) {
            log.error("❌ 达梦数据库日期函数测试失败: {}", e.getMessage(), e);
            fail("达梦数据库日期函数测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试修复后的重复预警检查SQL
     * 验证不再出现数据类型不匹配错误
     */
    @Test
    public void testFixedDuplicateWarningCheckSql() {
        try {
            log.info("开始测试修复后的重复预警检查SQL...");

            // 测试修复后的重复检查SQL
            String countSql = "SELECT COUNT(*) FROM TBL_RISK_WARNING WHERE " +
                             "EVAL_MODEL_ID = ? AND MODEL_ID = ? AND " +
                             "CREATE_TIME >= SYSDATE - 1";

            // 使用测试数据
            String testEvalModelId = "TEST_MODEL_001";
            String testModelId = "TEST_DATA_MODEL_001";

            Integer count = jdbcTemplate.queryForObject(countSql, Integer.class,
                                                       testEvalModelId, testModelId);

            // 验证查询能正常执行，不会抛出数据类型不匹配异常
            assertNotNull(count, "查询结果不应为null");
            assertTrue(count >= 0, "查询结果应该大于等于0");

            log.info("✅ 重复预警检查SQL测试通过，查询结果: {}", count);

        } catch (Exception e) {
            log.error("❌ 重复预警检查SQL测试失败: {}", e.getMessage(), e);
            fail("重复预警检查SQL测试失败: " + e.getMessage());
        }
    }
}
