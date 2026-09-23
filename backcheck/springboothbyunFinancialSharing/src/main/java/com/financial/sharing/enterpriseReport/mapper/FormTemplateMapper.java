package com.financial.sharing.enterpriseReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.enterpriseReport.entity.TblFormTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表单模板Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface FormTemplateMapper extends BaseMapper<TblFormTemplate> {

    /**
     * 查询表单模板列表(关联表单组名称)
     * 
     * @param tenantId 租户ID
     * @param groupId 表单组ID
     * @param templateName 模板名称
     * @param templateType 模板类型
     * @param status 状态
     * @return 表单模板列表
     */
    List<TblFormTemplate> selectFormTemplateList(@Param("tenantId") String tenantId,
                                                  @Param("groupId") String groupId,
                                                  @Param("templateName") String templateName,
                                                  @Param("templateType") String templateType,
                                                  @Param("status") String status);

    /**
     * 检查模板编码是否存在
     * 
     * @param templateCode 模板编码
     * @param tenantId 租户ID
     * @param excludeTemplateId 排除的模板ID
     * @return 数量
     */
    int checkTemplateCodeExists(@Param("templateCode") String templateCode,
                                @Param("tenantId") String tenantId,
                                @Param("excludeTemplateId") String excludeTemplateId);

    /**
     * 取消表单组下所有模板的默认版本标识
     * 
     * @param groupId 表单组ID
     * @param tenantId 租户ID
     */
    void cancelDefaultVersion(@Param("groupId") String groupId,
                              @Param("tenantId") String tenantId);
}

