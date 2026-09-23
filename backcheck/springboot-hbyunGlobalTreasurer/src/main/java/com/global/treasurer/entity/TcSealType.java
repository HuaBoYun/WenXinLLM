package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.io.Serializable;
import java.util.Date;

/**
 * 财资公共模块 - 印鉴类型表
 *
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
@ApiModel(value = "TcSealType", description = "印鉴类型管理")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TC_SEAL_TYPE")
public class TcSealType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     * 使用 @JsonProperty 让前端接收到 sealTypeId 字段名
     */
    @TableId(type = IdType.INPUT)
    @TableField("TYPE_ID")
    @ApiModelProperty(value = "主键ID")
    @JsonProperty("sealTypeId")
    private String id;

    /**
     * 类型编码
     */
    @TableField("TYPE_CODE")
    @ApiModelProperty(value = "类型编码", required = true)
    private String typeCode;

    /**
     * 类型名称
     */
    @TableField("TYPE_NAME")
    @ApiModelProperty(value = "类型名称", required = true)
    private String typeName;

    /**
     * 类型描述
     * 同时输出为 description 以适配前端
     */
    @TableField("TYPE_DESC")
    @ApiModelProperty(value = "类型描述")
    private String typeDesc;

    /**
     * 获取前端需要的 description 字段
     */
    @JsonProperty("description")
    public String getDescription() {
        return this.typeDesc;
    }

    /**
     * 设置 description 值（用于接收前端传入的值）
     */
    public void setDescription(String description) {
        this.typeDesc = description;
    }

    /**
     * 使用范围
     */
    @TableField("USAGE_SCOPE")
    @ApiModelProperty(value = "使用范围")
    private String usageScope;

    /**
     * 权限级别
     */
    @TableField("PERMISSION_LEVEL")
    @ApiModelProperty(value = "权限级别")
    private String permissionLevel;

    /**
     * 安全级别
     */
    @TableField("SECURITY_LEVEL")
    @ApiModelProperty(value = "安全级别")
    private String securityLevel;

    /**
     * 排序顺序
     */
    @TableField("SORT_ORDER")
    @ApiModelProperty(value = "排序顺序")
    private Integer sortOrder;

    /**
     * 状态：1-启用，0-停用
     */
    @TableField("STATUS")
    @ApiModelProperty(value = "状态：1-启用，0-停用")
    private String status;

    /**
     * 获取前端需要的 isEnabled 字段（整数类型）
     * 将 status 字符串转换为前端期望的整数值
     */
    @JsonProperty("isEnabled")
    public Integer getIsEnabled() {
        if (this.status == null) {
            return 0;
        }
        return "1".equals(this.status) ? 1 : 0;
    }

    /**
     * 设置 isEnabled 值（用于接收前端传入的值）
     */
    public void setIsEnabled(Integer isEnabled) {
        this.status = isEnabled != null && isEnabled == 1 ? "1" : "0";
    }

    /**
     * 备注
     */
    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    @ApiModelProperty(value = "版本号", hidden = true)
    private Integer versionNo;

    /**
     * 根据ID创建实例
     */
    public static TcSealType ofId(String id) {
        TcSealType sealType = new TcSealType();
        sealType.setId(id);
        return sealType;
    }

    /**
     * 根据类型编码创建实例
     */
    public static TcSealType ofTypeCode(String typeCode) {
        TcSealType sealType = new TcSealType();
        sealType.setTypeCode(typeCode);
        return sealType;
    }

    // 添加缺失的getter和setter方法
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
