package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.QualityEvaluateEntity;

/**
 * @author Rui
 * @InterfaceName QualityEvaluateService
 * @Description
 * @DATE 2023/10/10
 */
public interface QualityEvaluateService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String name, Integer status) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(QualityEvaluateEntity qualityEvaluateEntity) throws Exception;

    void saveEntity(String token, QualityEvaluateEntity qualityEvaluateEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;

    void updateEntityStatus(QualityEvaluateEntity qualityEvaluateEntity) throws Exception;
}
