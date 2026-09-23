package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.QualityResultEntity;

/**
 * @author Rui
 * @ClassName QualityResultService
 * @Description
 * @DATE 2023/10/16
 */
public interface QualityResultService {
    void updateEntity(QualityResultEntity qualityResultEntity) throws Exception;

    JsonBean findById(String id) throws Exception;

    void saveEntity(String token, QualityResultEntity qualityResultEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;

    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String projectName) throws Exception;
}
