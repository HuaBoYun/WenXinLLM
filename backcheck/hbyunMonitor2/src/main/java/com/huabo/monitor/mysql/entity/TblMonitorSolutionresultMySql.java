package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Schema(name="TblMonitorSolutionresultMySql对象")
public class TblMonitorSolutionresultMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("SOLUTIONRESULTID")
    private BigDecimal solutionresultid;

    @TableField("SAVETIME")
    private LocalDateTime savetime;

    @TableField("MEMO")
    private String memo;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("SOLUTIONID")
    private BigDecimal solutionid;

    @TableField("SOURCE")
    private BigDecimal source;


}
