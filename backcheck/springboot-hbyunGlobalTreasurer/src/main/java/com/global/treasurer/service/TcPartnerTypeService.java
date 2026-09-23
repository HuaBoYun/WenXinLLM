package com.global.treasurer.service;

import com.global.treasurer.entity.TcPartnerType;
import java.util.List;
import java.util.Map;

/**
 * 合作伙伴类型 Service 接口
 */
public interface TcPartnerTypeService {
    
    /**
     * 分页查询列表
     */
    List<TcPartnerType> list(Integer pageNum, Integer pageSize, Map<String, Object> params);
    
    /**
     * 查询总数
     */
    int count(Map<String, Object> params);
    
    /**
     * 根据 ID 查询详情
     */
    TcPartnerType getById(Long partnerTypeId);
    
    /**
     * 新增
     */
    boolean save(TcPartnerType partnerType);
    
    /**
     * 更新
     */
    boolean update(TcPartnerType partnerType);
    
    /**
     * 删除
     */
    boolean delete(Long partnerTypeId);
    
    /**
     * 批量删除
     */
    boolean batchDelete(List<Long> ids);
    
    /**
     * 更新排序
     */
    boolean updateSortOrder(List<Map<String, Object>> sortList);
    
    /**
     * 切换启用状态
     */
    boolean toggleStatus(Long partnerTypeId, Integer isEnabled);
    
    /**
     * 查询所有启用的类型
     */
    List<TcPartnerType> getAllEnabled();
    
    /**
     * 导出
     */
    List<TcPartnerType> exportPartnerType(Map<String, Object> params);
    
    /**
     * 获取统计信息
     */
    Map<String, Object> getStatistics();
}
