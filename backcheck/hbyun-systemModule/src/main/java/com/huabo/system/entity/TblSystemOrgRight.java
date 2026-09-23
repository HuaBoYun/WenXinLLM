package com.huabo.system.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@Schema(name="TblSystemOrgRight对象", description="")
public class TblSystemOrgRight implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("RIGHTID")
    @Schema(name="权限ID")
    private BigDecimal rightid;

    @TableField("ORGID")
    @Schema(name="公司Id")
    private BigDecimal orgid;

    @TableField("RIGHTNAME")
    @Schema(name="公司设置的权限名称")
    private String rightname;

    @Schema(name="是否启用1是0否")
    @TableField("RIGHTSTATUS")
    private Integer rightstatus;


}
