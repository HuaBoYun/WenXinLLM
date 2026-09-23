package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.ServiceRequirementEntity;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * @author Rui
 * @InterfaceName ServiceSuggestionService
 * @Description
 * @DATE 2023/9/6
 */
public interface ServiceRequirementService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String id, String auditItem, Integer queryYear,String projectType) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(ServiceRequirementEntity serviceRequirementEntity) throws Exception;

    void saveEntity(String token, ServiceRequirementEntity serviceRequirementEntity, int type) throws Exception;

    void deleteByIds(String ids) throws Exception;

    void resolveSheet(XSSFSheet sheet, String token, Integer isCover) throws Exception;

    void distribute(String ids, String personIds) throws Exception;

	JsonBean getAutoNo(String token) throws Exception;

	List<ServiceRequirementEntity> findExprotList(String token, Integer pageNumber, Integer pageSize, String id, String auditItem,
			Integer queryYear, String ids) throws Exception;
}
