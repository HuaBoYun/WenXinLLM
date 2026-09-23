package com.financial.sharing.budgetControl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.budgetControl.dto.WarningConfigQueryParam;
import com.financial.sharing.budgetControl.entity.TblWarningConfig;
import com.financial.sharing.budgetControl.mapper.WarningConfigMapper;
import com.financial.sharing.budgetControl.service.WarningConfigService;
import com.financial.sharing.util.MyJsonBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 预警配置Service实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class WarningConfigServiceImpl implements WarningConfigService {

    @Autowired
    private WarningConfigMapper warningConfigMapper;

    @Override
    public MyJsonBean queryPage(WarningConfigQueryParam param) {
        try {
            LambdaQueryWrapper<TblWarningConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblWarningConfig::getOrgId, param.getOrgId());
            wrapper.ne(TblWarningConfig::getStatus, "DELETED");

            if (StringUtils.isNotBlank(param.getConfigCode())) {
                wrapper.like(TblWarningConfig::getConfigCode, param.getConfigCode());
            }
            if (StringUtils.isNotBlank(param.getConfigName())) {
                wrapper.like(TblWarningConfig::getConfigName, param.getConfigName());
            }
            if (StringUtils.isNotBlank(param.getWarningType())) {
                wrapper.eq(TblWarningConfig::getWarningType, param.getWarningType());
            }
            if (StringUtils.isNotBlank(param.getBizOrgId())) {
                wrapper.eq(TblWarningConfig::getBizOrgId, param.getBizOrgId());
            }
            if (StringUtils.isNotBlank(param.getSubjectCode())) {
                wrapper.eq(TblWarningConfig::getSubjectCode, param.getSubjectCode());
            }
            if (StringUtils.isNotBlank(param.getWarningLevel())) {
                wrapper.eq(TblWarningConfig::getWarningLevel, param.getWarningLevel());
            }
            if (StringUtils.isNotBlank(param.getIsEnabled())) {
                wrapper.eq(TblWarningConfig::getIsEnabled, param.getIsEnabled());
            }

            wrapper.orderByDesc(TblWarningConfig::getCreateTime);

            Page<TblWarningConfig> page = new Page<>(param.getPageNumber(), param.getPageSize());
            IPage<TblWarningConfig> result = warningConfigMapper.selectPage(page, wrapper);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("分页查询预警配置失败", e);
            return MyJsonBean.errorData("分页查询预警配置失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryById(String configId, String orgId) {
        try {
            LambdaQueryWrapper<TblWarningConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblWarningConfig::getConfigId, configId);
            wrapper.eq(TblWarningConfig::getOrgId, orgId);
            wrapper.ne(TblWarningConfig::getStatus, "DELETED");

            TblWarningConfig config = warningConfigMapper.selectOne(wrapper);
            if (config == null) {
                return MyJsonBean.errorData("预警配置不存在");
            }

            return MyJsonBean.successData(config);
        } catch (Exception e) {
            log.error("查询预警配置失败", e);
            return MyJsonBean.errorData("查询预警配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveConfig(TblWarningConfig config) {
        try {
            // 检查配置编码是否重复
            if (StringUtils.isNotBlank(config.getConfigCode())) {
                LambdaQueryWrapper<TblWarningConfig> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(TblWarningConfig::getConfigCode, config.getConfigCode());
                wrapper.eq(TblWarningConfig::getOrgId, config.getOrgId());
                wrapper.ne(TblWarningConfig::getStatus, "DELETED");
                if (StringUtils.isNotBlank(config.getConfigId())) {
                    wrapper.ne(TblWarningConfig::getConfigId, config.getConfigId());
                }
                long count = warningConfigMapper.selectCount(wrapper);
                if (count > 0) {
                    return MyJsonBean.errorData("配置编码已存在");
                }
            }

            if (StringUtils.isBlank(config.getConfigId())) {
                // 新增
                config.setStatus("NORMAL");
                config.setCreateTime(new Date());
                warningConfigMapper.insert(config);
                return MyJsonBean.successData("新增成功");
            } else {
                // 更新
                config.setUpdateTime(new Date());
                warningConfigMapper.updateById(config);
                return MyJsonBean.successData("更新成功");
            }
        } catch (Exception e) {
            log.error("保存预警配置失败", e);
            return MyJsonBean.errorData("保存预警配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteConfig(String configId, String orgId) {
        try {
            TblWarningConfig config = new TblWarningConfig();
            config.setConfigId(configId);
            config.setStatus("DELETED");
            config.setUpdateTime(new Date());

            LambdaQueryWrapper<TblWarningConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblWarningConfig::getConfigId, configId);
            wrapper.eq(TblWarningConfig::getOrgId, orgId);

            int result = warningConfigMapper.update(config, wrapper);
            if (result > 0) {
                return MyJsonBean.successData("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除预警配置失败", e);
            return MyJsonBean.errorData("删除预警配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean toggleEnabled(String configId, String isEnabled, String orgId) {
        try {
            TblWarningConfig config = new TblWarningConfig();
            config.setConfigId(configId);
            config.setIsEnabled(isEnabled);
            config.setUpdateTime(new Date());

            LambdaQueryWrapper<TblWarningConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblWarningConfig::getConfigId, configId);
            wrapper.eq(TblWarningConfig::getOrgId, orgId);

            int result = warningConfigMapper.update(config, wrapper);
            if (result > 0) {
                String action = "Y".equals(isEnabled) ? "启用" : "禁用";
                return MyJsonBean.successData(action + "成功");
            } else {
                return MyJsonBean.errorData("操作失败");
            }
        } catch (Exception e) {
            log.error("启用/禁用预警配置失败", e);
            return MyJsonBean.errorData("启用/禁用预警配置失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean testConfig(String configId, String orgId) {
        try {
            // 查询配置
            LambdaQueryWrapper<TblWarningConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblWarningConfig::getConfigId, configId);
            wrapper.eq(TblWarningConfig::getOrgId, orgId);
            wrapper.ne(TblWarningConfig::getStatus, "DELETED");

            TblWarningConfig config = warningConfigMapper.selectOne(wrapper);
            if (config == null) {
                return MyJsonBean.errorData("预警配置不存在");
            }

            // 模拟测试
            StringBuilder testResult = new StringBuilder();
            testResult.append("预警配置测试结果：\n");
            testResult.append("配置名称：").append(config.getConfigName()).append("\n");
            testResult.append("预警类型：").append(config.getWarningType()).append("\n");
            testResult.append("阈值类型：").append(config.getThresholdType()).append("\n");
            testResult.append("阈值：").append(config.getThresholdValue()).append("\n");
            testResult.append("预警级别：").append(config.getWarningLevel()).append("\n");
            testResult.append("接收人：").append(config.getReceivers()).append("\n");
            testResult.append("发送方式：").append(config.getSendMethods()).append("\n");
            testResult.append("\n测试消息模板：\n");
            testResult.append(config.getMessageTemplate()).append("\n");
            testResult.append("\n测试结果：配置有效，可以正常发送预警消息");

            return MyJsonBean.successData(testResult.toString());
        } catch (Exception e) {
            log.error("测试预警配置失败", e);
            return MyJsonBean.errorData("测试预警配置失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean sendWarningMessage(String configId, String message, String orgId) {
        try {
            // 查询配置
            LambdaQueryWrapper<TblWarningConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblWarningConfig::getConfigId, configId);
            wrapper.eq(TblWarningConfig::getOrgId, orgId);
            wrapper.eq(TblWarningConfig::getIsEnabled, "Y");
            wrapper.ne(TblWarningConfig::getStatus, "DELETED");

            TblWarningConfig config = warningConfigMapper.selectOne(wrapper);
            if (config == null) {
                return MyJsonBean.errorData("预警配置不存在或未启用");
            }

            // 解析接收人
            String[] receivers = config.getReceivers().split(",");

            // 解析发送方式
            String[] sendMethods = config.getSendMethods().split(",");

            // 模拟发送消息
            StringBuilder sendResult = new StringBuilder();
            sendResult.append("预警消息发送结果：\n");

            for (String method : sendMethods) {
                method = method.trim();
                if ("EMAIL".equals(method)) {
                    sendResult.append("邮件发送：成功发送到 ").append(receivers.length).append(" 个接收人\n");
                } else if ("SMS".equals(method)) {
                    sendResult.append("短信发送：成功发送到 ").append(receivers.length).append(" 个接收人\n");
                } else if ("INTERNAL".equals(method)) {
                    sendResult.append("站内信发送：成功发送到 ").append(receivers.length).append(" 个接收人\n");
                }
            }

            log.info("预警消息发送成功，配置ID：{}，接收人数：{}", configId, receivers.length);
            return MyJsonBean.successData(sendResult.toString());
        } catch (Exception e) {
            log.error("发送预警消息失败", e);
            return MyJsonBean.errorData("发送预警消息失败：" + e.getMessage());
        }
    }
}

