package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.LeaveAudit3LXFEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectXFEntity;
import com.huabo.audit.oracle.entity.TblYqnsGzfa;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author GJ.C
 * @CLASS_NAME: LeaveAudit3LXFEntityService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企模块-计划编制-央企模块-计划编制-三级单位离任审计 下发-分发表
 */
public interface LeaveAudit3LXFEntityService extends IService<LeaveAudit3LXFEntity> {


    /**
     * 根据三级单位离任审计 -分发表id查询三级单位离任审计 -分发表
     *
     * @param auditId
     * @return
     * @throws Exception
     */
    JsonBean getListByAuditId(String token, String auditId) throws Exception;


    /**
     * 保存或更新三级单位离任审计 -分发表
     *
     * @param auditId 1
     * @param userIds 1,2,
     * @return
     */
    JsonBean saveOrUpdate(String token,  String auditId , String userIds) throws Exception;


    /**
     * 删除所有分发人员
     *
     * @param auditId 1
     * @return
     */
    JsonBean delete(String token,  String auditId ) throws Exception;





}
