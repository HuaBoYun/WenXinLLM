package com.huabo.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.TransformRule;

/**
 * 转化规则服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface TransformRuleService extends IService<TransformRule> {

    /**
     * 保存转化规则
     */
    JsonBean saveTransformRule(TblStaffUtil staff, TransformRule rule) throws Exception;

    /**
     * 根据采集任务ID获取转化规则列表
     */
    JsonBean getRulesByCollectionTaskId(String collectionTaskId) throws Exception;

    /**
     * 执行转化规则
     */
    JsonBean executeTransformRule(TransformRule rule) throws Exception;

    /**
     * 预览转化SQL
     */
    JsonBean previewTransformSql(TransformRule rule) throws Exception;

    /**
     * 删除转化规则
     */
    JsonBean deleteTransformRule(String ruleId) throws Exception;
}

