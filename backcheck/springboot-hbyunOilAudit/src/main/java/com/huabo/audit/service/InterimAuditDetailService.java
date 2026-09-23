package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.InterimAuditDetailEntity;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * @author Rui
 * @InterfaceName InterimAuditDetailService
 * @Description
 * @DATE 2024/04/15
 */
public interface InterimAuditDetailService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String orgName, String teamLeader, String projectName,String createyear,BigDecimal tbid,String ids) throws Exception;

    JsonBean findById(String id) throws Exception;

    JsonBean updateEntity(InterimAuditDetailEntity interimAuditDetailEntity) throws Exception;

    JsonBean saveEntity(String token, InterimAuditDetailEntity interimAuditDetailEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;

    void distribute(String ids, String personIds) throws Exception;
    
    JsonBean resolveSheet(XSSFSheet sheet, String token) throws Exception;

	JsonBean getRzsjmxListDraftPlan(String token, Integer pageNumber, Integer pageSize, String orgName,
			String teamLeaderId, String projectName, String createyear, BigDecimal tbid, Integer sourceType, BigDecimal jhid) throws Exception;

	List<InterimAuditDetailEntity> findByIds(String idStrs) throws Exception;
    
}
