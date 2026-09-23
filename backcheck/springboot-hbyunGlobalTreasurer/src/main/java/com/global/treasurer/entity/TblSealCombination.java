package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 印鉴组合配置实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_SEAL_COMBINATION")
public class TblSealCombination implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 组合名称
     */
    private String combinationName;

    /**
     * 组合编码(唯一)
     */
    private String combinationCode;

    /**
     * 印章列表JSON
     */
    private String sealList;

    /**
     * 组合描述
     */
    private String description;

    /**
     * 状态:1启用,0禁用
     */
    private Integer status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCombinationName() { return combinationName; }
    public void setCombinationName(String combinationName) { this.combinationName = combinationName; }
    public String getCombinationCode() { return combinationCode; }
    public void setCombinationCode(String combinationCode) { this.combinationCode = combinationCode; }
    public String getSealList() { return sealList; }
    public void setSealList(String sealList) { this.sealList = sealList; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
