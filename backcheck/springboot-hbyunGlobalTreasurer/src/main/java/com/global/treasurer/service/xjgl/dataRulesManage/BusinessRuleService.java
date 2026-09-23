package com.global.treasurer.service.xjgl.dataRulesManage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblGtBusinessRule;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * 业务规则管理Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface BusinessRuleService extends IService<TblGtBusinessRule> {

    /**
     * 获取业务规则列表(分页)
     */
    PageInfo<TblGtBusinessRule> getBusinessRuleList(Map<String, Object> params, int pageNum, int pageSize);

    /**
     * 新增业务规则
     */
    int createBusinessRule(TblGtBusinessRule businessRule);

    /**
     * 更新业务规则
     */
    int updateBusinessRule(TblGtBusinessRule businessRule);

    /**
     * 删除业务规则
     */
    int deleteBusinessRule(Long ruleId);

    /**
     * 获取规则统计信息
     */
    Map<String, Object> getStatistics();
}
