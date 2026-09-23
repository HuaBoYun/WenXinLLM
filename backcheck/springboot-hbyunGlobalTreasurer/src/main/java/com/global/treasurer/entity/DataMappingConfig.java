package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 数据映射配置实体类
 * 对应表：TBL_DATA_MAPPING_CONFIG
 *
 * @author AI Developer
 * @date 2025-02-26
 */
@Data
@TableName("TBL_DATA_MAPPING_CONFIG")
public class DataMappingConfig {
    /**
     * 映射ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @TableField("MAPPING_ID")
    private Long id;

    /**
     * 映射名称
     */
    @TableField("MAPPING_NAME")
    private String mappingName;

    /**
     * 映射编码
     */
    @TableField("MAPPING_CODE")
    private String mappingCode;

    /**
     * 源系统(FINANCE财务系统, BANK银行系统, ERP系统, THIRD_PARTY第三方系统)
     */
    @TableField("SOURCE_SYSTEM")
    private String sourceSystem;

    /**
     * 目标系统
     */
    @TableField("TARGET_SYSTEM")
    private String targetSystem;

    /**
     * 源字段
     */
    @TableField("SOURCE_FIELD")
    private String sourceField;

    /**
     * 目标字段
     */
    @TableField("TARGET_FIELD")
    private String targetField;

    /**
     * 映射类型(FIELD字段映射, VALUE值映射, FUNCTION函数映射)
     */
    @TableField("MAPPING_TYPE")
    private String mappingType;

    /**
     * 映射规则
     */
    @TableField("MAPPING_RULE")
    private String mappingRule;

    /**
     * 转换函数
     */
    @TableField("TRANSFORM_FUNCTION")
    private String transformFunction;

    /**
     * 默认值
     */
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    /**
     * 是否必填(1必填, 0非必填)
     */
    @TableField("REQUIRED")
    private Integer required;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 状态(1启用, 0禁用)
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 创建人ID
     */
    @TableField("CREATED_BY")
    private Long createdBy;

    /**
     * 创建人姓名
     */
    @TableField("CREATED_BY_NAME")
    private String createdByName;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATED_BY")
    private Long updatedBy;

    /**
     * 更新人姓名
     */
    @TableField("UPDATED_BY_NAME")
    private String updatedByName;

    /**
     * 更新时间
     */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 删除标记(0未删除, 1已删除)
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    // Getter and Setter methods for Lombok compatibility
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
