package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.ScoreManageEntity;

/**
 * @author Rui
 * @InterfaceName ScoreManageService
 * @Description
 * @DATE 2023/10/9
 */
public interface ScoreManageService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, Integer type, Integer status) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(ScoreManageEntity scoreManageEntity) throws Exception;

    void saveEntity(String token, ScoreManageEntity scoreManageEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;

    JsonBean findAvailableScoreItemsByType(Integer type) throws Exception;

    void updateEntityStatus(ScoreManageEntity scoreManageEntity);
}
