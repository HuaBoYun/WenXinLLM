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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目成员表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PROJECT_MEMBER")
@ApiModel(value = "TblProjectMember", description = "项目成员表")
public class TblProjectMember implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "MEMBER_ID")
    @ApiModelProperty(value = "成员ID")
    private String memberId;

    @TableField("PROJECT_ID")
    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @TableField("USER_ID")
    @ApiModelProperty(value = "用户ID")
    private String userId;

    @TableField("USER_NAME")
    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @TableField("ROLE_TYPE")
    @ApiModelProperty(value = "角色类型")
    private String roleType;

    @TableField("JOIN_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "加入日期")
    private LocalDate joinDate;

    @TableField("LEAVE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "离开日期")
    private LocalDate leaveDate;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用")
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

