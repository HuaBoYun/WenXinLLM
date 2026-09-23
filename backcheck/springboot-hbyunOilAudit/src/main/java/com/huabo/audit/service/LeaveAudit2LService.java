package com.huabo.audit.service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTypeEntity;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rui
 * @InterfaceName LeaveAudit2LService
 * @Description
 * @DATE 2023/9/14
 */
public interface LeaveAudit2LService  {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String projectName, String auditOrg, Integer queryYear) throws Exception;

    JsonBean findById(String id) throws Exception;

    JsonBean updateEntity(LeaveAudit2LEntity leaveAudit2LEntity) throws Exception;

    /**
     * 新增方法
     * @param token
     * @param leaveAudit2LEntity
     * @param type 新增类型 1-导入  0手动新增
     * @throws Exception
     */
    JsonBean saveEntity(String token, LeaveAudit2LEntity leaveAudit2LEntity, int type) throws Exception;

    void deleteByIds(String ids) throws Exception;

    void distribute(String ids, String personIds) throws Exception;

    void resolveSheet(XSSFSheet sheet, String token, Integer isCover) throws Exception;

    /**
     * 通过ids查询 二级单位及成员单位离任审计
     *
     * @param ids
     * @return
     * @throws Exception
     */
    List<LeaveAudit2LEntity> findByIds(String ids)  ;

	List<LeaveAudit2LEntity> findExportList(String token, Integer pageNumber, Integer pageSize, String projectName,
			String auditOrg, Integer queryYear,String ids) throws Exception;

	JsonBean getAutoNo(String token) throws Exception;

	JsonBean getListByDraftPlan(String token, Integer pageNumber, Integer pageSize, String projectName, String auditOrg,
			Integer queryYear, Integer sourceType, BigDecimal jhid) throws Exception;


}
