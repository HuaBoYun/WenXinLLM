package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.InvestigationEntity;

/**
 * @author Rui
 * @InterfaceName InvestigationService
 * @Description
 * @DATE 2023/10/01
 */
public interface InvestigationService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String planNo, String projectName) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(InvestigationEntity investigationEntity) throws Exception;

    void saveEntity(String token, InvestigationEntity investigationEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;
}
