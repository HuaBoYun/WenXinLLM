package com.financial.sharing.service;

import com.financial.sharing.entity.TblTravelArchive;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商旅档案服务接口
 *
 * @author Financial Sharing System
 * @since 2025-01-30
 */
public interface TblTravelArchiveService {

    /**
     * 分页查询商旅档案
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult<TblTravelArchive>> getList(Object param);

    /**
     * 根据ID查询商旅档案详情
     *
     * @param archiveId 档案ID
     * @return 档案详情
     */
    MyJsonBean<TblTravelArchive> getById(String archiveId);

    /**
     * 保存或更新商旅档案
     *
     * @param param 保存参数
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(Object param);

    /**
     * 删除商旅档案
     *
     * @param archiveId 档案ID
     * @return 操作结果
     */
    MyJsonBean delete(String archiveId);

    /**
     * 更新档案状态
     *
     * @param archiveId 档案ID
     * @param isEnabled 是否启用
     * @return 操作结果
     */
    MyJsonBean updateStatus(String archiveId, Integer isEnabled);

    /**
     * 获取价格列表
     *
     * @param archiveId 档案ID
     * @return 价格列表
     */
    MyJsonBean<List<Map<String, Object>>> getPriceList(String archiveId);

    /**
     * 保存价格列表
     *
     * @param archiveId 档案ID
     * @param prices 价格列表
     * @return 操作结果
     */
    MyJsonBean savePriceList(String archiveId, List<Map<String, Object>> prices);

    /**
     * 获取合作协议
     *
     * @param archiveId 档案ID
     * @return 合作协议列表
     */
    MyJsonBean<List<Map<String, Object>>> getAgreements(String archiveId);

    /**
     * 保存合作协议列表
     *
     * @param archiveId 档案ID
     * @param agreements 协议列表
     * @return 操作结果
     */
    MyJsonBean saveAgreementList(String archiveId, List<Map<String, Object>> agreements);

    /**
     * 获取评价记录
     *
     * @param archiveId 档案ID
     * @return 评价记录列表
     */
    MyJsonBean<List<Map<String, Object>>> getEvaluations(String archiveId);

    /**
     * 保存评价
     *
     * @param archiveId 档案ID
     * @param evaluation 评价信息
     * @return 操作结果
     */
    MyJsonBean saveEvaluation(String archiveId, Map<String, Object> evaluation);

    /**
     * 获取平均评分
     *
     * @param archiveId 档案ID
     * @return 平均评分
     */
    MyJsonBean<BigDecimal> getAvgScore(String archiveId);
}
