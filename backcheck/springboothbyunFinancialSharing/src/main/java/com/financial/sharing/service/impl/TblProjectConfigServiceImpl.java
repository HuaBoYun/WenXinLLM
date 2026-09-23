package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.entity.TblProjectConfig;
import com.financial.sharing.mapper.TblProjectConfigMapper;
import com.financial.sharing.service.TblProjectConfigService;
import com.financial.sharing.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * 项目配置Service实现
 */
@Slf4j
@Service
public class TblProjectConfigServiceImpl extends ServiceImpl<TblProjectConfigMapper, TblProjectConfig> 
        implements TblProjectConfigService {

    @Override
    public PageResult<TblProjectConfig> getProjectConfigPage(Map<String, Object> params) {
        try {
            int pageNum = params.containsKey("pageNum") ? (int) params.get("pageNum") : 1;
            int pageSize = params.containsKey("pageSize") ? (int) params.get("pageSize") : 15;

            QueryWrapper<TblProjectConfig> queryWrapper = new QueryWrapper<>();

            // 项目名称模糊查询
            if (params.containsKey("projectName") && params.get("projectName") != null) {
                queryWrapper.like("PROJECT_NAME", params.get("projectName"));
            }

            // 项目状态查询
            if (params.containsKey("projectStatus") && params.get("projectStatus") != null) {
                queryWrapper.eq("PROJECT_STATUS", params.get("projectStatus"));
            }

            // 项目经理名称模糊查询
            if (params.containsKey("projectManager") && params.get("projectManager") != null) {
                queryWrapper.like("PROJECT_MANAGER_NAME", params.get("projectManager"));
            }

            // 项目经理ID查询
            if (params.containsKey("projectManagerId") && params.get("projectManagerId") != null) {
                queryWrapper.eq("PROJECT_MANAGER_ID", params.get("projectManagerId"));
            }

            // 启用状态查询
            if (params.containsKey("isEnabled") && params.get("isEnabled") != null) {
                queryWrapper.eq("IS_ENABLED", params.get("isEnabled"));
            }

            queryWrapper.orderByDesc("CREATE_TIME");

            // 使用MyBatis-Plus的分页查询
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<TblProjectConfig> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
            this.page(page, queryWrapper);

            PageResult<TblProjectConfig> result = new PageResult<>();
            result.setTlist(page.getRecords());
            result.setTotalRecord((int) page.getTotal());
            result.setCurrentPage((int) page.getCurrent());
            result.setPageSize((int) page.getSize());

            return result;
        } catch (Exception e) {
            log.error("查询项目配置分页数据失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    @Override
    public TblProjectConfig getByProjectCode(String projectCode) {
        return this.baseMapper.selectByProjectCode(projectCode);
    }

    @Override
    public List<TblProjectConfig> getByProjectManagerId(String projectManagerId) {
        return this.baseMapper.selectByProjectManagerId(projectManagerId);
    }

    @Override
    public List<TblProjectConfig> getByDepartmentId(String departmentId) {
        return this.baseMapper.selectByDepartmentId(departmentId);
    }

    @Override
    public List<TblProjectConfig> getByProjectStatus(String projectStatus) {
        return this.baseMapper.selectByProjectStatus(projectStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProjectStatus(String projectId, String projectStatus) {
        try {
            TblProjectConfig config = this.getById(projectId);
            if (config == null) {
                throw new RuntimeException("项目不存在");
            }
            config.setProjectStatus(projectStatus);
            config.setUpdateTime(java.time.LocalDateTime.now());
            return this.updateById(config);
        } catch (Exception e) {
            log.error("更新项目状态失败", e);
            throw new RuntimeException("更新失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getProjectStatistics(String projectId) {
        try {
            TblProjectConfig config = this.getById(projectId);
            if (config == null) {
                throw new RuntimeException("项目不存在");
            }
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("projectId", projectId);
            statistics.put("projectName", config.getProjectName());
            statistics.put("budgetAmount", config.getBudgetAmount());
            statistics.put("usedAmount", config.getUsedAmount());
            statistics.put("remainAmount", config.getBudgetAmount().subtract(config.getUsedAmount()));
            statistics.put("usageRate", config.getBudgetAmount().doubleValue() > 0 ? 
                    (config.getUsedAmount().doubleValue() / config.getBudgetAmount().doubleValue() * 100) : 0);
            
            return statistics;
        } catch (Exception e) {
            log.error("获取项目统计信息失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }
}

