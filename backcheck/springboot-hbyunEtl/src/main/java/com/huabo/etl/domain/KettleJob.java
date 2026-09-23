package com.huabo.etl.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 作业调度对象 kettle_job
 *
 * @author zhibo.cao
 * @date 2021-07-22
 */
@Schema(name="作业调度对象")
@TableName("kettle_job")
public class KettleJob implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    
    @Schema
    @TableId
    private Long id;

    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema
    private Date createdTime;

    /**
     *
     */
    @Schema
    private String createdBy;

    /**
     * 作业名称
     */
    @Schema
    private String jobName;

    /**
     * 描述
     */
    @Schema
    private String jobDescription;

    /**
     * 作业类型(file,ftp,sf)
     */
    @Schema
    private String jobType;

    /**
     * 路径
     */
    @Schema
    private String jobPath;

    /**
     * 资源库id
     */
    
    @Schema
    private Long jobRepositoryId;

    @TableField(exist = false)
    @Schema
    private String jobRepository;

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
    private String jobLogLevel;

    /**
     * 状态
     */
    @Schema
    private String jobStatus;

    /**
     * 是否删除
     */
    @Schema
    private Integer isDel;



    /**
     * 是否监控
     */
    @Schema
    private Integer isMonitorEnabled;

    /**
     * 可执行角色key,用+号拼接
     */
    @Schema
    private String roleKey;

    /**
     *
     */
    @Schema
    private String tplKey;

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobPath() {
        return jobPath;
    }

    public void setJobPath(String jobPath) {
        this.jobPath = jobPath;
    }

    public Long getJobRepositoryId() {
        return jobRepositoryId;
    }

    public void setJobRepositoryId(Long jobRepositoryId) {
        this.jobRepositoryId = jobRepositoryId;
    }

    public String getJobLogLevel() {
        return jobLogLevel;
    }

    public void setJobLogLevel(String jobLogLevel) {
        this.jobLogLevel = jobLogLevel;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
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

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getTplKey() {
        return tplKey;
    }

    public void setTplKey(String tplKey) {
        this.tplKey = tplKey;
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

    public String getJobRepository() {
        return jobRepository;
    }

    public void setJobRepository(String jobRepository) {
        this.jobRepository = jobRepository;
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

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}
