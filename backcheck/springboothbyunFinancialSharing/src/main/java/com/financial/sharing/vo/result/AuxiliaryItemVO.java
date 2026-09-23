package com.financial.sharing.vo.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 辅助核算项返回对象
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class AuxiliaryItemVO {

    /**
     * 辅助核算项ID
     */
    private Long auxiliaryId;

    /**
     * 辅助核算项编码
     */
    private String auxiliaryCode;

    /**
     * 辅助核算项名称
     */
    private String auxiliaryName;

    /**
     * 辅助核算类型
     */
    private String auxiliaryType;

    /**
     * 辅助核算类型名称
     */
    private String auxiliaryTypeName;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 上级名称
     */
    private String parentName;

    /**
     * 辅助核算项级次
     */
    private Integer auxiliaryLevel;

    /**
     * 是否末级(0否1是)
     */
    private Integer isLeaf;

    /**
     * 是否启用(0否1是)
     */
    private Integer isEnabled;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long updater;
}
