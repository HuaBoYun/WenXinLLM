package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.AuditSuggestion2LEntity;
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;

import java.util.List;

/**
 * @author Rui
 * @InterfaceName AuditSuggestion2LService
 * @Description
 * @DATE 2023/9/23
 */
public interface AuditSuggestion2LService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String org, String name) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(AuditSuggestion2LEntity auditSuggestion2LEntity) throws Exception;

    void saveEntity(String token, AuditSuggestion2LEntity auditSuggestion2LEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;

    /**
     * 通过ids查询 二级机构人中立项建议
     *
     * @param ids
     * @return
     * @throws Exception
     */
    List<AuditSuggestion2LEntity> findByIds(String ids)  ;

	JsonBean findListDraftPlan(String token, Integer pageNumber, Integer pageSize, String org, String name) throws Exception;

}
