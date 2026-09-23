package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.dto.TblYqnsSiteReviewContentDto;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-09 14:34
 **/
public interface SiteReviewContentService {
    /**
     * 保存/更新现场审查主要内容
     * @param param
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsSiteReviewContentDto param) throws Exception;

    /**
     * 根据ID删除记录
     * @param id
     * @return
     */
    JsonBean delete(String token,Long id) throws Exception;

    /**
     * 根据参数查询现场审查主要内容列表
     * @param projectName
     * @param settleProjectNum
     * @param pageNumber
     * @param pageSize
     * @param templateId 
     * @return
     */
    JsonBean findSiteReviewContentListByParam(String token,String projectName, String settleProjectNum, Integer pageNumber, Integer pageSize, BigDecimal templateId,BigDecimal projectId) throws Exception;

    /**
     * 根据ID查询现场审查主要内容
     * @param siteReviewId
     * @return
     */
    JsonBean findOneSiteReviewById(String token,Long siteReviewId) throws Exception;
}
