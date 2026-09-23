package com.huabo.audit.oracle.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.dto.TblYqnsAuditProjectDto;

/**
 * @program: springboot-hbyunMonitor
 * @description: 审计实施---审计项目情况表
 * @author: WangZhenDong
 * @create: 2023-10-07 21:35
 **/
public interface AuditProjectService {
    /**
     * 查询审计项目情况列表
     * @param pageNumber
     * @param pageSize
     * @param projectName
     * @return
     */
    JsonBean getProjectList(String token,Integer pageNumber, Integer pageSize, String name,String auditUnitId,String sceneApproveStaerTime,String sceneApproveEndTime) throws Exception;

    /**
     * 根据审计项目id查询审计项目记录
     * @param projectId
     * @return
     */
    JsonBean getProjectById(String token,Long projectId) throws Exception;

    /**
     * 保存或更新审计项目情况
     * @param param
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsAuditProjectDto param) throws Exception;

    /**
     * 删除审计项目情况
     * @param id
     * @return
     */
    JsonBean delete(String token,Integer id) throws Exception;
}

