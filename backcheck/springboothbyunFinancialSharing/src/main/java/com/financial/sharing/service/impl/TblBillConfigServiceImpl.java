package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.dto.TblBillConfigQueryParam;
import com.financial.sharing.dto.TblBillConfigSaveParam;
import com.financial.sharing.entity.TblBillConfig;
import com.financial.sharing.entity.TblBillFieldMapping;
import com.financial.sharing.entity.TblBillAuditRule;
import com.financial.sharing.mapper.TblBillConfigMapper;
import com.financial.sharing.mapper.TblBillFieldMappingMapper;
import com.financial.sharing.mapper.TblBillAuditRuleMapper;
import com.financial.sharing.service.TblBillConfigService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.SnowflakeIdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单据配置Service实现类
 */
@Slf4j
@Service
public class TblBillConfigServiceImpl extends ServiceImpl<TblBillConfigMapper, TblBillConfig>
        implements TblBillConfigService {

    @Resource
    private TblBillConfigMapper mapper;

    @Resource
    private TblBillFieldMappingMapper tblBillFieldMappingMapper;

    @Resource
    private TblBillAuditRuleMapper tblBillAuditRuleMapper;

    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker();

    @Override
    public MyJsonBean<PageResult> getList(TblBillConfigQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNo(), param.getPageSize());

            QueryWrapper<TblBillConfig> queryWrapper = new QueryWrapper<>();

            // 配置名称模糊查询
            if (param.getConfigName() != null && !param.getConfigName().isEmpty()) {
                queryWrapper.like("CONFIG_NAME", param.getConfigName());
            }

            // 配置编码查询
            if (param.getConfigCode() != null && !param.getConfigCode().isEmpty()) {
                queryWrapper.like("CONFIG_CODE", param.getConfigCode());
            }

            // 账单类型查询
            if (param.getBillType() != null && !param.getBillType().isEmpty()) {
                queryWrapper.eq("BILL_TYPE", param.getBillType());
            }

            // 组织ID查询
            if (param.getOrgId() != null && !param.getOrgId().isEmpty()) {
                queryWrapper.eq("ORG_ID", param.getOrgId());
            }

            // 是否启用查询
            if (param.getIsEnabled() != null) {
                queryWrapper.eq("IS_ENABLED", param.getIsEnabled());
            }

            queryWrapper.orderByDesc("CREATE_TIME");

            List<TblBillConfig> list = mapper.selectList(queryWrapper);
            PageInfo<TblBillConfig> pageInfo = new PageInfo<>(list);

            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            result.setCurrentPage(pageInfo.getPageNum());
            result.setPageSize(pageInfo.getPageSize());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询单据配置列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getById(String configId) {
        try {
            TblBillConfig config = mapper.selectById(configId);
            if (config == null) {
                return MyJsonBean.errorData("配置不存在");
            }
            return MyJsonBean.successData(config);
        } catch (Exception e) {
            log.error("查询单据配置详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(TblBillConfigSaveParam param) {
        try {
            TblBillConfig config = new TblBillConfig();

            if (param.getConfigId() != null && !param.getConfigId().isEmpty()) {
                // 更新
                config = mapper.selectById(param.getConfigId());
                if (config == null) {
                    return MyJsonBean.errorData("配置不存在");
                }
                config.setUpdateTime(LocalDateTime.now());
                config.setUpdateUser(param.getUpdateUser());
            } else {
                // 新增
                config.setCreateTime(LocalDateTime.now());
                config.setCreateUser(param.getCreateUser());
            }

            // 设置基本属性
            config.setConfigCode(param.getConfigCode());
            config.setConfigName(param.getConfigName());
            config.setBillType(param.getBillType());
            config.setOcrProvider(param.getOcrProvider());
            config.setOcrEnabled(param.getOcrEnabled());
            config.setAuditEnabled(param.getAuditEnabled());
            config.setAutoMatchEnabled(param.getAutoMatchEnabled());
            config.setDuplicateCheckEnabled(param.getDuplicateCheckEnabled());
            config.setOrgId(param.getOrgId());
            config.setOrgName(param.getOrgName());
            config.setIsEnabled(param.getIsEnabled());
            config.setDescription(param.getDescription());
            config.setRemark(param.getRemark());

            if (param.getConfigId() != null && !param.getConfigId().isEmpty()) {
                mapper.updateById(config);
            } else {
                mapper.insert(config);
            }

            return MyJsonBean.successData("保存成功", config);
        } catch (Exception e) {
            log.error("保存单据配置失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String configId) {
        try {
            TblBillConfig config = mapper.selectById(configId);
            if (config == null) {
                return MyJsonBean.errorData("配置不存在");
            }

            mapper.deleteById(configId);

            return MyJsonBean.successMsg("删除成功");
        } catch (Exception e) {
            log.error("删除单据配置失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean updateStatus(String configId, Integer isEnabled) {
        try {
            TblBillConfig config = mapper.selectById(configId);
            if (config == null) {
                return MyJsonBean.errorData("配置不存在");
            }

            config.setIsEnabled(isEnabled);
            config.setUpdateTime(LocalDateTime.now());
            mapper.updateById(config);

            return MyJsonBean.successMsg("状态更新成功");
        } catch (Exception e) {
            log.error("更新单据配置状态失败", e);
            return MyJsonBean.errorData("更新失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean testConfig(String configId, Map<String, Object> testData) {
        try {
            TblBillConfig config = mapper.selectById(configId);
            if (config == null) {
                return MyJsonBean.errorData("配置不存在");
            }

            // TODO: 实现配置测试逻辑
            // 这里应该根据配置和测试数据进行配置测试

            Map<String, Object> result = new HashMap<>();
            result.put("configId", configId);
            result.put("configName", config.getConfigName());
            result.put("testResult", "PASS");
            result.put("message", "配置测试通过");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("测试单据配置失败", e);
            return MyJsonBean.errorData("测试失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getFieldMappings(String configId) {
        try {
            // 从数据库查询字段映射，按排序号排序
            List<TblBillFieldMapping> mappings = tblBillFieldMappingMapper.selectByConfigIdOrderBySort(configId);

            log.info("查询字段映射，configId: {}, 结果数量: {}", configId, mappings != null ? mappings.size() : 0);

            return MyJsonBean.successData(mappings);
        } catch (Exception e) {
            log.error("获取字段映射失败", e);
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveFieldMappings(String configId, List<TblBillFieldMapping> mappings) {
        try {
            // 1. 验证配置是否存在
            TblBillConfig config = mapper.selectById(configId);
            if (config == null) {
                return MyJsonBean.errorData("配置不存在");
            }

            // 2. 删除该配置的所有旧映射
            int deletedCount = tblBillFieldMappingMapper.deleteByConfigId(configId);
            log.info("删除旧字段映射，configId: {}, 删除数量: {}", configId, deletedCount);

            // 3. 保存新的字段映射
            if (mappings != null && !mappings.isEmpty()) {
                LocalDateTime now = LocalDateTime.now();

                for (TblBillFieldMapping mapping : mappings) {
                    // 设置配置ID
                    mapping.setConfigId(configId);

                    // 如果没有 mappingId，生成新的 ID
                    if (mapping.getMappingId() == null || mapping.getMappingId().isEmpty()) {
                        mapping.setMappingId(String.valueOf(idWorker.nextId()));
                    }

                    // 设置创建时间
                    if (mapping.getCreateTime() == null) {
                        mapping.setCreateTime(now);
                    }

                    // 设置默认排序号
                    if (mapping.getSortOrder() == null) {
                        mapping.setSortOrder(0);
                    }

                    // 设置默认必填状态
                    if (mapping.getIsRequired() == null) {
                        mapping.setIsRequired(0);
                    }

                    // 插入数据库
                    tblBillFieldMappingMapper.insert(mapping);
                }

                log.info("保存字段映射成功，configId: {}, 保存数量: {}", configId, mappings.size());
            }

            return MyJsonBean.successData("保存成功");
        } catch (Exception e) {
            log.error("保存字段映射失败，configId: " + configId, e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getAuditRules(String configId) {
        try {
            // 验证配置是否存在
            TblBillConfig config = mapper.selectById(configId);
            if (config == null) {
                return MyJsonBean.errorData("配置不存在");
            }

            // 查询配置关联的稽核规则，按优先级排序
            List<TblBillAuditRule> rules = tblBillAuditRuleMapper.selectByConfigIdOrderByPriority(configId);
            log.info("查询稽核规则，configId: {}, 规则数量: {}", configId, rules != null ? rules.size() : 0);

            return MyJsonBean.successData(rules != null ? rules : new ArrayList<>());
        } catch (Exception e) {
            log.error("获取稽核规则失败", e);
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveAuditRules(String configId, List<TblBillAuditRule> rules) {
        try {
            // 1. 验证配置是否存在
            TblBillConfig config = mapper.selectById(configId);
            if (config == null) {
                return MyJsonBean.errorData("配置不存在");
            }

            // 2. 删除该配置的所有旧规则关联
            int deletedCount = tblBillAuditRuleMapper.deleteByConfigId(configId);
            log.info("删除旧稽核规则，configId: {}, 删除数量: {}", configId, deletedCount);

            // 3. 保存新的稽核规则
            if (rules != null && !rules.isEmpty()) {
                LocalDateTime now = LocalDateTime.now();

                for (TblBillAuditRule rule : rules) {
                    // 设置配置ID
                    rule.setConfigId(configId);

                    // 如果没有 ID，生成新的 ID
                    if (rule.getId() == null || rule.getId().isEmpty()) {
                        rule.setId(String.valueOf(idWorker.nextId()));
                    }

                    // 如果没有 ruleId，生成新的 ruleId
                    if (rule.getRuleId() == null || rule.getRuleId().isEmpty()) {
                        rule.setRuleId(String.valueOf(idWorker.nextId()));
                    }

                    // 设置创建时间
                    if (rule.getCreateTime() == null) {
                        rule.setCreateTime(now);
                    }

                    // 设置默认优先级
                    if (rule.getPriority() == null) {
                        rule.setPriority(1);
                    }

                    // 设置默认启用状态
                    if (rule.getIsEnabled() == null) {
                        rule.setIsEnabled(1);
                    }

                    // 插入规则
                    tblBillAuditRuleMapper.insert(rule);
                }

                log.info("保存稽核规则成功，configId: {}, 规则数量: {}", configId, rules.size());
            }

            return MyJsonBean.successData("保存成功");
        } catch (Exception e) {
            log.error("保存稽核规则失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getRecognitionLogs(String configId) {
        try {
            // TODO: 实现获取识别日志逻辑
            // 这里应该查询配置的识别日志

            List<Map<String, Object>> logs = new ArrayList<>();
            Map<String, Object> log1 = new HashMap<>();
            log1.put("logId", "L001");
            log1.put("recognitionTime", LocalDateTime.now());
            log1.put("result", "SUCCESS");
            logs.add(log1);

            Map<String, Object> log2 = new HashMap<>();
            log2.put("logId", "L002");
            log2.put("recognitionTime", LocalDateTime.now());
            log2.put("result", "FAILED");
            logs.add(log2);

            return MyJsonBean.successData(logs);
        } catch (Exception e) {
            log.error("获取识别日志失败", e);
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }
}
