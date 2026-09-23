package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.PlanDataEntity;

/**
 * @author Rui
 * @InterfaceName PlanDataService
 * @Description
 * @DATE 2023/10/9
 */
public interface PlanDataService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String name) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(PlanDataEntity planDataEntity) throws Exception;

    void saveEntity(String token, PlanDataEntity planDataEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;
}
