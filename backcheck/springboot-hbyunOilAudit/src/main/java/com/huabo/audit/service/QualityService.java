package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.QualityEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @InterfaceName QualityService
 * @Description
 * @DATE 2023/10/12
 */
public interface QualityService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String projectName, Integer type) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(QualityEntity qualityEntity) throws Exception;

    void saveEntity(String token, QualityEntity qualityEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;

    List<QualityEntity> selectQualityByProjectId(BigDecimal projectId);
}
