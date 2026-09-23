package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_MONITOR_INDICATORRESULT")
@Schema(name="TblMonitorIndicatorresult对象", description="")
public class TblMonitorIndicatorresult implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "RESULTID",type = IdType.INPUT)
      private BigDecimal resultid;

    @TableField("SCORE")
    private Float score;

    @TableField("INDICATORID")
    private BigDecimal indicatorid;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("SOLUTIONRESULTID")
    private BigDecimal solutionresultid;

    @TableField("SAVETIME")
    private LocalDateTime savetime;

    @TableField("SOURCE")
    private BigDecimal source;

    @TableField("SIGN")
    private String sign;

    @TableField("TOLERANCE")
    private String tolerance;

    @TableField("EXECUTEID")
    private String executeid;


}
