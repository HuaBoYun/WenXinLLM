package com.financial.sharing.dataCollection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.financial.sharing.dataCollection.dto.MappingRuleQueryParam;
import com.financial.sharing.dataCollection.entity.TblMappingRule;
import com.financial.sharing.dataCollection.mapper.MappingRuleMapper;
import com.financial.sharing.dataCollection.service.MappingRuleService;
import com.financial.sharing.util.MyJsonBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 映射规则Service实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class MappingRuleServiceImpl implements MappingRuleService {

    @Autowired
    private MappingRuleMapper mappingRuleMapper;

    @Override
    public MyJsonBean queryPage(MappingRuleQueryParam param, String orgId) {
        try {
            // 使用 PageHelper 分页 (达梦数据库下 MyBatis-Plus 分页插件的 COUNT 子查询会保留 ORDER BY,
            // 触发达梦语法错误, 与项目其他模块保持一致, 改用 PageHelper)
            LambdaQueryWrapper<TblMappingRule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblMappingRule::getOrgId, orgId);

            if (StringUtils.isNotBlank(param.getRuleCode())) {
                wrapper.like(TblMappingRule::getRuleCode, param.getRuleCode());
            }
            if (StringUtils.isNotBlank(param.getRuleName())) {
                wrapper.like(TblMappingRule::getRuleName, param.getRuleName());
            }
            if (StringUtils.isNotBlank(param.getSourceId())) {
                wrapper.eq(TblMappingRule::getSourceId, param.getSourceId());
            }
            if (StringUtils.isNotBlank(param.getTargetTable())) {
                wrapper.like(TblMappingRule::getTargetTable, param.getTargetTable());
            }
            if (StringUtils.isNotBlank(param.getIsEnabled())) {
                wrapper.eq(TblMappingRule::getIsEnabled, param.getIsEnabled());
            }
            if (StringUtils.isNotBlank(param.getIsIncremental())) {
                wrapper.eq(TblMappingRule::getIsIncremental, param.getIsIncremental());
            }
            if (StringUtils.isNotBlank(param.getConflictStrategy())) {
                wrapper.eq(TblMappingRule::getConflictStrategy, param.getConflictStrategy());
            }

            // 按排序号和创建时间排序
            wrapper.orderByAsc(TblMappingRule::getSortNo);
            wrapper.orderByDesc(TblMappingRule::getCreateTime);

            int pageNumber = param.getPageNumber() != null ? param.getPageNumber() : 1;
            int pageSize = param.getPageSize() != null ? param.getPageSize() : 10;

            PageHelper.startPage(pageNumber, pageSize);
            List<TblMappingRule> list = mappingRuleMapper.selectList(wrapper);
            PageInfo<TblMappingRule> pageInfo = new PageInfo<>(list);

            // 前端期望读 data.records / data.total
            Map<String, Object> result = new HashMap<>();
            result.put("records", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("pageNumber", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());
            result.put("pages", pageInfo.getPages());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("分页查询映射规则失败", e);
            return MyJsonBean.errorData("分页查询映射规则失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryById(String ruleId, String orgId) {
        try {
            LambdaQueryWrapper<TblMappingRule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblMappingRule::getRuleId, ruleId);
            wrapper.eq(TblMappingRule::getOrgId, orgId);

            TblMappingRule mappingRule = mappingRuleMapper.selectOne(wrapper);
            if (mappingRule == null) {
                return MyJsonBean.errorData("映射规则不存在");
            }

            return MyJsonBean.successData(mappingRule);
        } catch (Exception e) {
            log.error("查询映射规则失败", e);
            return MyJsonBean.errorData("查询映射规则失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveMappingRule(TblMappingRule mappingRule, String orgId, String userId) {
        try {
            mappingRule.setOrgId(orgId);

            if (StringUtils.isBlank(mappingRule.getRuleId())) {
                // 新增
                // 检查规则编码是否重复
                LambdaQueryWrapper<TblMappingRule> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(TblMappingRule::getSourceId, mappingRule.getSourceId());
                wrapper.eq(TblMappingRule::getRuleCode, mappingRule.getRuleCode());
                wrapper.eq(TblMappingRule::getOrgId, orgId);

                Integer count = mappingRuleMapper.selectCount(wrapper);
                if (count > 0) {
                    return MyJsonBean.errorData("规则编码已存在");
                }

                mappingRule.setCreateUser(userId);
                mappingRule.setCreateTime(new Date());
                mappingRule.setUpdateUser(userId);
                mappingRule.setUpdateTime(new Date());

                int result = mappingRuleMapper.insert(mappingRule);
                if (result > 0) {
                    return MyJsonBean.successData("新增成功");
                } else {
                    return MyJsonBean.errorData("新增失败");
                }
            } else {
                // 更新
                // 检查规则编码是否重复（排除自身）
                LambdaQueryWrapper<TblMappingRule> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(TblMappingRule::getSourceId, mappingRule.getSourceId());
                wrapper.eq(TblMappingRule::getRuleCode, mappingRule.getRuleCode());
                wrapper.eq(TblMappingRule::getOrgId, orgId);
                wrapper.ne(TblMappingRule::getRuleId, mappingRule.getRuleId());

                Integer count = mappingRuleMapper.selectCount(wrapper);
                if (count > 0) {
                    return MyJsonBean.errorData("规则编码已存在");
                }

                mappingRule.setUpdateUser(userId);
                mappingRule.setUpdateTime(new Date());

                int result = mappingRuleMapper.updateById(mappingRule);
                if (result > 0) {
                    return MyJsonBean.successData("更新成功");
                } else {
                    return MyJsonBean.errorData("更新失败");
                }
            }
        } catch (Exception e) {
            log.error("保存映射规则失败", e);
            return MyJsonBean.errorData("保存映射规则失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteMappingRule(String ruleId, String orgId) {
        try {
            LambdaQueryWrapper<TblMappingRule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblMappingRule::getRuleId, ruleId);
            wrapper.eq(TblMappingRule::getOrgId, orgId);

            int result = mappingRuleMapper.delete(wrapper);
            if (result > 0) {
                return MyJsonBean.successData("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除映射规则失败", e);
            return MyJsonBean.errorData("删除映射规则失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean toggleEnabled(String ruleId, String isEnabled, String orgId, String userId) {
        try {
            TblMappingRule mappingRule = new TblMappingRule();
            mappingRule.setIsEnabled(isEnabled);
            mappingRule.setUpdateUser(userId);
            mappingRule.setUpdateTime(new Date());

            LambdaUpdateWrapper<TblMappingRule> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(TblMappingRule::getRuleId, ruleId);
            wrapper.eq(TblMappingRule::getOrgId, orgId);

            int result = mappingRuleMapper.update(mappingRule, wrapper);
            if (result > 0) {
                String action = "Y".equals(isEnabled) ? "启用" : "禁用";
                return MyJsonBean.successData(action + "成功");
            } else {
                return MyJsonBean.errorData("操作失败");
            }
        } catch (Exception e) {
            log.error("启用/禁用映射规则失败", e);
            return MyJsonBean.errorData("启用/禁用映射规则失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean copyMappingRule(String ruleId, String orgId, String userId) {
        try {
            // 查询原规则
            LambdaQueryWrapper<TblMappingRule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblMappingRule::getRuleId, ruleId);
            wrapper.eq(TblMappingRule::getOrgId, orgId);

            TblMappingRule originalRule = mappingRuleMapper.selectOne(wrapper);
            if (originalRule == null) {
                return MyJsonBean.errorData("原规则不存在");
            }

            // 创建新规则
            TblMappingRule newRule = new TblMappingRule();
            newRule.setSourceId(originalRule.getSourceId());
            newRule.setRuleCode(originalRule.getRuleCode() + "_COPY");
            newRule.setRuleName(originalRule.getRuleName() + "_副本");
            newRule.setTargetTable(originalRule.getTargetTable());
            newRule.setSourceQuery(originalRule.getSourceQuery());
            newRule.setFieldMappings(originalRule.getFieldMappings());
            newRule.setFilterCondition(originalRule.getFilterCondition());
            newRule.setTransformRules(originalRule.getTransformRules());
            newRule.setValidationRules(originalRule.getValidationRules());
            newRule.setConflictStrategy(originalRule.getConflictStrategy());
            newRule.setBatchSize(originalRule.getBatchSize());
            newRule.setIsIncremental(originalRule.getIsIncremental());
            newRule.setIncrementalField(originalRule.getIncrementalField());
            newRule.setIsEnabled("N"); // 默认禁用
            newRule.setSortNo(originalRule.getSortNo() + 1);
            newRule.setOrgId(orgId);
            newRule.setCreateUser(userId);
            newRule.setCreateTime(new Date());
            newRule.setUpdateUser(userId);
            newRule.setUpdateTime(new Date());

            int result = mappingRuleMapper.insert(newRule);
            if (result > 0) {
                return MyJsonBean.successData("复制成功", newRule);
            } else {
                return MyJsonBean.errorData("复制失败");
            }
        } catch (Exception e) {
            log.error("复制映射规则失败", e);
            return MyJsonBean.errorData("复制映射规则失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean updateSortNo(String ruleId, Integer sortNo, String orgId, String userId) {
        try {
            TblMappingRule mappingRule = new TblMappingRule();
            mappingRule.setSortNo(sortNo);
            mappingRule.setUpdateUser(userId);
            mappingRule.setUpdateTime(new Date());

            LambdaUpdateWrapper<TblMappingRule> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(TblMappingRule::getRuleId, ruleId);
            wrapper.eq(TblMappingRule::getOrgId, orgId);

            int result = mappingRuleMapper.update(mappingRule, wrapper);
            if (result > 0) {
                return MyJsonBean.successData("更新排序号成功");
            } else {
                return MyJsonBean.errorData("更新排序号失败");
            }
        } catch (Exception e) {
            log.error("更新排序号失败", e);
            return MyJsonBean.errorData("更新排序号失败：" + e.getMessage());
        }
    }
}

