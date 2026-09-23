package com.financial.sharing.service;

import com.financial.sharing.entity.TblAccountingRuleCenter;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.TblAccountingRuleCenterQueryParam;
import com.financial.sharing.vo.param.TblAccountingRuleCenterSaveParam;

import java.util.Map;

/**
 * 会计规则中心服务接口
 *
 * @author system
 * @since 2025-01-30
 */
public interface TblAccountingRuleCenterService {

    /**
     * 分页查询会计规则列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult<TblAccountingRuleCenter>> getList(TblAccountingRuleCenterQueryParam param);

    /**
     * 根据ID查询会计规则详情
     *
     * @param ruleId 规则ID
     * @return 规则详情
     */
    MyJsonBean getById(String ruleId);

    /**
     * 保存或更新会计规则
     *
     * @param param 保存参数
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(TblAccountingRuleCenterSaveParam param);

    /**
     * 删除会计规则
     *
     * @param ruleId 规则ID
     * @return 操作结果
     */
    MyJsonBean delete(String ruleId);

    /**
     * 更新启用状态
     *
     * @param ruleId 规则ID
     * @param isEnabled 启用状态
     * @return 操作结果
     */
    MyJsonBean updateStatus(String ruleId, Integer isEnabled);

    /**
     * 获取规则分类
     *
     * @return 分类列表
     */
    MyJsonBean getCategories();

    /**
     * 获取规则库
     *
     * @return 规则库列表
     */
    MyJsonBean getLibraries();

    /**
     * 获取版本列表
     *
     * @param ruleId 规则ID
     * @return 版本列表
     */
    MyJsonBean getVersions(String ruleId);

    /**
     * 创建新版本
     *
     * @param ruleId 规则ID
     * @return 操作结果
     */
    MyJsonBean createVersion(String ruleId);

    /**
     * 规则测试
     *
     * @param ruleId 规则ID
     * @param testData 测试数据
     * @return 测试结果
     */
    MyJsonBean testRule(String ruleId, Map<String, Object> testData);
}
