package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.dto.MonthlyEvaluationDTO;
import com.huabo.fxgl.dto.ReminderDTO;
import com.huabo.fxgl.dto.RiskDatabaseDTO;
import com.huabo.fxgl.dto.RiskReviewAnalysisDTO;
import com.huabo.fxgl.dto.RiskEventCountDTO;
import com.huabo.fxgl.dto.RiskMeasureStatusDTO;
import com.huabo.fxgl.dto.RiskCompletionDTO;
import com.huabo.fxgl.mapper.ReminderMapper;
import com.huabo.fxgl.service.IReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 催办提醒 服务实现类
 *
 * @author AI Assistant
 * @since 2025-01-07
 */
@Slf4j
@Service
public class ReminderServiceImpl implements IReminderService {

    @Resource
    private ReminderMapper reminderMapper;

    /**
     * 查询未读的催办提醒列表
     *
     * @return 催办提醒列表
     */
    @Override
    public List<ReminderDTO> getUnreadReminderList() {
        log.info("查询未读催办提醒列表");
        List<ReminderDTO> list = reminderMapper.selectUnreadReminderList();

        // 将 lx 列的表名映射为中文
        if (list != null && !list.isEmpty()) {
            list.forEach(item -> {
                String lx = item.getLx();
                if (lx != null && !lx.trim().isEmpty()) {
                    // 映射表名到中文名称
                    switch (lx.trim()) {
                        case "TBL_RISK_MONTHLY_EVALUATION":
                            item.setLx("月度评估");
                            break;
                        case "TBL_CONTROL_ENTRIES":
                            item.setLx("管控措施");
                            break;
                        default:
                            // 其他表名保持原样或显示"其他类型"
                            if (lx.trim().isEmpty() || "yd".equalsIgnoreCase(lx.trim())) {
                                item.setLx("其他类型");
                            }
                            // 否则保持原表名显示
                            break;
                    }
                } else {
                    item.setLx("其他类型");
                }
            });
        }

        log.info("查询到未读催办提醒数量: {}", list != null ? list.size() : 0);
        return list;
    }

    /**
     * 查询月度评估情况一览表
     *
     * @return 月度评估情况列表
     */
    @Override
    public List<MonthlyEvaluationDTO> getMonthlyEvaluationList() {
        log.info("查询月度评估情况一览表");
        List<MonthlyEvaluationDTO> list = reminderMapper.selectMonthlyEvaluationList();

        // 处理空值：如果 riskchange 或 copingcount 为 null，设置默认值
        if (list != null) {
            list.forEach(item -> {
                if (item.getRiskchange() == null || item.getRiskchange().trim().isEmpty()) {
                    item.setRiskchange("0");
                }
                if (item.getCopingcount() == null) {
                    item.setCopingcount(0);
                }
            });
        }

        log.info("查询到月度评估情况数量: {}", list != null ? list.size() : 0);
        return list;
    }

    /**
     * 查询风险数据库一览表
     *
     * @return 风险数据库一览表列表
     */
    @Override
    public List<RiskDatabaseDTO> getRiskDatabaseList() {
        log.info("查询风险数据库一览表");
        List<RiskDatabaseDTO> list = reminderMapper.selectRiskDatabaseList();

        // 处理空值
        if (list != null) {
            list.forEach(item -> {
                if (item.getTotalCount() == null) {
                    item.setTotalCount(0);
                }
                if (item.getCompletedCount() == null) {
                    item.setCompletedCount(0);
                }
                if (item.getUncompletedCount() == null) {
                    item.setUncompletedCount(0);
                }
            });
        }

        log.info("查询到风险数据库一览表数量: {}", list != null ? list.size() : 0);
        return list;
    }

    /**
     * 查询风险审查情况分析(近12个月)
     *
     * @return 风险审查情况分析列表
     */
    @Override
    public List<RiskReviewAnalysisDTO> getRiskReviewAnalysis() {
        log.info("查询风险审查情况分析(近12个月)");
        List<RiskReviewAnalysisDTO> list = reminderMapper.selectRiskReviewAnalysis();

        // 处理空值
        if (list != null) {
            list.forEach(item -> {
                if (item.getRiskReviewCount() == null) {
                    item.setRiskReviewCount(0);
                }
                if (item.getOrgname() == null || item.getOrgname().trim().isEmpty()) {
                    item.setOrgname("未知部门");
                }
            });
        }

        log.info("查询到风险审查情况分析数量: {}", list != null ? list.size() : 0);
        return list;
    }

    /**
     * 查询风险事件数统计
     *
     * @return 风险事件数统计列表
     */
    @Override
    public List<RiskEventCountDTO> getRiskEventCount() {
        log.info("查询风险事件数统计");
        List<RiskEventCountDTO> list = reminderMapper.selectRiskEventCount();

        // 处理空值
        if (list != null) {
            list.forEach(item -> {
                if (item.getCategory1() == null) {
                    item.setCategory1(0);
                }
                if (item.getCategory2() == null) {
                    item.setCategory2(0);
                }
                if (item.getOccurredDepartment() == null || item.getOccurredDepartment().trim().isEmpty()) {
                    item.setOccurredDepartment("未知部门");
                }
            });
        }

        log.info("查询到风险事件数统计数量: {}", list != null ? list.size() : 0);
        return list;
    }

    /**
     * 查询风险措施状态统计
     *
     * @return 风险措施状态统计
     */
    @Override
    public RiskMeasureStatusDTO getRiskMeasureStatus() {
        log.info("查询风险措施状态统计");
        RiskMeasureStatusDTO result = reminderMapper.selectRiskMeasureStatus();

        // 处理空值
        if (result != null) {
            if (result.getNullRisklevelCount() == null) {
                result.setNullRisklevelCount(0);
            }
            if (result.getNotNullRisklevelCount() == null) {
                result.setNotNullRisklevelCount(0);
            }
        } else {
            // 如果查询结果为空，返回默认值
            result = new RiskMeasureStatusDTO();
            result.setNullRisklevelCount(0);
            result.setNotNullRisklevelCount(0);
        }

        log.info("查询到风险措施状态统计: 未完成={}, 完成={}",
                result.getNullRisklevelCount(), result.getNotNullRisklevelCount());
        return result;
    }

    /**
     * 查询风险完成情况统计
     *
     * @return 风险完成情况统计列表
     */
    @Override
    public List<RiskCompletionDTO> getRiskCompletion() {
        log.info("查询风险完成情况统计");
        List<RiskCompletionDTO> list = reminderMapper.selectRiskCompletion();

        // 处理空值
        if (list != null) {
            list.forEach(item -> {
                if (item.getGb() == null) {
                    item.setGb(0);
                }
                if (item.getCount() == null) {
                    item.setCount(0);
                }
            });
        }

        log.info("查询到风险完成情况统计数量: {}", list != null ? list.size() : 0);
        return list;
    }
}
