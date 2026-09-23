package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.entity.TblAccountingRuleCenter;
import com.financial.sharing.entity.TblAccountingRuleCategory;
import com.financial.sharing.entity.TblAccountingRuleLibrary;
import com.financial.sharing.entity.TblAccountingRuleVersion;
import com.financial.sharing.mapper.TblAccountingRuleCategoryMapper;
import com.financial.sharing.mapper.TblAccountingRuleCenterMapper;
import com.financial.sharing.mapper.TblAccountingRuleLibraryMapper;
import com.financial.sharing.mapper.TblAccountingRuleVersionMapper;
import com.financial.sharing.service.TblAccountingRuleCenterService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.TblAccountingRuleCenterQueryParam;
import com.financial.sharing.vo.param.TblAccountingRuleCenterSaveParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 会计规则中心服务实现类
 *
 * @author system
 * @since 2025-01-30
 */
@Slf4j
@Service
public class TblAccountingRuleCenterServiceImpl implements TblAccountingRuleCenterService {

    @Autowired
    private TblAccountingRuleCenterMapper ruleCenterMapper;

    @Autowired
    private TblAccountingRuleCategoryMapper categoryMapper;

    @Autowired
    private TblAccountingRuleLibraryMapper libraryMapper;

    @Autowired
    private TblAccountingRuleVersionMapper versionMapper;

    @Autowired
    private DateBaseConfig dateBaseConfig;

    // 规则类型名称映射
    private static final Map<String, String> RULE_TYPE_NAME_MAP = new HashMap<>();

    static {
        RULE_TYPE_NAME_MAP.put("ACCOUNTING", "会计核算");
        RULE_TYPE_NAME_MAP.put("BUDGET", "预算管理");
        RULE_TYPE_NAME_MAP.put("COST", "成本管理");
        RULE_TYPE_NAME_MAP.put("REVENUE", "收入管理");
        RULE_TYPE_NAME_MAP.put("ASSET", "资产管理");
    }

    @Override
    public MyJsonBean<PageResult<TblAccountingRuleCenter>> getList(TblAccountingRuleCenterQueryParam param) {
        try {
            log.info("查询会计规则列表，参数：{}", param);

            Page<TblAccountingRuleCenter> page = new Page<>(param.getPageNo(), param.getPageSize());
            LambdaQueryWrapper<TblAccountingRuleCenter> wrapper = new LambdaQueryWrapper<>();

            // 添加查询条件
            if (StringUtils.hasText(param.getRuleName())) {
                wrapper.like(TblAccountingRuleCenter::getRuleName, param.getRuleName());
            }
            if (StringUtils.hasText(param.getRuleType())) {
                wrapper.eq(TblAccountingRuleCenter::getRuleType, param.getRuleType());
            }
            if (param.getIsEnabled() != null) {
                wrapper.eq(TblAccountingRuleCenter::getIsEnabled, param.getIsEnabled());
            }

            wrapper.orderByDesc(TblAccountingRuleCenter::getCreateTime);

            IPage<TblAccountingRuleCenter> result = ruleCenterMapper.selectPage(page, wrapper);

            PageResult<TblAccountingRuleCenter> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage((int) result.getCurrent());
            pageResult.setPageSize((int) result.getSize());
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setTlist(result.getRecords());

            return MyJsonBean.successData(pageResult);
        } catch (Exception e) {
            log.error("查询会计规则列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getById(String ruleId) {
        try {
            if (!StringUtils.hasText(ruleId)) {
                return MyJsonBean.errorData("规则ID不能为空");
            }

            log.info("查询会计规则详情，ruleId={}", ruleId);

            TblAccountingRuleCenter rule = ruleCenterMapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }

            return MyJsonBean.successData(rule);
        } catch (Exception e) {
            log.error("查询会计规则详情失败，ruleId={}", ruleId, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(TblAccountingRuleCenterSaveParam param) {
        try {
            log.info("保存或更新会计规则，参数：{}", param);

            TblAccountingRuleCenter rule = new TblAccountingRuleCenter();
            BeanUtils.copyProperties(param, rule);

            if (StringUtils.hasText(param.getRuleId())) {
                // 更新
                rule.setUpdateTime(LocalDateTime.now());
                ruleCenterMapper.updateById(rule);
                return MyJsonBean.successData("更新成功", param.getRuleId());
            } else {
                // 新增
                String ruleId = UUID.randomUUID().toString().replace("-", "");
                rule.setRuleId(ruleId);
                rule.setCreateTime(LocalDateTime.now());
                ruleCenterMapper.insert(rule);
                return MyJsonBean.successData("新增成功", ruleId);
            }
        } catch (Exception e) {
            log.error("保存或更新会计规则失败", e);
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String ruleId) {
        try {
            if (!StringUtils.hasText(ruleId)) {
                return MyJsonBean.errorData("规则ID不能为空");
            }

            log.info("删除会计规则，ruleId={}", ruleId);

            ruleCenterMapper.deleteById(ruleId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除会计规则失败，ruleId={}", ruleId, e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean updateStatus(String ruleId, Integer isEnabled) {
        try {
            if (!StringUtils.hasText(ruleId)) {
                return MyJsonBean.errorData("规则ID不能为空");
            }

            log.info("更新会计规则状态，ruleId={}, isEnabled={}", ruleId, isEnabled);

            TblAccountingRuleCenter rule = ruleCenterMapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }

            rule.setIsEnabled(isEnabled);
            rule.setUpdateTime(LocalDateTime.now());
            ruleCenterMapper.updateById(rule);

            return MyJsonBean.successData("更新成功");
        } catch (Exception e) {
            log.error("更新会计规则状态失败", e);
            return MyJsonBean.errorData("更新失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getCategories() {
        try {
            log.info("获取规则分类列表");

            List<TblAccountingRuleCategory> categories = categoryMapper.selectList(null);
            return MyJsonBean.successData(categories);
        } catch (Exception e) {
            log.error("获取规则分类列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getLibraries() {
        try {
            log.info("获取规则库列表");

            List<TblAccountingRuleLibrary> libraries = libraryMapper.selectList(null);
            return MyJsonBean.successData(libraries);
        } catch (Exception e) {
            log.error("获取规则库列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getVersions(String ruleId) {
        try {
            if (!StringUtils.hasText(ruleId)) {
                return MyJsonBean.errorData("规则ID不能为空");
            }

            log.info("获取规则版本列表，ruleId={}", ruleId);

            LambdaQueryWrapper<TblAccountingRuleVersion> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblAccountingRuleVersion::getRuleId, ruleId);
            wrapper.orderByDesc(TblAccountingRuleVersion::getCreateTime);
            List<TblAccountingRuleVersion> versions = versionMapper.selectList(wrapper);

            return MyJsonBean.successData(versions);
        } catch (Exception e) {
            log.error("获取规则版本列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean createVersion(String ruleId) {
        try {
            if (!StringUtils.hasText(ruleId)) {
                return MyJsonBean.errorData("规则ID不能为空");
            }

            log.info("创建规则新版本，ruleId={}", ruleId);

            TblAccountingRuleCenter rule = ruleCenterMapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }

            // 创建新版本
            TblAccountingRuleVersion version = new TblAccountingRuleVersion();
            version.setVersionId(UUID.randomUUID().toString().replace("-", ""));
            version.setRuleId(ruleId);
            version.setVersionNumber("V" + System.currentTimeMillis());
            version.setVersionData(rule.getDimensionConfig()); // 使用dimensionConfig作为版本数据
            version.setCreateTime(LocalDateTime.now());
            versionMapper.insert(version);

            return MyJsonBean.successData("创建成功", version.getVersionId());
        } catch (Exception e) {
            log.error("创建规则新版本失败", e);
            return MyJsonBean.errorData("创建失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean testRule(String ruleId, Map<String, Object> testData) {
        try {
            if (!StringUtils.hasText(ruleId)) {
                return MyJsonBean.errorData("规则ID不能为空");
            }

            log.info("测试会计规则，ruleId={}, testData={}", ruleId, testData);

            TblAccountingRuleCenter rule = ruleCenterMapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }

            // 模拟规则测试结果
            Map<String, Object> testResult = new HashMap<>();
            testResult.put("ruleId", ruleId);
            testResult.put("ruleName", rule.getRuleName());
            testResult.put("testData", testData);
            testResult.put("result", "success");
            testResult.put("message", "测试通过");

            // 模拟生成的会计凭证
            Map<String, Object> voucher = new HashMap<>();
            voucher.put("debitAccount", rule.getDebitAccount());
            voucher.put("creditAccount", rule.getCreditAccount());
            voucher.put("amount", testData.get("amount"));
            voucher.put("summary", "根据规则生成的凭证");
            testResult.put("voucher", voucher);

            return MyJsonBean.successData(testResult);
        } catch (Exception e) {
            log.error("测试会计规则失败", e);
            return MyJsonBean.errorData("测试失败：" + e.getMessage());
        }
    }
}
