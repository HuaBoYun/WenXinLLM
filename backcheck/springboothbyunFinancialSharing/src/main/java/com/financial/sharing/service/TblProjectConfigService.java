package com.financial.sharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.entity.TblProjectConfig;
import com.financial.sharing.util.PageResult;
import java.util.List;
import java.util.Map;

/**
 * 项目配置Service接口
 */
public interface TblProjectConfigService extends IService<TblProjectConfig> {
    
    /**
     * 分页查询项目配置
     */
    PageResult<TblProjectConfig> getProjectConfigPage(Map<String, Object> params);
    
    /**
     * 根据项目编码查询
     */
    TblProjectConfig getByProjectCode(String projectCode);
    
    /**
     * 根据项目经理ID查询
     */
    List<TblProjectConfig> getByProjectManagerId(String projectManagerId);
    
    /**
     * 根据部门ID查询
     */
    List<TblProjectConfig> getByDepartmentId(String departmentId);
    
    /**
     * 根据项目状态查询
     */
    List<TblProjectConfig> getByProjectStatus(String projectStatus);
    
    /**
     * 更新项目状态
     */
    boolean updateProjectStatus(String projectId, String projectStatus);
    
    /**
     * 获取项目统计信息
     */
    Map<String, Object> getProjectStatistics(String projectId);
}

