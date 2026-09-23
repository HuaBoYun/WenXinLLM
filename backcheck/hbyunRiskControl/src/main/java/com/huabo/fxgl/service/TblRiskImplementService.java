package com.huabo.fxgl.service;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.dto.TblMajorRiskbranchCreateDto;
import com.huabo.fxgl.entity.TblRiskImplementEntity;

import java.math.BigDecimal;
import java.util.List;

public interface TblRiskImplementService {

    /**
     * 下发表单-下发
     * @param token
     * @param tblRiskImplementEntity
     * @return
     * @throws Exception
     */
    JsonBean issuedAndSubmit(TblRiskImplementEntity tblRiskImplementEntity, String token) throws Exception;
   
    void issuedImplementDelete(String id) throws Exception;

    
    /**
     * 重大风险填报新增/修改
     * @param token
     * @param tblRiskImplementEntity
     * @return
     * @throws Exception
     */
    JsonBean issuedSaveOrUpdate(TblRiskImplementEntity tblRiskImplementEntity, String token) throws Exception;
    
    /**
     * 下发表单列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    JsonBean issuedImplementList(String token, Integer pageNumber, Integer pageSize,  String impRiskName,String majorid,String id) throws Exception;

    /**
     * 重大事件汇总列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    JsonBean issuedImplementSummary(String token, Integer pageNumber, Integer pageSize, String impRiskName,String id,String orgname,String jd) throws Exception;
//按照风险名称排序
    JsonBean issuedImplementSummaryOrder(String token, Integer pageNumber, Integer pageSize,String id,TblRiskImplementEntity entity) throws Exception;

    /**
     * 下发表单详情
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    JsonBean issuedImplementDetails(String id, String token) throws Exception;
    /**
     * 分公司判断是否可新建
     * @param token
     * @return
     * @throws Exception
     */
    JsonBean isCreateSubmit(String token) throws Exception;
    
    /**
     * 重大风险事件填报-下发记录-导出
     * @param token
     * @return
     * @throws Exception
     */
   List<TblRiskImplementEntity> exportIssuedImplement(String token,String ids,String impRiskName);


   /**
    * 重大风险事件汇总-导出
    * @param token
    * @return
    * @throws Exception
    */
   List<TblRiskImplementEntity> exportIssuedImplementSummary(String token,String ids,String impRiskName,String orgname,String jd);

   
   List<TblRiskImplementEntity> exportIssuedImplementSummaryOrder(String token,String ids,TblRiskImplementEntity entity);

   /**
    * 重大风险事件填报-上报
    * @param token
    * @return
    * @throws Exception
    */
   JsonBean reportToLeader(String token,BigDecimal id)throws Exception;
   
   JsonBean getMajorRiskCreateList(String token,Integer pageNumber,Integer pageSize,TblMajorRiskbranchCreateDto dto,Integer authorityType)throws Exception;

   JsonBean mjorDetail(String id)throws Exception;
   
   JsonBean  majorTransfer(String token,String ids,BigDecimal staffId)throws Exception;
   
   JsonBean  getMajorTransferList(String token,BigDecimal id)throws Exception;
   
   JsonBean getMajorRiskTrack(String token,Integer pageNumber,Integer pageSize,String nd,String jd,String orgName)throws Exception;

}
