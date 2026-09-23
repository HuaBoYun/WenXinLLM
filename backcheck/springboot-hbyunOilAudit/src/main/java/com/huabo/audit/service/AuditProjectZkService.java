package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.AuditProjectZkEntity;

import java.math.BigDecimal;

/**
 * @author Rui
 * @InterfaceName AuditProjectZkService
 * @Description
 * @DATE 2023/10/17
 */
public interface AuditProjectZkService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String projectName, BigDecimal money) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(AuditProjectZkEntity auditProjectZkEntity) throws Exception;

    void saveEntity(String token, AuditProjectZkEntity auditProjectZkEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;
}
