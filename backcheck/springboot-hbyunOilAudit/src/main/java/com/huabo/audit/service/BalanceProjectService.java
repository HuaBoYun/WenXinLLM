package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.BalanceProjectEntity;

/**
 * @author Rui
 * @InterfaceName BalanceProjectService
 * @Description
 * @DATE 2023/10/1
 */
public interface BalanceProjectService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String planNo, String projectName, String contractNo) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(BalanceProjectEntity balanceProjectEntity) throws Exception;

    void saveEntity(String token, BalanceProjectEntity balanceProjectEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;
}
