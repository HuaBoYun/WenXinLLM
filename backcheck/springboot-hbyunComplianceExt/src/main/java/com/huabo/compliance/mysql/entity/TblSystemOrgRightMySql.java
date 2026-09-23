package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2022-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SYSTEM_ORG_RIGHT")
@Schema(name="TblSystemOrgRightMySql对象")
public class TblSystemOrgRightMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("RIGHTID")
    @Schema(name = "权限ID")
    private BigDecimal rightid;

    @TableField("ORGID")
    @Schema(name = "公司Id")
    private BigDecimal orgid;

    @TableField("RIGHTNAME")
    @Schema(name = "公司设置的权限名称")
    private String rightname;

    @Schema(name = "是否启用1是0否")
    @TableField("RIGHTSTATUS")
    private Integer rightstatus;


}
