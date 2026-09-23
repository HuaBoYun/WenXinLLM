package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.SubContractEntity;

/**
 * @author Rui
 * @InterfaceName SubContractService
 * @Description
 * @DATE 2023/10/1
 */
public interface SubContractService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize,  String projectName, String subContractNo) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(SubContractEntity subContractEntity) throws Exception;

    void saveEntity(String token, SubContractEntity subContractEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;
}
