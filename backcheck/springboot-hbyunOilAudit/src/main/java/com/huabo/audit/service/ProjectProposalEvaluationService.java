package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;
import com.huabo.audit.oracle.entity.ProjectProposalEvaluationEntity;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * @author CJ
 * @InterfaceName ProjectProposalEvaluationService
 * @Description
 * @DATE 2024/5/28
 */
public interface ProjectProposalEvaluationService {
  
	
	JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String id, String projectName,String projectType,String projectPurpose,String createyear,BigDecimal tbid,String ids) throws Exception;
	
	JsonBean getLxZxsjList(String token, Integer pageNumber, Integer pageSize,ProjectProposalEvaluationEntity projectProposalEvaluationEntity) throws Exception;

	JsonBean findById(BigDecimal id) throws Exception;

	JsonBean updateEntity(ProjectProposalEvaluationEntity projectProposalEvaluationEntity,String attids) throws Exception;

	JsonBean saveEntity(String token, ProjectProposalEvaluationEntity projectProposalEvaluationEntity,String attids) throws Exception;

    void deleteByIds(BigDecimal ids) throws Exception;
    /**
     * 通过ids查询  
     *
     * @param ids
     * @return
     * @throws Exception
     */
    List<ProjectProposalEvaluationEntity> findByIds(List<String> ids)  ;
    
    
    JsonBean resolveSheet(XSSFSheet sheet, String token) throws Exception;
    
    
	void updateAuditScope(BigDecimal id,String auditScope) throws Exception;

	JsonBean getLxZxsjHzChooseList(String token, Integer pageNumber, Integer pageSize,
			ProjectProposalEvaluationEntity projectProposalEvaluationEntity) throws Exception;

	JsonBean setLxZxsjHzShowList(String token, String idStrs) throws Exception;

	JsonBean getLxZxsjHzDetailList(String token, Integer pageNumber, Integer pageSize,
			ProjectProposalEvaluationEntity projectProposalEvaluationEntity) throws Exception;


}
