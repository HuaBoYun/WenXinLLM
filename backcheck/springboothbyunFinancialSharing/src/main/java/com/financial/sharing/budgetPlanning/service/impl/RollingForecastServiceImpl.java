package com.financial.sharing.budgetPlanning.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.budgetPlanning.dto.RollingForecastQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblRollingForecast;
import com.financial.sharing.budgetPlanning.entity.TblRollingForecastData;
import com.financial.sharing.budgetPlanning.mapper.RollingForecastDataMapper;
import com.financial.sharing.budgetPlanning.mapper.RollingForecastMapper;
import com.financial.sharing.budgetPlanning.service.RollingForecastService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.financial.sharing.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 滚动预测Service实现类
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Service
public class RollingForecastServiceImpl implements RollingForecastService {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(RollingForecastServiceImpl.class);

    @Autowired
    private RollingForecastMapper forecastMapper;

    @Autowired
    private RollingForecastDataMapper forecastDataMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createForecast(TblRollingForecast forecast) {
        String userId = UserUtils.requireUserId();
        String orgId = UserUtils.requireOrgId();
        Date now = new Date();

        // 生成预测任务ID和编号
        forecast.setForecastId(UUIDUtil.getUUID());
        forecast.setForecastNo(forecastMapper.generateForecastNo());
        forecast.setStatus("DRAFT");
        forecast.setOrgId(orgId);
        forecast.setCreateUser(userId);
        forecast.setCreateTime(now);
        forecast.setUpdateUser(userId);
        forecast.setUpdateTime(now);

        // 插入预测任务
        forecastMapper.insert(forecast);

        // 插入预测数据
        if (forecast.getDataList() != null && !forecast.getDataList().isEmpty()) {
            for (TblRollingForecastData data : forecast.getDataList()) {
                data.setDataId(UUIDUtil.getUUID());
                data.setForecastId(forecast.getForecastId());
                data.setOrgId(orgId);
                data.setCreateUser(userId);
                data.setCreateTime(now);
                data.setUpdateUser(userId);
                data.setUpdateTime(now);

                // 计算差异值和差异率
                calculateVariance(data);
            }
            forecastDataMapper.batchInsert(forecast.getDataList());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateForecast(TblRollingForecast forecast) {
        String userId = UserUtils.requireUserId();
        Date now = new Date();

        // 检查状态
        TblRollingForecast existingForecast = forecastMapper.selectById(forecast.getForecastId());
        if (existingForecast == null) {
            throw new RuntimeException("预测任务不存在");
        }
        if (!"DRAFT".equals(existingForecast.getStatus())) {
            throw new RuntimeException("只能修改草稿状态的预测任务");
        }

        // 更新预测任务
        forecast.setUpdateUser(userId);
        forecast.setUpdateTime(now);
        forecastMapper.updateById(forecast);

        // 删除旧的预测数据
        forecastDataMapper.deleteByForecastId(forecast.getForecastId());

        // 插入新的预测数据
        if (forecast.getDataList() != null && !forecast.getDataList().isEmpty()) {
            for (TblRollingForecastData data : forecast.getDataList()) {
                if (data.getDataId() == null || data.getDataId().isEmpty()) {
                    data.setDataId(UUIDUtil.getUUID());
                }
                data.setForecastId(forecast.getForecastId());
                data.setOrgId(existingForecast.getOrgId());
                data.setCreateUser(userId);
                data.setCreateTime(now);
                data.setUpdateUser(userId);
                data.setUpdateTime(now);

                // 计算差异值和差异率
                calculateVariance(data);
            }
            forecastDataMapper.batchInsert(forecast.getDataList());
        }
    }

    @Override
    public PageInfo<TblRollingForecast> getForecastList(RollingForecastQueryParam param) {
        String orgId = UserUtils.requireOrgId();
        param.setOrgId(orgId);

        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblRollingForecast> list = forecastMapper.selectForecastList(param);
        return new PageInfo<>(list);
    }

    @Override
    public TblRollingForecast getForecastById(String forecastId) {
        return forecastMapper.selectById(forecastId);
    }

    @Override
    public TblRollingForecast getForecastWithData(String forecastId) {
        return forecastMapper.selectForecastWithData(forecastId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteForecast(String forecastId) {
        // 检查状态
        TblRollingForecast forecast = forecastMapper.selectById(forecastId);
        if (forecast == null) {
            throw new RuntimeException("预测任务不存在");
        }
        if (!"DRAFT".equals(forecast.getStatus())) {
            throw new RuntimeException("只能删除草稿状态的预测任务");
        }

        // 删除预测数据
        forecastDataMapper.deleteByForecastId(forecastId);

        // 删除预测任务
        forecastMapper.deleteById(forecastId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteForecast(List<String> forecastIds) {
        for (String forecastId : forecastIds) {
            deleteForecast(forecastId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForecast(String forecastId) {
        String userId = UserUtils.requireUserId();
        Date now = new Date();

        // 检查状态
        TblRollingForecast forecast = forecastMapper.selectById(forecastId);
        if (forecast == null) {
            throw new RuntimeException("预测任务不存在");
        }
        if (!"DRAFT".equals(forecast.getStatus())) {
            throw new RuntimeException("只能提交草稿状态的预测任务");
        }

        // 更新状态
        forecast.setStatus("SUBMITTED");
        forecast.setSubmitUser(userId);
        forecast.setSubmitTime(now);
        forecast.setUpdateUser(userId);
        forecast.setUpdateTime(now);
        forecastMapper.updateById(forecast);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveForecast(String forecastId, Boolean approved, String opinion) {
        String userId = UserUtils.requireUserId();
        Date now = new Date();

        // 检查状态
        TblRollingForecast forecast = forecastMapper.selectById(forecastId);
        if (forecast == null) {
            throw new RuntimeException("预测任务不存在");
        }
        if (!"SUBMITTED".equals(forecast.getStatus())) {
            throw new RuntimeException("只能审批已提交状态的预测任务");
        }

        // 更新状态
        forecast.setStatus(approved ? "APPROVED" : "REJECTED");
        forecast.setApproveUser(userId);
        forecast.setApproveTime(now);
        forecast.setApproveOpinion(opinion);
        forecast.setUpdateUser(userId);
        forecast.setUpdateTime(now);
        forecastMapper.updateById(forecast);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdrawForecast(String forecastId) {
        String userId = UserUtils.requireUserId();
        Date now = new Date();

        // 检查状态
        TblRollingForecast forecast = forecastMapper.selectById(forecastId);
        if (forecast == null) {
            throw new RuntimeException("预测任务不存在");
        }
        if (!"SUBMITTED".equals(forecast.getStatus())) {
            throw new RuntimeException("只能撤销已提交状态的预测任务");
        }

        // 更新状态
        forecast.setStatus("DRAFT");
        forecast.setSubmitUser(null);
        forecast.setSubmitTime(null);
        forecast.setUpdateUser(userId);
        forecast.setUpdateTime(now);
        forecastMapper.updateById(forecast);
    }

    @Override
    public Map<String, Object> getForecastStatistics() {
        String orgId = UserUtils.requireOrgId();
        Map<String, Object> raw = forecastMapper.selectForecastStatistics(orgId);
        log.info("[RollingForecast.statistics] orgId={}, raw={}", orgId, raw);
        // 关键: 达梦驱动可能把别名转成大写, 这里做大小写归一化, 同名取非零值优先
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("totalCount", 0);
        result.put("draftCount", 0);
        result.put("submittedCount", 0);
        result.put("approvedCount", 0);
        result.put("rejectedCount", 0);
        if (raw != null) {
            for (Map.Entry<String, Object> entry : raw.entrySet()) {
                String camelKey = toCamelLower(entry.getKey());
                Object val = entry.getValue();
                if (val == null) {
                    continue;
                }
                Object existed = result.get(camelKey);
                if (existed == null || isZeroLike(existed)) {
                    result.put(camelKey, val);
                }
            }
        }
        return result;
    }

    /** "TOTALCOUNT" / "totalCount" / "totalcount" 全部归一化成 "totalCount" 等已知 key */
    private static String toCamelLower(String key) {
        if (key == null) return "";
        String low = key.toLowerCase();
        switch (low) {
            case "totalcount":     return "totalCount";
            case "draftcount":     return "draftCount";
            case "submittedcount": return "submittedCount";
            case "approvedcount":  return "approvedCount";
            case "rejectedcount":  return "rejectedCount";
            default: return key;
        }
    }

    private static boolean isZeroLike(Object v) {
        if (v == null) return true;
        if (v instanceof Number) return ((Number) v).longValue() == 0L;
        String s = v.toString();
        return s.isEmpty() || "0".equals(s);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateForecastData(String forecastId) {
        // 此方法用于基于基准数据生成预测数据
        // 实际实现需要根据baseType从对应的表中查询数据
        // 这里提供一个简化的实现框架
        TblRollingForecast forecast = forecastMapper.selectById(forecastId);
        if (forecast == null) {
            throw new RuntimeException("预测任务不存在");
        }

        // TODO: 根据baseType从对应的表中查询基准数据
        // 例如：HISTORY-从历史数据表查询，BUDGET-从预算数据表查询，ACTUAL-从实际数据表查询
        // 然后生成预测数据并插入到TBL_ROLLING_FORECAST_DATA表中
    }

    /**
     * 计算差异值和差异率
     *
     * @param data 预测数据
     */
    private void calculateVariance(TblRollingForecastData data) {
        BigDecimal forecastValue = data.getForecastValue() != null ? data.getForecastValue() : BigDecimal.ZERO;
        BigDecimal baseValue = data.getBaseValue() != null ? data.getBaseValue() : BigDecimal.ZERO;

        // 差异值 = 预测值 - 基准值
        BigDecimal varianceValue = forecastValue.subtract(baseValue);
        data.setVarianceValue(varianceValue);

        // 差异率 = (预测值 - 基准值) / 基准值 * 100%
        if (baseValue.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal varianceRate = varianceValue.divide(baseValue, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            data.setVarianceRate(varianceRate);
        } else {
            data.setVarianceRate(BigDecimal.ZERO);
        }
    }
}
