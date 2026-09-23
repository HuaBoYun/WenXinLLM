package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 影响因素定义实体类
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INFLUENCE_FACTOR")
@ApiModel(value = "InfluenceFactorEntity对象", description = "影响因素定义表")
public class InfluenceFactorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "影响因素ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long factorId;

    @ApiModelProperty(value = "影响因素编码")
    @TableField("FACTOR_CODE")
    private String factorCode;

    @ApiModelProperty(value = "影响因素名称")
    @TableField("FACTOR_NAME")
    private String factorName;

    @ApiModelProperty(value = "影响因素类型(1固定2比率3公式4手工)")
    @TableField("FACTOR_TYPE")
    private Integer factorType;

    @ApiModelProperty(value = "数据类型(STRING/NUMBER/BOOLEAN)")
    @TableField("DATA_TYPE")
    private String dataType;

    @ApiModelProperty(value = "因素值")
    @TableField("FACTOR_VALUE")
    private String factorValue;

    @ApiModelProperty(value = "默认值")
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    @ApiModelProperty(value = "计算公式")
    @TableField("CALCULATION_FORMULA")
    private String calculationFormula;

    @ApiModelProperty(value = "影响范围")
    @TableField("AFFECT_SCOPE")
    private String affectScope;

    @ApiModelProperty(value = "优先级")
    @TableField("PRIORITY_LEVEL")
    private Integer priorityLevel;

    @ApiModelProperty(value = "因素分类")
    @TableField("FACTOR_CATEGORY")
    private String factorCategory;

    @ApiModelProperty(value = "计算方法")
    @TableField("CALCULATION_METHOD")
    private String calculationMethod;

    @ApiModelProperty(value = "计量单位")
    @TableField("UNIT")
    private String unit;

    @ApiModelProperty(value = "备注说明")
    @TableField("REMARK")
    private String remark;

    @ApiModelProperty(value = "是否启用(0否1是)")
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @ApiModelProperty(value = "因素描述")
    @TableField("DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private Long tenantId;

    @ApiModelProperty(value = "账簿ID")
    @TableField("BOOK_ID")
    private Long bookId;

    @ApiModelProperty(value = "版本号")
    @TableField("VERSION")
    private Integer version;

    @ApiModelProperty(value = "删除标识(0否1是)")
    @TableField("IS_DELETED")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATE_BY")
    private Long createBy;

    @ApiModelProperty(value = "更新人")
    @TableField("UPDATE_BY")
    private Long updateBy;
}
