package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.ProjectEvaluationEntity;

import java.math.BigDecimal;

/**
 * @author zkl
 * @InterfaceName ProjectEvaluationService
 * @Description
 * @DATE 2024/04/13
 */
public interface ProjectEvaluationService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String projectName) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(ProjectEvaluationEntity entity) throws Exception;

    void saveEntity(String token, ProjectEvaluationEntity entity) throws Exception;

    void deleteByIds(String ids) throws Exception;
}
