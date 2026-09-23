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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 标准级别配置表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_GENERAL_STANDARD_LEVEL")
@ApiModel(value = "TblGeneralStandardLevel", description = "标准级别配置表")
public class TblGeneralStandardLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "LEVEL_ID")
    @ApiModelProperty(value = "级别ID")
    private String levelId;

    @TableField("STANDARD_ID")
    @ApiModelProperty(value = "标准ID")
    private String standardId;

    @TableField("LEVEL_CODE")
    @ApiModelProperty(value = "级别编码")
    private String levelCode;

    @TableField("LEVEL_NAME")
    @ApiModelProperty(value = "级别名称")
    private String levelName;

    @TableField("STANDARD_VALUE")
    @ApiModelProperty(value = "标准值")
    private BigDecimal standardValue;

    @TableField("UNIT")
    @ApiModelProperty(value = "单位")
    private String unit;

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
