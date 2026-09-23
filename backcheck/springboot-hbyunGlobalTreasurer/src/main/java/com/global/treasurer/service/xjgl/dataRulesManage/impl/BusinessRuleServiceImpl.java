package com.global.treasurer.service.xjgl.dataRulesManage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGtBusinessRule;
import com.global.treasurer.mapper.TblGtBusinessRuleMapper;
import com.global.treasurer.service.xjgl.dataRulesManage.BusinessRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务规则管理Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Service("tblGtBusinessRuleServiceImpl")
public class BusinessRuleServiceImpl extends ServiceImpl<TblGtBusinessRuleMapper, TblGtBusinessRule> implements BusinessRuleService {
    @Override
    public PageInfo<TblGtBusinessRule> getBusinessRuleList(Map<String, Object> params, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        QueryWrapper<TblGtBusinessRule> queryWrapper = new QueryWrapper<>();

        if (params.get("ruleCode") != null && !"".equals(params.get("ruleCode"))) {
            queryWrapper.like("RULE_CODE", params.get("ruleCode"));
        }
        if (params.get("ruleName") != null && !"".equals(params.get("ruleName"))) {
            queryWrapper.like("RULE_NAME", params.get("ruleName"));
        }
        if (params.get("ruleType") != null && !"".equals(params.get("ruleType"))) {
            queryWrapper.eq("RULE_TYPE", params.get("ruleType"));
        }
        if (params.get("moduleCode") != null && !"".equals(params.get("moduleCode"))) {
            queryWrapper.eq("MODULE_CODE", params.get("moduleCode"));
        }
        if (params.get("ruleStatus") != null && !"".equals(params.get("ruleStatus"))) {
            queryWrapper.eq("RULE_STATUS", params.get("ruleStatus"));
        }

        // 排除软删除数据（STATUS=0），STATUS为null或1的都显示
        queryWrapper.and(w -> w.isNull("STATUS").or().ne("STATUS", 0));
        queryWrapper.orderByDesc("CREATE_TIME");

        List<TblGtBusinessRule> list = this.baseMapper.selectList(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createBusinessRule(TblGtBusinessRule businessRule) {
        businessRule.setCreateTime(new Date());
        businessRule.setUpdateTime(new Date());
        businessRule.setStatus(1);
        businessRule.setIsActive(1);
        return this.baseMapper.insert(businessRule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBusinessRule(TblGtBusinessRule businessRule) {
        businessRule.setUpdateTime(new Date());
        return this.baseMapper.updateById(businessRule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBusinessRule(Long ruleId) {
        TblGtBusinessRule businessRule = new TblGtBusinessRule();
        businessRule.setRuleId(ruleId);
        businessRule.setStatus(0);
        businessRule.setUpdateTime(new Date());
        return this.baseMapper.updateById(businessRule);
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 清除可能残留的 PageHelper ThreadLocal，防止被上一个请求的分页参数污染
        PageHelper.clearPage();

        QueryWrapper<TblGtBusinessRule> wrapper = new QueryWrapper<>();
        wrapper.and(w -> w.isNull("STATUS").or().ne("STATUS", 0));

        List<TblGtBusinessRule> allRules = this.baseMapper.selectList(wrapper);
        statistics.put("totalRules", allRules.size());

        long enabledCount = allRules.stream().filter(r -> Integer.valueOf(1).equals(r.getIsActive())).count();
        statistics.put("enabledRules", enabledCount);

        statistics.put("lastUpdateTime", new Date().getMinutes() + "分钟前");

        return statistics;
    }
}
