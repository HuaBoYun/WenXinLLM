package com.financial.sharing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 存货分类实体类
 * 
 * @author system
 * @since 2026-01-27
 */
@Data
@TableName("TBL_INVENTORY_CATEGORY")
public class InventoryCategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "CATEGORY_ID", type = IdType.INPUT)
    private Long categoryId;

    /**
     * 分类编码
     */
    @TableField("CATEGORY_CODE")
    private String categoryCode;

    /**
     * 分类名称
     */
    @TableField("CATEGORY_NAME")
    private String categoryName;

    /**
     * 上级分类ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 分类层级
     */
    @TableField("CATEGORY_LEVEL")
    private Integer categoryLevel;

    /**
     * 分类路径(逗号分隔ID)
     */
    @TableField("CATEGORY_PATH")
    private String categoryPath;

    /**
     * 计价方法(1-移动平均法,2-先进先出法,3-加权平均法,4-个别计价法)
     */
    @TableField("PRICING_METHOD")
    private Integer pricingMethod;

    /**
     * 默认计量单位ID
     */
    @TableField("DEFAULT_UNIT_ID")
    private Long defaultUnitId;

    /**
     * 默认仓库ID
     */
    @TableField("DEFAULT_WAREHOUSE_ID")
    private Long defaultWarehouseId;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 状态(0-禁用,1-启用)
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private Long tenantId;

    /**
     * 创建人ID
     */
    @TableField("CREATOR_ID")
    private String creatorId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATER_ID")
    private String updaterId;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 删除标识(0-未删除,1-已删除)
     */
    @TableField("DELETED")
    private Integer deleted;

    /**
     * 子分类列表(非数据库字段)
     */
    @TableField(exist = false)
    private List<InventoryCategoryEntity> children;

    /**
     * 存货数量(非数据库字段)
     */
    @TableField(exist = false)
    private Integer inventoryCount;
}

