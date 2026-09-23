package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.DesignPaymentInfoEntity;

/**
 * @author Rui
 * @InterfaceName DesignPaymentInfoService
 * @Description
 * @DATE 2023/9/30
 */
public interface DesignPaymentInfoService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String planId, String contractNo) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(DesignPaymentInfoEntity designPaymentInfoEntity) throws Exception;

    void saveEntity(String token, DesignPaymentInfoEntity designPaymentInfoEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;
}
