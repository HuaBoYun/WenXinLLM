package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsMyTaskReviewEntity;
import com.huabo.audit.oracle.entity.TblYqnsMyTaskReviewEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsMyTaskReviewService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/28 14:40.
 * @version: V1.0
 * @description: 央企内审-审计实施-我的任务-审查
 */
public interface TblYqnsMyTaskReviewService extends IService<TblYqnsMyTaskReviewEntity> {



    /**
     * 根据我的任务-审查id查询我的任务-审查
     * @param id
     * @return
     */
    JsonBean getReviewById(String token, Long id)  throws Exception;

    /**
     * 根据我的任务-审查id查询我的任务-审查
     * @param id
     * @return
     */
     TblYqnsMyTaskReviewEntity getReviewById(Long id);


    /**
     * 根据我的任务-审查关联  TypeId  央企内审-基础配置-工程审计类型
     * @param typeId
     * @return
     */
    TblYqnsMyTaskReviewEntity getReviewByTypeId(Long typeId);
    
    

    /**
     * 根据我的任务-审查关联  TypeId 关联项目查询
     * @param typeId
     * @return
     */
    TblYqnsMyTaskReviewEntity gettaskTypeId(Long typeId,Long templateId,BigDecimal projectId);


    /**
     * 保存或更新我的任务-审查
     * @param vo
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsMyTaskReviewEntity vo)  throws Exception;

    /**
     * 保存或更新我的任务-审查（List）
     * @param voList
     * @return
     */
    JsonBean saveOrUpdateList(String token, List<TblYqnsMyTaskReviewEntity> voList)  throws Exception;

    /**
     * 删除我的任务-审查(直接删除)
     * @param id
     * @return
     */
    JsonBean delete(String token,Long id) throws Exception;


    /**
     * 删除我的任务-审查 附件
     * @param token
     * @param attId
     * @return
     * @throws Exception
     */
    JsonBean deleteFileAttach(String token,String attId) throws Exception;
    
    /**
     * 我的任务-审查详情id 查询附件列表
     * @param id
     * @return
     */
    JsonBean getReviewattlistById(String token,String id,String attids) throws Exception;

	JsonBean getMyTaskReviewList(String token, TblYqnsMyTaskReviewEntity vo) throws Exception;

}
