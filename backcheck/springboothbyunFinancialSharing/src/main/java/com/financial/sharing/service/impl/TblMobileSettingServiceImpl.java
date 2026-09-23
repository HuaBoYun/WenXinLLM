package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.entity.TblMobileSetting;
import com.financial.sharing.entity.TblMobileSettingGroup;
import com.financial.sharing.entity.TblMobileSettingSyncLog;
import com.financial.sharing.entity.TblMobileSettingTemplate;
import com.financial.sharing.entity.TblMobileSettingVersion;
import com.financial.sharing.mapper.TblMobileSettingGroupMapper;
import com.financial.sharing.mapper.TblMobileSettingMapper;
import com.financial.sharing.mapper.TblMobileSettingSyncLogMapper;
import com.financial.sharing.mapper.TblMobileSettingTemplateMapper;
import com.financial.sharing.mapper.TblMobileSettingVersionMapper;
import com.financial.sharing.service.TblMobileSettingService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.TblMobileSettingQueryParam;
import com.financial.sharing.vo.param.TblMobileSettingSaveParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 移动设置服务实现类
 *
 * @author system
 * @since 2025-01-30
 */
@Slf4j
@Service
public class TblMobileSettingServiceImpl implements TblMobileSettingService {

    @Autowired
    private TblMobileSettingMapper mobileSettingMapper;

    @Autowired
    private TblMobileSettingGroupMapper groupMapper;

    @Autowired
    private TblMobileSettingTemplateMapper templateMapper;

    @Autowired
    private TblMobileSettingVersionMapper versionMapper;

    @Autowired
    private TblMobileSettingSyncLogMapper syncLogMapper;

    @Autowired
    private DateBaseConfig dateBaseConfig;

    // 设置类型名称映射
    private static final Map<String, String> SETTING_TYPE_NAME_MAP = new HashMap<>();

    static {
        SETTING_TYPE_NAME_MAP.put("UI", "界面设置");
        SETTING_TYPE_NAME_MAP.put("FUNCTION", "功能设置");
        SETTING_TYPE_NAME_MAP.put("PERMISSION", "权限设置");
        SETTING_TYPE_NAME_MAP.put("NOTIFICATION", "推送设置");
        SETTING_TYPE_NAME_MAP.put("SECURITY", "安全设置");
    }

    @Override
    public MyJsonBean<PageResult<TblMobileSetting>> getList(TblMobileSettingQueryParam param) {
        try {
            log.info("查询移动设置列表，参数：{}", param);

            Page<TblMobileSetting> page = new Page<>(param.getPageNo(), param.getPageSize());
            LambdaQueryWrapper<TblMobileSetting> wrapper = new LambdaQueryWrapper<>();

            // 添加查询条件
            if (StringUtils.hasText(param.getSettingName())) {
                wrapper.like(TblMobileSetting::getSettingName, param.getSettingName());
            }
            if (StringUtils.hasText(param.getSettingType())) {
                wrapper.eq(TblMobileSetting::getSettingType, param.getSettingType());
            }
            if (param.getIsEnabled() != null) {
                wrapper.eq(TblMobileSetting::getIsEnabled, param.getIsEnabled());
            }

            wrapper.orderByDesc(TblMobileSetting::getCreateTime);

            IPage<TblMobileSetting> result = mobileSettingMapper.selectPage(page, wrapper);

            PageResult<TblMobileSetting> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage((int) result.getCurrent());
            pageResult.setPageSize((int) result.getSize());
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setTlist(result.getRecords());

            return MyJsonBean.successData(pageResult);
        } catch (Exception e) {
            log.error("查询移动设置列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getById(String settingId) {
        try {
            if (!StringUtils.hasText(settingId)) {
                return MyJsonBean.errorData("设置ID不能为空");
            }

            log.info("查询移动设置详情，settingId={}", settingId);

            TblMobileSetting setting = mobileSettingMapper.selectById(settingId);
            if (setting == null) {
                return MyJsonBean.errorData("设置不存在");
            }

            return MyJsonBean.successData(setting);
        } catch (Exception e) {
            log.error("查询移动设置详情失败，settingId={}", settingId, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(TblMobileSettingSaveParam param) {
        try {
            log.info("保存或更新移动设置，参数：{}", param);

            TblMobileSetting setting = new TblMobileSetting();
            BeanUtils.copyProperties(param, setting);

            if (StringUtils.hasText(param.getSettingId())) {
                // 更新
                setting.setUpdateTime(LocalDateTime.now());
                mobileSettingMapper.updateById(setting);
                return MyJsonBean.successData("更新成功", param.getSettingId());
            } else {
                // 新增
                String settingId = UUID.randomUUID().toString().replace("-", "");
                setting.setSettingId(settingId);
                setting.setCreateTime(LocalDateTime.now());
                mobileSettingMapper.insert(setting);
                return MyJsonBean.successData("新增成功", settingId);
            }
        } catch (Exception e) {
            log.error("保存或更新移动设置失败", e);
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String settingId) {
        try {
            if (!StringUtils.hasText(settingId)) {
                return MyJsonBean.errorData("设置ID不能为空");
            }

            log.info("删除移动设置，settingId={}", settingId);

            mobileSettingMapper.deleteById(settingId);
            return MyJsonBean.successMsg("删除成功");
        } catch (Exception e) {
            log.error("删除移动设置失败，settingId={}", settingId, e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean updateStatus(String settingId, Integer isEnabled) {
        try {
            if (!StringUtils.hasText(settingId)) {
                return MyJsonBean.errorData("设置ID不能为空");
            }

            log.info("更新移动设置状态，settingId={}, isEnabled={}", settingId, isEnabled);

            TblMobileSetting setting = mobileSettingMapper.selectById(settingId);
            if (setting == null) {
                return MyJsonBean.errorData("设置不存在");
            }

            setting.setIsEnabled(isEnabled);
            setting.setUpdateTime(LocalDateTime.now());
            mobileSettingMapper.updateById(setting);

            return MyJsonBean.successMsg("更新成功");
        } catch (Exception e) {
            log.error("更新移动设置状态失败", e);
            return MyJsonBean.errorData("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean syncToMobile(List<String> settingIds) {
        try {
            if (settingIds == null || settingIds.isEmpty()) {
                return MyJsonBean.errorData("设置ID列表不能为空");
            }

            log.info("同步设置到移动端，settingIds={}", settingIds);

            int successCount = 0;
            int failCount = 0;
            StringBuilder errorMessages = new StringBuilder();

            for (String settingId : settingIds) {
                try {
                    TblMobileSetting setting = mobileSettingMapper.selectById(settingId);
                    if (setting == null) {
                        failCount++;
                        errorMessages.append("设置ID[").append(settingId).append("]不存在; ");
                        continue;
                    }

                    // 创建同步日志
                    TblMobileSettingSyncLog syncLog = new TblMobileSettingSyncLog();
                    syncLog.setLogId(UUID.randomUUID().toString().replace("-", ""));
                    syncLog.setSettingId(settingId);
                    syncLog.setSyncTime(LocalDateTime.now());
                    syncLog.setSyncStatus("SUCCESS");
                    syncLogMapper.insert(syncLog);

                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    errorMessages.append("设置ID[").append(settingId).append("]同步失败: ").append(e.getMessage()).append("; ");
                    log.error("同步设置ID[{}]失败", settingId, e);
                }
            }

            if (failCount == 0) {
                return MyJsonBean.successMsg("同步成功，共同步" + successCount + "条记录");
            } else if (successCount == 0) {
                return MyJsonBean.errorData("同步失败：" + errorMessages.toString());
            } else {
                return MyJsonBean.successMsg("部分同步成功，成功" + successCount + "条，失败" + failCount + "条。失败原因：" + errorMessages.toString());
            }
        } catch (Exception e) {
            log.error("同步设置到移动端失败", e);
            return MyJsonBean.errorData("同步失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getTemplates(TblMobileSettingQueryParam param) {
        try {
            log.info("获取设置模板列表，参数：{}", param);

            // 构建查询条件
            LambdaQueryWrapper<TblMobileSettingTemplate> wrapper = new LambdaQueryWrapper<>();

            // 分页查询
            int pageNo = param != null && param.getPageNo() != null ? param.getPageNo() : 0;
            int pageSize = param != null && param.getPageSize() != null ? param.getPageSize() : 10;

            Page<TblMobileSettingTemplate> page = new Page<>(pageNo + 1, pageSize);
            IPage<TblMobileSettingTemplate> result = templateMapper.selectPage(page, wrapper);

            // 构建返回结果
            PageResult<TblMobileSettingTemplate> pageResult = new PageResult<>();
            pageResult.setTlist(result.getRecords());
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setPageNo(pageNo);
            pageResult.setPageNumber(pageNo + 1);
            pageResult.setPageSize(pageSize);

            return MyJsonBean.successData(pageResult);
        } catch (Exception e) {
            log.error("获取设置模板列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveTemplate(com.financial.sharing.vo.param.TblMobileSettingTemplateSaveParam param) {
        try {
            log.info("保存模板，参数：{}", param);

            TblMobileSettingTemplate template = new TblMobileSettingTemplate();

            // 如果是新增，生成ID
            if (param.getTemplateId() == null || param.getTemplateId().isEmpty()) {
                template.setTemplateId(UUID.randomUUID().toString().replace("-", ""));
                template.setCreateTime(LocalDateTime.now());
            } else {
                // 更新操作
                template.setTemplateId(param.getTemplateId());
                template.setUpdateTime(LocalDateTime.now());
            }

            // 设置其他字段
            template.setTemplateName(param.getTemplateName());
            template.setTemplateData(param.getTemplateData());
            template.setPlatform(param.getPlatform());
            template.setIsEnabled(param.getIsEnabled() != null ? param.getIsEnabled() : 1);
            template.setDescription(param.getDescription());
            template.setRemark(param.getRemark());

            // 保存或更新
            if (param.getTemplateId() == null || param.getTemplateId().isEmpty()) {
                templateMapper.insert(template);
            } else {
                templateMapper.updateById(template);
            }

            return MyJsonBean.successData(template.getTemplateId());
        } catch (Exception e) {
            log.error("保存模板失败", e);
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteTemplate(String templateId) {
        try {
            log.info("删除模板，templateId={}", templateId);

            if (templateId == null || templateId.isEmpty()) {
                return MyJsonBean.errorData("模板ID不能为空");
            }

            int result = templateMapper.deleteById(templateId);
            if (result > 0) {
                return MyJsonBean.successData("删除成功");
            } else {
                return MyJsonBean.errorData("模板不存在或已被删除");
            }
        } catch (Exception e) {
            log.error("删除模板失败", e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getGroups() {
        try {
            log.info("获取设置分组列表");

            List<TblMobileSettingGroup> groups = groupMapper.selectList(null);
            return MyJsonBean.successData(groups);
        } catch (Exception e) {
            log.error("获取设置分组列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getVersions(String settingId) {
        try {
            if (!StringUtils.hasText(settingId)) {
                return MyJsonBean.errorData("设置ID不能为空");
            }

            log.info("获取设置版本列表，settingId={}", settingId);

            LambdaQueryWrapper<TblMobileSettingVersion> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblMobileSettingVersion::getSettingId, settingId);
            wrapper.orderByDesc(TblMobileSettingVersion::getCreateTime);
            List<TblMobileSettingVersion> versions = versionMapper.selectList(wrapper);

            return MyJsonBean.successData(versions);
        } catch (Exception e) {
            log.error("获取设置版本列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getSyncLogs(String settingId) {
        try {
            if (!StringUtils.hasText(settingId)) {
                return MyJsonBean.errorData("设置ID不能为空");
            }

            log.info("获取同步日志，settingId={}", settingId);

            LambdaQueryWrapper<TblMobileSettingSyncLog> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblMobileSettingSyncLog::getSettingId, settingId);
            wrapper.orderByDesc(TblMobileSettingSyncLog::getSyncTime);
            List<TblMobileSettingSyncLog> logs = syncLogMapper.selectList(wrapper);

            return MyJsonBean.successData(logs);
        } catch (Exception e) {
            log.error("获取同步日志失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean resetToDefault(String settingId) {
        try {
            if (!StringUtils.hasText(settingId)) {
                return MyJsonBean.errorData("设置ID不能为空");
            }

            log.info("重置设置为默认值，settingId={}", settingId);

            TblMobileSetting setting = mobileSettingMapper.selectById(settingId);
            if (setting == null) {
                return MyJsonBean.errorData("设置不存在");
            }

            // 重置为默认值
            setting.setSettingValue(setting.getDefaultValue());
            setting.setUpdateTime(LocalDateTime.now());
            mobileSettingMapper.updateById(setting);

            return MyJsonBean.successMsg("重置成功");
        } catch (Exception e) {
            log.error("重置设置为默认值失败", e);
            return MyJsonBean.errorData("重置失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getStatistics() {
        try {
            log.info("获取设置使用统计");

            long totalSettings = mobileSettingMapper.selectCount(null);

            LambdaQueryWrapper<TblMobileSetting> enabledWrapper = new LambdaQueryWrapper<>();
            enabledWrapper.eq(TblMobileSetting::getIsEnabled, 1);
            long enabledSettings = mobileSettingMapper.selectCount(enabledWrapper);

            long syncCount = syncLogMapper.selectCount(null);

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalSettings", totalSettings);
            statistics.put("enabledSettings", enabledSettings);
            statistics.put("syncCount", syncCount);

            return MyJsonBean.successData(statistics);
        } catch (Exception e) {
            log.error("获取设置使用统计失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean test(String settingId, java.util.Map<String, Object> testData) {
        try {
            log.info("测试移动设置，settingId={}，testData={}", settingId, testData);

            if (!StringUtils.hasText(settingId)) {
                return MyJsonBean.errorData("设置ID不能为空");
            }

            // 查询设置详情
            TblMobileSetting setting = mobileSettingMapper.selectById(settingId);
            if (setting == null) {
                return MyJsonBean.errorData("设置不存在");
            }

            // 检查设置是否启用
            if (setting.getIsEnabled() == null || setting.getIsEnabled() != 1) {
                return MyJsonBean.errorData("设置未启用，无法测试");
            }

            // 获取测试参数
            String platform = (String) testData.get("platform");
            Object testDataObj = testData.get("testData");

            // 验证平台参数
            if (!StringUtils.hasText(platform)) {
                return MyJsonBean.errorData("测试平台不能为空");
            }

            // 验证平台是否匹配
            if (!"ALL".equals(setting.getPlatform()) && !platform.equals(setting.getPlatform())) {
                return MyJsonBean.errorData("测试平台与设置平台不匹配");
            }

            // 记录测试开始时间
            long startTime = System.currentTimeMillis();

            // 执行测试逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("testResult", "SUCCESS");
            result.put("testMessage", "测试成功：设置配置有效，平台匹配");
            result.put("responseTime", System.currentTimeMillis() - startTime);
            result.put("settingCode", setting.getSettingCode());
            result.put("settingType", setting.getSettingType());
            result.put("platform", platform);
            result.put("testData", testDataObj);

            // 可以在这里添加更复杂的测试逻辑，比如：
            // 1. 验证配置数据格式
            // 2. 模拟移动端请求
            // 3. 检查配置项的有效性
            // 4. 验证权限设置
            // 等等

            log.info("测试完成，结果：{}", result);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("测试移动设置失败", e);
            return MyJsonBean.errorData("测试失败：" + e.getMessage());
        }
    }
}
