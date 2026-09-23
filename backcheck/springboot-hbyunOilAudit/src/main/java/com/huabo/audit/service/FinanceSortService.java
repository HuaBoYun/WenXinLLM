package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.FinanceSortEntity;

/**
 * @author Rui
 * @InterfaceName FinanceSortService
 * @Description
 * @DATE 2023/9/23
 */
public interface FinanceSortService {

    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String projectName) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(FinanceSortEntity financeSortEntity) throws Exception;

    void saveEntity(String token, FinanceSortEntity financeSortEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;
}
