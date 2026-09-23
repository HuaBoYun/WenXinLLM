package com.global.treasurer.financialProductDefinition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.financialProductDefinition.dto.PrincipalCalculateRequest;
import com.global.treasurer.financialProductDefinition.dto.PrincipalCalculateResponse;
import com.global.treasurer.financialProductDefinition.dto.RepaymentPlan;
import com.global.treasurer.financialProductDefinition.entity.TblProductPrincipalRule;
import com.global.treasurer.financialProductDefinition.mapper.TblProductPrincipalRuleMapper;
import com.global.treasurer.financialProductDefinition.service.TblProductPrincipalRuleService;
import com.hbfk.util.SnowflakeIdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 产品本金规则管理Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class TblProductPrincipalRuleServiceImpl extends ServiceImpl<TblProductPrincipalRuleMapper, TblProductPrincipalRule>
        implements TblProductPrincipalRuleService {
    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public IPage<TblProductPrincipalRule> getPage(Integer pageNo, Integer pageSize, String ruleCode,
                                                   String ruleName, String productType, Integer isEnabled, Long orgId) {
        Page<TblProductPrincipalRule> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TblProductPrincipalRule> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(ruleCode)) {
            wrapper.like("RULE_CODE", ruleCode);
        }
        if (StringUtils.hasText(ruleName)) {
            wrapper.like("RULE_NAME", ruleName);
        }
        if (StringUtils.hasText(productType)) {
            wrapper.eq("PRODUCT_TYPE", productType);
        }
        if (isEnabled != null) {
            wrapper.eq("IS_ENABLED", isEnabled);
        }
        if (orgId != null) {
            wrapper.eq("ORG_ID", orgId);
        }
        wrapper.orderByDesc("CREATE_TIME");
        return this.page(page, wrapper);
    }

    @Override
    public TblProductPrincipalRule getDetail(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblProductPrincipalRule create(TblProductPrincipalRule entity, String createBy) {
        entity.setRuleId(snowflakeIdWorker.nextId());
        entity.setCreateBy(createBy);
        entity.setCreateTime(new Date());
        entity.setUpdateBy(createBy);
        entity.setUpdateTime(new Date());
        if (entity.getIsEnabled() == null) {
            entity.setIsEnabled(1);
        }
        this.save(entity);
        return entity;
    }

    @Override
    public boolean update(TblProductPrincipalRule entity, String updateBy) {
        entity.setUpdateBy(updateBy);
        return this.updateById(entity);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public boolean updateStatus(Long id, Integer isEnabled, String updateBy) {
        TblProductPrincipalRule entity = new TblProductPrincipalRule();
        entity.setRuleId(id);
        entity.setIsEnabled(isEnabled);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(new Date());
        return this.updateById(entity);
    }

    @Override
    public boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy) {
        return baseMapper.batchUpdateStatus(ids, isEnabled, updateBy) > 0;
    }

    @Override
    public List<TblProductPrincipalRule> getEnabledList(Long orgId) {
        QueryWrapper<TblProductPrincipalRule> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_ENABLED", 1);
        if (orgId != null) {
            wrapper.eq("ORG_ID", orgId);
        }
        return this.list(wrapper);
    }

    @Override
    public boolean checkCodeUnique(String ruleCode, Long excludeId) {
        return baseMapper.checkCodeUnique(ruleCode, excludeId) == 0;
    }

    @Override
    public List<TblProductPrincipalRule> getByProductType(String productType, Long orgId) {
        return baseMapper.selectByProductType(productType, orgId);
    }

    @Override
    public TblProductPrincipalRule copy(Long id, String newCode, String newName, String createBy) {
        TblProductPrincipalRule source = this.getById(id);
        if (source == null) {
            return null;
        }
        TblProductPrincipalRule target = new TblProductPrincipalRule();
        BeanUtils.copyProperties(source, target);
        target.setRuleId(snowflakeIdWorker.nextId());
        target.setRuleCode(newCode);
        target.setRuleName(newName);
        target.setCreateBy(createBy);
        target.setCreateTime(new Date());
        target.setUpdateBy(createBy);
        target.setUpdateTime(new Date());
        this.save(target);
        return target;
    }

    @Override
    public Map<String, Object> validateDelete(Long id) {
        Map<String, Object> result = new HashMap<>();
        int usage = baseMapper.countUsage(id);
        result.put("canDelete", usage == 0);
        result.put("usageCount", usage);
        if (usage > 0) {
            result.put("message", "该本金规则已被使用,无法删除");
        }
        return result;
    }

    @Override
    public Map<String, Object> getUsage(Long id) {
        Map<String, Object> result = new HashMap<>();
        int usage = baseMapper.countUsage(id);
        result.put("usageCount", usage);
        result.put("isUsed", usage > 0);
        return result;
    }

    @Override
    public PrincipalCalculateResponse calculate(PrincipalCalculateRequest request) {
        // 1. 查询规则
        TblProductPrincipalRule rule = this.getById(request.getRuleId());
        if (rule == null) {
            throw new RuntimeException("规则不存在");
        }

        // 2. 创建响应对象
        PrincipalCalculateResponse response = new PrincipalCalculateResponse();
        response.setRuleCode(rule.getRuleCode());
        response.setRuleName(rule.getRuleName());
        response.setInvestmentAmount(request.getInvestmentAmount());
        response.setInvestmentPeriod(request.getInvestmentPeriod());
        response.setPeriodUnit(request.getPeriodUnit());
        response.setRepaymentMethod(rule.getRepaymentMethod());
        response.setRepaymentMethodName(getRepaymentMethodName(rule.getRepaymentMethod()));
        response.setAnnualRate(request.getAnnualRate());
        response.setPrincipalGuaranteeRate(rule.getPrincipalGuaranteeRate() != null ?
            rule.getPrincipalGuaranteeRate() : BigDecimal.valueOf(100));

        // 3. 计算收益和保障金额
        BigDecimal investmentAmount = request.getInvestmentAmount();
        BigDecimal annualRate = request.getAnnualRate() != null ?
            request.getAnnualRate() : BigDecimal.valueOf(4.5); // 默认年化4.5%
        BigDecimal principalGuaranteeRate = rule.getPrincipalGuaranteeRate() != null ?
            rule.getPrincipalGuaranteeRate() : BigDecimal.valueOf(100);

        // 计算投资期限（年）
        double periodInYears = calculatePeriodInYears(request.getInvestmentPeriod(), request.getPeriodUnit());

        // 计算预期收益
        BigDecimal expectedIncome = investmentAmount
            .multiply(annualRate)
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(periodInYears))
            .setScale(2, RoundingMode.HALF_UP);
        response.setExpectedIncome(expectedIncome);

        // 计算总额
        BigDecimal totalAmount = investmentAmount.add(expectedIncome);
        response.setTotalAmount(totalAmount);

        // 计算保障金额
        BigDecimal guaranteedAmount = investmentAmount
            .multiply(principalGuaranteeRate)
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        response.setGuaranteedAmount(guaranteedAmount);

        // 4. 生成返还计划
        List<RepaymentPlan> plans = generateRepaymentPlans(
            rule, investmentAmount, annualRate, request.getInvestmentPeriod(), request.getPeriodUnit()
        );
        response.setRepaymentPlans(plans);

        return response;
    }

    /**
     * 计算投资期限（年）
     */
    private double calculatePeriodInYears(Integer period, String unit) {
        if (period == null) return 0;
        switch (unit) {
            case "day":
                return period / 365.0;
            case "month":
                return period / 12.0;
            case "year":
                return period.doubleValue();
            default:
                return period / 12.0;
        }
    }

    /**
     * 获取本金处理方式名称
     */
    private String getRepaymentMethodName(String method) {
        if (method == null) return "";
        switch (method) {
            case "MATURITY_LUMP_SUM":
                return "到期一次性返还";
            case "INSTALLMENT":
                return "分期返还";
            case "ANYTIME_REDEMPTION":
                return "随时赎回";
            case "PRINCIPAL_GUARANTEED":
                return "保本保息";
            default:
                return method;
        }
    }

    /**
     * 生成返还计划
     */
    private List<RepaymentPlan> generateRepaymentPlans(
            TblProductPrincipalRule rule, BigDecimal investmentAmount,
            BigDecimal annualRate, Integer period, String periodUnit) {
        List<RepaymentPlan> plans = new ArrayList<>();

        String repaymentMethod = rule.getRepaymentMethod();
        Calendar calendar = Calendar.getInstance();

        if ("MATURITY_LUMP_SUM".equals(repaymentMethod)) {
            // 到期一次性返还
            RepaymentPlan plan = new RepaymentPlan();
            plan.setPeriod(1);
            calendar.add(getCalendarField(periodUnit), period);
            plan.setRepaymentDate(calendar.getTime());
            plan.setPrincipal(investmentAmount);
            plan.setIncome(calculateIncome(investmentAmount, annualRate, period, periodUnit));
            plan.setTotal(plan.getPrincipal().add(plan.getIncome()));
            plan.setRemark("到期一次性还本付息");
            plans.add(plan);

        } else if ("INSTALLMENT".equals(repaymentMethod)) {
            // 分期返还
            int installmentCount = determineInstallmentCount(period, periodUnit);
            BigDecimal periodPrincipal = investmentAmount.divide(
                BigDecimal.valueOf(installmentCount), 2, RoundingMode.HALF_UP);
            BigDecimal periodIncome = calculateIncome(
                investmentAmount, annualRate, period, periodUnit
            ).divide(BigDecimal.valueOf(installmentCount), 2, RoundingMode.HALF_UP);

            for (int i = 1; i <= installmentCount; i++) {
                RepaymentPlan plan = new RepaymentPlan();
                plan.setPeriod(i);

                // 计算每期的日期
                int interval = period / installmentCount;
                calendar.add(getCalendarField(periodUnit), interval * i);
                plan.setRepaymentDate(calendar.getTime());

                plan.setPrincipal(periodPrincipal);
                plan.setIncome(periodIncome);
                plan.setTotal(periodPrincipal.add(periodIncome));

                // 最后一期调整金额
                if (i == installmentCount) {
                    BigDecimal totalPrincipal = periodPrincipal.multiply(BigDecimal.valueOf(installmentCount - 1));
                    BigDecimal lastPrincipal = investmentAmount.subtract(totalPrincipal);
                    plan.setPrincipal(lastPrincipal);
                    plan.setTotal(lastPrincipal.add(periodIncome));
                }

                plan.setRemark("第" + i + "期返还");
                plans.add(plan);
            }
        } else if ("PRINCIPAL_GUARANTEED".equals(repaymentMethod)) {
            // 保本保息
            RepaymentPlan plan = new RepaymentPlan();
            plan.setPeriod(1);
            calendar.add(getCalendarField(periodUnit), period);
            plan.setRepaymentDate(calendar.getTime());
            plan.setPrincipal(investmentAmount);
            plan.setIncome(calculateIncome(investmentAmount, annualRate, period, periodUnit));
            plan.setTotal(plan.getPrincipal().add(plan.getIncome()));
            plan.setRemark("保本保息到期返还");
            plans.add(plan);
        } else {
            // 随时赎回（提前赎回）
            RepaymentPlan plan = new RepaymentPlan();
            plan.setPeriod(1);
            calendar.add(getCalendarField(periodUnit), period);
            plan.setRepaymentDate(calendar.getTime());
            plan.setPrincipal(investmentAmount);
            plan.setIncome(calculateIncome(investmentAmount, annualRate, period, periodUnit));
            plan.setTotal(plan.getPrincipal().add(plan.getIncome()));
            plan.setRemark("随时赎回（以实际赎回日为准）");
            plans.add(plan);
        }

        return plans;
    }

    /**
     * 计算收益
     */
    private BigDecimal calculateIncome(BigDecimal principal, BigDecimal annualRate,
                                       Integer period, String periodUnit) {
        double periodInYears = calculatePeriodInYears(period, periodUnit);
        return principal
            .multiply(annualRate)
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(periodInYears))
            .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 确定分期数
     */
    private int determineInstallmentCount(Integer period, String periodUnit) {
        if ("month".equals(periodUnit)) {
            return period; // 按月分期
        } else if ("year".equals(periodUnit)) {
            return period * 12; // 按年分期，转换为月
        } else {
            return Math.max(1, period / 30); // 按天分期，约按月
        }
    }

    /**
     * 获取日历字段
     */
    private int getCalendarField(String periodUnit) {
        switch (periodUnit) {
            case "day":
                return Calendar.DAY_OF_YEAR;
            case "month":
                return Calendar.MONTH;
            case "year":
                return Calendar.YEAR;
            default:
                return Calendar.MONTH;
        }
    }
}
