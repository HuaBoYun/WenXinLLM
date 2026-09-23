package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsAuditMyManuscriptEntity;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditMyManuscriptService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企内审-审计实施-我的底稿
 */
public interface TblYqnsAuditMyManuscriptService extends IService<TblYqnsAuditMyManuscriptEntity> {


    /**
     * 查询我的底稿列表-分页 -
     *
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @param xmnd 
     * @param staffId 
     * @return
     */
    JsonBean getMyManuscriptPage(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditMyManuscriptEntity vo, BigDecimal staffId, Integer xmnd) throws Exception;


    /**
     * 我的底稿--获取我的底稿列表 通过TypeId
     *
     * @param typeId
     * @return
     */
    JsonBean getMyManuscriptListByTypeId(String token, String typeId,String templateId) throws Exception;

    /**
     * 根据我的底稿id查询我的底稿
     *
     * @param id
     * @return
     */
    JsonBean getMyManuscriptById(String token, Long id) throws Exception;


    /**
     * 根据我的底稿id和TypeId 查询我的底稿
     *
     * @param id
     * @return
     */
    TblYqnsAuditMyManuscriptEntity getMyManuscriptById(Long id);

    /**
     * 保存或更新我的底稿
     *
     * @param vo
     * @param reportIds 
     * @return
     */
    JsonBean saveOrUpdate(String token, TblYqnsAuditMyManuscriptEntity vo,String type) throws Exception;

    /**
     * 删除我的底稿(直接删除)
     *
     * @param id
     * @return
     */
    JsonBean delete(String token, Long id) throws Exception;

    /**
     * 生成底稿 获取底稿
     *
     * @return
     */
    String generateDraftNumber();


    /**
     * 底稿管理页面
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean dggl_list(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditMyManuscriptEntity vo) throws Exception;

    /**
     * 根据底稿管理id查询底稿列表
     *
     * @param id
     * @return
     */
    JsonBean getManuscriptById(String token, Long id) throws Exception;

    /**
     * 底稿管理-导出
     */
    JsonBean excelUtils(HttpServletResponse response, String token,TblYqnsAuditMyManuscriptEntity vo, List<String> idList) throws Exception;

    /**
     * 我的底稿--底稿清单--汇总;汇总查询数据-按照底稿导出
     * @param token
     * @return
     * @throws Exception
     */
    JsonBean getDraftListPage(String token, Integer pageNumber, Integer pageSize,TblYqnsAuditMyManuscriptEntity entity) throws Exception;


    /**
     * 审计发现页面
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean question_store_list(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditMyManuscriptEntity vo) throws Exception;

    
    JsonBean autoCode(String token) throws Exception;

	JsonBean delMyDraftWorkReportRela(String token, Long workReprotId, Long myDraftId) throws Exception;


	JsonBean getManuscriptPage(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditMyManuscriptEntity vo) throws Exception;
	
	/**
     * 底稿清单导出
     *
     * @param response
     * @param token
     * @param idList
     * @return
     * @throws Exception
     */
	JsonBean qdexcelUtils(HttpServletResponse response, String token, TblYqnsAuditMyManuscriptEntity vo, List<String> idList) throws Exception ;


    JsonBean excelWord(HttpServletResponse response, String token, TblYqnsAuditMyManuscriptEntity vo) throws Exception ;
    
    JsonBean getwtqdManuscriptPage(String token, Integer pageNumber, Integer pageSize,TblYqnsAuditMyManuscriptEntity entity) throws Exception ;

}
