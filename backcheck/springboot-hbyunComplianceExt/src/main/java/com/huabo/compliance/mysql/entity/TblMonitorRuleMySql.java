package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
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
@TableName("TBL_MONITOR_RULE")
@Schema(name="TblMonitorRuleMySql对象")
public class TblMonitorRuleMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("RULEID")
    private BigDecimal ruleid;

    @TableField("RULENAME")
    private String rulename;

    @TableField("RULEDESCRIPTION")
    private String ruledescription;

    @TableField("RULECODE")
    private String rulecode;

    @TableField("RULEPRIORITY")
    private String rulepriority;

    @TableField("RISKLEVEL")
    private String risklevel;

    @TableField("SATUS")
    private String satus;

    @TableField("REGEXP")
    private String regexp;

    @TableField("RULESQL")
    private String rulesql;

    @TableField("MEMO")
    private String memo;

    @TableField("INRULEDB")
    private String inruledb;

    @TableField("TIPS")
    private String tips;

    @TableField("ORGID")
    private BigDecimal orgid;

    @TableField("CONNECTIONSTRINGS")
    private String connectionstrings;

    @TableField("RUNSTATUS")
    private BigDecimal runstatus;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("REPORTSQL")
    private String reportsql;


//TblMonitorSolution

    @Transient
    private BigDecimal solutionid;
    @Transient
    private String solutioncode;
    @Transient
    private String solutionname;
    @Transient
    private String solutionstatus;
    @Transient
    private LocalDateTime createdate;
    @Transient
    private BigDecimal type;
    @Transient
    private String exefrequncy;
}
