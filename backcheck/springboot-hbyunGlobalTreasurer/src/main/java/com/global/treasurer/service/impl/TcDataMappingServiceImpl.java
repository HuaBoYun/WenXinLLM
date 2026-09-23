package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcDataMapping;
import com.global.treasurer.mapper.TcDataMappingMapper;
import com.global.treasurer.service.TcDataMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据映射配置Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
@Service
public class TcDataMappingServiceImpl extends ServiceImpl<TcDataMappingMapper, TcDataMapping>
        implements TcDataMappingService {
    private static final Logger log = LoggerFactory.getLogger(TcDataMappingServiceImpl.class);

    @Autowired
    private TcDataMappingMapper tcDataMappingMapper;

    @Override
    public IPage<TcDataMapping> getDataMappingPage(Integer page, Integer limit, String mappingName,
                                                   String sourceSystem, String targetSystem,
                                                   String mappingType, Integer status) {
        log.info("查询数据映射配置列表 - page: {}, limit: {}, mappingName: {}, sourceSystem: {}, targetSystem: {}, mappingType: {}, status: {}",
                page, limit, mappingName, sourceSystem, targetSystem, mappingType, status);

        // 使用 PageHelper 进行分页（项目已禁用 MyBatis-Plus 分页插件）
        PageHelper.startPage(page, limit);

        // 调用自定义的条件查询方法，传递所有查询参数
        List<TcDataMapping> list = tcDataMappingMapper.selectListWithConditions(
                mappingName,
                sourceSystem,
                targetSystem,
                mappingType,
                null,  // sourceField
                null,  // targetField
                null,  // fieldType
                status != null ? status.toString() : null
        );

        // 获取 PageInfo 并转换为 IPage
        PageInfo<TcDataMapping> pageInfo = new PageInfo<>(list);

        // 手动构造 IPage 对象返回
        Page<TcDataMapping> result = new Page<>(page, limit, pageInfo.getTotal());
        result.setRecords(list);
        result.setPages(pageInfo.getPages());

        return result;
    }

    @Override
    public List<TcDataMapping> getBySystemId(String systemId) {
        // 注意：表结构已更新，SYSTEM_ID字段已拆分为SOURCE_SYSTEM和TARGET_SYSTEM
        // 此方法查询源系统或目标系统匹配的映射配置
        QueryWrapper<TcDataMapping> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper
                .eq("SOURCE_SYSTEM", systemId)
                .or()
                .eq("TARGET_SYSTEM", systemId)
        );
        queryWrapper.orderByAsc("SORT_ORDER");
        return this.list(queryWrapper);
    }

    @Override
    public Map<String, Object> refreshMapping() {
        log.info("开始刷新数据映射配置...");

        Map<String, Object> result = new HashMap<>();
        int totalCount = 0;
        int updatedCount = 0;
        int invalidCount = 0;
        int activeCount = 0;

        try {
            // 1. 获取所有映射配置
            List<TcDataMapping> allMappings = this.list();
            totalCount = allMappings.size();

            for (TcDataMapping mapping : allMappings) {
                try {
                    // 2. 检查映射配置的有效性
                    boolean isValid = validateMapping(mapping);

                    if (isValid) {
                        // 3. 更新映射配置的刷新时间
                        mapping.setUpdateTime(new Date());

                        // 确保状态为启用（status是String类型）
                        if (mapping.getStatus() == null || !"1".equals(mapping.getStatus())) {
                            mapping.setStatus("1");
                        }

                        this.updateById(mapping);
                        updatedCount++;
                        activeCount++;
                    } else {
                        // 4. 标记失效的映射规则
                        mapping.setStatus("0"); // 设置为禁用状态
                        mapping.setUpdateTime(new Date());
                        mapping.setRemark(mapping.getRemark() != null ?
                            mapping.getRemark() + " [刷新时标记为失效]" : "[刷新时标记为失效]");

                        this.updateById(mapping);
                        invalidCount++;
                    }
                } catch (Exception e) {
                    log.warn("刷新映射配置失败: id={}, error={}", mapping.getId(), e.getMessage());
                    invalidCount++;
                }
            }

            result.put("success", true);
            result.put("totalCount", totalCount);
            result.put("updatedCount", updatedCount);
            result.put("invalidCount", invalidCount);
            result.put("activeCount", activeCount);
            result.put("refreshTime", new Date());
            result.put("message", String.format("刷新完成：共%d条，有效%d条，失效%d条",
                totalCount, activeCount, invalidCount));

            log.info("数据映射配置刷新完成: total={}, updated={}, invalid={}",
                totalCount, updatedCount, invalidCount);

        } catch (Exception e) {
            log.error("刷新数据映射配置失败", e);
            result.put("success", false);
            result.put("message", "刷新失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 验证映射配置的有效性
     * 检查源字段、目标字段、映射规则等是否有效
     *
     * @param mapping 映射配置
     * @return 是否有效
     */
    private boolean validateMapping(TcDataMapping mapping) {
        // 检查必要字段是否存在
        if (mapping.getMappingName() == null || mapping.getMappingName().trim().isEmpty()) {
            return false;
        }

        // 检查源字段和目标字段
        if (mapping.getSourceField() == null || mapping.getSourceField().trim().isEmpty()) {
            return false;
        }

        if (mapping.getTargetField() == null || mapping.getTargetField().trim().isEmpty()) {
            return false;
        }

        // 检查映射类型
        String mappingType = mapping.getMappingType();
        if (mappingType != null) {
            // 验证映射类型是否为支持的类型
            if (!isValidMappingType(mappingType)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 验证映射类型是否有效
     *
     * @param mappingType 映射类型
     * @return 是否有效
     */
    private boolean isValidMappingType(String mappingType) {
        if (mappingType == null || mappingType.trim().isEmpty()) {
            return false;
        }

        // 支持的映射类型列表
        String[] validTypes = {
            "DIRECT",       // 直接映射
            "TRANSFORM",    // 转换映射
            "CALCULATE",    // 计算映射
            "LOOKUP",       // 查找映射
            "CUSTOM"        // 自定义映射
        };

        for (String validType : validTypes) {
            if (validType.equals(mappingType)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean existsByMappingCode(String mappingCode) {
        if (!StringUtils.hasText(mappingCode)) {
            return false;
        }
        QueryWrapper<TcDataMapping> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("MAPPING_CODE", mappingCode);
        return this.count(queryWrapper) > 0;
    }
}
