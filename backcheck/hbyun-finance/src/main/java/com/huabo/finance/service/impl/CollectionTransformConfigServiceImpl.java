package com.huabo.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.ResponseFormat;
import com.huabo.finance.entity.CollectionTransformConfig;
import com.huabo.finance.entity.TransformRule;
import com.huabo.finance.mapper.CollectionTransformConfigMapper;
import com.huabo.finance.mapper.TransformRuleMapper;
import com.huabo.finance.service.CollectionTransformConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 采集任务转化配置服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Service
public class CollectionTransformConfigServiceImpl extends ServiceImpl<CollectionTransformConfigMapper, CollectionTransformConfig> 
        implements CollectionTransformConfigService {

    @Resource
    private CollectionTransformConfigMapper collectionTransformConfigMapper;

    @Resource
    private TransformRuleMapper transformRuleMapper;

    @Override
    public JsonBean saveTransformConfig(TblStaffUtil staff, CollectionTransformConfig config) throws Exception {
        if (StringUtils.isNotBlank(config.getConfigId())) {
            // 更新
            config.setModifier(staff.getStaffid());
            config.setModifyTime(new Date());
            collectionTransformConfigMapper.updateById(config);
        } else {
            // 新增
            config.setConfigId(RandomUtil.uuStringId());
            config.setCreator(staff.getStaffid());
            config.setCreateTime(new Date());
            config.setLinkOrgId(staff.getCurrentOrg().getOrgid());
            config.setLinkDeptId(staff.getLinkDetp().getOrgid());
            config.setExecuteStatus("PENDING"); // 初始状态为待执行
            collectionTransformConfigMapper.insert(config);
        }
        return ResponseFormat.retParam(1, "保存成功", config);
    }

    @Override
    public JsonBean getConfigByCollectionTaskId(String collectionTaskId) throws Exception {
        CollectionTransformConfig config = collectionTransformConfigMapper.selectByCollectionTaskId(collectionTaskId);
        return ResponseFormat.retParam(1, 200, config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean executeAutoTransform(String collectionTaskId) throws Exception {
        log.info("开始执行采集任务[{}]的自动转化", collectionTaskId);
        
        // 1. 获取转化配置
        CollectionTransformConfig config = collectionTransformConfigMapper.selectByCollectionTaskId(collectionTaskId);
        if (config == null) {
            log.warn("采集任务[{}]未配置转化规则", collectionTaskId);
            return ResponseFormat.retParam(0, "未配置转化规则", null);
        }

        if (config.getAutoExecute() == null || config.getAutoExecute() != 1) {
            log.info("采集任务[{}]未启用自动执行", collectionTaskId);
            return ResponseFormat.retParam(0, "未启用自动执行", null);
        }

        // 2. 更新状态为执行中
        collectionTransformConfigMapper.updateExecuteStatus(config.getConfigId(), "RUNNING");
        config.setStartTime(new Date());

        int successCount = 0;
        int failedCount = 0;
        StringBuilder errorMsg = new StringBuilder();

        try {
            // 3. 获取需要自动执行的转化规则(按执行顺序排序)
            List<TransformRule> ruleList = transformRuleMapper.selectAutoExecuteRules(collectionTaskId);
            log.info("采集任务[{}]共有{}条自动执行规则", collectionTaskId, ruleList.size());

            // 4. 按顺序执行转化规则
            for (TransformRule rule : ruleList) {
                try {
                    log.info("执行转化规则[{}]: {} -> {}", rule.getRuleId(), rule.getSourceTable(), rule.getTargetTable());
                    
                    // TODO: 调用转化规则执行逻辑
                    // transformRuleService.executeTransformRule(rule);
                    
                    successCount++;
                } catch (Exception e) {
                    failedCount++;
                    String error = String.format("规则[%s]执行失败: %s", rule.getRuleId(), e.getMessage());
                    errorMsg.append(error).append("; ");
                    log.error(error, e);

                    // 根据错误处理策略决定是否继续
                    if ("STOP".equals(config.getOnError())) {
                        log.warn("错误处理策略为STOP,停止后续转化");
                        break;
                    } else if ("ROLLBACK".equals(config.getOnError())) {
                        log.warn("错误处理策略为ROLLBACK,回滚所有转化");
                        throw new Exception("转化失败,执行回滚: " + error);
                    }
                    // CONTINUE策略继续执行
                }
            }

            // 5. 更新执行结果
            String finalStatus = failedCount > 0 ? "FAILED" : "SUCCESS";
            collectionTransformConfigMapper.updateExecuteResult(
                config.getConfigId(), 
                finalStatus, 
                successCount, 
                failedCount, 
                errorMsg.toString()
            );

            log.info("采集任务[{}]转化完成: 成功{}, 失败{}", collectionTaskId, successCount, failedCount);
            return ResponseFormat.retParam(1, "转化完成", null);

        } catch (Exception e) {
            // 执行失败,更新状态
            collectionTransformConfigMapper.updateExecuteResult(
                config.getConfigId(), 
                "FAILED", 
                successCount, 
                failedCount, 
                e.getMessage()
            );
            log.error("采集任务[{}]转化失败", collectionTaskId, e);
            throw e;
        }
    }

    @Override
    public JsonBean updateExecuteStatus(String configId, String status) throws Exception {
        collectionTransformConfigMapper.updateExecuteStatus(configId, status);
        return ResponseFormat.retParam(1, "状态更新成功", null);
    }
}

