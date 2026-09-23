package com.financial.sharing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字段映射表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_BILL_FIELD_MAPPING")
@ApiModel(value = "TblBillFieldMapping", description = "字段映射表")
public class TblBillFieldMapping implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "MAPPING_ID")
    @ApiModelProperty(value = "映射ID")
    private String mappingId;

    @TableField("CONFIG_ID")
    @ApiModelProperty(value = "配置ID")
    private String configId;

    @TableField("SOURCE_FIELD")
    @ApiModelProperty(value = "源字段")
    private String sourceField;

    @TableField("TARGET_FIELD")
    @ApiModelProperty(value = "目标字段")
    private String targetField;

    @TableField("TRANSFORM_RULE")
    @ApiModelProperty(value = "转换规则")
    private String transformRule;

    @TableField("DEFAULT_VALUE")
    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    @TableField("IS_REQUIRED")
    @ApiModelProperty(value = "是否必填")
    private Integer isRequired;

    @TableField("SORT_ORDER")
    @ApiModelProperty(value = "排序号")
    private Integer sortOrder;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
