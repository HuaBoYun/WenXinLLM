package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 工作移交记录表
 * </p>
 *
 * @author lhp
 * @since 2025-01-03
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_TRANSFER_WORK")
@Schema(name="TblTransferWork对象", description="工作移交记录表")
public class TblTransferWork implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name="主键")
      @TableId(value = "TRANSFERID",type = IdType.INPUT)
    private BigDecimal transferid;

      @Schema(name="创建人")
      @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;

      @Schema(name="创建人姓名")
      @TableField("CREATESTAFFNAME")
    private String createstaffname;

      @Schema(name="单据隶属部门")
      @TableField("LINKDEPTID")
    private BigDecimal linkdeptid;

      @Schema(name="单据隶属公司")
      @TableField("LINKORGID")
    private BigDecimal linkorgid;

      @Schema(name="移交人主键")
      @TableField("TRANSFERSTAFFID")
    private BigDecimal transferstaffid;

      @Schema(name="移交人姓名")
      @TableField("TRANSEFRSTAFFNAME")
    private String transefrstaffname;

      @Schema(name="对接人主键")
      @TableField("DOCKSTAFFID")
    private BigDecimal dockstaffid;

      @Schema(name="对接人姓名")
      @TableField("DOCKSTAFFNAME")
    private String dockstaffname;

      @Schema(name="移交时间")
      @TableField("TRANSFERTIME")
    private Date transfertime;

      @Schema(name="移交失效时间")
      @TableField("TRANSFERLOSETIME")
    private Date transferlosetime;

      @Schema(name="移交对接生效审批状态")
      @TableField("TRANENABLESTATUS")
    private Integer tranenablestatus;

      @Schema(name="移交对接失效审批状态")
      @TableField("TRANDEPRECATEDSTATUS")
    private Integer trandeprecatedstatus;

      @Schema(name="对接状态，1-启用，2-弃用 ，默认0-未生效")
      @TableField("TRANSTATUS")
    private Integer transtatus;

      @Schema(name="工作移交原因")
      @TableField("TRANENABLEREASON")
    private String tranenablereason;

    @TableField("TRANDEPRECATEDREASON")
    private String trandeprecatedreason;

      @Schema(name="移交部门数据主键")
      @TableField("TRANORGIDSTRS")
    private String tranorgidstrs;
      
      @Schema(name="移交部门数据名称")
      @TableField(exist = false)
      private String tranorgnamestrs;


}
