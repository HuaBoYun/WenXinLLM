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
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_MONITOR_SOLUTIONRESULT")
@Schema(name="TblMonitorSolutionresult对象", description="")
public class TblMonitorSolutionresult implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value="SOLUTIONRESULTID",type = IdType.INPUT)
      private BigDecimal solutionresultid;

    @TableField("SAVETIME")
    private Date savetime;

    @TableField("MEMO")
    private String memo;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("SOLUTIONID")
    private BigDecimal solutionid;

    @TableField("SOURCE")
    private BigDecimal source;


}
