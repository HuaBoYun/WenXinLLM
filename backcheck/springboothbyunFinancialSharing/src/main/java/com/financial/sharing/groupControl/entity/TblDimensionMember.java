package com.financial.sharing.groupControl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 维度成员表
 * 
 * @author system
 * @since 2026-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_DIMENSION_MEMBER")
@Schema(description = "维度成员信息")
public class TblDimensionMember implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成员ID
     */
    @TableId(value = "MEMBER_ID", type = IdType.ASSIGN_ID)
    @Schema(description = "成员ID")
    private String memberId;

    /**
     * 维度ID
     */
    @TableField("DIMENSION_ID")
    @Schema(description = "维度ID")
    private String dimensionId;

    /**
     * 成员编码
     */
    @TableField("MEMBER_CODE")
    @Schema(description = "成员编码")
    private String memberCode;

    /**
     * 成员名称
     */
    @TableField("MEMBER_NAME")
    @Schema(description = "成员名称")
    private String memberName;

    /**
     * 成员类型：NORMAL(普通)/SHARED(共享)
     */
    @TableField("MEMBER_TYPE")
    @Schema(description = "成员类型")
    private String memberType;

    /**
     * 源成员ID（共享成员）
     */
    @TableField("SOURCE_MEMBER_ID")
    @Schema(description = "源成员ID")
    private String sourceMemberId;

    /**
     * 父成员ID
     */
    @TableField("PARENT_MEMBER_ID")
    @Schema(description = "父成员ID")
    private String parentMemberId;

    /**
     * 成员层级
     */
    @TableField("MEMBER_LEVEL")
    @Schema(description = "成员层级")
    private Integer memberLevel;

    /**
     * 是否末级：Y/N
     */
    @TableField("IS_LEAF")
    @Schema(description = "是否末级")
    private String isLeaf;

    /**
     * 排序号
     */
    @TableField("SORT_NO")
    @Schema(description = "排序号")
    private Integer sortNo;

    /**
     * 状态：ACTIVE(启用)/INACTIVE(停用)
     */
    @TableField("STATUS")
    @Schema(description = "状态")
    private String status;

    /**
     * 扩展属性（JSON格式）
     */
    @TableField("EXTENDED_ATTRS")
    @Schema(description = "扩展属性")
    private String extendedAttrs;

    /**
     * 来源系统
     */
    @TableField("SOURCE_SYSTEM")
    @Schema(description = "来源系统")
    private String sourceSystem;

    /**
     * 来源系统ID
     */
    @TableField("SOURCE_ID")
    @Schema(description = "来源系统ID")
    private String sourceId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    @Schema(description = "租户ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    @Schema(description = "创建人")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField("UPDATE_USER")
    @Schema(description = "修改人")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    @Schema(description = "修改时间")
    private Date updateTime;

    /**
     * 子节点列表（非数据库字段）
     */
    @TableField(exist = false)
    @Schema(description = "子节点列表")
    private List<TblDimensionMember> children;
}

