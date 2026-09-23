package com.management.accountant.oracle.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统活动记录实体类
 * 
 * @description 系统活动记录实体,用于记录用户操作日志和系统活动
 * @author AI Agent
 * @date 2026-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SYSTEM_ACTIVITY")
@ApiModel(value = "SystemActivity对象", description = "系统活动记录")
public class SystemActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ACTIVITY_ID", type = IdType.ASSIGN_UUID)
    private String activityId;

    @ApiModelProperty(value = "用户ID")
    @TableField("USER_ID")
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    @TableField("USER_NAME")
    private String userName;

    @ApiModelProperty(value = "用户部门")
    @TableField("USER_DEPT")
    private String userDept;

    @ApiModelProperty(value = "操作动作（CREATE/UPDATE/DELETE/QUERY/EXPORT/IMPORT）")
    @TableField("ACTION")
    private String action;

    @ApiModelProperty(value = "操作名称")
    @TableField("ACTION_NAME")
    private String actionName;

    @ApiModelProperty(value = "模块名称")
    @TableField("MODULE_NAME")
    private String moduleName;

    @ApiModelProperty(value = "目标类型（ORGANIZATION/DIMENSION/INDICATOR/MODEL/TASK等）")
    @TableField("TARGET_TYPE")
    private String targetType;

    @ApiModelProperty(value = "目标对象ID")
    @TableField("TARGET_ID")
    private String targetId;

    @ApiModelProperty(value = "目标对象名称")
    @TableField("TARGET_NAME")
    private String targetName;

    @ApiModelProperty(value = "操作详情")
    @TableField("OPERATION_DETAIL")
    private String operationDetail;

    @ApiModelProperty(value = "操作结果")
    @TableField("OPERATION_RESULT")
    private String operationResult;

    @ApiModelProperty(value = "状态:SUCCESS-成功,FAILED-失败,PARTIAL-部分成功")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "错误信息")
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP_ADDRESS")
    private String ipAddress;

    @ApiModelProperty(value = "用户代理")
    @TableField("USER_AGENT")
    private String userAgent;

    @ApiModelProperty(value = "请求URL")
    @TableField("REQUEST_URL")
    private String requestUrl;

    @ApiModelProperty(value = "请求方法（GET/POST/PUT/DELETE）")
    @TableField("REQUEST_METHOD")
    private String requestMethod;

    @ApiModelProperty(value = "执行时间（毫秒）")
    @TableField("EXECUTE_TIME")
    private Integer executeTime;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "公司ID")
    @TableField("COMPANY_ID")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    @TableField("COMPANY_NAME")
    private String companyName;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;
}

