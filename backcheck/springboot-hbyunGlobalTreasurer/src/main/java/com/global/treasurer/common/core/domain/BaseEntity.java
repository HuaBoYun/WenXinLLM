package com.global.treasurer.common.core.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity基类
 * 
 * @author global-treasurer
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 创建时间 */
    @TableField(value = "CREATETIME", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 创建用户 */
    @TableField(value = "CREATEUSER", fill = FieldFill.INSERT)
    private Long createUser;

    /** 更新时间 */
    @TableField(value = "UPDATETIME", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /** 更新用户 */
    @TableField(value = "UPDATEUSER", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /** 备注 */
    @TableField("REMARK")
    private String remark;

    /** 组织ID */
    @TableField("ORGID")
    private Long orgId;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }

}
