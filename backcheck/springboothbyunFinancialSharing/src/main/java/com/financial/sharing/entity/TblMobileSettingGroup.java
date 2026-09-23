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
 * 设置分组表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_MOBILE_SETTING_GROUP")
@ApiModel(value = "TblMobileSettingGroup", description = "设置分组表")
public class TblMobileSettingGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "GROUP_ID")
    @ApiModelProperty(value = "分组ID")
    private String groupId;

    @TableField("GROUP_CODE")
    @ApiModelProperty(value = "分组编码")
    private String groupCode;

    @TableField("GROUP_NAME")
    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @TableField("GROUP_TYPE")
    @ApiModelProperty(value = "分组类型")
    private String groupType;

    @TableField("SORT_ORDER")
    @ApiModelProperty(value = "排序号")
    private Integer sortOrder;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用(0-禁用,1-启用)")
    private Integer isEnabled;

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
