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
 * @since 2022-04-27
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_MONITOR_SOLUTION")
@Schema(name="TblMonitorSolution对象", description="")
public class TblMonitorSolution implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value="SOLUTIONID",type = IdType.INPUT)
      private BigDecimal solutionid;

    @TableField("SOLUTIONCODE")
    private String solutioncode;

    @TableField("SOLUTIONNAME")
    private String solutionname;

    @TableField("SOLUTIONSTATUS")
    private String solutionstatus;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("CREATEDATE")
    private LocalDateTime createdate;

    @TableField("MEMO")
    private String memo;

    @TableField("ORGID")
    private BigDecimal orgid;

    @TableField("RUNSTATUS")
    private BigDecimal runstatus;

    @TableField("TYPE")
    private BigDecimal type;

    @TableField("EXEFREQUNCY")
    private String exefrequncy;


}
