package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.LeaveAudit3LEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @InterfaceName LeaveAudit3LService
 * @Description
 * @DATE 2023/9/14
 */
public interface LeaveAudit3LService {

    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, LeaveAudit3LEntity leaveAudit3LEntity,BigDecimal jdid) throws Exception;

    JsonBean findById(String id) throws Exception;

    JsonBean updateEntity(String token,LeaveAudit3LEntity leaveAudit3LEntity) throws Exception;
 
    JsonBean saveEntity(String token, LeaveAudit3LEntity leaveAudit3LEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;


    /**
     * 通过ids查询 三级单位离任审计
     *
     * @param ids
     * @return
     * @throws Exception
     */
    List<LeaveAudit3LEntity> findByIds(String ids);


    /**
     * 查询三级单位离任审计汇总
     * @param queryYear 
     *
     * @return
     */
    JsonBean selectLeaveAudit3LSummary(String token, Integer queryYear) throws Exception;


    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean exportData(HttpServletResponse response, String token, LeaveAudit3LEntity vo,BigDecimal jdid) throws Exception;

    /**
     * 导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    JsonBean importData(MultipartFile file, String token,BigDecimal jdid) throws Exception;

	JsonBean getListDraftPlan(String token, Integer pageNumber, Integer pageSize, LeaveAudit3LEntity leaveAudit3LEntity,
			BigDecimal jdid) throws Exception;

	JsonBean getDetailDistributeList(String token, Integer pageNumber, Integer pageSize, LeaveAudit3LEntity vo) throws Exception;

	JsonBean getDistributeReceiveList(String token, Integer pageNumber, Integer pageSize, LeaveAudit3LEntity vo) throws Exception;

	JsonBean saveDistributionPerson(String token, String idStrs, BigDecimal disFirstPerson, BigDecimal disSecondPerson) throws Exception;


}
