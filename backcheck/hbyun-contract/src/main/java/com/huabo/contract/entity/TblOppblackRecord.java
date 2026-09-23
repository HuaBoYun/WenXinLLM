package com.huabo.contract.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
 * 相对方加入黑名单记录表
 * </p>
 *
 * @author lhp
 * @since 2025-02-26
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_OPPBLACK_RECORD")
@Schema(name="TblOppblackRecord对象", description="相对方加入黑名单记录表")
public class TblOppblackRecord implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "黑名单记录主键")
        @TableId("BRID")
      private String brid;

      @Schema(name = "相对方主键")
      @TableField("OPPOID")
    private BigDecimal oppoid;

      @Schema(name = "版本信息")
      @TableField("VERSION")
    private Integer version;

      @Schema(name = "黑名单类型   1 短期   2长期")
      @TableField("OBRTYPE")
    private Integer obrtype;

      @Schema(name = "短期黑名单截止日期")
      @TableField("BLACKDEADTIME")
    private Date blackdeadtime;

      @Schema(name = "黑名单审批状态")
      @TableField("APRSTATUS")
    private Integer aprstatus;

      @Schema(name = "加入黑名单原因")
      @TableField("BACKREASON")
    private String backreason;

      @Schema(name = "创建时间")
      @TableField("CREATETIME")
    private Date createtime;

      @Schema(name = "创建人")
      @TableField("CREATESTAFF")
    private BigDecimal createstaff;

      @Schema(name = "数据所属部门")
      @TableField("LINKDEPTID")
    private BigDecimal linkdeptid;

      @Schema(name = "数据所属公司")
      @TableField("LINKORGID")
    private BigDecimal linkorgid;

      @Schema(name = "修改时间")
      @TableField("MODIFYTIME")
    private Date modifytime;

      @Schema(name = "修改人")
      @TableField("MODIFYSTAFF")
    private BigDecimal modifystaff;


}
