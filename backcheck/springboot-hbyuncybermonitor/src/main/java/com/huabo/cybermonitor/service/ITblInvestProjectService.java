package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblInvestProject;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblInvestProjectQueryVO;

import java.util.Map;

/**
 * 投资项目台账服务接口 - 投资穿透式监管
 */
public interface ITblInvestProjectService extends IService<TblInvestProject> {

    /**
     * 分页查询投资项目
     */
    PageResult<TblInvestProject> selectByPage(TblInvestProjectQueryVO queryVO);

    /**
     * 新增投资项目
     */
    boolean addProject(TblInvestProject project);

    /**
     * 更新投资项目
     */
    boolean updateProject(TblInvestProject project);

    /**
     * 删除投资项目
     */
    boolean deleteProject(String projectId);

    /**
     * 获取投资统计数据
     */
    Map<String, Object> getStatistics(String companyId);
}

