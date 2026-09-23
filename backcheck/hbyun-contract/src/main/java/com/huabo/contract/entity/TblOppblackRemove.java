package com.huabo.contract.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 相对方移出黑名单记录表
 * </p>
 *
 * @author lhp
 * @since 2025-02-26
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_OPPBLACK_REMOVE")
@Schema(name="TblOppblackRemove对象", description="相对方移出黑名单记录表")
public class TblOppblackRemove implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("RBID")
      private String rbid;

      @Schema(name = "相对方主键")
      @TableField("OPPOID")
    private BigDecimal oppoid;

      @Schema(name = "黑名单记录表主键")
      @TableField("BRID")
    private String brid;

      @Schema(name = "移除原因")
      @TableField("REMREASON")
    private String remreason;

      @Schema(name = "移除黑名单审批状态")
      @TableField("RMSTATUS")
    private Integer rmstatus;

      @Schema(name = "创建时间")
      @TableField("CREATETIME")
    private Date createtime;

      @Schema(name = "创建人")
      @TableField("CREATESTAFF")
    private BigDecimal createstaff;

      @Schema(name = "修改时间")
      @TableField("MODIFYTIME")
    private Date modifytime;

      @Schema(name = "修改人")
      @TableField("MODIFYSTAFF")
    private BigDecimal modifystaff;

      @Schema(name = "数据所属部门")
      @TableField("LINKDEPTID")
    private BigDecimal linkdeptid;

      @Schema(name = "数据所属公司")
      @TableField("LINKORGID")
    private BigDecimal linkorgid;


}
