package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Schema(name="TblMonitorSolutionMySql对象")
public class TblMonitorSolutionMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("SOLUTIONID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
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
