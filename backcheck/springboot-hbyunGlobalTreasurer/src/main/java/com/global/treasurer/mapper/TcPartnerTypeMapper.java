package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcPartnerType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 合作伙伴类型 Mapper 接口
 */
@Mapper
public interface TcPartnerTypeMapper {
    
    /**
     * 分页查询合作伙伴类型列表
     */
    List<TcPartnerType> listPartnerType(@Param("params") Map<String, Object> params);
    
    /**
     * 查询总数
     */
    int countPartnerType(@Param("params") Map<String, Object> params);
    
    /**
     * 根据 ID 查询详情
     */
    TcPartnerType getById(@Param("partnerTypeId") Long partnerTypeId);
    
    /**
     * 根据编码查询
     */
    TcPartnerType getByCode(@Param("typeCode") String typeCode);
    
    /**
     * 新增
     */
    int insert(TcPartnerType partnerType);
    
    /**
     * 更新
     */
    int update(TcPartnerType partnerType);
    
    /**
     * 删除
     */
    int delete(@Param("partnerTypeId") Long partnerTypeId);
    
    /**
     * 批量删除
     */
    int batchDelete(@Param("ids") List<Long> ids);
    
    /**
     * 更新排序
     */
    int updateSortOrder(@Param("sortList") List<Map<String, Object>> sortList);
    
    /**
     * 切换启用状态
     */
    int toggleStatus(@Param("partnerTypeId") Long partnerTypeId, @Param("isEnabled") Integer isEnabled);
    
    /**
     * 查询所有启用的类型
     */
    List<TcPartnerType> getAllEnabled();
    
    /**
     * 导出
     */
    List<TcPartnerType> exportPartnerType(@Param("params") Map<String, Object> params);
}
