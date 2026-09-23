package com.huabo.monitor.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Table;

/**
 * 权限修改名称中间表
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "TBL_ORG_RIGHT_NEW")
@Schema(name="TblOrgRightMySql")
public class TblOrgRightnewMySql implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @TableField("RIGHTID")
    private Integer rightid;
    @TableField("ORGID")
    private Integer orgid;
    @TableField("RIGHTNAME")
    private String rightname;
    @TableField("INDICATORSTATUS")
    private String indicatorstatus; // 是否启用


}