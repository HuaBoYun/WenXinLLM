package com.huabo.finance.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 转换任务DTO
 * 
 * @author 华博云开发团队
 * @since 2025-10-24
 */
@Data
@Schema(name="TransformTaskDTO", description="转换任务数据传输对象")
public class TransformTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "任务名称")
    private String taskName;

    @Schema(name =  "采集任务ID")
    private String collectionTaskId;

    @Schema(name =  "字段映射列表")
    private List<FieldMappingDTO> mappings;

    @Schema(name =  "计算规则列表")
    private List<TransformRuleDTO> rules;

    @Schema(name =  "备注")
    private String remark;
}

