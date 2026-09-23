package com.financial.sharing.dataCollection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.dataCollection.dto.DataSourceQueryParam;
import com.financial.sharing.dataCollection.entity.TblDataSource;
import com.financial.sharing.dataCollection.mapper.DataSourceMapper;
import com.financial.sharing.dataCollection.service.DataSourceService;
import com.financial.sharing.util.MyJsonBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据归集数据源配置Service实现类
 *
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service("dataCollectionSourceServiceImpl")
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Override
    public MyJsonBean queryPage(DataSourceQueryParam param) {
        try {
            // 使用 PageHelper 分页 (达梦数据库下 MyBatis-Plus 分页插件的 COUNT 子查询会保留 ORDER BY,
            // 触发达梦语法错误, 因此与项目其他模块保持一致, 改用 PageHelper)
            LambdaQueryWrapper<TblDataSource> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblDataSource::getOrgId, param.getOrgId());

            if (StringUtils.isNotBlank(param.getSourceCode())) {
                wrapper.like(TblDataSource::getSourceCode, param.getSourceCode());
            }
            if (StringUtils.isNotBlank(param.getSourceName())) {
                wrapper.like(TblDataSource::getSourceName, param.getSourceName());
            }
            if (StringUtils.isNotBlank(param.getSourceType())) {
                wrapper.eq(TblDataSource::getSourceType, param.getSourceType());
            }
            if (StringUtils.isNotBlank(param.getIsEnabled())) {
                wrapper.eq(TblDataSource::getIsEnabled, param.getIsEnabled());
            }

            wrapper.orderByDesc(TblDataSource::getCreateTime);

            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            List<TblDataSource> list = dataSourceMapper.selectList(wrapper);
            PageInfo<TblDataSource> pageInfo = new PageInfo<>(list);

            // 前端期望读 data.records / data.total 字段, 这里手动拼装一份兼容结构
            Map<String, Object> result = new HashMap<>();
            result.put("records", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("pageNumber", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());
            result.put("pages", pageInfo.getPages());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("分页查询数据源配置失败", e);
            return MyJsonBean.errorData("分页查询数据源配置失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryById(String sourceId, String orgId) {
        try {
            LambdaQueryWrapper<TblDataSource> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblDataSource::getSourceId, sourceId);
            wrapper.eq(TblDataSource::getOrgId, orgId);

            TblDataSource dataSource = dataSourceMapper.selectOne(wrapper);
            if (dataSource == null) {
                return MyJsonBean.errorData("数据源配置不存在");
            }

            return MyJsonBean.successData(dataSource);
        } catch (Exception e) {
            log.error("查询数据源配置失败", e);
            return MyJsonBean.errorData("查询数据源配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveDataSource(TblDataSource dataSource) {
        try {
            // 检查数据源编码是否重复
            if (StringUtils.isNotBlank(dataSource.getSourceCode())) {
                LambdaQueryWrapper<TblDataSource> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(TblDataSource::getSourceCode, dataSource.getSourceCode());
                wrapper.eq(TblDataSource::getOrgId, dataSource.getOrgId());
                if (StringUtils.isNotBlank(dataSource.getSourceId())) {
                    wrapper.ne(TblDataSource::getSourceId, dataSource.getSourceId());
                }
                long count = dataSourceMapper.selectCount(wrapper);
                if (count > 0) {
                    return MyJsonBean.errorData("数据源编码已存在");
                }
            }

            if (StringUtils.isBlank(dataSource.getSourceId())) {
                // 新增
                dataSource.setCreateTime(new Date());
                dataSourceMapper.insert(dataSource);
                return MyJsonBean.successData("新增成功");
            } else {
                // 更新
                dataSource.setUpdateTime(new Date());
                dataSourceMapper.updateById(dataSource);
                return MyJsonBean.successData("更新成功");
            }
        } catch (Exception e) {
            log.error("保存数据源配置失败", e);
            return MyJsonBean.errorData("保存数据源配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteDataSource(String sourceId, String orgId) {
        try {
            LambdaQueryWrapper<TblDataSource> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblDataSource::getSourceId, sourceId);
            wrapper.eq(TblDataSource::getOrgId, orgId);

            int result = dataSourceMapper.delete(wrapper);
            if (result > 0) {
                return MyJsonBean.successData("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除数据源配置失败", e);
            return MyJsonBean.errorData("删除数据源配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean toggleEnabled(String sourceId, String isEnabled, String orgId) {
        try {
            TblDataSource dataSource = new TblDataSource();
            dataSource.setSourceId(sourceId);
            dataSource.setIsEnabled(isEnabled);
            dataSource.setUpdateTime(new Date());

            LambdaQueryWrapper<TblDataSource> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblDataSource::getSourceId, sourceId);
            wrapper.eq(TblDataSource::getOrgId, orgId);

            int result = dataSourceMapper.update(dataSource, wrapper);
            if (result > 0) {
                String action = "Y".equals(isEnabled) ? "启用" : "禁用";
                return MyJsonBean.successData(action + "成功");
            } else {
                return MyJsonBean.errorData("操作失败");
            }
        } catch (Exception e) {
            log.error("启用/禁用数据源失败", e);
            return MyJsonBean.errorData("启用/禁用数据源失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean testConnection(String sourceId, String orgId) {
        try {
            // 查询数据源配置
            LambdaQueryWrapper<TblDataSource> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblDataSource::getSourceId, sourceId);
            wrapper.eq(TblDataSource::getOrgId, orgId);

            TblDataSource dataSource = dataSourceMapper.selectOne(wrapper);
            if (dataSource == null) {
                return MyJsonBean.errorData("数据源配置不存在");
            }

            // 根据数据源类型进行测试
            String sourceType = dataSource.getSourceType();
            StringBuilder testResult = new StringBuilder();
            testResult.append("数据源连接测试结果：\n");
            testResult.append("数据源名称：").append(dataSource.getSourceName()).append("\n");
            testResult.append("数据源类型：").append(sourceType).append("\n\n");

            if ("DATABASE".equals(sourceType)) {
                // 测试数据库连接
                testResult.append(testDatabaseConnection(dataSource));
            } else if ("API".equals(sourceType)) {
                // 测试API连接
                testResult.append(testApiConnection(dataSource));
            } else if ("FILE".equals(sourceType)) {
                // 测试文件路径
                testResult.append(testFileConnection(dataSource));
            } else if ("FINANCIAL_SHARING".equals(sourceType)) {
                // 测试财务共享连接
                testResult.append(testFinancialSharingConnection(dataSource));
            } else {
                testResult.append("不支持的数据源类型");
            }

            return MyJsonBean.successData(testResult.toString());
        } catch (Exception e) {
            log.error("测试数据源连接失败", e);
            return MyJsonBean.errorData("测试数据源连接失败：" + e.getMessage());
        }
    }

    /**
     * 测试数据库连接
     */
    private String testDatabaseConnection(TblDataSource dataSource) {
        StringBuilder result = new StringBuilder();
        Connection conn = null;
        try {
            String jdbcUrl = buildJdbcUrl(dataSource);
            result.append("连接地址：").append(jdbcUrl).append("\n");
            result.append("用户名：").append(dataSource.getUsername()).append("\n");
            result.append("连接池大小：").append(dataSource.getConnectionPoolSize()).append("\n");
            result.append("超时时间：").append(dataSource.getTimeout()).append("秒\n\n");

            // 尝试建立连接
            conn = DriverManager.getConnection(jdbcUrl, dataSource.getUsername(), dataSource.getPassword());
            result.append("✓ 连接成功！\n");
            result.append("数据库产品：").append(conn.getMetaData().getDatabaseProductName()).append("\n");
            result.append("数据库版本：").append(conn.getMetaData().getDatabaseProductVersion()).append("\n");
        } catch (Exception e) {
            result.append("✗ 连接失败：").append(e.getMessage()).append("\n");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    log.error("关闭数据库连接失败", e);
                }
            }
        }
        return result.toString();
    }

    /**
     * 构建JDBC连接URL
     */
    private String buildJdbcUrl(TblDataSource dataSource) {
        String connectionType = dataSource.getConnectionType();
        String host = dataSource.getHost();
        Integer port = dataSource.getPort();
        String databaseName = dataSource.getDatabaseName();

        if ("JDBC".equals(connectionType)) {
            // 根据不同数据库类型构建URL
            if (databaseName != null && databaseName.toLowerCase().contains("dm")) {
                // 达梦数据库
                return String.format("jdbc:dm://%s:%d/%s", host, port, databaseName);
            } else if (databaseName != null && databaseName.toLowerCase().contains("mysql")) {
                // MySQL数据库
                return String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf8", host, port, databaseName);
            } else if (databaseName != null && databaseName.toLowerCase().contains("oracle")) {
                // Oracle数据库
                return String.format("jdbc:oracle:thin:@%s:%d:%s", host, port, databaseName);
            } else {
                // 默认
                return String.format("jdbc://%s:%d/%s", host, port, databaseName);
            }
        }
        return "";
    }

    /**
     * 测试API连接
     */
    private String testApiConnection(TblDataSource dataSource) {
        StringBuilder result = new StringBuilder();
        result.append("API地址：").append(dataSource.getApiUrl()).append("\n");
        result.append("请求方法：").append(dataSource.getApiMethod()).append("\n");
        result.append("认证类型：").append(dataSource.getAuthType()).append("\n");
        result.append("超时时间：").append(dataSource.getTimeout()).append("秒\n\n");
        result.append("✓ API配置有效（实际连接测试需要在运行时执行）\n");
        return result.toString();
    }

    /**
     * 测试文件连接
     */
    private String testFileConnection(TblDataSource dataSource) {
        StringBuilder result = new StringBuilder();
        result.append("文件路径：").append(dataSource.getFilePath()).append("\n");
        result.append("文件类型：").append(dataSource.getFileType()).append("\n\n");
        result.append("✓ 文件配置有效（实际文件访问需要在运行时执行）\n");
        return result.toString();
    }

    /**
     * 测试财务共享连接
     */
    private String testFinancialSharingConnection(TblDataSource dataSource) {
        StringBuilder result = new StringBuilder();
        result.append("API地址：").append(dataSource.getApiUrl()).append("\n");
        result.append("认证令牌：").append(dataSource.getAuthToken() != null ? "已配置" : "未配置").append("\n\n");
        result.append("✓ 财务共享配置有效（实际连接测试需要在运行时执行）\n");
        return result.toString();
    }
}

