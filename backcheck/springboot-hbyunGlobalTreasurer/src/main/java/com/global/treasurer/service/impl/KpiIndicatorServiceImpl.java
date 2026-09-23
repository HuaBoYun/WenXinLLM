package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.KpiIndicator;
import com.global.treasurer.mapper.KpiIndicatorMapper;
import com.global.treasurer.service.IKpiIndicatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * KPI指标Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
@Service
public class KpiIndicatorServiceImpl extends ServiceImpl<KpiIndicatorMapper, KpiIndicator>
        implements IKpiIndicatorService {
    private static final Logger log = LoggerFactory.getLogger(KpiIndicatorServiceImpl.class);

    @Override
    public IPage<KpiIndicator> selectPage(IPage<KpiIndicator> page, Map<String, Object> params) {
        log.info("========== KPI指标查询Service ==========");
        log.info("接收到的分页参数 - 当前页: {}, 每页大小: {}", page.getCurrent(), page.getSize());
        log.info("接收到的查询参数: {}", params);

        // 提取查询参数
        String kpiCode = params.get("kpiCode") != null ? params.get("kpiCode").toString() : null;
        String kpiName = params.get("kpiName") != null ? params.get("kpiName").toString() : null;
        String kpiCategory = params.get("kpiCategory") != null ? params.get("kpiCategory").toString() : null;
        String kpiType = params.get("kpiType") != null ? params.get("kpiType").toString() : null;
        String kpiStatus = params.get("kpiStatus") != null ? params.get("kpiStatus").toString() : null;
        Long orgId = params.get("orgId") != null ? Long.parseLong(params.get("orgId").toString()) : null;

        // 使用 PageHelper 进行分页（项目已禁用 MyBatis-Plus 分页插件）
        PageHelper.startPage((int)page.getCurrent(), (int)page.getSize());

        // 调用自定义的条件查询方法
        List<KpiIndicator> list = baseMapper.selectByCondition(
                kpiCode, kpiName, kpiCategory, kpiType, kpiStatus, orgId);

        log.info("查询结果记录数: {}", list.size());

        // 使用 PageInfo 获取分页信息
        PageInfo<KpiIndicator> pageInfo = new PageInfo<>(list);

        // 构建返回的 IPage 对象
        Page<KpiIndicator> resultPage = new Page<>(page.getCurrent(), page.getSize(), pageInfo.getTotal());
        resultPage.setRecords(list);
        resultPage.setPages(pageInfo.getPages());

        log.info("返回的分页信息 - 当前页: {}, 每页大小: {}, 总记录数: {}, 总页数: {}",
                resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal(), resultPage.getPages());

        return resultPage;
    }

    @Override
    public boolean calculateKpi(Long kpiId) {
        try {
            KpiIndicator kpi = this.getById(kpiId);
            if (kpi == null) {
                log.error("KPI指标不存在，kpiId: {}", kpiId);
                return false;
            }

            log.info("开始计算KPI指标，kpiId: {}, kpiName: {}", kpiId, kpi.getKpiName());

            // 模拟计算KPI值
            BigDecimal calculatedValue = BigDecimal.valueOf(85.5);

            // 更新当前值
            kpi.setCurrentValue(calculatedValue);
            kpi.setCalculationDate(LocalDate.now());
            kpi.setUpdateTime(LocalDateTime.now());

            // 根据当前值更新KPI状态
            if (kpi.getCriticalThreshold() != null && calculatedValue.compareTo(kpi.getCriticalThreshold()) <= 0) {
                kpi.setKpiStatus("CRITICAL");
                kpi.setTrend("DOWN");
            } else if (kpi.getWarningThreshold() != null && calculatedValue.compareTo(kpi.getWarningThreshold()) <= 0) {
                kpi.setKpiStatus("WARNING");
                kpi.setTrend("DOWN");
            } else {
                kpi.setKpiStatus("NORMAL");
                kpi.setTrend("STABLE");
            }

            boolean updated = this.updateById(kpi);
            if (updated) {
                log.info("KPI指标计算成功，kpiId: {}, currentValue: {}", kpiId, calculatedValue);
            }

            return updated;
        } catch (Exception e) {
            log.error("计算KPI指标失败，kpiId: {}", kpiId, e);
            return false;
        }
    }

    @Override
    public boolean updateCurrentValue(Long kpiId, BigDecimal currentValue) {
        try {
            KpiIndicator kpi = this.getById(kpiId);
            if (kpi == null) {
                log.error("KPI指标不存在，kpiId: {}", kpiId);
                return false;
            }

            kpi.setCurrentValue(currentValue);
            kpi.setCalculationDate(LocalDate.now());
            kpi.setUpdateTime(LocalDateTime.now());

            // 根据当前值更新KPI状态
            if (kpi.getCriticalThreshold() != null && currentValue.compareTo(kpi.getCriticalThreshold()) <= 0) {
                kpi.setKpiStatus("CRITICAL");
                kpi.setTrend("DOWN");
            } else if (kpi.getWarningThreshold() != null && currentValue.compareTo(kpi.getWarningThreshold()) <= 0) {
                kpi.setKpiStatus("WARNING");
                kpi.setTrend("DOWN");
            } else {
                kpi.setKpiStatus("NORMAL");
                // 判断趋势
                if (kpi.getTargetValue() != null) {
                    if (currentValue.compareTo(kpi.getTargetValue()) >= 0) {
                        kpi.setTrend("UP");
                    } else {
                        kpi.setTrend("STABLE");
                    }
                } else {
                    kpi.setTrend("STABLE");
                }
            }

            boolean updated = this.updateById(kpi);
            if (updated) {
                log.info("更新KPI当前值成功，kpiId: {}, currentValue: {}", kpiId, currentValue);
            }

            return updated;
        } catch (Exception e) {
            log.error("更新KPI当前值失败，kpiId: {}", kpiId, e);
            return false;
        }
    }

    @Override
    public boolean updateKpiStatus(Long kpiId, String kpiStatus) {
        try {
            KpiIndicator kpi = this.getById(kpiId);
            if (kpi == null) {
                log.error("KPI指标不存在，kpiId: {}", kpiId);
                return false;
            }

            kpi.setKpiStatus(kpiStatus);
            kpi.setUpdateTime(LocalDateTime.now());

            boolean updated = this.updateById(kpi);
            if (updated) {
                log.info("更新KPI状态成功，kpiId: {}, kpiStatus: {}", kpiId, kpiStatus);
            }

            return updated;
        } catch (Exception e) {
            log.error("更新KPI状态失败，kpiId: {}, kpiStatus: {}", kpiId, kpiStatus, e);
            return false;
        }
    }

    @Override
    public Map<String, Object> batchCalculateKpi(java.util.List<Long> kpiIds) {
        try {
            log.info("开始批量计算KPI指标，kpiIds: {}", kpiIds);

            if (kpiIds == null || kpiIds.isEmpty()) {
                Map<String, Object> result = new java.util.HashMap<>();
                result.put("success", false);
                result.put("message", "KPI ID列表不能为空");
                return result;
            }

            int totalCount = kpiIds.size();
            int successCount = 0;
            int failCount = 0;
            List<Map<String, Object>> detailList = new java.util.ArrayList<>();

            for (Long kpiId : kpiIds) {
                try {
                    KpiIndicator kpi = this.getById(kpiId);
                    if (kpi == null) {
                        log.warn("KPI指标不存在，kpiId: {}", kpiId);
                        failCount++;

                        Map<String, Object> detail = new java.util.HashMap<>();
                        detail.put("kpiId", kpiId);
                        detail.put("success", false);
                        detail.put("message", "KPI指标不存在");
                        detailList.add(detail);
                        continue;
                    }

                    // 模拟计算KPI值
                    BigDecimal calculatedValue = BigDecimal.valueOf(80 + Math.random() * 20);

                    // 更新当前值
                    kpi.setCurrentValue(calculatedValue);
                    kpi.setCalculationDate(LocalDate.now());
                    kpi.setUpdateTime(LocalDateTime.now());

                    // 根据当前值更新KPI状态
                    if (kpi.getCriticalThreshold() != null && calculatedValue.compareTo(kpi.getCriticalThreshold()) <= 0) {
                        kpi.setKpiStatus("CRITICAL");
                        kpi.setTrend("DOWN");
                    } else if (kpi.getWarningThreshold() != null && calculatedValue.compareTo(kpi.getWarningThreshold()) <= 0) {
                        kpi.setKpiStatus("WARNING");
                        kpi.setTrend("DOWN");
                    } else {
                        kpi.setKpiStatus("NORMAL");
                        kpi.setTrend("STABLE");
                    }

                    boolean updated = this.updateById(kpi);
                    if (updated) {
                        successCount++;
                        log.info("KPI指标计算成功，kpiId: {}, currentValue: {}", kpiId, calculatedValue);

                        Map<String, Object> detail = new java.util.HashMap<>();
                        detail.put("kpiId", kpiId);
                        detail.put("kpiName", kpi.getKpiName());
                        detail.put("success", true);
                        detail.put("currentValue", calculatedValue);
                        detail.put("kpiStatus", kpi.getKpiStatus());
                        detailList.add(detail);
                    } else {
                        failCount++;
                        Map<String, Object> detail = new java.util.HashMap<>();
                        detail.put("kpiId", kpiId);
                        detail.put("success", false);
                        detail.put("message", "更新KPI失败");
                        detailList.add(detail);
                    }
                } catch (Exception e) {
                    log.error("计算KPI指标失败，kpiId: {}", kpiId, e);
                    failCount++;

                    Map<String, Object> detail = new java.util.HashMap<>();
                    detail.put("kpiId", kpiId);
                    detail.put("success", false);
                    detail.put("message", "计算失败: " + e.getMessage());
                    detailList.add(detail);
                }
            }

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("success", true);
            result.put("totalCount", totalCount);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("details", detailList);
            result.put("message", String.format("批量计算完成，成功: %d, 失败: %d", successCount, failCount));

            log.info("批量计算KPI指标完成，总数: {}, 成功: {}, 失败: {}", totalCount, successCount, failCount);

            return result;

        } catch (Exception e) {
            log.error("批量计算KPI指标失败", e);
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("success", false);
            result.put("message", "批量计算失败: " + e.getMessage());
            return result;
        }
    }
}
