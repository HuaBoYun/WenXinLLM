package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 智慧首页-业务领域统计实体
 *
 * @author cybermonitor
 * @date 2026-05-28
 */
@Data
@TableName("TBL_GZCT_SMART_HOME_DOMAIN_STAT")
public class GzctSmartHomeDomainStat implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_UUID)
    @TableField("ID")
    private String id;

    /** 类型名称 */
    @TableField("TYPE_NAME")
    private String typeName;

    /** 数量 */
    @TableField("COUNT_NUM")
    private Integer countNum;

    /** 占比 */
    @TableField("RATIO")
    private Double ratio;

    /** 颜色 */
    @TableField("COLOR")
    private String color;

    /** 排序号 */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private Date updateTime;
}
