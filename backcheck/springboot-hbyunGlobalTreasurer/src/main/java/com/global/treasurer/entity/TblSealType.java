package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 印鉴类型实体类
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
@TableName("TC_SEAL_TYPE")
public class TblSealType implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID (对应前端需要的id字段)
     * 对应数据库列: TYPE_ID
     */
    @TableId(value = "TYPE_ID", type = IdType.INPUT)
    private String id;

    /**
     * 类型编码 (前端字段名: code)
     * 对应数据库列: TYPE_CODE
     */
    @TableField("TYPE_CODE")
    @JsonProperty("code")
    private String typeCode;

    /**
     * 类型名称 (对应前端需要的name字段)
     * 对应数据库列: TYPE_NAME
     */
    @TableField("TYPE_NAME")
    private String name;

    /**
     * 印鉴级别: 1-一级印鉴, 2-二级印鉴, 3-三级印鉴
     * 对应数据库列: SEAL_LEVEL
     */
    @TableField("SEAL_LEVEL")
    private String sealLevel;

    /**
     * 适用范围
     * 对应数据库列: SCOPE
     */
    @TableField("SCOPE")
    private String scope;

    /**
     * 描述信息 (前端字段名: description)
     * 对应数据库列: REMARK
     */
    @TableField("REMARK")
    @JsonProperty("description")
    private String remark;

    /**
     * 状态:1启用,0禁用 (前端字段名: status)
     * 对应数据库列: IS_ACTIVE
     */
    @TableField("IS_ACTIVE")
    @JsonProperty("status")
    private Integer isActive;

    /**
     * 创建时间
     * 对应数据库列: CREATE_TIME
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    // Getter/Setter 方法

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTypeCode() { return typeCode; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
    // 兼容前端 code 字段
    public String getCode() { return typeCode; }
    public void setCode(String code) { this.typeCode = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSealLevel() { return sealLevel; }
    public void setSealLevel(String sealLevel) { this.sealLevel = sealLevel; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    // 兼容前端 description 字段
    public String getDescription() { return remark; }
    public void setDescription(String description) { this.remark = description; }

    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }
    // 兼容前端 status 字段
    public Integer getStatus() { return isActive; }
    public void setStatus(Integer status) { this.isActive = status; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
