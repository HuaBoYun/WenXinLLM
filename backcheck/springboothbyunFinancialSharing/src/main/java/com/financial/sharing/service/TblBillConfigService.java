package com.financial.sharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.dto.TblBillConfigQueryParam;
import com.financial.sharing.dto.TblBillConfigSaveParam;
import com.financial.sharing.entity.TblBillConfig;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;

import java.util.Map;

/**
 * 单据配置Service接口
 */
public interface TblBillConfigService extends IService<TblBillConfig> {

    /**
     * 分页查询单据配置列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult> getList(TblBillConfigQueryParam param);

    /**
     * 根据配置ID查询详情
     *
     * @param configId 配置ID
     * @return 配置详情
     */
    MyJsonBean getById(String configId);

    /**
     * 保存或更新单据配置
     *
     * @param param 保存参数
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(TblBillConfigSaveParam param);

    /**
     * 删除单据配置
     *
     * @param configId 配置ID
     * @return 操作结果
     */
    MyJsonBean delete(String configId);

    /**
     * 更新配置状态
     *
     * @param configId 配置ID
     * @param isEnabled 是否启用
     * @return 操作结果
     */
    MyJsonBean updateStatus(String configId, Integer isEnabled);

    /**
     * 测试配置
     *
     * @param configId 配置ID
     * @param testData 测试数据
     * @return 测试结果
     */
    MyJsonBean testConfig(String configId, Map<String, Object> testData);

    /**
     * 获取字段映射
     *
     * @param configId 配置ID
     * @return 字段映射
     */
    MyJsonBean getFieldMappings(String configId);

    /**
     * 保存字段映射
     *
     * @param configId 配置ID
     * @param mappings 字段映射列表
     * @return 保存结果
     */
    MyJsonBean saveFieldMappings(String configId, java.util.List<com.financial.sharing.entity.TblBillFieldMapping> mappings);

    /**
     * 获取关联的稽核规则
     *
     * @param configId 配置ID
     * @return 稽核规则列表
     */
    MyJsonBean getAuditRules(String configId);

    /**
     * 保存稽核规则
     *
     * @param configId 配置ID
     * @param rules 稽核规则列表
     * @return 操作结果
     */
    MyJsonBean saveAuditRules(String configId, java.util.List<com.financial.sharing.entity.TblBillAuditRule> rules);

    /**
     * 获取识别日志
     *
     * @param configId 配置ID
     * @return 识别日志列表
     */
    MyJsonBean getRecognitionLogs(String configId);
}
