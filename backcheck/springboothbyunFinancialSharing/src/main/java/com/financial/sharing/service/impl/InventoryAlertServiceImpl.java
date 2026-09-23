package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.oracle.entity.InventoryAlertRecordEntity;
import com.financial.sharing.oracle.entity.InventoryAlertRuleEntity;
import com.financial.sharing.service.InventoryAlertService;
import com.financial.sharing.util.PageResult;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 库存预警服务实现类
 * 
 * @author system
 * @since 2026-01-29
 */
@Slf4j
@Service
public class InventoryAlertServiceImpl implements InventoryAlertService {

    @Autowired
    private DateBaseConfig dateBaseConfig;

    @Autowired
    private UserProvider userProvider;

    @Override
    public Map<String, Object> getAlertStatistics(Map<String, Object> param) {
        try {
            // 获取租户ID - 使用orgid作为租户ID
            Long tenantId = 1L;
            try {
                if (userProvider.get() != null && userProvider.get().getOrgid() != null) {
                    tenantId = userProvider.get().getOrgid().longValue();
                }
            } catch (Exception e) {
                log.warn("获取租户ID失败，使用默认值", e);
            }
            param.put("tenantId", tenantId);
            
            return dateBaseConfig.getOracleInventoryAlertRecordMapper()
                .countAlertStatistics(param);
        } catch (Exception e) {
            log.error("获取预警统计失败", e);
            throw new RuntimeException("获取预警统计失败: " + e.getMessage());
        }
    }

    @Override
    public PageResult<Map<String, Object>> getAlertPage(Map<String, Object> param) {
        try {
            int pageNum = param.get("pageNum") != null ? (int) param.get("pageNum") : 1;
            int pageSize = param.get("pageSize") != null ? (int) param.get("pageSize") : 10;
            
            // 获取租户ID - 使用orgid作为租户ID
            Long tenantId = 1L;
            try {
                if (userProvider.get() != null && userProvider.get().getOrgid() != null) {
                    tenantId = userProvider.get().getOrgid().longValue();
                }
            } catch (Exception e) {
                log.warn("获取租户ID失败，使用默认值", e);
            }
            param.put("tenantId", tenantId);

            Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
            com.baomidou.mybatisplus.core.metadata.IPage<Map<String, Object>> result = 
                dateBaseConfig.getOracleInventoryAlertRecordMapper()
                    .selectRecordPage(page, param);

            return new PageResult<>(
                (int) result.getTotal(),
                (int) result.getCurrent(),
                (int) result.getPages(),
                (int) result.getSize(),
                result.getRecords()
            );
        } catch (Exception e) {
            log.error("查询预警列表失败", e);
            throw new RuntimeException("查询预警列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processAlert(Long alertId, Map<String, Object> param) {
        try {
            Integer processStatus = Integer.parseInt(param.get("processStatus").toString());
            String processRemark = (String) param.get("processRemark");
            String processorId = userProvider.get() != null && userProvider.get().getStaffid() != null ?
                userProvider.get().getStaffid().toString() : "SYSTEM";
            String processorName = userProvider.get() != null ? userProvider.get().getUsername() : "系统";

            int result = dateBaseConfig.getOracleInventoryAlertRecordMapper()
                .processAlert(alertId, processStatus, processRemark, processorId, processorName);

            log.info("处理预警成功, alertId: {}", alertId);
            return result > 0;
        } catch (Exception e) {
            log.error("处理预警失败, alertId: {}", alertId, e);
            throw new RuntimeException("处理预警失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean ignoreAlert(Long alertId) {
        try {
            String processorId = userProvider.get() != null && userProvider.get().getStaffid() != null ?
                userProvider.get().getStaffid().toString() : "SYSTEM";
            String processorName = userProvider.get() != null ? userProvider.get().getUsername() : "系统";

            int result = dateBaseConfig.getOracleInventoryAlertRecordMapper()
                .ignoreAlert(alertId, processorId, processorName);

            log.info("忽略预警成功, alertId: {}", alertId);
            return result > 0;
        } catch (Exception e) {
            log.error("忽略预警失败, alertId: {}", alertId, e);
            throw new RuntimeException("忽略预警失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchProcessAlert(List<Long> alertIds, Map<String, Object> param) {
        try {
            Integer processStatus = Integer.parseInt(param.get("processStatus").toString());
            String processorId = userProvider.get() != null && userProvider.get().getStaffid() != null ?
                userProvider.get().getStaffid().toString() : "SYSTEM";

            int result = dateBaseConfig.getOracleInventoryAlertRecordMapper()
                .batchProcessAlert(alertIds, processStatus, processorId);

            log.info("批量处理预警成功, count: {}", result);
            return result > 0;
        } catch (Exception e) {
            log.error("批量处理预警失败", e);
            throw new RuntimeException("批量处理预警失败: " + e.getMessage());
        }
    }

    @Override
    public PageResult<Map<String, Object>> getRulePage(Map<String, Object> param) {
        try {
            int pageNum = param.get("pageNum") != null ? (int) param.get("pageNum") : 1;
            int pageSize = param.get("pageSize") != null ? (int) param.get("pageSize") : 10;

            // 获取租户ID - 使用orgid作为租户ID
            Long tenantId = 1L;
            try {
                if (userProvider.get() != null && userProvider.get().getOrgid() != null) {
                    tenantId = userProvider.get().getOrgid().longValue();
                }
            } catch (Exception e) {
                log.warn("获取租户ID失败，使用默认值", e);
            }
            param.put("tenantId", tenantId);

            Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
            com.baomidou.mybatisplus.core.metadata.IPage<Map<String, Object>> result =
                dateBaseConfig.getOracleInventoryAlertRuleMapper()
                    .selectRulePage(page, param);

            return new PageResult<>(
                (int) result.getTotal(),
                (int) result.getCurrent(),
                (int) result.getPages(),
                (int) result.getSize(),
                result.getRecords()
            );
        } catch (Exception e) {
            log.error("查询预警规则列表失败", e);
            throw new RuntimeException("查询预警规则列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateRule(Map<String, Object> param) {
        try {
            InventoryAlertRuleEntity entity = new InventoryAlertRuleEntity();

            if (param.get("ruleId") != null) {
                entity.setRuleId(Long.parseLong(param.get("ruleId").toString()));
            }
            entity.setRuleName((String) param.get("ruleName"));
            entity.setRuleType((String) param.get("ruleType"));
            entity.setRuleTypeName((String) param.get("ruleTypeName"));
            entity.setAlertLevel(param.get("alertLevel") != null ?
                Integer.parseInt(param.get("alertLevel").toString()) : 1);
            entity.setStatus(param.get("status") != null ?
                Integer.parseInt(param.get("status").toString()) : 1);

            // 获取租户ID - 使用orgid作为租户ID
            Long tenantId = 1L;
            try {
                if (userProvider.get() != null && userProvider.get().getOrgid() != null) {
                    tenantId = userProvider.get().getOrgid().longValue();
                }
            } catch (Exception e) {
                log.warn("获取租户ID失败，使用默认值", e);
            }
            entity.setTenantId(tenantId);

            int result;
            if (entity.getRuleId() == null) {
                result = dateBaseConfig.getOracleInventoryAlertRuleMapper().insert(entity);
            } else {
                result = dateBaseConfig.getOracleInventoryAlertRuleMapper().updateById(entity);
            }

            log.info("保存预警规则成功, ruleId: {}", entity.getRuleId());
            return result > 0;
        } catch (Exception e) {
            log.error("保存预警规则失败", e);
            throw new RuntimeException("保存预警规则失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRule(Long ruleId) {
        try {
            int result = dateBaseConfig.getOracleInventoryAlertRuleMapper().deleteById(ruleId);
            log.info("删除预警规则成功, ruleId: {}", ruleId);
            return result > 0;
        } catch (Exception e) {
            log.error("删除预警规则失败, ruleId: {}", ruleId, e);
            throw new RuntimeException("删除预警规则失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleRuleStatus(Long ruleId, Integer status) {
        try {
            InventoryAlertRuleEntity entity = dateBaseConfig.getOracleInventoryAlertRuleMapper()
                .selectById(ruleId);

            if (entity == null) {
                throw new RuntimeException("预警规则不存在");
            }

            entity.setStatus(status);
            int result = dateBaseConfig.getOracleInventoryAlertRuleMapper().updateById(entity);

            log.info("切换预警规则状态成功, ruleId: {}, status: {}", ruleId, status);
            return result > 0;
        } catch (Exception e) {
            log.error("切换预警规则状态失败, ruleId: {}", ruleId, e);
            throw new RuntimeException("切换预警规则状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchToggleRuleStatus(List<Long> ruleIds, Integer status) {
        try {
            String updaterId = userProvider.get() != null && userProvider.get().getStaffid() != null ?
                userProvider.get().getStaffid().toString() : "SYSTEM";
            int result = dateBaseConfig.getOracleInventoryAlertRuleMapper()
                .batchToggleStatus(ruleIds, status, updaterId);

            log.info("批量切换预警规则状态成功, count: {}", result);
            return result > 0;
        } catch (Exception e) {
            log.error("批量切换预警规则状态失败", e);
            throw new RuntimeException("批量切换预警规则状态失败: " + e.getMessage());
        }
    }

    @Override
    public String exportAlert(Map<String, Object> param) {
        try {
            // TODO: 实现导出功能
            log.info("导出预警数据, param: {}", param);
            return "export_file_path.xlsx";
        } catch (Exception e) {
            log.error("导出预警数据失败", e);
            throw new RuntimeException("导出预警数据失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAlertTrend(Map<String, Object> param) {
        try {
            // 获取租户ID - 使用orgid作为租户ID
            Long tenantId = 1L;
            try {
                if (userProvider.get() != null && userProvider.get().getOrgid() != null) {
                    tenantId = userProvider.get().getOrgid().longValue();
                }
            } catch (Exception e) {
                log.warn("获取租户ID失败，使用默认值", e);
            }
            param.put("tenantId", tenantId);

            return dateBaseConfig.getOracleInventoryAlertRecordMapper()
                .selectAlertTrend(param);
        } catch (Exception e) {
            log.error("获取预警趋势分析失败", e);
            throw new RuntimeException("获取预警趋势分析失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAlertDistribution(Map<String, Object> param) {
        try {
            // 获取租户ID - 使用orgid作为租户ID
            Long tenantId = 1L;
            try {
                if (userProvider.get() != null && userProvider.get().getOrgid() != null) {
                    tenantId = userProvider.get().getOrgid().longValue();
                }
            } catch (Exception e) {
                log.warn("获取租户ID失败，使用默认值", e);
            }
            param.put("tenantId", tenantId);

            return dateBaseConfig.getOracleInventoryAlertRecordMapper()
                .selectAlertDistribution(param);
        } catch (Exception e) {
            log.error("获取预警类型分布失败", e);
            throw new RuntimeException("获取预警类型分布失败: " + e.getMessage());
        }
    }
}


