package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.imports.ImportBusinessSystemDTO;
import com.global.treasurer.dto.imports.ImportResultDTO;
import com.global.treasurer.entity.TcBusinessSystem;
import com.global.treasurer.mapper.TcBusinessSystemMapper;
import com.global.treasurer.service.TcBusinessSystemService;
import com.global.treasurer.util.excel.ExcelImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 业务系统注册Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
@Service
public class TcBusinessSystemServiceImpl extends ServiceImpl<TcBusinessSystemMapper, TcBusinessSystem>
        implements TcBusinessSystemService {
    private static final Logger log = LoggerFactory.getLogger(TcBusinessSystemServiceImpl.class);

    @Autowired
    private TcBusinessSystemMapper tcBusinessSystemMapper;

    @Override
    public IPage<TcBusinessSystem> getBusinessSystemPage(Integer page, Integer limit, String systemCode,
                                                          String systemName, String systemType, String connectionStatus, String status) {
        log.info("查询业务系统列表 - page: {}, limit: {}, systemCode: {}, systemName: {}, systemType: {}",
                page, limit, systemCode, systemName, systemType);

        // 使用 PageHelper 进行分页（项目已禁用 MyBatis-Plus 分页插件）
        PageHelper.startPage(page, limit);

        // 调用自定义的条件查询方法
        List<TcBusinessSystem> list = tcBusinessSystemMapper.selectByCondition(
                systemCode, systemName, systemType, connectionStatus, status);

        // 为每条记录设置默认值，确保前端显示正常
        for (TcBusinessSystem system : list) {
            if (!StringUtils.hasText(system.getStatus())) {
                system.setStatus("1"); // 默认启用
            }
            if (!StringUtils.hasText(system.getConnectionStatus())) {
                system.setConnectionStatus("OFFLINE"); // 默认离线
            }
            if (!StringUtils.hasText(system.getAuthConfig())) {
                system.setAuthConfig("{}");
            }
        }

        // 获取 PageInfo 并转换为 IPage
        PageInfo<TcBusinessSystem> pageInfo = new PageInfo<>(list);

        // 手动构造 IPage 对象返回
        Page<TcBusinessSystem> result = new Page<>(page, limit, pageInfo.getTotal());
        result.setRecords(list);
        result.setPages(pageInfo.getPages());

        return result;
    }

    @Override
    public void syncSystemStatus() {
        // TODO: 实现系统状态同步逻辑
    }

    @Override
    public boolean testSystemConnection(Long id) {
        TcBusinessSystem system = this.getById(id);
        if (system == null) {
            log.warn("业务系统不存在, ID: {}", id);
            return false;
        }

        // TODO: 实现系统连接测试逻辑
        // 这里应该根据系统配置测试实际的连接
        // 例如: 测试URL可达性、数据库连接、接口调用等

        // 临时返回true,实际应该测试真实的连接
        // 如果配置了API地址，可以尝试简单的连接测试
        if (StringUtils.hasText(system.getApiUrl())) {
            try {
                // 这里可以添加实际的HTTP连接测试
                // 简单的示例：检查URL格式是否正确
                log.info("测试系统连接 - 系统: {}, API地址: {}", system.getSystemName(), system.getApiUrl());
                return true;
            } catch (Exception e) {
                log.error("测试系统连接失败 - 系统: {}", system.getSystemName(), e);
                return false;
            }
        }

        log.info("测试系统连接 - 系统: {}, 未配置API地址", system.getSystemName());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportResultDTO importBusinessSystems(MultipartFile file, String currentUser) {
        ImportResultDTO result = new ImportResultDTO();

        try (ExcelImport excelImport = new ExcelImport(file, 1, 0)) {
            // 解析Excel数据，从第2行开始（第1行是标题）
            List<ImportBusinessSystemDTO> dataList = excelImport.getDataList(ImportBusinessSystemDTO.class);

            result.setTotal(dataList.size());
            int successCount = 0;
            int failCount = 0;
            List<TcBusinessSystem> toSaveList = new ArrayList<>();

            // 遍历数据进行验证和转换
            for (int i = 0; i < dataList.size(); i++) {
                ImportBusinessSystemDTO dto = dataList.get(i);
                int rowNum = i + 2; // Excel行号从2开始（第1行是标题）

                // 数据验证
                String validateError = dto.validate();
                if (validateError != null) {
                    result.addError(rowNum, validateError);
                    failCount++;
                    continue;
                }

                // 检查系统编码是否已存在
                if (existsBySystemCode(dto.getSystemCode())) {
                    result.addError(rowNum, "系统编码已存在: " + dto.getSystemCode());
                    failCount++;
                    continue;
                }

                // 转换为实体对象
                TcBusinessSystem entity = convertToEntity(dto, currentUser);
                toSaveList.add(entity);
                successCount++;
            }

            // 批量保存
            if (!toSaveList.isEmpty()) {
                this.saveBatch(toSaveList);
            }

            result.setSuccessCount(successCount);
            result.setFailCount(failCount);

            log.info("业务系统导入完成 - 总数: {}, 成功: {}, 失败: {}",
                    result.getTotal(), successCount, failCount);

        } catch (Exception e) {
            log.error("导入业务系统失败", e);
            throw new RuntimeException("导入失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public boolean existsBySystemCode(String systemCode) {
        if (!StringUtils.hasText(systemCode)) {
            return false;
        }
        LambdaQueryWrapper<TcBusinessSystem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TcBusinessSystem::getSystemCode, systemCode);
        return this.count(queryWrapper) > 0;
    }

    /**
     * 将导入DTO转换为实体对象
     */
    private TcBusinessSystem convertToEntity(ImportBusinessSystemDTO dto, String currentUser) {
        TcBusinessSystem entity = new TcBusinessSystem();
        entity.setId(System.currentTimeMillis() + (long)(Math.random() * 1000));
        entity.setSystemName(dto.getSystemName().trim());
        entity.setSystemCode(dto.getSystemCode().trim());
        entity.setSystemType(dto.getSystemType().trim());
        entity.setApiUrl(dto.getApiUrl().trim());
        entity.setAuthType(dto.getAuthType().trim());
        entity.setSystemDesc(dto.getDescription() != null ? dto.getDescription().trim() : null);
        entity.setAuthConfig("{}");
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setCreateUser(currentUser);
        entity.setUpdateUser(currentUser);
        return entity;
    }
}
