package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 融资基础参数实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FINANCING_BASIC_PARAMS")
public class TblFinancingBasicParams implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 参数ID */
    @TableId(type = IdType.AUTO)
    private Long paramId;

    /** 参数编码 */
    @TableField("PARAM_CODE")
    private String paramCode;

    /** 参数类型 */
    @TableField("PARAM_TYPE")
    private String paramType;

    /** 参数名称 */
    @TableField("PARAM_NAME")
    private String paramName;

    /** 参数值 */
    @TableField("PARAM_VALUE")
    private String paramValue;

    /** 参数描述 */
    @TableField("PARAM_DESC")
    private String description;

    /** 状态：ENABLE-启用，DISABLE-禁用 */
    @TableField("STATUS")
    private String status;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /** 创建人ID */
    @TableField("CREATE_USER")
    private Long createUser;

    /** 更新人ID */
    @TableField("UPDATE_USER")
    private Long updateUser;

    /** 组织ID */
    @TableField("ORG_ID")
    private Long orgId;

    /** 备注 */
    @TableField("REMARK")
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getParamId() { return paramId; }
    public void setParamId(Long paramId) { this.paramId = paramId; }
    public String getParamCode() { return paramCode; }
    public void setParamCode(String paramCode) { this.paramCode = paramCode; }
    public String getParamType() { return paramType; }
    public void setParamType(String paramType) { this.paramType = paramType; }
    public String getParamName() { return paramName; }
    public void setParamName(String paramName) { this.paramName = paramName; }
    public String getParamValue() { return paramValue; }
    public void setParamValue(String paramValue) { this.paramValue = paramValue; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
