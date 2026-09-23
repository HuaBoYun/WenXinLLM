package com.management.accountant.vo.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 预算模板DTO
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
@Data
@ApiModel("预算模板DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetTemplateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("模板ID")
    private String templateId;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("模板编码")
    private String templateCode;

    @ApiModelProperty("模板类型")
    private String templateType;

    @ApiModelProperty("模板分类")
    private String category;

    @ApiModelProperty("结构配置(JSON)")
    private String structure;

    @ApiModelProperty("公式配置(JSON)")
    private String formulas;

    @ApiModelProperty("维度配置(JSON)")
    private String dimensions;

    @ApiModelProperty("指标配置(JSON)")
    private String indicators;

    @ApiModelProperty("期间配置(JSON)")
    private String periods;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("是否启用")
    private Integer isEnabled;

    @ApiModelProperty("是否系统模板")
    private Integer isSystem;

    @ApiModelProperty("是否公开")
    private Integer isPublic;

    @ApiModelProperty("使用次数")
    private Integer useCount;

    @ApiModelProperty("复制来源")
    private String copiedFrom;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("排序序号")
    private Integer sortOrder;

    @ApiModelProperty("标签")
    private String tags;

    @ApiModelProperty("是否默认")
    private Integer isDefault;

    @ApiModelProperty("模板描述")
    private String templateDescription;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("公司ID")
    private String companyId;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("分类ID（前端传入，可为字符串或数组，取最后一个元素）")
    private Object categoryId;

    @ApiModelProperty("适用年度（前端传入日期字符串，取年份）")
    private String applicableYear;

    @ApiModelProperty("是否允许复制")
    private Integer allowCopy;

    @ApiModelProperty("模板字段列表（JSON序列化后存入templateConfig）")
    private List<Map<String, Object>> templateFields;

    @ApiModelProperty("模板配置JSON字符串（直接传入时原样保存，优先级低于templateFields）")
    private String templateConfig;
}

