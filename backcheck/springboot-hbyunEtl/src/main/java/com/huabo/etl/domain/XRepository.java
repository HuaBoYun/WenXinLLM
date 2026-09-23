package com.huabo.etl.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;

/**
 * 资源库对象 kettle_repository
 *
 * @author zhibo.cao
 * @date 2021-07-12
 */
@Schema(name="作业调度对象")
@TableName("kettle_repository")
public class XRepository implements Serializable {
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
    @Schema
    private String repoId;

    /**
     * 资源库名称
     */
    @Schema
    private String repoName;

    /**
     * 当资源库类型是db时候的用户名
     */
    @Schema
    private String repoUsername;

    /**
     * 当资源库类型是db时候的密码
     */
    @Schema
    private String repoPassword;

    /**
     * db类型
     */
    @Schema
    private String repoType;

    /**
     * 当资源库类型是db时候的连接类型
     */
    @Schema
    private String dbAccess;

    /**
     * 当资源库类型是db时候的ip
     */
    @Schema
    private String dbHost;

    /**
     * 当资源库类型是db时候的端口
     */
    @Schema
    private String dbPort;

    /**
     * 当资源库类型是db时候的db库名
     */
    @Schema
    private String dbName;

    /**
     * 当资源库类型是db时候的db用户名
     */
    @Schema
    private String dbUsername;

    /**
     * 当资源库类型是db时候的db用户密码
     */
    @Schema
    private String dbPassword;

    /**
     * 软删除
     */
    @Schema
    private int isDel;

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
     * 资源库类型
     */
    @Schema
    private String type;

    /**
     * 基础路径
     */
    @Schema
    private String baseDir;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }

    public String getRepoId() {
        return repoId;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoUsername(String repoUsername) {
        this.repoUsername = repoUsername;
    }

    public String getRepoUsername() {
        return repoUsername;
    }

    public void setRepoPassword(String repoPassword) {
        this.repoPassword = repoPassword;
    }

    public String getRepoPassword() {
        return repoPassword;
    }

    public void setRepoType(String repoType) {
        this.repoType = repoType;
    }

    public String getRepoType() {
        return repoType;
    }

    public void setDbAccess(String dbAccess) {
        this.dbAccess = dbAccess;
    }

    public String getDbAccess() {
        return dbAccess;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getBaseDir() {
        return baseDir;
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

}
