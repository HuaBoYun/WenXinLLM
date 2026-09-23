package com.huabo.etl.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;

/**
 * 转换对象 kettle_trans
 *
 * @author zhibo.cao
 * @date 2022-12-01
 */
@Schema(name="转换对象")
@TableName("kettle_trans")
public class KettleTrans implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    
    @Schema
    @TableId
    private Long id;

    /**
     * 转换名称
     */
    @Schema
    private String transName;

    /**
     * 转换描述
     */
    @Schema
    private String transDescription;

    /**
     *
     */
    @Schema
    private Date createdTime;

    /**
     *
     */
    @Schema
    private String createdBy;

    /**
     * 转换类型(file,ftp,sf)
     */
    @Schema
    private String transType;

    /**
     * 路径
     */
    @Schema
    private String transPath;

    /**
     * 所属资源库id
     */
    
    @Schema
    private Long transRepositoryId;

    /**
     * 资源库
     */
    @TableField(exist = false)
    @Schema
    private String transRepository;

    /**
     * 基础路径
     */
    @TableField(exist = false)
    @Schema
    private String baseDir;


    /**
     * 日志级别
     */
    @Schema
    private String transLogLevel;

    /**
     * 状态
     */
    @Schema
    private String transStatus;

    /**
     * 是否删除
     */
    @Schema
    private Integer isDel;

    /**
     * 是否启用
     */
    @Schema
    private Integer isMonitorEnabled;

    /**
     * 保留备用
     */
    @Schema
    private String tplKey;

    /**
     * 可执行角色key,用+号拼接
     */
    @Schema
    private String roleKey;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema
    private Date lastSucceedTime;

    @Schema
    private String cron;

    @Schema
    private String cronStatus;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    public String getTransDescription() {
        return transDescription;
    }

    public void setTransDescription(String transDescription) {
        this.transDescription = transDescription;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransPath() {
        return transPath;
    }

    public void setTransPath(String transPath) {
        this.transPath = transPath;
    }

    public Long getTransRepositoryId() {
        return transRepositoryId;
    }

    public void setTransRepositoryId(Long transRepositoryId) {
        this.transRepositoryId = transRepositoryId;
    }

    public String getTransRepository() {
        return transRepository;
    }

    public void setTransRepository(String transRepository) {
        this.transRepository = transRepository;
    }

    public String getTransLogLevel() {
        return transLogLevel;
    }

    public void setTransLogLevel(String transLogLevel) {
        this.transLogLevel = transLogLevel;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Integer getIsMonitorEnabled() {
        return isMonitorEnabled;
    }

    public void setIsMonitorEnabled(Integer isMonitorEnabled) {
        this.isMonitorEnabled = isMonitorEnabled;
    }

    public String getTplKey() {
        return tplKey;
    }

    public void setTplKey(String tplKey) {
        this.tplKey = tplKey;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public Date getLastSucceedTime() {
        return lastSucceedTime;
    }

    public void setLastSucceedTime(Date lastSucceedTime) {
        this.lastSucceedTime = lastSucceedTime;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getCronStatus() {
        return cronStatus;
    }

    public void setCronStatus(String cronStatus) {
        this.cronStatus = cronStatus;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}
